package com.perenc.xh.lsp.controller.admin.user;


import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.DataGrid;
import com.perenc.xh.commonUtils.model.PageHelper;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.StringOrDate.MD5Utils;
import com.perenc.xh.commonUtils.utils.StringOrRsa.RSAUtils;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.user.User;
import com.perenc.xh.lsp.entity.userRole.UserRole;
import com.perenc.xh.lsp.service.menu.MenuService;
//import com.perenc.xh.lsp.service.store.StoreService;
import com.perenc.xh.lsp.service.user.UserService;
import com.perenc.xh.lsp.service.userRole.UserRoleService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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

;

@Controller
@RequestMapping("user")
public class UserController {

    private static Logger logger = Logger.getLogger("UserController.class");

    private static String password = "123456";

    @Autowired(required = false)
    private UserService userService;

    @Autowired(required = false)
    private MenuService wmMenuService;

    @Autowired(required = false)
    private UserRoleService wmUserRoleService;



    /**
     * 用户登录
     * @param request
     * @param response
     * @param username
     * @param password
     * @throws Exception
     */
    /*@RequestMapping("/loginPage")
    @ResponseBody
    public ReturnJsonData loginPage(HttpServletRequest request, HttpServletResponse response, String username, String password) throws Exception {
        //判断收集的参数是否为空
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
            String md5Passeord = MD5Utils.MD5Encode(password, "utf8");
            param.put("password", md5Passeord);
            User user = userService.userLoginPage(param);
            if (null != user) {
                if ("0".equals(user.getStatus() + "")) {
                    request.getSession().setAttribute("user", user);
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该用户已停止使用", null);
                } else {
                    param.clear();
                    param.put("userId", user.getId());
                    List list = wmMenuService.getUserMenu(param);
                    user.setUserType(user.getUserType());
                    request.getSession().setAttribute("user", user);
                    logger.info("------用户id-------" + request.getSession().getId());
                    request.getSession().setAttribute("grantSessionList", list);
                    param.clear();
                    param.put("headImageUrl", user.getHeadImageUrl());
                    param.put("username", user.getUsername());
                    return new ReturnJsonData(DataCodeUtil.SUCCESS, "登陆成功", param);
                }
            } else {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "账号或密码不对", null);
            }
        } catch (Exception e) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "网络连接超时，请稍后再试", null);
        }

    }*/


    /**
     * RSA用户登录
     * @param request
     * @param response
     * @param username
     * @param password
     * @throws Exception
     */
    @RequestMapping("/loginPage")
    @ResponseBody
    public ReturnJsonData loginPage(HttpServletRequest request, HttpServletResponse response, String username, String password) throws Exception {
        //判断收集的参数是否为空
        if(StringUtils.isEmpty(username)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的用户名为空",null);
        }

        if(StringUtils.isEmpty(password)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的密码为空",null);
        }
        if(password.length()>=117){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的密码过长",null);
        }
        //处理业务逻辑
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("username", username);
            //String md5Passeord = MD5Utils.MD5Encode(password, "utf8");
            //param.put("password", md5Passeord);
            String rsaPasseord = RSAUtils.encryptByPrivate(password);
            param.put("password", rsaPasseord);
            User user = userService.userLoginPage(param);
            if (null != user) {
                if ("0".equals(user.getStatus() + "")) {
                    request.getSession().setAttribute("user", user);
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该用户已停止使用", null);
                } else {
                    param.clear();
                    param.put("userId", user.getId());
                    List list = wmMenuService.getUserMenu(param);
                    user.setUserType(user.getUserType());
                    request.getSession().setAttribute("user", user);
                    logger.info("------用户id-------" + request.getSession().getId());
                    request.getSession().setAttribute("grantSessionList", list);
                    param.clear();
                    param.put("headImageUrl", user.getHeadImageUrl());
                    param.put("username", user.getUsername());
                    return new ReturnJsonData(DataCodeUtil.SUCCESS, "登陆成功", param);
                }
            } else {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "账号或密码不对", null);
            }
        } catch (Exception e) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "网络连接超时，请稍后再试", null);
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
        User user = (User) request.getSession().getAttribute("user");
        if (null != user) {
            request.getSession().removeAttribute(user.getId() + "");
            request.getSession().invalidate();

            return new ReturnJsonData(DataCodeUtil.SUCCESS,"注销成功",null);
        } else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"您还未登录",null);
        }
    }




    /**
     * 跳转用户添加页面
     * @param request
     * @param response
     * @return
     */
//    @RequestMapping("add")
//    public String add(HttpServletRequest request,HttpServletResponse response){
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");
//
//        //跳转添加页面
//        return "redirect:"+ PropertiesGetValue.getProperty("PAGE.URL")+"manager_add.html";
//    }

    /**
     * 跳转用户更新页面
     * @param request
     * @param response
     * @return
     */
//    @RequestMapping("update")
//    public String update(HttpServletRequest request,HttpServletResponse response){
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");
//
//        //获取更新用户id
//        String id = request.getParameter("id");
//        //跳转更新页面
//        return "redirect:"+ PropertiesGetValue.getProperty("PAGE.URL")+"manager_update.html?userId="+id;
//    }

    /**
     * 获取用户列表
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getUserList")
    @ResponseBody
    public ReturnJsonData getUserList(HttpServletRequest request, HttpServletResponse response) throws Exception{

        //条件筛选

        //商家登录
        int showType = ServletRequestUtils.getIntParameter(request, "showType", 0);
        //用户名
        String username = ServletRequestUtils.getStringParameter(request, "username", "");
        //电话号码
        String phone = ServletRequestUtils.getStringParameter(request, "phone", "");
        //邮箱
        String email = ServletRequestUtils.getStringParameter(request, "email", "");
        //页数
        int pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
        //条数
        int rows = ServletRequestUtils.getIntParameter(request, "rows", 1);
        PageHelper page = new PageHelper();
        page.setPage(pageNo);
        page.setRows(rows);
        DataGrid dataGrid = new DataGrid();
        QueryParam param = new QueryParam();
        param.setPageNum(page.getPage());
        param.setPageSize(page.getRows());
        if(StringUtils.isNotEmpty(username)){
            param.addCondition("username","like","%"+username+"%");
        }
        if(StringUtils.isNotEmpty(phone)){
            param.addCondition("phone","like","%"+phone+"%");
        }
        if(StringUtils.isNotEmpty(email)){
            param.addCondition("email","like","%"+email+"%");
        }
        if(showType == 1){
            param.addCondition("show_type","=",1);
        }
        List<Map<String,Object>> userList = userService.getUserList(param);
        dataGrid.setRows(userList);
        if(userList.size() <= 0){
            dataGrid.setTotal(0);
        }else {
            dataGrid.setTotal(Long.valueOf(String.valueOf(userList.get(0).get("count"))));
        }

        dataGrid.setPageNo(pageNo);
        dataGrid.setPageSize(rows);
        Map<String,Object> condition = new HashMap<>();
        condition.put("userList",dataGrid);
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
    }

    @RequestMapping("addUser")
    @ResponseBody
    public ReturnJsonData addUser(HttpServletRequest request,HttpServletResponse response,User user,String storeId) throws Exception{

        //获取数据并封装数据
        String username = ServletRequestUtils.getStringParameter(request, "username", "");
        if(StringUtils.isNotEmpty(username)){
            user.setUsername(username);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的用户名为空",null);
        }
        String headImageUrl = ServletRequestUtils.getStringParameter(request, "headImageUrl", "");
        if(StringUtils.isNotEmpty(headImageUrl)){
            user.setHeadImageUrl(headImageUrl);
        }
        user.setSex(ServletRequestUtils.getIntParameter(request, "sex", 0));
        String phone = ServletRequestUtils.getStringParameter(request, "phone", "");
        if(StringUtils.isNotEmpty(phone)){
            user.setPhone(phone);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的电话号码为空",null);
        }

        String pwd = phone.substring(phone.length() - 6);
        //String encodePassword = MD5Utils.MD5Encode(pwd, "utf8");
        //user.setPassword(encodePassword);
        String encodePassword = RSAUtils.encryptByPrivate(pwd);
        user.setPassword(encodePassword);

        String email = ServletRequestUtils.getStringParameter(request, "email", "");
        if(StringUtils.isNotEmpty(email)) {
            user.setEmail(email);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的邮箱为空",null);
        }

        if(user.getShowType() == 1){
            if(StringUtils.isNotEmpty(storeId)) {
                user.setStoreId(storeId);
            }else{
                return new ReturnJsonData(DataCodeUtil.FALSE,"传入的店铺ID为空",null);
            }
        }
        user.setAccount(phone);
        user.setUserType(1);
        user.setStatus(1);
        user.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        return userService.insertUser(user);
    }

    @RequestMapping("updateUser")
    @ResponseBody
    public ReturnJsonData updateUser(HttpServletRequest request,HttpServletResponse response) throws Exception{

        User user = new User();
        String userId = ServletRequestUtils.getStringParameter(request, "userId","");
        if(StringUtils.isNotEmpty(userId)){
            user.setId(Integer.valueOf(userId));
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的账号ID为空",null);
        }
        String username = ServletRequestUtils.getStringParameter(request, "username", "");
        if(StringUtils.isNotEmpty(username)){
            user.setUsername(username);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的用户名为空",null);
        }
        String headImageUrl = ServletRequestUtils.getStringParameter(request, "headImageUrl", "");
        if(StringUtils.isNotEmpty(headImageUrl)){
            user.setHeadImageUrl(headImageUrl);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的头像为空",null);
        }
        user.setSex(ServletRequestUtils.getIntParameter(request, "sex", 0));
        String phone = ServletRequestUtils.getStringParameter(request, "phone", "");
        if(StringUtils.isNotEmpty(phone)){
            user.setPhone(phone);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的电话为空",null);
        }
        /*String password = ServletRequestUtils.getStringParameter(request, "password", "");
        if(StringUtils.isNotEmpty(password)) {
            //判断密码
            if(!ValidateUtils.isValidatePassword(password)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"密码只能输入6-18位字母或数字",null);
            }
            //user.setPassword(MD5Utils.MD5Encode(password, "utf8"));
            user.setPassword(RSAUtils.encryptByPrivate(password));
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的密码为空",null);
        }*/
        String email = ServletRequestUtils.getStringParameter(request, "email", "");
        if(StringUtils.isNotEmpty(email)) {
            user.setEmail(email);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的邮箱为空",null);
        }
        user.setUserType(1);
        user.setStatus(1);
        return userService.updateUser(user);
    }

    @ResponseBody
    @RequestMapping("/getUserInfo")
    public ReturnJsonData getUserInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {

        String id = request.getParameter("userId");
        if(StringUtils.isNotEmpty(id)) {
            User user = userService.getUserById(Integer.valueOf(id));
            Map<String,Object> param = new HashMap<>();
            param.put("user",user);
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",param);
        }
        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"获取数据失败",null);
    }

    /**
     * 用户授权
     */
//    @RequestMapping("/authRoles")
//    public String toAssignRole(HttpServletRequest request,HttpServletResponse response) {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");
//
//        //获取用户ID
//        String userId = request.getParameter("userId");
//        //跳转授权页面
//        return "redirect:"+ PropertiesGetValue.getProperty("PAGE.URL")+"manager_authRoles.html?userId="+userId;
//    }

    /**
     * 给管理员分配角色
     * @param request
     * @throws Exception
     */
    @RequestMapping("/addUserRole")
    @ResponseBody
    public ReturnJsonData addUserRole(HttpServletRequest request,HttpServletResponse response) {


        String userId = ServletRequestUtils.getStringParameter(request,"userId","");
        if(StringUtils.isEmpty(userId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的账号为空",null);
        }
        List<UserRole> userRoleList = new ArrayList<>();
        String roleIds = ServletRequestUtils.getStringParameter(request,"roleIds","");
        if(StringUtils.isNotEmpty(roleIds)){
            String[] roleId = roleIds.split(",");
            if(roleId != null && roleId.length > 0){
                for(int i=0;i<roleId.length;i++) {
                    UserRole userRole = new UserRole();
                    userRole.setRoleId(Integer.valueOf(roleId[i]));
                    userRole.setUserId(Integer.valueOf(userId));
                    userRoleList.add(userRole);
                }
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请选择角色",null);
        }
        return wmUserRoleService.addWmUserRoles(userRoleList);
    }

    /**
     * 根据用户id查询该用户角色信息
     * @param request
     * @return
     */
    @RequestMapping("/findRolesByUserId")
    @ResponseBody
    public ReturnJsonData findRolesByUserId(HttpServletRequest request,HttpServletResponse response) {

        String userId = ServletRequestUtils.getStringParameter(request,"userId","");
        if(StringUtils.isEmpty(userId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的账号为空",null);
        }
        QueryParam param = new QueryParam();
        param.put("user_id",userId);
        List<Map<String, Object>> userRoleList = wmUserRoleService.getWmUserRoleList(param);
        Map<String,Object> condition = new HashMap<>();
        condition.put("userRoleList",userRoleList);
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
    }

    /**
     * 获取当前用户信息
     * @param request
     * @param response
     * @param username
     * @param password
     * @throws Exception
     */
    @RequestMapping("/getCurrentUser")
    @ResponseBody
    public ReturnJsonData getCurrentUser(HttpServletRequest request, HttpServletResponse response, String username, String password) throws Exception {
        //处理业务逻辑
        try {
            Map<String,Object> result = new HashMap<>();
            User user =(User)request.getSession().getAttribute("user");
            if (user!=null) {
                result.put("userId",user.getId());
                result.put("headImageUrl",user.getHeadImageUrl());
                result.put("username",user.getUsername());
            } else {
                result.put("userId","");
                result.put("headImageUrl","");
                result.put("username","");
            }
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取成功",result);
        } catch (Exception e) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"获取失败",null);
        }
    }


    /**
     * 删除用户
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    public ReturnJsonData removeUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String id = ServletRequestUtils.getStringParameter(request, "id");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的用户id为空",null);
        }

        return userService.removeUser(Integer.valueOf(id));

    }

    /**
     * 重置密码
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("resetPassword")
    @ResponseBody
    public ReturnJsonData resetPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String id = ServletRequestUtils.getStringParameter(request, "id");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的用户id为空",null);
        }

        return userService.resetPassword(Integer.valueOf(id));

    }


    /**
     * 修改密码
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("updatePWD")
    @ResponseBody
    public ReturnJsonData updatePWD(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String password = ServletRequestUtils.getStringParameter(request, "password");
        if(StringUtils.isEmpty(password)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的老密码为空",null);
        }
        String newPassword = ServletRequestUtils.getStringParameter(request, "newPassword");
        if(StringUtils.isEmpty(newPassword)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的新密码为空",null);
        }
        User user =(User)request.getSession().getAttribute("user");
        if(user == null){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"登录已超时，请重新登录",null);
        }
        //判断密码
        if(!ValidateUtils.isValidatePassword(newPassword)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"密码只能输入6-18位字母或数字",null);
        }
        Map<String,Object> condition = new HashMap<>();
        condition.put("password",password);
        condition.put("newPassword",newPassword);
        condition.put("userID",user.getId());
        return userService.updatePWd(condition);
    }


}
