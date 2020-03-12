package com.perenc.xh.lsp.service.tcCarInpass.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.ConstantType;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.JsReturnJsonData;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.jshttp.JsHttpClientUtil;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.entity.tcCar.TcCar;
import com.perenc.xh.lsp.entity.tcCar.TcCarInpass;
import com.perenc.xh.lsp.entity.tcCar.TcCarJsclientlog;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCar;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerFreecar;
import com.perenc.xh.lsp.service.tcCarInpass.TcCarInpassService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


@Service("tcCarInpassService")
@Transactional(rollbackFor = Exception.class)
public class TcCarInpassServiceImpl implements TcCarInpassService {

    private static final Logger logger = Logger.getLogger(TcCarInpassServiceImpl.class);

    @Autowired
    private MongoComDAO mongoComDAO;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    // 本地线程存储log的id
    private static final ThreadLocal<String> logCarNameThreadLocal = new NamedThreadLocal<>("logCarNameThreadLocal");

    @Override
    public ReturnJsonData insert(TcCarInpass tcCarInpass) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        Map<String,Object> conditions = new HashMap<>();
        conditions.put("carNum",tcCarInpass.getCarNum());
        conditions.put("status",1);
        //当车辆进入出去时根据车牌号查询
        TcCar tcCar = mongoComDAO.executeForObjectByCon(conditions, TcCar.class);
        if(tcCar != null) {
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车牌号不存在",null);
        }
        tcCarInpass.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcCarInpass.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcCarInpass);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    /**
     * 捷顺
     * 第三方接口
     * 接收车辆入场过闸记录
     * @param tcCarInpass
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public JsReturnJsonData insertCarin(TcCarInpass tcCarInpass, Map<String, Object> map) throws Exception {
        try {
            //判断是否是真实的号码
            if(StringUtils.isNotEmpty(tcCarInpass.getCarNum()) && tcCarInpass.getCarNum().equals("未识别")) {
                return new JsReturnJsonData(DataCodeUtil.INSERT_DATABASE + "", "车辆未识别", null);
            }
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("carNum", tcCarInpass.getCarNum());
            conditions.put("status", 1);
            //当车辆进入出去时根据车牌号查询
            TcCar returnTcCar = mongoComDAO.executeForObjectByCon(conditions, TcCar.class);
            String carId = "";
            if (returnTcCar == null) {
                TcCar tcCar = new TcCar();
                tcCar.setCarNum(tcCarInpass.getCarNum());
                tcCar.setCarColor(tcCarInpass.getCarColor());
                tcCar.setType(ConstantType.CAR_TYPE_SMALL); //默认为小型车
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
                tcCar.setIsEntry(1);//1:已入场，2未入场
                //查询计费时间，是否入场
                //判断入场时间是否为空
                if (StringUtils.isNotEmpty(tcCarInpass.getInTime())) {
                    tcCar.setCostSdate(tcCarInpass.getInTime());
                } else {
                    tcCar.setCostSdate(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                }
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
                }
            } else {
                carId = returnTcCar.getId();
                //更改入场状态
                if(StringUtils.isNotEmpty(tcCarInpass.getInTime())) {
                    returnTcCar.setCostSdate(tcCarInpass.getInTime());
                }else {
                    returnTcCar.setCostSdate(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                }
                returnTcCar.setIsEntry(1);
                mongoComDAO.executeUpdate(returnTcCar);
            }
            //把车辆添加到车辆进出表
            tcCarInpass.setParklotId(""); //本地停车场Id
            tcCarInpass.setCarId(carId); //车辆ID
            tcCarInpass.setType(ConstantType.CAR_TYPE_SMALL); //默认为小型车
            tcCarInpass.setSdate(""); //计费开始时间
            tcCarInpass.setEdate(""); //计费结束时间
            tcCarInpass.setParkHour(null); //停车时长
            //图片判断
            String inImg=tcCarInpass.getIntimg();
            String temInImg="";
            if(StringUtils.isNotEmpty(inImg)){
                //判断是否包含http
                if(inImg.indexOf(ConstantType.JS_SERVICE_IP_LOCAL_IP)!=-1)
                { //包含
                    temInImg=inImg.replace(ConstantType.JS_SERVICE_IP_LOCAL_IP, ConstantType.JS_SERVICE_IP_LOCAL_ACCESS_IP);
                }
            }
            tcCarInpass.setIntimg(temInImg);
            tcCarInpass.setIsEntry(1);//1:已入场，2未入场
            tcCarInpass.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            tcCarInpass.setStatus(1);
            int flag = mongoComDAO.executeInsert(tcCarInpass);
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
    public ReturnJsonData update(TcCarInpass tcCarInpass) throws Exception {
        TcCarInpass returnTcCarInpass = mongoComDAO.executeForObjectById(tcCarInpass.getId(), TcCarInpass.class);
        if(returnTcCarInpass != null){
            int flag = mongoComDAO.executeUpdate(returnTcCarInpass);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcCarInpass.class);
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
        TcCarInpass tcCarInpass = mongoComDAO.executeForObjectById(id, TcCarInpass.class);
        if (tcCarInpass!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcCarInpass.getId());
            condition.put("parklotId",tcCarInpass.getParklotId());
            condition.put("parkId",tcCarInpass.getParkId());
            condition.put("inRecordId",tcCarInpass.getInRecordId());
            condition.put("inDeviceId",tcCarInpass.getInDeviceId());
            condition.put("inDeviceName",tcCarInpass.getInDeviceName());
            condition.put("inTime",tcCarInpass.getInTime());
            condition.put("carId",tcCarInpass.getCarId());
            condition.put("carNum",tcCarInpass.getCarNum());
            condition.put("carColor",tcCarInpass.getCarColor());
            condition.put("type",tcCarInpass.getType());
            condition.put("sdate",tcCarInpass.getSdate());
            condition.put("edate",tcCarInpass.getEdate());
            // 停车时长（时分）
            String carDuration= ToolUtil.getDateDayHourMinute(tcCarInpass.getInTime(),strNowTime);
            condition.put("parkHour",carDuration);
            condition.put("intimg",tcCarInpass.getIntimg());
            condition.put("isEntry",tcCarInpass.getIsEntry());
            condition.put("stationOperator",tcCarInpass.getStationOperator());
            condition.put("sealName",tcCarInpass.getSealName());
            condition.put("status",tcCarInpass.getStatus());
            condition.put("createTime",tcCarInpass.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String strNowTime =DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS);
        String carNum =  MapUtils.getString(map, "carNum");
        String inSdate =  MapUtils.getString(map, "inSdate");
        String inEdate =  MapUtils.getString(map, "inEdate");
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
        if(StringUtils.isNotEmpty(inSdate) && StringUtils.isNotEmpty(inEdate)){
            Criteria criteria = Criteria.where("inTime").gte(inSdate).lte(inEdate);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcCarInpass> tcCarInpasss = mongoComDAO.executeForObjectList(criteriasa,TcCarInpass.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcCarInpass.class);
        List<Map<String,Object>> tcCarInpassslist = new ArrayList<>();
        for(TcCarInpass tcCarInpass : tcCarInpasss){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcCarInpass.getId());
            condition.put("parklotId",tcCarInpass.getParklotId());
            condition.put("parkId",tcCarInpass.getParkId());
            condition.put("inRecordId",tcCarInpass.getInRecordId());
            condition.put("inDeviceId",tcCarInpass.getInDeviceId());
            condition.put("inDeviceName",tcCarInpass.getInDeviceName());
            condition.put("inTime",tcCarInpass.getInTime());
            condition.put("carId",tcCarInpass.getCarId());
            condition.put("carNum",tcCarInpass.getCarNum());
            condition.put("carColor",tcCarInpass.getCarColor());
            condition.put("type",tcCarInpass.getType());
            condition.put("sdate",tcCarInpass.getSdate());
            condition.put("edate",tcCarInpass.getEdate());
            // 停车时长（时分）
            String carDuration=ToolUtil.getDateDayHourMinute(tcCarInpass.getInTime(),strNowTime);
            /*String carDuration="";
            if (tcCarInpass.getIsEntry().equals(1)) {
                //捷顺车辆请求计费
                //调用捷顺接口
                String reqJsonStr = "{\"plateNumber\":\""+tcCarInpass.getCarNum()+"\"}";
                JSONObject jsonCharging= JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL+"park/charging",reqJsonStr);
                String code = jsonCharging.getString("code");
                JSONObject jsondata  = jsonCharging.getJSONObject("data");
                if(code.equals("0")) {
                    carDuration=jsondata.getString("stopTime");
                }else if(code.equals("1")) {//"车辆未入场:状态为1,更改车辆状态
                    tcCarInpass.setIsEntry(2);
                    mongoComDAO.executeUpdate(tcCarInpass);
                }
            }*/
            condition.put("parkHour",carDuration);
            condition.put("intimg",tcCarInpass.getIntimg());
            condition.put("isEntry",tcCarInpass.getIsEntry());
            condition.put("stationOperator",tcCarInpass.getStationOperator());
            condition.put("sealName",tcCarInpass.getSealName());
            condition.put("status",tcCarInpass.getStatus());
            condition.put("createTime",tcCarInpass.getCreateTime());
            tcCarInpassslist.add(condition);
        }
        map.clear();
        map.put("list",tcCarInpassslist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

    /**
     * 场内车辆查询
     * 场内所停的车辆
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    /*@Override
    public ReturnJsonData getInsideList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String strNowTime =DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS);
        String carNum =  MapUtils.getString(map, "carNum");
        String inSdate =  MapUtils.getString(map, "inSdate");
        String inEdate =  MapUtils.getString(map, "inEdate");
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
        if(StringUtils.isNotEmpty(inSdate) && StringUtils.isNotEmpty(inEdate)){
            Criteria criteria = Criteria.where("inTime").gte(inSdate).lte(inEdate);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        //标识为场内入场状态:已入场
        Criteria criteriab = Criteria.where("isEntry").is(1);
        criteriasa.add(criteriab);
        List<TcCarInpass> tcCarInpasss = mongoComDAO.executeForObjectList(criteriasa,TcCarInpass.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcCarInpass.class);
        List<Map<String,Object>> tcCarInpassslist = new ArrayList<>();
        for(TcCarInpass tcCarInpass : tcCarInpasss){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcCarInpass.getId());
            condition.put("parklotId",tcCarInpass.getParklotId());
            condition.put("parkId",tcCarInpass.getParkId());
            condition.put("inRecordId",tcCarInpass.getInRecordId());
            condition.put("inDeviceId",tcCarInpass.getInDeviceId());
            condition.put("inDeviceName",tcCarInpass.getInDeviceName());
            condition.put("inTime",tcCarInpass.getInTime());
            condition.put("carId",tcCarInpass.getCarId());
            condition.put("carNum",tcCarInpass.getCarNum());
            condition.put("carColor",tcCarInpass.getCarColor());
            condition.put("type",tcCarInpass.getType());
            condition.put("sdate",tcCarInpass.getSdate());
            condition.put("edate",tcCarInpass.getEdate());
            // 停车时长（时分）
            String carDuration=ToolUtil.getDateDayHourMinute(tcCarInpass.getInTime(),strNowTime);
            condition.put("parkHour", carDuration);
            condition.put("intimg",tcCarInpass.getIntimg());
            condition.put("isEntry",tcCarInpass.getIsEntry());
            condition.put("stationOperator",tcCarInpass.getStationOperator());
            condition.put("sealName",tcCarInpass.getSealName());
            condition.put("status",tcCarInpass.getStatus());
            condition.put("createTime",tcCarInpass.getCreateTime());
            tcCarInpassslist.add(condition);
        }
        map.clear();
        map.put("list",tcCarInpassslist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }*/

    /**
     * 场内车辆查询
     * 场内所停的车辆
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getInsideList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String strNowTime =DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS);
        String carNum =  MapUtils.getString(map, "carNum");
        String inSdate =  MapUtils.getString(map, "inSdate");
        String inEdate =  MapUtils.getString(map, "inEdate");
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
        if(StringUtils.isNotEmpty(inSdate) && StringUtils.isNotEmpty(inEdate)){
            Criteria criteria = Criteria.where("costSdate").gte(inSdate).lte(inEdate);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        //标识为场内入场状态:已入场
        Criteria criteriab = Criteria.where("isEntry").is(1);
        criteriasa.add(criteriab);
        List<TcCar> tcCars = mongoComDAO.executeForObjectList(criteriasa,TcCar.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcCar.class);
        List<Map<String,Object>> tcCarInpassslist = new ArrayList<>();
        for(TcCar tcCar : tcCars){
            List<Criteria> criteriasab = new ArrayList<Criteria>();
            Criteria criteriapb = Criteria.where("carNum").is(tcCar.getCarNum());
            criteriasab.add(criteriapb);
            PageHelper pageHelperb = new PageHelper();
            pageHelperb.setPage(1);
            pageHelperb.setRows(1);
            List<TcCarInpass> tcCarInpasss = mongoComDAO.executeForObjectList(criteriasab,TcCarInpass.class,pageHelperb,orders);
            TcCarInpass tcCarInpass=tcCarInpasss.get(0);
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcCarInpass.getId());
            condition.put("parklotId",tcCarInpass.getParklotId());
            condition.put("parkId",tcCarInpass.getParkId());
            condition.put("inRecordId",tcCarInpass.getInRecordId());
            condition.put("inDeviceId",tcCarInpass.getInDeviceId());
            condition.put("inDeviceName",tcCarInpass.getInDeviceName());
            condition.put("inTime",tcCarInpass.getInTime());
            condition.put("carId",tcCarInpass.getCarId());
            condition.put("carNum",tcCarInpass.getCarNum());
            condition.put("carColor",tcCarInpass.getCarColor());
            condition.put("type",tcCarInpass.getType());
            condition.put("sdate",tcCarInpass.getSdate());
            condition.put("edate",tcCarInpass.getEdate());
            // 停车时长（时分）
            String carDuration=ToolUtil.getDateDayHourMinute(tcCarInpass.getInTime(),strNowTime);
            condition.put("parkHour", carDuration);
            condition.put("intimg",tcCarInpass.getIntimg());
            condition.put("isEntry",tcCarInpass.getIsEntry());
            condition.put("stationOperator",tcCarInpass.getStationOperator());
            condition.put("sealName",tcCarInpass.getSealName());
            condition.put("status",tcCarInpass.getStatus());
            condition.put("createTime",tcCarInpass.getCreateTime());
            tcCarInpassslist.add(condition);
        }
        map.clear();
        map.put("list",tcCarInpassslist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

    /**
     * 场内车辆查询
     * 场内所停的车辆_调用捷顺
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getInsideJsList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfymd = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        String strNowTime = sdf.format(calendar.getTime());
        String carNum =  MapUtils.getString(map, "carNum");
        String inSdate =  MapUtils.getString(map, "inSdate");
        String inEdate =  MapUtils.getString(map, "inEdate");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));

        List<Map<String,Object>> tcCarInpassslist = new ArrayList<>();
        long count =0;
        //调用捷顺接口查询场内记录
        String reqJsonChargStr = "{\"pageIndex\":" +pageHelper.getPage()+ "," +
                "\"pageSize\":" +pageHelper.getRows();
        //车牌号
        if(StringUtils.isNotEmpty(carNum)){
            reqJsonChargStr=reqJsonChargStr+",\"plateNumber\":\""+carNum+"\"";
        }
        //出场开始时间
        if(StringUtils.isNotEmpty(inSdate)){
            reqJsonChargStr=reqJsonChargStr+",\"startTime\":\""+inSdate+"\"";
        }
        //出场结束时间
        if(StringUtils.isNotEmpty(inEdate)){
            reqJsonChargStr=reqJsonChargStr+",\"endTime\":\""+inEdate+"\"";
        }
        reqJsonChargStr=reqJsonChargStr+"}";
        JSONObject jsonCharging = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/inparkingrecord", reqJsonChargStr);
        if (jsonCharging != null) {
            String code = jsonCharging.getString("code");
            JSONObject jsondata  = jsonCharging.getJSONObject("data");
            if (code.equals("0")) {
                JSONArray recordJsarry=jsondata.getJSONArray("records");
                count=jsondata.getInteger("pageCount");
                for (int i = 0; i < recordJsarry.size(); i++) {
                    JSONObject jsonRecords = recordJsarry.getJSONObject(i);
                    Map<String,Object> condition = new HashMap<>();
                    condition.put("parkId",jsonRecords.getString("parkId"));
                    condition.put("inRecordId",jsonRecords.getString("inRecordId"));
                    condition.put("inDeviceId",jsonRecords.getString("inDeviceId"));
                    condition.put("inDeviceName",jsonRecords.getString("inDeviceName"));
                    String inTime=jsonRecords.getString("inTime");
                    String teminTime= inTime.replaceAll("/", "-");
                    condition.put("inTime",teminTime);
                    condition.put("carNum",jsonRecords.getString("plateNumber"));
                    condition.put("carColor",jsonRecords.getString("plateColor"));
                    //计算停车时长
                    //停车时长（小时）：当前时间-进厂时间
                    String carDuration = ToolUtil.getDateDayHourMinute(teminTime,strNowTime);
                    condition.put("parkHour",carDuration);

                    condition.put("intimg",jsonRecords.getString("inImage"));
                    condition.put("isEntry",1);
                    condition.put("stationOperator",jsonRecords.getString("stationOperator"));
                    condition.put("sealName",jsonRecords.getString("sealName"));
                    tcCarInpassslist.add(condition);
                }
            }
        }
        map.clear();
        map.put("list",tcCarInpassslist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }



    /**
     * 定时任务每天晚上12点执行
     * 根据捷顺查询场内车辆，更改入场状态
     * 是否入场（1:已入场，2:未入场）
     * @return
     * @throws Exception
     */
    @Override
    public Integer updateBatchIsEntryByInsideJscar() throws Exception {
        Integer num=0;
        // 根据当前时间，车辆15天前的入场状态更改
        num=updateBatchTcCarIsEntryByEdate();
        //修改判断车牌号是否存在
        /*List<Criteria> criteriasa = new ArrayList<Criteria>();
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        //标识为场内入场状态:已入场
        Criteria criteriab = Criteria.where("isEntry").is(1);
        criteriasa.add(criteriab);
        //车辆信息
        List<TcCar> tcCars = mongoComDAO.executeForObjectList(criteriasa,TcCar.class);
        for (int i = 0; i < tcCars.size(); i++) {
            TcCar tcCar=tcCars.get(i);
            String reqJsonChargStr = "{\"pageIndex\":" +1+ "," +
                    "\"pageSize\":" +1;
            reqJsonChargStr=reqJsonChargStr+",\"plateNumber\":\""+tcCar.getCarNum()+"\"";
            reqJsonChargStr=reqJsonChargStr+"}";
            JSONObject jsonCharging =new JSONObject();
            String logCarName = logCarNameThreadLocal.get();
            if(!tcCar.getCarNum().equals(logCarName)) {
                jsonCharging = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/inparkingrecord", reqJsonChargStr);
            }
            logCarNameThreadLocal.set(tcCar.getCarNum());
            //添加日志
            final String finalreqJsonChargStr=reqJsonChargStr;
            final JSONObject finalJsonCharging = jsonCharging;
            if(i<5 || i==tcCars.size()-1) {
                threadPoolTaskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //添加日志
                            TcCarJsclientlog tjsclientlog = new TcCarJsclientlog();
                            tjsclientlog.setType(1);
                            tjsclientlog.setUrl(JsHttpClientUtil.JS_CLIENT_URL + "park/inparkingrecord");
                            tjsclientlog.setRmode(1);
                            tjsclientlog.setRparameter(finalreqJsonChargStr);
                            tjsclientlog.setContent(finalJsonCharging.toString());
                            tjsclientlog.setCarNum(tcCar.getCarNum());
                            tjsclientlog.setRemark("查询车辆场内");
                            tjsclientlog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                            tjsclientlog.setStatus(1);
                            mongoComDAO.executeInsert(tjsclientlog);
                        } catch (Exception e) {
                            logger.error("执行错误：" + e.getMessage(), e);
                        }
                    }
                });
            }
            if (jsonCharging == null) {
                break;
            }
            String code = jsonCharging.getString("code");
            if(!code.equals("0")) {
                break;
            }
            long logTime =jsonCharging.getLongValue("rstime")+100;
            JSONObject jsondata = jsonCharging.getJSONObject("data");
            String strRecords=jsondata.getString("records");
            JSONArray recordJsarry=JSONArray.parseArray(strRecords);
            if(recordJsarry.size()<1) {
                //更改车辆入场状态
                TcCar returnTcCar = mongoComDAO.executeForObjectById(tcCar.getId(), TcCar.class);
                if (returnTcCar != null) {
                    returnTcCar.setIsEntry(2);
                    num = +mongoComDAO.executeUpdate(returnTcCar);
                }
            }
            Thread.sleep(logTime); //设置暂停的时间 5 秒
        }*/
        return num;
    }

    /**
     * 根据当前时间
     * 删除数据
     * 根据当前时间，车辆15天前的入场状态更改
     * @return
     * @throws Exception
     */
    public Integer updateBatchTcCarIsEntryByEdate() throws Exception {
        Integer num=0;
        // 更改15天前的车辆
        SimpleDateFormat  sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar =null;
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -15);//当前时间前去天数,
        String  strnowDate=sdfhms.format(calendar.getTime()); //当前时间
        //修改判断车牌号是否存在
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        Criteria criteria = Criteria.where("costSdate").lte(strnowDate);
        criteriasa.add(criteria);
        Criteria criterib = Criteria.where("isEntry").is(1);
        criteriasa.add(criterib);
        Criteria criteric = Criteria.where("status").is(1);
        criteriasa.add(criteric);

        Map<String,Object> contenta=new HashMap<>();
        contenta.put("isEntry",2);
        num=+mongoComDAO.executeUpdate(criteriasa,contenta,TcCar.class);

        //修改判断车牌号是否存在
        List<Criteria> criteriasab = new ArrayList<Criteria>();
        //车牌模糊查询
        Pattern pattern = Pattern.compile("^.*" + "民航" + ".*$");
        Criteria criteriab = Criteria.where("carNum").regex(pattern);
        criteriasab.add(criteriab);
        Criteria criteribb = Criteria.where("isEntry").is(1);
        criteriasab.add(criteribb);
        Criteria critericb = Criteria.where("status").is(1);
        criteriasab.add(critericb);
        Map<String,Object> contentb=new HashMap<>();
        contentb.put("isEntry",2);
        num=+mongoComDAO.executeUpdate(criteriasab,contentb,TcCar.class);
        return  num;
    }


    /**
     * 根据捷顺查询场内车辆非正常，更改入场状态
     * 是否入场（1:已入场，2:未入场）
     * @return
     * @throws Exception
     */
    @Override
    public Integer updateBatchIsEntryByInsideJscarAbnormal(String carNum, String tocNum) throws Exception {
        Integer num=0;
        String reqJsonChargStr = "{\"pageIndex\":" +1+ "," +
                "\"pageSize\":" +1;
        reqJsonChargStr=reqJsonChargStr+",\"plateNumber\":\""+carNum+"\"";
        reqJsonChargStr=reqJsonChargStr+"}";
        JSONObject jsonCharging =new JSONObject();
        jsonCharging = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/inparkingrecord", reqJsonChargStr);
        if (jsonCharging == null) {
            return 0;
        }
        String code = jsonCharging.getString("code");
        if(!code.equals("0")) {
            return 0;
        }
        JSONObject jsondata = jsonCharging.getJSONObject("data");
        JSONArray recordJsarry=jsondata.getJSONArray("records");
        if(recordJsarry.size()>0) {
            JSONObject jsondatar=recordJsarry.getJSONObject(0);
            String inRecordId=jsondatar.getString("inRecordId");
            String ncarNum="桂FDR3P2";
            if(StringUtils.isNotEmpty(tocNum)){
                ncarNum=tocNum;
            }
            //更改车辆入场状态
            String reqJsonChargCorStr = "{\"inRecordId\":\"" +inRecordId+"\""+",\"plateNumber\":\""+ncarNum+"\"";
            reqJsonChargCorStr=reqJsonChargCorStr+"}";
            JSONObject jsonChargCoring =new JSONObject();
            jsonChargCoring = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/correctplate", reqJsonChargCorStr);
            if (jsonChargCoring == null) {
                return 0;
            }
            String codeCor = jsonChargCoring.getString("code");
            if(codeCor.equals("0")) {
                num=1;
            }
        }
        return num;
    }



    /**
     * 根据当前时间
     * 删除数据
     * 根据当前时间，日志30天前的日志删除
     */
    @Override
    public Integer removeBatchTcCarInpassByEdate() throws Exception {
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
        num =+ mongoComDAO.executeDelete(criteriasa,TcCarInpass.class);
        return num;
    }



}
