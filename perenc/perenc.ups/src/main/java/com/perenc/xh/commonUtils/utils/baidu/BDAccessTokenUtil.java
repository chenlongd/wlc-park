package com.perenc.xh.commonUtils.utils.baidu;

import com.perenc.xh.commonUtils.utils.http.HttpCilentUtils;
import com.perenc.xh.commonUtils.utils.redis.JedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/2/27 16:45
 **/
public class BDAccessTokenUtil {

    static Logger logger = Logger.getLogger(BDAccessTokenUtil.class);
    /**
     * 获取百度 智能写作 access_token1
     * @param ak
     * @param sk
     * @return
     * @throws Exception
     */
    public static String getBaiduNaturalVoiceAcToken(String ak,String sk) throws Exception {
        // 获取token地址
        //String key = "bdacToken-"+ak;
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String url = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            String access_token="";
            String bdNaturalVoiceAcToken= (String) JedisUtil.getString("baiduNaturalVoiceAcToken");
                if(StringUtils.isEmpty(bdNaturalVoiceAcToken)) {
                String result = HttpCilentUtils.sendGet(url);
                JSONObject jsonObject = new JSONObject(result);
                access_token = jsonObject.getString("access_token");
                JedisUtil.setString("baiduNaturalVoiceAcToken", access_token);
            }else {
             access_token=bdNaturalVoiceAcToken;
            }
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }

//    public static void main(String[] args) throws Exception  {
//        String ak="rRznlnh2L3lNpGtf2sEjMMbX";
//        String sk="1bhsjrrVwoXKt1FToPo3ROnduLLwojLu";
//         String ab= getBaiduNaturalVoiceAcToken(ak,sk);
//        System.out.println("=======actoken========"+ab);
//    }


}
