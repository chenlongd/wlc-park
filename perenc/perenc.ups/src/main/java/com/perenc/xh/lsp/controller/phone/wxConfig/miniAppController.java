package com.perenc.xh.lsp.controller.phone.wxConfig;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.service.wxCustomerInfo.WxCustomerInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("api")
public class miniAppController {

    @Autowired(required = false)
    private WxCustomerInfoService wmCustomerInfoService;

    @ResponseBody
    @RequestMapping("loginMiniWxUser")
    public ReturnJsonData loginMiniWxUser(HttpServletRequest request, HttpServletResponse response)throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");

        String storeId = request.getParameter("storeId");
        String code = request.getParameter("code");
        String encryptedData = request.getParameter("encryptedData");
        String iv = request.getParameter("iv");
        if (StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(encryptedData)
                && StringUtils.isNotEmpty(iv)) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            map.put("encryptedData", encryptedData);
            map.put("iv", iv);
            map.put("storeId", storeId);
            return wmCustomerInfoService.loginMiniWxUser(map);
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "参数不能为空", null);
        }
    }

    @ResponseBody
    @RequestMapping("authMiniWxPhone")
    public ReturnJsonData authMiniWxPhone(HttpServletRequest request, HttpServletResponse response)throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");

        String type = request.getParameter("type");
        String code = request.getParameter("code");
        String encryptedData = request.getParameter("encryptedData");
        String iv = request.getParameter("iv");
        if (StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(encryptedData)
                && StringUtils.isNotEmpty(iv) && StringUtils.isNotEmpty(type)) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            map.put("encryptedData", encryptedData);
            map.put("iv", iv);
            map.put("type", type);
            return wmCustomerInfoService.authMiniWxPhone(map);
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "参数不能为空", null);
        }
    }

    /**
     * 停车场小程序认证电话号码
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("authTcMiniWxPhone")
    public ReturnJsonData authTcMiniWxPhone(HttpServletRequest request, HttpServletResponse response)throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");

        String type = request.getParameter("type");
        String code = request.getParameter("code");
        String encryptedData = request.getParameter("encryptedData");
        String iv = request.getParameter("iv");
        String extendId = request.getParameter("extendId");
        if (StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(encryptedData)
                && StringUtils.isNotEmpty(iv) && StringUtils.isNotEmpty(type)) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            map.put("encryptedData", encryptedData);
            map.put("iv", iv);
            map.put("type", type);
            map.put("extendId", extendId);
            return wmCustomerInfoService.authTcMiniWxPhone(map);
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "参数不能为空", null);
        }
    }

    /**
     * 停车场小程序认证获取用户接口
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("loginTcMiniWxUser")
    public ReturnJsonData loginTcMiniWxUser(HttpServletRequest request, HttpServletResponse response)throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");

        String type = request.getParameter("type");
        String code = request.getParameter("code");
        String encryptedData = request.getParameter("encryptedData");
        String iv = request.getParameter("iv");
        String extendId = request.getParameter("extendId");
        if (StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(encryptedData)
                && StringUtils.isNotEmpty(iv) && StringUtils.isNotEmpty(type)) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            map.put("encryptedData", encryptedData);
            map.put("iv", iv);
            map.put("type", type);
            map.put("extendId", extendId);
            return wmCustomerInfoService.loginTcMiniWxUser(map);
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "参数不能为空", null);
        }
    }

    /**
     * 商家端微信登录
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("authTcSellerMiniWxPhone")
    public ReturnJsonData authTcSellerMiniWxPhone(HttpServletRequest request, HttpServletResponse response)throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");

        String type = request.getParameter("type");
        String code = request.getParameter("code");
        String encryptedData = request.getParameter("encryptedData");
        String iv = request.getParameter("iv");
        if (StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(encryptedData)
                && StringUtils.isNotEmpty(iv) && StringUtils.isNotEmpty(type)) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            map.put("encryptedData", encryptedData);
            map.put("iv", iv);
            map.put("type", type);
            return wmCustomerInfoService.authTcSellerMiniWxPhone(map);
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "参数不能为空", null);
        }
    }

}
