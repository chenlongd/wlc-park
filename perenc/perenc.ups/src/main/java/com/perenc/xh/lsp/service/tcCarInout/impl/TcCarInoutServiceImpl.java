package com.perenc.xh.lsp.service.tcCarInout.impl;

import com.alibaba.fastjson.JSONObject;
import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.ConstantType;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.JsReturnJsonData;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.jshttp.JsHttpClientUtil;
import com.perenc.xh.commonUtils.utils.lang.RandomUtils;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.dao.order.SysOrderDao;
import com.perenc.xh.lsp.entity.order.SysOrder;
import com.perenc.xh.lsp.entity.tcCar.TcCar;
import com.perenc.xh.lsp.entity.tcCar.TcCarInout;
import com.perenc.xh.lsp.entity.tcCar.TcCarJsclientlog;
import com.perenc.xh.lsp.entity.tcCoupon.TcCoupon;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCoupon;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerFreecar;
import com.perenc.xh.lsp.service.tcCarInout.TcCarInoutService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 车辆出场识别记录
 */
@Service("tcCarInoutService")
@Transactional(rollbackFor = Exception.class)
public class TcCarInoutServiceImpl implements TcCarInoutService {

    private static final Logger logger = Logger.getLogger(TcCarInoutServiceImpl.class);

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private SysOrderDao orderDao;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public ReturnJsonData insert(TcCarInout tcCarInout) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        Map<String,Object> conditions = new HashMap<>();
        conditions.put("carNum",tcCarInout.getCarNum());
        conditions.put("status",1);
        //当车辆进入出去时根据车牌号查询
        TcCar tcCar = mongoComDAO.executeForObjectByCon(conditions, TcCar.class);
        if(tcCar != null) {
            tcCarInout.setParklotId(tcCar.getParklotId());
            tcCarInout.setCarId(tcCar.getId());
            tcCarInout.setCarNum(tcCar.getCarNum());
            tcCarInout.setType(tcCar.getType());
            tcCarInout.setSdate(tcCar.getCostSdate());
            tcCarInout.setEdate(tcCar.getCostEdate());
            tcCarInout.setIntimg(tcCar.getIntimg());
            tcCarInout.setOutimg(tcCar.getOutimg());
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车牌号不存在",null);
        }
        //计算停车时长，单位小时
        double parkHour =0;
        if(StringUtils.isNotEmpty(tcCarInout.getSdate()) && StringUtils.isNotEmpty(tcCarInout.getEdate())) {
            parkHour = ToolUtil.getDateHourNum(tcCarInout.getSdate(), tcCarInout.getEdate());
        }
        tcCarInout.setParkHour(ToolUtil.getDoubleToInt(parkHour));
        tcCarInout.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcCarInout.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcCarInout);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }


    //商家id
    private Integer sellerId = 0;
    //支付编号
    private String payNo = "";
    //应收金额
    private Integer chargeTotal = 0;
    //折扣金额
    private Integer discountAmount = 0;
    //实收金额
    private Integer charge = 0;
    //捷顺停车开始时间
    private String inTime="";
    //捷顺停车结束时间
    private String feesTime="";
    //捷顺停车时长
    private String stopTime="";
    private JsReturnJsonData faildResponse = new JsReturnJsonData("-1", "", null);
    /**
     * 捷顺
     * 第三方接口
     * 接收车辆出场识别记录
     * @param tcCarInout
     * @param map
     * @return
     * @throws Exception
     */
    public JsReturnJsonData insertOutrecognition(TcCarInout tcCarInout, Map<String, Object> map) throws Exception {
        String carNum =  tcCarInout.getCarNum();
        if(null == carNum || carNum.isEmpty()){
            logger.error("----------车辆出场识别记录:车牌号为NULL----------");
            return this.faildResponse;
        }
        //判断是否是真实的号码
        if(carNum.equals("未识别")) {
            return this.faildResponse;
        }
        //1.请求计费开始
        String reqJsonStr = "{\"plateNumber\":\""+carNum+"\"}";
        logger.info("----------开始请求计费--------------\n"+reqJsonStr);
        JSONObject jsonCharging= JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL+"park/charging",reqJsonStr);
        if(null == jsonCharging){
            logger.error("----------请求计费响应为null--------------");
            return faildResponse;
        }
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //添加日志
                    TcCarJsclientlog tjsclientlog = new TcCarJsclientlog();
                    tjsclientlog.setType(1);
                    tjsclientlog.setUrl(JsHttpClientUtil.JS_CLIENT_URL + "park/charging");
                    tjsclientlog.setRmode(1);
                    tjsclientlog.setRparameter(reqJsonStr);
                    tjsclientlog.setContent(jsonCharging.toString());
                    tjsclientlog.setCarNum(carNum);
                    tjsclientlog.setRemark("接收出场请求计费查询");
                    tjsclientlog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    tjsclientlog.setStatus(1);
                    mongoComDAO.executeInsert(tjsclientlog);
                    logger.info("----------添加车辆查询日志--------------");
                } catch (Exception e) {
                    logger.error("执行错误："+e.getMessage(),e);
                }
            }
        });

        String code = jsonCharging.getString("code");
        if (null == code || !code.equals("0")) {
            logger.error("----------请求计费失败:--------------\n" + jsonCharging.toString());
            return faildResponse;
        }
        JSONObject data = jsonCharging.getJSONObject("data");
        payNo = data.getString("payNo");
        chargeTotal = data.getInteger("chargeTotal");
        discountAmount = data.getInteger("discountAmount");
        charge = data.getInteger("charge");
        inTime= data.getString("inTime");
        feesTime = data.getString("feesTime");
        stopTime = data.getString("stopTime");
        logger.info("----------请求计费成功--------------" + jsonCharging.toString());
        //判断判断是否大于0
        if(charge<=0) {
            logger.error("----------计费小于0:--------------\n" + jsonCharging.toString());
            return faildResponse;
        }
        //当车辆进入出去时根据车牌号查询
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("carNum", carNum);
        conditions.put("status", 1);
        TcCar tcCar = mongoComDAO.executeForObjectByCon(conditions, TcCar.class);
        logger.info("----------查询本地车辆信息--------------"+JSONObject.toJSONString(tcCar));
        //业务服务器无此车辆入场信息，按临时车处理，由收费员放行。
        if(tcCar==null) {
            return faildResponse;
        }
        //1.是临时车，需要用户自己用app缴费
        if(tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_TEMP)) {
            logger.info("----------车辆为临时车--------------");
            return this.faildResponse;
        }
        //2.是vip或者免费车,就要给开闸
        if (tcCar.getVipType().intValue() == ConstantType.CAR_VIP_TYPE_CARD || tcCar.getVipType().intValue() == ConstantType.CAR_VIP_TYPE_FREE) {
            logger.info("----------vip或者免费车发送支付结果通知开始--------------\n" + tcCar.getVipType());
            //发送支付结果通知
            JsReturnJsonData jsrData=sendPaymentresult(payNo,charge,carNum);
            if(jsrData == this.faildResponse) return this.faildResponse;
        }
        logger.info("----------生成定单开始--------------");
        // 查询设置免费车商家ID
        Map<String, Object> conditionsfreecar = new HashMap<>();
        conditionsfreecar.put("carId", tcCar.getId());
        conditionsfreecar.put("useStatus",ConstantType.USE_STATUS_USED);
        conditionsfreecar.put("status", 1);
        //查询免费车商家ID
        TcSellerFreecar tcSellerFreecar = mongoComDAO.executeForObjectByCon(conditionsfreecar, TcSellerFreecar.class);
        if(tcSellerFreecar!=null) {
            sellerId=tcSellerFreecar.getSellerId();
        }
        //最后生成定单
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //添加日志
                    //生成定单
                    String orderDescribe = "VIP车免费";
                    //付款方式 1=在线支付;2=余额支付，3=停车券抵扣;4=虚拟商品;5=月卡车;6=免费车
                    Integer payMode = 5;
                    //日志备注
                    if (tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_FREE)){
                        orderDescribe="免费车免费";
                        payMode=6;
                    }
                    //获取当前时间
                    logger.info("----------生成定单开始--------------");
                    String strNowTime =DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS);
                    String orderNo = RandomUtils.generateOrderNo("zmsc", 10);
                    SysOrder order = new SysOrder();
                    order.setCustomerId(0);
                    order.setStoreId(tcCar.getId());
                    order.setSellerId(sellerId);
                    order.setObjId(payNo);
                    order.setCarId(tcCar.getId());
                    order.setCarNum(tcCar.getCarNum());
                    order.setJspayNo(payNo);
                    order.setJschargeTotal(chargeTotal);
                    order.setJsdiscountAmount(discountAmount);
                    order.setJscharge(charge);
                    order.setJsinTime(inTime);
                    order.setJsfeesTime(feesTime);
                    order.setJsstopTime(stopTime);
                    order.setFree(charge);
                    order.setParklotId(tcCar.getParklotId());
                    double carDuration = ToolUtil.getDateHourNum(tcCar.getCostSdate(), strNowTime);
                    order.setParkStartTime(tcCar.getCostSdate());
                    order.setParkEndTime(strNowTime);
                    order.setParkDuration(ToolUtil.getDoubleToInt(carDuration));
                    order.setOrderDescribe(orderDescribe);
                    order.setPayMode(payMode);
                    order.setNumber(1);
                    order.setUnitPrice(charge);
                    order.setTotalPrice(charge);
                    order.setObjTotalPrice(chargeTotal);
                    order.setType(1);
                    order.setPayStatus(1);//支付完成
                    order.setPayTime(strNowTime);
                    order.setPayType(1);
                    order.setOrderStatus(2);//支付成功
                    order.setConfirmType(1);
                    order.setIsInvoice(2); //是否开发票，1：是，2：否
                    order.setInvoiceStatus(1); //开发票状态（1:待申请2：开票中，3:已开发票）
                    order.setCommetStatus(0);//评论 0=未评论;1=评论
                    order.setCreateTime(strNowTime);
                    order.setOrderNo(orderNo);
                    int i=orderDao.add(DBUtil.toInsertParam(order));
                    if(i>0) {
                        logger.info("----------生成定单成功------------");
                    }else {
                        logger.info("----------生成定单失败--------------");
                    }
                    logger.info("----------生成定单结束--------------");
                } catch (Exception e) {
                    logger.error("执行错误："+e.getMessage(),e);
                }
            }
        });
        logger.info("----------添加接收车辆出场记录开始--------------");
        //接收车辆出场记录
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    tcCarInout.setParklotId(""); //本地停车场Id
                    tcCarInout.setCarInId(""); //入场ID
                    tcCarInout.setInRecognitionTime("");//入场识别时间
                    tcCarInout.setCarId(tcCar.getId()); //车辆ID
                    tcCarInout.setType(ConstantType.CAR_TYPE_SMALL); //默认为小型车
                    tcCarInout.setSdate(""); //计费开始时间
                    tcCarInout.setEdate(""); //计费结束时间
                    tcCarInout.setParkHour(null); //停车时长
                    //图片判断
                    String inImg=tcCarInout.getIntimg();
                    String temInImg="";
                    logger.info("----------获取进场图片--------------"+inImg);
                    if(StringUtils.isNotEmpty(inImg)){
                        logger.info("----------获取进场图片-----不为空---------"+inImg);
                        //判断是否包含http
                        if(inImg.indexOf(ConstantType.JS_SERVICE_IP_LOCAL_IP)!=-1)
                        { //包含
                            temInImg=inImg.replace(ConstantType.JS_SERVICE_IP_LOCAL_IP, ConstantType.JS_SERVICE_IP_LOCAL_ACCESS_IP);
                            logger.info("----------获取进场图片-----转换为新地址---------"+temInImg);
                        }
                    }
                    tcCarInout.setIntimg(temInImg);
                    String outImg=tcCarInout.getOutimg();
                    String temOutImg="";
                    logger.info("----------获取出场图片--------------"+outImg);
                    if(StringUtils.isNotEmpty(outImg)){
                        logger.info("----------获取出场图片-----不为空---------"+outImg);
                        //判断是否包含http
                        if(outImg.indexOf(ConstantType.JS_SERVICE_IP_LOCAL_IP)!=-1)
                        { //包含
                            temOutImg=outImg.replace(ConstantType.JS_SERVICE_IP_LOCAL_IP, ConstantType.JS_SERVICE_IP_LOCAL_ACCESS_IP);
                            logger.info("----------获取出场图片-----转换为新地址---------"+temOutImg);
                        }
                    }
                    tcCarInout.setOutimg(temOutImg);
                    tcCarInout.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    tcCarInout.setStatus(1);
                    int flag = mongoComDAO.executeInsert(tcCarInout);
                    if(flag>0) {
                        logger.info("----------添加接收车辆出场成功------------");
                    }else {
                        logger.info("----------添加接收车辆出场失败------------");
                    }
                    logger.info("----------添加接收车辆出场记录结束--------------");
                } catch (Exception e) {
                    logger.error("执行错误："+e.getMessage(),e);
                }
            }
        });
        return new JsReturnJsonData(DataCodeUtil.SUCCESS + "", "", null);
    }


    /**
     * 发送结果通知
     * @param payNo
     * @param charge
     * @param carNum
     * @throws Exception
     */
    private JsReturnJsonData sendPaymentresult(String payNo,Integer charge,String carNum) throws Exception {
        //发送支付结果通知
        String reqJsonPay = "{\"payNo\":\"" + payNo + "\"," +
                "\"payType\":\"" + ConstantType.JS_PAY_TYPE_OTHER + "\"," +
                "\"chargeTime\":\""+DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS)+"\","+
                "\"charge\":" + charge + "}";
        logger.info("----------开始发送支付结果通知--------------\n"+reqJsonPay);
        JSONObject jsonPay= JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL+"/park/paymentresult",reqJsonPay);
        if(null == jsonPay){
            logger.error("----------发送支付结果通知响应为null--------------");
            return this.faildResponse;
        }
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //添加日志
                    TcCarJsclientlog tjsclientlog = new TcCarJsclientlog();
                    tjsclientlog.setType(1);
                    tjsclientlog.setUrl(JsHttpClientUtil.JS_CLIENT_URL + "park/charging");
                    tjsclientlog.setRmode(1);
                    tjsclientlog.setRparameter(reqJsonPay);
                    tjsclientlog.setContent(jsonPay.toString());
                    tjsclientlog.setCarNum(carNum);
                    tjsclientlog.setRemark("vip及免费车车辆查询");
                    tjsclientlog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    tjsclientlog.setStatus(1);
                    mongoComDAO.executeInsert(tjsclientlog);
                } catch (Exception e) {
                    logger.error("执行错误："+e.getMessage(),e);
                }
            }
        });
        String payCode = jsonPay.getString("code");
        if (null == payCode || !payCode.equals("0")) {
            logger.error("----------发送支付结果通知失败:--------------\n" + jsonPay.toString());
            return this.faildResponse;
        }
        logger.info("----------成功发送支付结果通知--------------" + jsonPay.toString());
        return new JsReturnJsonData("0","",null);
    }



    /**
     * 捷顺
     * 第三方接口
     * 测试远程开闸
     * @param tcCarInout
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public JsReturnJsonData jsOpengate(TcCarInout tcCarInout, Map<String, Object> map) throws Exception {
        try {
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("carNum", tcCarInout.getCarNum());
            conditions.put("status", 1);
             //如果是vip或者免费车,就要给开闸
            //调用捷顺接口远程开闸,调用捷顺接口
            String reqJsonChargStr = "{\"deviceGuid\":\"" + tcCarInout.getOutDeviceId() + "\"," +
                    "\"openModel\":" + tcCarInout.getType() + "," +
                    "\"parkId\":\""+tcCarInout.getParkId()+"\"}";
            JSONObject jsonCharging = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/opengate", reqJsonChargStr);
            if (jsonCharging != null) {
                String code = jsonCharging.getString("code");
                if (code.equals("0")) {
                    return new JsReturnJsonData(DataCodeUtil.SUCCESS + "", "开闸成功", null);
                }else {
                    return new JsReturnJsonData(DataCodeUtil.PARAM_ERROR + "", jsonCharging.getString("msg"), null);
                }
            } else {
                return new JsReturnJsonData(DataCodeUtil.PARAM_ERROR + "", "开闸失败", null);
            }
        }catch (Exception e) {
            logger.info("执行错误"+e.getMessage());
        }
        return new JsReturnJsonData(DataCodeUtil.INSERT_DATABASE + "", "开闸失败", null);
    }


    /**
     * 捷顺
     * 第三方接口
     * 存在优惠券计算
     * @param tcCarInout
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public JsReturnJsonData jsCouponCharging(TcCarInout tcCarInout, Map<String, Object> map) throws Exception {
        try {
            String extendCouponId =  MapUtils.getString(map, "extendCouponId");
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("carNum", tcCarInout.getCarNum());
            conditions.put("status", 1);
            TcCar tcCar = mongoComDAO.executeForObjectByCon(conditions, TcCar.class);
            //捷顺车辆请求计费
            //支付编号
            String payNo = "";
            //应收金额
            Integer chargeTotal = 0;
            //折扣金额
            Integer discountAmount = 0;
            //实收金额
            Integer charge = 0;
            if(tcCar.getIsEntry().equals(1)) {
                TcExtendCoupon tcExtendCoupon = mongoComDAO.executeForObjectById(extendCouponId, TcExtendCoupon.class);
                if (tcExtendCoupon!=null) {
                    TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcExtendCoupon.getCouponId(), TcCoupon.class);
                    String couponName="";
                    String couponWay="";
                    double couponMoney=0;
                    if(tcCoupon!=null) {
                        couponName=tcCoupon.getName();
                        couponWay=tcCoupon.getCouponWay();
                        couponMoney=tcCoupon.getCouponMoney();
                        if(tcCoupon.getType().equals(ConstantType.COUPON_USE_TYPE_MINUSMONEY)) {
                            couponMoney=ToolUtil.getIntToDouble(tcCoupon.getCouponMoney());
                        }
                    }
                    //车场停车优惠下发捷顺
                    //调用捷顺接口
                    String reqJsonCouponStr = "{\"plateNumber\":\"" + tcCar.getCarNum() + "\"," +
                            "\"plateColor\":\"" + tcCar.getCarColor() + "\"," +
                            "\"couponNum\":\"" + tcExtendCoupon.getId() + "\"," +
                            "\"couponName\":\"" + couponName + "\"," +
                            "\"couponWay\":\"" + couponWay + "\"," +
                            "\"couponMoney\":" + couponMoney + "," +
                            "\"couponStatus\":" + ConstantType.JS_COUPON_STATUS_WAITFOR + "," +
                            "\"startTime\":\"" + tcExtendCoupon.getSdate() + "\"," +
                            "\"endTime\":\"" + tcExtendCoupon.getEdate() + "\"}";
                    JSONObject jsonCoupon = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/putdiscount", reqJsonCouponStr);
                    if (jsonCoupon != null) {
                        String code = String.valueOf(jsonCoupon.get("code"));
                        if (!code.equals("0")) {
                            return new JsReturnJsonData(DataCodeUtil.PARAM_ERROR + "", "未找到优惠券信息", null);
                        }
                    } else {
                        return new JsReturnJsonData(DataCodeUtil.UPDATE_DATABASE + "", "未找到优惠券信息", null);
                    }
                    //调用捷顺接口
                    String reqJsonChargStr = "{\"plateNumber\":\"" + tcCar.getCarNum() + "\"," +
                            "\"plateColor\":\"" + tcCar.getCarColor() + "\"," +
                            "\"coupons\":[{\"couponNum\":\"" + tcExtendCoupon.getId() + "\"," +
                            "\"couponName\":\"" + couponName + "\"," +
                            "\"couponWay\":\"" + couponWay + "\"," +
                            "\"couponMoney\":" + couponMoney + "}]}";
                    JSONObject jsonCharging = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/charging", reqJsonChargStr);
                    if (jsonCharging != null) {
                        String code = jsonCharging.getString("code");
                        JSONObject jsondata = jsonCharging.getJSONObject("data");
                        if (!code.equals("0")) {
                            return new JsReturnJsonData(DataCodeUtil.PARAM_ERROR + "", "暂未查询到该车辆的订单", null);
                        }
                        payNo = jsondata.getString("payNo");
                        chargeTotal = jsondata.getInteger("chargeTotal");
                        discountAmount = jsondata.getInteger("discountAmount");
                        charge = jsondata.getInteger("charge");
                    } else {
                        return new JsReturnJsonData(DataCodeUtil.PARAM_ERROR + "", "暂未查询到该车辆的订单", null);
                    }
                }
            }
        }catch (Exception e) {
            logger.info("执行错误"+e.getMessage());
        }
        return new JsReturnJsonData(DataCodeUtil.SUCCESS + "", "查询成功", null);
    }





    @Override
    public ReturnJsonData update(TcCarInout tcCarInout) throws Exception {
        TcCarInout returnTcCarInout = mongoComDAO.executeForObjectById(tcCarInout.getId(), TcCarInout.class);
        if(returnTcCarInout != null){
            returnTcCarInout.setParklotId(tcCarInout.getParklotId());
            returnTcCarInout.setCarId(tcCarInout.getCarId());
            returnTcCarInout.setCarNum(tcCarInout.getCarNum());
            returnTcCarInout.setType(tcCarInout.getType());
            returnTcCarInout.setSdate(tcCarInout.getSdate());
            returnTcCarInout.setEdate(tcCarInout.getEdate());
            returnTcCarInout.setParkHour(tcCarInout.getParkHour());
            returnTcCarInout.setIntimg(tcCarInout.getIntimg());
            returnTcCarInout.setOutimg(tcCarInout.getOutimg());
            int flag = mongoComDAO.executeUpdate(returnTcCarInout);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    @Override
    public ReturnJsonData delete(String[] ids) throws Exception {
         Map<String,Object> condition=new HashMap<>();
         condition.put("ids",ids);
         condition.put("status",2);
        int flag= mongoComDAO.executeUpdateByIds(condition, TcCarInout.class);
        if (flag > 0) {
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "删除成功", null);
        } else {
            return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE, "删除失败", null);
        }
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public ReturnJsonData getById(String id) throws Exception {
        TcCarInout tcCarInout = mongoComDAO.executeForObjectById(id, TcCarInout.class);
        if (tcCarInout!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcCarInout.getId());
            condition.put("parklotId",tcCarInout.getParklotId());
            condition.put("parkId",tcCarInout.getParkId());
            condition.put("carInId",tcCarInout.getCarInId());
            condition.put("inRecordId",tcCarInout.getInRecordId());
            condition.put("inDeviceId",tcCarInout.getInDeviceId());
            condition.put("inDeviceName",tcCarInout.getInDeviceName());
            condition.put("inRecognitionTime",tcCarInout.getInRecognitionTime());
            condition.put("outDeviceId",tcCarInout.getOutDeviceId());
            condition.put("outDeviceName",tcCarInout.getOutDeviceName());
            condition.put("outRecognitionTime",tcCarInout.getOutRecognitionTime());
            condition.put("carId",tcCarInout.getCarId());
            condition.put("carNum",tcCarInout.getCarNum());
            condition.put("carColor",tcCarInout.getCarColor());
            condition.put("type",tcCarInout.getType());
            condition.put("sdate",tcCarInout.getSdate());
            condition.put("edate",tcCarInout.getEdate());
            condition.put("parkHour",tcCarInout.getParkHour());
            condition.put("intimg",tcCarInout.getIntimg());
            condition.put("outimg",tcCarInout.getOutimg());
            condition.put("status",tcCarInout.getStatus());
            condition.put("createTime",tcCarInout.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String carNum =  MapUtils.getString(map, "carNum");
        String outSdate =  MapUtils.getString(map, "outSdate");
        String outEdate =  MapUtils.getString(map, "outEdate");
        //String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //车牌模糊查询
        if(StringUtils.isNotEmpty(carNum)){
            Pattern pattern = Pattern.compile("^.*" + carNum + ".*$");
            Criteria criteria = Criteria.where("carNum").regex(pattern);
            criteriasa.add(criteria);
        }
        //出场开始时间-出场结束时间
        if(StringUtils.isNotEmpty(outSdate) && StringUtils.isNotEmpty(outEdate)){
            Criteria criteria = Criteria.where("outRecognitionTime").gte(outSdate).lte(outEdate);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcCarInout> tcCarInouts = mongoComDAO.executeForObjectList(criteriasa,TcCarInout.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcCarInout.class);
        List<Map<String,Object>> tcCarInoutlist = new ArrayList<>();
        for(TcCarInout tcCarInout : tcCarInouts){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcCarInout.getId());
            condition.put("parklotId",tcCarInout.getParklotId());
            condition.put("parkId",tcCarInout.getParkId());
            condition.put("carInId",tcCarInout.getCarInId());
            condition.put("inRecordId",tcCarInout.getInRecordId());
            condition.put("inDeviceId",tcCarInout.getInDeviceId());
            condition.put("inDeviceName",tcCarInout.getInDeviceName());
            condition.put("inRecognitionTime",tcCarInout.getInRecognitionTime());
            condition.put("outDeviceId",tcCarInout.getOutDeviceId());
            condition.put("outDeviceName",tcCarInout.getOutDeviceName());
            condition.put("outRecognitionTime",tcCarInout.getOutRecognitionTime());
            condition.put("carId",tcCarInout.getCarId());
            condition.put("carNum",tcCarInout.getCarNum());
            condition.put("carColor",tcCarInout.getCarColor());
            condition.put("type",tcCarInout.getType());
            condition.put("sdate",tcCarInout.getSdate());
            condition.put("edate",tcCarInout.getEdate());
            condition.put("parkHour",tcCarInout.getParkHour());
            condition.put("intimg",tcCarInout.getIntimg());
            condition.put("outimg",tcCarInout.getOutimg());
            condition.put("status",tcCarInout.getStatus());
            condition.put("createTime",tcCarInout.getCreateTime());
            tcCarInoutlist.add(condition);
        }
        map.clear();
        map.put("list",tcCarInoutlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }


    /**
     * 根据当前时间
     * 删除数据
     * 根据当前时间，日志30天前的日志删除
     */
    @Override
    public Integer removeBatchTcCarInoutByEdate() throws Exception {
        Integer num=0;
        SimpleDateFormat  sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar =null;
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);//当前时间前去天数,
        String  endTime=sdfhms.format(calendar.getTime()); //当前时间
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //结束时间小于等于当前时间
        Criteria criteria = Criteria.where("createTime").lte(endTime);
        criteriasa.add(criteria);
        num =+ mongoComDAO.executeDelete(criteriasa,TcCarInout.class);
        return num;
    }

}
