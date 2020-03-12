package com.perenc.xh.lsp.entity.user;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 9086558335328874085L;

    private int id;
    //账号
    private String account;
    //用户名
    private String username;
    //用户头像
    private String headImageUrl;
    //性别 1=男；2=女；0=未知
    private int sex;
    //电话
    private String phone;
    //密码
    private String password;
    //邮箱
    private String email;
    //账号类型 0=超级管理员  1=其他管理员
    private Integer userType;
    //店铺ID
    private String storeId;
    //展示类型 0=普通;1=商城
    private int showType;
    //创建时间
    private String createTime;
    //是否使用
    private int status = 1;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
