package com.perenc.xh.lsp.service.tcCarIn.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.ConstantType;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.JsReturnJsonData;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.entity.tcCar.TcCar;
import com.perenc.xh.lsp.entity.tcCar.TcCarIn;
import com.perenc.xh.lsp.service.tcCarIn.TcCarInService;
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


@Service("tcCarInService")
@Transactional(rollbackFor = Exception.class)
public class TcCarInServiceImpl implements TcCarInService {

    private static final Logger logger = Logger.getLogger(TcCarInServiceImpl.class);

    @Autowired
    private MongoComDAO mongoComDAO;

    @Override
    public ReturnJsonData insert(TcCarIn tcCarIn) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        Map<String,Object> conditions = new HashMap<>();
        conditions.put("carNum",tcCarIn.getCarNum());
        conditions.put("status",1);
        //当车辆进入出去时根据车牌号查询
        TcCar tcCar = mongoComDAO.executeForObjectByCon(conditions, TcCar.class);
        if(tcCar != null) {
            tcCarIn.setParklotId(tcCar.getParklotId());
            tcCarIn.setCarId(tcCar.getId());
            tcCarIn.setCarNum(tcCar.getCarNum());
            tcCarIn.setType(tcCar.getType());
            tcCarIn.setSdate(tcCar.getCostSdate());
            tcCarIn.setEdate(tcCar.getCostEdate());
            tcCarIn.setIntimg(tcCar.getIntimg());
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车牌号不存在",null);
        }
        tcCarIn.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcCarIn.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcCarIn);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    /**
     * 捷顺
     * 第三方接口
     * 接收车辆入场识别记录
     * @param tcCarIn
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public JsReturnJsonData insertInrecognition(TcCarIn tcCarIn, Map<String, Object> map) throws Exception {
         try {
             //判断是否是真实的号码
             if(StringUtils.isNotEmpty(tcCarIn.getCarNum()) && tcCarIn.getCarNum().equals("未识别")) {
                 return new JsReturnJsonData(DataCodeUtil.SUCCESS + "", "车辆未识别", null);
             }
            // 把车辆添加到车辆进出表
            tcCarIn.setParklotId(""); //本地停车场Id
            //tcCarIn.setCarId(carId); //车辆ID
            tcCarIn.setType(ConstantType.CAR_TYPE_SMALL); //默认为小型车
            tcCarIn.setSdate(""); //计费开始时间
            tcCarIn.setEdate(""); //计费结束时间
            tcCarIn.setParkHour(null); //停车时长
            //图片判断
            String inImg=tcCarIn.getIntimg();
            String temInImg="";
            if(StringUtils.isNotEmpty(inImg)){
                //判断是否包含http
                if(inImg.indexOf(ConstantType.JS_SERVICE_IP_LOCAL_IP)!=-1)
                { //包含
                    temInImg=inImg.replace(ConstantType.JS_SERVICE_IP_LOCAL_IP, ConstantType.JS_SERVICE_IP_LOCAL_ACCESS_IP);
                }
            }
            tcCarIn.setIntimg(temInImg);
            tcCarIn.setIsEntry(1);//1:已入场，2未入场
            tcCarIn.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            tcCarIn.setStatus(1);
            int flag = mongoComDAO.executeInsert(tcCarIn);
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
    public ReturnJsonData update(TcCarIn tcCarIn) throws Exception {
        TcCarIn returnTcCarIn = mongoComDAO.executeForObjectById(tcCarIn.getId(), TcCarIn.class);
        if(returnTcCarIn != null){
            returnTcCarIn.setParklotId(tcCarIn.getParklotId());
            returnTcCarIn.setCarId(tcCarIn.getCarId());
            returnTcCarIn.setCarNum(tcCarIn.getCarNum());
            returnTcCarIn.setType(tcCarIn.getType());
            returnTcCarIn.setSdate(tcCarIn.getSdate());
            returnTcCarIn.setEdate(tcCarIn.getEdate());
            returnTcCarIn.setParkHour(tcCarIn.getParkHour());
            returnTcCarIn.setIntimg(tcCarIn.getIntimg());
            int flag = mongoComDAO.executeUpdate(returnTcCarIn);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcCarIn.class);
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
        TcCarIn tcCarIn = mongoComDAO.executeForObjectById(id, TcCarIn.class);
        if (tcCarIn!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcCarIn.getId());
            condition.put("parklotId",tcCarIn.getParklotId());
            condition.put("parkId",tcCarIn.getParkId());
            condition.put("inDeviceId",tcCarIn.getInDeviceId());
            condition.put("inDeviceName",tcCarIn.getInDeviceName());
            condition.put("inRecognitionTime",tcCarIn.getInRecognitionTime());
            condition.put("carId",tcCarIn.getCarId());
            condition.put("carNum",tcCarIn.getCarNum());
            condition.put("carColor",tcCarIn.getCarColor());
            condition.put("type",tcCarIn.getType());
            condition.put("sdate",tcCarIn.getSdate());
            condition.put("edate",tcCarIn.getEdate());
            condition.put("parkHour",tcCarIn.getParkHour());
            condition.put("intimg",tcCarIn.getIntimg());
            condition.put("isEntry",tcCarIn.getIsEntry());
            condition.put("status",tcCarIn.getStatus());
            condition.put("createTime",tcCarIn.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
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
            Criteria criteria = Criteria.where("inRecognitionTime").gte(inSdate).lte(inEdate);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcCarIn> tcCarIns = mongoComDAO.executeForObjectList(criteriasa,TcCarIn.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcCarIn.class);
        List<Map<String,Object>> TcCarInlist = new ArrayList<>();
        for(TcCarIn tcCarIn : tcCarIns){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcCarIn.getId());
            condition.put("parklotId",tcCarIn.getParklotId());
            condition.put("parkId",tcCarIn.getParkId());
            condition.put("inDeviceId",tcCarIn.getInDeviceId());
            condition.put("inDeviceName",tcCarIn.getInDeviceName());
            condition.put("inRecognitionTime",tcCarIn.getInRecognitionTime());
            condition.put("carId",tcCarIn.getCarId());
            condition.put("carNum",tcCarIn.getCarNum());
            condition.put("carColor",tcCarIn.getCarColor());
            condition.put("type",tcCarIn.getType());
            condition.put("sdate",tcCarIn.getSdate());
            condition.put("edate",tcCarIn.getEdate());
            condition.put("parkHour",tcCarIn.getParkHour());
            condition.put("intimg",tcCarIn.getIntimg());
            condition.put("isEntry",tcCarIn.getIsEntry());
            condition.put("status",tcCarIn.getStatus());
            condition.put("createTime",tcCarIn.getCreateTime());
            TcCarInlist.add(condition);
        }
        map.clear();
        map.put("list",TcCarInlist);//返回前端集合命名为list
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
    public Integer removeBatchTcCarInByEdate() throws Exception {
        Integer num=0;
        SimpleDateFormat sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar =null;
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);//当前时间前去天数,
        String  endTime=sdfhms.format(calendar.getTime()); //当前时间
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //结束时间小于等于当前时间
        Criteria criteria = Criteria.where("createTime").lte(endTime);
        criteriasa.add(criteria);
        num =+ mongoComDAO.executeDelete(criteriasa,TcCarIn.class);
        return num;
    }



}
