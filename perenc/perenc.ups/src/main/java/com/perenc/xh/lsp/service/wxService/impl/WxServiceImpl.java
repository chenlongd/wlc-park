package com.perenc.xh.lsp.service.wxService.impl;


import com.perenc.xh.common.loginFilter.cache.Cache;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.utils.wxUtil.StrUtil;
import com.perenc.xh.commonUtils.utils.wxUtil.WxUtil;
import com.perenc.xh.lsp.entity.publicAccount.WxPublicAccount;
import com.perenc.xh.lsp.entity.wxConfig.App;
import com.perenc.xh.lsp.entity.wxConfig.Cfg;
import com.perenc.xh.lsp.entity.wxCustomer.WmCustomer;
import com.perenc.xh.lsp.entity.wxCustomerInfo.WmCustomerInfo;
import com.perenc.xh.lsp.service.wxCustomer.WxCustomerService;
import com.perenc.xh.lsp.service.wxCustomerInfo.WxCustomerInfoService;
import com.perenc.xh.lsp.service.wxService.WxService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.Map;


@Service("wxService")
@Transactional(rollbackFor = Exception.class)
public class WxServiceImpl implements WxService {

    private static Logger logger = Logger.getLogger(WxServiceImpl.class);

    @Autowired(required = false)
    private WxCustomerService wmCustomerService;

    @Autowired(required = false)
    private WxCustomerInfoService wmCustomerInfoService;

    @Override
    public Map<String, Object> getJsApiCfg(String url) throws Exception {
        url = URLDecoder.decode(url, "utf-8");
//		App wx = merchantWxService.getAppByMch(mch);
        App app = new App();
        app.setAppId(Cfg.getWxappid());
        app.setAppSecret(Cfg.getWxappsecret());
        //获取JSAPI配置
        Map<String, Object> jsapiConfig = WxUtil.getJsApiCfg(Cache.getInstance().getWxTicket(app), url, app);
        return jsapiConfig;
    }

    @Override
    public  void authBack(HttpSession session, String code,String appId, String appSecret) throws Exception {
        if (StringUtils.isEmpty(code)) {
            return;
        }
        //通过授权结果code拉取微信用户
        WxPublicAccount wxPublicAccount = WxUtil.getWxPublicAccount(code, appId, appSecret);
        logger.info("----------拉去用户信息------------"+wxPublicAccount);
        if (wxPublicAccount == null || StringUtils.isEmpty(((WxPublicAccount) wxPublicAccount).getOpenid())) {
            return;
        }
        //查询是否已有该用户
        QueryParam param = new QueryParam();
        param.put("openid", wxPublicAccount.getOpenid());
        WmCustomerInfo customerInfo = wmCustomerInfoService.getOne(param);
        Integer customerId = null;
        if (customerInfo != null) {
            //没有用户添加用户
            WmCustomer customer = wmCustomerService.getById(customerInfo.getCustomerId());
            if (customer == null) {
                customer = new WmCustomer();
                customer.setOpenId(wxPublicAccount.getOpenid());
                customer.setNickName(StrUtil.filterOffUtf8Mb4(wxPublicAccount.getNickname()));
                customer.setHeadImgUrl(wxPublicAccount.getHeadimgurl());
                customer.setSex(wxPublicAccount.getSex());
                customer.setRegisterMode(2);//微信公众号
                customer.setCity(wxPublicAccount.getCity());
                customer.setProvince(wxPublicAccount.getProvince());
                customer.setCountry(wxPublicAccount.getCountry());
                wmCustomerService.insertWmCustomer(customer);
                QueryParam query = new QueryParam();
                query.put("open_id",wxPublicAccount.getOpenid());
                WmCustomer wmCustomer = wmCustomerService.getWmCustomer(query);
                if(wmCustomer != null){
                    customerId = wmCustomer.getId();
                }
            }else {
                customerId = customerInfo.getCustomerId();
            }
        } else {
            QueryParam queryParam = new QueryParam();
            queryParam.put("open_id",wxPublicAccount.getOpenid());
            WmCustomer wcus = wmCustomerService.getWmCustomer(queryParam);
            if(wcus != null){
                WmCustomerInfo wmCustomerInfo = new WmCustomerInfo();
                wmCustomerInfo.setUnionid(wxPublicAccount.getUnionid());
                wmCustomerInfo.setOpenid(wxPublicAccount.getOpenid());
                if (wcus != null) {
                    wmCustomerInfo.setCustomerId(wcus.getId());
                    customerId = wcus.getId();
                }
                wmCustomerInfo.setNickName(StrUtil.filterOffUtf8Mb4(wxPublicAccount.getNickname()));
                wmCustomerInfo.setSex(wxPublicAccount.getSex());
                wmCustomerInfo.setAppid(appId);
                wmCustomerInfo.setHeadImg(wxPublicAccount.getHeadimgurl());
                wmCustomerInfo.setCity(wxPublicAccount.getCity());
                wmCustomerInfo.setProvince(wxPublicAccount.getProvince());
                wmCustomerInfo.setCountry(wxPublicAccount.getCountry());
                wmCustomerInfo.setComeFrom(2);//微信公众号
                wmCustomerInfoService.insertWmCustomerInfo(wmCustomerInfo);
            }else{
                WmCustomer customer = new WmCustomer();
                customer.setOpenId(wxPublicAccount.getOpenid());
                customer.setNickName(StrUtil.filterOffUtf8Mb4(wxPublicAccount.getNickname()));
                customer.setHeadImgUrl(wxPublicAccount.getHeadimgurl());
                customer.setSex(wxPublicAccount.getSex());
                customer.setRegisterMode(2);//微信公众号
                customer.setCity(wxPublicAccount.getCity());
                customer.setProvince(wxPublicAccount.getProvince());
                customer.setCountry(wxPublicAccount.getCountry());
                int flag = wmCustomerService.insertWmCustomer(customer);
                if (flag > 0) {
                    QueryParam query = new QueryParam();
                    query.put("open_id", wxPublicAccount.getOpenid());
                    WmCustomer wmCustomer = wmCustomerService.getWmCustomer(query);
                    WmCustomerInfo wmCustomerInfo = new WmCustomerInfo();
                    wmCustomerInfo.setUnionid(wxPublicAccount.getUnionid());
                    wmCustomerInfo.setOpenid(wxPublicAccount.getOpenid());
                    if (wmCustomer != null) {
                        wmCustomerInfo.setCustomerId(wmCustomer.getId());
                        customerId = wmCustomer.getId();
                    }
                    wmCustomerInfo.setNickName(StrUtil.filterOffUtf8Mb4(wxPublicAccount.getNickname()));
                    wmCustomerInfo.setSex(wxPublicAccount.getSex());
                    wmCustomerInfo.setHeadImg(wxPublicAccount.getHeadimgurl());
                    wmCustomerInfo.setCity(wxPublicAccount.getCity());
                    wmCustomerInfo.setProvince(wxPublicAccount.getProvince());
                    wmCustomerInfo.setCountry(wxPublicAccount.getCountry());
                    wmCustomerInfo.setAppid(appId);
                    wmCustomerInfo.setComeFrom(2);//微信公众号
                    wmCustomerInfoService.insertWmCustomerInfo(wmCustomerInfo);
                }
            }
        }
        WmCustomer wmCustomer = wmCustomerService.getById(customerId);
        //账号信息
        session.setAttribute("wxChatUser", wmCustomer);
    }
}
