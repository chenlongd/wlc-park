package com.perenc.xh.lsp.service.tcCar.impl;

import com.alibaba.fastjson.JSONObject;
import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.ConstantType;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.jshttp.JsHttpClientUtil;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.dao.order.SysOrderDao;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.order.SysOrder;
import com.perenc.xh.lsp.entity.tcCar.TcCar;
import com.perenc.xh.lsp.entity.tcCar.TcCarJsclientlog;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCar;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegral;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegralTerm;
import com.perenc.xh.lsp.entity.tcParklot.TcParklot;
import com.perenc.xh.lsp.entity.tcVip.TcVip;
import com.perenc.xh.lsp.service.tcCar.TcCarService;
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


@Service("tcCarService")
@Transactional(rollbackFor = Exception.class)
public class TcCarServiceImpl implements TcCarService {

    private static final Logger logger = Logger.getLogger(TcCarServiceImpl.class);

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;

    @Autowired(required = false)
    private SysOrderDao orderDao;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public ReturnJsonData insert(TcCar tcCar) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcCar.setCostSdate(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcCar.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcCar.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcCar);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加成功", null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }


    /**
     * 绑定车辆
     * 添加车辆同时，建立客户车辆关系
     * @param tcCar
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertExtendCar(TcCar tcCar,Map<String, Object> map) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String extendId =  MapUtils.getString(map, "extendId");
        try {
            Calendar calendar = null;
            calendar = Calendar.getInstance();
            String strNowTime = sdf.format(calendar.getTime());
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("carNum", tcCar.getCarNum());
            conditions.put("status", 1);
            //查询所属停车场
            //查询车辆是否已经添加过
            TcCar returnTcCar = mongoComDAO.executeForObjectByCon(conditions, TcCar.class);
            String carId = "";
            //判断车辆是否入场，查询捷顺的场内车辆是否存在
            Integer isEntry=2;
            String strCostSdate="";
            //调用捷顺接口查询场内记录
            String reqJsonStr = "{\"plateNumber\":\""+tcCar.getCarNum()+"\"}";
            JSONObject jsonCharging= JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL+"park/charging",reqJsonStr);
            //添加日志
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TcCarJsclientlog tjsclientlog=new TcCarJsclientlog();
                        tjsclientlog.setType(1);
                        tjsclientlog.setUrl(JsHttpClientUtil.JS_CLIENT_URL+"park/charging");
                        tjsclientlog.setRmode(1);
                        tjsclientlog.setRparameter(reqJsonStr);
                        tjsclientlog.setContent(jsonCharging.toString());
                        tjsclientlog.setCarNum(tcCar.getCarNum());
                        tjsclientlog.setRemark("车辆查询绑定车");
                        tjsclientlog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                        tjsclientlog.setStatus(1);
                        mongoComDAO.executeInsert(tjsclientlog);
                    } catch (Exception e) {
                        logger.error("执行错误："+e.getMessage(),e);
                    }
                }
            });
            String code = jsonCharging.getString("code");
            if (code.equals("0")) {
                JSONObject jsondata  = jsonCharging.getJSONObject("data");
                strCostSdate=jsondata.getString("inTime");
                isEntry=1;
            }
            if (returnTcCar == null) {
                //查询会员类型
                tcCar.setVipType(ConstantType.CAR_VIP_TYPE_TEMP);
                //第一次vipId为空
                tcCar.setVipId("");
                //车辆行车证
                tcCar.setTravelId("");
                //是否认证否
                tcCar.setIsTravel(2);
                tcCar.setTravelImg("");
                //停车场ID
                tcCar.setParklotId(ConstantType.SYS_TC_TCPARKLOT_ID);
                tcCar.setIsEntry(isEntry);//1:已入场，2未入场
                //查询计费时间，是否入场
                tcCar.setCostSdate(strCostSdate);
                tcCar.setCostEdate("");
                tcCar.setIntimg("");
                tcCar.setOutimg("");
                //vip开始时间
                tcCar.setVipSdate("");
                tcCar.setVipEdate("");
                tcCar.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                tcCar.setStatus(1);
                int flag = mongoComDAO.executeInsert(tcCar);
                if (flag > 0) {
                    carId = tcCar.getId();
                } else {
                    return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE, "添加失败", null);
                }
            } else {
                carId = returnTcCar.getId();
                returnTcCar.setIsEntry(isEntry);
                returnTcCar.setCostSdate(strCostSdate);
                mongoComDAO.executeUpdate(returnTcCar);
            }
            //查询车辆是否已经绑定过
            Map<String, Object> conditionsec = new HashMap<>();
            conditionsec.put("carId", carId);
            conditionsec.put("extendId", Integer.valueOf(extendId));
            conditionsec.put("status", 1);
            TcExtendCar returnTcExtendCar = mongoComDAO.executeForObjectByCon(conditionsec, TcExtendCar.class);
            if (returnTcExtendCar != null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该车辆已经绑定", null);
            }
            TcExtendCar tcExtendCar = new TcExtendCar();
            tcExtendCar.setCarId(carId);
            tcExtendCar.setExtendId(Integer.valueOf(extendId));
            tcExtendCar.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            tcExtendCar.setStatus(1);
            int flagEcar = mongoComDAO.executeInsert(tcExtendCar);
            if (flagEcar > 0) {
                //查询是否是首次绑定
                Map<String, Object> conditionsit = new HashMap<>();
                conditionsit.put("type", 2);
                conditionsit.put("isEnabled", 1); //启用状态 1:启用
                conditionsit.put("status", 1);
                //查询认证车辆获得的积分
                TcIntegralTerm tcIntegralTerm = mongoComDAO.executeForObjectByCon(conditionsit, TcIntegralTerm.class);
                if (tcIntegralTerm != null) {
                    Map<String, Object> conditionsel = new HashMap<>();
                    conditionsel.put("extendId",Integer.valueOf(extendId));
                    conditionsel.put("integralTermId", tcIntegralTerm.getId());
                    conditionsel.put("status", 1);
                    //查询是否已经获得积分
                    TcIntegral tcIntegralsel = mongoComDAO.executeForObjectByCon(conditionsel, TcIntegral.class);
                    if(tcIntegralsel==null) {
                        TcIntegral tcIntegral = new TcIntegral();
                        tcIntegral.setExtendId(Integer.valueOf(extendId));
                        tcIntegral.setIntegralConvertruleId("");
                        tcIntegral.setIntegralTermId(tcIntegralTerm.getId());
                        tcIntegral.setType(1);
                        tcIntegral.setNumber(tcIntegralTerm.getNumber());
                        tcIntegral.setRemark("绑定车辆");
                        tcIntegral.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                        tcIntegral.setStatus(1);
                        int flagit = mongoComDAO.executeInsert(tcIntegral);
                        if (flagit > 0) {
                            //更改用户积分
                            ExtendUser returnUser = extendUserDao.getById(Integer.valueOf(extendId));
                            returnUser.setIntegral(returnUser.getIntegral() + tcIntegral.getNumber());
                            extendUserDao.update(DBUtil.toUpdateParam(returnUser, "id"));
                        }
                    }
                }
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加成功", null);
            } else {
                return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE, "添加失败", null);
            }
        }catch (Exception e) {
        logger.info("执行错误"+e.getMessage());
        System.out.println("执行错误"+e.getMessage());
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE, "添加失败", null);
    }



    @Override
    public ReturnJsonData update(TcCar tcCar) throws Exception {
        //修改判断车牌号是否存在
        try {
            List<Criteria> criteriasa = new ArrayList<Criteria>();
            Criteria criteria = Criteria.where("carNum").is(tcCar.getCarNum());
            criteriasa.add(criteria);
            Criteria criterib = Criteria.where("id").ne(tcCar.getId());
            criteriasa.add(criterib);
            Criteria criteric = Criteria.where("status").is(1);
            criteriasa.add(criteric);
            List<TcCar> tcCars = mongoComDAO.executeForObjectList(criteriasa, TcCar.class);
            if (tcCars.size() > 0) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "车牌号已经存在", null);
            }
            TcCar returnTcCar = mongoComDAO.executeForObjectById(tcCar.getId(), TcCar.class);
            if (returnTcCar != null) {
                returnTcCar.setCarNum(tcCar.getCarNum());
                int flag = mongoComDAO.executeUpdate(returnTcCar);
                if (flag > 0) {
                    return new ReturnJsonData(DataCodeUtil.SUCCESS, "修改成功", null);
                }
            } else {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "传入的ID错误", null);
            }
        }catch (Exception e) {
            logger.info("执行错误"+e.getMessage());
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }


    /**
     * 车辆实名制认证，
     * 上传行车证图片
     * @param tcCar
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateTravelImg(TcCar tcCar,Map<String, Object> map) throws Exception {
        String extendId =  MapUtils.getString(map, "extendId");
        TcCar returnTcCar = mongoComDAO.executeForObjectById(tcCar.getId(), TcCar.class);
        if(returnTcCar != null){
            returnTcCar.setIsTravel(1);
            returnTcCar.setTravelImg(tcCar.getTravelImg());
            int flag = mongoComDAO.executeUpdate(returnTcCar);
            if(flag > 0){
                //查询是否是首次认证
                Map<String, Object> conditions = new HashMap<>();
                conditions.put("type", 3);
                conditions.put("isEnabled", 1); //启用状态 1:启用
                conditions.put("status", 1);
                //查询认证车辆获得的积分
                TcIntegralTerm tcIntegralTerm = mongoComDAO.executeForObjectByCon(conditions, TcIntegralTerm.class);
                if (tcIntegralTerm != null) {
                    Map<String, Object> conditionsel = new HashMap<>();
                    conditionsel.put("extendId",Integer.valueOf(extendId));
                    conditionsel.put("integralTermId", tcIntegralTerm.getId());
                    conditionsel.put("status", 1);
                    //查询是否已经获得积分
                    TcIntegral tcIntegralsel = mongoComDAO.executeForObjectByCon(conditionsel, TcIntegral.class);
                    if(tcIntegralsel==null) {
                        TcIntegral tcIntegral = new TcIntegral();
                        tcIntegral.setExtendId(Integer.valueOf(extendId));
                        tcIntegral.setIntegralConvertruleId("");
                        tcIntegral.setIntegralTermId(tcIntegralTerm.getId());
                        tcIntegral.setType(1);
                        tcIntegral.setNumber(tcIntegralTerm.getNumber());
                        tcIntegral.setRemark("认证车辆");
                        tcIntegral.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                        tcIntegral.setStatus(1);
                        int flagit = mongoComDAO.executeInsert(tcIntegral);
                        if (flagit > 0) {
                            //更改用户积分
                            ExtendUser returnUser = extendUserDao.getById(Integer.valueOf(extendId));
                            returnUser.setIntegral(returnUser.getIntegral() + tcIntegral.getNumber());
                            extendUserDao.update(DBUtil.toUpdateParam(returnUser, "id"));
                        }
                    }
                }
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcCar.class);
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
        TcCar tcCar = mongoComDAO.executeForObjectById(id, TcCar.class);
        if (tcCar!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcCar.getId());
            condition.put("carNum",tcCar.getCarNum());
            condition.put("type",tcCar.getType());
            condition.put("vipType",tcCar.getVipType());
            condition.put("vipId",tcCar.getType());
            TcVip tcVip = mongoComDAO.executeForObjectById(tcCar.getVipId(), TcVip.class);
            if(tcVip != null){
                condition.put("vipName",tcVip.getName());
            }else{
                condition.put("vipName","");
            }
            condition.put("travelId",tcCar.getTravelId());
            condition.put("isTravel",tcCar.getIsTravel());
            condition.put("travelImg",tcCar.getTravelImg());
            condition.put("parklotId",tcCar.getParklotId());
            TcParklot tcParklot = mongoComDAO.executeForObjectById(tcCar.getParklotId(), TcParklot.class);
            if(tcParklot != null){
                condition.put("parklotName",tcParklot.getName());
            }else{
                condition.put("parklotName","");
            }
            condition.put("costSdate",tcCar.getCostSdate());
            condition.put("costEdate",tcCar.getCostEdate());
            condition.put("intimg",tcCar.getIntimg());
            condition.put("outimg",tcCar.getOutimg());
            condition.put("vipSdate",tcCar.getVipEdate());
            condition.put("vipEdate",tcCar.getVipEdate());
            condition.put("status",tcCar.getStatus());
            condition.put("createTime",tcCar.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String parklotId =  MapUtils.getString(map, "parklotId");
        //String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //所属类型
        if(StringUtils.isNotEmpty(parklotId)){
            Criteria criteria = Criteria.where("parklotId").is(Integer.valueOf(parklotId));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcCar> tcCars = mongoComDAO.executeForObjectList(criteriasa,TcCar.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcCar.class);
        List<Map<String,Object>> tcCarlist = new ArrayList<>();
        for(TcCar tcCar : tcCars){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcCar.getId());
            condition.put("carNum",tcCar.getCarNum());
            condition.put("type",tcCar.getType());
            condition.put("vipType",tcCar.getVipType());
            condition.put("vipId",tcCar.getType());
            TcVip tcVip = mongoComDAO.executeForObjectById(tcCar.getVipId(), TcVip.class);
            if(tcVip != null){
                condition.put("vipName",tcVip.getName());
            }else{
                condition.put("vipName","");
            }
            condition.put("travelId",tcCar.getTravelId());
            condition.put("isTravel",tcCar.getIsTravel());
            condition.put("travelImg",tcCar.getTravelImg());
            condition.put("parklotId",tcCar.getParklotId());
            TcParklot tcParklot = mongoComDAO.executeForObjectById(tcCar.getParklotId(), TcParklot.class);
            if(tcParklot != null){
                condition.put("parklotName",tcParklot.getName());
            }else{
                condition.put("parklotName","");
            }
            condition.put("costSdate",tcCar.getCostSdate());
            condition.put("costEdate",tcCar.getCostEdate());
            condition.put("intimg",tcCar.getIntimg());
            condition.put("outimg",tcCar.getOutimg());
            condition.put("vipSdate",tcCar.getVipEdate());
            condition.put("vipEdate",tcCar.getVipEdate());
            condition.put("status",tcCar.getStatus());
            condition.put("createTime",tcCar.getCreateTime());
            tcCarlist.add(condition);
        }
        map.clear();
        map.put("list",tcCarlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }


    /**
     * 根据车牌号查询
     * 查询车辆信息 ，最近的一条
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData findOneCarOrder(Map<String, Object> map) throws Exception {
        String carNum =  MapUtils.getString(map, "carNum");
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("carNum", carNum);
        TcCar tcCar = mongoComDAO.executeForObjectByCon(conditions, TcCar.class);
        if(tcCar==null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"不存在该车牌号",null);
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("id",tcCar.getId());
        condition.put("carNum",tcCar.getCarNum());
        condition.put("vipType",tcCar.getVipType());
        condition.put("vipId",tcCar.getType());
        if (tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_TEMP)) {
            condition.put("vipName", ConstantType.CAR_VIP_TYPE_TEMP_NAME);
        } else if (tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_CARD)) {
            condition.put("vipName", ConstantType.CAR_VIP_TYPE_CARD_NAME);
        }else if(tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_FREE)) {
            condition.put("vipName", ConstantType.CAR_VIP_TYPE_FREE_NAME);
        }
        //停车每小时多少钱
        double carDuration = 0;
        double parkPrice = 0;
        //捷顺停车开始时间
        String inTime="";
        //捷顺停车时长
        String stopTime="";
        if (tcCar.getIsEntry().equals(1)) {
            //捷顺车辆请求计费
            String reqJsonStr = "{\"plateNumber\":\""+carNum+"\"}";
            JSONObject jsonCharging= JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL+"park/charging",reqJsonStr);
            String code = jsonCharging.getString("code");
            JSONObject jsondata  = jsonCharging.getJSONObject("data");
            if(code.equals("0")) {
                parkPrice= ToolUtil.getIntToDouble(jsondata.getInteger("charge"));
                inTime=jsondata.getString("inTime");
                stopTime = jsondata.getString("stopTime");
            }
        }
        condition.put("inTime",inTime);
        condition.put("duration", stopTime);
        condition.put("parkPrice", parkPrice);
        condition.put("vipSdate",tcCar.getVipEdate());
        condition.put("vipEdate",tcCar.getVipEdate());
        condition.put("isEntry",tcCar.getIsEntry());
        //查询车辆已支付最近一条定单
        QueryParam param = new QueryParam();
        param.addCondition("car_id", "=", tcCar.getId());
        param.addCondition("type", "=", 1);
        //
        param.addCondition("pay_status", "=", 1);
        SysOrder order=orderDao.getOne(param);
        //订单编号
        String orderNo="";
        //停车开始时间
        String parkStartTime="";
        //停车结束时间
        String parkEndTime="";
        //停车时长
        String parkDuration="";
        //停车费用
        double totalPrice=0;
        //支付时间
        String payTime="";
        //支付状态 0=未支付;1=已支付
        Integer payStatus=0;
        //缴费方式
        String strPayMode="";
        //是否使用停车券（1:是、2:否）
        Integer isCoupon=2;
        //停车券小时数
        double couponDuration=0;
        //停车券金额
        double couponAmount=0;
        if(order!=null) {
            orderNo=order.getOrderNo();
            parkStartTime=order.getJsinTime();
            parkEndTime=order.getJsfeesTime();
            parkDuration=order.getJsstopTime();
            totalPrice=ToolUtil.getIntToDouble(order.getObjTotalPrice());
            payTime=order.getPayTime();
            payStatus=order.getPayStatus();
            if(StringUtils.isNotEmpty(order.getCouponId())){
                isCoupon=1;
                couponAmount=ToolUtil.getIntToDouble(order.getCouponAmount());
                couponDuration=order.getCouponDuration();
            }
            // 支付方式 1=在线支付;2=余额支付，3=停车券抵扣;4=虚拟商品;5=月卡车;6=免费车
            Integer payMode=order.getPayMode();
            if(payMode.equals(1)) {
                strPayMode="在线支付";
                if(StringUtils.isNotEmpty(order.getCouponId())){
                    strPayMode="在线支付+停车券";
                }
            }else if(payMode.equals(2)) {
                strPayMode="余额支付";
                if(StringUtils.isNotEmpty(order.getCouponId())){
                    strPayMode="余额支付+停车券";
                }
            }else if(payMode.equals(3)) {
                strPayMode="停车券";
            }else if(payMode.equals(5)) {
                strPayMode="月卡车";
            }else if(payMode.equals(6)) {
                strPayMode="免费车";
            }
        }
        condition.put("orderNo", orderNo);
        condition.put("parkStartTime", parkStartTime);
        condition.put("parkEndTime", parkEndTime);
        condition.put("parkDuration",parkDuration);
        condition.put("totalPrice",totalPrice);
        condition.put("payMode",strPayMode);
        condition.put("payTime",payTime);
        condition.put("payStatus",payStatus);
        condition.put("isCoupon",isCoupon);
        condition.put("couponDuration",couponDuration);
        condition.put("couponAmount",couponAmount);
        condition.put("status",tcCar.getStatus());
        condition.put("createTime",tcCar.getCreateTime());
        return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
    }



    /**
     * 查询捷顺设备
     * 查询所有控制设备
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getJsDevices(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String parklotId =  MapUtils.getString(map, "parklotId");
        JSONObject jsonCoupon= JsHttpClientUtil.httpGet(JsHttpClientUtil.JS_CLIENT_URL+"base/devices",map);



        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",jsonCoupon);
    }





    /**
     * 定时任务 :VIP车
     * 根据当前时间判断vip时间判断
     * viptype(VIP车类型1:临时车；2:Vip车；3:免费车)
     * @return
     * @throws Exception
     */
    @Override
    public Integer updateBatchVipByedate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat  sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar =null;
        calendar = Calendar.getInstance();
        String  strnowDate=sdfhms.format(calendar.getTime()); //当前时间
        Integer num=0;
        //修改判断车牌号是否存在
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        Criteria criterib = Criteria.where("vipEdate").lte(strnowDate);
        criteriasa.add(criterib);
        Criteria criteric = Criteria.where("vipType").is(2);
        criteriasa.add(criteric);
        Criteria criterid = Criteria.where("status").is(1);
        criteriasa.add(criterid);
        //查询vip结束时小于当前时间 ，把车辆更改为临时车
        List<TcCar> tcCars = mongoComDAO.executeForObjectList(criteriasa,TcCar.class);
        for(TcCar tcCar : tcCars){
            TcCar returnTcCar = mongoComDAO.executeForObjectById(tcCar.getId(), TcCar.class);
            if(returnTcCar != null) {
                returnTcCar.setVipId("");
                returnTcCar.setVipType(ConstantType.CAR_VIP_TYPE_TEMP);
                returnTcCar.setVipSdate("");
                returnTcCar.setVipEdate("");
                num = +mongoComDAO.executeUpdateEmpty(returnTcCar);
            }
        }
        return num;
    }


    /**
     * 定时任务 免费车
     * 根据当前时间判断免费车时间判断
     * viptype(VIP车类型1:临时车；2:Vip车；3:免费车)
     * @return
     * @throws Exception
     */
    @Override
    public Integer updateBatchFreeByedate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat  sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar =null;
        calendar = Calendar.getInstance();
        String  strnowDate=sdfhms.format(calendar.getTime()); //当前时间
        Integer num=0;
        //修改判断车牌号是否存在
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        Criteria criterib = Criteria.where("vipEdate").lte(strnowDate);
        criteriasa.add(criterib);
        Criteria criteric = Criteria.where("vipType").is(3);
        criteriasa.add(criteric);
        Criteria criterid = Criteria.where("status").is(1);
        criteriasa.add(criterid);
        //查询vip结束时小于当前时间 ，把车辆更改为临时车
        List<TcCar> tcCars = mongoComDAO.executeForObjectList(criteriasa,TcCar.class);
        for(TcCar tcCar : tcCars){
            TcCar returnTcCar = mongoComDAO.executeForObjectById(tcCar.getId(), TcCar.class);
            if(returnTcCar != null) {
                returnTcCar.setVipId("");
                returnTcCar.setVipType(ConstantType.CAR_VIP_TYPE_TEMP);
                returnTcCar.setVipSdate("");
                returnTcCar.setVipEdate("");
                num = +mongoComDAO.executeUpdateEmpty(returnTcCar);
            }
        }
        return num;
    }



}
