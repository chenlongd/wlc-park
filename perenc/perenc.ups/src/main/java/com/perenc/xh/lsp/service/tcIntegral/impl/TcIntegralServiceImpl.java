package com.perenc.xh.lsp.service.tcIntegral.impl;

import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.ConstantType;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.tcCar.TcCar;
import com.perenc.xh.lsp.entity.tcCoupon.TcCoupon;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCoupon;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegral;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegralConvertrule;
import com.perenc.xh.lsp.service.tcIntegral.TcIntegralService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


@Service("tcIntegralService")
@Transactional(rollbackFor = Exception.class)
public class TcIntegralServiceImpl implements TcIntegralService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired
    private ExtendUserDao extendUserDao;


    @Override
    public ReturnJsonData insert(TcIntegral tcIntegral) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        //查询用户
        ExtendUser user = extendUserDao.getById(tcIntegral.getExtendId());
        if(user==null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID错误",null);
        }
        Integer userIntegral=user.getIntegral();
        Integer integral=0;
        //增加
        if(tcIntegral.getType().equals(1))  {
            integral=userIntegral+tcIntegral.getNumber();
        }
        user.setIntegral(integral);
        tcIntegral.setRemark("获得积分");
        tcIntegral.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcIntegral.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcIntegral);
        if(flag > 0){
            int flagi = extendUserDao.update(DBUtil.toUpdateParam(user, "id"));
            if(flagi>0) {
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加成功", null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
    }

    /**
     * 积分兑换
     * @param tcIntegral
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertConvert(TcIntegral tcIntegral,Map<String, Object> map) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        String carId =  MapUtils.getString(map, "carId");
        //日期转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfymd = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        String strNowTime = sdf.format(calendar.getTime());
        //当前时间
        int startNowYear = Integer.parseInt(strNowTime.substring(0, 4));
        int startNowMonth = Integer.parseInt(strNowTime.substring(5, 7));
        int startNowDay = Integer.parseInt(strNowTime.substring(8, 10));
        //查询用户
        ExtendUser user = extendUserDao.getById(tcIntegral.getExtendId());
        if(user==null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID错误",null);
        }
        Integer userIntegral=user.getIntegral();
        Integer integral=0;
        int flagc=0;
        //增加
        if(tcIntegral.getType().equals(1))  {
            integral=userIntegral+tcIntegral.getNumber();
            tcIntegral.setRemark("获得积分");
        //积分兑换
        }else if(tcIntegral.getType().equals(2)){
            if(userIntegral<tcIntegral.getNumber()) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"积分不足",null);
            }
            //添加票券
            TcIntegralConvertrule tcConvertrule = mongoComDAO.executeForObjectById(tcIntegral.getIntegralConvertruleId(), TcIntegralConvertrule.class);
            if(tcConvertrule == null){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的积分兑换ID错误",null);
            }
            //查询车辆是否已经添加过
            TcCar returnTcCar = mongoComDAO.executeForObjectById(carId, TcCar.class);
            if(returnTcCar == null){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID错误",null);
            }
            //计算积分
            if(userIntegral<tcConvertrule.getRatio()) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"积分不足",null);
            }
            TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcConvertrule.getCouponId(), TcCoupon.class);
            if(tcCoupon == null){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的票券ID错误",null);
            }
            integral = userIntegral - tcConvertrule.getRatio();
            tcIntegral.setNumber(tcConvertrule.getRatio());
            tcIntegral.setRemark("积分兑换");
            TcExtendCoupon tcExtendCoupon =new TcExtendCoupon();
            tcExtendCoupon.setType(3); //来源类型 1=商家发放；2=活动发放;3:积分兑换
            tcExtendCoupon.setCouponId(tcConvertrule.getCouponId());
            tcExtendCoupon.setCouponDuration(tcCoupon.getDuration());
            tcExtendCoupon.setCouponAmount(tcCoupon.getAmount());
            tcExtendCoupon.setExtendId(tcIntegral.getExtendId());
            tcExtendCoupon.setCarId(carId);
            tcExtendCoupon.setCarNum(returnTcCar.getCarNum());
            tcExtendCoupon.setSellerId(0);
            tcExtendCoupon.setSellerApplycouponId("");
            tcExtendCoupon.setObjectId(tcConvertrule.getId());
            //有效期
            tcExtendCoupon.setSdate(strNowTime);
            String strEdate = sdfymd.format(calendar.getTime()) + " 23:59:59"; //票券有效期当天有效
            tcExtendCoupon.setEdate(strEdate);
            tcExtendCoupon.setUseStatus(ConstantType.USE_STATUS_WAITFOR);
            tcExtendCoupon.setRemark("积分兑换");
            tcExtendCoupon.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            tcExtendCoupon.setStatus(1);
            flagc = mongoComDAO.executeInsert(tcExtendCoupon);
        }
        user.setIntegral(integral);
        tcIntegral.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcIntegral.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcIntegral);
        if(flag > 0 && flagc>0){
           extendUserDao.update(DBUtil.toUpdateParam(user, "id"));
           return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加成功", null);
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
    }

    @Override
    public ReturnJsonData update(TcIntegral tcIntegral) throws Exception {
        TcIntegral returnTcIntegral = mongoComDAO.executeForObjectById(tcIntegral.getId(), TcIntegral.class);
        if(returnTcIntegral != null){
            returnTcIntegral.setExtendId(tcIntegral.getExtendId());
            returnTcIntegral.setType(tcIntegral.getType());
            returnTcIntegral.setNumber(tcIntegral.getNumber());
            returnTcIntegral.setGoodsId(tcIntegral.getGoodsId());
            returnTcIntegral.setRemark(tcIntegral.getRemark());
            int flag = mongoComDAO.executeUpdate(returnTcIntegral);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    @Override
    public ReturnJsonData delete(String[] ids) throws Exception {
         Map<String,Object> condition=new HashMap<>();
         condition.put("ids",ids);
         condition.put("status",2);
        int flag= mongoComDAO.executeUpdateByIds(condition, TcIntegral.class);
        if (flag > 0) {
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "删除成功", null);
        } else {
            return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE, "删除失败", null);
        }
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public ReturnJsonData getById(String id) throws Exception {
        TcIntegral tcIntegral = mongoComDAO.executeForObjectById(id, TcIntegral.class);
        if (tcIntegral!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcIntegral.getId());
            condition.put("extendId",tcIntegral.getExtendId());
            ExtendUser extendUser = extendUserDao.getById(tcIntegral.getExtendId());
            if(extendUser != null){
                condition.put("extendPhone",extendUser.getPhone());
            }else {
                condition.put("extendPhone","");
            }
            condition.put("type",tcIntegral.getType());
            condition.put("number",tcIntegral.getNumber());
            condition.put("goodsId",tcIntegral.getGoodsId());
            condition.put("remark",tcIntegral.getRemark());
            condition.put("status",tcIntegral.getStatus());
            condition.put("createTime",tcIntegral.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String extendId =  MapUtils.getString(map, "extendId");
       String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //客户ID
        if(StringUtils.isNotEmpty(extendId)){
            Criteria criteria = Criteria.where("extendId").is(Integer.valueOf(extendId));
            criteriasa.add(criteria);
        }
        //所属类型
        if(StringUtils.isNotEmpty(type)){
            Criteria criteria = Criteria.where("type").is(Integer.valueOf(type));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcIntegral> tcIntegrals = mongoComDAO.executeForObjectList(criteriasa,TcIntegral.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcIntegral.class);
        List<Map<String,Object>> tcIntegrallist = new ArrayList<>();
        for(TcIntegral tcIntegral : tcIntegrals){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcIntegral.getId());
            condition.put("extendId",tcIntegral.getExtendId());
            ExtendUser extendUser = extendUserDao.getById(tcIntegral.getExtendId());
            if(extendUser != null){
                condition.put("extendPhone",extendUser.getPhone());
            }else {
                condition.put("extendPhone","");
            }
            condition.put("type",tcIntegral.getType());
            condition.put("number",tcIntegral.getNumber());
            condition.put("goodsId",tcIntegral.getGoodsId());
            condition.put("remark",tcIntegral.getRemark());
            condition.put("status",tcIntegral.getStatus());
            condition.put("createTime",tcIntegral.getCreateTime());
            tcIntegrallist.add(condition);
        }
        map.clear();
        map.put("list",tcIntegrallist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }



}
