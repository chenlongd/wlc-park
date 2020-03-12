package com.perenc.xh.lsp.controller.interceptor;


import com.perenc.xh.lsp.entity.urlLog.UrlLog;
import com.perenc.xh.lsp.service.urlLog.UrlLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * @name: UrlLoggerInterceptor
 * @description: 日志拦截器记录操作日志
 * @author: StarFall
 */
public class UrlLoggerInterceptor implements HandlerInterceptor {

    @Autowired(required = false)
    private UrlLogService urlLogService;

    // 本地线程存储消耗时间变量
    private static final ThreadLocal<Long> costTimeThreadLocal = new NamedThreadLocal<>("costTimeThreadLocal");
    // 本地线程存储log的id
    private static final ThreadLocal<String> logIdThreadLocal = new NamedThreadLocal<>("logIdThreadLocal");

    /**
     * 该方法在目标方法之前被调用.<br>
     * 若返回值为 true, 则继续调用后续的拦截器和目标方法. <br>
     * 若返回值为 false, 则不会再调用后续的拦截器和目标方法.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
       String origin = request.getHeader("origin");// 获取源站
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        String url = request.getRequestURI();
        String extendId = ServletRequestUtils.getStringParameter(request, "extendId", "0");
        String sellerId = ServletRequestUtils.getStringParameter(request, "sellerId", "0");
        String sellerUserId = ServletRequestUtils.getStringParameter(request, "sellerUserId", "0");
        //判断是否包含http,日志记录为客户端操作
        if(url.indexOf("/api")!=-1) { //包含
            long startTime = System.currentTimeMillis();
            costTimeThreadLocal.set(startTime);
            // 初始化UrlLog对象
            UrlLog log = new UrlLog();
            if (StringUtils.isNotEmpty(extendId)) {
                log.setExtendId(Integer.valueOf(extendId));
            }
            if (StringUtils.isNotEmpty(sellerId)) {
                log.setSellerId(Integer.valueOf(sellerId));
            }
            if (StringUtils.isNotEmpty(sellerUserId)) {
                log.setSellerUserId(Integer.valueOf(sellerUserId));
            }
            log.setRequestUrl(request.getRequestURL().toString());
            log.setRequestMethod(request.getMethod());
            log.setResponseStatus("");
            log.setResponseBody("");
            if (handler instanceof HandlerMethod) {
                HandlerMethod h = (HandlerMethod) handler;
                log.setControllerName(h.getBean().getClass().getName());
                log.setControllerMethod(h.getMethod().getName());
            }
            log.setRequestParams(getParamString(request.getParameterMap()));
            UrlLog urlLog=urlLogService.insertLog(log);
            // 获取自动生成的主键
            String logId =urlLog.getId();
            logIdThreadLocal.set(logId);
        }
        return true;

    }

    /**
     * 调用目标方法之后, 但在DispatcherServlet渲染视图之前被调用. <br>
     * 可以处理对应的ModelAndView
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        response.setCharacterEncoding("UTF-8");
       response.setContentType("application/json; charset=utf-8");
        String url = request.getRequestURI();
        //判断是否包含http,日志记录为客户端操作
        if(url.indexOf("/api")!=-1) { //包含
            long endTime = System.currentTimeMillis();
            long startTime = costTimeThreadLocal.get();
            String responseBody = null;
            if (request.getAttribute("response") != null) {
                responseBody = request.getAttribute("response").toString();
            }
            // 更新log对象
            String logId = logIdThreadLocal.get();
            UrlLog log = new UrlLog();
            log.setId(logId);
            log.setResponseStatus(String.valueOf(response.getStatus()));
            log.setResponseBody(responseBody);
            log.setCostTime(String.valueOf((endTime - startTime)));
            urlLogService.update(log);
            // 清除本地线程
            logIdThreadLocal.remove();
            costTimeThreadLocal.remove();
        }
    }

    /**
     * 在DispatcherServlet渲染视图之后被调用. 主要作用是释放资源<br>
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    /**
     * 从Map参数获取参数字符串
     *
     * @param map
     *            参数map
     * @return
     */
    private String getParamString(Map<String, String[]> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> e : map.entrySet()) {
            sb.append(e.getKey()).append("=");
            String[] value = e.getValue();
            if (value != null && value.length == 1) {
                sb.append(value[0]).append("&");
            } else {
                sb.append(Arrays.toString(value)).append("&");
            }
        }
        if (sb.length() >= 1) {
            if (sb.substring(sb.length() - 1, sb.length()).equals("&")) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        return sb.toString();
    }
}