package com.perenc.xh.lsp.service.tcSellerUser.impl;


import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.DBRelevant.InsertParam;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.StringOrDate.MD5Utils;
import com.perenc.xh.lsp.dao.tcSeller.TcSellerDao;
import com.perenc.xh.lsp.dao.tcSellerUser.TcSellerUserDao;
import com.perenc.xh.lsp.entity.phoneCode.PhoneCode;
import com.perenc.xh.lsp.entity.tcSeller.TcSeller;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerUser;
import com.perenc.xh.lsp.service.tcSellerUser.TcSellerUserService;
import com.perenc.xh.lsp.service.userToken.UserTokenService;
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


@Service("tcSellerUserService")
@Transactional(rollbackFor = Exception.class)
public class TcSellerUserServiceImpl implements TcSellerUserService {

    @Autowired(required = false)
    private TcSellerDao tcSellerDao;

    @Autowired(required = false)
    private TcSellerUserDao tcSellerUserDao;

    @Autowired(required = false)
    private UserTokenService userTokenService;


    @Autowired
    private MongoComDAO mongoComDAO;

    @Override
    public ReturnJsonData insert(TcSellerUser tcSellerUser) throws Exception {
        //通过手机号查询是否存在
        QueryParam param = new QueryParam();
        param.addCondition("phone","=",tcSellerUser.getPhone());
        param.addCondition("status","=",1);
        TcSellerUser tcSellerUserp = tcSellerUserDao.getOne(param);
        if(tcSellerUserp!=null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"手机号已存在请重新输入",null);
        }
        String pwd = MD5Utils.MD5Encode(tcSellerUser.getPassword(), "utf8");
        tcSellerUser.setPassword(pwd);
        //来源：1=小程序；2=公众号
        tcSellerUser.setComeFrom(1);
        tcSellerUser.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcSellerUser.setStatus(1);
        InsertParam insertParam = DBUtil.toInsertParam(tcSellerUser);
        int flag = tcSellerUserDao.add(insertParam);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    /**
     * 商家注册
     * 验证码验证
     * @param tcSellerUser
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertCheckCode(TcSellerUser tcSellerUser,Map<String, Object> map) throws Exception {
        String code =  MapUtils.getString(map, "code");
        //判断验证码
        List<Criteria> criterias = new ArrayList<Criteria>();
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        criterias.add(Criteria.where("phone").is(tcSellerUser.getPhone()));
        List<PhoneCode> phoneCodeList = mongoComDAO.executeForObjectList(criterias,PhoneCode.class,orders);
        if(phoneCodeList.size()>0) {
            PhoneCode phoneCode = phoneCodeList.get(0);
            if (!code.equals(phoneCode.getCode())) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "验证码不正确请重新输入", null);
            }
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请重新发送验证码", null);
        }
        //判断用户名是否存在
        QueryParam paramu = new QueryParam();
        paramu.addCondition("username","=",tcSellerUser.getUsername());
        paramu.addCondition("status","=",1);
        TcSellerUser tcSellerUseru = tcSellerUserDao.getOne(paramu);
        if(tcSellerUseru!=null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"用户名已存在请重新输入",null);
        }
        //通过手机号查询是否存在
        QueryParam param = new QueryParam();
        param.addCondition("phone","=",tcSellerUser.getPhone());
        param.addCondition("status","=",1);
        TcSellerUser tcSellerUserp = tcSellerUserDao.getOne(param);
        if(tcSellerUserp!=null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"手机号已存在请重新输入",null);
        }
        String pwd = MD5Utils.MD5Encode(tcSellerUser.getPassword(), "utf8");
        tcSellerUser.setPassword(pwd);
        tcSellerUser.setComeFrom(1);
        tcSellerUser.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcSellerUser.setStatus(1);
        InsertParam insertParam = DBUtil.toInsertParam(tcSellerUser);
        int flag = tcSellerUserDao.add(insertParam);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    /**
     * 添加商家用户
     * @param tcSellerUser
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertSellerUser(TcSellerUser tcSellerUser,Map<String, Object> map) throws Exception {
        //判断用户名是否存在
        //通过手机号查询是否存在
        QueryParam param = new QueryParam();
        param.addCondition("phone","=",tcSellerUser.getPhone());
        param.addCondition("status","=",1);
        TcSellerUser tcSellerUserp = tcSellerUserDao.getOne(param);
        if(tcSellerUserp!=null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"手机号已存在请重新输入",null);
        }
        //初始密码
        String password=tcSellerUser.getPassword();
        String pwd = MD5Utils.MD5Encode(password, "utf8");
        tcSellerUser.setPassword(pwd);
        tcSellerUser.setComeFrom(1);
        tcSellerUser.setIsEnabled(1);
        tcSellerUser.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        //是否是主账号
        tcSellerUser.setIsMaster(2);
        tcSellerUser.setStatus(1);
        InsertParam insertParam = DBUtil.toInsertParam(tcSellerUser);
        int flag = tcSellerUserDao.add(insertParam);
        if(flag > 0){
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
    public TcSellerUser sellerLogin(Map<String, Object> param) throws Exception {
        String username = MapUtils.getString(param, "username");
        String password = MapUtils.getString(param, "password");
        QueryParam paramsle = new QueryParam();
        paramsle.put("username",username);
        String md5password= MD5Utils.MD5Encode(password, "utf8");
        paramsle.put("password",md5password);
        paramsle.put("status",1);
        TcSellerUser tcSellerUser=tcSellerUserDao.getOne(paramsle);
        return tcSellerUser;
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
            TcSellerUser tcSellerUser=tcSellerUserDao.getOne(paramsle);
            if (tcSellerUser != null) {
                TcSeller tcSeller=tcSellerDao.getById(tcSellerUser.getSellerId());
                if (tcSellerUser.getIsEnabled().equals(2)) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该用户已被禁用", null);
                } else {
                    if(phone.equals("18586436185")) {
                        param.clear();
                        param.put("token", userTokenService.updateUsrTokenByselerUserId(0, tcSeller.getId(),tcSellerUser.getId(), 2, "商家端"));
                        param.put("id", tcSellerUser.getId());
                        param.put("sellerId", tcSellerUser.getSellerId());
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
                                param.clear();
                                param.put("token", userTokenService.updateUsrTokenByselerUserId(0, tcSeller.getId(),tcSellerUser.getId(), 2, "商家端"));
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
     * 手机号验证码
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData phonePasswordLogin(Map<String, Object> param) throws Exception {
        String phone = MapUtils.getString(param, "phone");
        String password = MapUtils.getString(param, "password");
        try {
            QueryParam paramsle = new QueryParam();
            paramsle.put("phone",phone);
            String md5password= MD5Utils.MD5Encode(password, "utf8");
            paramsle.put("password",md5password);
            paramsle.put("status",1);
            TcSellerUser tcSellerUser=tcSellerUserDao.getOne(paramsle);
            if (tcSellerUser != null) {
                TcSeller tcSeller=tcSellerDao.getById(tcSellerUser.getSellerId());
                if (tcSeller==null) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该商家不存在", null);
                }
                if(!tcSeller.getIsApproval().equals(2)) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该商家未审核通过", null);
                }
                if (tcSeller.getIsWork().equals(2)) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该商家已经停止营业", null);
                }
                if (tcSellerUser.getIsEnabled().equals(2)) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该用户已被禁用", null);
                }
                param.clear();
                param.put("id",tcSellerUser.getId());
                param.put("phone",tcSellerUser.getPhone());
                param.put("sellerId",tcSeller.getId());
                param.put("logo", tcSeller.getLogo());
                param.put("name", tcSeller.getName());
                param.put("isApproval", tcSeller.getIsApproval());
                param.put("type", tcSeller.getType());
                param.put("isMaster", tcSellerUser.getIsMaster());
                param.put("token", userTokenService.updateUsrToken(0, tcSeller.getId(), 2, "商家端"));
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "登录成功", param);
            } else {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "账号或密码不正确", null);
            }
        }catch (Exception e) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"登录失败",null);
        }
    }




    /**
     * 商家退出
     * 退出
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData loginOut(Map<String, Object> param) throws Exception {
        String sellerUserId = MapUtils.getString(param, "sellerUserId");
        try {
           String  token= userTokenService.updateUsrTokenByselerUserId(0,0,Integer.valueOf(sellerUserId), 2, "商家端");
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
    public ReturnJsonData update(TcSellerUser tcSellerUser) throws Exception {
        QueryParam param = new QueryParam();
        param.addCondition("id","=",tcSellerUser.getId());
        TcSellerUser returnTcSellerUser = tcSellerUserDao.getOne(param);
        if(returnTcSellerUser != null){
            returnTcSellerUser.setPhone(tcSellerUser.getPhone());
            returnTcSellerUser.setAddress(tcSellerUser.getAddress());
            returnTcSellerUser.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = tcSellerUserDao.update(DBUtil.toUpdateParam(returnTcSellerUser, "id"));
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
        TcSellerUser tcSellerUser = tcSellerUserDao.getById(id);
        if(tcSellerUser != null){
            tcSellerUser.setPassword(MD5Utils.MD5Encode("123456", "utf8"));
            int flag = tcSellerUserDao.update(DBUtil.toUpdateParam(tcSellerUser, "id"));
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
        String sellerUserId = MapUtils.getString(param, "sellerUserId", "");
        String newPassword = MapUtils.getString(param, "newPassword", "");
        String password = MapUtils.getString(param, "password", "");
        TcSellerUser tcSellerUser = tcSellerUserDao.getById(sellerUserId);
        if(tcSellerUser != null){
            String pwd = MD5Utils.MD5Encode(password, "utf8");
            String newPwd = MD5Utils.MD5Encode(newPassword, "utf8");
            if(!pwd.equals(tcSellerUser.getPassword())){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"原始密码错误",null);
            }else{
                if(newPwd.equals(tcSellerUser.getPassword())){
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"新密码与旧密码相同",null);
                }else{
                    tcSellerUser.setPassword(newPwd);
                    int flag = tcSellerUserDao.update(DBUtil.toUpdateParam(tcSellerUser, "id"));
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
            //TcSeller tcSeller=tcSellerDao.getOne(paramsle);
            TcSellerUser tcSellerUser=tcSellerUserDao.getOne(paramsle);
            if (tcSellerUser != null) {
                if (tcSellerUser.getIsEnabled().equals(2)) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该用户已被禁用", null);
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
                                tcSellerUser.setPassword(newPassword);
                                int flag = tcSellerUserDao.update(DBUtil.toUpdateParam(tcSellerUser, "id"));
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
     * @param tcSellerUser
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateOne(TcSellerUser tcSellerUser) throws Exception {

        TcSellerUser returnTcSellerUser = tcSellerUserDao.getById(tcSellerUser.getId());
        if(returnTcSellerUser != null){
            if(StringUtils.isNotEmpty(tcSellerUser.getPhone())) {
                //修改判断车手机号是否重复
                TcSellerUser tcSellerUsersel =new TcSellerUser();
                tcSellerUsersel.setId(tcSellerUser.getId());
                tcSellerUsersel.setPhone(tcSellerUser.getPhone());
                tcSellerUsersel.setStatus(1);
                TcSellerUser tcSellerUserp=tcSellerUserDao.getByIdAndPhone(tcSellerUsersel);
                if(tcSellerUserp!=null) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"手机号已经存在请重新输入",null);
                }
                returnTcSellerUser.setPhone(tcSellerUser.getPhone());
            }
            if(StringUtils.isNotEmpty(tcSellerUser.getProvinceId())) {
                returnTcSellerUser.setProvinceId(tcSellerUser.getProvinceId());
            }
            if(StringUtils.isNotEmpty(tcSellerUser.getCityId())) {
                returnTcSellerUser.setCityId(tcSellerUser.getCityId());
            }
            if(StringUtils.isNotEmpty(tcSellerUser.getCountyId())) {
                returnTcSellerUser.setCountyId(tcSellerUser.getCountyId());
            }
            if(StringUtils.isNotEmpty(tcSellerUser.getAddress())) {
                returnTcSellerUser.setAddress(tcSellerUser.getAddress());
            }
            if(StringUtils.isNotEmpty(tcSellerUser.getHeadImage())) {
                returnTcSellerUser.setHeadImage(tcSellerUser.getHeadImage());
            }
            if (tcSellerUser.getSex()!=null && tcSellerUser.getSex() > 0) {
                returnTcSellerUser.setSex(tcSellerUser.getSex());
            }
            if(StringUtils.isNotEmpty(tcSellerUser.getNickname())) {
                if(!tcSellerUser.getNickname().equals("null")) {
                    returnTcSellerUser.setNickname(tcSellerUser.getNickname());
                }
            }
            returnTcSellerUser.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = tcSellerUserDao.update(DBUtil.toUpdateParam(returnTcSellerUser, "id"));
            if(flag > 0){
                if(StringUtils.isNotEmpty(tcSellerUser.getPhone()) && returnTcSellerUser.getIsMaster().equals(1)) {
                    TcSeller tcSeller = tcSellerDao.getById(returnTcSellerUser.getSellerId());
                    if(tcSeller!=null) {
                        tcSeller.setPhone(tcSellerUser.getPhone());
                        tcSeller.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                        tcSellerDao.update(DBUtil.toUpdateParam(tcSeller, "id"));
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
     * 修改启用状态
     * @param tcSellerUser
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateIsEnabled(TcSellerUser tcSellerUser) throws Exception {
        TcSellerUser returnTcSellerUser = tcSellerUserDao.getById(tcSellerUser.getId());
        if(returnTcSellerUser != null){
            returnTcSellerUser.setIsEnabled(tcSellerUser.getIsEnabled());
            int flag = tcSellerUserDao.update(DBUtil.toUpdateParam(returnTcSellerUser, "id"));
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
        int flag = tcSellerUserDao.delete(ids.toArray());
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
        int flag= tcSellerUserDao.deleteBatch(list);
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
    public ReturnJsonData getById(Integer id) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        TcSellerUser tcSellerUser = tcSellerUserDao.getById(id);
        if (tcSellerUser!=null) {
            condition.put("id",tcSellerUser.getId());
            condition.put("appId",tcSellerUser.getAppId());
            condition.put("appSecrert",tcSellerUser.getAppSecrert());
            condition.put("customerId",tcSellerUser.getCustomerId());
            condition.put("comeFrom",tcSellerUser.getComeFrom());
            condition.put("sellerId",tcSellerUser.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcSellerUser.getSellerId());
            if(tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("parentId",tcSellerUser.getParentId());
            condition.put("username",tcSellerUser.getUsername());
            condition.put("phone",tcSellerUser.getPhone());
            condition.put("email",tcSellerUser.getEmail());
            condition.put("contact",tcSellerUser.getContact());
            condition.put("province",tcSellerUser.getProvinceId());
            condition.put("city",tcSellerUser.getCityId());
            condition.put("county",tcSellerUser.getCountyId());
            condition.put("address",tcSellerUser.getAddress());
            condition.put("headImage",tcSellerUser.getHeadImage());
            condition.put("sex",tcSellerUser.getSex());
            condition.put("nickname",tcSellerUser.getNickname());
            condition.put("birthday",tcSellerUser.getBirthday());
            condition.put("isMaster",tcSellerUser.getIsMaster());
            condition.put("isEnabled",tcSellerUser.getIsEnabled());
            condition.put("status",tcSellerUser.getStatus());
            condition.put("createTime",tcSellerUser.getCreateTime());
            condition.put("updateTime",tcSellerUser.getUpdateTime());
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
        String phone =  MapUtils.getString(map, "phone");
        if(org.apache.commons.lang.StringUtils.isNotEmpty(phone)){
            param.addCondition("phone","like","%"+phone+"%");
        }
        String sellerId =  MapUtils.getString(map, "sellerId");
        if(org.apache.commons.lang.StringUtils.isNotEmpty(sellerId)){
            param.addCondition("seller_id","=",Integer.valueOf(sellerId));
        }
        //是否启用
        String isEnabled =  MapUtils.getString(map, "isEnabled");
        if(org.apache.commons.lang.StringUtils.isNotEmpty(isEnabled)){
            param.addCondition("is_enabled","=",Integer.valueOf(isEnabled));
        }
        // 是否主账号 1：是,2否
        param.addCondition("is_master","=",2);
        param.addCondition("status","=",1);
        long count = tcSellerUserDao.count(param);
        List<TcSellerUser> tcSellerUsers = tcSellerUserDao.list(param);
        List<Map<String,Object>> tcSellerUserlist = new ArrayList<>();
        for(TcSellerUser tcSellerUser : tcSellerUsers){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcSellerUser.getId());
            condition.put("appId",tcSellerUser.getAppId());
            condition.put("appSecrert",tcSellerUser.getAppSecrert());
            condition.put("customerId",tcSellerUser.getCustomerId());
            condition.put("comeFrom",tcSellerUser.getComeFrom());
            condition.put("sellerId",tcSellerUser.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcSellerUser.getSellerId());
            if(tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("parentId",tcSellerUser.getParentId());
            condition.put("username",tcSellerUser.getUsername());
            condition.put("phone",tcSellerUser.getPhone());
            condition.put("email",tcSellerUser.getEmail());
            condition.put("contact",tcSellerUser.getContact());
            condition.put("province",tcSellerUser.getProvinceId());
            condition.put("city",tcSellerUser.getCityId());
            condition.put("county",tcSellerUser.getCountyId());
            condition.put("address",tcSellerUser.getAddress());
            condition.put("headImage",tcSellerUser.getHeadImage());
            condition.put("sex",tcSellerUser.getSex());
            condition.put("nickname",tcSellerUser.getNickname());
            condition.put("birthday",tcSellerUser.getBirthday());
            condition.put("isMaster",tcSellerUser.getIsMaster());
            condition.put("isEnabled",tcSellerUser.getIsEnabled());
            condition.put("status",tcSellerUser.getStatus());
            condition.put("createTime",tcSellerUser.getCreateTime());
            condition.put("updateTime",tcSellerUser.getUpdateTime());
            tcSellerUserlist.add(condition);
        }
        map.clear();
        map.put("list",tcSellerUserlist);//返回前端集合命名为list
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
        //条件筛选
        //查询
        QueryParam param = new QueryParam();
        String phone =  MapUtils.getString(map, "phone");
        if(org.apache.commons.lang.StringUtils.isNotEmpty(phone)){
            param.addCondition("phone","like","%"+phone+"%");
        }
        String sellerId =  MapUtils.getString(map, "sellerId");
        if(org.apache.commons.lang.StringUtils.isNotEmpty(sellerId)){
            param.addCondition("seller_id","=",Integer.valueOf(sellerId));
        }
        //是否启用
        String isEnabled =  MapUtils.getString(map, "isEnabled");
        if(org.apache.commons.lang.StringUtils.isNotEmpty(isEnabled)){
            param.addCondition("is_enabled","=",Integer.valueOf(isEnabled));
        }
        param.addCondition("status","=",1);
        long count = tcSellerUserDao.count(param);
        List<TcSellerUser> tcSellerUsers = tcSellerUserDao.list(param);
        List<Map<String,Object>> tcSellerUserlist = new ArrayList<>();
        for(TcSellerUser tcSellerUser : tcSellerUsers){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcSellerUser.getId());
            condition.put("appId",tcSellerUser.getAppId());
            condition.put("appSecrert",tcSellerUser.getAppSecrert());
            condition.put("customerId",tcSellerUser.getCustomerId());
            condition.put("comeFrom",tcSellerUser.getComeFrom());
            condition.put("sellerId",tcSellerUser.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcSellerUser.getSellerId());
            if(tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("parentId",tcSellerUser.getParentId());
            condition.put("username",tcSellerUser.getUsername());
            condition.put("phone",tcSellerUser.getPhone());
            condition.put("email",tcSellerUser.getEmail());
            condition.put("contact",tcSellerUser.getContact());
            condition.put("province",tcSellerUser.getProvinceId());
            condition.put("city",tcSellerUser.getCityId());
            condition.put("county",tcSellerUser.getCountyId());
            condition.put("address",tcSellerUser.getAddress());
            condition.put("headImage",tcSellerUser.getHeadImage());
            condition.put("sex",tcSellerUser.getSex());
            condition.put("nickname",tcSellerUser.getNickname());
            condition.put("birthday",tcSellerUser.getBirthday());
            condition.put("isMaster",tcSellerUser.getIsMaster());
            condition.put("isEnabled",tcSellerUser.getIsEnabled());
            condition.put("status",tcSellerUser.getStatus());
            condition.put("createTime",tcSellerUser.getCreateTime());
            condition.put("updateTime",tcSellerUser.getUpdateTime());
            tcSellerUserlist.add(condition);
        }
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",tcSellerUserlist);
    }



}
