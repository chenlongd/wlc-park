package com.perenc.xh.lsp.entity.extendUser;

import java.io.Serializable;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/4/11 16:51
 **/
public class ExtendUser implements Serializable {

    private static final long serialVersionUID = 5596051338420981798L;

    private int id;
    //appID
    private String appId;
    //appSecrert
    private String appSecrert;
    //客户ID
    private int customerId;
    //来源：1=小程序；2=公众号
    private int comeFrom;
    //手机号
    private String phone;
    //支付密码
    private String payPsword;
    //积分
    private int integral;
    //余额
    private int balance;
    //邮箱
    private String email;
    //联系人
    private String contact;

    /**
     * 地区省ID 关联tc_province表的id       db_column: province_id
     * @NotNull
     */
    private String provinceId;

    /**
     * 地区市ID 关联tc_city表的id       db_column: city_id
     * @NotNull
     */
    private String cityId;

    /**
     * 地区县ID 关联tc_county表的id       db_column: county_id
     * @NotNull
     */
    private String countyId;

    //地址
    private String address;
    //头像
    private String headImage;
    //性别 1=男;2=女;0=未知
    private int sex;
    //昵称
    private String nickname;
    //用户名
    private String username;
    //生日
    private String birthday;
    //驾驶证ID
    private int driveId;
    //驾驶证
    private String driveImg;
    //驾龄
    private int driveAge;
    //地区
    private String area;
    //行车证ID
    private int travelId;
    //记录状态 1：可用;2：不可用
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecrert() {
        return appSecrert;
    }

    public void setAppSecrert(String appSecrert) {
        this.appSecrert = appSecrert;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getComeFrom() {
        return comeFrom;
    }

    public void setComeFrom(int comeFrom) {
        this.comeFrom = comeFrom;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPayPsword() {
        return payPsword;
    }

    public void setPayPsword(String payPsword) {
        this.payPsword = payPsword;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getDriveId() {
        return driveId;
    }

    public void setDriveId(int driveId) {
        this.driveId = driveId;
    }

    public String getDriveImg() {
        return driveImg;
    }

    public void setDriveImg(String driveImg) {
        this.driveImg = driveImg;
    }

    public int getDriveAge() {
        return driveAge;
    }

    public void setDriveAge(int driveAge) {
        this.driveAge = driveAge;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
    public int getTravelId() {
        return travelId;
    }

    public void setTravelId(int travelId) {
        this.travelId = travelId;
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
