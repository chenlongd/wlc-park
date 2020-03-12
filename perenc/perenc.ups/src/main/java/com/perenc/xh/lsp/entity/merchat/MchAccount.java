package com.perenc.xh.lsp.entity.merchat;

import java.io.Serializable;

/**
 * @Description 商家电话号码
 * @Author xiaobai
 * @Date 2019/5/21 14:14
 **/
public class MchAccount implements Serializable {

    private static final long serialVersionUID = 8695695379306555273L;
    //商家ID
    private int id;
    //商家电话号码(登录账号)
    private String phone;
    //商家登录密码
    private String password;
    //商家名称
    private String mchName;
    //商家图片
    private String mchImg;
    //商家描述
    private String mchDescribe;
    //商家状态 1=可用;0=不可用
    private int status;
    //创建时间
    private String createTime;
    //更新时间
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public String getMchImg() {
        return mchImg;
    }

    public void setMchImg(String mchImg) {
        this.mchImg = mchImg;
    }

    public String getMchDescribe() {
        return mchDescribe;
    }

    public void setMchDescribe(String mchDescribe) {
        this.mchDescribe = mchDescribe;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
