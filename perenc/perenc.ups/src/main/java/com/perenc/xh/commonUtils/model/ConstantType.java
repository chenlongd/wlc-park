package com.perenc.xh.commonUtils.model;

/**
 * 类型常量值
 */
public class ConstantType {



    /** VIP_TYPE:VIP类型卡：月卡    value:1 */
    public static int VIP_TYPE_ONE  = 1;
    /** VIP_TYPE:VIP类型卡：季卡   value:2 */
    public static int VIP_TYPE_THREE  = 2;
    /** VIP_TYPE:VIP类型卡：半年卡    value:3 */
    public static int VIP_TYPE_HALFAYEAR  = 3;
    /** VIP_TYPE:VIP类型卡：年卡    value:4 */
    public static int VIP_TYPE_AYEAR = 4;


    /** VIP_MONTH:VIP月数：一月    value:1 */
    public static int VIP_MONTH_ONE  = 1;
    /** VIP_MONTH:VIP月数：三月    value:3 */
    public static int VIP_MONTH_THREE  = 3;
    /** VIP_MONTH:VIP月数：半年    value:6 */
    public static int VIP_MONTH_HALFAYEAR  = 6;
    /** VIP_MONTH:VIP月数：一年    value:12 */
    public static int VIP_MONTH_AYEAR = 12;


    /** CAR_TYPE:车类型：小型车    value:1 */
    public static int CAR_TYPE_SMALL  = 1;
    /** CAR_TYPE:车类型：中型车    value:2 */
    public static int CAR_VIP_TYPE_CENTRE  = 2;
    /** CAR_TYPE:车类型：大型车    value:3 */
    public static int CAR_VIP_TYPE_BIG  = 3;


    /** CAR_VIP_TYPE:VIP车类型：临时车    value:1 */
    public static int CAR_VIP_TYPE_TEMP  = 1;
    /** CAR_VIP_TYPE:VIP车类型：VIP车    value:2 */
    public static int CAR_VIP_TYPE_CARD  = 2;
    /** CAR_VIP_TYPE:VIP车类型：免费车    value:3 */
    public static int CAR_VIP_TYPE_FREE  = 3;

    /** CAR_VIP_TYPE_NAME:VIP车类型：临时车    value:临时车 */
    public static String CAR_VIP_TYPE_TEMP_NAME  = "临时车";
    /** CAR_VIP_TYPE_NAME:VIP车类型：VIP车    value:购卡车 */
    public static String CAR_VIP_TYPE_CARD_NAME  = "购卡车";
    /** CAR_VIP_TYPE_NAME:VIP车类型：免费车    value:免费车 */
    public static String CAR_VIP_TYPE_FREE_NAME  = "免费车";


    /** INVOICE_NUMBER:发票流水号：前缀     value:WLC-I- */
    public static String INVOICE_NUMBER_PREFIX   ="WLC-I-";

    /** INVOICE_AMOUNT:开发票最低金额限制：金额    value:10000 */
    public static int INVOICE_MIN_AMOUNT   =10000;


    /** INVOICE_AMOUNT:免费停车分钟数：分钟数    value:15 */
    public static int CAR_FREE_MINUTE_NUMBER   =15;


    /** USE_STATUS:使用状态：待使用    value:1 */
    public static int USE_STATUS_WAITFOR  = 1;
    /** USE_STATUS:使用状态：被冻结    value:2 */
    public static int USE_STATUS_FROZEN  = 2;
    /** USE_STATUS:使用状态：已使用    value:3 */
    public static int USE_STATUS_USED  = 3;
    /** USE_STATUS:使用状态：已过期    value:4 */
    public static int USE_STATUS_EXPIRED  = 4;


    /** INVOICE_NUMBER_ID:用户停车默认停车场ID 关联计费规则：停车场Id     value:5cfdba20c9fb60ad54adaf2b */
    public static String SYS_TC_TCPARKLOT_ID   ="5cfdba20c9fb60ad54adaf2b";

    /** INVOICE_NUMBER:用户停车充值创建定单时：店铺Id     value:tcczstoreaaaaaaaaaaaaa01 */
    public static String SYS_ORDER_TC_STORE_ID   ="tcczstoreaaaaaaaaaaaaa01";

    /** INVOICE_NUMBER:用户停车充值创建定单时：商品ID     value:tcczattributeaaaaaaaaa01 */
    public static String SYS_ORDER_TC_ATTRIBUTE_ID   ="tcczattributeaaaaaaaaa01";


    /** COUPON_USE_TYPE:票券使用类型：减免金额     value:1 */
    public static int COUPON_USE_TYPE_MINUSMONEY  = 1;

    /** COUPON_USE_TYPE:票券使用类型：减免小时     value:1 */
    public static int COUPON_USE_TYPE_MINUSHOUR  = 2;

    /** COUPON_USE_TYPE:票券使用类型：百分比     value:1 */
    public static int COUPON_USE_TYPE_MINPERCENT  = 3;

    /** COUPON_USE_TYPE:票券使用类型：全免     value:1 */
    public static int COUPON_USE_TYPE_ALLFREE  = 4;


    /**捷顺字段值 **/
    /** JS_COUPON_WAY:优惠方式：减免金额   value:MINUSMONEY */
    public static String JS_COUPON_COUPON_WAY_MINUSMONEY  = "MINUSMONEY";

    /** JS_COUPON_WAY:优惠方式：减免小时   value:MINUSHOUR */
    public static String JS_COUPON_COUPON_WAY_MINUSHOUR  = "MINUSHOUR";

    /** JS_COUPON_WAY:优惠方式：百分比， 例如:0.9,则表示实收金额=应收金额*90%  value:MINPERCENT */
    public static String JS_COUPON_COUPON_WAY_MINPERCENT  = "MINPERCENT";

    /** JS_COUPON_WAY:优惠方式：全免    value:MINPERCENT */
    public static String JS_COUPON_COUPON_WAY_ALLFREE  = "ALLFREE";


    /** JS_COUPON_STATUS:使用状态：未使用    value:0 */
    public static int JS_COUPON_STATUS_WAITFOR  = 0;
    /** JS_COUPON_STATUS:使用状态：已使用    value:1 */
    public static int JS_COUPON_STATUS_USED  = 1;
    /** JS_COUPON_:使用状态：已过期，作废    value:2 */
    public static int JS_COUPON_STATUS_EXPIRED  = 2;


    /** JS_PAY_TYPE:支付方式：微信    value:WX */
    public static String JS_PAY_TYPE_WX  = "WX";
    /** JS_PAY_TYPE:支付方式：支付宝    value:ZFB */
    public static String JS_PAY_TYPE_ZFB  = "ZFB";
    /** JS_PAY_TYPE:支付方式：其它    value:OTHER */
    public static String JS_PAY_TYPE_OTHER  = "OTHER";

    /** JS_PAY_STATUS:支付状态：未支付    value:-1 */
    public static int JS_PAY_STATUS_NOT_PAY  = -1;
    /** JS_PAY_STATUS:支付状态：已支付    value:0 */
    public static int JS_PAY_STATUS_USED_PAY  = 0;


    /** JS_OPEN_MODEL:开闸模式：开闸    value:1 */
    public static int JS_OPEN_MODEL_ON  = 1;
    /** JS_OPEN_MODEL:开闸模式：关闸    value:2 */
    public static int JS_OPEN_MODEL_OFF  = 2;


    /** JS_COUPON_WAY:捷顺服务IP地址：本地IP   value:170.18.20.254 */
    public static String JS_SERVICE_IP_LOCAL_IP  = "170.18.20.254";

    /** JS_COUPON_WAY:捷顺服务IP地址：可访问本地IP   value:172.100.100.254 */
    public static String JS_SERVICE_IP_LOCAL_ACCESS_IP  = "111.85.248.64";


}
