package com.perenc.xh.commonUtils.utils.jshttp;

import com.alibaba.fastjson.JSONObject;
import com.perenc.xh.commonUtils.utils.StringOrDate.MD5Utils;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

public class JsHttpClientUtil {

    /****服务器捷顺配置开始***/
    //服务器捷顺接口地址Url
    public static final String JS_CLIENT_URL = "http://111.85.248.64:62718/api/";
    //捷顺接口地址Url_用户名
    public static final String JS_CLIENT_URL_USERNAME = "9999";
    //捷顺接口地址Url_密码
    public static final String JS_CLIENT_URL_PASSWORD = "js*168";
    //捷顺接口地址Url_接入渠道编码ID
    public static final String JS_CLIENT_APPID = "app01";
    //捷顺接口地址Url_密钥
    public static final String JS_CLIENT_KEY = "c9b4df46-a186-11e9-94dc-0894ef6502e6";
    /****服务器捷顺配置结束***/

    /****测试捷顺配置开始***/
    //服务器捷顺接口地址Url
    /* public static final String JS_CLIENT_URL = "http://172.22.7.254:8091/api/";
    //捷顺接口地址Url_用户名
    public static final String JS_CLIENT_URL_USERNAME = "8888";
    //捷顺接口地址Url_密码
    public static final String JS_CLIENT_URL_PASSWORD = "123456";
    //捷顺接口地址Url_接入渠道编码
    public static final String JS_CLIENT_APPID = "app01";
    //捷顺接口地址Url_密钥
    public static final String JS_CLIENT_KEY = "8f5a876a-a20c-11e9-852c-40b0767eccfe";*/



    //JSON 类型
    public static final String CONTENTTYPE_JSON = "application/json";
    //XML 类型
    public static final String CONTENTTYPE_XML = "application/xml";
    //MULTIPART 上传类型
    public static final String CONTENTTYPE_MULTIPART = "multipart/form-data";

    //超时时长，单位：毫秒
    public static final int CONNECT_TIMEOUT =15000;


    private static HttpHeaders newHeader(){
        HttpHeaders header = new HttpHeaders();

        header.add("Access-Control-Allow-Origin", "*");
        header.add("Access-Control-Allow-Headers", "X-Requested-With");
        header.add("Cache-Control", "no-cache");

        header.add("appId", JS_CLIENT_APPID);
        header.add("v", "1.0");
        String random=getSixNewcode()+"";
        header.add("random",random);
        String timestamp=System.currentTimeMillis() / 1000+"";
        header.add("timestamp",timestamp);
        String sign="random" + random + "timestamp" + timestamp + "key" + JS_CLIENT_KEY;
        String signmd5 = MD5Utils.MD5Encode(sign, "utf8");
        header.add("sign", signmd5);
        header.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return header;
    }



    /**
     * post 请求方法（JSON）
     *
     * @param url
     * @param map 参数
     * @return
     * @throws IOException
     */
    public static JSONObject httpGet(String url, Map<String, Object> map) throws IOException {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //1s
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(CONNECT_TIMEOUT);
        JSONObject result =null;

        //响应时间
        long rstime=0;
        //访问开始时间
        long rstart=0;
        //访问结束时间
        long rend=0;
        try {
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            rstart = System.currentTimeMillis();
            HttpHeaders header = newHeader();
            HttpEntity<String> requestEntity = new HttpEntity<String>(null, header);
            ResponseEntity<JSONObject> jsonObjectResult = restTemplate.exchange(url, HttpMethod.GET, requestEntity, JSONObject.class, map);
            rend = System.currentTimeMillis();
            rstime=(rend - rstart);
            if(jsonObjectResult.getStatusCodeValue()==200) {
                JSONObject jsonResult = jsonObjectResult.getBody();
                jsonResult.put("rstime", rstime);
                result = jsonResult;
            }else {
                JSONObject jsonErr=new JSONObject();
                jsonErr.put("code","error2");
                jsonErr.put("msg","访问失败");
                jsonErr.put("date","");
                jsonErr.put("rstime",rstime);
                result=jsonErr;
            }
        } catch (Exception e) {
            rend = System.currentTimeMillis();
            rstime=(rend - rstart);
            System.out.println(e.getMessage());
            JSONObject jsonErr=new JSONObject();
            jsonErr.put("code","error1");
            jsonErr.put("msg",e.getMessage());
            jsonErr.put("date","");
            jsonErr.put("rstime",rstime);
            result=jsonErr;
        }
        return result;
    }

    /**
     * post 请求方法（JSON）
     *
     * @param url
     * @param reqJsonStr 参数
     * @return
     * @throws IOException
     */
    public static JSONObject httpPost(String url,String reqJsonStr) throws IOException {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //1s
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(CONNECT_TIMEOUT);
        JSONObject result =null;
        //响应时间
        long rstime=0;
        //访问开始时间
        long rstart=0;
        //访问结束时间
        long rend=0;
        try {
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            rstart = System.currentTimeMillis();
            HttpHeaders header = newHeader();
            HttpEntity<String> requestEntity = new HttpEntity<String>(reqJsonStr, header);
            ResponseEntity<JSONObject> jsonObjectResult = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JSONObject.class);
            rend = System.currentTimeMillis();
            rstime=(rend - rstart);
            if(jsonObjectResult.getStatusCodeValue()==200) {
                JSONObject jsonResult = jsonObjectResult.getBody();
                jsonResult.put("rstime", rstime);
                result = jsonResult;
            }else {
                JSONObject jsonErr=new JSONObject();
                jsonErr.put("code","error2");
                jsonErr.put("msg","访问失败");
                jsonErr.put("date","");
                jsonErr.put("rstime",rstime);
                result=jsonErr;
            }
        } catch (Exception e) {
            rend = System.currentTimeMillis();
            rstime=(rend - rstart);
            System.out.println(e.getMessage());
            JSONObject jsonErr=new JSONObject();
            jsonErr.put("code","error1");
            jsonErr.put("msg",e.getMessage());
            jsonErr.put("date","");
            jsonErr.put("rstime",rstime);
            result=jsonErr;
        }
        return result;
    }


    /**
     * post 请求方法（JSON）
     * 捷顺支付结果通知
     * @param url
     * @param reqJsonStr 参数
     * @return
     * @throws IOException
     */
    public static JSONObject httpPostNotice(String url,String reqJsonStr) throws IOException {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //1s
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(CONNECT_TIMEOUT);
        JSONObject result =null;
        //响应时间
        long rstime=0;
        //访问开始时间
        long rstart=0;
        //访问结束时间
        long rend=0;
        try {
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            rstart = System.currentTimeMillis();
            HttpHeaders header = newHeader();
            HttpEntity<String> requestEntity = new HttpEntity<String>(reqJsonStr, header);
            ResponseEntity<JSONObject> jsonObjectResult = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JSONObject.class);
            rend = System.currentTimeMillis();
            rstime=(rend - rstart);
            if(jsonObjectResult.getStatusCodeValue()==200) {
                JSONObject jsonResult = jsonObjectResult.getBody();
                jsonResult.put("rstime", rstime);
                result = jsonResult;
            }else {
                JSONObject jsonErr=new JSONObject();
                jsonErr.put("code","error2");
                jsonErr.put("msg","访问失败");
                jsonErr.put("date","");
                jsonErr.put("rstime",rstime);
                result=jsonErr;
            }
        } catch (Exception e) {
            rend = System.currentTimeMillis();
            rstime=(rend - rstart);
            System.out.println(e.getMessage());
            JSONObject jsonErr=new JSONObject();
            jsonErr.put("code","error1");
            jsonErr.put("msg",e.getMessage());
            jsonErr.put("date","");
            jsonErr.put("rstime",rstime);
            result=jsonErr;
        }
        return result;
    }



    /**
     * post 请求方法（JSON）
     * 捷顺_获取签名密钥
     * @param url
     * @param map 参数
     * @return
     * @throws IOException
     */
    public static JSONObject getJsSign(String url,Map<String, Object> map) throws IOException {
        JSONObject result =null;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "X-Requested-With");
        headers.add("Cache-Control", "no-cache");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<JSONObject> jsonObjectResult = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JSONObject.class, map);
            result = jsonObjectResult.getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }



    /**
     * 六位数的随机数
     * random
     * @return
     */
    public static int getSixNewcode(){
        int newcode = (int)((Math.random()*9+1)*100000);
        return newcode;
    }

}
