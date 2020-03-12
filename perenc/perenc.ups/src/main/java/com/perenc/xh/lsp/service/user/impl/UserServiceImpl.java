package com.perenc.xh.lsp.service.user.impl;


import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.DBRelevant.InsertParam;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.utils.StringOrDate.MD5Utils;
import com.perenc.xh.commonUtils.utils.StringOrRsa.RSAUtils;
import com.perenc.xh.lsp.dao.role.RoleDao;
import com.perenc.xh.lsp.dao.user.UserDao;
import com.perenc.xh.lsp.entity.role.Role;
import com.perenc.xh.lsp.entity.user.User;
import com.perenc.xh.lsp.service.user.UserService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
@Transactional(rollbackFor=Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserDao userDao;


    @Autowired(required = false)
    private RoleDao wmRoleDao;

    /*@Override
    public User userLoginPage(Map<String, Object> param) throws Exception {
        int loginType = MapUtils.getIntValue(param, "loginType",0);
        if(loginType == 0) {
            String username = MapUtils.getString(param, "username");
            String password = MapUtils.getString(param, "password");
            QueryParam condition = new QueryParam();
            condition.put("username", username);
            condition.put("password", password);
            return userDao.getOne(condition);
        }else{
            String account = MapUtils.getString(param, "account");
            String password = MapUtils.getString(param, "password");
            QueryParam condition = new QueryParam();
            condition.put("account", account);
            condition.put("password", password);
            return userDao.getOne(condition);
        }
    }*/

    /**
     * RSA登录
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public User userLoginPage(Map<String, Object> param) throws Exception {
        String username = MapUtils.getString(param, "username");
        String password = MapUtils.getString(param, "password");
        QueryParam condition = new QueryParam();
        condition.put("username", username);
        condition.put("password", password);
        return  userDao.getOne(condition);
    }

    @Override
    public ReturnJsonData insertUser(User user) throws Exception{
        //判断用户名是否存在
        QueryParam paramu = new QueryParam();
        paramu.addCondition("username","=",user.getUsername());
        User returnUsName = userDao.getOne(paramu);
        if(returnUsName!=null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"用户名已存在请重新输入",null);
        }
        //先查询之前是否有没有
        QueryParam param = new QueryParam();
        param.put("phone",user.getPhone());
        User returnUser = userDao.getOne(param);
        if(returnUser == null) {
            //没有再添加
            InsertParam ip = DBUtil.toInsertParam(user);
            int flag = userDao.add(ip);
            if (flag > 0) {
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加成功", null);
            } else {
                return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE, "添加失败", null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "您已经添加过了，请更换电话号码", null);
        }
    }

    @Override
    public ReturnJsonData updateUser(User user) {
        User returnUser = userDao.getById(user.getId());
        if(returnUser != null) {
            user.setCreateTime(returnUser.getCreateTime());
            int i = userDao.update(DBUtil.toUpdateParam(user, "id"));
            if (i > 0) {
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "修改成功", null);
            } else {
                return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE, "修改失败", null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该账号不存在",null);
        }
    }

    @Override
    public ReturnJsonData updateMapperUserPwd(User mp) {
        User user = userDao.getById(mp.getId());
        user.setPassword(mp.getPassword());
        int i = userDao.update(DBUtil.toUpdateParam(user, "id"));
        if (i > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "修改成功", null);
        } else{
            return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,  "修改失败", null);
        }
    }

    /*修改密码*/
    @Override
    public ReturnJsonData updatePWd(Map<String,Object> param){
        String userId = MapUtils.getString(param, "userID", "");
        String newPassword = MapUtils.getString(param, "newPassword", "");
        String password = MapUtils.getString(param, "password", "");
        try {
        User userResult = userDao.getById(userId);
        if(userResult != null){
            //String pwd = MD5Utils.MD5Encode(password, "utf8");
            //String newPwd = MD5Utils.MD5Encode(newPassword, "utf8");
            String pwd = RSAUtils.encryptByPrivate(password);
            String newPwd = RSAUtils.encryptByPrivate(newPassword);
            if(!pwd.equals(userResult.getPassword())){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"原始密码错误！",null);
            }else{
                if(newPwd.equals(userResult.getPassword())){
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"新密码与旧密码相同！",null);
                }else{
                    userResult.setPassword(newPwd);
                    Integer update = userDao.update(DBUtil.toUpdateParam(userResult, "id"));
                    if(update > 0){
                        return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功！",null);
                    }else{
                        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败！",null);
                    }
                }
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"用户不存在！",null);
        }
        }catch (Exception e) {
            System.out.println("执行错误"+e.getMessage());
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    @Override
    public ReturnJsonData mapperUserAll(QueryParam param) {
        param.setQueryall(true);//设置查询所有列表
        List<User> list = userDao.list(param);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        if(null!=list&&list.size()>0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "获取成功", map);
        }else{
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "获取失败", null);
        }

    }

    @Override
    public ReturnJsonData mapperUserList(QueryParam param) {
        List<User> list = userDao.list(param);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        return new ReturnJsonData(DataCodeUtil.SUCCESS, "获取成功", map);
    }

    @Override
    public User getUserById(int id) {
        return userDao.getById(id);
    }

    @Override
    public List<Map<String,Object>> getUserList(QueryParam param) {
        param.put("status",1);
        List<User> users = userDao.list(param);
        int count = userDao.count(param);
        List<Map<String,Object>> userList = new ArrayList<>();
        for(User user : users){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",user.getId());
            condition.put("username",user.getUsername());
            condition.put("headImageUrl",user.getHeadImageUrl());
            condition.put("sex",user.getSex());
            condition.put("phone",user.getPhone());
            condition.put("password",user.getPassword());
            condition.put("email",user.getEmail());
            condition.put("showType",user.getShowType());
            condition.put("storeId",user.getStoreId());
            condition.put("createTime",user.getCreateTime());
            condition.put("count",count);
            userList.add(condition);
        }
        return  userList;
    }

    @Override
    public Role getUserRole(QueryParam param) {
        List<Role> list = wmRoleDao.list(param);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 逻辑删除用户
     * @param id
     * @return
     */
    @Override
    public ReturnJsonData removeUser(int id) {
        User user = userDao.getById(id);
        if(user != null){
            user.setStatus(0);
            int flag = userDao.update(DBUtil.toUpdateParam(user, "id"));
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"删除用户成功",null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE,"删除用户失败",null);
    }

    @Override
    public ReturnJsonData resetPassword(int id) {
       try {
           User user = userDao.getById(id);
           if(user != null){
               //user.setPassword(MD5Utils.MD5Encode("123456", "utf8"));
               user.setPassword(RSAUtils.encryptByPrivate("123456"));
               int flag = userDao.update(DBUtil.toUpdateParam(user, "id"));
               if(flag > 0){
                   return new ReturnJsonData(DataCodeUtil.SUCCESS,"重置密码成功",null);
               }
           }
       }catch (Exception e) {
           System.out.println("执行错误"+e.getMessage());
       }
       return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"重置密码失败",null);
    }
}
