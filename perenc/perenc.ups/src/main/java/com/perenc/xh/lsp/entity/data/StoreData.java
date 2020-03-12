package com.perenc.xh.lsp.entity.data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 店铺数据
 * @Author xiaobai
 * @Date 2019/7/30 11:35
 **/
public class StoreData implements Serializable {
    private static final long serialVersionUID = 6478208417164121067L;
    //店铺ID
    private String storeId;
    //店铺总价
    private double storeTotalPrice;
    //店铺商品
    private List<GoodsInfo> goodsInfoList;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public double getStoreTotalPrice() {
        return storeTotalPrice;
    }

    public void setStoreTotalPrice(double storeTotalPrice) {
        this.storeTotalPrice = storeTotalPrice;
    }

    public List<GoodsInfo> getGoodsInfoList() {
        return goodsInfoList;
    }

    public void setGoodsInfoList(List<GoodsInfo> goodsInfoList) {
        this.goodsInfoList = goodsInfoList;
    }
}
