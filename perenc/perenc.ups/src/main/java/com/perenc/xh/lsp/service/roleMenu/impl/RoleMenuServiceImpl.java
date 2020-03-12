package com.perenc.xh.lsp.service.roleMenu.impl;


import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.DBRelevant.InsertParam;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.dao.menu.MenuDao;
import com.perenc.xh.lsp.dao.role.RoleDao;
import com.perenc.xh.lsp.dao.roleMenu.RoleMenuDao;
import com.perenc.xh.lsp.entity.menu.Menu;
import com.perenc.xh.lsp.entity.role.Role;
import com.perenc.xh.lsp.entity.roleMenu.RoleMenu;
import com.perenc.xh.lsp.service.roleMenu.RoleMenuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("wmRoleMenuService")
@Transactional(rollbackFor = Exception.class)
public class RoleMenuServiceImpl implements RoleMenuService {

    @Autowired(required = false)
    private RoleMenuDao wmRoleMenuDao;

    @Autowired(required = false)
    private MenuDao wmMenuDao;

    @Autowired(required = false)
    private RoleDao wmRoleDao;

    @Override
    public ReturnJsonData insertWmRoleMenu(RoleMenu wmRoleMenu) {
        QueryParam queryParam = new QueryParam();
        queryParam.put("role_id",wmRoleMenu.getRoleId());
        queryParam.put("menu_id",wmRoleMenu.getMenuId());
        List<RoleMenu> userRoles = wmRoleMenuDao.list(queryParam);
        if(userRoles != null && userRoles.size()>0){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"该用户和角色已经绑定!",null);
        }else{
            InsertParam param = DBUtil.toInsertParam(wmRoleMenu);
            int flag = wmRoleMenuDao.add(param);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加成功", null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
    }

    @Override
    public ReturnJsonData updateWmRoleMenu(RoleMenu wmRoleMenu) {
        int flag = wmRoleMenuDao.update(DBUtil.toUpdateParam(wmRoleMenu,"id"));
        if(flag>0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"更新成功",null);
        }else {
            return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"更新失败",null);
        }
    }

    @Override
    public ReturnJsonData delWmRoleMenu(int id) {
        int flag = wmRoleMenuDao.delete(new Object[]{id});
        if(flag>0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"删除成功",null);
        }else {
            return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE,"删除失败",null);
        }
    }

    @Override
    public RoleMenu getWmRoleMenuById(int id) {
        QueryParam param = new QueryParam();
        param.addCondition("id","=",id);
        return  wmRoleMenuDao.getOne(param);
    }

    @Override
    public List<RoleMenu> getAllWmRoleMenu(QueryParam param) {
        return  wmRoleMenuDao.list(param);
    }

    @Override
    public List<Map<String,Object>> getWmRoleMenuList(QueryParam param) {
        List<RoleMenu> wmRoleMenus = wmRoleMenuDao.list(param);
        int count = wmRoleMenuDao.count(param);
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        if(wmRoleMenus != null && wmRoleMenus.size()>0){
            for(RoleMenu wmRoleMenu : wmRoleMenus){
                Map<String,Object> result = new HashMap<String,Object>();
                result.put("id",wmRoleMenu.getId());
                if(StringUtils.isNotEmpty(String.valueOf(wmRoleMenu.getMenuId()))){
                    Menu wmMenu = wmMenuDao.getById(Integer.valueOf(wmRoleMenu.getMenuId()));
                    if(wmMenu != null){
                        result.put("menuName",wmMenu.getMenuName());
                    }else{
                        result.put("menuName","");
                    }
                }
                if(StringUtils.isNotEmpty(String.valueOf(wmRoleMenu.getRoleId()))){
                    Role role = wmRoleDao.getById(Integer.valueOf(wmRoleMenu.getRoleId()));
                    if(role != null){
                        result.put("roleName",role.getRoleName());
                    }else{
                        result.put("roleName","");
                    }
                }
                result.put("menuId",wmRoleMenu.getMenuId());
                result.put("roleId",wmRoleMenu.getRoleId());
                result.put("count",count);
                mapList.add(result);
            }
        }
        return mapList;
    }

    @Override
    public ReturnJsonData addWmRoleMenus(List<RoleMenu> wmRoleMenuList) {
    if(wmRoleMenuList != null && wmRoleMenuList.size()>0){
            List<RoleMenu> roleMenuList = wmRoleMenuDao.getWmRoleMenusByRoleId(wmRoleMenuList.get(0).getRoleId());
            if(roleMenuList != null && roleMenuList.size()>0){
                for(RoleMenu rmenu : roleMenuList) {
                    wmRoleMenuDao.delete(new Object[]{rmenu.getId()});
                }
            }
        }
        int flag = wmRoleMenuDao.addWmRoleMenus(wmRoleMenuList);
        if(flag>0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加菜单成功",null);
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加菜单失败",null);
    }

}
