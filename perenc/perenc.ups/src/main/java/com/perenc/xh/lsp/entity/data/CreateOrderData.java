package com.perenc.xh.lsp.entity.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 创建订单数据
 * @Author xiaobai
 * @Date 2019/6/13 15:47
 **/
@Data
public class CreateOrderData implements Serializable {

    private static final long serialVersionUID = 617993535692464414L;
    // 1=商品;2=停车场充值;3=停车场购卡充值;4=停车费缴费;
    private int type;

    private int customerId;

    private double totalPrice;

    // 停车场增加传参字段
    // 车辆客户Id
    private int extendId;
    //余额
    private int balance;
    // 车辆手机号
    private String phone;

    // 车辆Id
    private String carId;
    // objId(会员id,充值id)
    private String objId;

    private List<GoodsInfo> goodsInfoList;

}
