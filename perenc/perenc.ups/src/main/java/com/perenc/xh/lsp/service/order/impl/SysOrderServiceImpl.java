package com.perenc.xh.lsp.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.*;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.StringOrDate.MD5Utils;
import com.perenc.xh.commonUtils.utils.StringOrDate.XhrtStringUtils;
import com.perenc.xh.commonUtils.utils.http.HttpCilentUtils;
import com.perenc.xh.commonUtils.utils.jshttp.JsHttpClientUtil;
import com.perenc.xh.commonUtils.utils.lang.RandomUtils;
import com.perenc.xh.commonUtils.utils.pay.LogicException;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.commonUtils.utils.wxUtil.EncryptUtil;
import com.perenc.xh.commonUtils.utils.wxUtil.XmlUtil;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.dao.merchat.MchAccountDao;
import com.perenc.xh.lsp.dao.order.SysOrderDao;
import com.perenc.xh.lsp.dao.wxCustomer.WxCustomerDao;
import com.perenc.xh.lsp.dao.wxCustomerInfo.WxCustomerInfoDao;
import com.perenc.xh.lsp.entity.data.CreateOrderData;
import com.perenc.xh.lsp.entity.data.GoodsInfo;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.order.SysOrder;
import com.perenc.xh.lsp.entity.tcCar.*;
import com.perenc.xh.lsp.entity.tcCoupon.TcCoupon;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCoupon;
import com.perenc.xh.lsp.entity.tcParklot.TcParklot;
import com.perenc.xh.lsp.entity.tcVip.TcVip;
import com.perenc.xh.lsp.entity.tcVip.TcVipCar;
import com.perenc.xh.lsp.entity.weChatInfo.WeChatInfo;
import com.perenc.xh.lsp.entity.wxCustomer.WmCustomer;
import com.perenc.xh.lsp.service.order.SysOrderService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description 系统订单系统
 * @Author xiaobai
 * @Date 2019/5/22 11:36
 **/
@Service("sysOrderService")
@Transactional(rollbackFor = Exception.class)
public class SysOrderServiceImpl implements SysOrderService {

    private static final Logger logger = Logger.getLogger(SysOrderServiceImpl.class);

    @Autowired(required = false)
    private SysOrderDao orderDao;

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private WxCustomerDao customerDao;

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    /**
     * 创建订单
     * @param orderData
     * @return
     */
    @Override
    public ReturnJsonData createOrder(CreateOrderData orderData) throws Exception {
        boolean flag = true;
        //创建订单
        int totalPrice = 0;
        String orderNo = RandomUtils.generateOrderNo("zmsc", 10);
            //停车场微信充值
            if(2 == orderData.getType()){
            //查询充值项
            TcCarRecharge tRecharge = mongoComDAO.executeForObjectById(orderData.getObjId(), TcCarRecharge.class);
            if(tRecharge==null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "传入的充值项Id错误", null);
            }
            totalPrice =tRecharge.getOldPrice()+tRecharge.getGetPrice();
            //实际充值金额
            int oldPrice=tRecharge.getOldPrice();
            SysOrder order = new SysOrder();
            order.setCustomerId(orderData.getCustomerId());
            order.setExtendId(orderData.getExtendId());
            order.setPhone(orderData.getPhone());
            order.setStoreId(ConstantType.SYS_ORDER_TC_STORE_ID);
            order.setObjId(orderData.getObjId());
            order.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            order.setOrderDescribe("停车场充值");
            order.setNumber(1);
            order.setUnitPrice(totalPrice);
            order.setObjTotalPrice(totalPrice);
            order.setTotalPrice(totalPrice);
            order.setActual(tRecharge.getOldPrice());
            order.setFree(tRecharge.getGetPrice());
            order.setType(1);
            order.setPayStatus(0);
            order.setPayType(2);
            order.setOrderStatus(1);
            order.setConfirmType(0);
            if(totalPrice>=ConstantType.INVOICE_MIN_AMOUNT) {
                order.setIsInvoice(1); //是否开发票，1：是，2：否
            }else {
                order.setIsInvoice(2); //是否开发票，1：是，2：否
            }
            order.setInvoiceStatus(1); //开发票状态（1:待申请2：开票中，3:已开发票）
            order.setPayMode(1);//微信支付
            order.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            order.setOrderNo(orderNo);
            int i = orderDao.add(DBUtil.toInsertParam(order));
            if (i < 0) {
                flag = false;
            }
            if (flag) {
                Map<String, Object> condition = new HashMap<>();
                condition.put("orderNo", orderNo);
                condition.put("totalPrice", new BigDecimal(oldPrice / 100D).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加订单成功！", condition);
            }
            //停车场购卡微信充值
        }else if(3 == orderData.getType()){
            //查询Vip信息
            TcVip tcVip = mongoComDAO.executeForObjectById(orderData.getObjId(), TcVip.class);
            if(tcVip==null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的购卡ID错误",null);
            }
            //vip金额
            totalPrice =tcVip.getDiscountPrice();
            SysOrder order = new SysOrder();
            order.setCustomerId(orderData.getCustomerId());
            order.setExtendId(orderData.getExtendId());
            order.setPhone(orderData.getPhone());
            order.setStoreId(orderData.getCarId());
            order.setCarId(orderData.getCarId());
            order.setObjId(orderData.getObjId());
            order.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            order.setOrderDescribe("停车场购卡充值");
            order.setNumber(1);
            order.setUnitPrice(totalPrice);
            order.setTotalPrice(totalPrice);
            order.setObjTotalPrice(totalPrice);
            order.setType(1);
            order.setPayStatus(0);
            order.setPayType(5);
            order.setOrderStatus(1);
            order.setConfirmType(0);
            if(totalPrice>=ConstantType.INVOICE_MIN_AMOUNT) {
                order.setIsInvoice(1); //是否开发票，1：是，2：否
            }else {
                order.setIsInvoice(2); //是否开发票，1：是，2：否
            }
            order.setInvoiceStatus(1); //开发票状态（1:待申请2：开票中，3:已开发票）
            order.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            order.setOrderNo(orderNo);
            int i = orderDao.add(DBUtil.toInsertParam(order));
            if (i < 0) {
                flag = false;
            }
            if (flag) {
                Map<String, Object> condition = new HashMap<>();
                condition.put("orderNo", orderNo);
                condition.put("totalPrice", new BigDecimal(totalPrice / 100D).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加订单成功！", condition);
            }
            //停车停车费微信缴费
        }else if(4 == orderData.getType()){
            Map<String,Object> resultmap = new HashMap<>();
            //用户余额
            double balance=  ToolUtil.getIntToDouble(orderData.getBalance());
            resultmap.put("customerId",orderData.getCustomerId());
            resultmap.put("extendId",orderData.getExtendId());
            resultmap.put("balance",balance);
            //查询车辆
            TcCar tcCar =mongoComDAO.executeForObjectById(orderData.getCarId(), TcCar.class);
            if(tcCar==null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID错误",null);
            }
            //车牌号
            String carNum="";
            //查询停车场、计费规则
            TcParklot tcParklot = mongoComDAO.executeForObjectById(tcCar.getParklotId(), TcParklot.class);
            if(tcParklot != null){
                resultmap.put("parklotId",tcCar.getParklotId());
                resultmap.put("parklotName",tcParklot.getName());
                resultmap.put("parklotDescp",tcParklot.getDescp());
                resultmap.put("unitPrice",ToolUtil.getIntToDouble(tcParklot.getUnitPrice()));
            }
            carNum=tcCar.getCarNum();
            resultmap.put("carNum",carNum);
            //查询可使用的停车券列表
            List<Sort.Order> orders = new ArrayList<Sort.Order>();
            orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
            List<Criteria> criteriasa = new ArrayList<Criteria>();
            Criteria criterib = Criteria.where("useStatus").is(1);
            criteriasa.add(criterib);
            Criteria criterid = Criteria.where("carId").is(tcCar.getId());
            criteriasa.add(criterid);
            Criteria criteric = Criteria.where("status").is(1);
            criteriasa.add(criteric);
            List<TcExtendCoupon> tcExtendCoupons = mongoComDAO.executeForObjectList(criteriasa,TcExtendCoupon.class,orders);
            long count = mongoComDAO.executeForObjectListCount(criteriasa, TcExtendCoupon.class);
            List<Map<String,Object>> tcExtendCouponlist = new ArrayList<>();
            for(TcExtendCoupon tcExtendCoupon : tcExtendCoupons){
                Map<String,Object> condition = new HashMap<>();
                condition.put("id",tcExtendCoupon.getId());
                condition.put("couponId",tcExtendCoupon.getCouponId());
                TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcExtendCoupon.getCouponId(), TcCoupon.class);
                if(tcCoupon != null){
                    condition.put("couponName",tcCoupon.getName());
                    condition.put("couponDesc",tcCoupon.getDesc());
                    condition.put("couponDuration",tcCoupon.getDuration());
                    condition.put("couponAmount",ToolUtil.getIntToDouble(tcCoupon.getAmount()));
                }else {
                    condition.put("couponName","");
                    condition.put("couponDesc","");
                    condition.put("couponDuration","");
                    condition.put("couponAmount",0);
                }
                condition.put("extendId",tcExtendCoupon.getExtendId());
                condition.put("sellerId",tcExtendCoupon.getSellerId());
                condition.put("ticketImg",tcExtendCoupon.getTicketImg());
                condition.put("useStatus",tcExtendCoupon.getUseStatus());
                condition.put("status",tcExtendCoupon.getStatus());
                condition.put("createTime",tcExtendCoupon.getCreateTime());
                tcExtendCouponlist.add(condition);
            }
            resultmap.put("couponList",tcExtendCouponlist);//返回前端集合命名为list
            resultmap.put("couponTotal",count);//总条数
            //查询停车费用
            //支付编号
            String payNo="";
            String stopTime="";
            //捷顺车辆请求计费
            String reqJsonStr = "{\"plateNumber\":\""+tcCar.getCarNum()+"\"}";
            JSONObject jsonCharging = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/charging", reqJsonStr);
            //添加日志
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //添加日志
                        TcCarJsclientlog tjsclientlog=new TcCarJsclientlog();
                        tjsclientlog.setType(1);
                        tjsclientlog.setUrl(JsHttpClientUtil.JS_CLIENT_URL+"park/charging");
                        tjsclientlog.setRmode(1);
                        tjsclientlog.setRparameter(reqJsonStr);
                        tjsclientlog.setContent(jsonCharging.toString());
                        tjsclientlog.setCarNum(tcCar.getCarNum());
                        tjsclientlog.setRemark("车辆查询创建定单");
                        tjsclientlog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                        tjsclientlog.setStatus(1);
                        mongoComDAO.executeInsert(tjsclientlog);
                    } catch (Exception e) {
                        logger.error("执行错误："+e.getMessage(),e);
                    }
                }
            });
            String code = jsonCharging.getString("code");
            JSONObject jsondata  = jsonCharging.getJSONObject("data");
            if (code.equals("0")) {
                payNo = jsondata.getString("payNo");
                // 实收金额
                totalPrice = jsondata.getInteger("charge");
                stopTime = jsondata.getString("stopTime");
            }else {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, jsonCharging.getString("msg"), null);
            }
            SysOrder order = new SysOrder();
            order.setCustomerId(orderData.getCustomerId());
            order.setExtendId(orderData.getExtendId());
            order.setPhone(orderData.getPhone());
            order.setStoreId(orderData.getCarId());
            order.setObjId(payNo);
            order.setCarId(orderData.getCarId());
            order.setCarNum(tcCar.getCarNum());
            order.setJspayNo(payNo);
            order.setParklotId(tcCar.getParklotId());
            order.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            order.setOrderDescribe("停车场出场缴费");
            order.setNumber(1);
            order.setUnitPrice(totalPrice);
            order.setTotalPrice(totalPrice);
            order.setType(1);
            order.setPayStatus(0);
            order.setPayType(1);
            order.setOrderStatus(1);
            order.setConfirmType(0);
            if(totalPrice>=ConstantType.INVOICE_MIN_AMOUNT) {
                order.setIsInvoice(1); //是否开发票，1：是，2：否
            }else {
                order.setIsInvoice(2); //是否开发票，1：是，2：否
            }
            order.setInvoiceStatus(1); //开发票状态（1:待申请2：开票中，3:已开发票）
            order.setCommetStatus(0);//评论 0=未评论;1=评论
            order.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            order.setOrderNo(orderNo);
            int i = orderDao.add(DBUtil.toInsertParam(order));
            if (i < 0) {
                flag = false;
            }
            if (flag) {
                resultmap.put("duration",stopTime);
                resultmap.put("orderNo", orderNo);
                resultmap.put("totalPrice", new BigDecimal(totalPrice / 100D).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加订单成功！", resultmap);
            }
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE, "添加订单失败！", null);
    }

    /**
     * 确认订单
     * @param map
     * @return
     */
    @Override
    public ReturnJsonData confirmOrder(Map<String,Object> map) throws Exception {
        String spbillCreateIp = MapUtils.getString(map, "spbillCreateIp", "");
        String payType = MapUtils.getString(map, "payType", "");
        String remark = MapUtils.getString(map, "remark", "");
        double totalPrice = MapUtils.getDoubleValue(map, "totalPrice", 0);
        String orderNo = MapUtils.getString(map, "orderNo", "");

        //停车场
        //停车场优惠券
        String extendId = MapUtils.getString(map, "extendId", "");
        String extendCouponId = MapUtils.getString(map, "extendCouponId", "");
        // 余额支付密码
        String payPsword = MapUtils.getString(map, "payPsword", "");
        String payMode = MapUtils.getString(map, "payMode", "");

            //停车场微信充值支付 payType=2
            if("2".equals(payType)){
            //查询用户
            ExtendUser extendUser = extendUserDao.getById(Integer.valueOf(extendId));
            if(extendUser==null) {
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "用户信息不存在", null);
            }
            //用户
            WmCustomer customer = customerDao.getById(extendUser.getCustomerId());
            if (customer == null) {
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "用户信息不存在", null);
            }
            int totalAmount = 0;
            //实际充值的金额
            int oldPrice = 0;
            boolean flag = true;
            QueryParam param = new QueryParam();
            param.addCondition("order_no", "=", orderNo);
            SysOrder order =orderDao.getOne(param);
            if(order==null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的订单号错误",null);
            }
            totalAmount = totalAmount + order.getTotalPrice();
            oldPrice=oldPrice+order.getActual();
            //检测传上的总金额和订单里面的总金额是否相等
            if(totalPrice * 100 == oldPrice){
                order.setPayMode(1); //微信支付
                order.setTotalPrice(totalAmount);
                order.setActual(oldPrice);
                order.setWechat(oldPrice);
                order.setRemark(remark);
                order.setConfirmType(1);
                int i = orderDao.update(DBUtil.toUpdateParam(order, "id"));
                if (i< 0) {
                    flag = false;
                }
                if(flag) {
                    //获取相关的微信信息
                    map.clear();
                    map.put("mchAccountId", 3);
                    map.put("type", 3);
                    map.put("useType", 1);
                    WeChatInfo weChatInfo = mongoComDAO.executeForObjectByCon(map, WeChatInfo.class);
                    if (weChatInfo != null) {
                        //向微信统一下单
                        return unifyWxOrder(weChatInfo, orderNo, oldPrice, spbillCreateIp, customer.getOpenId());
                    } else {
                        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE, "请联系后台人员，完善微信相关配置", null);
                    }
                }
            }else{
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入总金额和订单总金额不匹配",null);
            }
            //购卡支付 payType=3,支付方式：2:余额支付
        }else if("3".equals(payType) && payMode.equals("2")){
            //查询用户
            ExtendUser extendUser = extendUserDao.getById(Integer.valueOf(extendId));
            if(extendUser==null) {
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "用户信息不存在", null);
            }
            int totalAmount = 0;
            boolean flag = true;

            QueryParam param = new QueryParam();
            param.addCondition("order_no", "=", orderNo);
            SysOrder order =orderDao.getOne(param);
            if(order==null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的订单号错误",null);
            }
            totalAmount = totalAmount + order.getTotalPrice();
            if(extendUser.getBalance()<totalAmount) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"余额不足",null);
            }
            //检测传上的总金额和订单里面的总金额是否相等
            if(totalPrice * 100 == totalAmount){
                order.setPayMode(2); //余额支付
                order.setTotalPrice(totalAmount);
                order.setActual(totalAmount);
                order.setBalance(totalAmount);
                order.setRemark(remark);
                order.setConfirmType(1);
                order.setPayStatus(1);//支付完成
                order.setOrderStatus(2); //支付成功
                order.setPayTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                int i = orderDao.update(DBUtil.toUpdateParam(order, "id"));
                if (i< 0) {
                    flag = false;
                }
                if(flag) {
                    //获取相关的微信信息
                    Integer utotalPrice=extendUser.getBalance()-totalAmount;
                    extendUser.setBalance(utotalPrice);
                    extendUser.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    int flaguser = extendUserDao.update(DBUtil.toUpdateParam(extendUser, "id"));
                    if(flaguser > 0){
                        return new ReturnJsonData(DataCodeUtil.SUCCESS,"支付成功",null);
                    }else {
                        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE, "支付失败", null);
                    }
                }
            }else{
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入总金额和订单总金额不匹配",null);
            }
            //停车场缴费支付 payType=4,支付方式：1:微信在线支付
        }else if("4".equals(payType) && payMode.equals("1")){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = null;
            calendar = Calendar.getInstance();
            String strNowTime = sdf.format(calendar.getTime());
            //查询用户
            ExtendUser extendUser = extendUserDao.getById(Integer.valueOf(extendId));
            if(extendUser==null) {
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "用户信息不存在", null);
            }
            //用户
            WmCustomer customer = customerDao.getById(extendUser.getCustomerId());
            if (customer == null) {
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "用户信息不存在", null);
            }
            //int totalAmount = 0;
            boolean flag = true;

            QueryParam param = new QueryParam();
            param.addCondition("order_no", "=", orderNo);
            SysOrder order =orderDao.getOne(param);
            if(order==null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的订单号错误",null);
            }
            //totalAmount = order.getTotalPrice();
            //查询车辆
            TcCar tcCar = mongoComDAO.executeForObjectById(order.getCarId(), TcCar.class);
            if(tcCar==null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"查询的车辆信息失败",null);
            }
            //如果优惠券为空
            if(StringUtils.isEmpty(extendCouponId)){
                //捷顺车辆请求计费
                //支付编号
                String payNo="";
                //应收金额
                Integer chargeTotal=0;
                //折扣金额
                Integer discountAmount=0;
                //实收金额
                Integer charge=0;
                //捷顺停车开始时间
                String inTime="";
                //捷顺停车结束时间
                String feesTime="";
                //捷顺停车时长
                String stopTime="";
                //调用捷顺接口
                String reqJsonChargStr = "{\"plateNumber\":\""+tcCar.getCarNum()+"\"}";
                JSONObject jsonCharging = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/charging", reqJsonChargStr);
                //添加日志
                threadPoolTaskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TcCarJsclientlog tjsclientlog=new TcCarJsclientlog();
                            tjsclientlog.setType(1);
                            tjsclientlog.setUrl(JsHttpClientUtil.JS_CLIENT_URL+"park/charging");
                            tjsclientlog.setRmode(1);
                            tjsclientlog.setRparameter(reqJsonChargStr);
                            tjsclientlog.setContent(jsonCharging.toString());
                            tjsclientlog.setCarNum(tcCar.getCarNum());
                            tjsclientlog.setRemark("微信支付车辆查询");
                            tjsclientlog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                            tjsclientlog.setStatus(1);
                            mongoComDAO.executeInsert(tjsclientlog);
                        } catch (Exception e) {
                            logger.error("执行错误："+e.getMessage(),e);
                        }
                    }
                });
                String code = jsonCharging.getString("code");
                JSONObject jsondata  = jsonCharging.getJSONObject("data");
                if (code.equals("0")) {
                    payNo = jsondata.getString("payNo");
                    chargeTotal = jsondata.getInteger("chargeTotal");
                    discountAmount = jsondata.getInteger("discountAmount");
                    charge = jsondata.getInteger("charge");
                    inTime = jsondata.getString("inTime");
                    feesTime = jsondata.getString("feesTime");
                    stopTime = jsondata.getString("stopTime");

                }else {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "暂未查询到该车辆的订单", null);
                }
                //更改停车时间及时长
                //停车时长（小时）：当前时间-入场时间
                double carDuration=ToolUtil.getDateHourNum(tcCar.getCostSdate(),strNowTime);
                order.setParkStartTime(tcCar.getCostSdate());
                order.setParkEndTime(strNowTime);
                order.setParkDuration(ToolUtil.getDoubleToInt(carDuration));
                //修改订单信息
                order.setJspayNo(payNo);
                order.setJschargeTotal(chargeTotal);
                order.setJsdiscountAmount(discountAmount);
                order.setJscharge(charge);
                order.setJsinTime(inTime);
                order.setJsfeesTime(feesTime);
                order.setJsstopTime(stopTime);
                //之前
                order.setPayMode(1); //微信支付
                order.setObjTotalPrice(chargeTotal);
                order.setTotalPrice(charge);
                order.setActual(charge);
                order.setWechat(charge);
                order.setRemark(remark);
                order.setConfirmType(1);
                int i = orderDao.update(DBUtil.toUpdateParam(order, "id"));
                if (i< 0) {
                    flag = false;
                }
                if(flag) {
                    //获取相关的微信信息
                    map.clear();
                    map.put("mchAccountId", 3);
                    map.put("type", 3);
                    map.put("useType", 1);
                    WeChatInfo weChatInfo = mongoComDAO.executeForObjectByCon(map, WeChatInfo.class);
                    if (weChatInfo != null) {
                        //向微信统一下单
                        return unifyWxOrder(weChatInfo, orderNo, charge, spbillCreateIp, customer.getOpenId());
                    } else {
                        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE, "请联系后台人员，完善微信相关配置", null);
                    }
                }
            }else{
                //有优惠券情况  优惠券规则
                TcExtendCoupon tcExtendCoupon = mongoComDAO.executeForObjectById(extendCouponId, TcExtendCoupon.class);
                if (tcExtendCoupon==null) {
                    return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE, "未找到停车券", null);
                }
                String couponName="";
                String couponWay="";
                double couponMoney=0;
                TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcExtendCoupon.getCouponId(), TcCoupon.class);
                if(tcCoupon==null) {
                    return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE, "未找到停车券", null);
                }
                couponName=tcCoupon.getName();
                couponWay=tcCoupon.getCouponWay();
                couponMoney=tcCoupon.getCouponMoney();
                if(tcCoupon.getType().equals(ConstantType.COUPON_USE_TYPE_MINUSMONEY)) {
                    couponMoney=ToolUtil.getIntToDouble(tcCoupon.getCouponMoney());
                }
                //捷顺车辆请求计费
                //支付编号
                String payNo = "";
                //应收金额
                Integer chargeTotal = 0;
                //折扣金额
                Integer discountAmount = 0;
                //实收金额
                Integer charge = 0;
                //捷顺停车开始时间
                String inTime="";
                //捷顺停车结束时间
                String feesTime="";
                //捷顺停车时长
                String stopTime="";
                //调用捷顺接口
                String reqJsonChargStr = "{\"plateNumber\":\"" + tcCar.getCarNum() + "\"," +
                        "\"plateColor\":\"" + tcCar.getCarColor() + "\"," +
                        "\"coupons\":[{\"couponNum\":\"" + tcExtendCoupon.getId() + "\"," +
                        "\"couponName\":\"" + couponName + "\"," +
                        "\"couponWay\":\"" + couponWay + "\"," +
                        "\"couponMoney\":" + couponMoney + "}]}";
                JSONObject jsonCharging = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/charging", reqJsonChargStr);
                //添加日志
                threadPoolTaskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TcCarJsclientlog tjsclientlogc=new TcCarJsclientlog();
                            tjsclientlogc.setType(1);
                            tjsclientlogc.setUrl(JsHttpClientUtil.JS_CLIENT_URL+"park/charging");
                            tjsclientlogc.setRmode(1);
                            tjsclientlogc.setRparameter(reqJsonChargStr);
                            tjsclientlogc.setContent(jsonCharging.toString());
                            tjsclientlogc.setCarNum(tcCar.getCarNum());
                            tjsclientlogc.setRemark("微信支付车辆查询使用停车券");
                            tjsclientlogc.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                            tjsclientlogc.setStatus(1);
                            mongoComDAO.executeInsert(tjsclientlogc);
                        } catch (Exception e) {
                            logger.error("执行错误："+e.getMessage(),e);
                        }
                    }
                });
                String codec = jsonCharging.getString("code");
                JSONObject jsondata  = jsonCharging.getJSONObject("data");
                if(codec.equals("0")) {
                    payNo = jsondata.getString("payNo");
                    chargeTotal = jsondata.getInteger("chargeTotal");
                    discountAmount = jsondata.getInteger("discountAmount");
                    charge = jsondata.getInteger("charge");
                    inTime = jsondata.getString("inTime");
                    feesTime = jsondata.getString("feesTime");
                    stopTime = jsondata.getString("stopTime");
                }else {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "暂未查询到该车辆的订单", null);
                }
                //更改使用券
                order.setCouponId(tcExtendCoupon.getId());
                order.setCouponDuration(tcCoupon.getDuration());
                order.setCouponAmount(tcCoupon.getCouponMoney());
                order.setFree(discountAmount);
                //更改停车时间及时长
                //停车时长（小时）：当前时间-入场时间
                double carDuration=ToolUtil.getDateHourNum(tcCar.getCostSdate(),strNowTime);
                order.setParkStartTime(tcCar.getCostSdate());
                order.setParkEndTime(strNowTime);
                order.setParkDuration(ToolUtil.getDoubleToInt(carDuration));
                //修改订单信息
                order.setJspayNo(payNo);
                order.setJschargeTotal(chargeTotal);
                order.setJsdiscountAmount(discountAmount);
                order.setJscharge(charge);
                order.setJsinTime(inTime);
                order.setJsfeesTime(feesTime);
                order.setJsstopTime(stopTime);
                //之前
                order.setPayMode(1); //微信支付
                order.setObjTotalPrice(chargeTotal);
                order.setTotalPrice(charge);
                order.setActual(charge);
                order.setWechat(charge);
                order.setRemark(remark);
                order.setConfirmType(1);
                int i = orderDao.update(DBUtil.toUpdateParam(order, "id"));
                if (i< 0) {
                    flag = false;
                }
                if(flag) {
                    //获取相关的微信信息
                    map.clear();
                    map.put("mchAccountId",3);
                    map.put("type", 3);
                    map.put("useType", 1);
                    WeChatInfo weChatInfo = mongoComDAO.executeForObjectByCon(map, WeChatInfo.class);
                    if (weChatInfo != null) {
                        return unifyWxOrder(weChatInfo, orderNo, charge, spbillCreateIp, customer.getOpenId());
                    } else {
                        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE, "请联系后台人员，完善微信相关配置", null);
                    }
                }
            }
            //停车场缴费支付 payType=4,支付方式：2:余额支付
        }else if("4".equals(payType) && payMode.equals("2")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calendar = null;
                calendar = Calendar.getInstance();
                String strNowTime = sdf.format(calendar.getTime());
                //查询用户
                ExtendUser extendUser = extendUserDao.getById(Integer.valueOf(extendId));
                if (extendUser == null) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "用户信息不存在", null);
                }
                //int totalAmount = 0;
                boolean flag = true;

                QueryParam param = new QueryParam();
                param.addCondition("order_no", "=", orderNo);
                SysOrder order = orderDao.getOne(param);
                if (order == null) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "传入的订单号错误", null);
                }
                //totalAmount = totalAmount + order.getTotalPrice();
                //查询车辆
                TcCar tcCar = mongoComDAO.executeForObjectById(order.getCarId(), TcCar.class);
                if (tcCar == null) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "查询的车辆信息失败", null);
                }
                //如果优惠券为空
                if (StringUtils.isEmpty(extendCouponId)) {
                    //检测传上的总金额和订单里面的总金额是否相等
                    //捷顺车辆请求计费
                    //支付编号
                    String payNo = "";
                    //应收金额
                    Integer chargeTotal = 0;
                    //折扣金额
                    Integer discountAmount = 0;
                    //实收金额
                    Integer charge = 0;
                    //捷顺停车开始时间
                    String inTime = "";
                    //捷顺停车结束时间
                    String feesTime = "";
                    //捷顺停车时长
                    String stopTime = "";
                    //调用捷顺接口
                    String reqJsonChargStr = "{\"plateNumber\":\"" + tcCar.getCarNum() + "\"}";
                    JSONObject jsonCharging = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/charging", reqJsonChargStr);
                    //添加日志
                    threadPoolTaskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                TcCarJsclientlog tjsclientlog = new TcCarJsclientlog();
                                tjsclientlog.setType(1);
                                tjsclientlog.setUrl(JsHttpClientUtil.JS_CLIENT_URL + "park/charging");
                                tjsclientlog.setRmode(1);
                                tjsclientlog.setRparameter(reqJsonChargStr);
                                tjsclientlog.setContent(jsonCharging.toString());
                                tjsclientlog.setCarNum(tcCar.getCarNum());
                                tjsclientlog.setRemark("余额支付车辆查询");
                                tjsclientlog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                                tjsclientlog.setStatus(1);
                                mongoComDAO.executeInsert(tjsclientlog);
                            } catch (Exception e) {
                                logger.error("执行错误：" + e.getMessage(), e);
                            }
                        }
                    });
                    String code = jsonCharging.getString("code");
                    JSONObject jsondata = jsonCharging.getJSONObject("data");
                    if (code.equals("0")) {
                        payNo = jsondata.getString("payNo");
                        chargeTotal = jsondata.getInteger("chargeTotal");
                        discountAmount = jsondata.getInteger("discountAmount");
                        charge = jsondata.getInteger("charge");
                        inTime = jsondata.getString("inTime");
                        feesTime = jsondata.getString("feesTime");
                        stopTime = jsondata.getString("stopTime");
                    } else {
                        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "暂未查询到该车辆的订单", null);
                    }
                    if (charge > 0) {
                        if (StringUtils.isEmpty(payPsword)) {
                            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请输入支付密码", null);
                        }
                        String md5payPsword = MD5Utils.MD5Encode(payPsword, "utf8");
                        if (!md5payPsword.equals(extendUser.getPayPsword())) {
                            return new ReturnJsonData(DataCodeUtil.INPUT_ERROR_PASSWORD_FORGET_PASSWORD, "支付密码错误", null);
                        }
                    }
                    if (extendUser.getBalance() < charge) {
                        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "余额不足", null);
                    }
                    //更改停车时间及时长
                    //停车时长（小时）：当前时间-入场时间
                    double carDuration = ToolUtil.getDateHourNum(tcCar.getCostSdate(), strNowTime);
                    order.setParkStartTime(tcCar.getCostSdate());
                    order.setParkEndTime(strNowTime);
                    order.setParkDuration(ToolUtil.getDoubleToInt(carDuration));
                    //修改订单信息
                    order.setJspayNo(payNo);
                    order.setJschargeTotal(chargeTotal);
                    order.setJsdiscountAmount(discountAmount);
                    order.setJscharge(charge);
                    order.setJsinTime(inTime);
                    order.setJsfeesTime(feesTime);
                    order.setJsstopTime(stopTime);
                    //之前
                    order.setPayMode(2); //余额支付
                    order.setObjTotalPrice(chargeTotal);
                    order.setTotalPrice(charge);
                    order.setActual(charge);
                    order.setBalance(charge);
                    order.setRemark(remark);
                    order.setConfirmType(1);
                    order.setPayStatus(1);//支付完成
                    order.setOrderStatus(2); //支付成功
                    order.setPayTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    int i = orderDao.update(DBUtil.toUpdateParam(order, "id"));
                    if (i < 0) {
                        flag = false;
                    }
                    if (flag) {
                        //获取相关的微信信息
                        //当客户余额大于等于支付余额
                        Integer utotalPrice = extendUser.getBalance() - charge;
                        extendUser.setBalance(utotalPrice);
                        extendUser.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                        int flaguser = extendUserDao.update(DBUtil.toUpdateParam(extendUser, "id"));
                        if (flaguser > 0) {
                            //捷顺车辆支付结果通知
                            //调用捷顺接口
                            String reqJsonPresutstr = "{\"payNo\":\"" + payNo + "\"," +
                                    "\"payType\":\"" + ConstantType.JS_PAY_TYPE_OTHER + "\"," +
                                    "\"chargeTime\":\"" + order.getPayTime() + "\"," +
                                    "\"charge\":" + charge + "}";
                            JSONObject jsonPresut = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/paymentresult", reqJsonPresutstr);
                            //添加日志
                            threadPoolTaskExecutor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        TcCarJsclientlog tjsclientlog = new TcCarJsclientlog();
                                        tjsclientlog.setType(1);
                                        tjsclientlog.setUrl(JsHttpClientUtil.JS_CLIENT_URL + "park/paymentresult");
                                        tjsclientlog.setRmode(1);
                                        tjsclientlog.setRparameter(reqJsonPresutstr);
                                        tjsclientlog.setContent(jsonPresut.toString());
                                        tjsclientlog.setCarNum(tcCar.getCarNum());
                                        tjsclientlog.setRemark("余额支付支付结果通知");
                                        tjsclientlog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                                        tjsclientlog.setStatus(1);
                                        mongoComDAO.executeInsert(tjsclientlog);
                                    } catch (Exception e) {
                                        logger.error("执行错误：" + e.getMessage(), e);
                                    }
                                }
                            });
                            return new ReturnJsonData(DataCodeUtil.SUCCESS, "支付成功", null);
                        } else {
                            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE, "支付失败", null);
                        }
                    }
                } else {
                    //有优惠券情况  优惠券规则
                    TcExtendCoupon tcExtendCoupon = mongoComDAO.executeForObjectById(extendCouponId, TcExtendCoupon.class);
                    //捷顺车辆请求计费
                    //支付编号
                    String payNo = "";
                    //应收金额
                    Integer chargeTotal = 0;
                    //折扣金额
                    Integer discountAmount = 0;
                    //实收金额
                    Integer charge = 0;
                    //捷顺停车开始时间
                    String inTime = "";
                    //捷顺停车结束时间
                    String feesTime = "";
                    //捷顺停车时长
                    String stopTime = "";
                    if (tcExtendCoupon == null) {
                        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE, "未找到停车券", null);
                    }
                    String couponName = "";
                    String couponWay = "";
                    double couponMoney = 0;
                    TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcExtendCoupon.getCouponId(), TcCoupon.class);
                    if (tcCoupon == null) {
                        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE, "未找到停车券", null);
                    }
                    couponName = tcCoupon.getName();
                    couponWay = tcCoupon.getCouponWay();
                    if (tcCoupon.getType().equals(ConstantType.COUPON_USE_TYPE_MINUSMONEY)) {
                        couponMoney = ToolUtil.getIntToDouble(tcCoupon.getCouponMoney());
                    }
                    //调用捷顺接口
                    String reqJsonChargStr = "{\"plateNumber\":\"" + tcCar.getCarNum() + "\"," +
                            "\"plateColor\":\"" + tcCar.getCarColor() + "\"," +
                            "\"coupons\":[{\"couponNum\":\"" + tcExtendCoupon.getId() + "\"," +
                            "\"couponName\":\"" + couponName + "\"," +
                            "\"couponWay\":\"" + couponWay + "\"," +
                            "\"couponMoney\":" + couponMoney + "}]}";
                    JSONObject jsonCharging = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/charging", reqJsonChargStr);
                    //添加日志
                    threadPoolTaskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                TcCarJsclientlog tjsclientlogc = new TcCarJsclientlog();
                                tjsclientlogc.setType(1);
                                tjsclientlogc.setUrl(JsHttpClientUtil.JS_CLIENT_URL + "park/charging");
                                tjsclientlogc.setRmode(1);
                                tjsclientlogc.setRparameter(reqJsonChargStr);
                                tjsclientlogc.setContent(jsonCharging.toString());
                                tjsclientlogc.setCarNum(tcCar.getCarNum());
                                tjsclientlogc.setRemark("余额支付车辆使用停车券");
                                tjsclientlogc.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                                tjsclientlogc.setStatus(1);
                                mongoComDAO.executeInsert(tjsclientlogc);
                            } catch (Exception e) {
                                logger.error("执行错误：" + e.getMessage(), e);
                            }
                        }
                    });
                    String codec = jsonCharging.getString("code");
                    JSONObject jsondatac = jsonCharging.getJSONObject("data");
                    if (codec.equals("0")) {
                        payNo = jsondatac.getString("payNo");
                        chargeTotal = jsondatac.getInteger("chargeTotal");
                        discountAmount = jsondatac.getInteger("discountAmount");
                        charge = jsondatac.getInteger("charge");
                        inTime = jsondatac.getString("inTime");
                        feesTime = jsondatac.getString("feesTime");
                        stopTime = jsondatac.getString("stopTime");
                    } else {
                        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "暂未查询到该车辆的订单", null);
                    }
                    if (extendUser.getBalance() < charge && charge > 0) {
                        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "余额不足", null);
                    }
                    if (charge > 0) {
                        if (StringUtils.isEmpty(payPsword)) {
                            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请输入支付密码", null);
                        }
                        String md5payPsword = MD5Utils.MD5Encode(payPsword, "utf8");
                        if (!md5payPsword.equals(extendUser.getPayPsword())) {
                            return new ReturnJsonData(DataCodeUtil.INPUT_ERROR_PASSWORD_FORGET_PASSWORD, "支付密码错误", null);
                        }
                    }
                    //更改使用券
                    order.setCouponId(tcExtendCoupon.getId());
                    order.setCouponDuration(tcCoupon.getDuration());
                    order.setCouponAmount(tcCoupon.getCouponMoney());
                    order.setFree(discountAmount);
                    //更改停车时间及时长
                    //停车时长（小时）：当前时间-入场时间
                    double carDuration = ToolUtil.getDateHourNum(tcCar.getCostSdate(), strNowTime);
                    order.setParkStartTime(tcCar.getCostSdate());
                    order.setParkEndTime(strNowTime);
                    order.setParkDuration(ToolUtil.getDoubleToInt(carDuration));
                    //修改订单信息
                    order.setJspayNo(payNo);
                    order.setJschargeTotal(chargeTotal);
                    order.setJsdiscountAmount(discountAmount);
                    order.setJscharge(charge);
                    order.setJsinTime(inTime);
                    order.setJsfeesTime(feesTime);
                    order.setJsstopTime(stopTime);
                    //之前
                    if (charge.equals(0)) {
                        order.setPayMode(3); //停车券抵扣
                    } else {
                        order.setPayMode(2); //余额支付
                    }
                    order.setObjTotalPrice(chargeTotal);
                    order.setTotalPrice(charge);
                    order.setActual(charge);
                    order.setBalance(charge);
                    order.setRemark(remark);
                    order.setConfirmType(1);
                    order.setPayStatus(1);//支付完成
                    order.setOrderStatus(2); //支付成功
                    order.setPayTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    int i = orderDao.update(DBUtil.toUpdateParam(order, "id"));
                    if (i < 0) {
                        flag = false;
                    }
                    if (flag) {
                        //获取相关的微信信息
                        //当实际支付的金额等于0时
                        if (charge.equals(0)) {
                            tcExtendCoupon.setUseStatus(ConstantType.USE_STATUS_USED);
                            int flagecoupon = mongoComDAO.executeUpdate(tcExtendCoupon);
                            if (flagecoupon > 0) {
                                //捷顺车辆支付结果通知
                                //调用捷顺接口
                                String reqJsonPresutstr = "{\"payNo\":\"" + payNo + "\"," +
                                        "\"payType\":\"" + ConstantType.JS_PAY_TYPE_OTHER + "\"," +
                                        "\"chargeTime\":\"" + order.getPayTime() + "\"," +
                                        "\"charge\":" + charge + "}";
                                JSONObject jsonPresut = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/paymentresult", reqJsonPresutstr);
                                //添加日志
                                threadPoolTaskExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            TcCarJsclientlog tjsclientlog = new TcCarJsclientlog();
                                            tjsclientlog.setType(1);
                                            tjsclientlog.setUrl(JsHttpClientUtil.JS_CLIENT_URL + "park/paymentresult");
                                            tjsclientlog.setRmode(1);
                                            tjsclientlog.setRparameter(reqJsonPresutstr);
                                            tjsclientlog.setContent(jsonPresut.toString());
                                            tjsclientlog.setCarNum(tcCar.getCarNum());
                                            tjsclientlog.setRemark("余额支付支付结果通知使用停车券");
                                            tjsclientlog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                                            tjsclientlog.setStatus(1);
                                            mongoComDAO.executeInsert(tjsclientlog);
                                        } catch (Exception e) {
                                            logger.error("执行错误：" + e.getMessage(), e);
                                        }
                                    }
                                });
                                return new ReturnJsonData(DataCodeUtil.SUCCESS, "支付成功", null);
                            } else {
                                return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE, "支付失败", null);
                            }
                        } else {
                            int flagecoupon = 0;
                            tcExtendCoupon.setUseStatus(ConstantType.USE_STATUS_USED);
                            flagecoupon = mongoComDAO.executeUpdate(tcExtendCoupon);
                            // 更改金额
                            Integer utotalPrice = extendUser.getBalance() - charge;
                            extendUser.setBalance(utotalPrice);
                            extendUser.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                            int flaguser = extendUserDao.update(DBUtil.toUpdateParam(extendUser, "id"));
                            if (flagecoupon > 0 && flaguser > 0) {
                                //捷顺车辆支付结果通知
                                //调用捷顺接口
                                String reqJsonPresutstr = "{\"payNo\":\"" + payNo + "\"," +
                                        "\"payType\":\"" + ConstantType.JS_PAY_TYPE_OTHER + "\"," +
                                        "\"chargeTime\":\"" + order.getPayTime() + "\"," +
                                        "\"charge\":" + charge + "}";
                                JSONObject jsonPresut = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/paymentresult", reqJsonPresutstr);
                                //添加日志
                                threadPoolTaskExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            TcCarJsclientlog tjsclientlog = new TcCarJsclientlog();
                                            tjsclientlog.setType(1);
                                            tjsclientlog.setUrl(JsHttpClientUtil.JS_CLIENT_URL + "park/paymentresult");
                                            tjsclientlog.setRmode(1);
                                            tjsclientlog.setRparameter(reqJsonPresutstr);
                                            tjsclientlog.setContent(jsonPresut.toString());
                                            tjsclientlog.setCarNum(tcCar.getCarNum());
                                            tjsclientlog.setRemark("余额支付支付结果通知");
                                            tjsclientlog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                                            tjsclientlog.setStatus(1);
                                            mongoComDAO.executeInsert(tjsclientlog);
                                        } catch (Exception e) {
                                            logger.error("执行错误：" + e.getMessage(), e);
                                        }
                                    }
                                });
                                return new ReturnJsonData(DataCodeUtil.SUCCESS, "支付成功", null);
                            } else {
                                return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE, "支付失败", null);
                            }
                        }
                    }
                }
            }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE, "支付失败！", null);
    }

    /**
     * 微信支付通知回调
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData notifyWechat(Map<String, Object> map) throws Exception {
        String orderNo = MapUtils.getString(map, "orderNo", "");
        String amount = MapUtils.getString(map, "amount", "");
        String transactionId = MapUtils.getString(map, "transactionId", "");
        if(StringUtils.isNotEmpty(orderNo)) {
            QueryParam param = new QueryParam();
            param.put("order_no", orderNo);
            List<SysOrder> orderList = orderDao.list(param);
            for (SysOrder order : orderList) {
                order.setTransactionId(transactionId);
                order.setPayStatus(1);//支付完成
                order.setOrderStatus(2);//待发货
                if (order.getPayType() == 2) {//微信充值,更改余额
                    order.setOrderStatus(2);//已支付
                    order.setPayTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    int flag = orderDao.update(DBUtil.toUpdateParam(order, "id"));
                    if (flag > 0) {
                        //查询用户
                        QueryParam userParam = new QueryParam();
                        userParam.addCondition("customer_id", "=", order.getCustomerId());
                        userParam.addCondition("status", "=", 1);
                        ExtendUser extendUser = extendUserDao.getOne(userParam);
                        Integer totalPrice = extendUser.getBalance() + order.getTotalPrice();
                        extendUser.setBalance(totalPrice);
                        extendUser.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                        extendUserDao.update(DBUtil.toUpdateParam(extendUser, "id"));
                    }
                } else if (order.getPayType() == 5) {//微信充值购卡,添加用户购卡信息
                    order.setOrderStatus(2);//已支付
                    order.setPayTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    int flag = orderDao.update(DBUtil.toUpdateParam(order, "id"));
                    if (flag > 0) {
                        //查询用户
                        QueryParam userParam = new QueryParam();
                        userParam.addCondition("customer_id", "=", order.getCustomerId());
                        userParam.addCondition("status", "=", 1);
                        ExtendUser extendUser = extendUserDao.getOne(userParam);
                        TcVipCar tcVipCar = new TcVipCar();
                        tcVipCar.setExtendId(extendUser.getId());
                        tcVipCar.setVipId(order.getObjId());
                        TcVip tcVip = mongoComDAO.executeForObjectById(order.getObjId(), TcVip.class);
                        Integer number = 0;
                        if (tcVip != null) {
                            number = tcVip.getNumber();
                        }
                        tcVipCar.setNumber(number);
                        tcVipCar.setCarId(order.getCarId());
                        tcVipCar.setSdate("");
                        tcVipCar.setEdate("");
                        tcVipCar.setUseStatus(ConstantType.USE_STATUS_WAITFOR); //未使用
                        tcVipCar.setUseTime(""); //使用时间
                        tcVipCar.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                        tcVipCar.setStatus(1);
                        mongoComDAO.executeInsert(tcVipCar);
                    }
                } else if (order.getPayType() == 1) {//停车缴费，在线支付,更改票券状态
                    order.setOrderStatus(2);//已支付
                    order.setPayTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    int flag = orderDao.update(DBUtil.toUpdateParam(order, "id"));
                    if (flag > 0) {
                        //查询用户
                        String extendCouponId = order.getCouponId();
                        if (StringUtils.isNotEmpty(extendCouponId)) {
                            TcExtendCoupon returnTcExtendCoupon = mongoComDAO.executeForObjectById(extendCouponId, TcExtendCoupon.class);
                            if (returnTcExtendCoupon != null) {
                                returnTcExtendCoupon.setUseStatus(ConstantType.USE_STATUS_USED);
                                mongoComDAO.executeUpdate(returnTcExtendCoupon);
                            }
                        }
                        //捷顺车辆支付结果通知
                        //调用捷顺接口
                        String reqJsonPresutstr = "{\"payNo\":\"" + order.getJspayNo() + "\"," +
                                "\"payType\":\"" + ConstantType.JS_PAY_TYPE_WX + "\"," +
                                "\"chargeTime\":\""+order.getPayTime()+"\","+
                                "\"charge\":" + order.getJscharge() + "}";
                        JSONObject jsonPresut = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/paymentresult", reqJsonPresutstr);
                        //添加日志
                        threadPoolTaskExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    TcCarJsclientlog tjsclientlog=new TcCarJsclientlog();
                                    tjsclientlog.setType(1);
                                    tjsclientlog.setUrl(JsHttpClientUtil.JS_CLIENT_URL+"park/paymentresult");
                                    tjsclientlog.setRmode(1);
                                    tjsclientlog.setRparameter(reqJsonPresutstr);
                                    tjsclientlog.setContent(jsonPresut.toString());
                                    tjsclientlog.setCarNum(order.getCarNum());
                                    tjsclientlog.setRemark("微信支付支付结果通知");
                                    tjsclientlog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                                    tjsclientlog.setStatus(1);
                                    mongoComDAO.executeInsert(tjsclientlog);
                                } catch (Exception e) {
                                    logger.error("执行错误："+e.getMessage(),e);
                                }
                            }
                        });
                    }
                }
            }
        }
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"微信支付回调完成",null);
    }


    /**
     * 订单详情
     * @param orderNo
     * @return
     * @throws Exception
     */
    @Override
    public SysOrder getOrderInfo(String orderNo) throws Exception {
        QueryParam param =new QueryParam();
        param.put("order_No",orderNo);
        SysOrder order = new SysOrder();
        List<SysOrder> orderList = orderDao.list(param);
        if(orderList.size() >0){
            if(orderList.get(0).getType() == 5){
                return orderList.get(0);
            }
            order.setAddressId(orderList.get(0).getAddressId());
            order.setCustomerId(orderList.get(0).getCustomerId());
            order.setStoreId(orderList.get(0).getStoreId());
            order.setVouchersId(orderList.get(0).getVouchersId());
            order.setRemark(orderList.get(0).getRemark());
            order.setTotalPrice(orderList.get(0).getTotalPrice());
        }
        return order;
    }

    /**
     * 停车定单查询
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getTcList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        //条件筛选
        //查询
        QueryParam param = new QueryParam();
        param.setPageNum(pageHelper.getPage());
        param.setPageSize(pageHelper.getRows());
        String extendId =  MapUtils.getString(map, "extendId");
        if(StringUtils.isNotEmpty(extendId)){
            //查询用户
            ExtendUser extendUser = extendUserDao.getById(extendId);
            if(extendUser==null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID错误",null);
            }
            param.addCondition("customer_id","=",extendUser.getCustomerId());
        }
        String orderNo =  MapUtils.getString(map, "orderNo");
        if(StringUtils.isNotEmpty(orderNo)){
            param.addCondition("order_no","=",orderNo);
        }
        //车牌号
        String carNum =  MapUtils.getString(map, "carNum");
        if(StringUtils.isNotEmpty(carNum)){
            param.addCondition("car_num","like","%"+carNum+"%");
        }
        //订单状态：1=未付款;2=付款
        param.addCondition("order_status","=",2);
        //支付类型
        String payType =  MapUtils.getString(map, "payType");
        if(StringUtils.isNotEmpty(payType)){
            param.addCondition("pay_type","=",Integer.valueOf(payType));
        }else {
            //停车定单类型（1：停车缴费支付，2：停车充值，5：停车购卡，）
            List<Integer> inttype = new ArrayList<>();
            inttype.add(1);
            inttype.add(2);
            inttype.add(5);
            param.addCondition("pay_type", "in", inttype);
        }
        String orderTime =  MapUtils.getString(map, "orderTime");
        if(StringUtils.isNotEmpty(orderTime)){
            SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar =null;
            calendar = Calendar.getInstance();
            String  strnowDate=orderTime; //当前时间
            int nowYear=Integer.parseInt(strnowDate.substring(0,4));
            int nowMonth=Integer.parseInt(strnowDate.substring(5,7));
            int nowDay=Integer.parseInt(strnowDate.substring(8,10));
            //本月第一天
            calendar = new GregorianCalendar(nowYear, nowMonth - 1, nowDay, 0, 0, 0);
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
            String stratOrderDate=sdf.format(calendar.getTime())+" 00:00:00";
            //本月最后一天
            calendar = new GregorianCalendar(nowYear, nowMonth - 1, nowDay, 0, 0, 0);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            String  endOrderDate =sdf.format(calendar.getTime())+" 23:59:59";
            param.addCondition("create_time",">=",stratOrderDate);
            param.addCondition("create_time","<=",endOrderDate);
        }
        //创建开始时间
        String createSdate =  MapUtils.getString(map, "createSdate");
        if(StringUtils.isNotEmpty(createSdate)){
            param.addCondition("create_time",">=",createSdate);
        }
        //创建结束时间
        String createEdate =  MapUtils.getString(map, "createEdate");
        if(StringUtils.isNotEmpty(createEdate)){
            param.addCondition("create_time","<=",createEdate);
        }
        long count = orderDao.count(param);
        List<SysOrder> sysOrders = orderDao.list(param);
        List<Map<String,Object>> sysOrderlist = new ArrayList<>();
        for(SysOrder order : sysOrders){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",order.getId());
            condition.put("orderNo",order.getOrderNo());
            condition.put("unitPrice",ToolUtil.getIntToDouble(order.getUnitPrice()));
            condition.put("number",order.getNumber());
            condition.put("orderDescribe",order.getOrderDescribe());
            condition.put("remark",order.getRemark());
            double  totalPrice=ToolUtil.getIntToDouble(order.getTotalPrice());
            condition.put("totalPrice",totalPrice);
            double  objTotalPrice=ToolUtil.getIntToDouble(order.getObjTotalPrice());
            condition.put("objTotalPrice",objTotalPrice);
            condition.put("type",order.getType());
            condition.put("payStatus",order.getPayStatus());
            condition.put("payType",order.getPayType());
            condition.put("payTime",order.getPayTime());
            condition.put("payMode",order.getPayMode());
            condition.put("customerId",order.getCustomerId());
            //查询用户
            QueryParam userParam = new QueryParam();
            userParam.addCondition("customer_id","=",order.getCustomerId());
            userParam.addCondition("status","=",1);
            ExtendUser extendUser = extendUserDao.getOne(userParam);
            if(extendUser!=null) {
                condition.put("extendPhone",extendUser.getPhone());
            }else {
                condition.put("extendPhone","");
            }
            condition.put("objId",order.getObjId());
            if(order.getType()==3) {
                //查询Vip信息
                TcVip tcVip = mongoComDAO.executeForObjectById(order.getObjId(), TcVip.class);
                if(tcVip!=null) {
                    condition.put("vipId",tcVip.getId());
                    condition.put("vipName",tcVip.getName());
                }else {
                    condition.put("vipId","");
                    condition.put("vipName","");
                }
            }else {
                condition.put("vipId","");
                condition.put("vipName","");
            }
            condition.put("parkStartTime",order.getParkStartTime());
            condition.put("parkEndTime",order.getParkEndTime());
            condition.put("parkDuration",order.getParkDuration());
            condition.put("extendCouponId",order.getCouponId());
            if(StringUtils.isNotEmpty(order.getCouponId())) {
                TcExtendCoupon tcExtendCoupon = mongoComDAO.executeForObjectById(order.getCouponId(), TcExtendCoupon.class);
                if (tcExtendCoupon!=null) {
                    condition.put("couponId", tcExtendCoupon.getCouponId());
                    TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcExtendCoupon.getCouponId(), TcCoupon.class);
                    if (tcCoupon != null) {
                        condition.put("couponName", tcCoupon.getName());
                    }else {
                        condition.put("couponName", "");
                    }
                }else {
                    condition.put("couponName", "");
                }
            }else {
                condition.put("couponName", "");
            }
            condition.put("couponAmount",ToolUtil.getIntToDouble(order.getCouponAmount()));
            condition.put("parklotId",order.getParklotId());
            if(StringUtils.isNotEmpty(order.getParklotId())) {
                TcParklot tcParklot = mongoComDAO.executeForObjectById(order.getParklotId(), TcParklot.class);
                if(tcParklot != null){
                    condition.put("parklotName",tcParklot.getName());
                }else {
                    condition.put("parklotName","");
                }
            }else {
                condition.put("parklotName","");
            }
            condition.put("carId",order.getCarId());
            condition.put("carNum",order.getCarNum());
            condition.put("isInvoice", order.getIsInvoice());
            condition.put("invoiceStatus",order.getInvoiceStatus());
            condition.put("orderStatus",order.getOrderStatus());
            condition.put("confirmType",order.getConfirmType());
            condition.put("createTime",order.getCreateTime());
            sysOrderlist.add(condition);
        }
        map.clear();
        map.put("list",sysOrderlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

    /**
     * 车辆费用
     * 缴费明细
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getTcListDetail(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        //条件筛选
        //查询
        QueryParam param = new QueryParam();
        param.setPageNum(pageHelper.getPage());
        param.setPageSize(pageHelper.getRows());
        String orderNo =  MapUtils.getString(map, "orderNo");
        if(StringUtils.isNotEmpty(orderNo)){
            param.addCondition("order_no","=",orderNo);
        }
        //车牌号
        String carNum =  MapUtils.getString(map, "carNum");
        if(StringUtils.isNotEmpty(carNum)){
            param.addCondition("car_num","like","%"+carNum+"%");
        }
        //手机号
        String phone =  MapUtils.getString(map, "phone");
        if(StringUtils.isNotEmpty(phone)){
            param.addCondition("phone","like","%"+phone+"%");
        }
        //订单状态：1=未付款;2=付款
        param.addCondition("order_status","=",2);
        //支付类型：（1：停车缴费支付，2：停车充值，5：停车购卡，）
        param.addCondition("pay_type", "=", 1);
        //支付开始时间
        String startTime =  MapUtils.getString(map, "startTime");
        if(StringUtils.isNotEmpty(startTime)){
            param.addCondition("pay_time",">=",startTime);
        }
        //支付结束时间
        String endTime =  MapUtils.getString(map, "endTime");
        if(StringUtils.isNotEmpty(endTime)){
            param.addCondition("pay_time","<=",endTime);
        }
        long count = orderDao.count(param);
        List<SysOrder> sysOrders = orderDao.list(param);
        List<Map<String,Object>> sysOrderlist = new ArrayList<>();
        for(SysOrder order : sysOrders){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",order.getId());
            condition.put("orderNo",order.getOrderNo());
            condition.put("carNum",order.getCarNum());
            condition.put("phone",order.getPhone());
            //停车时长
            condition.put("hours",order.getJsstopTime());
            condition.put("wechat",ToolUtil.getIntToDouble(order.getWechat()));
            condition.put("couponHours",order.getCouponDuration());
            condition.put("total",ToolUtil.getIntToDouble(order.getJschargeTotal()));
            condition.put("balance",ToolUtil.getIntToDouble(order.getBalance()));
            condition.put("actualMoney",ToolUtil.getIntToDouble(order.getActual()));
            condition.put("couponMoney",ToolUtil.getIntToDouble(order.getCouponAmount()));
            condition.put("orderDescribe",order.getOrderDescribe());
            condition.put("datetime",order.getPayTime());
            sysOrderlist.add(condition);
        }
        map.clear();
        map.put("list",sysOrderlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }


    /**
     * 车辆费用
     *  缴费明细查询所有
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String,Object>> getTcListAllDetail(Map<String, Object> map) throws Exception {
        //条件筛选
        //查询
        QueryParam param = new QueryParam();
        param.setPageNum(1);
        param.setPageSize(9999);
        String orderNo =  MapUtils.getString(map, "orderNo");
        if(StringUtils.isNotEmpty(orderNo)){
            param.addCondition("order_no","=",orderNo);
        }
        //车牌号
        String carNum =  MapUtils.getString(map, "carNum");
        if(StringUtils.isNotEmpty(carNum)){
            param.addCondition("car_num","like","%"+carNum+"%");
        }
        //手机号
        String phone =  MapUtils.getString(map, "phone");
        if(StringUtils.isNotEmpty(phone)){
            param.addCondition("phone","like","%"+phone+"%");
        }
        //订单状态：1=未付款;2=付款
        param.addCondition("order_status","=",2);
        //支付类型：（1：停车缴费支付，2：停车充值，5：停车购卡，）
        param.addCondition("pay_type", "=", 1);
        //支付开始时间
        String startTime =  MapUtils.getString(map, "startTime");
        if(StringUtils.isNotEmpty(startTime)){
            param.addCondition("pay_time",">=",startTime);
        }
        //支付结束时间
        String endTime =  MapUtils.getString(map, "endTime");
        if(StringUtils.isNotEmpty(endTime)){
            param.addCondition("pay_time","<=",endTime);
        }
        long count = orderDao.count(param);
        List<SysOrder> sysOrders = orderDao.list(param);
        List<Map<String,Object>> sysOrderlist = new ArrayList<>();
        for(SysOrder order : sysOrders){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",order.getId());
            condition.put("orderNo",order.getOrderNo());
            condition.put("carNum",order.getCarNum());
            condition.put("phone",order.getPhone());
            //停车时长
            condition.put("hours",order.getJsstopTime());
            condition.put("wechat",ToolUtil.getIntToDouble(order.getWechat()));
            condition.put("couponHours",order.getCouponDuration());
            condition.put("total",ToolUtil.getIntToDouble(order.getJschargeTotal()));
            condition.put("balance",ToolUtil.getIntToDouble(order.getBalance()));
            condition.put("actualMoney",ToolUtil.getIntToDouble(order.getActual()));
            condition.put("couponMoney",ToolUtil.getIntToDouble(order.getCouponAmount()));
            condition.put("orderDescribe",order.getOrderDescribe());
            condition.put("datetime",order.getPayTime());
            sysOrderlist.add(condition);
        }
        return sysOrderlist;
    }

    /**
     * 车辆费用
     * 充值明细
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getTcListRechargeDetail(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        //条件筛选
        //查询
        QueryParam param = new QueryParam();
        param.setPageNum(pageHelper.getPage());
        param.setPageSize(pageHelper.getRows());
        String orderNo =  MapUtils.getString(map, "orderNo");
        if(StringUtils.isNotEmpty(orderNo)){
            param.addCondition("order_no","=",orderNo);
        }
        //手机号
        String phone =  MapUtils.getString(map, "phone");
        if(StringUtils.isNotEmpty(phone)){
            param.addCondition("phone","like","%"+phone+"%");
        }
        //订单状态：1=未付款;2=付款
        param.addCondition("order_status","=",2);
        //支付类型：（1：停车缴费支付，2：停车充值，5：停车购卡，）
        param.addCondition("pay_type", "=", 2);
        //支付开始时间
        String startTime =  MapUtils.getString(map, "startTime");
        if(StringUtils.isNotEmpty(startTime)){
            param.addCondition("pay_time",">=",startTime);
        }
        //支付结束时间
        String endTime =  MapUtils.getString(map, "endTime");
        if(StringUtils.isNotEmpty(endTime)){
            param.addCondition("pay_time","<=",endTime);
        }
        long count = orderDao.count(param);
        List<SysOrder> sysOrders = orderDao.list(param);
        List<Map<String,Object>> sysOrderlist = new ArrayList<>();
        for(SysOrder order : sysOrders){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",order.getId());
            condition.put("orderNo",order.getOrderNo());
            //用户
            condition.put("phone",order.getPhone());
            //停车时长
            condition.put("datetime",order.getPayTime());
            condition.put("chargeMoney",ToolUtil.getIntToDouble(order.getActual()));
            sysOrderlist.add(condition);
        }
        map.clear();
        map.put("list",sysOrderlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

    /**
     * 车辆费用
     * 充值明细查询所有
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String,Object>> getTcListAllRechargeDetail(Map<String, Object> map) throws Exception {
        //条件筛选
        //查询
        QueryParam param = new QueryParam();
        param.setPageNum(1);
        param.setPageSize(9999);
        String orderNo =  MapUtils.getString(map, "orderNo");
        if(StringUtils.isNotEmpty(orderNo)){
            param.addCondition("order_no","=",orderNo);
        }
        //手机号
        String phone =  MapUtils.getString(map, "phone");
        if(StringUtils.isNotEmpty(phone)){
            param.addCondition("phone","like","%"+phone+"%");
        }
        //订单状态：1=未付款;2=付款
        param.addCondition("order_status","=",2);
        //支付类型：（1：停车缴费支付，2：停车充值，5：停车购卡，）
        param.addCondition("pay_type", "=", 2);
        //支付开始时间
        String startTime =  MapUtils.getString(map, "startTime");
        if(StringUtils.isNotEmpty(startTime)){
            param.addCondition("pay_time",">=",startTime);
        }
        //支付结束时间
        String endTime =  MapUtils.getString(map, "endTime");
        if(StringUtils.isNotEmpty(endTime)){
            param.addCondition("pay_time","<=",endTime);
        }
        long count = orderDao.count(param);
        List<SysOrder> sysOrders = orderDao.list(param);
        List<Map<String,Object>> sysOrderlist = new ArrayList<>();
        for(SysOrder order : sysOrders){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",order.getId());
            condition.put("orderNo",order.getOrderNo());
            //用户
            condition.put("phone",order.getPhone());
            //停车时长
            condition.put("datetime",order.getPayTime());
            condition.put("chargeMoney",ToolUtil.getIntToDouble(order.getActual()));
            sysOrderlist.add(condition);
        }
        return sysOrderlist;
    }

    /**
     * 车辆费用
     * 购卡明细
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getTcListvipDetail(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        //条件筛选
        //查询
        QueryParam param = new QueryParam();
        param.setPageNum(pageHelper.getPage());
        param.setPageSize(pageHelper.getRows());
        String orderNo =  MapUtils.getString(map, "orderNo");
        if(StringUtils.isNotEmpty(orderNo)){
            param.addCondition("order_no","=",orderNo);
        }
        //手机号
        String phone =  MapUtils.getString(map, "phone");
        if(StringUtils.isNotEmpty(phone)){
            param.addCondition("phone","like","%"+phone+"%");
        }
        //订单状态：1=未付款;2=付款
        param.addCondition("order_status","=",2);
        //支付类型：（1：停车缴费支付，2：停车充值，5：停车购卡，）
        param.addCondition("pay_type", "=", 5);
        //支付开始时间
        String startTime =  MapUtils.getString(map, "startTime");
        if(StringUtils.isNotEmpty(startTime)){
            param.addCondition("pay_time",">=",startTime);
        }
        //支付结束时间
        String endTime =  MapUtils.getString(map, "endTime");
        if(StringUtils.isNotEmpty(endTime)){
            param.addCondition("pay_time","<=",endTime);
        }
        long count = orderDao.count(param);
        List<SysOrder> sysOrders = orderDao.list(param);
        List<Map<String,Object>> sysOrderlist = new ArrayList<>();
        for(SysOrder order : sysOrders){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",order.getId());
            condition.put("orderNo",order.getOrderNo());
            //购卡时间
            condition.put("buytime",order.getPayTime());
            //购卡金额
            condition.put("money",ToolUtil.getIntToDouble(order.getTotalPrice()));
            //用户
            condition.put("phone",order.getPhone());
            //查询vip使用情况
            Map<String,Object> conditionvc = new HashMap<>();
            conditionvc.put("orderId",order.getId());
            TcVipCar tcVipCar = mongoComDAO.executeForObjectByCon(conditionvc, TcVipCar.class);
            if(tcVipCar!=null) {
                condition.put("vipId",tcVipCar.getVipId());
                TcVip tcVip = mongoComDAO.executeForObjectById(tcVipCar.getVipId(), TcVip.class);
                if(tcVip!=null) {
                    condition.put("type",tcVip.getType());
                    condition.put("vipName",tcVip.getName());
                }else {
                    condition.put("type","");
                    condition.put("vipName","");
                }
                condition.put("useStatus",tcVipCar.getUseStatus());
                condition.put("useTime",tcVipCar.getUseTime());
            }else {
                condition.put("vipId","");
                condition.put("type","");
                condition.put("vipName","");
                condition.put("useStatus","");
                condition.put("useTime","");
            }
            condition.put("carNum",order.getCarNum());
            sysOrderlist.add(condition);
        }
        map.clear();
        map.put("list",sysOrderlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

    /**
     * 车辆费用
     * 购卡明细 查询所有
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String,Object>> getTcListAllVipDetail(Map<String, Object> map) throws Exception {
        //条件筛选
        //查询
        QueryParam param = new QueryParam();
        param.setPageNum(1);
        param.setPageSize(9999);
        String orderNo =  MapUtils.getString(map, "orderNo");
        if(StringUtils.isNotEmpty(orderNo)){
            param.addCondition("order_no","=",orderNo);
        }
        //手机号
        String phone =  MapUtils.getString(map, "phone");
        if(StringUtils.isNotEmpty(phone)){
            param.addCondition("phone","like","%"+phone+"%");
        }
        //订单状态：1=未付款;2=付款
        param.addCondition("order_status","=",2);
        //支付类型：（1：停车缴费支付，2：停车充值，5：停车购卡，）
        param.addCondition("pay_type", "=", 5);
        //支付开始时间
        String startTime =  MapUtils.getString(map, "startTime");
        if(StringUtils.isNotEmpty(startTime)){
            param.addCondition("pay_time",">=",startTime);
        }
        //支付结束时间
        String endTime =  MapUtils.getString(map, "endTime");
        if(StringUtils.isNotEmpty(endTime)){
            param.addCondition("pay_time","<=",endTime);
        }
        long count = orderDao.count(param);
        List<SysOrder> sysOrders = orderDao.list(param);
        List<Map<String,Object>> sysOrderlist = new ArrayList<>();
        for(SysOrder order : sysOrders){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",order.getId());
            condition.put("orderNo",order.getOrderNo());
            //购卡时间
            condition.put("buytime",order.getPayTime());
            //购卡金额
            condition.put("money",ToolUtil.getIntToDouble(order.getTotalPrice()));
            //用户
            condition.put("phone",order.getPhone());
            //查询vip使用情况
            Map<String,Object> conditionvc = new HashMap<>();
            conditionvc.put("orderId",order.getId());
            TcVipCar tcVipCar = mongoComDAO.executeForObjectByCon(conditionvc, TcVipCar.class);
            if(tcVipCar!=null) {
                condition.put("vipId",tcVipCar.getVipId());
                TcVip tcVip = mongoComDAO.executeForObjectById(tcVipCar.getVipId(), TcVip.class);
                if(tcVip!=null) {
                    condition.put("type",tcVip.getType());
                    condition.put("vipName",tcVip.getName());
                }else {
                    condition.put("type","");
                    condition.put("vipName","");
                }
                condition.put("useStatus",tcVipCar.getUseStatus());
                condition.put("useTime",tcVipCar.getUseTime());
            }else {
                condition.put("vipId","");
                condition.put("type","");
                condition.put("vipName","");
                condition.put("useStatus","");
                condition.put("useTime","");
            }
            condition.put("carNum",order.getCarNum());
            sysOrderlist.add(condition);
        }
        return sysOrderlist;
    }


    /**
     * 绑定车辆
     * 停车费用总统计：总支付笔数，总收益，总的入场车次，总入场车辆，查询条件，微信支付
     * @param sysOrder
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData findTcOrderTotalData(SysOrder sysOrder,Map<String, Object> map) throws Exception {
        //条件筛选
        //查询
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> condition = new HashMap<>();
        //总用户数
        long countUser=0;
        //总支付笔数
        long countPay=0;
        //总收益
        double totalMoney=0;
        //总的入场车次
        long countCarin=0;
        //总入场车辆
        long countCar=0;
        //总的用户数统计
        countUser=extendUserDao.findCountUser(condition);
        //查询收费情况
        Map mapOrder = orderDao.findTcOrderTotalData(condition);
        if(mapOrder!=null && mapOrder.size()>0) {
            countPay=Integer.valueOf(mapOrder.get("countPay").toString());
            totalMoney=ToolUtil.getIntToDouble(Integer.valueOf(mapOrder.get("totalMoney").toString()));
        }
        //查询车辆车辆总的入场次数
        List<Criteria> criteriasina = new ArrayList<Criteria>();
        Criteria criteriina = Criteria.where("status").is(1);
        criteriasina.add(criteriina);
        countCarin = mongoComDAO.executeForObjectListCount(criteriasina, TcCarInpass.class);
        //查询车辆入场多少个车辆
        DBObject query = new BasicDBObject();
        query.put("status",1);
        countCar=mongoComDAO.executeForObjectDistinctCount(query,"tcCarInpass","carNum",TcCarInpass.class);
        result.put("countUser",countUser);//总用户数
        result.put("countPay",countPay);//总支付笔数
        result.put("totalMoney",totalMoney);//总收益
        result.put("countCarin",countCarin);//总的入场车次
        result.put("countCar",countCar);//总入场车辆
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",result);
    }


    /**
     * 绑定车辆
     * 添加车辆同时，建立客户车辆关系
     * @param sysOrder
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData findTcOrderStatistics(SysOrder sysOrder,Map<String, Object> map) throws Exception {
        //条件筛选
        //查询
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> condition = new HashMap<>();
        //支付开始时间
        String startTime =  MapUtils.getString(map, "startTime");
        if(StringUtils.isNotEmpty(startTime)) {
            condition.put("startTime",startTime);
        }
        //支付结束时间
        String endTime =  MapUtils.getString(map, "endTime");
        if(StringUtils.isNotEmpty(endTime)){
            condition.put("endTime",endTime);
        }
        //车辆出场次数
        long countOut=0;
        //车辆入场次数
        long countIn=0;
        //收费笔数
        Integer countPay=0;
        //未收费笔数
        Integer countFree=0;
        //充值笔数
        Integer countRecharge=0;
        //充值金额
        double rechargeMoney=0;
        //使用余额笔数
        Integer countUseRecharge=0;
        //余额金额
        double useRecharge=0;
        //使用在线微信笔数
        Integer countUseWechat=0;
        //在线微信支付金额
        double payWechatMoney=0;
        //应收金额
        double shouldPayMoney=0;
        //实收金额
        double payMoney=0;
        //免费金额
        double freeMoney=0;
        //优惠小时
        double couponHour=0;
        //折扣笔数
        Integer countUseCoupon=0;
        //折扣金额
        double couponMoney=0;
        //购买月卡的次数
        Integer countBuyMVip=0;
        //购买季卡的次数
        Integer countBuyQVip=0;
        //购买季卡的次数
        Integer countBuyHVip=0;
        //购买年卡的次数
        Integer countBuyYVip=0;
        //兑换停车券次数
        long countExchangeCoupon=0;
        //查询车辆车辆出场次数
        List<Criteria> criteriasouta = new ArrayList<Criteria>();
        if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            Criteria criteria = Criteria.where("outTime").gte(startTime).lte(endTime);
            criteriasouta.add(criteria);
        }
        Criteria criteriouta = Criteria.where("status").is(1);
        criteriasouta.add(criteriouta);
        countOut = mongoComDAO.executeForObjectListCount(criteriasouta, TcCarOutpass.class);
        //查询车辆车辆入场次数
        List<Criteria> criteriasina = new ArrayList<Criteria>();
        if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            Criteria criteria = Criteria.where("inTime").gte(startTime).lte(endTime);
            criteriasina.add(criteria);
        }
        Criteria criteriina = Criteria.where("status").is(1);
        criteriasina.add(criteriina);
        countIn = mongoComDAO.executeForObjectListCount(criteriasina, TcCarInpass.class);
        //查询收费情况
        Map mapOrder = orderDao.findTcOrderStatistics(condition);
        if(mapOrder!=null && mapOrder.size()>0) {
            countPay=Integer.valueOf(mapOrder.get("countPay").toString());
            countFree=Integer.valueOf(mapOrder.get("countFree").toString());
            countRecharge=Integer.valueOf(mapOrder.get("countRecharge").toString());
            rechargeMoney=ToolUtil.getIntToDouble(Integer.valueOf(mapOrder.get("rechargeMoney").toString()));
            countUseRecharge=Integer.valueOf(mapOrder.get("countUseRecharge").toString());
            useRecharge=ToolUtil.getIntToDouble(Integer.valueOf(mapOrder.get("useRecharge").toString()));
            countUseWechat=Integer.valueOf(mapOrder.get("countUseWechat").toString());
            payWechatMoney=ToolUtil.getIntToDouble(Integer.valueOf(mapOrder.get("payWechatMoney").toString()));
            shouldPayMoney=ToolUtil.getIntToDouble(Integer.valueOf(mapOrder.get("shouldPayMoney").toString()));
            payMoney=ToolUtil.getIntToDouble(Integer.valueOf(mapOrder.get("payMoney").toString()));
            freeMoney=ToolUtil.getIntToDouble(Integer.valueOf(mapOrder.get("freeMoney").toString()));
            couponHour=Integer.valueOf(mapOrder.get("couponHour").toString());
            countUseCoupon=Integer.valueOf(mapOrder.get("countUseCoupon").toString());
            couponMoney=ToolUtil.getIntToDouble(Integer.valueOf(mapOrder.get("couponMoney").toString()));
            countBuyMVip=Integer.valueOf(mapOrder.get("countBuyMVip").toString());
            countBuyQVip=Integer.valueOf(mapOrder.get("countBuyQVip").toString());
            countBuyHVip=Integer.valueOf(mapOrder.get("countBuyHVip").toString());
            countBuyYVip=Integer.valueOf(mapOrder.get("countBuyYVip").toString());
        }
        //查询兑换停车券次数
        List<Criteria> criteriasconva = new ArrayList<Criteria>();
        if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            Criteria criteria = Criteria.where("createTime").gte(startTime).lte(endTime);
            criteriasconva.add(criteria);
        }
        Criteria critericonva = Criteria.where("remark").is("积分兑换");
        criteriasconva.add(critericonva);
        Criteria critericonvb = Criteria.where("status").is(1);
        criteriasconva.add(critericonvb);
        countExchangeCoupon = mongoComDAO.executeForObjectListCount(criteriasconva, TcExtendCoupon.class);
        result.put("countOut",countOut);//出场次数
        result.put("countIn",countIn);//入场次数
        result.put("countPay",countPay); //收费笔数
        result.put("countFree",countFree); //未收费笔数
        result.put("countRecharge",countRecharge);//充值笔数
        result.put("rechargeMoney",rechargeMoney);//充值金额
        result.put("countUseRecharge",countUseRecharge);//使用余额笔数
        result.put("useRecharge",useRecharge); //余额金额
        result.put("countUseWechat",countUseWechat);//使用在线微信笔数
        result.put("payWechatMoney",payWechatMoney); //在线微信支付金额
        result.put("shouldPayMoney",shouldPayMoney);//应收金额
        result.put("payMoney",payMoney); //实收金额
        result.put("freeMoney",freeMoney);//免费金额
        result.put("couponHour",couponHour);//优惠小时
        result.put("countUseCoupon",countUseCoupon); //折扣笔数
        result.put("couponMoney",couponMoney); //折扣金额
        result.put("countBuyMVip",countBuyMVip); //购买月卡的次数
        result.put("countBuyQVip",countBuyQVip); //购买季卡的次数
        result.put("countBuyHVip",countBuyHVip);//购买年卡的次数
        result.put("countBuyYVip",countBuyYVip); //兑换停车券次数
        //兑换停车券次数
        result.put("countExchangeCoupon",countExchangeCoupon);//查询车辆车辆出场次数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",result);
    }

    /**
     * 停车场删除临时定单
     * 根据当前时间
     * @return
     */
    @Override
    public Integer deleteTcTempOrderByEdate() throws Exception {
        Integer num=0;
        //根据票券方案查询对就的人员
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar =null;
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);//当前时间前去天数
        String  strnowDate=sdf.format(calendar.getTime()); //当月的第一天
        Map map=new HashMap();
        map.put("ndate", strnowDate);// 票券状态待使用
        num=orderDao.deleteTcTempOrderByEdate(map);
        return num;
    }




    /**
     * 停车定单
     * 支付结果反查
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public JsReturnJsonData getOrderPaycheck(Map<String, Object> map) throws Exception {
        //条件筛选
        QueryParam param = new QueryParam();
        String parkId =  MapUtils.getString(map, "parkId");
        String payNo =  MapUtils.getString(map, "payNo");
        if(StringUtils.isNotEmpty(payNo)){
            param.addCondition("jspay_no","=",payNo);
        }
        //停车定单类型（1：停车缴费支付，2：停车充值，5：停车购卡，）
        param.addCondition("pay_type","=",1);
        SysOrder order = orderDao.getOne(param);
        if(order==null) {
            return new JsReturnJsonData(DataCodeUtil.SELECT_DATABASE+"","获取数据失败",null);
        }
        ///捷顺支付方式 其它;微信支付
        String payType=ConstantType.JS_PAY_TYPE_OTHER;
        // 捷顺支付状态 -1=未支付;0=已支付
        Integer payStatus=ConstantType.JS_PAY_STATUS_NOT_PAY;
        if(order.getPayMode()==1) {
            payType=ConstantType.JS_PAY_TYPE_WX;
        }
        if(order.getOrderStatus()==2) {
            payStatus=ConstantType.JS_PAY_STATUS_USED_PAY;
        }
        //日志增加数据
        String finalPayType = payType;
        Integer finalPayStatus = payStatus;
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TcCarPaycheck tcPaycheck=new TcCarPaycheck();
                    tcPaycheck.setParklotId("");
                    tcPaycheck.setParkId(parkId);
                    tcPaycheck.setPayNo(payNo);
                    tcPaycheck.setPayType(finalPayType);
                    tcPaycheck.setChargeTime(order.getPayTime());
                    tcPaycheck.setTransactionId(order.getOrderNo());
                    tcPaycheck.setPayStatus(finalPayStatus);
                    tcPaycheck.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    tcPaycheck.setStatus(1);
                    mongoComDAO.executeInsert(tcPaycheck);
                } catch (Exception e) {
                    logger.error("执行错误："+e.getMessage(),e);
                }
            }
        });
        if(order.getOrderStatus()==2) {
            Map<String, Object> result = new HashMap<>();
            result.put("payType", payType);//默认为微信支付
            result.put("chargeTime", order.getPayTime());//缴费时间
            result.put("transactionId", order.getOrderNo());//第三方支付交易 id
            result.put("payStatus",payStatus);//支付状态
            return new JsReturnJsonData(DataCodeUtil.SUCCESS+"","",result);
        }
        return new JsReturnJsonData("-1","缴费未支付",null);

    }




    /**
     * 修改订单地址
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateOrderAddressInfo(Map<String, Object> map) throws Exception{
        String orderNo = MapUtils.getString(map, "orderNo", "");
        String username = MapUtils.getString(map, "username", "");
        String phone = MapUtils.getString(map, "phone", "");
        String detailedAddress = MapUtils.getString(map, "detailedAddress", "");

        boolean flag = true;
        if(StringUtils.isNotEmpty(orderNo)){
            QueryParam param = new QueryParam();
            param.put("order_no",orderNo);
            List<SysOrder> orderList = orderDao.list(param);
            for(SysOrder order : orderList){
                if(StringUtils.isNotEmpty(phone)) {
                    order.setAddressPhone(phone);
                }
                if(StringUtils.isNotEmpty(username)){
                    order.setAddressUsername(username);
                }
                if(StringUtils.isNotEmpty(detailedAddress)){
                    order.setDetailedAddress(detailedAddress);
                }
                int i = orderDao.update(DBUtil.toUpdateParam(order, "id"));
                if(i < 0){
                    flag = false;
                    break;
                }
            }
            if(flag){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改订单地址成功",null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改订单地址失败",null);
    }







    /**
     * 删掉订单  半个小时删掉订单
     */
    @Override
    public void deleteOrder() {
        QueryParam orderParam = new QueryParam();
        orderParam.put("pay_status",0);
//        orderParam.put("confirm_type",1);
        orderParam.put("pay_type",4);
        Date time = adjustDateByHour(new Date(), 30, 0);
        String getTime = DateHelperUtils.formatter(time, DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS);
        orderParam.addCondition("create_time","<",getTime);
        orderParam.setQueryall(true);
        List<SysOrder> orderList = orderDao.list(orderParam);
        List<Integer> ids = new ArrayList<>();
        for(SysOrder order : orderList){
            ids.add(order.getId());
        }
        if(ids.size() > 0) {
            orderDao.delete(ids.toArray());
        }
    }

    /**
     * 客户端删除订单
     * @param map
     * @return
     */
    @Override
    public ReturnJsonData deleteOrderInfo(Map<String, Object> map) {
        //订单号
        String orderNo = MapUtils.getString(map, "orderNo", "");
        if(StringUtils.isNotEmpty(orderNo)){
            QueryParam param = new QueryParam();
            param.put("order_no",orderNo);
            List<SysOrder> orderList = orderDao.list(param);
            List<Integer> ids = new ArrayList<>();
            for(SysOrder order : orderList){
                ids.add(order.getId());
            }
            if(ids.size() > 0) {
                int flag = orderDao.delete(ids.toArray());
                if(flag > 0){
                    return new ReturnJsonData(DataCodeUtil.SUCCESS,"删除订单成功",null);
                }
            }
        }
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"删除订单失败",null);
    }



    /**
     *  微信下单
     * @param weChatInfo
     * @param orderNo
     * @param totalAmount
     * @param spbillCreateIp
     * @param openid
     * @return
     * @throws Exception
     */
    public ReturnJsonData unifyWxOrder(WeChatInfo weChatInfo,String orderNo,int totalAmount,String spbillCreateIp,String openid)throws Exception{
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid", weChatInfo.getAppId());
        params.put("mch_id", weChatInfo.getMchId());
        params.put("nonce_str", XhrtStringUtils.getUUID());
        params.put("body", "微信支付");
        params.put("out_trade_no", orderNo);
        params.put("total_fee", String.valueOf(new BigDecimal(totalAmount).setScale(2, BigDecimal.ROUND_HALF_UP).intValue()));
        params.put("spbill_create_ip", spbillCreateIp);
        params.put("notify_url", weChatInfo.getNotifyUrl());
        params.put("trade_type", "JSAPI");
        params.put("openid", openid);
        String sign = EncryptUtil.sign(params, "key", weChatInfo.getMerchantSecretKey());
        params.put("sign", sign);//签名
        String resXml = "";
        try {
            resXml = HttpCilentUtils.post("https://api.mch.weixin.qq.com/pay/unifiedorder", XmlUtil.mapToXml(params, "xml"));
            logger.info("请求统一下单返回结果串============================：" + resXml);
        } catch (Exception e) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "远程访问出错，请稍后！", null);
        }
        Map<String, String> resMap;
        try {
            resMap = XmlUtil.xmlToMap(resXml);
            logger.info("请求统一下单返回结果串============================：" + resMap);
        } catch (Exception e) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "远程访问解析有误！", null);
        }
        logger.info("===========统一下单返回结果------" + resMap + ",参数:" + params);
        //处理结果
        if (resMap != null && resMap.get("return_code").equals("FAIL")) {//------------下单失败
            logger.error("统一下单失败:" + resMap + ",参数:" + params);
            throw new LogicException(resMap.get("return_msg"));
        } else {//--------------------------------------------------下单成功，返回页面调用支付所需配置
            logger.info("===========进支付===============");
            Map<String, Object> jsApiPayConfig = new HashMap<String, Object>();
            jsApiPayConfig.put("appId", weChatInfo.getAppId());
            jsApiPayConfig.put("timeStamp", Long.toString(System.currentTimeMillis() / 1000));
            jsApiPayConfig.put("nonceStr", XhrtStringUtils.getUUID());
            jsApiPayConfig.put("package", "prepay_id=" + resMap.get("prepay_id"));
            jsApiPayConfig.put("signType", "MD5");
            jsApiPayConfig.put("paySign", EncryptUtil.sign(jsApiPayConfig, weChatInfo.getMerchantSecretKey()));//生成签名
            logger.info("支付配置:" + jsApiPayConfig);
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "微信支付成功", jsApiPayConfig);
        }
    }




    /**
     * 两个时间相减 转换成天时分
     * @param endDate
     * @param nowDate
     * @return
     */
    public String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        if(diff > 0) {
            // 计算差多少天
            long day = diff / nd;
            // 计算差多少小时
            long hour = diff % nd / nh;
            // 计算差多少分钟
            long min = diff % nd % nh / nm;
            // 计算差多少秒//输出结果
            // long sec = diff % nd % nh % nm / ns;
            return day + "天" + hour + "小时" + min + "分钟";
        }else{
            return "0";
        }
    }
    /**
     * 计算商品退款金额
     * @param cost
     * @param price
     * @param objPrice
     * @return
     */
    public static double refundPrice(double cost,double price,double objPrice) {
        return new BigDecimal(objPrice * (cost / price) ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public String getRefundPrice(double refundPrice,double number) {
        BigDecimal price = new BigDecimal(refundPrice);
        BigDecimal value = new BigDecimal(number);
        price = price.divide(value,4,BigDecimal.ROUND_HALF_UP).setScale(4,BigDecimal.ROUND_HALF_UP);
        return String.valueOf(price);
    }

    /**
     * 时间相加减
     * @param d
     * @param num
     * @param type
     * @return
     */
    public Date adjustDateByHour(Date d ,Integer num, int  type) {
        Calendar Cal= Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cal.setTime(d);
        if(type==0){
            Cal.add(Calendar.MINUTE,-num);
        }else {
            Cal.add(Calendar.MINUTE,num);
        }
        return Cal.getTime();
    }






}
