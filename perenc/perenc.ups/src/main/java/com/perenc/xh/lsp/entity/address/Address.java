package com.perenc.xh.lsp.entity.address;

import java.io.Serializable;

/**
 * @Description 地址
 * @Author xiaobai
 * @Date 2019/6/13 11:18
 **/
public class Address implements Serializable {
    private static final long serialVersionUID = 3764658302267772951L;

    private String id;
    //用户地址
    private int customerId;
    //用户名
    private String username;
    //电话号码
    private String phone;
    //省
    private String province;
    //城市
    private String city;
    //区
    private String area;
    //详细地址
    private String detailedAddress;
    //是否常用 1=常用
    private int useType;
    //创建时间
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
