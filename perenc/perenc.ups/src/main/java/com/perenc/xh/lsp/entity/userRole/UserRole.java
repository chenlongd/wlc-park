package com.perenc.xh.lsp.entity.userRole;

import java.io.Serializable;

public class UserRole implements Serializable{

    private static final long serialVersionUID = 47271482316904458L;

    private Integer id;
    //账号ID
    private Integer userId;
    //角色ID
    private Integer roleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
