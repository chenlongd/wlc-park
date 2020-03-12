package com.perenc.xh.lsp.service.tcVipCar.impl;

import com.alibaba.fastjson.JSONObject;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.ConstantType;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.jshttp.JsHttpClientUtil;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.entity.data.CreateOrderData;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.tcCar.TcCar;
import com.perenc.xh.lsp.entity.tcVip.TcVip;
import com.perenc.xh.lsp.entity.tcVip.TcVipCar;
import com.perenc.xh.lsp.service.order.SysOrderService;
import com.perenc.xh.lsp.service.tcVipCar.TcVipCarService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


@Service("tcVipCarService")
@Transactional(rollbackFor = Exception.class)
public class TcVipCarServiceImpl implements TcVipCarService {

    private static final Logger logger = Logger.getLogger(TcVipCarServiceImpl.class);

    @Autowired
    private MongoComDAO mongoComDAO;


    @Autowired
    private ExtendUserDao extendUserDao;

    @Autowired
    private SysOrderService sysOrderService;

    @Override
    public ReturnJsonData insert(TcVipCar tcVipCar) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcVipCar.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcVipCar.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcVipCar);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    /**
     * 车辆开通vip
     * Vip充值
     * @param tcVipCar
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertCarVip(TcVipCar tcVipCar,Map<String, Object> map) throws Exception {
        try {
            Map<String,Object> condition = new HashMap<>();
            String carNum =  MapUtils.getString(map, "carNum");
            //查询车牌号是否存在
            Map<String,Object> conditionCar = new HashMap<>();
            conditionCar.put("carNum",carNum);
            conditionCar.put("status",1);
            TcCar returnTcCar = mongoComDAO.executeForObjectByCon(conditionCar, TcCar.class);
            if(returnTcCar==null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车牌号不存在",null);
            }
            //判断该车辆是否在场内，并且产生费用
            //捷顺车辆请求计费
            //实收金额
            Integer charge=0;
            //调用捷顺接口
            String reqJsonStr = "{\"plateNumber\":\""+returnTcCar.getCarNum()+"\"}";
            JSONObject jsonCharging= JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL+"park/charging",reqJsonStr);
            if(jsonCharging!=null) {
                String code = jsonCharging.getString("code");
                JSONObject jsondata  = jsonCharging.getJSONObject("data");
                if(code.equals("0")) {
                    charge=jsondata.getInteger("charge");
                }
            }
            if(charge>0) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请先支付已产生的停车费才能购卡",null);
            }
            //vip充值待写
            //充值金额走微信
            //生成定单
            ExtendUser extendUser = extendUserDao.getById(tcVipCar.getExtendId());
            if(extendUser == null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的用户ID错误",null);
            }
            CreateOrderData createOrder = new CreateOrderData();
            //定单类型：1：商城，2:停车场客户户充值，3：停车场客户购卡充值，3：停车场用户支付停车费
            createOrder.setType(3);
            createOrder.setCustomerId(extendUser.getCustomerId());
            createOrder.setExtendId(extendUser.getId());
            createOrder.setPhone(extendUser.getPhone());
            //createOrder.setTotalPrice(ddiscountPrice);
            createOrder.setObjId(tcVipCar.getVipId());
            createOrder.setCarId(returnTcCar.getId());
                /*List<GoodsInfo> goodsInfoList = new ArrayList<GoodsInfo>();
                GoodsInfo goodsInfo = new GoodsInfo();
                goodsInfo.setGoodsAttributeId(tcVipCar.getVipId());
                goodsInfo.setNumber(1);
                goodsInfo.setPrice(ddiscountPrice);
                goodsInfo.setStoreId(returnTcCar.getId());
                goodsInfo.setGoodsName("停车场购卡充值");
                goodsInfoList.add(goodsInfo);
                createOrder.setGoodsInfoList(goodsInfoList);*/
            return sysOrderService.createOrder(createOrder);
        }catch (Exception e) {
            logger.info("执行错误"+e.getMessage());
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
    }


    /**
     * 车辆开通vip
     * 点击开始使用vip
     * Vip充值
     * @param tcVipCar
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateCarVip(TcVipCar tcVipCar,Map<String, Object> map) throws Exception {
        try {
            Map<String,Object> condition = new HashMap<>();
            //日期转换
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdfymd = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = null;
            calendar = Calendar.getInstance();
            String strNowTime = sdf.format(calendar.getTime());
            //查询vip关联表
            TcVipCar returnTcVipCar = mongoComDAO.executeForObjectById(tcVipCar.getId(), TcVipCar.class);
            if(returnTcVipCar == null){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主ID错误",null);
            }
            //判断是否使用
            if(returnTcVipCar.getUseStatus().equals(ConstantType.USE_STATUS_FROZEN)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"该卡已经使用",null);
            }
           TcCar returnTcCar = mongoComDAO.executeForObjectById(returnTcVipCar.getCarId(), TcCar.class);
            if(returnTcCar==null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车牌号不存在",null);
            }
            if(returnTcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_FREE)) {
               return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该车辆属于免费车", null);
            }
            //计算Vip有效期
            String strSdate=null;
            String strEdate=null;
            String streVipSdate=""; //每次充值vip开始时间
            //Vip类型
            Integer vipType=ConstantType.CAR_VIP_TYPE_TEMP;
            //当前时间
            int startNowYear = Integer.parseInt(strNowTime.substring(0, 4));
            int startNowMonth = Integer.parseInt(strNowTime.substring(5, 7));
            int startNowDay = Integer.parseInt(strNowTime.substring(8, 10));
            //查询车辆信息
            String strVipEndTime ="";
            int startVipEndYear = 0;
            int startVipEndMonth = 0;
            int startVipEndDay = 0;
            Date vipEndTime=null;
            boolean isfirst=true;
            if(returnTcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_CARD) && StringUtils.isNotEmpty(returnTcCar.getVipEdate())) {
                strVipEndTime = strVipEndTime = returnTcCar.getVipEdate();
                startVipEndYear = Integer.parseInt(strVipEndTime.substring(0, 4));
                startVipEndMonth = Integer.parseInt(strVipEndTime.substring(5, 7));
                startVipEndDay = Integer.parseInt(strVipEndTime.substring(8, 10));
                vipEndTime=sdfymd.parse(strVipEndTime);
                isfirst=false;
            }
            //vip月数
            Integer number=returnTcVipCar.getNumber();
            //当前时间大于vip结束时间
            Date nowTime=sdfymd.parse(strNowTime);
            //第二次充值，当前时间小于vip结束时间,充值vip开始时间为上次vip结束时间
            if (isfirst==false && nowTime.getTime()<=vipEndTime.getTime()) {
                if(number.equals(ConstantType.VIP_MONTH_ONE)) {
                    calendar = new GregorianCalendar(startVipEndYear, startVipEndMonth - 1, startVipEndDay, 0, 0, 0);
                    strSdate =returnTcCar.getVipSdate();// 当天开始时间为之前时间
                    streVipSdate=returnTcCar.getVipEdate();//充值vip开始时间为上次vip结束时间
                    //calendar.add(Calendar.MONTH, 1);
                    calendar.add(Calendar.DATE, 30);
                    strEdate = sdfymd.format(calendar.getTime()) + " 23:59:59"; //Vip办理第一天至有效期至1个月
                    vipType=ConstantType.CAR_VIP_TYPE_CARD;
                }
                if(number.equals(ConstantType.VIP_MONTH_THREE)) {
                    calendar = new GregorianCalendar(startVipEndYear, startVipEndMonth - 1, startVipEndDay, 0, 0, 0);
                    strSdate =returnTcCar.getVipSdate();// 当天开始时间为之前时间
                    streVipSdate=returnTcCar.getVipEdate();//充值vip开始时间为上次vip结束时间
                    calendar.add(Calendar.DATE, 90);
                    strEdate = sdfymd.format(calendar.getTime()) + " 23:59:59"; //Vip办理第一天至有效期至1个月
                    vipType=ConstantType.CAR_VIP_TYPE_CARD;
                }
                if(number.equals(ConstantType.VIP_MONTH_HALFAYEAR)) {
                    calendar = new GregorianCalendar(startVipEndYear, startVipEndMonth - 1, startVipEndDay, 0, 0, 0);
                    strSdate =returnTcCar.getVipSdate();// 当天开始时间为之前时间
                    calendar.add(Calendar.DATE, 180);
                    strEdate = sdfymd.format(calendar.getTime()) + " 23:59:59"; //Vip办理第一天至有效期至1个月
                    vipType=ConstantType.CAR_VIP_TYPE_CARD;
                }
                if(number.equals(ConstantType.VIP_MONTH_AYEAR)) {
                    calendar = new GregorianCalendar(startVipEndYear, startVipEndMonth - 1, startVipEndDay, 0, 0, 0);
                    strSdate =returnTcCar.getVipSdate();// 当天开始时间为之前时间
                    streVipSdate=returnTcCar.getVipEdate();//充值vip开始时间为上次vip结束时间
                    calendar.add(Calendar.DATE, 360);
                    strEdate = sdfymd.format(calendar.getTime()) + " 23:59:59"; //Vip办理第一天至有效期至1个月
                    vipType=ConstantType.CAR_VIP_TYPE_CARD;
                }
            }else { //第一次充值，当前时间大于vip结束时间
                System.out.println("===不==相同=====");
                if(number.equals(ConstantType.VIP_MONTH_ONE)) {
                    calendar = new GregorianCalendar(startNowYear, startNowMonth - 1, startNowDay, 0, 0, 0);
                    strSdate =strNowTime;//当天开始时间
                    streVipSdate=strNowTime;//充值vip开始时间为当前时间
                    calendar.add(Calendar.DATE, 30);
                    strEdate = sdfymd.format(calendar.getTime()) + " 23:59:59"; //Vip办理第一天至有效期至1个月
                    vipType=ConstantType.CAR_VIP_TYPE_CARD;
                }
                if(number.equals(ConstantType.VIP_MONTH_THREE)) {
                    calendar = new GregorianCalendar(startNowYear, startNowMonth - 1, startNowDay, 0, 0, 0);
                    strSdate =strNowTime;//当天开始时间
                    streVipSdate=strNowTime;//充值vip开始时间为当前时间
                    calendar.add(Calendar.DATE, 90);
                    strEdate = sdfymd.format(calendar.getTime()) + " 23:59:59"; //Vip办理第一天至有效期至1个月
                    vipType=ConstantType.CAR_VIP_TYPE_CARD;
                }
                if(number.equals(ConstantType.VIP_MONTH_HALFAYEAR)) {
                    calendar = new GregorianCalendar(startNowYear, startNowMonth - 1, startNowDay, 0, 0, 0);
                    strSdate = strNowTime;//当天开始时间
                    streVipSdate=strNowTime;//充值vip开始时间为当前时间
                    calendar.add(Calendar.DATE, 180);
                    strEdate = sdfymd.format(calendar.getTime()) + " 23:59:59"; //Vip办理第一天至有效期至1个月
                    vipType=ConstantType.CAR_VIP_TYPE_CARD;
                }
                if(number.equals(ConstantType.VIP_MONTH_AYEAR)) {
                    calendar = new GregorianCalendar(startNowYear, startNowMonth - 1, startNowDay, 0, 0, 0);
                    strSdate = strNowTime;//当天开始时间
                    streVipSdate=strNowTime;//充值vip开始时间为当前时间
                    calendar.add(Calendar.DATE, 360);
                    strEdate = sdfymd.format(calendar.getTime()) + " 23:59:59"; //Vip办理第一天至有效期至1个月
                    vipType=ConstantType.CAR_VIP_TYPE_CARD;
                }
            }
            //修改车辆vip时间
            returnTcCar.setVipId(returnTcVipCar.getVipId());
            returnTcCar.setVipSdate(strSdate);
            returnTcCar.setVipEdate(strEdate);
            returnTcCar.setVipType(vipType);
            int flagup = mongoComDAO.executeUpdate(returnTcCar);
            //更改vip信息
            returnTcVipCar.setSdate(streVipSdate);
            returnTcVipCar.setEdate(strEdate);
            returnTcVipCar.setUseStatus(ConstantType.USE_STATUS_FROZEN); //已使用
            returnTcVipCar.setUseTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = mongoComDAO.executeUpdate(returnTcVipCar);
            if(flagup>0 && flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
            } else{
                return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
            }
        }catch (Exception e) {
            logger.info("执行错误"+e.getMessage());
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
    }




    @Override
    public ReturnJsonData update(TcVipCar tcVipCar) throws Exception {
        TcVipCar returnTcVipCar = mongoComDAO.executeForObjectById(tcVipCar.getId(), TcVipCar.class);
        if(returnTcVipCar != null){
            returnTcVipCar.setVipId(tcVipCar.getVipId());
            returnTcVipCar.setCarId(tcVipCar.getCarId());
            returnTcVipCar.setSdate(tcVipCar.getSdate());
            returnTcVipCar.setEdate(tcVipCar.getEdate());
            int flag = mongoComDAO.executeUpdate(returnTcVipCar);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcVipCar.class);
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
        //日期转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        String strNowTime = sdf.format(calendar.getTime());
        TcVipCar tcVipCar = mongoComDAO.executeForObjectById(id, TcVipCar.class);
        if (tcVipCar!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcVipCar.getId());
            condition.put("vipId",tcVipCar.getVipId());
            TcVip tcVip = mongoComDAO.executeForObjectById(tcVipCar.getVipId(), TcVip.class);
            if(tcVip != null){
                condition.put("vipName",tcVip.getName());
            }else{
                condition.put("vipName","");
            }
            condition.put("carId",tcVipCar.getCarId());
            TcCar tcCar = mongoComDAO.executeForObjectById(tcVipCar.getCarId(), TcCar.class);
            if (tcCar!=null) {
                condition.put("carNum",tcCar.getCarNum());
            }else{
                condition.put("carNum","");
            }
            condition.put("sdate",tcVipCar.getSdate());
            condition.put("edate",tcVipCar.getEdate());
            Integer restDays=0;
            restDays= ToolUtil.getDateDayNum(strNowTime,tcVipCar.getEdate());
            condition.put("restDays",restDays);
            condition.put("status",tcVipCar.getStatus());
            condition.put("createTime",tcVipCar.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }




    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        //日期转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        String strNowTime = sdf.format(calendar.getTime());
        String extendId =  MapUtils.getString(map, "extendId");
        String carId =  MapUtils.getString(map, "carId");
        String useStatus =  MapUtils.getString(map, "useStatus");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //用户id
        if(StringUtils.isNotEmpty(extendId)){
            Criteria criteria = Criteria.where("extendId").is(Integer.valueOf(extendId));
            criteriasa.add(criteria);
        }
        //车Id
        if(StringUtils.isNotEmpty(carId)){
            Criteria criteria = Criteria.where("carId").is(carId);
            criteriasa.add(criteria);
        }
        //使用状态
        if(StringUtils.isNotEmpty(useStatus)){
            Criteria criteria = Criteria.where("useStatus").is(Integer.valueOf(useStatus));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcVipCar> tcVipCars = mongoComDAO.executeForObjectList(criteriasa,TcVipCar.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcVipCar.class);
        List<Map<String,Object>> tcVipCarlist = new ArrayList<>();
        for(TcVipCar tcVipCar : tcVipCars){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcVipCar.getId());
            condition.put("vipId",tcVipCar.getVipId());
            TcVip tcVip = mongoComDAO.executeForObjectById(tcVipCar.getVipId(), TcVip.class);
            if(tcVip != null){
                condition.put("vipName",tcVip.getName());
            }else{
                condition.put("vipName","");
            }
            condition.put("carId",tcVipCar.getCarId());
            TcCar tcCar = mongoComDAO.executeForObjectById(tcVipCar.getCarId(), TcCar.class);
            if (tcCar!=null) {
                condition.put("carNum",tcCar.getCarNum());
            }else{
                condition.put("carNum","");
            }
            condition.put("number",tcVipCar.getNumber());
            condition.put("sdate",tcVipCar.getSdate());
            condition.put("edate",tcVipCar.getEdate());
            Integer restDays=0;
            restDays=ToolUtil.getDateDayNum(strNowTime,tcVipCar.getEdate());
            condition.put("restDays",restDays);
            condition.put("useStatus",tcVipCar.getUseStatus());
            condition.put("useTime",tcVipCar.getUseTime());
            condition.put("status",tcVipCar.getStatus());
            condition.put("createTime",tcVipCar.getCreateTime());
            tcVipCarlist.add(condition);
        }
        map.clear();
        map.put("list",tcVipCarlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

    /**
     * 查询所有
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getAllList(Map<String, Object> map) throws Exception {
        //日期转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        String strNowTime = sdf.format(calendar.getTime());
        String carId =  MapUtils.getString(map, "carId");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //所属类型
        if(StringUtils.isNotEmpty(carId)){
            Criteria criteria = Criteria.where("carId").is(carId);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcVipCar> tcVipCars = mongoComDAO.executeForObjectList(criteriasa,TcVipCar.class,orders);
        List<Map<String,Object>> tcVipCarlist = new ArrayList<>();
        for(TcVipCar tcVipCar : tcVipCars){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcVipCar.getId());
            condition.put("vipId",tcVipCar.getVipId());
            TcVip tcVip = mongoComDAO.executeForObjectById(tcVipCar.getVipId(), TcVip.class);
            if(tcVip != null){
                condition.put("vipName",tcVip.getName());
            }else{
                condition.put("vipName","");
            }
            condition.put("carId",tcVipCar.getCarId());
            TcCar tcCar = mongoComDAO.executeForObjectById(tcVipCar.getCarId(), TcCar.class);
            if (tcCar!=null) {
                condition.put("carNum",tcCar.getCarNum());
            }else{
                condition.put("carNum","");
            }
            condition.put("number",tcVipCar.getNumber());
            condition.put("sdate",tcVipCar.getSdate());
            condition.put("edate",tcVipCar.getEdate());
            Integer restDays=0;
            restDays=ToolUtil.getDateDayNum(strNowTime,tcVipCar.getEdate());
            condition.put("restDays",restDays);
            condition.put("useStatus",tcVipCar.getUseStatus());
            condition.put("useTime",tcVipCar.getUseTime());
            condition.put("status",tcVipCar.getStatus());
            condition.put("createTime",tcVipCar.getCreateTime());
            tcVipCarlist.add(condition);
        }
        map.clear();
        map.put("list",tcVipCarlist);//返回前端集合命名为list
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }



}
