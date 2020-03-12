package com.perenc.xh.commonUtils.utils.aliyunSms;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;


public class AliyunSmsUtils {
    //产品域名,开发者无需替换
    //static final String domain = "dysmsapi.aliyuncs.com";
    //此处需要替换成开发者自己的AK
    //static final String accessKeyId = "LTAILxY7JKmq0L9G";  //ak
    //static final String accessKeySecret = "2CWhU11X5AoIJfOtOUGB1lQJNnVJQc";   //ac
    //2020-01-21替换成新AK
    static final String domain = "dysmsapi.aliyuncs.com";
    static final String accessKeyId = "LTAItMWKG0Yrfz5Z";  //ak
    static final String accessKeySecret = "IPTJDICkvphBJCBdit6nYpteWGr5Aw";   //ac

    public static CommonResponse sendSms(String telephone, String code) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);

        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(domain);
        request.setVersion("2017-05-25");//版本号
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        //必填:待发送手机号
        request.putQueryParameter("PhoneNumbers", telephone);
        //必填:短信签名-可在短信控制台中找到
        request.putQueryParameter("SignName", "鼎吉贸易");
        //必填:短信模板-可在短信控制台中找到
        //request.putQueryParameter("TemplateCode", "SMS_160765179");
        request.putQueryParameter("TemplateCode", "SMS_182668570");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.putQueryParameter("OutId", "123456");
        CommonResponse response=null;
        try {
            response = acsClient.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return response;
    }



    public static int getNewcode(){
       int newcode = (int)((Math.random()*9+1)*1000);//每次调用生成一位四位数的随机数
        return  newcode;
    }


    public static int getSixNewcode(){
        int newcode = (int)((Math.random()*9+1)*100000);
        return newcode;
    }


    public static void main(String[] args) throws ClientException, InterruptedException {

        //System.out.println("==========="+getNewcode());
        //System.out.println("==========="+getSixNewcode());
        String code = Integer.toString(getSixNewcode());
        System.out.println("发送的验证码为：" + code);
        //发短信
        CommonResponse response = sendSms("18285383669", code); // TODO 填写你需要测试的手机号码

        if(response!=null && response.getHttpStatus()==200) {
            String data = response.getData();
            JSONObject jsondate = (JSONObject) JSONObject.parseObject(data);
            if(jsondate.get("Code")!= null && jsondate.get("Code").equals("OK")){
                System.out.println("Code=" + jsondate.getString("Code"));
                System.out.println("Message=" + jsondate.getString("Message"));
                System.out.println("RequestId=" + jsondate.getString("RequestId"));
                System.out.println("BizId=" + jsondate.getString("BizId"));
                System.out.println("短信发送成功！");
            }else {
                System.out.println("短信发送失败！");
            }
        }else  {
            System.out.println("短信发送失败！");
        }
    }

}
