package com.perenc.xh.lsp.controller.phone.tcSeller;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcSeller.TcSeller;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerUser;
import com.perenc.xh.lsp.service.tcSeller.TcSellerService;
import com.perenc.xh.lsp.service.tcSellerUser.TcSellerUserService;
import com.perenc.xh.lsp.service.userToken.UserTokenService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("api/sellerUser")
public class TcSellerUserController {

    @Autowired(required = false)
    private TcSellerService tcSellerService;

    @Autowired(required = false)
    private TcSellerUserService tcSellerUserService;

    @Autowired(required = false)
    private UserTokenService userTokenService;


    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="商家用户",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String sellerId = ServletRequestUtils.getStringParameter(request,"sellerId","");
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        String password = ServletRequestUtils.getStringParameter(request,"password","");
        if(StringUtils.isEmpty(sellerId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家ID为空",null);
        }
        if(StringUtils.isEmpty(phone)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的手机号为空",null);
        }
        if(StringUtils.isEmpty(password)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的密码为空",null);
        }
        //判断手机号
        if(StringUtils.isNotEmpty(phone)) {
            if (phone.length() != 11) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "手机号应为11位数", null);
            }
            if (!ValidateUtils.isPhoneCheck(phone)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请输入正确的手机号", null);
            }
        }
        //判断密码
        if(!ValidateUtils.isValidatePassword(password)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"密码只能输入6-18位字母或数字",null);
        }
        Map<String, Object> condition = new HashMap<>();
        TcSellerUser tcSellerUser=new TcSellerUser();
        tcSellerUser.setPhone(phone);
        tcSellerUser.setPassword(password);
        tcSellerUser.setSellerId(Integer.valueOf(sellerId));
        return tcSellerUserService.insertSellerUser(tcSellerUser,condition);
    }

    /**
     * 用户登录
     * @param request
     * @param response
     * @param username
     * @param password
     * @throws Exception
     */
    @RequestMapping("/login")
    @ResponseBody
    @OperLog(operationType="商家用户",operationName="用户登录")
    public ReturnJsonData login(HttpServletRequest request, HttpServletResponse response, String username, String password) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        if(StringUtils.isEmpty(username)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的用户名为空",null);
        }

        if(StringUtils.isEmpty(password)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的密码为空",null);
        }
        //处理业务逻辑
       try {
           Map<String, Object> param = new HashMap<>();
           param.put("username", username);
           param.put("password", password);
           TcSellerUser tcSellerUser = tcSellerUserService.sellerLogin(param);
           if (tcSellerUser != null) {
               if (tcSellerUser.getIsEnabled().equals(2)) {
                   return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该用户已禁用", null);
               }
               TcSeller tcSeller = tcSellerService.findById(tcSellerUser.getSellerId());
               if (tcSeller==null) {
                   return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该商家不存在", null);
               }
               param.clear();
               param.put("id",tcSellerUser.getId());
               param.put("sellerId",tcSeller.getId());
               param.put("logo", tcSeller.getLogo());
               param.put("phone", tcSeller.getPhone());
               param.put("name", tcSeller.getName());
               param.put("isApproval",tcSeller.getIsApproval());
               param.put("type", tcSeller.getType());
               param.put("isMaster", tcSellerUser.getIsMaster());
               param.put("token", userTokenService.updateUsrToken(0, tcSeller.getId(), 2, "商家端"));
               return new ReturnJsonData(DataCodeUtil.SUCCESS, "登录成功", param);

           } else {
               return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "账号或密码不正确", null);
           }
       }catch (Exception e) {
           return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "登录失败", null);
       }
    }

    /**
     * 用户登录
     * @param request
     * @param response
     * @param phone
     * @param password
     * @throws Exception
     */
    @RequestMapping("/phoneLogin")
    @ResponseBody
    public ReturnJsonData phoneLogin(HttpServletRequest request, HttpServletResponse response, String phone, String password) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        if(StringUtils.isEmpty(phone)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入手机号",null);
        }
        if(StringUtils.isEmpty(password)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入密码",null);
        }
        if(phone.length()!=11) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的手机号",null);
        }
        if(!ValidateUtils.isPhoneCheck(phone)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的手机号",null);
        }
        //处理业务逻辑
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("phone", phone);
            param.put("password", password);
           return tcSellerUserService.phonePasswordLogin(param);
        }catch (Exception e) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "登录失败", null);
        }
    }

    /**
     * 注销当前登录
     *
     * @param request
     * @return
     */
    @RequestMapping("/loginOut")
    @ResponseBody
    public ReturnJsonData loginOut(HttpServletRequest request,HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String sellerUserId = ServletRequestUtils.getStringParameter(request,"sellerUserId","");
        if(StringUtils.isEmpty(sellerUserId)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家Id为空",null);
        }
        Map<String, Object> param = new HashMap<>();
        param.put("sellerUserId", sellerUserId);
        return tcSellerUserService.loginOut(param);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="商家用户",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        String province = ServletRequestUtils.getStringParameter(request,"province","");
        String city = ServletRequestUtils.getStringParameter(request,"city","");
        String county = ServletRequestUtils.getStringParameter(request,"country","");
        String address = ServletRequestUtils.getStringParameter(request,"address","");
        String headImage = ServletRequestUtils.getStringParameter(request,"headImage","");
        String sex = ServletRequestUtils.getStringParameter(request,"sex","");
        String nickname = ServletRequestUtils.getStringParameter(request,"nickname","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主Id为空",null);
        }
        //更改其它一项数据
        TcSellerUser tcSellerUser=new TcSellerUser();
        tcSellerUser.setId(Integer.valueOf(id));
        tcSellerUser.setPhone(phone);
        tcSellerUser.setProvinceId(province);
        tcSellerUser.setCityId(city);
        tcSellerUser.setCountyId(county);
        tcSellerUser.setAddress(address);
        tcSellerUser.setHeadImage(headImage);
        if(StringUtils.isNotEmpty(sex)) {
            tcSellerUser.setSex(Integer.valueOf(sex));
        }
        tcSellerUser.setNickname(nickname);
        return tcSellerUserService.updateOne(tcSellerUser);
    }


    /**
     * 修改禁用状态
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateIsEnabled")
    @ResponseBody
    @OperLog(operationType="商家用户",operationName="修改禁用状态")
    public ReturnJsonData updateIsEnabled(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String isEnabled = ServletRequestUtils.getStringParameter(request,"isEnabled","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主Id为空",null);
        }
        if(StringUtils.isEmpty(isEnabled)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的禁用状态为空",null);
        }
        //更改其它一项数据
        TcSellerUser tcSellerUser=new TcSellerUser();
        tcSellerUser.setId(Integer.valueOf(id));
        tcSellerUser.setIsEnabled(Integer.valueOf(isEnabled));
        return tcSellerUserService.updateIsEnabled(tcSellerUser);
    }



    /**
     * 修改密码
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("updatePassword")
    @ResponseBody
    @OperLog(operationType="商家",operationName="修改密码")
    public ReturnJsonData updatePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request, "id");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家Id为空",null);
        }
        String password = ServletRequestUtils.getStringParameter(request, "password");
        if(StringUtils.isEmpty(password)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的旧密码为空",null);
        }
        String newPassword = ServletRequestUtils.getStringParameter(request, "newPassword");
        if(StringUtils.isEmpty(newPassword)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的新密码为空",null);
        }
        //判断密码
        if(!ValidateUtils.isValidatePassword(newPassword)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"密码只能输入6-18位字母或数字",null);
        }
        Map<String,Object> condition = new HashMap<>();
        condition.put("password",password);
        condition.put("newPassword",newPassword);
        condition.put("sellerUserId",id);
        return tcSellerUserService.updatePWd(condition);
    }


    /**
     * 忘记密码
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/forgetPassword")
    @ResponseBody
    public ReturnJsonData forgetPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String phone = ServletRequestUtils.getStringParameter(request, "phone");
        String code = ServletRequestUtils.getStringParameter(request, "code");
        String password = ServletRequestUtils.getStringParameter(request, "password");
        String conPassword = ServletRequestUtils.getStringParameter(request, "conPassword");
        if(StringUtils.isEmpty(phone)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入手机号",null);
        }
        if(StringUtils.isEmpty(code)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入手机验证码",null);
        }
        if(StringUtils.isEmpty(password)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入密码",null);
        }
        if(StringUtils.isEmpty(conPassword)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入确认密码",null);
        }
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
        if(!ValidateUtils.isValidatePassword(password)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"密码只能输入6-18位字母或数字",null);
        }
        if(!ValidateUtils.isValidatePassword(conPassword)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"密码只能输入6-18位字母或数字",null);
        }
        //处理业务逻辑
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("phone", phone);
            param.put("code", code);
            param.put("password", password);
            param.put("conPassword", conPassword);
            return tcSellerUserService.forgetPassword(param);
        }catch (Exception e) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "修改失败", null);
        }
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="商家用户",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        List<Integer> list =new ArrayList<>();
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            for (int i = 0; i < strarray.length; i++){
                list.add(Integer.parseInt(strarray[i]));
            }
        }
        if(list!=null && list.size()>0){
            return tcSellerUserService.deleteBatch(list);
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入Id为空",null);
        }
    }


    /**
     * 根据Id查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/getById")
    @OperLog(operationType="商家用户",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcSellerUserService.getById(Integer.valueOf(id));
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入Id为空",null);
        }
    }


    /**
     * 后台列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    @ResponseBody
    @OperLog(operationType="商家用户",operationName="列表查询")
    public ReturnJsonData list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        String phone = ServletRequestUtils.getStringParameter(request, "phone", "");
        if (StringUtils.isNotEmpty(phone)) {
            condition.put("phone", phone);
        }
        String sellerId = ServletRequestUtils.getStringParameter(request, "sellerId", "");
        if (StringUtils.isNotEmpty(sellerId)) {
            condition.put("sellerId", sellerId);
        }
        String isEnabled = ServletRequestUtils.getStringParameter(request, "isEnabled", "");
        if (StringUtils.isNotEmpty(isEnabled)) {
            condition.put("isEnabled", isEnabled);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcSellerUserService.getList(condition,pageHelper);
    }
}
