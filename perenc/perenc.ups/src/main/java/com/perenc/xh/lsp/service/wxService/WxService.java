package com.perenc.xh.lsp.service.wxService;


import javax.servlet.http.HttpSession;
import java.util.Map;

public interface WxService {


   Map<String,Object> getJsApiCfg(String url) throws Exception;

    /**
     * 授权
     * @param session
     * @param code
     * @throws Exception
     */
    void authBack(HttpSession session, String code, String appId, String appSecret) throws Exception;
}
