package com.perenc.xh.lsp.service.tcExtendCar.impl;

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
import com.perenc.xh.lsp.entity.tcCar.TcCar;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCar;
import com.perenc.xh.lsp.service.tcCarInout.impl.TcCarInoutServiceImpl;
import com.perenc.xh.lsp.service.tcExtendCar.TcExtendCarService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("tcExtendCarService")
@Transactional(rollbackFor = Exception.class)
public class TcExtendCarServiceImpl implements TcExtendCarService {
    private static final Logger logger = Logger.getLogger(TcCarInoutServiceImpl.class);

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;

    @Override
    public ReturnJsonData insert(TcExtendCar tcExtendCar) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcExtendCar.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcExtendCar.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcExtendCar);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcExtendCar tcExtendCar) throws Exception {
        TcExtendCar returnTcExtendCar = mongoComDAO.executeForObjectById(tcExtendCar.getId(), TcExtendCar.class);
        if(returnTcExtendCar != null){
            returnTcExtendCar.setExtendId(tcExtendCar.getExtendId());
            returnTcExtendCar.setCarId(tcExtendCar.getCarId());
            int flag = mongoComDAO.executeUpdate(returnTcExtendCar);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 删除
     * @param tcExtendCar
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData deleteCar(TcExtendCar tcExtendCar) throws Exception {
        TcExtendCar returnTcExtendCar = mongoComDAO.executeForObjectById(tcExtendCar.getId(), TcExtendCar.class);
        if(returnTcExtendCar != null){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcExtendCar.getId());
            int flag = mongoComDAO.executeDelete(condition,TcExtendCar.class);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"删除成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"删除失败",null);
    }


    @Override
    public ReturnJsonData delete(String[] ids) throws Exception {
        Map<String,Object> condition=new HashMap<>();
        condition.put("ids",ids);
        condition.put("status",2);
        int flag= mongoComDAO.executeUpdateByIds(condition, TcExtendCar.class);
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
        String strNowTime =DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS);
        TcExtendCar tcExtendCar = mongoComDAO.executeForObjectById(id, TcExtendCar.class);
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        if (tcExtendCar!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcExtendCar.getId());
            condition.put("extendId",tcExtendCar.getExtendId());
            condition.put("carId",tcExtendCar.getCarId());
            TcCar tcCar = mongoComDAO.executeForObjectById(tcExtendCar.getCarId(), TcCar.class);
            if(tcCar != null){
                condition.put("parklotId",tcCar.getParklotId());
                condition.put("carNum",tcCar.getCarNum());
                condition.put("type",tcCar.getType());
                condition.put("vipType",tcCar.getVipType());
                condition.put("isTravel",tcCar.getIsTravel());
                condition.put("travelImg",tcCar.getTravelImg());
                condition.put("costSdate",tcCar.getCostSdate());
                condition.put("costEdate",tcCar.getCostEdate());
                condition.put("intimg",tcCar.getIntimg());
                condition.put("outimg",tcCar.getOutimg());
                condition.put("vipSdate",tcCar.getVipSdate());
                condition.put("vipEdate",tcCar.getVipEdate());
                condition.put("isEntry",tcCar.getIsEntry());
                //停车每小时多少钱
                double carDuration = 0;
                double parkPrice = 0;
                if (tcCar.getIsEntry().equals(1)) {
                    //捷顺车辆请求计费
                    //调用捷顺接口
                    String reqJsonStr = "{\"plateNumber\":\""+tcCar.getCarNum()+"\"}";
                    JSONObject jsonCharging= JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL+"park/charging",reqJsonStr);
                    String code = jsonCharging.getString("code");
                    JSONObject jsondata  = jsonCharging.getJSONObject("data");
                    if(code.equals("0")) {
                        parkPrice= ToolUtil.getIntToDouble(jsondata.getInteger("charge"));
                        //停车时长（小时）：当前时间-进厂时间
                        carDuration = ToolUtil.getDateHourNum(tcCar.getCostSdate(), strNowTime);
                    }else if(code.equals("1")) {//"车辆未入场:状态为1,更改车辆状态
                        tcCar.setIsEntry(2);
                        mongoComDAO.executeUpdate(tcCar);
                    }
                }
                condition.put("duration", carDuration);
                condition.put("parkPrice", parkPrice);
                if (tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_TEMP)) {
                    condition.put("vipName", ConstantType.CAR_VIP_TYPE_TEMP_NAME);
                } else if (tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_CARD)) {
                    condition.put("vipName", ConstantType.CAR_VIP_TYPE_CARD_NAME);
                }else if(tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_FREE)) {
                    condition.put("vipName", ConstantType.CAR_VIP_TYPE_FREE_NAME);
                }
            }
            condition.put("status",tcExtendCar.getStatus());
            condition.put("createTime",tcExtendCar.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String strNowTime =DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS);
        String extendId =  MapUtils.getString(map, "extendId");
        String carId =  MapUtils.getString(map, "carId");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //客户ID
        if(StringUtils.isNotEmpty(extendId)){
            Criteria criteria = Criteria.where("extendId").is(Integer.valueOf(extendId));
            criteriasa.add(criteria);
        }
        //车辆ID
        if(StringUtils.isNotEmpty(carId)){
            Criteria criteria = Criteria.where("carId").is(Integer.valueOf(carId));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcExtendCar> tcExtendCars = mongoComDAO.executeForObjectList(criteriasa,TcExtendCar.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcExtendCar.class);
        List<Map<String,Object>> tcExtendCarlist = new ArrayList<>();
        for(TcExtendCar tcExtendCar : tcExtendCars){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcExtendCar.getId());
            condition.put("extendId",tcExtendCar.getExtendId());
            condition.put("carId",tcExtendCar.getCarId());
            TcCar tcCar = mongoComDAO.executeForObjectById(tcExtendCar.getCarId(), TcCar.class);
            if(tcCar != null){
                condition.put("parklotId",tcCar.getParklotId());
                condition.put("carNum",tcCar.getCarNum());
                condition.put("type",tcCar.getType());
                condition.put("vipType",tcCar.getVipType());
                condition.put("isTravel",tcCar.getIsTravel());
                condition.put("travelImg",tcCar.getTravelImg());
                condition.put("costSdate",tcCar.getCostSdate());
                condition.put("costEdate",tcCar.getCostEdate());
                condition.put("intimg",tcCar.getIntimg());
                condition.put("outimg",tcCar.getOutimg());
                condition.put("vipSdate",tcCar.getVipSdate());
                condition.put("vipEdate",tcCar.getVipEdate());
                condition.put("isEntry",tcCar.getIsEntry());
                //停车每小时多少钱
                double carDuration = 0;
                double parkPrice = 0;
                if (tcCar.getIsEntry().equals(1)) {
                    //捷顺车辆请求计费
                    //调用捷顺接口
                    String reqJsonStr = "{\"plateNumber\":\""+tcCar.getCarNum()+"\"}";
                    JSONObject jsonCharging= JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL+"park/charging",reqJsonStr);
                    String code = jsonCharging.getString("code");
                    JSONObject jsondata  = jsonCharging.getJSONObject("data");
                    if(code.equals("0")) {
                        parkPrice=ToolUtil.getIntToDouble(jsondata.getInteger("charge"));
                        //停车时长（小时）：当前时间-进厂时间
                        carDuration = ToolUtil.getDateHourNum(tcCar.getCostSdate(), strNowTime);
                    }else if(code.equals("1")) {//"车辆未入场:状态为1,更改车辆状态
                        tcCar.setIsEntry(2);
                        mongoComDAO.executeUpdate(tcCar);
                    }
                }
                condition.put("duration", carDuration);
                condition.put("parkPrice", parkPrice);
                if (tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_TEMP)) {
                    condition.put("vipName", ConstantType.CAR_VIP_TYPE_TEMP_NAME);
                } else if (tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_CARD)) {
                    condition.put("vipName", ConstantType.CAR_VIP_TYPE_CARD_NAME);
                }else if(tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_FREE)) {
                    condition.put("vipName", ConstantType.CAR_VIP_TYPE_FREE_NAME);
                }
            }
            condition.put("status",tcExtendCar.getStatus());
            condition.put("createTime",tcExtendCar.getCreateTime());
            tcExtendCarlist.add(condition);
        }
        map.clear();
        map.put("list",tcExtendCarlist);//返回前端集合命名为list
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
        String extendId =  MapUtils.getString(map, "extendId");
        String carId =  MapUtils.getString(map, "carId");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //客户ID
        if(StringUtils.isNotEmpty(extendId)){
            Criteria criteria = Criteria.where("extendId").is(Integer.valueOf(extendId));
            criteriasa.add(criteria);
        }
        //车辆ID
        if(StringUtils.isNotEmpty(carId)){
            Criteria criteria = Criteria.where("carId").is(carId);
            criteriasa.add(criteria);
        }
        String isEntry =  MapUtils.getString(map, "isEntry");
        //是否入场
        if(StringUtils.isNotEmpty(isEntry)){
            Criteria criteria = Criteria.where("isEntry").is(isEntry);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcExtendCar> tcExtendCars = mongoComDAO.executeForObjectList(criteriasa,TcExtendCar.class,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcExtendCar.class);
        List<Map<String,Object>> tcExtendCarlist = new ArrayList<>();
        for(TcExtendCar tcExtendCar : tcExtendCars){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcExtendCar.getId());
            condition.put("extendId",tcExtendCar.getExtendId());
            condition.put("carId",tcExtendCar.getCarId());
            TcCar tcCar = mongoComDAO.executeForObjectById(tcExtendCar.getCarId(), TcCar.class);
            if(tcCar != null) {
                condition.put("parklotId", tcCar.getParklotId());
                condition.put("carNum", tcCar.getCarNum());
                condition.put("type", tcCar.getType());
                condition.put("vipType", tcCar.getVipType());
                condition.put("isTravel", tcCar.getIsTravel());
                condition.put("travelImg", tcCar.getTravelImg());
                condition.put("costSdate", tcCar.getCostSdate());
                condition.put("costEdate", tcCar.getCostEdate());
                condition.put("intimg", tcCar.getIntimg());
                condition.put("outimg", tcCar.getOutimg());
                condition.put("vipSdate", tcCar.getVipSdate());
                condition.put("vipEdate", tcCar.getVipEdate());
                condition.put("isEntry", tcCar.getIsEntry());
                //捷顺停车开始时间
                String inTime="";
                // 停车时长（时分）
                String carDuration="";
                double parkPrice = 0d;
                if (tcCar.getIsEntry().equals(1)) {
                    //捷顺车辆请求计费
                    //调用捷顺接口
                    String reqJsonStr = "{\"plateNumber\":\""+tcCar.getCarNum()+"\"}";
                    JSONObject jsonCharging= JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL+"park/charging",reqJsonStr);
                    String code = jsonCharging.getString("code");
                    JSONObject jsondata  = jsonCharging.getJSONObject("data");
                    if(code.equals("0")) {
                        parkPrice=ToolUtil.getIntToDouble(jsondata.getInteger("charge"));
                        inTime=jsondata.getString("inTime");
                        carDuration=jsondata.getString("stopTime");
                    }else if(code.equals("1")) {//"车辆未入场:状态为1,更改车辆状态
                        tcCar.setIsEntry(2);
                        mongoComDAO.executeUpdate(tcCar);
                    }
                    logger.info("查询车辆缴费信息："+jsonCharging);
                }
                condition.put("inTime", inTime);
                condition.put("duration", carDuration);
                condition.put("parkPrice", parkPrice);
                if (tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_TEMP)) {
                    condition.put("vipName", ConstantType.CAR_VIP_TYPE_TEMP_NAME);
                } else if (tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_CARD)) {
                    condition.put("vipName", ConstantType.CAR_VIP_TYPE_CARD_NAME);
                }else if(tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_FREE)) {
                    condition.put("vipName", ConstantType.CAR_VIP_TYPE_FREE_NAME);
                }
            }
            condition.put("status",tcExtendCar.getStatus());
            condition.put("createTime",tcExtendCar.getCreateTime());
            condition.put("count",count);
            tcExtendCarlist.add(condition);
        }
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",tcExtendCarlist);
    }

    /**
     * 查询我的车辆
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getMyList(Map<String, Object> map) throws Exception {
        String extendId =  MapUtils.getString(map, "extendId");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //客户ID
        if(StringUtils.isNotEmpty(extendId)){
            Criteria criteria = Criteria.where("extendId").is(Integer.valueOf(extendId));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcExtendCar> tcExtendCars = mongoComDAO.executeForObjectList(criteriasa,TcExtendCar.class,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcExtendCar.class);
        List<Map<String,Object>> tcExtendCarlist = new ArrayList<>();
        for(TcExtendCar tcExtendCar : tcExtendCars){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcExtendCar.getId());
            condition.put("extendId",tcExtendCar.getExtendId());
            condition.put("carId",tcExtendCar.getCarId());
            TcCar tcCar = mongoComDAO.executeForObjectById(tcExtendCar.getCarId(), TcCar.class);
            if(tcCar != null) {
                condition.put("parklotId", tcCar.getParklotId());
                condition.put("carNum", tcCar.getCarNum());
                condition.put("type", tcCar.getType());
                condition.put("vipType", tcCar.getVipType());
                condition.put("isTravel", tcCar.getIsTravel());
                condition.put("travelImg", tcCar.getTravelImg());
                condition.put("costSdate", tcCar.getCostSdate());
                condition.put("costEdate", tcCar.getCostEdate());
                condition.put("vipSdate", tcCar.getVipSdate());
                condition.put("vipEdate", tcCar.getVipEdate());
                condition.put("isEntry", tcCar.getIsEntry());
                if (tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_TEMP)) {
                    condition.put("vipName", ConstantType.CAR_VIP_TYPE_TEMP_NAME);
                } else if (tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_CARD)) {
                    condition.put("vipName", ConstantType.CAR_VIP_TYPE_CARD_NAME);
                }else if(tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_FREE)) {
                    condition.put("vipName", ConstantType.CAR_VIP_TYPE_FREE_NAME);
                }
            }
            condition.put("status",tcExtendCar.getStatus());
            condition.put("createTime",tcExtendCar.getCreateTime());
            condition.put("count",count);
            tcExtendCarlist.add(condition);
        }
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",tcExtendCarlist);
    }


}
