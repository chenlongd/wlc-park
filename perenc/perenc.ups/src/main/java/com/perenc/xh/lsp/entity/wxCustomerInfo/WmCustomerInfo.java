package com.perenc.xh.lsp.entity.wxCustomerInfo;

import java.io.Serializable;

public class WmCustomerInfo implements Serializable{

    private static final long serialVersionUID = -5969138576628044253L;

    private Integer id;
    //公共平台唯一标识
    private String unionid;
     //公共号或小程序ID
    private String appid;
     //某公众号下用户唯一标识
    private String openid;
     //用户ID
    private Integer customerId;
     //用户昵称
    private String nickName;
     //性别
    private Integer sex;
    //头像
    private String headImg;
    //城市
    private String city;
    //省份
    private String province;
    //国家
    private String country;
    //来自 1：小程序，2：公共号
    private Integer comeFrom;

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getComeFrom() {
        return comeFrom;
    }

    public void setComeFrom(Integer comeFrom) {
        this.comeFrom = comeFrom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
