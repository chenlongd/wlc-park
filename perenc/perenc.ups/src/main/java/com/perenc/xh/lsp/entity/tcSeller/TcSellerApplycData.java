package com.perenc.xh.lsp.entity.tcSeller;

import java.io.Serializable;
import java.util.List;

public class TcSellerApplycData implements Serializable {

    private static final long serialVersionUID = -7845052877875071536L;


    //申领ID
    private String id;

    //商家ID
    private Integer sellerId;

    //审核状态 是否审核通过（1:待审核，2:通过，3:未通过）
    private  Integer isApproval;

    //申领票券张数数组
    private List<TcSellerApplycoupon> sellerApplycouponList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getIsApproval() {
        return isApproval;
    }

    public void setIsApproval(Integer isApproval) {
        this.isApproval = isApproval;
    }

    public List<TcSellerApplycoupon> getSellerApplycouponList() {
        return sellerApplycouponList;
    }

    public void setSellerApplycouponList(List<TcSellerApplycoupon> sellerApplycouponList) {
        this.sellerApplycouponList = sellerApplycouponList;
    }


}
