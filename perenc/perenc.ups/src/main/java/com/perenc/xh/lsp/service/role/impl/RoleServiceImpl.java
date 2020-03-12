package com.perenc.xh.lsp.service.role.impl;


import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.DBRelevant.InsertParam;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.dao.role.RoleDao;
import com.perenc.xh.lsp.dao.roleMenu.RoleMenuDao;
import com.perenc.xh.lsp.entity.role.Role;
import com.perenc.xh.lsp.entity.roleMenu.RoleMenu;
import com.perenc.xh.lsp.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("wmRoleService")
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

    @Autowired(required = false)
    private RoleDao wmRoleDao;

    @Autowired(required = false)
    private RoleMenuDao roleMenuDao;


    @Override
    public ReturnJsonData insertRole(Role adminRole) {
        QueryParam param = new QueryParam();
        param.addCondition("role_name","=",adminRole.getRoleName());
        Role role = wmRoleDao.getOne(param);
        if(role == null) {
            InsertParam ip = DBUtil.toInsertParam(adminRole);
            int flag = wmRoleDao.add(ip);
            if (flag > 0) {
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加成功", null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"该角色名称已经添加了",null);
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
    }

    @Override
    public ReturnJsonData updateWmRole(Role adminRole) {
        int flag = wmRoleDao.update(DBUtil.toUpdateParam(adminRole,"id"));
        if(flag>0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    @Override
    public ReturnJsonData delWmRole(int id) {
        List<RoleMenu> roleMenuList = roleMenuDao.getWmRoleMenusByRoleId(id);
        if(roleMenuList != null && roleMenuList.size()>0){
            for(RoleMenu rmenu : roleMenuList) {
                roleMenuDao.delete(new Object[]{rmenu.getId()});
            }
        }
       int flag = wmRoleDao.delete(new Object[]{id});
       if(flag>0){
           return new ReturnJsonData(DataCodeUtil.SUCCESS,"删除成功",null);
       }
       return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE,"删除成功",null);
    }

    @Override
    public Role getWmRoleById(int id) {
        return  wmRoleDao.getById(id);
    }

    @Override
    public List<Role> getAllWmRole(QueryParam param) {
        return wmRoleDao.list(param);
    }

    @Override
    public List<Map<String, Object>> getWmRoleList(QueryParam param) {
        List<Role> wmRoleList = wmRoleDao.list(param);
        int count = wmRoleDao.count(param);
        List<Map<String,Object>> roleList = new ArrayList<>();
        for(Role wmRole : wmRoleList){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",wmRole.getId());
            condition.put("roleName",wmRole.getRoleName());
            condition.put("roleDescrip",wmRole.getRoleDescrip());
            condition.put("roleType",wmRole.getRoleType());
            condition.put("createTime",wmRole.getCreateTime());
            condition.put("count",count);
            roleList.add(condition);
        }
        return roleList;
    }
}
