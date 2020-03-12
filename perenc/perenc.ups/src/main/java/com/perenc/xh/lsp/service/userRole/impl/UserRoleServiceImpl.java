package com.perenc.xh.lsp.service.userRole.impl;

import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.DBRelevant.InsertParam;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.dao.UserRole.UserRoleDao;
import com.perenc.xh.lsp.entity.role.Role;
import com.perenc.xh.lsp.entity.user.User;
import com.perenc.xh.lsp.entity.userRole.UserRole;
import com.perenc.xh.lsp.service.role.RoleService;
import com.perenc.xh.lsp.service.user.UserService;
import com.perenc.xh.lsp.service.userRole.UserRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("wmUserRoleService")
@Transactional(rollbackFor = Exception.class)
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired(required = false)
    private UserRoleDao wmUserRoleDao;

    @Autowired(required = false)
    private UserService userService;

    @Autowired(required = false)
    private RoleService wmRoleService;

    @Override
    public ReturnJsonData insertWmUserRole(UserRole wmUserRole) {
        QueryParam queryParam = new QueryParam();
        queryParam.put("role_id",wmUserRole.getRoleId());
        queryParam.put("user_id",wmUserRole.getUserId());
        List<UserRole> userRoles = wmUserRoleDao.list(queryParam);
        if(userRoles != null && userRoles.size()>0){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"该用户和角色已经绑定!",null);
        }else{
            InsertParam param = DBUtil.toInsertParam(wmUserRole);
            int flag = wmUserRoleDao.add(param);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加成功", null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
    }

    @Override
    public ReturnJsonData updateWmUserRole(UserRole wmUserRole) {
        int flag = wmUserRoleDao.update(DBUtil.toUpdateParam(wmUserRole,"id"));
        if(flag>0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"更新成功",null);
        }else {
            return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"更新失败",null);
        }
    }

    @Override
    public ReturnJsonData delWmUserRole(int id) {
        int flag = wmUserRoleDao.delete(new Object[]{id});
        if(flag>0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"删除成功",null);
        }else {
            return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE,"删除失败",null);
        }
    }

    @Override
    public UserRole getWmUserRoleById(int id) {
        QueryParam param = new QueryParam();
        param.addCondition("id","=",id);
        return  wmUserRoleDao.getOne(param);
    }

    @Override
    public List<UserRole> getAllWmUserRole(QueryParam param) {
        return  wmUserRoleDao.list(param);
    }

    @Override
    public List<Map<String,Object>> getWmUserRoleList(QueryParam param) {
        List<UserRole> wmUserRoles = wmUserRoleDao.list(param);
        int count = wmUserRoleDao.count(param);
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        if(wmUserRoles != null && wmUserRoles.size()>0){
            for(UserRole wmUserRole : wmUserRoles){
                Map<String,Object> result = new HashMap<String,Object>();
                result.put("id",wmUserRole.getId());
                if(StringUtils.isNotEmpty(String.valueOf(wmUserRole.getUserId()))){
                    User user = userService.getUserById(wmUserRole.getUserId());
                    if(user != null){
                        result.put("username",user.getUsername());
                    }else{
                        result.put("username","");
                    }
                }
                if(StringUtils.isNotEmpty(String.valueOf(wmUserRole.getRoleId()))){
                    Role role = wmRoleService.getWmRoleById(wmUserRole.getRoleId());
                    if(role != null){
                        result.put("roleName",role.getRoleName());
                    }else{
                        result.put("roleName","");
                    }
                }
                result.put("userId",wmUserRole.getUserId());
                result.put("roleId",wmUserRole.getRoleId());
                result.put("count",count);
                mapList.add(result);
            }
        }
        return mapList;
    }

    @Override
    public ReturnJsonData addWmUserRoles(List<UserRole> userRoleList) {
        for(UserRole userRole : userRoleList){
            QueryParam param = new QueryParam();
            param.put("user_id",userRole.getUserId());
            List<UserRole> wmUserRoleList = wmUserRoleDao.list(param);
            for(UserRole role : wmUserRoleList){
                wmUserRoleDao.delete(new Object[]{role.getId()});
            }
        }
        int flag = wmUserRoleDao.addWmUserRoles(userRoleList);
        if(flag>0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加角色成功",null);
        }
        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"添加角色失败",null);
    }

}
