package com.perenc.xh.commonUtils.utils.baidu;

import com.alibaba.fastjson.JSONArray;
import com.baidu.aip.speech.AipSpeech;
import org.json.JSONObject;

import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BDPcmTextUtil {

    public static final String APP_ID = "15461281";
    public static final String API_KEY = "yQQwiwnY9QPDFSnhGQAr9Wre";
    public static final String SECRET_KEY = "V2lkENLc2OEHEuadqGOHy6Twg2jV0Cwb";


    public static String getPcmToText(AudioInputStream audioInputStream) {
        String result="";
        // 初始化一个AipSpeech
        try {
            AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

            // 可选：设置网络连接参数
            //client.setConnectionTimeoutInMillis(2000);
            //client.setSocketTimeoutInMillis(60000);

            // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
            // client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
            //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

            // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
            // 也可以直接通过jvm启动参数设置此环境变量
            // System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

            // 调用接口
            //JSONObject res = client.asr("D:\\logs\\sound\\pcm\\output1.pcm", "pcm", 16000, null);

            byte[] buffer = new byte[1024];
            //JSONObject res = client.asr("D:\\logs\\sound\\pcm\\output1.pcm", "pcm", 16000, null);
             JSONObject res = client.asr(input2byte(audioInputStream), "pcm", 16000, null);
             if(res!=null) {
                 String strresult = String.valueOf(res.get("result"));
                 JSONArray resultJsarry = JSONArray.parseArray(strresult);
                 if (resultJsarry.size() > 0) {
                     result = resultJsarry.get(0).toString();
                 }
             }
            System.out.println(res.toString(2));
        }catch (Exception e) {
            e.getMessage();
            result="默认";
            return result;
        }
        System.out.println("======result========"+result);

        return result;
    }

    public static String getPcmToTextPath(String filePaht) {
        String result="";
        // 初始化一个AipSpeech
        try {
            AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

            // 可选：设置网络连接参数
            //client.setConnectionTimeoutInMillis(2000);
            //client.setSocketTimeoutInMillis(60000);

            // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
            // client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
            //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

            // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
            // 也可以直接通过jvm启动参数设置此环境变量
            // System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

            // 调用接口
            //JSONObject res = client.asr("D:\\logs\\sound\\pcm\\output1.pcm", "pcm", 16000, null);
            JSONObject res = client.asr(filePaht, "pcm", 16000, null);
            //JSONObject res = client.asr(input2byte(audioInputStream), "pcm", 16000, null);
            if(res!=null) {
                String strresult = String.valueOf(res.get("result"));
                JSONArray resultJsarry = JSONArray.parseArray(strresult);
                if (resultJsarry.size() > 0) {
                    result = resultJsarry.get(0).toString();
                }
            }
            System.out.println(res.toString(2));
        }catch (Exception e) {
            e.getMessage();
            result="默认";
            return result;
        }
        System.out.println("======result========"+result);

        return result;
    }



    public static final byte[] input2byte(AudioInputStream inStream)
            throws IOException {
                  ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                  byte[] buff = new byte[100];
                 int rc = 0;
               while ((rc = inStream.read(buff, 0, 100)) > 0) {
                       swapStream.write(buff, 0, rc);
                      }
             byte[] in2b = swapStream.toByteArray();
                return in2b;
         }


}
