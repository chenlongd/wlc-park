package com.perenc.xh.lsp.entity.order;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 系统订单
 * @Author xiaobai
 * @Date 2019/5/22 10:14
 **/
@Data
public class SysOrder implements Serializable {

    private static final long serialVersionUID = -7803354199798856140L;

    private int id;
    //订单序列号
    private String workId;
    //店铺ID
    private String storeId;
    //地址ID
    private String addressId;
    //优惠券ID
    private String vouchersId;
    //优惠券金额
    private int vouchersAmount;
    //订单号
    private String orderNo;
    //单价
    private int unitPrice;
    //数量
    private int number;
    //订单描述
    private String orderDescribe;
    //备注
    private String remark;
    //物品总价
    private int objTotalPrice;
    //总价格
    private int totalPrice;
    //支付类型 1=微信;2=支付宝
    private int type;
    //支付状态 0=未支付;1=支付
    private int payStatus;
    //订单类型 1=停车费;2=停车充值;3=商户申请卷;4=商品;5=停车购卡;6=虚拟商品
    private int payType;
    //支付方式 1=在线支付;2=余额支付，3=停车券抵扣;4=虚拟商品;5=月卡车;6=免费车;7=在线支付停车券;8=余额支付停车券
    private int payMode;
    //手机号
    private String phone;
    //车辆客户ID
    private int extendId;
    //支付者ID
    private int customerId;
    //微信给的交易号
    private String transactionId;
    //商家ID 关联tc_seller表的id       db_column: seller_id
    private Integer sellerId;
    //支付对象ID
    private String objId;
    //停车开始时间
    private String parkStartTime;
    //停车结束时间
    private String parkEndTime;
    //停车时长
    private  int parkDuration;
    //停车优惠券ID
    private String couponId;
    //停车券小时数
    private int couponDuration;
    //停车优惠券金额
    private int couponAmount;
    //微信使用金额
    private int wechat;
    //使用余额
    private int balance;
    //使用积分
    private int integarl;
    //使用积分
    private int integarlMoney;
    //实际收费金额
    private int actual;
    //免费金额
    private int free;
    //停车场ID
    private String parklotId;
    //车辆ID
    private String carId;
    //车牌号
    private String carNum;
    //捷顺支付编号
    private String jspayNo;
    //捷顺停车总金额
    private int jschargeTotal;
    //捷顺打折减扣的金额
    private int jsdiscountAmount;
    //捷顺最后实收金额
    private int jscharge;
    //捷顺停车开始时间
    private String jsinTime;
    //捷顺停车结束时间
    private String jsfeesTime;
    //捷顺停车时长
    private  String jsstopTime;
    //支付时间
    private String payTime;
    //是否能开发票 1：是;2：否
    private Integer isInvoice;
    //开发票状态（1:待申请2：开票中，3:已开发票）
    private Integer invoiceStatus;
    //购卡VIP类型（1:月卡车,2:季卡车，3:半年卡车，4:年卡）
    private Integer vipType;
    //状态 1=待付款;2=待发货;3=待收货;4=申请退款;5=退款成功;6=评价;7=完成;
    private int orderStatus;
    //收货 1=收货;0=未收货
    private int acceptStatus = 0;
    //发货 1=发货;0=未发货
    private int sendStatus = 0;
    //确认订单 0=非订单;1=订单
    private int confirmType;
    //地址用户名
    private String addressUsername;
    //地址电话号码
    private String addressPhone;
    //详细地址
    private String detailedAddress;
    //是否评论
    private int commetStatus;
    //客房价格类型
    private String priceTypeId;
    //创建时间
    private String createTime;
}
