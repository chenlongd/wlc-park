package com.perenc.xh.lsp.entity.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 商品信息
 * @Author xiaobai
 * @Date 2019/6/13 15:54
 **/
@Data
public class GoodsInfo implements Serializable {
    private static final long serialVersionUID = 1736326310661946172L;

    private String storeId;

    private String goodsAttributeId;
    //客房价格类型
    private String priceTypeId;

    private int type;

    private int number;

    private double price;

    private String goodsName;
}
