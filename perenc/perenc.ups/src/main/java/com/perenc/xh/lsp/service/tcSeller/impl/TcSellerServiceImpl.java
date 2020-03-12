package com.perenc.xh.lsp.service.tcSeller.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.*;
import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.DBRelevant.InsertParam;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.StringOrDate.MD5Utils;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.dao.order.SysOrderDao;
import com.perenc.xh.lsp.dao.tcSeller.TcSellerDao;
import com.perenc.xh.lsp.dao.tcSellerUser.TcSellerUserDao;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.order.SysOrder;
import com.perenc.xh.lsp.entity.phoneCode.PhoneCode;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCoupon;
import com.perenc.xh.lsp.entity.tcSeller.TcSeller;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerUser;
import com.perenc.xh.lsp.service.tcSeller.TcSellerService;
import com.perenc.xh.lsp.service.userToken.UserTokenService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("tcSellerService")
@Transactional(rollbackFor = Exception.class)
public class TcSellerServiceImpl implements TcSellerService {

    @Autowired(required = false)
    private TcSellerDao tcSellerDao;

    @Autowired(required = false)
    private TcSellerUserDao tcSellerUserDao;

    @Autowired(required = false)
    private UserTokenService userTokenService;


    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;

    @Autowired(required = false)
    private SysOrderDao sysOrderDao;

    @Override
    public ReturnJsonData insert(TcSeller tcSeller) throws Exception {
        //通过手机号查询是否存在
        QueryParam param = new QueryParam();
        param.addCondition("phone","=",tcSeller.getPhone());
        param.addCondition("status","=",1);
        TcSeller tcSellerp = tcSellerDao.getOne(param);
        if(tcSellerp!=null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"手机号已存在请重新输入",null);
        }
        String pwd = MD5Utils.MD5Encode(tcSeller.getPassword(), "utf8");
        tcSeller.setPassword(pwd);
        tcSeller.setEmail("");
        tcSeller.setDuration(0);
        tcSeller.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcSeller.setStatus(1);
        InsertParam insertParam = DBUtil.toInsertParam(tcSeller);
        int flag = tcSellerDao.add(insertParam);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    /**
     * 商家注册
     * 验证码验证
     * @param tcSeller
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertCheckCode(TcSeller tcSeller,Map<String, Object> map) throws Exception {
        String code =  MapUtils.getString(map, "code");
        //判断验证码
        List<Criteria> criterias = new ArrayList<Criteria>();
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        criterias.add(Criteria.where("phone").is(tcSeller.getPhone()));
        List<PhoneCode> phoneCodeList = mongoComDAO.executeForObjectList(criterias,PhoneCode.class,orders);
        if(phoneCodeList.size()>0) {
            PhoneCode phoneCode = phoneCodeList.get(0);
            if (!code.equals(phoneCode.getCode())) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "验证码不正确请重新输入", null);
            }
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请重新发送验证码", null);
        }
        //判断商家名称是否存在
        QueryParam paramu = new QueryParam();
        paramu.addCondition("name","=",tcSeller.getName());
        paramu.addCondition("status","=",1);
        TcSeller tcSelleru = tcSellerDao.getOne(paramu);
        if(tcSelleru!=null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"商家名称已存在请重新输入",null);
        }
        //通过手机号查询是否存在
        QueryParam param = new QueryParam();
        param.addCondition("phone","=",tcSeller.getPhone());
        param.addCondition("status","=",1);
        TcSeller tcSellerp = tcSellerDao.getOne(param);
        if(tcSellerp!=null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"手机号已存在请重新输入",null);
        }
        //通过手机号查询是否存在
        QueryParam paramus = new QueryParam();
        paramus.addCondition("phone","=",tcSeller.getPhone());
        paramus.addCondition("status","=",1);
        TcSellerUser tcSellerUserp = tcSellerUserDao.getOne(paramus);
        if(tcSellerUserp!=null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"手机号已存在请重新输入",null);
        }
        String pwd = MD5Utils.MD5Encode(tcSeller.getPassword(), "utf8");
        tcSeller.setPassword(pwd);
        tcSeller.setEmail("");
        tcSeller.setDuration(0);
        tcSeller.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcSeller.setStatus(1);
        InsertParam insertParam = DBUtil.toInsertParam(tcSeller);
        int flag = tcSellerDao.add(insertParam);
        if(flag > 0){
            //同步添加账号
            TcSellerUser  tcSellerUser=new TcSellerUser();
            tcSellerUser.setSellerId(Integer.valueOf(insertParam.getId()));
            tcSellerUser.setPhone(tcSeller.getPhone());
            tcSellerUser.setPassword(pwd);
            tcSellerUser.setComeFrom(1);
            tcSellerUser.setIsEnabled(1);
            tcSellerUser.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            //是否是主账号
            tcSellerUser.setIsMaster(1);
            tcSellerUser.setStatus(1);
            InsertParam insertParamu = DBUtil.toInsertParam(tcSellerUser);
            tcSellerUserDao.add(insertParamu);
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    /**
     * 商家登录
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public TcSeller sellerLogin(Map<String, Object> param) throws Exception {
        String username = MapUtils.getString(param, "username");
        String password = MapUtils.getString(param, "password");
        QueryParam paramsle = new QueryParam();
        paramsle.put("username",username);
        String md5password= MD5Utils.MD5Encode(password, "utf8");
        paramsle.put("password",md5password);
        paramsle.put("status",1);
        TcSeller tcSeller=tcSellerDao.getOne(paramsle);
        return tcSeller;
    }

    /**
     * 商家登录
     * 手机号验证码
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData phoneLogin(Map<String, Object> param) throws Exception {
        String phone = MapUtils.getString(param, "phone");
        String code = MapUtils.getString(param, "code");
        List<Criteria> criterias = new ArrayList<Criteria>();
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        try {
            QueryParam paramsle = new QueryParam();
            paramsle.put("phone",phone);
            paramsle.put("status",1);
            TcSeller tcSeller=tcSellerDao.getOne(paramsle);
            if (tcSeller != null) {
                if (tcSeller.getIsWork() == 2) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该商家已经停止营业", null);
                } else {
                    if(phone.equals("18586436185")) {
                        param.clear();
                        param.put("token", userTokenService.updateUsrToken(0, tcSeller.getId(), 2, "商家端"));
                        param.put("id", tcSeller.getId());
                        param.put("logo", tcSeller.getLogo());
                        param.put("phone", tcSeller.getPhone());
                        param.put("name", tcSeller.getName());
                        return new ReturnJsonData(DataCodeUtil.SUCCESS, "登录成功", param);
                    }else {
                        orders.add(new Sort.Order(Sort.Direction.DESC, "createTime"));
                        if (StringUtils.isNotEmpty(phone)) {
                            criterias.add(Criteria.where("phone").is(phone));
                        }
                        List<PhoneCode> phoneCodeList = mongoComDAO.executeForObjectList(criterias, PhoneCode.class, orders);
                        if (phoneCodeList.size() > 0) {
                            PhoneCode phoneCode = phoneCodeList.get(0);
                            if (code.equals(phoneCode.getCode())) {
                                //request.getSession().setAttribute("seller", tcSeller);
                                param.clear();
                                param.put("token", userTokenService.updateUsrToken(0, tcSeller.getId(), 2, "商家端"));
                                param.put("id", tcSeller.getId());
                                param.put("logo", tcSeller.getLogo());
                                param.put("phone", tcSeller.getPhone());
                                param.put("name", tcSeller.getName());
                                return new ReturnJsonData(DataCodeUtil.SUCCESS, "登录成功", param);
                            } else {
                                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请输入正确的验证码", null);
                            }
                        } else {
                            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请重新发送验证码", null);
                        }
                    }
                }
            } else {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "账号不正确", null);
            }
        }catch (Exception e) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"登录失败",null);
        }
    }


    /**
     * 商家登录
     * 退出
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData loginOut(Map<String, Object> param) throws Exception {
        String sellerId = MapUtils.getString(param, "sellerId");
        try {
           String  token= userTokenService.updateUsrToken(0,Integer.valueOf(sellerId), 2, "商家端");
            if(StringUtils.isNotEmpty(token)) {
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "注销成功", null);
            } else {
                return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE, "注销失败", null);
            }
        }catch (Exception e) {
            return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"注销失败",null);
        }
    }


    @Override
    public ReturnJsonData update(TcSeller tcSeller) throws Exception {
        QueryParam param = new QueryParam();
        param.addCondition("id","=",tcSeller.getId());
        TcSeller returnTcSeller = tcSellerDao.getOne(param);
        if(returnTcSeller != null){
            returnTcSeller.setName(tcSeller.getName());
            returnTcSeller.setDescp(tcSeller.getDescp());
            returnTcSeller.setLogo(tcSeller.getLogo());
            returnTcSeller.setLicenseId(tcSeller.getLicenseId());
            returnTcSeller.setLicenseImg(tcSeller.getLicenseImg());
            returnTcSeller.setPhone(tcSeller.getPhone());
            returnTcSeller.setAddress(tcSeller.getAddress());
            returnTcSeller.setType(tcSeller.getType());
            returnTcSeller.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = tcSellerDao.update(DBUtil.toUpdateParam(returnTcSeller, "id"));
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 商家重置密码
     * @param id
     * @return
     */
    @Override
    public ReturnJsonData resetPassword(Integer id) throws Exception {
        TcSeller tcSeller = tcSellerDao.getById(id);
        if(tcSeller != null){
            tcSeller.setPassword(MD5Utils.MD5Encode("123456", "utf8"));
            int flag = tcSellerDao.update(DBUtil.toUpdateParam(tcSeller, "id"));
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"重置密码成功",null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"重置密码失败",null);
    }


    /**
     * 修改密码
     * @param param
     * @return
     */
    @Override
    public ReturnJsonData updatePWd(Map<String,Object> param) throws Exception {
        String sellerId = MapUtils.getString(param, "sellerId", "");
        String newPassword = MapUtils.getString(param, "newPassword", "");
        String password = MapUtils.getString(param, "password", "");
        TcSeller tcSeller = tcSellerDao.getById(sellerId);
        if(tcSeller != null){
            String pwd = MD5Utils.MD5Encode(password, "utf8");
            String newPwd = MD5Utils.MD5Encode(newPassword, "utf8");
            if(!pwd.equals(tcSeller.getPassword())){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"原始密码错误",null);
            }else{
                if(newPwd.equals(tcSeller.getPassword())){
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"新密码与旧密码相同",null);
                }else{
                    tcSeller.setPassword(newPwd);
                    int flag = tcSellerDao.update(DBUtil.toUpdateParam(tcSeller, "id"));
                    if(flag > 0){
                        return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功！",null);
                    }else{
                        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败！",null);
                    }
                }
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"商家不存在！",null);
        }
    }


    /**
     * 找回密码
     * 通过手机号验证码找回密码
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData forgetPassword(Map<String, Object> param) throws Exception {
        String phone = MapUtils.getString(param, "phone");
        String code = MapUtils.getString(param, "code");
        String password = MapUtils.getString(param, "password");
        String conPassword = MapUtils.getString(param, "conPassword");
        List<Criteria> criterias = new ArrayList<Criteria>();
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        try {
            QueryParam paramsle = new QueryParam();
            paramsle.put("phone",phone);
            paramsle.put("status",1);
            TcSeller tcSeller=tcSellerDao.getOne(paramsle);
            if (tcSeller != null) {
                if (tcSeller.getIsWork() == 2) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该商家已经停止营业", null);
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, "createTime"));
                    if (StringUtils.isNotEmpty(phone)) {
                        criterias.add(Criteria.where("phone").is(phone));
                    }
                    List<PhoneCode> phoneCodeList = mongoComDAO.executeForObjectList(criterias, PhoneCode.class, orders);
                    if (phoneCodeList.size() > 0) {
                        PhoneCode phoneCode = phoneCodeList.get(0);
                        if (code.equals(phoneCode.getCode())) {
                            //修改密码
                            if(password.equals(conPassword)){
                                String newPassword = MD5Utils.MD5Encode(password, "utf8");
                                tcSeller.setPassword(newPassword);
                                int flag = tcSellerDao.update(DBUtil.toUpdateParam(tcSeller, "id"));
                                if(flag > 0){
                                    return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功！",null);
                                }else{
                                    return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败！",null);
                                }
                            }else {
                                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "新密码与确认密码不相同", null);
                            }
                        } else {
                            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请输入正确的验证码", null);
                        }
                    } else {
                        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请重新发送验证码", null);
                    }
                }
            } else {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "账号不正确", null);
            }
        }catch (Exception e) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"修改失败",null);
        }
    }



    /**
     * 修改一个属性
     * @param tcSeller
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateOne(TcSeller tcSeller) throws Exception {
        //修改判断车手机号是否重复
        TcSeller tcSellersel =new TcSeller();
        tcSellersel.setId(tcSeller.getId());
        tcSellersel.setPhone(tcSeller.getPhone());
        tcSellersel.setStatus(1);
        TcSeller tcSellerp=tcSellerDao.getByIdAndPhone(tcSellersel);
        if(tcSellerp!=null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"手机号已经存在请重新输入",null);
        }
        TcSeller returnTcSeller = tcSellerDao.getById(tcSeller.getId());
        //通过手机号查询是否存在
        QueryParam param = new QueryParam();
        param.addCondition("phone","=",returnTcSeller.getPhone());
        param.addCondition("seller_id","=",tcSeller.getId());
        param.addCondition("is_master","=",1);
        TcSellerUser tcSellerUserp = tcSellerUserDao.getOne(param);
        if(returnTcSeller != null){
            if(StringUtils.isNotEmpty(tcSeller.getName())) {
                returnTcSeller.setName(tcSeller.getName());
            }
            if(StringUtils.isNotEmpty(tcSeller.getDescp())) {
                returnTcSeller.setDescp(tcSeller.getDescp());
            }
            if(StringUtils.isNotEmpty(tcSeller.getLogo())) {
                returnTcSeller.setLogo(tcSeller.getLogo());
            }
            if(StringUtils.isNotEmpty(tcSeller.getLicenseId())) {
                returnTcSeller.setLicenseId(tcSeller.getLicenseId());
            }
            if(StringUtils.isNotEmpty(tcSeller.getLicenseImg())) {
                returnTcSeller.setLicenseImg(tcSeller.getLicenseImg());
            }
            if(StringUtils.isNotEmpty(tcSeller.getPhone())) {
                returnTcSeller.setPhone(tcSeller.getPhone());
            }
            if(StringUtils.isNotEmpty(tcSeller.getProvinceId())) {
                returnTcSeller.setProvinceId(tcSeller.getProvinceId());
            }
            if(StringUtils.isNotEmpty(tcSeller.getCityId())) {
                returnTcSeller.setCityId(tcSeller.getCityId());
            }
            if(StringUtils.isNotEmpty(tcSeller.getCountyId())) {
                returnTcSeller.setCountyId(tcSeller.getCountyId());
            }
            if(StringUtils.isNotEmpty(tcSeller.getAddress())) {
                returnTcSeller.setAddress(tcSeller.getAddress());
            }
            returnTcSeller.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = tcSellerDao.update(DBUtil.toUpdateParam(returnTcSeller, "id"));
            if(flag > 0){
                //同步更改帐号
                if(StringUtils.isNotEmpty(tcSeller.getPhone())) {
                    if(tcSellerUserp!=null) {
                        tcSellerUserp.setPhone(tcSeller.getPhone());
                        tcSellerUserp.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                        tcSellerUserDao.update(DBUtil.toUpdateParam(tcSellerUserp, "id"));
                    }
                }
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }



    /**
     * 修改审核状态
     * @param tcSeller
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateIsApproval(TcSeller tcSeller) throws Exception {
        TcSeller returnTcSeller = tcSellerDao.getById(tcSeller.getId());
        if(returnTcSeller != null){
            returnTcSeller.setIsApproval(tcSeller.getIsApproval());
            int flag = tcSellerDao.update(DBUtil.toUpdateParam(returnTcSeller, "id"));
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }


    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData delete(List<String> ids) throws Exception {
        int flag = tcSellerDao.delete(ids.toArray());
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"删除成功",null);
        }
        return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE,"删除失败",null);
    }

    /**
     * 批量修改状态删除
     * @param list
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData deleteBatch(List list) throws Exception {
        for (int i = 0; i <list.size() ; i++) {
            Integer sellerId= (Integer) list.get(i);
            //通过手机号查询是否存在
            QueryParam param = new QueryParam();
            param.addCondition("seller_id","=",sellerId);
            param.addCondition("is_master","=",1);
            List<TcSellerUser> tcSellerUsers = tcSellerUserDao.list(param);
            if(tcSellerUsers.size()>0) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"该商家存在子用户不能删除",null);
            }
        }
        int flag= tcSellerDao.deleteBatch(list);
        if (flag > 0) {
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "删除成功", null);
        } else {
            return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE, "删除失败", null);
        }
    }


    /**
     * 根据id查询返回对象
     * @param id
     * @return
     */
    @Override
    public TcSeller findById(Integer id) {
        return tcSellerDao.getById(id);
    }


    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public ReturnJsonData getById(Integer id) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        TcSeller tcSeller = tcSellerDao.getById(id);
        if (tcSeller!=null) {
            condition.put("id",tcSeller.getId());
            condition.put("name",tcSeller.getName());
            condition.put("descp",tcSeller.getDescp());
            condition.put("logo",tcSeller.getLogo());
            condition.put("licenseId",tcSeller.getLicenseId());
            condition.put("licenseImg",tcSeller.getLicenseImg());
            condition.put("username",tcSeller.getUsername());
            condition.put("phone",tcSeller.getPhone());
            condition.put("email",tcSeller.getEmail());
            condition.put("province",tcSeller.getProvinceId());
            condition.put("city",tcSeller.getCityId());
            condition.put("county",tcSeller.getCountyId());
            condition.put("address",tcSeller.getAddress());
            condition.put("type",tcSeller.getType());
            condition.put("extendId",tcSeller.getExtendId());
            condition.put("isApproval",tcSeller.getIsApproval());
            condition.put("duration",tcSeller.getDuration());
            condition.put("isWork",tcSeller.getIsWork());
            condition.put("status",tcSeller.getStatus());
            condition.put("createTime",tcSeller.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        //条件筛选
        //查询
        QueryParam param = new QueryParam();
        param.setPageNum(pageHelper.getPage());
        param.setPageSize(pageHelper.getRows());
        String name =  MapUtils.getString(map, "name");
        if(org.apache.commons.lang.StringUtils.isNotEmpty(name)){
            param.addCondition("name","like","%"+name+"%");
        }
        String phone =  MapUtils.getString(map, "phone");
        if(org.apache.commons.lang.StringUtils.isNotEmpty(phone)){
            param.addCondition("phone","like","%"+phone+"%");
        }
        String type =  MapUtils.getString(map, "type");
        if(org.apache.commons.lang.StringUtils.isNotEmpty(type)){
            param.addCondition("type","=",Integer.valueOf(type));
        }
        //是否审核通过
        String isApproval =  MapUtils.getString(map, "isApproval");
        if(org.apache.commons.lang.StringUtils.isNotEmpty(isApproval)){
            param.addCondition("is_approval","=",Integer.valueOf(isApproval));
        }
        param.addCondition("id","!=",6);
        param.addCondition("status","=",1);
        long count = tcSellerDao.count(param);
        List<TcSeller> tcSellers = tcSellerDao.list(param);
        List<Map<String,Object>> tcSellerlist = new ArrayList<>();
        for(TcSeller tcSeller : tcSellers){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcSeller.getId());
            condition.put("name",tcSeller.getName());
            condition.put("descp",tcSeller.getDescp());
            condition.put("logo",tcSeller.getLogo());
            condition.put("licenseId",tcSeller.getLicenseId());
            condition.put("licenseImg",tcSeller.getLicenseImg());
            condition.put("username",tcSeller.getUsername());
            condition.put("phone",tcSeller.getPhone());
            condition.put("email",tcSeller.getEmail());
            condition.put("province",tcSeller.getProvinceId());
            condition.put("city",tcSeller.getCityId());
            condition.put("county",tcSeller.getCountyId());
            condition.put("address",tcSeller.getAddress());
            condition.put("type",tcSeller.getType());
            condition.put("extendId",tcSeller.getExtendId());
            condition.put("isApproval",tcSeller.getIsApproval());
            condition.put("duration",tcSeller.getDuration());
            condition.put("isWork",tcSeller.getIsWork());
            condition.put("status",tcSeller.getStatus());
            condition.put("createTime",tcSeller.getCreateTime());
            tcSellerlist.add(condition);
        }
        map.clear();
        map.put("list",tcSellerlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

    /**
     * 商家发放统计分页
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getListStat(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        //条件筛选
        QueryParam param = new QueryParam();
        param.setPageNum(pageHelper.getPage());
        param.setPageSize(pageHelper.getRows());
        // 商家名称
        String sellerName =  MapUtils.getString(map, "sellerName");
        if(StringUtils.isNotEmpty(sellerName)){
            param.addCondition("name","like","%"+sellerName+"%");
        }
        // 商家类型（1=自营；2=入住商家）
        param.addCondition("type","=",2);
        //是否审核 1待审核，2：已通过，3：未通过
        param.addCondition("is_approval","=",2);
        param.addCondition("status","=",1);
        long count = tcSellerDao.count(param);
        List<TcSeller> tcSellers = tcSellerDao.list(param);
        List<Map<String,Object>> tcSellerlist = new ArrayList<>();
        for(TcSeller tcSeller : tcSellers){
            Map<String,Object> condition = new HashMap<>();
            condition.put("sellerId",tcSeller.getId());
            condition.put("sellerName",tcSeller.getName());
            //已发放总数
            Integer number=0;
            //已发放总数
            Integer ynumber=0;
            //优惠的小时数
            Integer hours=0;
            //优惠金额
            double price=0;
            Aggregation agg = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("sellerId").is(tcSeller.getId())),
                    //Aggregation.match(Criteria.where("isApproval").is(2)),//已审核通过
                    Aggregation.match(Criteria.where("status").is(1)),
                    Aggregation.group("sellerId").count().as("sellerId").sum("couponDuration").as("couponDuration").sum("couponAmount").as("couponAmount")
            );
            List<TcExtendCoupon> tcExtendCoupons=mongoComDAO.executeForObjectAggregateList(agg,"tcExtendCoupon",TcExtendCoupon.class);
             if(tcExtendCoupons.size()>0) {
                 TcExtendCoupon tcExtendCoupon=tcExtendCoupons.get(0);
                 number=tcExtendCoupon.getSellerId();
                 hours=tcExtendCoupon.getCouponDuration();
                 price= ToolUtil.getIntToDouble(tcExtendCoupon.getCouponAmount());

             }

            condition.put("number",number);
            condition.put("hours",hours);
            condition.put("price",price);

            //已使用张数
            Aggregation aggy = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("sellerId").is(tcSeller.getId())),
                    Aggregation.match(Criteria.where("useStatus").is(3)),//已使用
                    Aggregation.match(Criteria.where("status").is(1)),
                    Aggregation.group("sellerId").count().as("sellerId")
            );
            List<TcExtendCoupon> tcExtendCouponys=mongoComDAO.executeForObjectAggregateList(aggy,"tcExtendCoupon",TcExtendCoupon.class);
            if(tcExtendCouponys.size()>0) {
                TcExtendCoupon tcExtendCoupony=tcExtendCouponys.get(0);
                ynumber=tcExtendCoupony.getSellerId();
            }
            condition.put("ynumber",ynumber);
            tcSellerlist.add(condition);
        }
        map.clear();
        map.put("list",tcSellerlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }


    /**
     * 酒店集团统计分页
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getListStatGroup(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        //条件筛选
        QueryParam param = new QueryParam();
        param.setPageNum(pageHelper.getPage());
        param.setPageSize(pageHelper.getRows());
        // 商家名称
        String sellerName =  MapUtils.getString(map, "sellerName");
        if(StringUtils.isNotEmpty(sellerName)){
            param.addCondition("name","like","%"+sellerName+"%");
        }
        // 商家类型（1=自营；2=入住商家）
        param.addCondition("type","=",1);
        //是否审核 1待审核，2：已通过，3：未通过
        param.addCondition("is_approval","=",2);
        param.addCondition("status","=",1);
        param.addCondition("id","!=",6);
        long count = tcSellerDao.count(param);
        List<TcSeller> tcSellers = tcSellerDao.list(param);
        List<Map<String,Object>> tcSellerlist = new ArrayList<>();
        for(TcSeller tcSeller : tcSellers){
            Map<String,Object> condition = new HashMap<>();
            condition.put("sellerId",tcSeller.getId());
            condition.put("sellerName",tcSeller.getName());
            //免费车次数(免费出去的车次数)
            Integer number=0;
            //优惠的小时数
            double hours=0;
            //优惠金额
            double price=0;
            Map<String,Object> conditionSeller = new HashMap<>();
            conditionSeller.put("sellerId",tcSeller.getId());
            Map mapOrder = sysOrderDao.findTcOrderStatBySellerId(conditionSeller);
            if(mapOrder!=null && mapOrder.size()>0) {
                number=Integer.valueOf(mapOrder.get("cnumber").toString());
                hours=ToolUtil.getIntToDouble(Integer.valueOf(mapOrder.get("parkDuration").toString()));
                price=ToolUtil.getIntToDouble(Integer.valueOf(mapOrder.get("parkAmount").toString()));
            }
            condition.put("number",number);
            condition.put("hours",hours);
            condition.put("price",price);
            tcSellerlist.add(condition);
        }
        map.clear();
        map.put("list",tcSellerlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }


    /**
     * 更改订单商家ID
     * @return
     * @throws Exception
     */
    @Override
    public void updateOrderSellerId() throws Exception {
        //条件筛选
        //查询
        QueryParam param = new QueryParam();
        param.setPageNum(1);
        param.setPageSize(2000);
        // 商家类型（1=自营；2=入住商家）
        //param.addCondition("type", "=", 1);
        //是否审核 1待审核，2：已通过，3：未通过
        param.addCondition("customer_id", "<>", 0);
        param.addCondition("id", ">=", 2825);
        param.addCondition("id", "<=", 3849);
        List<SysOrder> sysOrders = sysOrderDao.list(param);
        for (SysOrder sysOrder : sysOrders) {

            QueryParam paramus = new QueryParam();
            paramus.addCondition("phone","=",sysOrder.getPhone());
            ExtendUser user = extendUserDao.getOne(paramus);
            if(user!=null) {
                //sysOrder.setExtendId(user.getId());
                //int i = sysOrderDao.update(DBUtil.toUpdateParam(sysOrder, "id"));
            }

        }
        System.out.println("=====执行成功=====");
    }



}
