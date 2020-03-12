package com.perenc.xh.lsp.entity.banner;

import java.io.Serializable;

/**
 * @Description 系统轮播图
 * @Author xiaobai
 * @Date 2019/5/23 14:20
 **/
public class SysBanner implements Serializable {

    private static final long serialVersionUID = -1092344848739018865L;

    private String id;
    //图片路径
    private String imageUrl;
    //类型 1=停车场;2=商城
    private int type;
    //轮播图类型 1=店铺;2=客户
    private int bannerType;
    //创建对象ID
    private String objId;
    //跳转类型 1=外部链接;2=内部商品;3=活动介绍
    private int redirectType;
    //排序
    private int sort;
    //标题
    private String title;
    //描述
    private String describe;
    //创建时间
    private String createTime;
    //更新时间
    private String updateTime;
    //链接地址
    private String linkUrl;
    //状态 1=可用;0=不可用
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBannerType() {
        return bannerType;
    }

    public void setBannerType(int bannerType) {
        this.bannerType = bannerType;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public int getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(int redirectType) {
        this.redirectType = redirectType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
