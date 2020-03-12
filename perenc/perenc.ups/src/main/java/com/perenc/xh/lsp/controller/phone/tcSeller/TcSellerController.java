package com.perenc.xh.lsp.controller.phone.tcSeller;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcSeller.TcSeller;
import com.perenc.xh.lsp.service.tcSeller.TcSellerService;
import com.perenc.xh.lsp.service.userToken.UserTokenService;
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
@RequestMapping("api/seller")
public class TcSellerController {

    @Autowired(required = false)
    private TcSellerService tcSellerService;

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
    @OperLog(operationType="商家",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String descp = ServletRequestUtils.getStringParameter(request,"descp","");
        String logo = ServletRequestUtils.getStringParameter(request,"logo","");
        String licenseImg = ServletRequestUtils.getStringParameter(request,"licenseImg","");
        String password = ServletRequestUtils.getStringParameter(request,"password","");
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        String code = ServletRequestUtils.getStringParameter(request,"code","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家名称为空",null);
        }
        if(StringUtils.isEmpty(licenseImg)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的营业执照为空",null);
        }
        if(StringUtils.isEmpty(password)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的密码为空",null);
        }
        if(StringUtils.isEmpty(phone)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的手机号为空",null);
        }
        if(StringUtils.isEmpty(code)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的验证码为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家类型为空",null);
        }
        //判断密码
        if(!ValidateUtils.isValidatePassword(password)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"密码只能输入6-18位字母或数字",null);
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
        Map<String, Object> condition = new HashMap<>();
        condition.put("code",code);
        TcSeller tcSeller=new TcSeller();
        tcSeller.setName(name);
        tcSeller.setDescp(descp);
        tcSeller.setLogo(logo);
        tcSeller.setLicenseImg(licenseImg);
        tcSeller.setPassword(password);
        tcSeller.setPhone(phone);
        tcSeller.setType(Integer.valueOf(type));
        if(StringUtils.isNotEmpty(extendId)) {
            tcSeller.setExtendId(Integer.valueOf(extendId));
        }else {
            tcSeller.setExtendId(null);
        }
        tcSeller.setIsApproval(1);
        tcSeller.setIsWork(1);
        return tcSellerService.insertCheckCode(tcSeller,condition);
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
    @OperLog(operationType="商家",operationName="用户登录")
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
           TcSeller tcSeller = tcSellerService.sellerLogin(param);
           if (tcSeller != null) {
               if(!tcSeller.getIsApproval().equals(2)) {
                   return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该商家未审核通过", null);
               }
               if (tcSeller.getIsWork().equals(2)) {
                   return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该商家已经停止营业", null);
               }
               request.getSession().setAttribute("seller", tcSeller);
               param.clear();
               param.put("id",tcSeller.getId());
               param.put("logo", tcSeller.getLogo());
               param.put("phone", tcSeller.getPhone());
               param.put("name", tcSeller.getName());
               param.put("isApproval", tcSeller.getIsApproval());
               param.put("type", tcSeller.getType());
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
     * @param code
     * @throws Exception
     */
    @RequestMapping("/phoneLogin")
    @ResponseBody
//    @OperLog(operationType="账号列表",operationName="用户登录")
    public ReturnJsonData phoneLogin(HttpServletRequest request, HttpServletResponse response, String phone, String code) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        if(StringUtils.isEmpty(phone)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入手机号",null);
        }
        if(StringUtils.isEmpty(code)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入手机验证码",null);
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
        //处理业务逻辑
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("phone", phone);
            param.put("code", code);
           return tcSellerService.phoneLogin(param);
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
        String sellerId = ServletRequestUtils.getStringParameter(request,"sellerId","");
        if(StringUtils.isEmpty(sellerId)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家Id为空",null);
        }
        Map<String, Object> param = new HashMap<>();
        param.put("sellerId", sellerId);
        return tcSellerService.loginOut(param);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="商家",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String descp = ServletRequestUtils.getStringParameter(request,"descp","");
        String logo = ServletRequestUtils.getStringParameter(request,"logo","");
        String licenseimg = ServletRequestUtils.getStringParameter(request,"licenseimg","");
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        String province = ServletRequestUtils.getStringParameter(request,"province","");
        String city = ServletRequestUtils.getStringParameter(request,"city","");
        String county = ServletRequestUtils.getStringParameter(request,"country","");
        String address = ServletRequestUtils.getStringParameter(request,"address","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主Id为空",null);
        }
        //更改其它一项数据
        TcSeller tcSeller=new TcSeller();
        tcSeller.setId(Integer.valueOf(id));
        tcSeller.setName(name);
        tcSeller.setDescp(descp);
        tcSeller.setLogo(logo);
        tcSeller.setLicenseImg(licenseimg);
        tcSeller.setPhone(phone);
        tcSeller.setProvinceId(province);
        tcSeller.setCityId(city);
        tcSeller.setCountyId(county);
        tcSeller.setAddress(address);
        return tcSellerService.updateOne(tcSeller);
    }

    /**
     * 重置密码
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("resetPassword")
    @ResponseBody
    @OperLog(operationType="商家",operationName="重置密码")
    public ReturnJsonData resetPassword(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家Id为空",null);
        }
        return tcSellerService.resetPassword(Integer.valueOf(id));
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
        /*TcSeller tcSeller =(TcSeller)request.getSession().getAttribute("seller");
        if(tcSeller == null){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"登录已超时，请重新登录",null);
        }*/

        Map<String,Object> condition = new HashMap<>();
        condition.put("password",password);
        condition.put("newPassword",newPassword);
        condition.put("sellerId",id);
        return tcSellerService.updatePWd(condition);
    }


    /**
     * 用户登录
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
            return tcSellerService.forgetPassword(param);
        }catch (Exception e) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "修改失败", null);
        }
    }


    /**
     * 根据Id查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/getById")
    @OperLog(operationType="商家",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcSellerService.getById(Integer.valueOf(id));
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
    @RequestMapping("page")
    @ResponseBody
    @OperLog(operationType="商家",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        String name = ServletRequestUtils.getStringParameter(request, "name", "");
        if (StringUtils.isNotEmpty(name)) {
            condition.put("name", name);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcSellerService.getList(condition,pageHelper);
    }
}
