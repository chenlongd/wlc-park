package com.perenc.xh.lsp.controller.phone.user;

import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.utils.StringOrDate.MD5;
import com.perenc.xh.lsp.entity.user.User;
import com.perenc.xh.lsp.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("api")
public class MapperUserController {

    @Autowired(required = false)
    private UserService userService;

//    @ResponseBody
//    @RequestMapping("addUser")
//    public ReturnJsonData delMapperUser(HttpServletRequest request, HttpServletResponse response)throws Exception{
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");
//
//        User user = new User();
//        user.setPassword("123456");
//        user.setPhone("12345678901");
//        user.setSex("男");
//        user.setUsername("小白");
//
//        return userService.insertUser(user);
//    }


    /**
     * 重置员工密码
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("edtStaffPassword")
    public ReturnJsonData edtMapperUser(HttpServletRequest request, HttpServletResponse response)throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String phone = request.getParameter("phone");
        if(StringUtils.isNotEmpty(phone)){
            String substring = phone.substring(phone.length() - 6, phone.length());
            String pwd = new MD5().hexString(substring, Charset.forName("UTF-8"));
            User user = new User();
            user.setPassword(pwd);
            return userService.updateMapperUserPwd(user);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"参数不能为空",null);
        }

   }


    @ResponseBody
    @RequestMapping("mapperUserAll")
    public ReturnJsonData mapperUserAll(HttpServletRequest request, HttpServletResponse response)throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        QueryParam param = new QueryParam();
        return userService.mapperUserAll(param);
    }

    @ResponseBody
    @RequestMapping("mapperUserList")
    public ReturnJsonData mapperUserList(HttpServletRequest request, HttpServletResponse response)throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        QueryParam param = new QueryParam();
        param.setPageNum(1);//初始当前页，从1开始。
        param.setPageSize(20);
        param.put("id",41);
        param.addCondition("phone", "like", "%1%");
        return userService.mapperUserList(param);
    }

    /*修改秘码*/
    @ResponseBody
    @RequestMapping("updatePassword")
    public ReturnJsonData updatePassword(HttpServletRequest request, HttpServletResponse response)throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String userID = request.getParameter("userID");
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isEmpty(userID)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"userID为空",null);
        }
        map.put("userID",userID);
        String newPassword = request.getParameter("newPassword");
        if(StringUtils.isEmpty(newPassword)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"新密码为空",null);
        }
        map.put("newPassword",newPassword);
        String password = request.getParameter("password");
        if(StringUtils.isEmpty(password)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"旧密码为空",null);
        }
        map.put("password",password);
        return userService.updatePWd(map);
    }

}
