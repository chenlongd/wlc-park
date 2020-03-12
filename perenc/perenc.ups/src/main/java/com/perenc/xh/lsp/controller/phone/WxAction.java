package com.perenc.xh.lsp.controller.phone;



import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.utils.wxUtil.WxUtil;
import com.perenc.xh.lsp.entity.wxConfig.Cfg;
import com.perenc.xh.lsp.entity.wxCustomer.WmCustomer;
import com.perenc.xh.lsp.service.wxService.WxService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Controller
@RequestMapping("/wx")
public class WxAction {
    Logger logger = Logger.getLogger(WxAction.class);

    @Autowired
    private WxService wxService;


    @ResponseBody
    @RequestMapping("wxConfig")
    public ReturnJsonData getJsApiCfg(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");

        String url = ServletRequestUtils.getStringParameter(request, "url");
        logger.info("---------获取签名url------------"+url);
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取微信配置信息",wxService.getJsApiCfg(url));
    }

    /**
     * 授权
     *
     * @param backUrl
     */
    @RequestMapping("authorize")
    public void authorize(HttpServletRequest req, HttpServletResponse resp, String backUrl)
            throws Exception {
        backUrl = URLEncoder.encode(backUrl, "utf-8");
        String wxRtn = "";
        wxRtn = Cfg.getApproot() + "wx/authBack?backUrl=" + backUrl;
        logger.info("授权跳转url===wxRtn==== " + wxRtn);
        String toWx = getAuthUrl(Cfg.getWxappid(), wxRtn);
        req.setAttribute("toUrl", toWx);
        logger.info("---去授权：" + toWx);
        forward(req, resp, "redirect.jsp");
    }

    /**
     * 授权回调
     */
    @RequestMapping("authBack")
    public void authBack(HttpServletRequest req, HttpServletResponse resp, String code, String backUrl)
            throws Exception {
        if (StringUtils.isEmpty(code)) {
            // 重试
            backUrl = URLEncoder.encode(backUrl, "utf-8");
            String wxRtn = Cfg.getApproot() + "wx/authBack?backUrl=" + backUrl;
            String toWx = WxUtil.getAuthUrl(Cfg.getWxappid(), wxRtn);
            req.setAttribute("toUrl", toWx);
            logger.info("---重试，去授权：" + toWx);
            forward(req, resp, "redirect.jsp");
            return;
        } else {
            wxService.authBack(req.getSession(), code, Cfg.getWxappid(), Cfg.getWxappsecret());
            WmCustomer user = (WmCustomer) req.getSession().getAttribute("wxChatUser");
            logger.info("--------小慧微信用户-----------"+user);
            if (user == null) {
                forward(req, resp, "erre_page.jsp");
                return;
            }
            req.setAttribute("toUrl", Cfg.getApproot() + backUrl);
            logger.info("--------小慧回调地址-----------"+Cfg.getApproot() + backUrl);
            forward(req, resp, "redirect.jsp");
        }
    }

    /**
     * 跳转
     * @param req
     * @param resp
     * @param path
     */
    public static void forward(HttpServletRequest req, HttpServletResponse resp, String path) {
        try {
            req.getRequestDispatcher("/"+path).forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAuthUrl(String appid, String rtnUrl) {
        try {
            rtnUrl = URLEncoder.encode(rtnUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 请求地址
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + appid + "&redirect_uri="
                + rtnUrl + "&response_type=code" + "&scope=snsapi_userinfo" + "&state=666#wechat_redirect";

        return url;
    }

}
