package com.perenc.xh.lsp.dao.roleMenu;



import com.perenc.xh.commonUtils.dao.BaseDao;
import com.perenc.xh.lsp.entity.roleMenu.RoleMenu;
import org.apache.ibatis.annotations.Param;


import java.util.List;


public interface RoleMenuDao extends BaseDao<RoleMenu> {

    int addWmRoleMenus(@Param("roleMenus") List<RoleMenu> userRoleList);

    List<RoleMenu> getWmRoleMenusByRoleId(int roleId);
}
