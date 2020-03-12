package com.perenc.xh.lsp.service.tcExtendCoupon.impl;

import com.alibaba.fastjson.JSONObject;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.ConstantType;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.jshttp.JsHttpClientUtil;
import com.perenc.xh.commonUtils.utils.redis.RedisUtil;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.dao.tcSeller.TcSellerDao;
import com.perenc.xh.lsp.entity.tcCar.TcCar;
import com.perenc.xh.lsp.entity.tcCoupon.TcCoupon;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCar;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCoupon;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegralActivty;
import com.perenc.xh.lsp.entity.tcSeller.TcSeller;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplycoupon;
import com.perenc.xh.lsp.service.tcExtendCoupon.TcExtendCouponService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


@Service("tcExtendCouponService")
@Transactional(rollbackFor = Exception.class)
public class TcExtendCouponServiceImpl implements TcExtendCouponService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;

    @Autowired(required = false)
    private TcSellerDao tcSellerDao;


    @Override
    public ReturnJsonData insert(TcExtendCoupon tcExtendCoupon) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcExtendCoupon.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcExtendCoupon.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcExtendCoupon);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    /**
     * 添加多张停车券
     * @param tcExtendCoupon
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertBatch(TcExtendCoupon tcExtendCoupon,Map<String, Object> map) throws Exception {
        //日期转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfymd = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        String strNowTime = sdf.format(calendar.getTime());
        String sellerApplycouponId =  MapUtils.getString(map, "sellerApplycouponId");
        RedisUtil.setObject(tcExtendCoupon.getCarId(),sellerApplycouponId,720000);
        String number =  MapUtils.getString(map, "number");
        Integer intNumber=Integer.valueOf(number);
        int flag =0;
        Map<String,Object> condition = new HashMap<>();
        //查询商家申请停车券表id
        TcSellerApplycoupon tcSellerApplycoupon = mongoComDAO.executeForObjectById(sellerApplycouponId, TcSellerApplycoupon.class);
        if(tcSellerApplycoupon == null){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        //判断发放的张数是否大于可发的张数
        if(intNumber>tcSellerApplycoupon.getKnumber()) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"发放的票券不足",null);
        }
        TcCar tcCar = mongoComDAO.executeForObjectById(tcExtendCoupon.getCarId(), TcCar.class);
        if(tcCar == null){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID错误",null);
        }
        //查询客户车辆关系
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("carId",tcCar.getId());
        conditions.put("status", 1);
        //当车辆进入出去时根据车牌号查询
        TcExtendCar tcExtendCar = mongoComDAO.executeForObjectByCon(conditions, TcExtendCar.class);
        if(tcExtendCar==null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID错误",null);
        }
        TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcSellerApplycoupon.getCouponId(), TcCoupon.class);
        if(tcCoupon == null){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的票券ID错误",null);
        }
        tcExtendCoupon.setType(1); //来源类型 1=商家发放；2=活动发放;3:积分兑换
        tcExtendCoupon.setCouponId(tcSellerApplycoupon.getCouponId());
        tcExtendCoupon.setCouponDuration(tcCoupon.getDuration());
        tcExtendCoupon.setCouponAmount(tcCoupon.getAmount());
        tcExtendCoupon.setExtendId(tcExtendCar.getExtendId());
        tcExtendCoupon.setSellerId(tcSellerApplycoupon.getSellerId());
        tcExtendCoupon.setSellerApplycouponId(sellerApplycouponId);
        tcExtendCoupon.setObjectId("");
        tcExtendCoupon.setCarId(tcExtendCoupon.getCarId());
        tcExtendCoupon.setCarNum(tcCar.getCarNum());
        //有效期
        tcExtendCoupon.setSdate(strNowTime);
        String strEdate = sdfymd.format(calendar.getTime()) + " 23:59:59"; //票券有效期当天有效
        tcExtendCoupon.setEdate(strEdate);
        tcExtendCoupon.setRemark("商家发放");
        tcExtendCoupon.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcExtendCoupon.setStatus(1);
        flag = mongoComDAO.executeInsert(tcExtendCoupon);
        //计算可使用张数
        Integer knumber=tcSellerApplycoupon.getKnumber()-intNumber;
        tcSellerApplycoupon.setKnumber(knumber);
        //计算已经发的张数
        Integer ynumber=tcSellerApplycoupon.getYnumber()+intNumber;
        tcSellerApplycoupon.setYnumber(ynumber);
        int flagup = mongoComDAO.executeUpdate(tcSellerApplycoupon);
        if(flag > 0 && flagup>0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }


    /**
     * 添加会议，宴会停车券
     * @param tcExtendCoupon
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertMeeting(TcExtendCoupon tcExtendCoupon,Map<String, Object> map) throws Exception {
        //日期转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfymd = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        String strNowTime = sdf.format(calendar.getTime());
        String sellerApplycouponId =  MapUtils.getString(map, "sellerApplycouponId");
        RedisUtil.setObject(tcExtendCoupon.getCarId(),sellerApplycouponId,720000);
        String number =  MapUtils.getString(map, "number");
        Integer intNumber=Integer.valueOf(number);
        int flag =0;
        //查询商家申请停车券表id
        TcSellerApplycoupon tcSellerApplycoupon = mongoComDAO.executeForObjectById(sellerApplycouponId, TcSellerApplycoupon.class);
        if(tcSellerApplycoupon == null){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        Date nowTime=calendar.getTime();
        Date eDate=sdf.parse(tcSellerApplycoupon.getEdate());
        if(eDate.before(nowTime)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "有效期已过期", null);
        }
        //判断发放的张数是否大于可发的张数
        if(intNumber>tcSellerApplycoupon.getKnumber()) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"发放的票券不足",null);
        }
        TcCar tcCar = mongoComDAO.executeForObjectById(tcExtendCoupon.getCarId(), TcCar.class);
        if(tcCar == null){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID错误",null);
        }
        // 判断车辆是否在场内
        String reqJsonStr = "{\"plateNumber\":\""+tcCar.getCarNum()+"\"}";
        JSONObject jsonCharging= JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL+"park/charging",reqJsonStr);
        String code = jsonCharging.getString("code");
        if (!code.equals("0")) { // code为1时车辆未入场
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"该车辆未入场",null);
        }
        // 检查车辆是否已经发券
        Map<String, Object> conditionecoups = new HashMap<>();
        conditionecoups.put("sellerApplycouponId",sellerApplycouponId);
        conditionecoups.put("carId",tcExtendCoupon.getCarId());
        conditionecoups.put("status", 1);
        // 查询该车辆，每个宴会第个车只能发一张停车券
        TcExtendCoupon tcisExtendCoupon = mongoComDAO.executeForObjectByCon(conditionecoups, TcExtendCoupon.class);
        if(tcisExtendCoupon!=null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该车辆已发券", null);
        }
        //查询客户车辆关系
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("carId",tcCar.getId());
        conditions.put("status", 1);
        //当车辆进入出去时根据车牌号查询
        TcExtendCar tcExtendCar = mongoComDAO.executeForObjectByCon(conditions, TcExtendCar.class);
        if(tcExtendCar==null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID错误",null);
        }
        TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcSellerApplycoupon.getCouponId(), TcCoupon.class);
        if(tcCoupon == null){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的票券ID错误",null);
        }
        tcExtendCoupon.setType(4); //来源类型 1=商家发放；2=活动发放;3:积分兑换;4:会议、宴会
        tcExtendCoupon.setCouponId(tcSellerApplycoupon.getCouponId());
        tcExtendCoupon.setCouponDuration(tcCoupon.getDuration());
        tcExtendCoupon.setCouponAmount(tcCoupon.getAmount());
        tcExtendCoupon.setExtendId(tcExtendCar.getExtendId());
        tcExtendCoupon.setSellerId(tcSellerApplycoupon.getSellerId());
        tcExtendCoupon.setSellerUserId(tcSellerApplycoupon.getSellerUserId());
        tcExtendCoupon.setSellerApplycouponId(sellerApplycouponId);
        tcExtendCoupon.setObjectId("");
        tcExtendCoupon.setCarId(tcExtendCoupon.getCarId());
        tcExtendCoupon.setCarNum(tcCar.getCarNum());
        tcExtendCoupon.setTicketImg(tcSellerApplycoupon.getTicketImg());
        //有效期
        tcExtendCoupon.setSdate(strNowTime);
        String strEdate = sdfymd.format(calendar.getTime()) + " 23:59:59"; //票券有效期当天有效
        tcExtendCoupon.setEdate(strEdate);
        tcExtendCoupon.setUseStatus(1);
        tcExtendCoupon.setRemark("会议宴会发放");
        tcExtendCoupon.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcExtendCoupon.setStatus(1);
        flag = mongoComDAO.executeInsert(tcExtendCoupon);
        //计算可使用张数
        Integer knumber=tcSellerApplycoupon.getKnumber()-intNumber;
        tcSellerApplycoupon.setKnumber(knumber);
        //计算已经发的张数
        Integer ynumber=tcSellerApplycoupon.getYnumber()+intNumber;
        tcSellerApplycoupon.setYnumber(ynumber);
        int flagup = mongoComDAO.executeUpdate(tcSellerApplycoupon);
        if(flag > 0 && flagup>0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"发券成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"发券失败",null);
        }
    }

    /**
     * 用户第一次注册
     * 首次发绑定车辆
     * 给车辆发券2小时的停车券
     * @param tcExtendCoupon
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertFirst(TcExtendCoupon tcExtendCoupon,Map<String, Object> map) throws Exception {
        //日期转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfymd = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        String strNowTime = sdf.format(calendar.getTime());
        Map<String,Object> condition = new HashMap<>();
        TcCar tcCar = mongoComDAO.executeForObjectById(tcExtendCoupon.getCarId(), TcCar.class);
        if(tcCar == null){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID错误",null);
        }
        //首次绑定车辆，给客户发2小时停车券
        Map<String, Object> conditionsia = new HashMap<>();
        conditionsia.put("type", 1);
        conditionsia.put("isEnabled", 1); //启用状态 1:启用
        conditionsia.put("status", 1);
        //查询认证车辆获得的积分
        TcIntegralActivty integralActivty = mongoComDAO.executeForObjectByCon(conditionsia, TcIntegralActivty.class);
        if(integralActivty==null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"该活动已经结束",null);
        }
        Map<String, Object> conditionsel = new HashMap<>();
        conditionsel.put("extendId",tcExtendCoupon.getExtendId());
        conditionsel.put("objectId", integralActivty.getId());
        //查询是否已经发过券
        TcExtendCoupon rettCoupon = mongoComDAO.executeForObjectByCon(conditionsel, TcExtendCoupon.class);
        if(rettCoupon!=null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"已经首次绑定",null);
        }
        Map<String, Object> conditioncarsel = new HashMap<>();
        conditioncarsel.put("carId",tcExtendCoupon.getCarId());
        conditioncarsel.put("objectId", integralActivty.getId());
        //查询是否已经发过券
        TcExtendCoupon rettCouponcar = mongoComDAO.executeForObjectByCon(conditioncarsel, TcExtendCoupon.class);
        if(rettCouponcar!=null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"该车辆已经领取首次绑定停车券",null);
        }
        TcCoupon tcCoupon = mongoComDAO.executeForObjectById(integralActivty.getCouponId(), TcCoupon.class);
        if(tcCoupon == null){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的票券ID错误",null);
        }
        //发放停车券
        tcExtendCoupon.setType(2); //来源类型 1=商家发放；2=活动发放;3:积分兑换
        tcExtendCoupon.setCouponId(integralActivty.getCouponId());
        tcExtendCoupon.setCouponDuration(tcCoupon.getDuration());
        tcExtendCoupon.setCouponAmount(tcCoupon.getAmount());
        tcExtendCoupon.setExtendId(tcExtendCoupon.getExtendId());
        tcExtendCoupon.setCarId(tcExtendCoupon.getCarId());
        tcExtendCoupon.setCarNum(tcCar.getCarNum());
        tcExtendCoupon.setSellerId(0);
        tcExtendCoupon.setSellerApplycouponId("");
        tcExtendCoupon.setObjectId(integralActivty.getId());
        //有效期
        tcExtendCoupon.setSdate(strNowTime);
        String strEdate = sdfymd.format(calendar.getTime()) + " 23:59:59"; //票券有效期当天有效
        tcExtendCoupon.setEdate(strEdate);
        tcExtendCoupon.setUseStatus(ConstantType.USE_STATUS_WAITFOR);
        tcExtendCoupon.setRemark("首次绑定车辆");
        tcExtendCoupon.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcExtendCoupon.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcExtendCoupon);
        if (flag > 0) {
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加成功", null);
        } else {
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE, "添加失败", null);
        }
    }

    @Override
    public ReturnJsonData update(TcExtendCoupon tcExtendCoupon) throws Exception {
        TcExtendCoupon returnTcExtendCoupon = mongoComDAO.executeForObjectById(tcExtendCoupon.getId(), TcExtendCoupon.class);
        if(returnTcExtendCoupon != null){
            returnTcExtendCoupon.setCouponId(tcExtendCoupon.getCouponId());
            returnTcExtendCoupon.setExtendId(tcExtendCoupon.getExtendId());
            returnTcExtendCoupon.setSellerId(tcExtendCoupon.getSellerId());
            returnTcExtendCoupon.setSdate(tcExtendCoupon.getSdate());
            returnTcExtendCoupon.setEdate(tcExtendCoupon.getEdate());
            //returnTcExtendCoupon.setUseStatus(tcExtendCoupon.getUseStatus());
            int flag = mongoComDAO.executeUpdate(returnTcExtendCoupon);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcExtendCoupon.class);
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
        TcExtendCoupon tcExtendCoupon = mongoComDAO.executeForObjectById(id, TcExtendCoupon.class);
        if (tcExtendCoupon!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcExtendCoupon.getId());
            condition.put("type",tcExtendCoupon.getType());
            condition.put("couponId",tcExtendCoupon.getCouponId());
            TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcExtendCoupon.getCouponId(), TcCoupon.class);
            if(tcCoupon != null){
                condition.put("couponName",tcCoupon.getName());
                condition.put("couponDesc",tcCoupon.getDesc());
                condition.put("couponDuration",tcCoupon.getDuration());
                condition.put("couponAmount",tcCoupon.getAmount());
            }else {
                condition.put("couponName","");
                condition.put("couponDesc","");
                condition.put("couponDuration","");
                condition.put("couponAmount","");
            }
            condition.put("carId",tcExtendCoupon.getCarId());
            condition.put("carNum",tcExtendCoupon.getCarNum());
            condition.put("sellerId",tcExtendCoupon.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcExtendCoupon.getSellerId());
            if (tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("sellerUserId",tcExtendCoupon.getSellerUserId());
            condition.put("ticketImg",tcExtendCoupon.getTicketImg());
            condition.put("sdate",tcExtendCoupon.getSdate());
            condition.put("edate",tcExtendCoupon.getEdate());
            condition.put("useStatus",tcExtendCoupon.getUseStatus());
            condition.put("status",tcExtendCoupon.getStatus());
            condition.put("createTime",tcExtendCoupon.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }

    /**
     * 根据extendId查询
     * 首次发放停车券判断
     * @param extendId
     * @return
     */
    @Override
    public ReturnJsonData getIsFirst(String extendId) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        Integer isFirst=2; //否
        //首次绑定车辆，给客户发2小时停车券
        Map<String, Object> conditionsia = new HashMap<>();
        conditionsia.put("type", 1);
        conditionsia.put("isEnabled", 1); //启用状态 1:启用
        conditionsia.put("status", 1);
        //查询认证车辆获得的积分
        TcIntegralActivty integralActivty = mongoComDAO.executeForObjectByCon(conditionsia, TcIntegralActivty.class);
        if(integralActivty!=null) {
            Map<String, Object> conditionsel = new HashMap<>();
            conditionsel.put("extendId", Integer.valueOf(extendId));
            conditionsel.put("objectId", integralActivty.getId());
            //查询是否已经发过券
            TcExtendCoupon rettCoupon = mongoComDAO.executeForObjectByCon(conditionsel, TcExtendCoupon.class);
            if (rettCoupon!= null) {
                isFirst=1;
            }
        }
        condition.put("isFirst", isFirst);
        return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
    }

    /**
     * 轮询查询发券
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getPolling(Map<String, Object> map) throws Exception {
        //日期转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        String carId =  MapUtils.getString(map, "carId");
        String scode =  MapUtils.getString(map, "scode");
        // 获取申请普通或者宴会id
        String sellerApplycouponId="";
        if(RedisUtil.existsObject(carId)) {
            sellerApplycouponId=RedisUtil.getObject(carId,String.class);
        }
        if(StringUtils.isEmpty(sellerApplycouponId)){
            return new ReturnJsonData(DataCodeUtil.POLLING_WAITING,"发券等待",null);
        }
        //查询商家申请停车券表id
        TcSellerApplycoupon tcSellerApplycoupon = mongoComDAO.executeForObjectById(sellerApplycouponId, TcSellerApplycoupon.class);
        if(tcSellerApplycoupon == null){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的申请券ID错误",null);
        }
        Integer type=tcSellerApplycoupon.getType();
        //  申请类型 1=商家发放；2=婚宴、会议发放
        if(type.equals(1)) {
            // 检查是否已发券
            Map<String, Object> conditioniscoups = new HashMap<>();
            conditioniscoups.put("scode",scode);
            conditioniscoups.put("status", 1);
            //查询该车辆是否发券成功
            TcExtendCoupon tcExtendCoupon = mongoComDAO.executeForObjectByCon(conditioniscoups, TcExtendCoupon.class);
            if(tcExtendCoupon==null) {
                return new ReturnJsonData(DataCodeUtil.POLLING_WAITING,"发券等待",null);
            }else {
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"发券成功",null);
            }
        } else if(type.equals(2)) {
            RedisUtil.deleteObject(carId);
            TcCar tcCar = mongoComDAO.executeForObjectById(carId, TcCar.class);
            if (tcCar == null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "传入的车辆ID错误", null);
            }
            // 判断车辆是否在场内
            String reqJsonStr = "{\"plateNumber\":\"" + tcCar.getCarNum() + "\"}";
            JSONObject jsonCharging = JsHttpClientUtil.httpPost(JsHttpClientUtil.JS_CLIENT_URL + "park/charging", reqJsonStr);
            String code = jsonCharging.getString("code");
            if (!code.equals("0")) { // code为1时车辆未入场
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "该车辆未入场", null);
            }
            if(StringUtils.isNotEmpty(sellerApplycouponId)) {
                Map<String, Object> conditionecoupsc = new HashMap<>();
                conditionecoupsc.put("sellerApplycouponId", sellerApplycouponId);
                conditionecoupsc.put("carId", carId);
                conditionecoupsc.put("status", 1);
                // 查询该车辆，每个宴会第个车只能发一张停车券
                TcExtendCoupon tcExtendCouponsc = mongoComDAO.executeForObjectByCon(conditionecoupsc, TcExtendCoupon.class);
                if (tcExtendCouponsc != null) {
                    if(!tcExtendCouponsc.getScode().equals(scode)) {
                        return new ReturnJsonData(DataCodeUtil.SUCCESS, "该车辆已发券", null);
                    }
                }
            }
            // 检查车辆是否已经发券
            Map<String, Object> conditioniscoups = new HashMap<>();
            conditioniscoups.put("scode",scode);
            conditioniscoups.put("status", 1);
            //查询该车辆是否发券成功
            TcExtendCoupon tcExtendCoupon = mongoComDAO.executeForObjectByCon(conditioniscoups, TcExtendCoupon.class);
            if(tcExtendCoupon==null) {
                return new ReturnJsonData(DataCodeUtil.POLLING_WAITING,"发券等待",null);
            }
            if(tcExtendCoupon!=null) {
                RedisUtil.deleteObject(carId);
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "发券成功", null);
            }
            Date nowTime = calendar.getTime();
            Date eDate = sdf.parse(tcSellerApplycoupon.getEdate());
            if (eDate.before(nowTime)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "有效期已过期", null);
            }
            //判断发放的张数是否大于可发的张数
            if (tcSellerApplycoupon.getKnumber() < 1) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "发放的票券不足", null);
            }

        }
        return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE,"发券失败",null);

    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String sellerName =  MapUtils.getString(map, "sellerName");
        String extendId =  MapUtils.getString(map, "extendId");
        String carId =  MapUtils.getString(map, "carId");
        String sellerId =  MapUtils.getString(map, "sellerId");
        String carNum =  MapUtils.getString(map, "carNum");
        String type =  MapUtils.getString(map, "type");
        String useStatus =  MapUtils.getString(map, "useStatus");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //商家名称
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
        //用户ID
        if(StringUtils.isNotEmpty(extendId) && StringUtils.isEmpty(carId)){
            //查询
            List<Criteria> criteriascar = new ArrayList<Criteria>();
            Criteria criterica = Criteria.where("extendId").is(Integer.valueOf(extendId));
            criteriascar.add(criterica);
            Criteria critericb = Criteria.where("status").is(1);
            criteriascar.add(critericb);
            List<TcExtendCar> tcExtendCars = mongoComDAO.executeForObjectList(criteriascar,TcExtendCar.class);
            List<String> listCarIds=new ArrayList<>();
            for(TcExtendCar tcExtendCar : tcExtendCars){
                listCarIds.add(tcExtendCar.getCarId());
            }
            Criteria criteria = Criteria.where("carId").in(listCarIds);
            criteriasa.add(criteria);
        }
        //车辆ID
        if(StringUtils.isNotEmpty(carId)){
            Criteria criteria = Criteria.where("carId").is(carId);
            criteriasa.add(criteria);
        }
        //商家ID
        if(StringUtils.isNotEmpty(sellerId)){
            Criteria criteria = Criteria.where("sellerId").is(Integer.valueOf(sellerId));
            criteriasa.add(criteria);
        }
        //车牌号模糊
        if(StringUtils.isNotEmpty(carNum)){
            Pattern pattern = Pattern.compile("^.*" + carNum + ".*$");
            Criteria criteria = Criteria.where("carNum").regex(pattern);
            criteriasa.add(criteria);
        }
        //使用类型
        if(StringUtils.isNotEmpty(type)){
            Criteria criteria = Criteria.where("type").is(Integer.valueOf(type));
            criteriasa.add(criteria);
        }
        //使用状态
        if(StringUtils.isNotEmpty(useStatus)){
            Criteria criteria = Criteria.where("useStatus").is(Integer.valueOf(useStatus));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcExtendCoupon> tcExtendCoupons = mongoComDAO.executeForObjectList(criteriasa,TcExtendCoupon.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcExtendCoupon.class);
        List<Map<String,Object>> tcExtendCouponlist = new ArrayList<>();
        for(TcExtendCoupon tcExtendCoupon : tcExtendCoupons){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcExtendCoupon.getId());
            condition.put("type",tcExtendCoupon.getType());
            condition.put("couponId",tcExtendCoupon.getCouponId());
            TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcExtendCoupon.getCouponId(), TcCoupon.class);
            if(tcCoupon != null){
                condition.put("couponName",tcCoupon.getName());
                condition.put("couponDesc",tcCoupon.getDesc());
                condition.put("couponDuration",tcCoupon.getDuration());
                condition.put("couponAmount",tcCoupon.getAmount());
            }else {
                condition.put("couponName","");
                condition.put("couponDesc","");
                condition.put("couponDuration","");
                condition.put("couponAmount","");
            }
            condition.put("carId",tcExtendCoupon.getCarId());
            condition.put("carNum",tcExtendCoupon.getCarNum());
            condition.put("sellerId",tcExtendCoupon.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcExtendCoupon.getSellerId());
            if (tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("sellerUserId",tcExtendCoupon.getSellerUserId());
            condition.put("ticketImg",tcExtendCoupon.getTicketImg());
            condition.put("sdate",tcExtendCoupon.getSdate());
            condition.put("edate",tcExtendCoupon.getEdate());
            condition.put("useStatus",tcExtendCoupon.getUseStatus());
            condition.put("status",tcExtendCoupon.getStatus());
            condition.put("createTime",tcExtendCoupon.getCreateTime());
            tcExtendCouponlist.add(condition);
        }
        map.clear();
        map.put("list",tcExtendCouponlist);//返回前端集合命名为list
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
    public Integer updateBatchCouponUseStatusByEdate() throws Exception {
        Integer num=0;
        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat  sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar =null;
        calendar = Calendar.getInstance();
        String  strnowDate=sdfhms.format(calendar.getTime()); //当前时间
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //结束时间小于等于当前时间
        Criteria criteria = Criteria.where("edate").lte(strnowDate);
        criteriasa.add(criteria);
        //票券状态为待使用
        Criteria criterib = Criteria.where("useStatus").is(1);
        criteriasa.add(criterib);
        List<TcExtendCoupon> tcExtendCoupons = mongoComDAO.executeForObjectList(criteriasa,TcExtendCoupon.class);
        for(TcExtendCoupon tcExtendCoupon : tcExtendCoupons){
            TcExtendCoupon returnTcExtendCoupon = mongoComDAO.executeForObjectById(tcExtendCoupon.getId(), TcExtendCoupon.class);
            if(returnTcExtendCoupon != null) {
                returnTcExtendCoupon.setUseStatus(ConstantType.USE_STATUS_EXPIRED);
                num =+ mongoComDAO.executeUpdate(returnTcExtendCoupon);
            }
        }
        return num;
    }




}
