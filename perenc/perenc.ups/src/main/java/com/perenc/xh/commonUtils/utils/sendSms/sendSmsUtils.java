package com.perenc.xh.commonUtils.utils.sendSms;

import com.perenc.xh.commonUtils.utils.http.HttpCilentUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

public class sendSmsUtils {

    private static final Logger LOGGER = Logger.getLogger(sendSmsUtils.class);

    public static final String JUHE_APPKEY = "4832bc47224a5a851dc22c651dad9dee";
    public static final String JUHE_TPLID = "50847";

    public static String send(String mobile, String verificationCode) {
        String tpl_value = "#code#=" + verificationCode ;//变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，<a href="http://www.juhe.cn/news/index/id/50" target="_blank">详细说明></a>
        return sendBase(mobile, JUHE_TPLID, tpl_value);
    }

    private static String sendBase(String mobile, String templateId, String tpl_value) {
        String url = "http://v.juhe.cn/sms/send";//请求接口地址
        java.util.Map<String, Object> params = new HashMap<>();//请求参数
        params.put("mobile", mobile);//接收短信的手机号码
        params.put("tpl_id", templateId);//短信模板ID，请参考个人中心短信模板设置
        params.put("tpl_value", tpl_value);//变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，<a href="http://www.juhe.cn/news/index/id/50" target="_blank">详细说明></a>
        params.put("key", JUHE_APPKEY);//应用APPKEY(应用详细页查询)
        params.put("dtype", "json");//返回数据的格式,xml或json，默认json

        String result = "";
        try {
            result = HttpCilentUtils.httpPost(url, params);
        } catch (IOException e) {
            LOGGER.error("短信业务发送失败！" + e);
            e.printStackTrace();
        }
        return result;
    }


}
