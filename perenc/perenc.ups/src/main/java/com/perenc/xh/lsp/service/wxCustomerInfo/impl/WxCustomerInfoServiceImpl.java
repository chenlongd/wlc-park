package com.perenc.xh.lsp.service.wxCustomerInfo.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.DBRelevant.InsertParam;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.crypto.WxMaCryptUtils;
import com.perenc.xh.commonUtils.utils.http.HttpCilentUtils;
import com.perenc.xh.commonUtils.utils.properties.PropertiesGetValue;
import com.perenc.xh.commonUtils.utils.redis.JedisUtil;
import com.perenc.xh.commonUtils.utils.wxUtil.StrUtil;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.dao.merchat.MchAccountDao;
import com.perenc.xh.lsp.dao.tcSeller.TcSellerDao;
import com.perenc.xh.lsp.dao.wxCustomer.WxCustomerDao;
import com.perenc.xh.lsp.dao.wxCustomerInfo.WxCustomerInfoDao;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.merchat.MchAccount;
import com.perenc.xh.lsp.entity.miniapp.WxMaJscode2SessionResult;
import com.perenc.xh.lsp.entity.miniapp.WxMaUserInfo;
import com.perenc.xh.lsp.entity.miniapp.WxMaPhoneNumberInfo;
import com.perenc.xh.lsp.entity.store.Store;
import com.perenc.xh.lsp.entity.tcSeller.TcSeller;
import com.perenc.xh.lsp.entity.weChatInfo.WeChatInfo;
import com.perenc.xh.lsp.entity.wxCustomer.WmCustomer;
import com.perenc.xh.lsp.entity.wxCustomerInfo.WmCustomerInfo;
import com.perenc.xh.lsp.service.wxCustomerInfo.WxCustomerInfoService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Service("wmCustomerInfoService")
@Transactional(rollbackFor = Exception.class)
public class WxCustomerInfoServiceImpl implements WxCustomerInfoService {

    private static Logger logger = Logger.getLogger("WxCustomerInfoServiceImpl.class");

    @Value("${miniapp.appid}")
    private String  miniapp_appid;
    @Value("${miniapp.secret}")
    private String  miniapp_secret;
    @Value("${jscode.to.session.url}")
    private String jscode_to_session_url;


    @Autowired(required = false)
    private WxCustomerInfoDao wmCustomerInfoDao;
    @Autowired(required = false)
    private WxCustomerDao wmCustomerDao;
    @Autowired(required = false)
    private MongoComDAO mongoComDAO;
    @Autowired(required = false)
    private MchAccountDao mchAccountDao;
    @Autowired(required = false)
    private ExtendUserDao extendUserDao;
    @Autowired(required = false)
    private TcSellerDao tcSellerDao;


    @Override
    public ReturnJsonData insertWmCustomerInfo(WmCustomerInfo wmCustomerInfo) throws Exception {
        InsertParam param = DBUtil.toInsertParam(wmCustomerInfo);
        int flag = wmCustomerInfoDao.add(param);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加数据成功",null);
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加数据失败",null);
    }

    @Override
    public WmCustomerInfo getOne(QueryParam param) throws Exception {
        return wmCustomerInfoDao.getOne(param);
    }

    @Override
    public ReturnJsonData loginMiniWxUser(Map<String, Object> map) throws Exception {
        String jsCode = MapUtils.getString(map,"code");
        String encryptedData = MapUtils.getString(map, "encryptedData");
        String iv = MapUtils.getString(map, "iv");
        String storeId = MapUtils.getString(map, "storeId");

        //通过code获取openid
        Map<String, String> params = new HashMap<>(8);
        Store store = null;
        if(StringUtils.isNotEmpty(storeId)) {
            store = mongoComDAO.executeForObjectById(storeId, Store.class);
        }else{
            store = mongoComDAO.executeForObjectById("5d3fa734e4b08372ad0e5c2d", Store.class);
        }
        if (store != null) {
            int storeAccountId = store.getAccountId();
            MchAccount account = mchAccountDao.getById(storeAccountId);
            if (account != null) {
                int accountId = account.getId();
                if (StringUtils.isNotEmpty(String.valueOf(accountId))) {
                    map.clear();
                    map.put("mchAccountId", account.getId());
                    map.put("type", 1);
                    map.put("useType", 1);
                    WeChatInfo weChatInfo = mongoComDAO.executeForObjectByCon(map, WeChatInfo.class);
                    logger.info("-----------------"+weChatInfo);
                    logger.info("-----------------"+weChatInfo.getAppId());
                    logger.info("-----------------"+weChatInfo.getAppSecret());
                    if(weChatInfo != null){
                        params.put("appid", weChatInfo.getAppId());
                        params.put("secret", weChatInfo.getAppSecret());
                    }else{
                        params.put("appid", PropertiesGetValue.getProperty("miniapp.appid"));
                        params.put("secret", PropertiesGetValue.getProperty("miniapp.secret"));
                    }
                }
            }
        }
        params.put("js_code", jsCode);
        params.put("grant_type", "authorization_code");
        //解密微信小程序信息
        WxMaUserInfo wxMaUserInfo = getMiniWxInfo(params,encryptedData,iv,jsCode);
        if(wxMaUserInfo != null && !"1002".equals(wxMaUserInfo.getUnionId())) {
            String phoneNumber = wxMaUserInfo.getPhoneNumber();

            //查询是否已有该用户
            QueryParam param = new QueryParam();
            param.put("openid", wxMaUserInfo.getOpenId());
            WmCustomerInfo customerInfo = wmCustomerInfoDao.getOne(param);
            int customerId = 0;
            if (customerInfo != null) {
                WmCustomer customer = wmCustomerDao.getById(customerInfo.getCustomerId());
                if (customer == null) {
                    customer = new WmCustomer();
                    customer.setNickName(StrUtil.filterOffUtf8Mb4(wxMaUserInfo.getNickName()));
                    customer.setOpenId(wxMaUserInfo.getOpenId());
                    customer.setHeadImgUrl(wxMaUserInfo.getAvatarUrl());
                    customer.setSex(Integer.parseInt(wxMaUserInfo.getGender()));
                    customer.setBelongToType(1);//南山婆
                    customer.setRegisterMode(1);//微信小程序注册
                    customer.setCity(wxMaUserInfo.getCity());
                    customer.setProvince(wxMaUserInfo.getProvince());
                    customer.setCountry(wxMaUserInfo.getCountry());
                    InsertParam insertParam = DBUtil.toInsertParam(customer);
                    int i = wmCustomerDao.add(insertParam);
                    if (i > 0) {
                        customerId = Integer.valueOf(insertParam.getId());
                        Map<String, Object> condition = new HashMap<>();
                        condition.put("customerId", customerId);

                        return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加用户信息成功", condition);
                    }
                } else {
                    customerId = customer.getId();
                    Map<String, Object> condition = new HashMap<>();
                    condition.put("customerId", customerId);
                    if(StringUtils.isNotEmpty(phoneNumber)) {
                        customer.setPhone(phoneNumber);
                        condition.put("phone", phoneNumber);
                    }
                    wmCustomerDao.update(DBUtil.toUpdateParam(customer,"id"));
                    return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加用户信息成功", condition);
                }
            } else {
                WmCustomer customer = new WmCustomer();
                customer.setNickName(StrUtil.filterOffUtf8Mb4(wxMaUserInfo.getNickName()));
                customer.setOpenId(wxMaUserInfo.getOpenId());
                customer.setHeadImgUrl(wxMaUserInfo.getAvatarUrl());
                customer.setSex(Integer.parseInt(wxMaUserInfo.getGender()));
                customer.setBelongToType(1);//南山婆
                customer.setRegisterMode(1);//微信小程序注册
                customer.setCity(wxMaUserInfo.getCity());
                customer.setProvince(wxMaUserInfo.getProvince());
                customer.setCountry(wxMaUserInfo.getCountry());
                InsertParam insertParam = DBUtil.toInsertParam(customer);
                int i = wmCustomerDao.add(insertParam);
                if (i > 0) {
                    customerId = Integer.valueOf(insertParam.getId());
                    WmCustomerInfo wmCustomerInfo = new WmCustomerInfo();
                    wmCustomerInfo.setUnionid(wxMaUserInfo.getUnionId());
                    wmCustomerInfo.setAppid(wxMaUserInfo.getWatermark().getAppid());
                    wmCustomerInfo.setOpenid(wxMaUserInfo.getOpenId());
                    wmCustomerInfo.setCustomerId(Integer.valueOf(insertParam.getId()));
                    wmCustomerInfo.setNickName(StrUtil.filterOffUtf8Mb4(wxMaUserInfo.getNickName()));
                    wmCustomerInfo.setSex(Integer.parseInt(wxMaUserInfo.getGender()));
                    wmCustomerInfo.setHeadImg(wxMaUserInfo.getAvatarUrl());
                    wmCustomerInfo.setCity(wxMaUserInfo.getCity());
                    wmCustomerInfo.setProvince(wxMaUserInfo.getProvince());
                    wmCustomerInfo.setCountry(wxMaUserInfo.getCountry());
                    wmCustomerInfo.setComeFrom(1);
                    i = wmCustomerInfoDao.add(DBUtil.toInsertParam(wmCustomerInfo));
                    if (i > 0) {
                        Map<String, Object> condition = new HashMap<>();
                        condition.put("customerId", customerId);
                        return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加小程序用户信息成功", condition);
                    }
                }
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "微信获取用户信息错误", null);
        }
        return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "获取小程序数据不成功", null);
    }

    /**
     * 小程序认证电话号码
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData authMiniWxPhone(Map<String, Object> map) throws Exception {
        String jsCode = MapUtils.getString(map,"code");
        String encryptedData = MapUtils.getString(map, "encryptedData");
        String iv = MapUtils.getString(map, "iv");
        String type = MapUtils.getString(map, "type");

        //通过code获取openid
        Map<String, String> params = new HashMap<>(8);
        if (StringUtils.isNotEmpty(type)) {
            map.clear();
            map.put("type", Integer.valueOf(type));
            map.put("useType", 1);
            WeChatInfo weChatInfo = mongoComDAO.executeForObjectByCon(map, WeChatInfo.class);
            logger.info("-----------------"+weChatInfo);
            logger.info("-----------------"+weChatInfo.getAppId());
            logger.info("-----------------"+weChatInfo.getAppSecret());
            if(weChatInfo != null){
                params.put("appid", weChatInfo.getAppId());
                params.put("secret", weChatInfo.getAppSecret());
            }else{
                params.put("appid", "wx6284b75a737e288d");
                params.put("secret", "2e7152af24f6ef719d4e546dca8f2066");
            }
        }
        params.put("js_code", jsCode);
        params.put("grant_type", "authorization_code");
        //解密微信小程序信息
        WxMaUserInfo wxMaUserInfo = getMiniWxInfo(params,encryptedData,iv,jsCode);
        //业务逻辑处理
        if(wxMaUserInfo != null && !"1002".equals(wxMaUserInfo.getUnionId())){
            //先查询
            String phoneNumber = wxMaUserInfo.getPhoneNumber();
            if(StringUtils.isNotEmpty(phoneNumber)){
                map.clear();
                map.put("phone",phoneNumber);
                Store store = mongoComDAO.executeForObjectByCon(map, Store.class);
                if(store != null){
                    return new ReturnJsonData(DataCodeUtil.SUCCESS,"登录店铺成功",store);
                }
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "微信获取用户信息错误", null);
        }
        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"登录店铺失败",null);
    }

    /**
     * 解密微信小程序信息
     * @param params
     * @param encryptedData
     * @param iv
     * @return
     * @throws Exception
     */
    public WxMaUserInfo getMiniWxInfo(Map<String,String> params,String encryptedData,String iv,String jsCode){
        try{
            String sessionKey = JedisUtil.getString(jsCode);
            if(StringUtils.isNotEmpty(sessionKey)) {
                WxMaUserInfo wxMaUserInfo = WxMaUserInfo.fromJson(WxMaCryptUtils.decrypt(sessionKey, encryptedData, iv));
                return wxMaUserInfo;
            }else {
                String url = "https://api.weixin.qq.com/sns/jscode2session" + "?" + Joiner.on("&").withKeyValueSeparator("=").join(params);
                String json = HttpCilentUtils.httpGet(url);
                logger.info("---------json--------" + json);
                String openid = "";
                if (StringUtils.isNotEmpty(json)) {
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    openid = (String) jsonObject.get("openid");
                    if (StringUtils.isNotEmpty(openid)) {
                        JedisUtil.setString(jsCode, 3600, (String) jsonObject.getString("session_key"));
                    }
                }
                WxMaJscode2SessionResult wxMaJscode2SessionResult = WxMaJscode2SessionResult.fromJson(json);
                //通过sessionkey和encryptedData解密获取用户
                WxMaUserInfo wxMaUserInfo = WxMaUserInfo.fromJson(WxMaCryptUtils.decrypt(wxMaJscode2SessionResult.getSessionKey(),
                        encryptedData, iv));
                if (StringUtils.isNotEmpty(openid)) {
                    wxMaUserInfo.setOpenId(openid);
                }
                return wxMaUserInfo;
            }
        }catch(Exception e){
            WxMaUserInfo wxMaUserInfo = new WxMaUserInfo();
            wxMaUserInfo.setUnionId("1002");
            return wxMaUserInfo;
        }
    }


    /**
     * 停车场小程序认证电话号码
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData authTcMiniWxPhone(Map<String, Object> map) throws Exception {
        String jsCode = MapUtils.getString(map,"code");
        String encryptedData = MapUtils.getString(map, "encryptedData");
        String iv = MapUtils.getString(map, "iv");
        //类型：3：停车客户端小程序，4：停车商户端小程序
        String type = MapUtils.getString(map, "type");
        String extendIdinp = MapUtils.getString(map, "extendId");
        //通过code获取openid
        Map<String, String> params = new HashMap<>(8);
        map.clear();
        map.put("type", Integer.valueOf(type));
        map.put("useType", 1);
        WeChatInfo weChatInfo = mongoComDAO.executeForObjectByCon(map, WeChatInfo.class);
        logger.info("-----------------"+weChatInfo);
        logger.info("-----------------"+weChatInfo.getAppId());
        logger.info("-----------------"+weChatInfo.getAppSecret());
        if(weChatInfo != null){
            params.put("appid", weChatInfo.getAppId());
            params.put("secret", weChatInfo.getAppSecret());
        }else{
            params.put("appid", miniapp_appid);
            params.put("secret",miniapp_secret);
        }
        params.put("js_code", jsCode);
        params.put("grant_type", "authorization_code");
        //解密微信小程序信息
        WxMaPhoneNumberInfo phoneNumberInfo = getWxMaPhoneNumberInfo(params,encryptedData,iv,jsCode);
        //业务逻辑处理
        if(phoneNumberInfo != null) {
            //先查询
            String phoneNumber = phoneNumberInfo.getPhoneNumber();
            String openid = phoneNumberInfo.getOpenid();
            if (StringUtils.isNotEmpty(phoneNumber) && StringUtils.isNotEmpty(openid)) {
                //查询是否已有该用户
                QueryParam param = new QueryParam();
                param.put("phone",phoneNumber);
                WmCustomer customer = wmCustomerDao.getOne(param);
                Integer customerId = 0;
                if (customer != null) {
                    customerId = customer.getId();
                    //判断第二次进入存在用户信息，更新用户信息
                    if(!phoneNumber.equals(customer.getPhone())) {
                        customer.setPhone(phoneNumber);
                        wmCustomerDao.update(DBUtil.toUpdateParam(customer, "id"));
                    }
                    QueryParam paramct = new QueryParam();
                    paramct.addCondition("customer_id","=",customerId);
                    WmCustomerInfo customerInfo = wmCustomerInfoDao.getOne(paramct);
                    if (customerInfo == null) {
                        customerInfo = new WmCustomerInfo();
                        customerInfo.setOpenid(customer.getOpenId());
                        customerInfo.setComeFrom(1);//微信小程序注册
                        customerInfo.setCustomerId(customerId);
                        InsertParam insertParam = DBUtil.toInsertParam(customer);
                        wmCustomerInfoDao.add(insertParam);
                        Map<String,Object> condition = new HashMap<>();
                        //判断扩展用户
                        Integer extendId = 0;
                        QueryParam paramu = new QueryParam();
                        paramu.addCondition("customer_id","=",customerId);
                        ExtendUser returnUser = extendUserDao.getOne(paramu);
                        if(returnUser!=null) {
                            extendId=returnUser.getId();
                            condition.put("extendId",extendId);
                            //判断第二次进入存在用户信息，更新用户信息
                            if(!phoneNumber.equals(returnUser.getPhone())){
                                returnUser.setPhone(phoneNumber);
                                extendUserDao.update(DBUtil.toUpdateParam(returnUser, "id"));
                            }
                            return new ReturnJsonData(DataCodeUtil.SUCCESS, "登录停车场成功", condition);
                        }else {
                            ExtendUser addUser=new ExtendUser();
                            addUser.setCustomerId(customerId);
                            addUser.setComeFrom(1); //小程序
                            addUser.setPhone(phoneNumber);
                            addUser.setStatus(1);
                            addUser.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                            InsertParam insertParamu = DBUtil.toInsertParam(addUser);
                            int flag = extendUserDao.add(insertParamu);
                            if(flag>0) {
                                return new ReturnJsonData(DataCodeUtil.SUCCESS, "登录停车场成功", condition);
                            }else {
                                return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "登录停车场失败", null);
                            }
                        }
                    }else {
                        //判断扩展用户
                        Integer extendId = 0;
                        Map<String,Object> condition = new HashMap<>();
                        QueryParam paramu = new QueryParam();
                        paramu.addCondition("customer_id","=",customerId);
                        ExtendUser returnUser = extendUserDao.getOne(paramu);
                        if(returnUser!=null) {
                            extendId =returnUser.getId();
                            condition.put("extendId",extendId);
                            //判断第二次进入存在用户信息，更新用户信息
                            if(!phoneNumber.equals(returnUser.getPhone())){
                                returnUser.setPhone(phoneNumber);
                                extendUserDao.update(DBUtil.toUpdateParam(returnUser, "id"));
                            }
                            return new ReturnJsonData(DataCodeUtil.SUCCESS, "登录停车场成功", condition);
                        }else {
                            ExtendUser addUser=new ExtendUser();
                            addUser.setCustomerId(customerId);
                            addUser.setComeFrom(1); //小程序
                            addUser.setPhone(phoneNumber);
                            addUser.setStatus(1);
                            addUser.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                            InsertParam insertParamu = DBUtil.toInsertParam(addUser);
                            int flag = extendUserDao.add(insertParamu);
                            if(flag>0) {
                                extendId = Integer.valueOf(insertParamu.getId());
                                condition.put("extendId",extendId);
                                return new ReturnJsonData(DataCodeUtil.SUCCESS, "登录停车场成功", condition);
                            }else {
                                return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "登录停车场失败", null);
                            }
                        }
                    }
                }else {
                    WmCustomer customerInser = new WmCustomer();
                    customerInser.setOpenId(openid);
                    customerInser.setRegisterMode(1);//微信小程序注册
                    customerInser.setPhone(phoneNumber);
                    InsertParam insertParam = DBUtil.toInsertParam(customerInser);
                    int wct= wmCustomerDao.add(insertParam);
                    if(wct<1) {
                        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"登录停车场失败",null);
                    }
                    customerId = Integer.valueOf(insertParam.getId());
                    WmCustomerInfo wmCustomerInfo = new WmCustomerInfo();
                    wmCustomerInfo.setAppid(phoneNumberInfo.getWatermark().getAppid());
                    wmCustomerInfo.setOpenid(openid);
                    wmCustomerInfo.setCustomerId(customerId);
                    wmCustomerInfo.setComeFrom(1);
                    int wmc = wmCustomerInfoDao.add(DBUtil.toInsertParam(wmCustomerInfo));
                    if(wmc<1) {
                        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"登录停车场失败",null);
                    }
                    Map<String, Object> condition = new HashMap<>();
                    // 判断扩展用户
                    Integer extendId = 0;
                    QueryParam paramu = new QueryParam();
                    paramu.addCondition("customer_id", "=", customerId);
                    ExtendUser returnUser = extendUserDao.getOne(paramu);
                    if (returnUser != null) {
                        extendId = returnUser.getId();
                        condition.put("extendId", extendId);
                        //判断第二次进入存在用户信息，更新用户信息
                        if (!phoneNumber.equals(returnUser.getPhone())) {
                            returnUser.setPhone(phoneNumber);
                            extendUserDao.update(DBUtil.toUpdateParam(returnUser, "id"));
                        }
                        return new ReturnJsonData(DataCodeUtil.SUCCESS, "登录停车场成功", condition);
                    } else {
                        ExtendUser addUser = new ExtendUser();
                        addUser.setCustomerId(customerId);
                        addUser.setComeFrom(1); //小程序
                        addUser.setPhone(phoneNumber);
                        addUser.setStatus(1);
                        addUser.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                        InsertParam insertParamu = DBUtil.toInsertParam(addUser);
                        int flag = extendUserDao.add(insertParamu);
                        if (flag > 0) {
                            extendId = Integer.valueOf(insertParamu.getId());
                            condition.put("extendId",extendId);
                            return new ReturnJsonData(DataCodeUtil.SUCCESS, "登录停车场成功", condition);
                        } else {
                            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "登录停车场失败", null);
                        }
                    }
                }
            }
            //判断第二次进来session未过期
            if (StringUtils.isNotEmpty(phoneNumber) && StringUtils.isEmpty(openid)) {
                //查询是否已有该用户
                QueryParam param = new QueryParam();
                param.put("phone",phoneNumber);
                WmCustomer customer = wmCustomerDao.getOne(param);
                Integer customerId = 0;
                if (customer == null) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"登录停车场失败",null);
                }
                customerId = customer.getId();
                QueryParam paramct = new QueryParam();
                paramct.addCondition("customer_id", "=", customerId);
                WmCustomerInfo customerInfo = wmCustomerInfoDao.getOne(paramct);
                if (customerInfo == null) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "登录停车场失败", null);
                }
                //判断扩展用户
                Integer extendId = 0;
                Map<String, Object> condition = new HashMap<>();
                QueryParam paramu = new QueryParam();
                paramu.addCondition("customer_id", "=", customerId);
                ExtendUser returnUser = extendUserDao.getOne(paramu);
                if (returnUser == null) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"登录停车场失败",null);
                }
                extendId = returnUser.getId();
                condition.put("extendId", extendId);
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "登录停车场成功", condition);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "微信获取用户信息错误", null);
        }
        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"登录停车场失败",null);
    }



    /**
     * 解密微信小程序信息
     * @param params
     * @param encryptedData
     * @param iv
     * @return
     * @throws Exception
     */
    public WxMaPhoneNumberInfo getWxMaPhoneNumberInfo(Map<String,String> params,String encryptedData,String iv,String jsCode){
        try{
            String sessionKey = JedisUtil.getString(jsCode);
            if(StringUtils.isNotEmpty(sessionKey)) {
                WxMaPhoneNumberInfo wxMaPhoneNumberInfo = WxMaPhoneNumberInfo.fromJson(WxMaCryptUtils.decrypt(sessionKey, encryptedData, iv));
                return wxMaPhoneNumberInfo;
            }else {
                String url =  "https://api.weixin.qq.com/sns/jscode2session?" + Joiner.on("&").withKeyValueSeparator("=").join(params);
                String json = HttpCilentUtils.httpGet(url);
                logger.info("---------json--------" + json);
                String openid = "";
                if (StringUtils.isNotEmpty(json)) {
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    openid = (String) jsonObject.get("openid");
                    if (StringUtils.isNotEmpty(openid)) {
                        JedisUtil.setString(jsCode, 3600, (String) jsonObject.getString("session_key"));
                    }
                }
                WxMaJscode2SessionResult wxMaJscode2SessionResult = WxMaJscode2SessionResult.fromJson(json);
                //通过sessionkey和encryptedData解密获取用户
//                WxMaUserInfo wxMaUserInfo = WxMaUserInfo.fromJson(WxMaCryptUtils.decrypt(wxMaJscode2SessionResult.getSessionKey(),
//                        encryptedData, iv));
                WxMaPhoneNumberInfo wxMaPhoneNumberInfo = WxMaPhoneNumberInfo.fromJson(WxMaCryptUtils.decrypt(wxMaJscode2SessionResult.getSessionKey(), encryptedData, iv));
                if(wxMaPhoneNumberInfo != null) {
                    if (StringUtils.isNotEmpty(openid)) {
                        wxMaPhoneNumberInfo.setOpenid(openid);
                    }
                    return wxMaPhoneNumberInfo;
                }else{
                    WxMaPhoneNumberInfo phoneNumberInfo = new WxMaPhoneNumberInfo();
                    return phoneNumberInfo;
                }
            }
        }catch(Exception e){
            WxMaPhoneNumberInfo phoneNumberInfo = new WxMaPhoneNumberInfo();
            return phoneNumberInfo;
        }
    }



    /**
     * 停车场获取微信小程序获取用户信息
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData loginTcMiniWxUser(Map<String, Object> map) throws Exception {
        String jsCode = MapUtils.getString(map,"code");
        String encryptedData = MapUtils.getString(map, "encryptedData");
        String iv = MapUtils.getString(map, "iv");
        String sellerId = MapUtils.getString(map, "sellerId");
        //类型：3：停车客户端小程序，4：停车商户端小程序
        String type = MapUtils.getString(map, "type");
        String extendIdinp = MapUtils.getString(map, "extendId");
        //解密微信小程序信息
        //WxMaUserInfo wxMaUserInfo = getMiniWxInfo(params,encryptedData,iv);
        WxMaJscode2SessionResult wxMaJscode2SessionResult=null;
        WxMaUserInfo wxMaUserInfo =null;
        String sessionKey=JedisUtil.getString(extendIdinp+"");
        if(StringUtils.isEmpty(extendIdinp) || StringUtils.isEmpty(sessionKey)) {
            //通过code获取openid
            Map<String, String> params = new HashMap<>(8);
            map.clear();
            map.put("type", Integer.valueOf(type));
            map.put("useType", 1);
            WeChatInfo weChatInfo = mongoComDAO.executeForObjectByCon(map, WeChatInfo.class);
            logger.info("-----------------"+weChatInfo);
            logger.info("-----------------"+weChatInfo.getAppId());
            logger.info("-----------------"+weChatInfo.getAppSecret());
            if(weChatInfo != null){
                params.put("appid", weChatInfo.getAppId());
                params.put("secret", weChatInfo.getAppSecret());
            }else{
                params.put("appid", miniapp_appid);
                params.put("secret",miniapp_secret);
            }
            params.put("js_code", jsCode);
            params.put("grant_type", "authorization_code");
            String url =jscode_to_session_url + "?" + Joiner.on("&").withKeyValueSeparator("=").join(params);
            String json = HttpCilentUtils.httpGet(url);
            logger.info("---------json--------" + json);
            wxMaJscode2SessionResult = WxMaJscode2SessionResult.fromJson(json);
            //通过sessionkey和encryptedData解密获取用户
            wxMaUserInfo = WxMaUserInfo.fromJson(WxMaCryptUtils.decrypt(wxMaJscode2SessionResult.getSessionKey(),
                    encryptedData, iv));
            if (StringUtils.isNotEmpty(json)) {
                JSONObject jsonObject = JSONObject.parseObject(json);
                String openid = (String) jsonObject.get("openid");
                if (StringUtils.isNotEmpty(openid)) {
                    wxMaUserInfo.setOpenId(openid);
                }
            }
        }
        if(StringUtils.isNotEmpty(sessionKey)) {
            wxMaUserInfo = WxMaUserInfo.fromJson(WxMaCryptUtils.decrypt(sessionKey,
                    encryptedData, iv));
            ExtendUser returnUser = extendUserDao.getById(extendIdinp);
            if(returnUser!=null) {
                QueryParam param = new QueryParam();
                param.put("customer_id", returnUser.getCustomerId());
                WmCustomerInfo customerInfo = wmCustomerInfoDao.getOne(param);
                if(customerInfo!=null) {
                    String openid =customerInfo.getOpenid();
                    if (StringUtils.isNotEmpty(openid)) {
                        wxMaUserInfo.setOpenId(openid);
                    }
                }
            }
        }
        //查询是否已有该用户
        QueryParam param = new QueryParam();
        param.put("openid", wxMaUserInfo.getOpenId());
        WmCustomerInfo customerInfo = wmCustomerInfoDao.getOne(param);
        int customerId = 0;
        if (customerInfo != null) {
            //判断是第二次获取
            if(StringUtils.isEmpty(customerInfo.getNickName())){
                customerInfo.setNickName(StrUtil.filterOffUtf8Mb4(wxMaUserInfo.getNickName()));
                customerInfo.setSex(Integer.parseInt(wxMaUserInfo.getGender()));
                customerInfo.setHeadImg(wxMaUserInfo.getAvatarUrl());
                customerInfo.setCity(wxMaUserInfo.getCity());
                customerInfo.setProvince(wxMaUserInfo.getProvince());
                customerInfo.setCountry(wxMaUserInfo.getCountry());
                wmCustomerInfoDao.update(DBUtil.toUpdateParam(customerInfo, "id"));
            }
            WmCustomer customer = wmCustomerDao.getById(customerInfo.getCustomerId());
            if (customer == null) {
                customer = new WmCustomer();
                customer.setNickName(StrUtil.filterOffUtf8Mb4(wxMaUserInfo.getNickName()));
                customer.setOpenId(wxMaUserInfo.getOpenId());
                customer.setHeadImgUrl(wxMaUserInfo.getAvatarUrl());
                customer.setSex(Integer.parseInt(wxMaUserInfo.getGender()));
                customer.setRegisterMode(1);//微信小程序注册
                customer.setCity(wxMaUserInfo.getCity());
                customer.setProvince(wxMaUserInfo.getProvince());
                customer.setCountry(wxMaUserInfo.getCountry());
                InsertParam insertParam = DBUtil.toInsertParam(customer);
                int i = wmCustomerDao.add(insertParam);
                if (i > 0) {
                    customerId = Integer.valueOf(insertParam.getId());
                    Map<String,Object> condition = new HashMap<>();
                    //判断扩展用户
                    Integer extendId = 0;
                    QueryParam paramu = new QueryParam();
                    paramu.addCondition("customer_id","=",customerId);
                    ExtendUser returnUser = extendUserDao.getOne(paramu);
                    if(returnUser!=null) {
                        extendId=returnUser.getId();
                        condition.put("extendId",extendId);
                        //判断第二次进入存在用户信息，更新用户信息
                        if(StringUtils.isEmpty(returnUser.getHeadImage())){
                            returnUser.setSex(customer.getSex());
                            returnUser.setHeadImage(customer.getHeadImgUrl());
                            returnUser.setNickname(StrUtil.filterOffUtf8Mb4(wxMaUserInfo.getNickName()));
                            returnUser.setProvinceId(wxMaUserInfo.getProvince());
                            returnUser.setCityId(wxMaUserInfo.getCity());
                            returnUser.setCountyId(wxMaUserInfo.getCountry());
                            extendUserDao.update(DBUtil.toUpdateParam(returnUser, "id"));
                        }
                        return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加用户信息成功", condition);
                    }else {
                        ExtendUser addUser=new ExtendUser();
                        addUser.setCustomerId(customerId);
                        addUser.setComeFrom(1); //小程序
                        addUser.setSex(customer.getSex());
                        addUser.setHeadImage(customer.getHeadImgUrl());
                        addUser.setNickname(customer.getNickName());
                        addUser.setProvinceId(customer.getProvince());
                        addUser.setCityId(customer.getCity());
                        addUser.setCountyId(customer.getCountry());
                        addUser.setStatus(1);
                        addUser.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                        InsertParam insertParamu = DBUtil.toInsertParam(addUser);
                        int flag = extendUserDao.add(insertParamu);
                        if(flag>0) {
                            extendId = Integer.valueOf(insertParamu.getId());
                            condition.put("extendId",extendId);
                            JedisUtil.setString(extendId+"", 3600,wxMaJscode2SessionResult.getSessionKey());
                            return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加用户信息成功", condition);
                        }else {
                            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "获取小程序数据不成功", null);
                        }
                    }
                }
            }else {
                customerId = customer.getId();
                Map<String,Object> condition = new HashMap<>();
                //判断是第二次进入
                if(StringUtils.isEmpty(customer.getNickName())){
                    customer.setNickName(StrUtil.filterOffUtf8Mb4(wxMaUserInfo.getNickName()));
                    customer.setHeadImgUrl(wxMaUserInfo.getAvatarUrl());
                    customer.setSex(Integer.parseInt(wxMaUserInfo.getGender()));
                    customer.setCity(wxMaUserInfo.getCity());
                    customer.setProvince(wxMaUserInfo.getProvince());
                    customer.setCountry(wxMaUserInfo.getCountry());
                    wmCustomerDao.update(DBUtil.toUpdateParam(customer, "id"));
                }
                //判断扩展用户
                Integer extendId = 0;
                QueryParam paramu = new QueryParam();
                paramu.addCondition("customer_id","=",customerId);
                ExtendUser returnUser = extendUserDao.getOne(paramu);
                if(returnUser!=null) {
                    extendId=returnUser.getId();
                    condition.put("extendId",extendId);
                    //判断第二次进入存在用户信息，更新用户信息
                    if(StringUtils.isEmpty(returnUser.getHeadImage())){
                        returnUser.setSex(customer.getSex());
                        returnUser.setHeadImage(customer.getHeadImgUrl());
                        returnUser.setNickname(customer.getNickName());
                        extendUserDao.update(DBUtil.toUpdateParam(returnUser, "id"));
                    }
                    return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加用户信息成功", condition);
                }else {
                    ExtendUser addUser=new ExtendUser();
                    addUser.setCustomerId(customerId);
                    addUser.setComeFrom(1); //小程序
                    addUser.setSex(customer.getSex());
                    addUser.setHeadImage(customer.getHeadImgUrl());
                    addUser.setNickname(customer.getNickName());
                    addUser.setProvinceId(customer.getProvince());
                    addUser.setCityId(customer.getCity());
                    addUser.setCountyId(customer.getCountry());
                    addUser.setStatus(1);
                    addUser.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    InsertParam insertParamu = DBUtil.toInsertParam(addUser);
                    int flag = extendUserDao.add(insertParamu);
                    if(flag>0) {
                        extendId = Integer.valueOf(insertParamu.getId());
                        condition.put("extendId",extendId);
                        JedisUtil.setString(extendId+"", 3600,wxMaJscode2SessionResult.getSessionKey());
                        return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加用户信息成功", condition);
                    }else {
                        return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "获取小程序数据不成功", null);
                    }
                }
            }
        }else {
            WmCustomer customer = new WmCustomer();
            customer.setNickName(StrUtil.filterOffUtf8Mb4(wxMaUserInfo.getNickName()));
            customer.setOpenId(wxMaUserInfo.getOpenId());
            customer.setHeadImgUrl(wxMaUserInfo.getAvatarUrl());
            customer.setSex(Integer.parseInt(wxMaUserInfo.getGender()));
            customer.setRegisterMode(1);//微信小程序注册
            customer.setCity(wxMaUserInfo.getCity());
            customer.setProvince(wxMaUserInfo.getProvince());
            customer.setCountry(wxMaUserInfo.getCountry());
            InsertParam insertParam = DBUtil.toInsertParam(customer);
            int i = wmCustomerDao.add(insertParam);
            if (i > 0) {
                customerId = Integer.valueOf(insertParam.getId());;
                WmCustomerInfo wmCustomerInfo = new WmCustomerInfo();
                wmCustomerInfo.setUnionid(wxMaUserInfo.getUnionId());
                wmCustomerInfo.setAppid(wxMaUserInfo.getWatermark().getAppid());
                wmCustomerInfo.setOpenid(wxMaUserInfo.getOpenId());
                wmCustomerInfo.setCustomerId(Integer.valueOf(insertParam.getId()));
                wmCustomerInfo.setNickName(StrUtil.filterOffUtf8Mb4(wxMaUserInfo.getNickName()));
                wmCustomerInfo.setSex(Integer.parseInt(wxMaUserInfo.getGender()));
                wmCustomerInfo.setHeadImg(wxMaUserInfo.getAvatarUrl());
                wmCustomerInfo.setCity(wxMaUserInfo.getCity());
                wmCustomerInfo.setProvince(wxMaUserInfo.getProvince());
                wmCustomerInfo.setCountry(wxMaUserInfo.getCountry());
                wmCustomerInfo.setComeFrom(1);
                i = wmCustomerInfoDao.add(DBUtil.toInsertParam(wmCustomerInfo));
                if (i > 0) {
                    Map<String,Object> condition = new HashMap<>();
                    //判断扩展用户
                    Integer extendId = 0;
                    QueryParam paramu = new QueryParam();
                    paramu.addCondition("customer_id","=",customerId);
                    ExtendUser returnUser = extendUserDao.getOne(paramu);
                    if(returnUser!=null) {
                        extendId=returnUser.getId();
                        condition.put("extendId",extendId);
                        //判断第二次进入存在用户信息，更新用户信息
                        if(StringUtils.isEmpty(returnUser.getHeadImage())){
                            returnUser.setSex(wmCustomerInfo.getSex());
                            returnUser.setHeadImage(wmCustomerInfo.getHeadImg());
                            returnUser.setNickname(wmCustomerInfo.getNickName());
                            returnUser.setProvinceId(wmCustomerInfo.getProvince());
                            returnUser.setCityId(wmCustomerInfo.getCity());
                            returnUser.setCountyId(wmCustomerInfo.getCountry());
                            extendUserDao.update(DBUtil.toUpdateParam(returnUser, "id"));
                        }
                        return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加用户信息成功", condition);
                    }else {
                        ExtendUser addUser=new ExtendUser();
                        addUser.setCustomerId(customerId);
                        addUser.setComeFrom(1); //小程序
                        addUser.setSex(wmCustomerInfo.getSex());
                        addUser.setHeadImage(wmCustomerInfo.getHeadImg());
                        addUser.setNickname(wmCustomerInfo.getNickName());
                        addUser.setProvinceId(customer.getProvince());
                        addUser.setCityId(customer.getCity());
                        addUser.setCountyId(customer.getCountry());
                        addUser.setStatus(1);
                        addUser.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                        InsertParam insertParamu = DBUtil.toInsertParam(addUser);
                        int flag = extendUserDao.add(insertParamu);
                        if(flag>0) {
                            extendId = Integer.valueOf(insertParamu.getId());
                            condition.put("extendId",extendId);
                            JedisUtil.setString(extendId+"", 3600,wxMaJscode2SessionResult.getSessionKey());
                            return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加用户信息成功", condition);
                        }else {
                            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "获取小程序数据不成功", null);
                        }
                    }
                }
            }
        }
        return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "获取小程序数据不成功", null);
    }



    /**
     * 停车场商户端小程序认证电话号码
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData authTcSellerMiniWxPhone(Map<String, Object> map) throws Exception {
        String jsCode = MapUtils.getString(map,"code");
        String encryptedData = MapUtils.getString(map, "encryptedData");
        String iv = MapUtils.getString(map, "iv");
        String type = MapUtils.getString(map, "type");
        //通过code获取openid
        Map<String, String> params = new HashMap<>(8);
        if (StringUtils.isNotEmpty(type)) {
            map.clear();
            map.put("type", Integer.valueOf(type));
            map.put("useType", 1);
            WeChatInfo weChatInfo = mongoComDAO.executeForObjectByCon(map, WeChatInfo.class);
            logger.info("-----------------"+weChatInfo);
            logger.info("-----------------"+weChatInfo.getAppId());
            logger.info("-----------------"+weChatInfo.getAppSecret());
            if(weChatInfo != null){
                params.put("appid", weChatInfo.getAppId());
                params.put("secret", weChatInfo.getAppSecret());
            }else{
                params.put("appid", miniapp_appid);
                params.put("secret",miniapp_secret);
            }
        }
        params.put("js_code", jsCode);
        params.put("grant_type", "authorization_code");
        //解密微信小程序信息
        WxMaUserInfo wxMaUserInfo = getMiniWxInfo(params,encryptedData,iv,jsCode);
        //业务逻辑处理
        if(wxMaUserInfo != null){
            //先查询
            String phoneNumber = wxMaUserInfo.getPhoneNumber();
            if(StringUtils.isNotEmpty(phoneNumber)){
                QueryParam param = new QueryParam();
                param.addCondition("phone","=",phoneNumber);
                TcSeller tcSeller = tcSellerDao.getOne(param);
                if(tcSeller != null){
                    Map<String,Object> condition = new HashMap<>();
                    condition.put("sellerId",tcSeller.getId());
                    return new ReturnJsonData(DataCodeUtil.SUCCESS,"登录店铺成功",condition);
                }
            }
        }
        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"登录店铺失败",null);
    }

}
