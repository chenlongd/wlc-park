package com.perenc.xh.lsp.service.tcSellerFreecar.impl;

import com.alibaba.fastjson.JSONObject;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.ConstantType;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.jshttp.JsHttpClientUtil;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.dao.tcSeller.TcSellerDao;
import com.perenc.xh.lsp.entity.tcCar.TcCar;
import com.perenc.xh.lsp.entity.tcCar.TcCarJsclientlog;
import com.perenc.xh.lsp.entity.tcSeller.TcSeller;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerFreecar;
import com.perenc.xh.lsp.service.tcSellerFreecar.TcSellerFreecarService;
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


@Service("tcSellerFreecarService")
@Transactional(rollbackFor = Exception.class)
public class TcSellerFreecarServiceImpl implements TcSellerFreecarService {

    private static final Logger logger = Logger.getLogger(TcSellerFreecarServiceImpl.class);

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private TcSellerDao tcSellerDao;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public ReturnJsonData insert(TcSellerFreecar tcSellerFreecar) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcSellerFreecar.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcSellerFreecar.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcSellerFreecar);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    /**
     * 自营商家指定免费车辆
     * @param tcSellerFreecar
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertFreecar(TcSellerFreecar tcSellerFreecar,Map<String, Object> map) throws Exception {
        String carNum =  MapUtils.getString(map, "carNum");
        try {
            //免费车的时间判断
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = null;
            calendar = Calendar.getInstance();
            Date nowTime=calendar.getTime();
            Date eDate=sdf.parse(tcSellerFreecar.getEdate());
            if(eDate.before(nowTime)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "结束时间不能小于当前时间", null);
            }
            //停车时长（小时）：当前时间-进厂时间
            double duration = ToolUtil.getDateHourNum(tcSellerFreecar.getSdate(),tcSellerFreecar.getEdate());
            if(duration<4) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "免费时间至少大于4小时", null);
            }
            // 判断车辆是否存在
            String carId="";
            // 判断车辆是否入场
            Integer isEntry=2;
            //调用捷顺接口查询场内记录
            String reqJsonStr = "{\"plateNumber\":\""+carNum+"\"}";
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
                        tjsclientlog.setCarNum(carNum);
                        tjsclientlog.setRemark("添加设置免费车车辆查询");
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
                isEntry=1;
            }
            Map<String,Object> conditionCar = new HashMap<>();
            conditionCar.put("carNum",carNum);
            conditionCar.put("status",1);
            TcCar returnTcCar = mongoComDAO.executeForObjectByCon(conditionCar, TcCar.class);
            if(returnTcCar==null) {
                TcCar tcCar =new TcCar();
                tcCar.setCarNum(carNum);
                tcCar.setType(ConstantType.CAR_TYPE_SMALL);
                //查询会员类型
                tcCar.setVipType(ConstantType.CAR_VIP_TYPE_FREE);
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
                tcCar.setCostSdate("");
                tcCar.setCostEdate("");
                tcCar.setIntimg("");
                tcCar.setOutimg("");
                tcCar.setVipSdate(tcSellerFreecar.getSdate());
                tcCar.setVipEdate(tcSellerFreecar.getEdate());
                tcCar.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                tcCar.setStatus(1);
                int flagadd = mongoComDAO.executeInsert(tcCar);
                if(flagadd>0) {
                    carId=tcCar.getId();
                    // 添加到免费车记录表
                    TcSellerFreecar tcSellerFreecaradd= new TcSellerFreecar();
                    tcSellerFreecaradd.setSellerId(tcSellerFreecar.getSellerId());
                    tcSellerFreecaradd.setSellerUserId(tcSellerFreecar.getSellerUserId());
                    tcSellerFreecaradd.setSdate(tcSellerFreecar.getSdate());
                    tcSellerFreecaradd.setEdate(tcSellerFreecar.getEdate());
                    tcSellerFreecaradd.setTicketImg(tcSellerFreecar.getTicketImg());
                    tcSellerFreecaradd.setCarId(carId);
                    tcSellerFreecaradd.setCarNum(carNum);
                    tcSellerFreecaradd.setUseStatus(ConstantType.USE_STATUS_USED);//已使用
                    tcSellerFreecaradd.setIsApproval(2);//审核通过
                    tcSellerFreecaradd.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    tcSellerFreecaradd.setStatus(1);
                    mongoComDAO.executeInsert(tcSellerFreecaradd);
                    return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
                }else {
                    return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
                }
            } else {
                // 车辆信息存在
                carId = returnTcCar.getId();
                if (returnTcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_CARD)) {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该车辆是月卡车无法设置", null);
                }
                //判断是否已经是免费车
                if (returnTcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_FREE)) {
                    String strVipEdate = returnTcCar.getVipEdate();
                    if (StringUtils.isNotEmpty(strVipEdate)) {
                        Date vipEdate = sdf.parse(returnTcCar.getVipEdate());
                        if (nowTime.before(vipEdate)) {
                            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该车辆已是免费车", null);
                        }
                    }
                }
                returnTcCar.setVipType(ConstantType.CAR_VIP_TYPE_FREE);
                returnTcCar.setVipSdate(tcSellerFreecar.getSdate());
                returnTcCar.setVipEdate(tcSellerFreecar.getEdate());
                returnTcCar.setIsEntry(isEntry);
                int flagupd = mongoComDAO.executeUpdate(returnTcCar);
                if (flagupd > 0) {
                    // 添加到免费车记录表
                    TcSellerFreecar tcSellerFreecaradd = new TcSellerFreecar();
                    tcSellerFreecaradd.setSellerId(tcSellerFreecar.getSellerId());
                    tcSellerFreecaradd.setSellerUserId(tcSellerFreecar.getSellerUserId());
                    tcSellerFreecaradd.setSdate(tcSellerFreecar.getSdate());
                    tcSellerFreecaradd.setEdate(tcSellerFreecar.getEdate());
                    tcSellerFreecaradd.setTicketImg(tcSellerFreecar.getTicketImg());
                    tcSellerFreecaradd.setCarId(carId);
                    tcSellerFreecaradd.setCarNum(carNum);
                    tcSellerFreecaradd.setUseStatus(ConstantType.USE_STATUS_USED);//已使用
                    tcSellerFreecaradd.setIsApproval(2);//审核通过
                    tcSellerFreecaradd.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    tcSellerFreecaradd.setStatus(1);
                    mongoComDAO.executeInsert(tcSellerFreecaradd);
                    return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加成功", null);
                } else {
                    return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE, "添加失败", null);
                }
            }
        }catch (Exception e) {
            logger.info("执行错误"+e.getMessage());
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
    }

    @Override
    public ReturnJsonData update(TcSellerFreecar tcSellerFreecar) throws Exception {
        TcSellerFreecar returnTcSellerFreecar = mongoComDAO.executeForObjectById(tcSellerFreecar.getId(), TcSellerFreecar.class);
        if(returnTcSellerFreecar != null){
            returnTcSellerFreecar.setSellerId(tcSellerFreecar.getSellerId());
            returnTcSellerFreecar.setCarId(tcSellerFreecar.getCarId());
            returnTcSellerFreecar.setSdate(tcSellerFreecar.getSdate());
            returnTcSellerFreecar.setEdate(tcSellerFreecar.getEdate());
            int flag = mongoComDAO.executeUpdate(returnTcSellerFreecar);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 修改免费车
     * 开始时间-结束时间
     * @param tcSellerFreecar
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateTime(TcSellerFreecar tcSellerFreecar) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Calendar calendar = null;
            calendar = Calendar.getInstance();
            Date nowTime = calendar.getTime();
            Date eDate = sdf.parse(tcSellerFreecar.getEdate());
            //免费车的时间判断
            if (eDate.before(nowTime)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "结束时间不能小于当前时间", null);
            }
            //停车时长（小时）：当前时间-开始时间
            double duration = ToolUtil.getDateHourNum(tcSellerFreecar.getSdate(), tcSellerFreecar.getEdate());
            if (duration < 4) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "免费时间至少大于4小时", null);
            }
            TcSellerFreecar returnTcSellerFreecar = mongoComDAO.executeForObjectById(tcSellerFreecar.getId(), TcSellerFreecar.class);
            if(returnTcSellerFreecar==null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "传入的ID错误", null);
            }
            TcCar returnTcCar = mongoComDAO.executeForObjectById(returnTcSellerFreecar.getCarId(), TcCar.class);
            if(returnTcCar==null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "传入的ID错误", null);
            }
            returnTcCar.setVipType(ConstantType.CAR_VIP_TYPE_FREE);
            returnTcCar.setVipSdate(tcSellerFreecar.getSdate());
            returnTcCar.setVipEdate(tcSellerFreecar.getEdate());
            int flagupc= mongoComDAO.executeUpdate(returnTcCar);
            if(flagupc>0) {
                returnTcSellerFreecar.setSellerUserId(tcSellerFreecar.getSellerUserId());
                returnTcSellerFreecar.setSdate(tcSellerFreecar.getSdate());
                returnTcSellerFreecar.setEdate(tcSellerFreecar.getEdate());
                returnTcSellerFreecar.setUseStatus(ConstantType.USE_STATUS_USED);//已使用
                int flagupfree=mongoComDAO.executeUpdate(returnTcSellerFreecar);
                if (flagupfree>0) {
                    return new ReturnJsonData(DataCodeUtil.SUCCESS, "修改成功", null);
                } else {
                    return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
                }
            }else {
                return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
            }
        }catch (Exception e) {
            logger.info("执行错误"+e.getMessage());
            System.out.println("执行错误"+e.getMessage());
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }



    @Override
    public ReturnJsonData delete(String[] ids) throws Exception {
        Map<String,Object> condition=new HashMap<>();
        condition.put("ids",ids);
        condition.put("status",2);
        int flag= mongoComDAO.executeUpdateByIds(condition, TcSellerFreecar.class);
        if (flag > 0) {
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "删除成功", null);
        } else {
            return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE, "删除失败", null);
        }
    }

    /**
     * 删除免费车
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData deleteFreecar(String id) throws Exception {
        TcSellerFreecar tcSellerFreecar = mongoComDAO.executeForObjectById(id, TcSellerFreecar.class);
        if (tcSellerFreecar!=null) {
            tcSellerFreecar.setStatus(2);
            int flag = mongoComDAO.executeUpdate(tcSellerFreecar);
            if (flag > 0) {
                TcCar tcCar = mongoComDAO.executeForObjectById(tcSellerFreecar.getCarId(), TcCar.class);
                if(tcCar != null && tcCar.getVipType().equals(ConstantType.CAR_VIP_TYPE_FREE)){
                    //查询会员类型
                    tcCar.setVipType(ConstantType.CAR_VIP_TYPE_TEMP);
                    tcCar.setVipSdate("");
                    tcCar.setVipEdate("");
                    mongoComDAO.executeUpdateEmpty(tcCar);
                }
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "删除成功", null);
            } else {
                return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE, "删除失败", null);
            }
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "删除失败", null);
        }
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public ReturnJsonData getById(String id) throws Exception {
        TcSellerFreecar tcSellerFreecar = mongoComDAO.executeForObjectById(id, TcSellerFreecar.class);
        if (tcSellerFreecar!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcSellerFreecar.getId());
            condition.put("sellerId",tcSellerFreecar.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcSellerFreecar.getSellerId());
            if(tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("sellerUserId",tcSellerFreecar.getSellerUserId());
            condition.put("carId",tcSellerFreecar.getCarId());
            TcCar tcCar = mongoComDAO.executeForObjectById(tcSellerFreecar.getCarId(), TcCar.class);
            if(tcCar!=null) {
                condition.put("carNum",tcCar.getCarNum());
            }else {
                condition.put("carNum","");
            }
            condition.put("sdate",tcSellerFreecar.getSdate());
            condition.put("edate",tcSellerFreecar.getEdate());
            condition.put("ticketImg",tcSellerFreecar.getTicketImg());
            condition.put("status",tcSellerFreecar.getStatus());
            condition.put("createTime",tcSellerFreecar.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String sellerId =  MapUtils.getString(map, "sellerId");
        String sellerName =  MapUtils.getString(map, "sellerName");
        String carNum =  MapUtils.getString(map, "carNum");
        String useStatus =  MapUtils.getString(map, "useStatus");
        //String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //商家名称模糊
        if(StringUtils.isNotEmpty(sellerName)){
            //筛选
            QueryParam param = new QueryParam();
            param.addCondition("name","like","%"+sellerName+"%");
            param.addCondition("status","=",1);
            List<TcSeller> tcSellers = tcSellerDao.list(param);
            //查询
            List<Integer> listSellerIds=new ArrayList<>();
            for(TcSeller tcSeller : tcSellers){
                listSellerIds.add(tcSeller.getId());
            }
            Criteria criteria = Criteria.where("sellerId").in(listSellerIds);
            criteriasa.add(criteria);
        }
        //车牌号模糊
        if(StringUtils.isNotEmpty(carNum)){
                Pattern pattern = Pattern.compile("^.*" + carNum + ".*$");
                Criteria criteria = Criteria.where("carNum").regex(pattern);
                criteriasa.add(criteria);
        }
        //跳转类型
        if(StringUtils.isNotEmpty(sellerId)){
            Criteria criteria = Criteria.where("sellerId").is(Integer.valueOf(sellerId));
            criteriasa.add(criteria);
        }
        //是否过期
        if(StringUtils.isNotEmpty(useStatus)){
            Criteria criteria = Criteria.where("useStatus").is(Integer.valueOf(useStatus));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcSellerFreecar> tcSellerFreecars = mongoComDAO.executeForObjectList(criteriasa,TcSellerFreecar.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcSellerFreecar.class);
        List<Map<String,Object>> tcSellerFreecarlist = new ArrayList<>();
        for(TcSellerFreecar tcSellerFreecar : tcSellerFreecars){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcSellerFreecar.getId());
            condition.put("sellerId",tcSellerFreecar.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcSellerFreecar.getSellerId());
            if(tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("sellerUserId",tcSellerFreecar.getSellerUserId());
            condition.put("carId",tcSellerFreecar.getCarId());
            TcCar tcCar = mongoComDAO.executeForObjectById(tcSellerFreecar.getCarId(), TcCar.class);
            if(tcCar!=null) {
                condition.put("carNum",tcCar.getCarNum());
                condition.put("isEntry",tcCar.getIsEntry());
            }else {
                condition.put("carNum","");
                condition.put("isEntry",2);
            }
            condition.put("sdate",tcSellerFreecar.getSdate());
            condition.put("edate",tcSellerFreecar.getEdate());
            condition.put("ticketImg",tcSellerFreecar.getTicketImg());
            condition.put("useStatus",tcSellerFreecar.getUseStatus());
            condition.put("status",tcSellerFreecar.getStatus());
            condition.put("createTime",tcSellerFreecar.getCreateTime());
            tcSellerFreecarlist.add(condition);
        }
        map.clear();
        map.put("list",tcSellerFreecarlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }



    /**
     * 根据当前时间
     * 票券状态已过期处理
     * 根据当前时间，把票券状态更改
     * @param
     * @return
     */
    @Override
    public Integer updateBatchFreecarUseStatusByEdate() throws Exception {
        Integer num=0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat  sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar =null;
        calendar = Calendar.getInstance();
        String  strnowDate=sdfhms.format(calendar.getTime()); //当前时间
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //结束时间小于等于当前时间
        Criteria criteria = Criteria.where("edate").lte(strnowDate);
        criteriasa.add(criteria);
        //票券状态为待使用
        Criteria criterib = Criteria.where("useStatus").is(ConstantType.USE_STATUS_USED);
        criteriasa.add(criterib);
        List<TcSellerFreecar> TcSellerFreecars = mongoComDAO.executeForObjectList(criteriasa,TcSellerFreecar.class);
        for(TcSellerFreecar tcSellerFreecar : TcSellerFreecars){
            TcSellerFreecar returnTcSellerFreecar = mongoComDAO.executeForObjectById(tcSellerFreecar.getId(), TcSellerFreecar.class);
            if(returnTcSellerFreecar != null) {
                returnTcSellerFreecar.setUseStatus(ConstantType.USE_STATUS_EXPIRED);
                num =+ mongoComDAO.executeUpdate(returnTcSellerFreecar);
            }
        }
        return num;
    }



}
