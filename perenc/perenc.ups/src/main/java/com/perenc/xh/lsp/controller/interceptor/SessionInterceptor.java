package com.perenc.xh.lsp.controller.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.lsp.entity.user.User;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class SessionInterceptor implements HandlerInterceptor {

	private Logger logger = Logger.getLogger(SessionInterceptor.class);


	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		String origin = request.getHeader("origin");// 获取源站
		response.setHeader("Access-Control-Allow-Origin", origin);
		response.setHeader("Access-Control-Allow-Methods", "POST, GET");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
//		String url = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/"));
		String url = request.getRequestURI();
		String menuId = request.getParameter("menuId");
		if (!StringUtils.isEmpty(menuId)) {
			request.setAttribute("menuId", menuId);
		}

		if (url.contains("/loginOut") || url.contains("/loginPage") || url.contains("/ups/api") || url.contains("/saveHotelMemberData") || url.contains("/uploadFile") || url.contains("/api")) {
			return true;
		} else {
			
			User user = (User) request.getSession().getAttribute("user");
			// 如果没有登录，则跳转到登录页面
			if (user == null) {
				logger.info("没有登录");
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				JSONObject json = new JSONObject();
				/*json.put("success", false);
				json.put("msg", "该用户没有登录!");*/
				json.put("code", DataCodeUtil.NOT_LOGON);
				json.put("msg", "该用户没有登录!");
				out.print(json);
				return false;
			} else {
				return true;
			}
		}
	}
}
