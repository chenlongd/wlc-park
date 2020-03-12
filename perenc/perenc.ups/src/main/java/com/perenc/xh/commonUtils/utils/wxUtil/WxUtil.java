package com.perenc.xh.commonUtils.utils.wxUtil;


import com.perenc.xh.common.loginFilter.cache.Cache;
import com.perenc.xh.lsp.entity.publicAccount.WxPublicAccount;
import com.perenc.xh.lsp.entity.wxConfig.App;
import com.perenc.xh.lsp.entity.wxConfig.msg.IndustryTemplateMessageSend;
import com.perenc.xh.lsp.entity.wxCustomer.WmCustomer;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 微信工具
 *
 * @author zgh
 */
public class WxUtil {

    static Logger logger = Logger.getLogger(WxUtil.class);

    /**
     * 取授权地址
     *
     * @param appid
     * @param rtnUrl 授权后的回调地址
     * @return
     */
    public static String getAuthUrl(String appid, String rtnUrl) {
        try {
            rtnUrl = URLEncoder.encode(rtnUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 请求地址
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + appid + "&redirect_uri="
                + rtnUrl + "&response_type=code" + "&scope=snsapi_userinfo" + "&state=666#wechat_redirect";

        return url;
    }


    public static WmCustomer getWxUser(String code, App wx) throws Exception {
        return getWxUser(code, wx.getAppId(), wx.getAppSecret());
    }

    /**
     * 通过授权结果code拉取微信用户
     *
     * @param code
     * @return
     */
    public static WmCustomer getWxUser(String code, String appid, String secret) throws Exception {
        // 第一步：通过code换取access_token与openid
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token" + "?appid=" + appid + "&secret=" + secret
                + "&code=" + code + "&grant_type=authorization_code";
        String res = HttpUtil.sendGet(url);
        String accessToken = JsonUtil.parseMap(res).get("access_token") + "";
        String openId = JsonUtil.parseMap(res).get("openid") + "";

        // 第二步：通过access_token与openid拉取用户信息
        url = "https://api.weixin.qq.com/sns/userinfo" + "?access_token=" + accessToken + "&openid=" + openId
                + "&lang=zh_CN";
        res = HttpUtil.sendGet(url);
        WmCustomer wmCustomer = JsonUtil.parsePojo(res, WmCustomer.class);

        return wmCustomer;
    }

    /**
     * 取微信用户信息（通过openId）
     */
    public static WxPublicAccount getWxPublicAccount(String code, String appid, String secret) throws Exception {
        // 第一步：通过code换取access_token与openid
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token" + "?appid=" + appid + "&secret=" + secret
                + "&code=" + code + "&grant_type=authorization_code";
        String res = HttpUtil.sendGet(url);
        String openId = JsonUtil.parseMap(res).get("openid") + "";
        String accessToken = JsonUtil.parseMap(res).get("access_token") + "";
        App app = new App();
        app.setAppId(appid);
        app.setAppSecret(secret);
        url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + Cache.getInstance().getWxToken(app)
                + "&openid=" + openId + "&lang=zh_CN";
        res = HttpUtil.sendGet(url);
        logger.info("=======拉取数据========"+res);
        WxPublicAccount wxPublicAccount = JsonUtil.parsePojo(res, WxPublicAccount.class);
        if(wxPublicAccount != null){
            if(wxPublicAccount.getSubscribe()== 0 && StringUtils.isEmpty(wxPublicAccount.getNickname())){
                logger.info("======accessToken==="+accessToken);
                url = "https://api.weixin.qq.com/sns/userinfo" + "?access_token=" + accessToken + "&openid=" + openId
                        + "&lang=zh_CN";
                res = HttpUtil.sendGet(url);
                logger.info("==============获取用户信息============"+res);
                WxPublicAccount wxUser = JsonUtil.parsePojo(res, WxPublicAccount.class);
                logger.info("-------微信公众号信息1---------"+wxUser.getSex());
                return  wxUser;
            }
        }
        logger.info("-------微信公众号信息2---------"+wxPublicAccount.getSex());
        logger.info("-------微信公众号信息3---------"+wxPublicAccount.getSubscribe());
        return wxPublicAccount;
    }

//    /**
//     * 取微信用户信息（通过openId）
//     *
//     * @param openId
//     */
    public static WxPublicAccount getWxUserByOpenid(String openId, String appid, String secret) throws Exception {
        App app = new App();
        app.setAppId(appid);
        app.setAppSecret(secret);
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + Cache.getInstance().getWxToken(app)
                + "&openid=" + openId + "&lang=zh_CN";

        String res = HttpUtil.sendGet(url);
        logger.info("============通过OPENID获取的微信信息为=========="+res);
        WxPublicAccount wxUser = JsonUtil.parsePojo(res, WxPublicAccount.class);
        return wxUser;
    }

    /**
     * 通过静默授权结果code拉取微信用户openid
     *
     * @param code
     * @return
     */
    public static String getWxUserOpenid(String code, String appid, String secret) throws Exception {
        // 第一步：通过code换取access_token与openid
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token" + "?appid=" + appid + "&secret=" + secret
                + "&code=" + code + "&grant_type=authorization_code";
        String res = HttpUtil.sendGet(url);
        String accessToken = JsonUtil.parseMap(res).get("access_token") + "";
        String openId = JsonUtil.parseMap(res).get("openid") + "";

        return openId;
    }

    public static Map<String, Object> getJsApiCfg(String jsapi_ticket, String url, App wx) throws Exception {

        String appId = wx.getAppId();

        String nonce_str = UUID.randomUUID().toString();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);

        // 生成签名(注意这里参数名必须全部小写，且必须有序)
        String signature = "";
        String string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url="
                + url;

        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(string1.getBytes("UTF-8"));
        signature = byteToHex(crypt.digest());

        // 组装成配置
        Map<String, Object> cfg = new HashMap<String, Object>();
        cfg.put("appId", appId);
        cfg.put("timestamp", timestamp);
        cfg.put("nonceStr", nonce_str);
        cfg.put("signature", signature);
        cfg.put("rawString", string1);
        return cfg;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 发送客服消息(文本)
     *
     * @param openId
     * @param content
     * @throws Exception
     */
    public static void sendCustomMsg(String openId, String content, App wx,String appId,String appSecret) {
        try {
            logger.error("进=========sendCustomMsg==========");
            logger.error("=========openId=========="+openId);
            logger.error("=========content=========="+content);
            logger.error("=========wx=========="+wx);
            logger.error("=========appId=========="+appId);
            logger.error("=========appSecret=========="+appSecret);
            String token = Cache.getInstance().getWxTokenMsg(appId,appSecret);
            logger.error("access_token=========================="+token);
            String reqUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token;
            String jsonParam = "{'touser':'{OPENID}'," + "'msgtype':'text'," + "'text':" + "{'content':'{CONTENT}'}"
                    + "}";
            jsonParam = jsonParam.replace("'", "\"").replace("{OPENID}", openId).replace("{CONTENT}", content);
            String res = HttpUtil.SSLRequest(reqUrl, "POST", jsonParam);
            if ((Integer) JsonUtil.parseMap(res).get("errcode") != 0) {
                logger.error("发送客服消息失败，res = " + res);
            }
            logger.info("---发送客服消息成功，openId=" + openId + ", content=" + content + ",res = " + res);
        } catch (Exception e) {

            logger.error("发送客服消息异常", e);
        }

    }

    /**
     * 发送模版信息
     * @param msg
     * @throws Exception
     */
    public static void sendTemplateMsg(IndustryTemplateMessageSend msg, String appId, String appSecret) {
        try {
            String token = Cache.getInstance().getWxTokenMsg(appId,appSecret);
//            String token = "9_zXDb5EK5faqzmLNR81uqmv6BpCT8H4vuarUEOhF2n8oC-yeN2SoFvRp5PVnZTwgSsOpiwksvKtiNpmqTWXmEV-walU2b37kKh9Zap2qm1rFwXcSxdZebZkVkloLSqsa5KjzCH7PoGi64UsgzBAKjACAPOY";
            String reqUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+ token;
            String jsonParam = new JSONObject(msg).toString();
            String res = HttpUtil.SSLRequest(reqUrl, "POST", jsonParam);
            if ((Integer) JsonUtil.parseMap(res).get("errcode") != 0) {
                logger.error("发送客服消息失败，res = " + res);
            }
            logger.info("---发送客服消息成功----------res = " + res);
        } catch (Exception e) {
            logger.error("发送客服消息异常", e);
        }
    }

}
