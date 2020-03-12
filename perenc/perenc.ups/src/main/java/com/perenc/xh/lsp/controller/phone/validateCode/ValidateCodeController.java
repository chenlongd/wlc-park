package com.perenc.xh.lsp.controller.phone.validateCode;


import com.perenc.xh.commonUtils.model.PhoneReturnJson;
import com.perenc.xh.commonUtils.utils.captcha.Captcha;
import com.perenc.xh.commonUtils.utils.captcha.SpecCaptcha;
import com.perenc.xh.commonUtils.utils.redis.JedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping("api")
public class ValidateCodeController {


    @ResponseBody
    @RequestMapping("/getCaptcha")
    public PhoneReturnJson getCaptcha(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        // 设置响应的类型格式为图片格式
        //response.setContentType("image/png");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        specCaptcha.setCharType(Captcha.TYPE_ONLY_CHAR);
        // 验证码存入session
        //request.getSession().setAttribute("imgCodeCaptcha", specCaptcha.text().toLowerCase());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        JedisUtil.setString(uuid, specCaptcha.text().toLowerCase());
        // 输出图片流
        try {
            //specCaptcha.out(response.getOutputStream());
            String soutpath = "d:/logs/code/outputcode.png";
            // linux下
            if ("/".equals(File.separator)) {
                soutpath = "/home/project/xiaohui/phone/apache-tomcat-8.5.23/webapps/bddata/upload/imgcode/"+uuid+".png";
            }
            String imgurl = "http://www.wlczmsc.com/bddata/upload/imgcode/"+uuid+".png";
            specCaptcha.out(new FileOutputStream(new File(soutpath)));
            Map<String,Object> map = new HashMap<>();
            map.put("uuid",uuid);
            map.put("imageurl",imgurl);
            return new PhoneReturnJson(true,"获取成功",map);
        } catch (IOException e) {
            e.printStackTrace();
            return new PhoneReturnJson(false,"请重试获取失败",null);
        }
    }

    @ResponseBody
    @RequestMapping("/validateCaptcha")
    public PhoneReturnJson validateCaptcha(HttpServletRequest request, HttpServletResponse response,HttpSession session) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String uuid = ServletRequestUtils.getStringParameter(request, "uuid", "");
        String code = ServletRequestUtils.getStringParameter(request, "code", "");
        try {
            if(StringUtils.isEmpty(code)){
                return new PhoneReturnJson(false,"传入的验证码为空",null);
            }
            if(StringUtils.isEmpty(uuid)){
                return new PhoneReturnJson(false,"传入的用户标识为空",null);
            }
            // 获取session中的验证码
            //String sessionCode = (String) request.getSession().getAttribute("imgCodeCaptcha");
            String sessionCode =JedisUtil.getString(uuid);
            if(sessionCode==null) {
                return new PhoneReturnJson(false,"请重新生成验证码",null);
            }
            // 判断验证码
            if (code!=null && sessionCode.equals(code.trim().toLowerCase())) {
                return new PhoneReturnJson(true,"验证码正确",null);
            }else {
                return new PhoneReturnJson(false,"验证码不正确",null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new PhoneReturnJson(false,"请重试验证失败",null);
        }
    }











}
