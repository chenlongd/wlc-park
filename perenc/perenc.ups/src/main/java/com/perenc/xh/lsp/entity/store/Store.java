package com.perenc.xh.lsp.entity.store;

import java.io.Serializable;

/**
 * @Description 店铺
 * @Author xiaobai
 * @Date 2019/5/8 14:19
 **/
public class Store implements Serializable {

    private static final long serialVersionUID = 4512922300794228056L;
    //店铺ID
    private String id;
    //账号ID
    private int accountId;
    //店铺名称
    private String storeName;
    //店铺电话
    private String phone;
    //店铺图片
    private String storeImg;
    //所在地
    private String address;
    //企业资质
    private String businessLicenseImg;
    //排序
    private int sort;
    //店铺类型 1=自营
    private int type;
    //店铺类型 1= 酒店商城
    private int storeType = 0;
    //是否 审核通过 1=审核；0=未审核
    private int isApproval;
    //状态 0=不可用；1=可用
    private int status;
    //创建时间
    private String createTime;
    //修改时间
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStoreImg() {
        return storeImg;
    }

    public void setStoreImg(String storeImg) {
        this.storeImg = storeImg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessLicenseImg() {
        return businessLicenseImg;
    }

    public void setBusinessLicenseImg(String businessLicenseImg) {
        this.businessLicenseImg = businessLicenseImg;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsApproval() {
        return isApproval;
    }

    public void setIsApproval(int isApproval) {
        this.isApproval = isApproval;
    }

    public int getStoreType() {
        return storeType;
    }

    public void setStoreType(int storeType) {
        this.storeType = storeType;
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
