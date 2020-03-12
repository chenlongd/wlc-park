package com.perenc.xh.lsp.entity.role;

import java.io.Serializable;

public class Role implements Serializable{

    private static final long serialVersionUID = -2397084862406106092L;

    private Integer id;
    //角色名称
    private String roleName;
    //角色描述
    private String roleDescrip;
    //角色标识
    private Integer roleType;
    //创建时间
    private String createTime;

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRoleDescrip() {
        return roleDescrip;
    }

    public void setRoleDescrip(String roleDescrip) {
        this.roleDescrip = roleDescrip;
    }
}
