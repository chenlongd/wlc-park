package com.perenc.xh.lsp.service.extendUser.impl;


import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.StringOrDate.MD5Utils;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.dao.wxCustomer.WxCustomerDao;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.phoneCode.PhoneCode;
import com.perenc.xh.lsp.entity.tcCoupon.TcCoupon;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCar;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCoupon;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegral;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegralConvertrule;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegralTerm;
import com.perenc.xh.lsp.entity.wxCustomer.WmCustomer;
import com.perenc.xh.lsp.service.extendUser.ExtendUserService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/4/12 16:01
 **/
@Service("extendUserService")
@Transactional(rollbackFor = Exception.class)
public class ExtendUserServiceImpl implements ExtendUserService {

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;
    @Autowired(required = false)
    private WxCustomerDao customerDao;
    @Autowired
    private MongoComDAO mongoComDAO;


    @Override
    public ReturnJsonData insert(ExtendUser extendUser) throws Exception {
        //先判断是否存在  不存在添加
        QueryParam param = new QueryParam();
        param.addCondition("phone","=",extendUser.getPhone());
        ExtendUser user = extendUserDao.getOne(param);
        if(user == null){
            extendUser.setStatus(1);
            extendUser.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            extendUser.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = extendUserDao.add(DBUtil.toInsertParam(extendUser));
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
    }

    @Override
    public ReturnJsonData update(ExtendUser extendUser) throws Exception {
        //先根据id查询  是否存在  如果存在再进行修改
        QueryParam param = new QueryParam();
        param.addCondition("id","=",extendUser.getId());
        ExtendUser returnUser = extendUserDao.getOne(param);
        if(returnUser != null){
            //性别
            if(extendUser.getSex()>0) {
                returnUser.setSex(extendUser.getSex());
            }
            if(StringUtils.isNotEmpty(extendUser.getEmail())) {
                returnUser.setEmail(extendUser.getEmail());
            }
            //联系人
            if(StringUtils.isNotEmpty(extendUser.getContact())) {
                returnUser.setContact(extendUser.getContact());
            }
            if(StringUtils.isNotEmpty(extendUser.getProvinceId())) {
                returnUser.setProvinceId(extendUser.getProvinceId());
            }
            if(StringUtils.isNotEmpty(extendUser.getCityId())) {
                returnUser.setCityId(extendUser.getCityId());
            }
            if(StringUtils.isNotEmpty(extendUser.getCountyId())) {
                returnUser.setCountyId(extendUser.getCountyId());
            }
            if(StringUtils.isNotEmpty(extendUser.getAddress())) {
                returnUser.setAddress(extendUser.getAddress());
            }
            //头像
            if(StringUtils.isNotEmpty(extendUser.getHeadImage())) {
                returnUser.setHeadImage(extendUser.getHeadImage());
            }
            //昵称
            if(StringUtils.isNotEmpty(extendUser.getNickname())) {
                returnUser.setNickname(extendUser.getNickname());
            }
            if(StringUtils.isNotEmpty(extendUser.getUsername())) {
                returnUser.setUsername(extendUser.getUsername());
            }
            //生日
            if(StringUtils.isNotEmpty(extendUser.getBirthday())) {
                returnUser.setBirthday(extendUser.getBirthday());
            }
            if(extendUser.getDriveAge()>0) {
                returnUser.setDriveAge(extendUser.getDriveAge());
            }
            returnUser.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = extendUserDao.update(DBUtil.toUpdateParam(returnUser, "id"));
            if(flag > 0){
                //完善资料获得积分
                Map<String, Object> conditions = new HashMap<>();
                conditions.put("type", 4);
                conditions.put("isEnabled", 1); //启用状态 1:启用
                conditions.put("status", 1);
                //查询完善资料获得的积分
                TcIntegralTerm tcIntegralTerm = mongoComDAO.executeForObjectByCon(conditions, TcIntegralTerm.class);
                if(tcIntegralTerm!=null) {
                    Map<String, Object> conditionsel = new HashMap<>();
                    conditionsel.put("extendId",extendUser.getId());
                    conditionsel.put("integralTermId", tcIntegralTerm.getId());
                    conditionsel.put("status", 1);
                    //查询是否已经获得积分
                    TcIntegral tcIntegralsel = mongoComDAO.executeForObjectByCon(conditionsel, TcIntegral.class);
                    if(tcIntegralsel==null) {
                        if (returnUser.getSex() > 0 && returnUser.getDriveAge() > 0 && returnUser.getProvinceId() != null) {
                            TcIntegral tcIntegral = new TcIntegral();
                            tcIntegral.setExtendId(returnUser.getId());
                            tcIntegral.setIntegralConvertruleId("");
                            tcIntegral.setIntegralTermId(tcIntegralTerm.getId());
                            tcIntegral.setType(1);
                            tcIntegral.setNumber(tcIntegralTerm.getNumber());
                            tcIntegral.setRemark("完善资料");
                            tcIntegral.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                            tcIntegral.setStatus(1);
                            int flagit = mongoComDAO.executeInsert(tcIntegral);
                            if (flagit > 0) {
                                //更改用户积分
                                ExtendUser returnUserIn = extendUserDao.getById(extendUser.getId());
                                returnUserIn.setIntegral(returnUser.getIntegral() + tcIntegral.getNumber());
                                extendUserDao.update(DBUtil.toUpdateParam(returnUserIn, "id"));
                            }
                        }
                    }
                }
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 设置支付密码
     * @param extendUser
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updatePayPsword(ExtendUser extendUser) throws Exception {
        //先根据id查询  是否存在  如果存在再进行修改
        QueryParam param = new QueryParam();
        param.addCondition("id","=",extendUser.getId());
        ExtendUser returnUser = extendUserDao.getOne(param);
        if(returnUser != null){
            if(returnUser.getPayPsword()!=null && returnUser.getPayPsword().length()>0) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"支付密码已经设置",null);
            }
            String payPwword = MD5Utils.MD5Encode(extendUser.getPayPsword(), "utf8");
            returnUser.setPayPsword(payPwword);
            returnUser.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = extendUserDao.update(DBUtil.toUpdateParam(returnUser, "id"));
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"设置成功",null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"设置失败",null);
    }

    /**
     * 找回支付密码
     * @param extendUser
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData forgetPayPsword(ExtendUser extendUser,Map<String, Object> map) throws Exception {
        //先查询手机验证码是否正确
        String phone =  MapUtils.getString(map, "phone");
        String code =  MapUtils.getString(map, "code");
        String confmpayPsword =  MapUtils.getString(map, "confmpayPsword");
        //判断两次输入的密码是否一致
        if(!extendUser.getPayPsword().equals(confmpayPsword)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"新密码与确认密码不相同",null);
        }
        List<Criteria> criterias = new ArrayList<Criteria>();
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        if(StringUtils.isNotEmpty(phone)){
            criterias.add(Criteria.where("phone").is(phone));
        }
        List<PhoneCode> phoneCodeList = mongoComDAO.executeForObjectList(criterias, PhoneCode.class,orders);
        if(phoneCodeList.size()>0) {
            PhoneCode phoneCode=phoneCodeList.get(0);
            if (code.equals(phoneCode.getCode())) {
                //先根据id查询  是否存在  如果存在再进行修改
                QueryParam param = new QueryParam();
                param.addCondition("id","=",extendUser.getId());
                ExtendUser returnUser = extendUserDao.getOne(param);
                if(returnUser != null){
                    String payPwword = MD5Utils.MD5Encode(extendUser.getPayPsword(), "utf8");
                    returnUser.setPayPsword(payPwword);
                    returnUser.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    int flag = extendUserDao.update(DBUtil.toUpdateParam(returnUser, "id"));
                    if(flag > 0){
                        return new ReturnJsonData(DataCodeUtil.SUCCESS,"设置成功",null);
                    }
                }
            }else {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的验证码",null);
            }
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请重新发送验证码",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"设置失败",null);
    }

    @Override
    public ReturnJsonData delete(List<String> ids) throws Exception {
        //删除扩展用户
        int flag = extendUserDao.delete(ids.toArray());
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"删除成功",null);
        }
        return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE,"删除失败",null);
    }

    /**
     * 获取详情
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getById(Integer id) throws Exception {
        ExtendUser user = extendUserDao.getById(id);
        Map<String,Object> condition = new HashMap<>();
        if(user != null){
            condition.put("id",user.getId());
            condition.put("appId",user.getAppId());
            condition.put("appSecrert",user.getAppSecrert());
            int customerId = user.getCustomerId();
            WmCustomer customer = customerDao.getById(customerId);
            if(customer != null){
                condition.put("customerId",customer.getId());
                condition.put("nickName",customer.getNickName());
            }else{
                condition.put("customerId","");
                condition.put("nickName","");
            }
            condition.put("comeFrom", user.getComeFrom());
            condition.put("phone",user.getPhone());
            //是否设置支付密码:1:是，2，否
            Integer isPayPsword=2;
            if(user.getPayPsword()!=null && user.getPayPsword().length()>0) {
                isPayPsword=1;
            }
            condition.put("isPayPsword",isPayPsword);
            condition.put("integral",user.getIntegral());
            condition.put("balance", ToolUtil.getIntToDouble(user.getBalance()));
            condition.put("email",user.getEmail());
            condition.put("contact",user.getContact());
            //区域查询
            condition.put("provinceId",user.getProvinceId());
            condition.put("cityId",user.getCityId());
            condition.put("countyId",user.getCountyId());
            condition.put("address",user.getAddress());
            condition.put("headImage",user.getHeadImage());
            condition.put("sex", user.getSex());
            condition.put("nickname",user.getNickname());
            condition.put("username",user.getUsername());
            condition.put("birthday",user.getBirthday());
            //司机ID  后面补上
            condition.put("driveId","后面进行修改");
            condition.put("driveImg",user.getDriveImg());
            condition.put("driveAge",user.getDriveAge());
            condition.put("area",user.getArea());
            //驾驶证ID
            condition.put("travelId","后面进行修改");
            condition.put("status",user.getStatus());
            condition.put("createTime",user.getBirthday());
            //查询停车券张数，先查询车辆
            List<Criteria> criteriasca = new ArrayList<Criteria>();
            //用户ID
            Criteria criteriac = Criteria.where("extendId").is(id);
            criteriasca.add(criteriac);
            Criteria criteriab = Criteria.where("status").is(1);
            criteriasca.add(criteriab);
            List<TcExtendCar> tcExtendCars = mongoComDAO.executeForObjectList(criteriasca,TcExtendCar.class);
           //查询
            List<String> listCarIds=new ArrayList<>();
            for(TcExtendCar tcExtendCar : tcExtendCars){
                listCarIds.add(tcExtendCar.getCarId());
            }
            //停车券张数
            long couponNumber=0;
            List<Criteria> criteriasa = new ArrayList<Criteria>();
            //车辆ID
            if(listCarIds.size()>0) {
                Criteria criteria = Criteria.where("carId").in(listCarIds);
                criteriasa.add(criteria);
                //待使用
                Criteria criterib = Criteria.where("useStatus").is(1);
                criteriasa.add(criterib);
                Criteria criteric = Criteria.where("status").is(1);
                criteriasa.add(criteric);
                couponNumber = mongoComDAO.executeForObjectListCount(criteriasa, TcExtendCoupon.class);
            }
            condition.put("couponNumber",couponNumber);

            return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
        }else{
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }

    /**
     * 获取用户中心数据
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getUserCenter(Integer id) throws Exception {
        ExtendUser user = extendUserDao.getById(id);
        Map<String,Object> condition = new HashMap<>();
        if(user != null){
            condition.put("id",user.getId());
            condition.put("phone",user.getPhone());
            condition.put("integral",user.getIntegral());
            condition.put("balance",ToolUtil.getIntToDouble(user.getBalance()));
            //停车券张数
            long couponNumber=0;
            List<Criteria> criteriasa = new ArrayList<Criteria>();
            //用户ID
            Criteria criteria = Criteria.where("extendId").is(id);
            criteriasa.add(criteria);
            Criteria criterib = Criteria.where("status").is(1);
            criteriasa.add(criterib);
            couponNumber = mongoComDAO.executeForObjectListCount(criteriasa, TcExtendCoupon.class);
            condition.put("couponNumber",couponNumber);
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
        }else{
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }

    /**
     * 获取用户积分
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getUserIntegral(Integer id) throws Exception {
        ExtendUser user = extendUserDao.getById(id);
        Map<String,Object> condition = new HashMap<>();
        if(user != null){
            condition.put("id",user.getId());
            condition.put("phone",user.getPhone());
            condition.put("integral",user.getIntegral());
            condition.put("balance",ToolUtil.getIntToDouble(user.getBalance()));
            //查询
            List<Sort.Order> orders = new ArrayList<Sort.Order>();
            orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
            List<Criteria> criteriasa = new ArrayList<Criteria>();
            Criteria criterib = Criteria.where("status").is(1);
            criteriasa.add(criterib);
            //是否启用(1：是,2否 )
            Criteria criteric = Criteria.where("isEnabled").is(1);
            criteriasa.add(criteric);
            //查询积分项
            List<TcIntegralTerm> tcIntegralTerms = mongoComDAO.executeForObjectList(criteriasa,TcIntegralTerm.class,orders);
             List<Map<String,Object>> tcIntegralTrermlist = new ArrayList<>();
            for(TcIntegralTerm tcIntegralTerm : tcIntegralTerms){
                Map<String,Object> conditionit = new HashMap<>();
                conditionit.put("id",tcIntegralTerm.getId());
                conditionit.put("name",tcIntegralTerm.getName());
                conditionit.put("type",tcIntegralTerm.getType());
                conditionit.put("number",tcIntegralTerm.getNumber());
                conditionit.put("url",tcIntegralTerm.getUrl());
                conditionit.put("remark",tcIntegralTerm.getRemark());
                //判断是否完成状态
                //是否完成 （1：是，2：否）
                int isFinish=2;
                //绑定车   //认证车辆 //完善资料
                //查询人员是否已经绑定过车辆
                Map<String, Object> conditionsel = new HashMap<>();
                conditionsel.put("extendId",user.getId());
                conditionsel.put("integralTermId", tcIntegralTerm.getId());
                conditionsel.put("status", 1);
                //查询是否已经获得积分
                TcIntegral tcIntegralsel = mongoComDAO.executeForObjectByCon(conditionsel, TcIntegral.class);
                if(tcIntegralsel!=null){
                    isFinish=1;
                }
                conditionit.put("isFinish",isFinish);
                conditionit.put("status",tcIntegralTerm.getStatus());
                conditionit.put("createTime",tcIntegralTerm.getCreateTime());
                tcIntegralTrermlist.add(conditionit);
            }
            condition.put("integraltrList",tcIntegralTrermlist);//返回前端集合命名为list
            //积分兑换规则列表
            List<Criteria> criteriasacv = new ArrayList<Criteria>();
            Criteria criteriacv = Criteria.where("isEnabled").is(1);
            criteriasacv.add(criteriacv);
            Criteria criteribcv = Criteria.where("status").is(1);
            criteriasacv.add(criteribcv);
            List<TcIntegralConvertrule> tcIntegralConvertrules = mongoComDAO.executeForObjectList(criteriasacv,TcIntegralConvertrule.class,orders);
            List<Map<String,Object>> tciConvertrulelist = new ArrayList<>();
            for(TcIntegralConvertrule tciConvertrule : tcIntegralConvertrules){
                Map<String,Object> conditioncv = new HashMap<>();
                conditioncv.put("id",tciConvertrule.getId());
                conditioncv.put("couponId",tciConvertrule.getCouponId());
                TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tciConvertrule.getCouponId(), TcCoupon.class);
                if(tcCoupon != null){
                    conditioncv.put("couponName",tcCoupon.getName());
                    conditioncv.put("couponDesc",tcCoupon.getDesc());
                    conditioncv.put("couponDuration",tcCoupon.getDuration());
                    conditioncv.put("couponamount",tcCoupon.getAmount());
                }else {
                    conditioncv.put("couponName","");
                    conditioncv.put("couponDesc","");
                    conditioncv.put("couponDuration","");
                    conditioncv.put("couponamount","");
                }
                conditioncv.put("name",tciConvertrule.getName());
                conditioncv.put("ratio",tciConvertrule.getRatio());
                conditioncv.put("formula",tciConvertrule.getFormula());
                conditioncv.put("number",tciConvertrule.getNumber());
                conditioncv.put("limitNumber",tciConvertrule.getLimitNumber());
                conditioncv.put("sdate",tciConvertrule.getSdate());
                conditioncv.put("edate",tciConvertrule.getEdate());
                conditioncv.put("remark",tciConvertrule.getRemark());
                conditioncv.put("status",tciConvertrule.getStatus());
                conditioncv.put("createTime",tciConvertrule.getCreateTime());
                tciConvertrulelist.add(conditioncv);
            }
            condition.put("integralcvList",tciConvertrulelist);//返回前端集合命名为list
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
        }else{
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getExtendUserList(Map<String,Object> condition, PageHelper pageHelper) {
        //条件筛选
        //查询
        QueryParam param = new QueryParam();
        param.setPageNum(pageHelper.getPage());
        param.setPageSize(pageHelper.getRows());
        String phone =  MapUtils.getString(condition, "phone");
        if(StringUtils.isNotEmpty(phone)){
            param.addCondition("phone","like","%"+phone+"%");
        }
        int count = extendUserDao.count(param);
        List<ExtendUser> users = extendUserDao.list(param);
        List<Map<String,Object>> extendsUserList = new ArrayList<>();
        for(ExtendUser user : users){
            Map<String,Object> map = new HashMap<>();
            map.put("id",user.getId());
            map.put("appId",user.getAppId());
            map.put("appSecrert",user.getAppSecrert());
            int customerId = user.getCustomerId();
            WmCustomer customer = customerDao.getById(customerId);
            if(customer != null){
                map.put("customerId",customer.getId());
                map.put("nickName",customer.getNickName());
            }else{
                map.put("customerId","");
                map.put("nickName","");
            }
            map.put("comeFrom", user.getComeFrom());
            map.put("phone",user.getPhone());
            map.put("integral",user.getIntegral());
            map.put("balance",ToolUtil.getIntToDouble(user.getBalance()));
            map.put("email",user.getEmail());
            map.put("contact",user.getContact());
            //区域查询
            map.put("provinceId",user.getProvinceId());
            map.put("cityId",user.getCityId());
            map.put("countyId",user.getCountyId());
            map.put("address",user.getAddress());
            map.put("headImage",user.getHeadImage());
            map.put("sex", user.getSex());
            map.put("nickname",user.getNickname());
            map.put("username",user.getUsername());
            map.put("birthday",user.getBirthday());
            //司机ID  后面补上
            map.put("driveId","后面进行修改");
            map.put("driveImg",user.getDriveImg());
            map.put("driveAge",user.getDriveAge());
            map.put("area",user.getArea());
            //驾驶证ID
            map.put("travelId","后面进行修改");
            map.put("status",user.getStatus());
            map.put("createTime",user.getBirthday());
            extendsUserList.add(map);
        }
        condition.clear();
        condition.put("list",extendsUserList);//返回前端集合命名为list
        condition.put("total",count);//总条数
        condition.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
    }

}
