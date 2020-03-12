package com.perenc.xh.lsp.controller.interceptor;



import com.perenc.xh.commonUtils.utils.StringOrDate.MD5Utils;
import com.perenc.xh.lsp.entity.user.User;
import com.perenc.xh.lsp.service.user.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class LoginLogInterceptor extends HandlerInterceptorAdapter {

	@Autowired(required = false)
	private UserService userService;
	
	private Logger logger = Logger.getLogger(LoginLogInterceptor.class);
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String url = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/"));
		if(!url.equals("/loginPage"))
		{
			return true;
		}
		logger.info("认证日志--开始");
		String ip=request.getHeader("x-forwarded-for");
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
		        ip = request.getHeader("Proxy-Client-IP");
		    }
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
		        ip = request.getHeader("WL-Proxy-Client-IP");
		    }
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
		        ip = request.getRemoteAddr();
		    }
		    ip=(ip.equals("0:0:0:0:0:0:0:1"))?"127.0.0.1":ip;
			try {
				Map<String,Object> param = new HashMap<>();
				int loginType = ServletRequestUtils.getIntParameter(request, "loginType", 0);
				if(loginType == 1) {
					param.put("loginType", 1);
					param.put("account", request.getParameter("username"));
				}else{
					param.put("loginType", 0);
					param.put("username", request.getParameter("username"));
				}
				String md5Passeord = MD5Utils.MD5Encode(request.getParameter("password"), "utf8");
				param.put("password",md5Passeord);
				User user = userService.userLoginPage(param);
				if(user == null)
				{
					return true;
				}
				if ("0".equals(user.getStatus()+"")) {
					logger.info("---认证日志---新增成功！！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("---认证日志---新增失败！！");
				return true;
			}
		logger.info("认证日志--结束");
		return true;
	}

}
