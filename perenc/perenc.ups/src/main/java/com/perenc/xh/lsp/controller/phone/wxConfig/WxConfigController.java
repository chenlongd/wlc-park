package com.perenc.xh.lsp.controller.phone.wxConfig;


import com.perenc.xh.lsp.entity.wxCustomer.WmCustomer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("web")
public class WxConfigController {

    private static Logger logger = Logger.getLogger("WxConfigController.class");

    /**
     * 授权页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "authHtml", produces = "application/json; charset=utf-8")
    public String authHtml(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        WmCustomer wmCustomer = (WmCustomer) request.getSession().getAttribute("wxChatUser");
        return "redirect:http://www.wlczmsc.com/dl/dl.html?customerId="+wmCustomer.getId();
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "shareHtml", produces = "application/json; charset=utf-8")
    public String shareHtml(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Logger logger = Logger.getLogger(WxConfigController.class);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");


        //获取上级微信微信ID
        String suid = request.getParameter("suid");
        WmCustomer wxCustomer = (WmCustomer) request.getSession().getAttribute("wxChatUser");

        //跳转到分享页面
        return "redirect:http://www.wlczmsc.com/dl/dl.html?suid="+suid+"&customerId="+wxCustomer.getId();
    }


}
