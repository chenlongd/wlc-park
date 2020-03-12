//package com.perenc.xh.common.loginFilter;
//
//import com.perenc.xh.lsp.entity.wxConfig.Cfg;
//import com.perenc.xh.lsp.controller.phone.WxAction;
//import org.apache.log4j.Logger;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.HashMap;
//import java.util.Map;
//
//public class LoginFilter implements Filter {
//
//    private Logger logger = Logger.getLogger(LoginFilter.class);
//
//    private ApplicationContext context;
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        context = new ClassPathXmlApplicationContext("applicationContext.xml");
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        httpResponse.addHeader("Access-Control-Allow-Origin", "*");
//        httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
//        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, PUT, DELETE, POST");
//        try {
//            HttpServletRequest req = (HttpServletRequest) request;
//            HttpServletResponse resp = (HttpServletResponse) response;
//            HttpSession session = req.getSession();
//            String url = req.getRequestURI().substring(req.getContextPath().length() + 1);
//            Boolean validate = this.validate(session, url);
//            if (validate) {//------被拦截
//                this.handleIntercept(req, resp, url);//处理拦截的业务逻辑
//            } else {//-----------放行
//                chain.doFilter(request, response);
//            }
//        } catch (Exception e) {
//            logger.error("", e);
//            chain.doFilter(request, response);
//        }
//    }
//
//    /**
//     * 验证是否放行
//     * @param session
//     * @param url
//     */
//    private Boolean validate(HttpSession session, String url) {
//        Boolean passed = false;
//        //判断
//        Object customer = session.getAttribute("wxChatUser");
//        if (customer == null) {
//            if (url.startsWith("api/")) {
//                return false;
//            }
//            if (url.startsWith("web/") ) {
//                passed = true;
//            }
//            if (url.startsWith("wx/")) {
//                return false;
//            }
//        }
//        return passed;
//    }
//
//    /**
//     * 处理被拦截后的业务逻辑
//     * @param req
//     * @param resp
//     */
//    private void handleIntercept(HttpServletRequest req, HttpServletResponse resp, String url) throws Exception {
//        String backUrl = url + getParamStr(req);
//        logger.info("------拦截器被处理backUrl--------"+backUrl);
//        backUrl = URLEncoder.encode(backUrl, "utf-8");
//        String wxRtn = Cfg.getApproot() + "wx/authBack?backUrl=" + backUrl;
//        logger.info("------拦截器被处理wxRtn--------"+wxRtn);
//        String toWx = getAuthUrl(Cfg.getWxappid(), wxRtn);
//        logger.info("============toWx============"+toWx);
//        req.setAttribute("toUrl", toWx);
//        WxAction.forward(req, resp, "redirect.jsp");
//
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//
//    /**
//     * 获取参数名值对
//     * @param req
//     * @return
//     */
//    public static Map<String,String> getParamMap(HttpServletRequest req) {
//        Map<String, String[]> reqParams = req.getParameterMap();
//        Map<String, String> params = new HashMap<String, String>();
//        for(String key : reqParams.keySet()) {
//            params.put(key, reqParams.get(key)[0]);
//        }
//        return params;
//    }
//
//    /**
//     * 获取参数串
//     * @param req
//     * @return
//     */
//    public static String getParamStr(HttpServletRequest req) {
//        Map<String, String> paramMap = getParamMap(req);
//        return getParamStr(paramMap);
//    }
//
//    public static String getParamStr(Map<String, String> paramMap) {
//        String str = "";
//        for(String key : paramMap.keySet()) {
//            if("".equals(str)) {
//                str += "?";
//            } else {
//                str += "&";
//            }
//            str += key+"="+paramMap.get(key);
//        }
//        return str;
//    }
//
//    public static String getAuthUrl(String appid, String rtnUrl) {
//        try {
//            rtnUrl = URLEncoder.encode(rtnUrl, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        // 请求地址
//        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + appid + "&redirect_uri="
//                + rtnUrl + "&response_type=code" + "&scope=snsapi_userinfo" + "&state=666#wechat_redirect";
//        return url;
//    }
//}
