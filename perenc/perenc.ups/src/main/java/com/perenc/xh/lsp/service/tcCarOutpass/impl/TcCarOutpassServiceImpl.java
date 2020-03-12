package com.perenc.xh.lsp.service.tcCarOutpass.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.ConstantType;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.JsReturnJsonData;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.entity.tcCar.TcCar;
import com.perenc.xh.lsp.entity.tcCar.TcCarInpass;
import com.perenc.xh.lsp.entity.tcCar.TcCarOutpass;
import com.perenc.xh.lsp.service.tcCarOutpass.TcCarOutpassService;
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
import java.util.regex.Pattern;


@Service("tcCarOutpassService")
@Transactional(rollbackFor = Exception.class)
public class TcCarOutpassServiceImpl implements TcCarOutpassService {

    private static final Logger logger = Logger.getLogger(TcCarOutpassServiceImpl.class);

    @Autowired
    private MongoComDAO mongoComDAO;

    @Override
    public ReturnJsonData insert(TcCarOutpass tcCarOutpass) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        Map<String,Object> conditions = new HashMap<>();
        conditions.put("carNum",tcCarOutpass.getCarNum());
        conditions.put("status",1);
        //当车辆进入出去时根据车牌号查询
        TcCar tcCar = mongoComDAO.executeForObjectByCon(conditions, TcCar.class);
        if(tcCar != null) {
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车牌号不存在",null);
        }
        tcCarOutpass.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcCarOutpass.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcCarOutpass);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }


    /**
     * 捷顺
     * 第三方接口
     * 接收车辆出场过闸记录
     * @param tcCarOutpass
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public JsReturnJsonData insertCarout(TcCarOutpass tcCarOutpass, Map<String, Object> map) throws Exception {
        try {
            //判断是否是真实的号码
            if(StringUtils.isNotEmpty(tcCarOutpass.getCarNum()) && tcCarOutpass.getCarNum().equals("未识别")) {
                return new JsReturnJsonData(DataCodeUtil.UPDATE_DATABASE + "", "车辆未识别", null);
            }
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("carNum", tcCarOutpass.getCarNum());
            conditions.put("status", 1);
            //当车辆进入出去时根据车牌号查询
            TcCar returnTcCar = mongoComDAO.executeForObjectByCon(conditions, TcCar.class);
            String carId = "";
            if (returnTcCar != null) {
                carId = returnTcCar.getId();
                //更改是否入场状态
                returnTcCar.setIsEntry(2);//1:已入场，2未入场
                mongoComDAO.executeUpdate(returnTcCar);
            }
            //把车辆添加到车辆进出表
            //把车辆添加出场过闸表,更改车辆入场过闸记录表的是否入场状态为未入场
            List<Criteria> criteriasa = new ArrayList<Criteria>();
            if(StringUtils.isNotEmpty(tcCarOutpass.getCarNum())) {
                Criteria criteria = Criteria.where("carNum").is(tcCarOutpass.getCarNum());
                criteriasa.add(criteria);
                Criteria criterib = Criteria.where("isEntry").is(1);
                criteriasa.add(criterib);
            }
            List<TcCarInpass> TcCarInpass = mongoComDAO.executeForObjectList(criteriasa,TcCarInpass.class);
            if(TcCarInpass.size()>0) {
                Map<String, Object> conditionin = new HashMap<>();
                conditionin.put("carNum", tcCarOutpass.getCarNum());
                conditionin.put("isEntry", 1); //1:已入场，2未入场
                Map<String, Object> conditioninup = new HashMap<>();
                conditioninup.put("isEntry", 2); //1:已入场，2未入场
                mongoComDAO.executeUpdate(conditionin, conditioninup, TcCarInpass.class);
            }
            //当车辆进入出去时根据车牌号查询
            tcCarOutpass.setParklotId(""); //本地停车场Id
            //入场ID
            tcCarOutpass.setCarInpassId("");
            tcCarOutpass.setCarId(carId); //车辆ID
            tcCarOutpass.setType(ConstantType.CAR_TYPE_SMALL); //默认为小型车
            tcCarOutpass.setSdate(""); //计费开始时间
            tcCarOutpass.setEdate(""); //计费结束时间
            tcCarOutpass.setParkHour(null); //停车时长
            //图片判断
            String inImg=tcCarOutpass.getIntimg();
            String temInImg="";
            if(StringUtils.isNotEmpty(inImg)){
                //判断是否包含http
                if(inImg.indexOf(ConstantType.JS_SERVICE_IP_LOCAL_IP)!=-1)
                { //包含
                    temInImg=inImg.replace(ConstantType.JS_SERVICE_IP_LOCAL_IP, ConstantType.JS_SERVICE_IP_LOCAL_ACCESS_IP);
                }
            }
            tcCarOutpass.setIntimg(temInImg);
            String outImg=tcCarOutpass.getOutimg();
            String temOutImg="";
            if(StringUtils.isNotEmpty(outImg)){
                //判断是否包含http
                if(outImg.indexOf(ConstantType.JS_SERVICE_IP_LOCAL_IP)!=-1)
                { //包含
                    temOutImg=outImg.replace(ConstantType.JS_SERVICE_IP_LOCAL_IP, ConstantType.JS_SERVICE_IP_LOCAL_ACCESS_IP);
                }
            }
            tcCarOutpass.setOutimg(temOutImg);
            tcCarOutpass.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            tcCarOutpass.setStatus(1);
            int flag = mongoComDAO.executeInsert(tcCarOutpass);
            if (flag > 0) {
                return new JsReturnJsonData(DataCodeUtil.SUCCESS + "", "添加成功", null);
            } else {
                return new JsReturnJsonData(DataCodeUtil.INSERT_DATABASE + "", "添加失败", null);
            }
        }catch (Exception e) {
            logger.info("执行错误"+e.getMessage());
        }
        return new JsReturnJsonData(DataCodeUtil.INSERT_DATABASE + "", "添加失败", null);
    }




    @Override
    public ReturnJsonData update(TcCarOutpass tcCarOutpass) throws Exception {
        TcCarOutpass returnTcCarOutpass = mongoComDAO.executeForObjectById(tcCarOutpass.getId(), TcCarOutpass.class);
        if(returnTcCarOutpass != null){
            returnTcCarOutpass.setParklotId(tcCarOutpass.getParklotId());
            returnTcCarOutpass.setCarId(tcCarOutpass.getCarId());
            returnTcCarOutpass.setCarNum(tcCarOutpass.getCarNum());
            int flag = mongoComDAO.executeUpdate(returnTcCarOutpass);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcCarOutpass.class);
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
        TcCarOutpass tcCarOutpass = mongoComDAO.executeForObjectById(id, TcCarOutpass.class);
        if (tcCarOutpass!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcCarOutpass.getId());
            condition.put("parklotId",tcCarOutpass.getParklotId());
            condition.put("parkId",tcCarOutpass.getParkId());
            condition.put("carInpassId",tcCarOutpass.getCarInpassId());
            condition.put("inRecordId",tcCarOutpass.getInRecordId());
            condition.put("outRecordId",tcCarOutpass.getOutRecordId());
            condition.put("inDeviceId",tcCarOutpass.getInDeviceId());
            condition.put("inDeviceName",tcCarOutpass.getInDeviceName());
            condition.put("inTime",tcCarOutpass.getInTime());
            condition.put("outDeviceId",tcCarOutpass.getOutDeviceId());
            condition.put("outDeviceName",tcCarOutpass.getOutDeviceName());
            condition.put("outTime",tcCarOutpass.getOutTime());
            condition.put("carId",tcCarOutpass.getCarId());
            condition.put("carNum",tcCarOutpass.getCarNum());
            condition.put("type",tcCarOutpass.getType());
            condition.put("sdate",tcCarOutpass.getSdate());
            condition.put("edate",tcCarOutpass.getEdate());
            //计算停车时长
            //停车时长（小时）：出场时间-入场时间
            String carDuration = ToolUtil.getDateDayHourMinute(tcCarOutpass.getInTime(),tcCarOutpass.getOutTime());
            condition.put("parkHour",carDuration);
            condition.put("intimg",tcCarOutpass.getIntimg());
            condition.put("outimg",tcCarOutpass.getOutimg());
            condition.put("stationOperator",tcCarOutpass.getStationOperator());
            condition.put("chargeTotal",ToolUtil.getIntToDouble(tcCarOutpass.getChargeTotal()));
            condition.put("discountAmount",ToolUtil.getIntToDouble(tcCarOutpass.getDiscountAmount()));
            condition.put("charge",ToolUtil.getIntToDouble(tcCarOutpass.getCharge()));
            condition.put("sealName",tcCarOutpass.getSealName());
            condition.put("status",tcCarOutpass.getStatus());
            condition.put("createTime",tcCarOutpass.getCreateTime());
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
            Criteria criteria = Criteria.where("outTime").gte(outSdate).lte(outEdate);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcCarOutpass> tcCarOutpasss = mongoComDAO.executeForObjectList(criteriasa,TcCarOutpass.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcCarOutpass.class);
        List<Map<String,Object>> tcCarOutpassslist = new ArrayList<>();
        for(TcCarOutpass tcCarOutpass : tcCarOutpasss){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcCarOutpass.getId());
            condition.put("parklotId",tcCarOutpass.getParklotId());
            condition.put("parkId",tcCarOutpass.getParkId());
            condition.put("carInpassId",tcCarOutpass.getCarInpassId());
            condition.put("inRecordId",tcCarOutpass.getInRecordId());
            condition.put("outRecordId",tcCarOutpass.getOutRecordId());
            condition.put("inDeviceId",tcCarOutpass.getInDeviceId());
            condition.put("inDeviceName",tcCarOutpass.getInDeviceName());
            condition.put("inTime",tcCarOutpass.getInTime());
            condition.put("outDeviceId",tcCarOutpass.getOutDeviceId());
            condition.put("outDeviceName",tcCarOutpass.getOutDeviceName());
            condition.put("outTime",tcCarOutpass.getOutTime());
            condition.put("carId",tcCarOutpass.getCarId());
            condition.put("carNum",tcCarOutpass.getCarNum());
            condition.put("type",tcCarOutpass.getType());
            condition.put("sdate",tcCarOutpass.getSdate());
            condition.put("edate",tcCarOutpass.getEdate());
            //计算停车时长
            //停车时长（小时）：出场时间-入场时间
            String carDuration = ToolUtil.getDateDayHourMinute(tcCarOutpass.getInTime(),tcCarOutpass.getOutTime());
            condition.put("parkHour",carDuration);
            condition.put("intimg",tcCarOutpass.getIntimg());
            condition.put("outimg",tcCarOutpass.getOutimg());
            condition.put("stationOperator",tcCarOutpass.getStationOperator());
            condition.put("chargeTotal",ToolUtil.getIntToDouble(tcCarOutpass.getChargeTotal()));
            condition.put("discountAmount",ToolUtil.getIntToDouble(tcCarOutpass.getDiscountAmount()));
            condition.put("charge",ToolUtil.getIntToDouble(tcCarOutpass.getCharge()));
            condition.put("sealName",tcCarOutpass.getSealName());
            condition.put("status",tcCarOutpass.getStatus());
            condition.put("createTime",tcCarOutpass.getCreateTime());
            tcCarOutpassslist.add(condition);
        }
        map.clear();
        map.put("list",tcCarOutpassslist);//返回前端集合命名为list
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
    public Integer removeBatchTcCarOutpassByEdate() throws Exception {
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
        num =+ mongoComDAO.executeDelete(criteriasa,TcCarOutpass.class);
        return num;
    }

}
