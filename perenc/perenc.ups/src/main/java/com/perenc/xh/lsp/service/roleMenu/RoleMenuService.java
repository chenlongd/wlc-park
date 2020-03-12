package com.perenc.xh.lsp.service.roleMenu;

import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.entity.roleMenu.RoleMenu;

import java.util.List;
import java.util.Map;



public interface RoleMenuService {

    ReturnJsonData insertWmRoleMenu(RoleMenu wmRoleMenu);

    ReturnJsonData updateWmRoleMenu(RoleMenu wmRoleMenu);

    ReturnJsonData delWmRoleMenu(int id);

    RoleMenu getWmRoleMenuById(int id);

    List<RoleMenu> getAllWmRoleMenu(QueryParam param);

    List<Map<String,Object>> getWmRoleMenuList(QueryParam param);

    /**
     * 批量添加账号角色
     * @param wmRoleMenuList
     * @return
     */
    ReturnJsonData addWmRoleMenus(List<RoleMenu> wmRoleMenuList);

}
