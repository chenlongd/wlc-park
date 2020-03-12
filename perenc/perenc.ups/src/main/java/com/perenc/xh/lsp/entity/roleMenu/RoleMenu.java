package com.perenc.xh.lsp.entity.roleMenu;

import java.io.Serializable;

public class RoleMenu implements Serializable{

    private static final long serialVersionUID = 2961091410596156954L;

    private Integer id;
    //角色ID
    private Integer roleId;
    //菜单ID
    private Integer menuId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}
