package com.perenc.xh.lsp.service.tcCoupon.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.ConstantType;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.entity.tcCoupon.TcCoupon;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCoupon;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplycoupon;
import com.perenc.xh.lsp.service.tcCoupon.TcCouponService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Service("tcCouponService")
@Transactional(rollbackFor = Exception.class)
public class TcCouponServiceImpl implements TcCouponService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Override
    public ReturnJsonData insert(TcCoupon tcCoupon) throws Exception {
        //根据类型判断赋值给捷顺字段
        //减免金额
        if(tcCoupon.getUseType().equals(ConstantType.COUPON_USE_TYPE_MINUSMONEY)) {
            tcCoupon.setCouponWay(ConstantType.JS_COUPON_COUPON_WAY_MINUSMONEY);
            tcCoupon.setCouponMoney(tcCoupon.getAmount());
        //减免小时
        }else if(tcCoupon.getUseType().equals(ConstantType.COUPON_USE_TYPE_MINUSHOUR)) {
            tcCoupon.setCouponWay(ConstantType.JS_COUPON_COUPON_WAY_MINUSHOUR);
            tcCoupon.setCouponMoney(tcCoupon.getDuration());
        //百分比
        }else if(tcCoupon.getUseType().equals(ConstantType.COUPON_USE_TYPE_MINPERCENT)) {
            tcCoupon.setCouponWay(ConstantType.JS_COUPON_COUPON_WAY_MINPERCENT);
            tcCoupon.setCouponMoney(tcCoupon.getAmount());
        }
        tcCoupon.setIsEnabled(1);
        tcCoupon.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcCoupon.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcCoupon);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcCoupon tcCoupon) throws Exception {
        TcCoupon returnTcCoupon = mongoComDAO.executeForObjectById(tcCoupon.getId(), TcCoupon.class);
        if(returnTcCoupon != null){
            returnTcCoupon.setName(tcCoupon.getName());
            returnTcCoupon.setType(tcCoupon.getType());
            returnTcCoupon.setUseType(tcCoupon.getUseType());
            //减免金额
            if(tcCoupon.getUseType().equals(ConstantType.COUPON_USE_TYPE_MINUSMONEY)) {
                returnTcCoupon.setCouponWay(ConstantType.JS_COUPON_COUPON_WAY_MINUSMONEY);
                returnTcCoupon.setCouponMoney(tcCoupon.getAmount());
                //减免小时
            }else if(tcCoupon.getUseType().equals(ConstantType.COUPON_USE_TYPE_MINUSHOUR)) {
                returnTcCoupon.setCouponWay(ConstantType.JS_COUPON_COUPON_WAY_MINUSHOUR);
                returnTcCoupon.setCouponMoney(tcCoupon.getDuration());
                //百分比
            }else if(tcCoupon.getUseType().equals(ConstantType.COUPON_USE_TYPE_MINPERCENT)) {
                returnTcCoupon.setCouponWay(ConstantType.JS_COUPON_COUPON_WAY_MINPERCENT);
                returnTcCoupon.setCouponMoney(tcCoupon.getAmount());
            }
            returnTcCoupon.setDesc(tcCoupon.getDesc());
            returnTcCoupon.setDuration(tcCoupon.getDuration());
            returnTcCoupon.setAmount(tcCoupon.getAmount());
            int flag = mongoComDAO.executeUpdate(returnTcCoupon);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 更改启用状态
     * @param tcCoupon
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateIsEnabled(TcCoupon tcCoupon) throws Exception {
        TcCoupon returnTcCoupon = mongoComDAO.executeForObjectById(tcCoupon.getId(), TcCoupon.class);
        if(returnTcCoupon != null){
            returnTcCoupon.setIsEnabled(tcCoupon.getIsEnabled());
            int flag = mongoComDAO.executeUpdate(returnTcCoupon);
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
        //判断是否存在子表
        for (int i = 0; i <ids.length ; i++) {
            Map<String,Object> conditioncr = new HashMap<>();
            conditioncr.put("couponId",ids[i]);
            conditioncr.put("status",1);
            TcSellerApplycoupon tcSellerApplycoupon = mongoComDAO.executeForObjectByCon(conditioncr, TcSellerApplycoupon.class);
            if(tcSellerApplycoupon != null){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"票券已申领无法删除",null);
            }
        }
        for (int i = 0; i <ids.length ; i++) {
            Map<String,Object> conditioncr = new HashMap<>();
            conditioncr.put("couponId",ids[i]);
            conditioncr.put("status",1);
            TcExtendCoupon tcExtendCoupon = mongoComDAO.executeForObjectByCon(conditioncr, TcExtendCoupon.class);
            if(tcExtendCoupon != null){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"票券已发放无法删除",null);
            }
        }
         Map<String,Object> condition=new HashMap<>();
         condition.put("ids",ids);
         condition.put("status",2);
        int flag= mongoComDAO.executeUpdateByIds(condition, TcCoupon.class);
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
        TcCoupon tcCoupon = mongoComDAO.executeForObjectById(id, TcCoupon.class);
        if (tcCoupon!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcCoupon.getId());
            condition.put("name",tcCoupon.getName());
            condition.put("type",tcCoupon.getType());
            condition.put("useType",tcCoupon.getUseType());
            condition.put("desc",tcCoupon.getDesc());
            condition.put("duration",tcCoupon.getDuration());
            condition.put("amount", ToolUtil.getIntToDouble(tcCoupon.getAmount()));
            condition.put("isEnabled",tcCoupon.getIsEnabled());
            condition.put("sdate",tcCoupon.getSdate());
            condition.put("edate",tcCoupon.getEdate());
            condition.put("status",tcCoupon.getStatus());
            condition.put("createTime",tcCoupon.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String name =  MapUtils.getString(map, "name");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //优惠券名模糊查询
        if(StringUtils.isNotEmpty(name)){
            Pattern pattern = Pattern.compile("^.*" + name + ".*$");
            Criteria criteria = Criteria.where("name").regex(pattern);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcCoupon> tcCoupons = mongoComDAO.executeForObjectList(criteriasa,TcCoupon.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcCoupon.class);
        List<Map<String,Object>> tcCouponlist = new ArrayList<>();
        for(TcCoupon tcCoupon : tcCoupons){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcCoupon.getId());
            condition.put("name",tcCoupon.getName());
            condition.put("type",tcCoupon.getType());
            condition.put("useType",tcCoupon.getUseType());
            condition.put("desc",tcCoupon.getDesc());
            condition.put("duration",tcCoupon.getDuration());
            condition.put("amount", ToolUtil.getIntToDouble(tcCoupon.getAmount()));
            condition.put("isEnabled",tcCoupon.getIsEnabled());
            condition.put("sdate",tcCoupon.getSdate());
            condition.put("edate",tcCoupon.getEdate());
            condition.put("status",tcCoupon.getStatus());
            condition.put("createTime",tcCoupon.getCreateTime());
            tcCouponlist.add(condition);
        }
        map.clear();
        map.put("list",tcCouponlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

    /**
     * 查询所有
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getAllList(Map<String, Object> map) throws Exception {
        String name =  MapUtils.getString(map, "name");
        String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //优惠券名模糊查询
        if(StringUtils.isNotEmpty(name)){
            Pattern pattern = Pattern.compile("^.*" + name + ".*$");
            Criteria criteria = Criteria.where("name").regex(pattern);
            criteriasa.add(criteria);
        }
        //类型
        if(StringUtils.isNotEmpty(type)){
            Criteria criteria = Criteria.where("type").is(Integer.valueOf(type));
            criteriasa.add(criteria);
        }
        //查询为启用状态
        Criteria criteria = Criteria.where("isEnabled").is(1);
        criteriasa.add(criteria);
        Criteria criterib = Criteria.where("status").is(1);
        criteriasa.add(criterib);
        List<TcCoupon> tcCoupons = mongoComDAO.executeForObjectList(criteriasa,TcCoupon.class,orders);
        List<Map<String,Object>> tcCouponlist = new ArrayList<>();
        for(TcCoupon tcCoupon : tcCoupons){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcCoupon.getId());
            condition.put("name",tcCoupon.getName());
            condition.put("type",tcCoupon.getType());
            condition.put("useType",tcCoupon.getUseType());
            condition.put("desc",tcCoupon.getDesc());
            condition.put("duration",tcCoupon.getDuration());
            condition.put("amount", ToolUtil.getIntToDouble(tcCoupon.getAmount()));
            condition.put("isEnabled",tcCoupon.getIsEnabled());
            condition.put("sdate",tcCoupon.getSdate());
            condition.put("edate",tcCoupon.getEdate());
            condition.put("status",tcCoupon.getStatus());
            condition.put("createTime",tcCoupon.getCreateTime());
            tcCouponlist.add(condition);
        }
        map.clear();
        map.put("list",tcCouponlist);//返回前端集合命名为list
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }



}
