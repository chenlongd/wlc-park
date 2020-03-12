package com.perenc.xh.lsp.entity.data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 商城创建订单
 * @Author xiaobai
 * @Date 2019/7/30 11:14
 **/
public class CreateOrder implements Serializable {
    private static final long serialVersionUID = 5767360450102561076L;
    //用户ID
    private int customerId;
    // 1=商品;2=停车场充值;3=停车场购卡充值;4=停车费缴费;
    private int goodsType;
    //用户ID
    private int payType;
    //总价
    private double totalPrice;
    //店铺数据信息
    private List<StoreData> storeList;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<StoreData> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreData> storeList) {
        this.storeList = storeList;
    }
}
