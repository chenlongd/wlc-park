package com.perenc.xh.lsp.controller.phone.phoneCode;


import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.service.phoneCode.PhoneCodeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("api/phone")
public class PhoneCodeController {


    @Autowired(required = false)
    private PhoneCodeService phoneCodeService;


    @ResponseBody
    @RequestMapping("/getCode")
    public ReturnJsonData getCode(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String phone = ServletRequestUtils.getStringParameter(request, "phone", "");
        try {
            if(StringUtils.isEmpty(phone)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入手机号",null);
            }
            if(StringUtils.isNotEmpty(phone)) {
                if(phone.length()!=11) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的手机号",null);
                }
                if(!ValidateUtils.isPhoneCheck(phone)) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的手机号",null);
                }
                Map<String,Object> condition = new HashMap<>();
                condition.put("phone",phone);
                return phoneCodeService.getPhoneCode(condition);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请重试网络连接不稳定",null);
        }
        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请重试短信发送失败",null);
    }


    @ResponseBody
    @RequestMapping("/validateCode")
    public ReturnJsonData validateCode(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String phone = ServletRequestUtils.getStringParameter(request, "phone", "");
        String code = ServletRequestUtils.getStringParameter(request, "code", "");
        try {
            if(StringUtils.isEmpty(phone)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入手机号",null);
            }
            if(StringUtils.isEmpty(code)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入手机验证码",null);
            }
            if(StringUtils.isNotEmpty(phone) && StringUtils.isNotEmpty(code)) {
                if(phone.length()!=11) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的手机号",null);
                }
                if(!ValidateUtils.isPhoneCheck(phone)) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的手机号",null);
                }
                if(code.length()!=6) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的验证码",null);
                }
                if(!ValidateUtils.isSixNumber(code)) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的验证码",null);
                }
                Map<String,Object> condition = new HashMap<>();
                condition.put("phone",phone);
                condition.put("code",code);
                return phoneCodeService.getPhoneValidate(condition);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请重试网络连接不稳定",null);
        }
        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请重试输入验证失败",null);
    }




}
