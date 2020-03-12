package com.perenc.xh.lsp.controller.phone.tcCar;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.JsReturnJsonData;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.jsdata.*;
import com.perenc.xh.lsp.entity.tcCar.TcCarIn;
import com.perenc.xh.lsp.entity.tcCar.TcCarInout;
import com.perenc.xh.lsp.entity.tcCar.TcCarInpass;
import com.perenc.xh.lsp.entity.tcCar.TcCarOutpass;
import com.perenc.xh.lsp.service.order.SysOrderService;
import com.perenc.xh.lsp.service.tcCarIn.TcCarInService;
import com.perenc.xh.lsp.service.tcCarInout.TcCarInoutService;
import com.perenc.xh.lsp.service.tcCarInpass.TcCarInpassService;
import com.perenc.xh.lsp.service.tcCarOutpass.TcCarOutpassService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/19 15:28
 **/
@Controller
@RequestMapping("api/carInout")
public class TcCarInoutController {

    private static final Logger logger = Logger.getLogger(TcCarInoutController.class);

    @Autowired(required = false)
    private TcCarInService tcCarInService;

    @Autowired(required = false)
    private TcCarInoutService tcCarInoutService;

    @Autowired(required = false)
    private TcCarInpassService tcCarInpassService;

    @Autowired(required = false)
    private TcCarOutpassService tcCarOutpassService;

    @Autowired(required = false)
    private SysOrderService sysOrderService;


    /**
     * 捷顺
     * 第三方接口y
     * 接收车辆入场识别记录
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("inrecognition")
    @ResponseBody
    public JsReturnJsonData inrecognition(@RequestBody TcCarInData tcCarInData, HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        logger.info("入场识别记录："+ JSONObject.fromObject(tcCarInData));
        //车场ID
        String parkId = tcCarInData.getParkId();
        //入场设备标识
        String inDeviceId =tcCarInData.getInDeviceId();
        //入场设备名称
        String inDeviceName =tcCarInData.getInDeviceName();
        //入场识别时间
        String inRecognitionTime =tcCarInData.getRecognitionTime();
        //入场车辆图片
        String inImage = tcCarInData.getInImage();
        //车牌号
        String carNum =tcCarInData.getPlateNumber();
        //车辆颜色
        String carColor = tcCarInData.getPlateColor();
        Map<String, Object> condition = new HashMap<>();
        TcCarIn tcCarIn= new TcCarIn();
        tcCarIn.setParkId(parkId);
        tcCarIn.setInDeviceId(inDeviceId);
        tcCarIn.setInDeviceName(inDeviceName);
        tcCarIn.setInRecognitionTime(inRecognitionTime);
        tcCarIn.setIntimg(inImage);
        tcCarIn.setCarNum(carNum);
        tcCarIn.setCarColor(carColor);
        return tcCarInService.insertInrecognition(tcCarIn,condition);
    }


    /**
     * 捷顺
     * 第三方接口outrecognition
     * 接收车辆出场识别记录
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("outrecognition")
    @ResponseBody
    public JsReturnJsonData outrecognition(@RequestBody TcCarOutdata tcCarOutdata, HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        logger.info("出场识别记录："+JSONObject.fromObject(tcCarOutdata));
        //车场ID
        String parkId = tcCarOutdata.getParkId();
        //出场设备标识
        String outDeviceId =tcCarOutdata.getOutDeviceId();
        //出场设备名称
        String outDeviceName = tcCarOutdata.getOutDeviceName();
        //出场识别时间
        String outRecognitionTime =tcCarOutdata.getRecognitionTime();
        //入场记录唯一标识
        String inRecordId =tcCarOutdata.getInRecordId();
        //入场设备标识
        String inDeviceId = tcCarOutdata.getInDeviceId();
        //入场设备名称
        String inDeviceName =tcCarOutdata.getInDeviceName();
        //入场车辆图片
        String inImage = tcCarOutdata.getInImage();
        //出场车辆图片
        String outImage = tcCarOutdata.getOutImage();
        //车牌号
        String carNum = tcCarOutdata.getPlateNumber();
        //车辆颜色
        String carColor =tcCarOutdata.getPlateColor();

        Map<String, Object> condition = new HashMap<>();
        TcCarInout tcCarInout= new TcCarInout();
        tcCarInout.setParkId(parkId);
        tcCarInout.setOutDeviceId(outDeviceId);
        tcCarInout.setOutDeviceName(outDeviceName);
        tcCarInout.setOutRecognitionTime(outRecognitionTime);
        tcCarInout.setInRecordId(inRecordId);
        tcCarInout.setInDeviceId(inDeviceId);
        tcCarInout.setInDeviceName(inDeviceName);
        tcCarInout.setIntimg(inImage);
        tcCarInout.setOutimg(outImage);
        tcCarInout.setCarNum(carNum);
        tcCarInout.setCarColor(carColor);
        return tcCarInoutService.insertOutrecognition(tcCarInout,condition);
    }

    /**
     * 捷顺
     * 第三方接口
     * 接收车辆入场过闸记录
     * @param request
     * @return
     * @throws Exception
     */
   @RequestMapping("carin")
    @ResponseBody
    public JsReturnJsonData carin(@RequestBody TcCarInpassData tcCarInpassData, HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
       logger.info("入场过闸记录："+JSONObject.fromObject(tcCarInpassData));
        //入场唯一标识
        String inRecordId = tcCarInpassData.getInRecordId();
        //车场ID
        String parkId =tcCarInpassData.getParkId();
        //入场设备标识
        String inDeviceId =tcCarInpassData.getInDeviceId();
        //入场设备名称
        String inDeviceName = tcCarInpassData.getInDeviceName();
        //入场时间
        String inTime = tcCarInpassData.getInTime();
        //入场车辆图片
        String inImage = tcCarInpassData.getInImage();
        //车牌号
        String carNum = tcCarInpassData.getPlateNumber();
        //车辆颜色
        String carColor = tcCarInpassData.getPlateColor();
        //操作员
        String stationOperator =tcCarInpassData.getStationOperator();
        //套餐名称
        String sealName = ServletRequestUtils.getStringParameter(request,"sealName","");

        Map<String, Object> condition = new HashMap<>();
        TcCarInpass tcCarInpass= new TcCarInpass();
        tcCarInpass.setInRecordId(inRecordId);
        tcCarInpass.setParkId(parkId);
        tcCarInpass.setInDeviceId(inDeviceId);
        tcCarInpass.setInDeviceName(inDeviceName);
        tcCarInpass.setInTime(inTime);
        tcCarInpass.setIntimg(inImage);
        tcCarInpass.setCarNum(carNum);
        tcCarInpass.setCarColor(carColor);
        tcCarInpass.setStationOperator(stationOperator);
        tcCarInpass.setSealName(sealName);
        return tcCarInpassService.insertCarin(tcCarInpass,condition);
    }


    /*
     * 捷顺
     * 第三方接口
     * 接收车辆出场过闸记录
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("carout")
    @ResponseBody
    public JsReturnJsonData carout(@RequestBody TcCarOutpassData tcCarOutpassData, HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        logger.info("出场过闸记录："+JSONObject.fromObject(tcCarOutpassData));
        //车场ID
        String parkId =tcCarOutpassData.getParkId();
        //出场记录唯一标识
        String outRecordId = tcCarOutpassData.getOutRecordId();
        //出场设备标识
        String outDeviceId =tcCarOutpassData.getOutDeviceId();
        //出场设备名称
        String outDeviceName =tcCarOutpassData.getOutDeviceName();
        //出场时间
        String outTime =tcCarOutpassData.getOutTime();
        //入场记录唯一标识
        String inRecordId = tcCarOutpassData.getInRecordId();
        //入场设备标识
        String inDeviceId =tcCarOutpassData.getInDeviceId();
        //入场设备名称
        String inDeviceName = tcCarOutpassData.getInDeviceName();
        //入场时间
        String inTime = tcCarOutpassData.getInTime();
        //入场车辆图片
        String inImage = tcCarOutpassData.getInImage();
        //出场车辆图片
        String outImage =tcCarOutpassData.getOutImage();
        //车牌号
        String carNum = tcCarOutpassData.getPlateNumber();
        //车辆颜色
        String carColor = tcCarOutpassData.getPlateColor();
        //操作员
        String stationOperator = tcCarOutpassData.getStationOperator();
        //应收金额
        Integer chargeTotal = tcCarOutpassData.getChargeTotal();
        //折扣金额
        Integer discountAmount =tcCarOutpassData.getDiscountAmount();
        //实收金额
        Integer charge = tcCarOutpassData.getCharge();
        //套餐名称
        String sealName = tcCarOutpassData.getSealName();

        Map<String, Object> condition = new HashMap<>();
        TcCarOutpass tcCarOutpass= new TcCarOutpass();
        tcCarOutpass.setParkId(parkId);
        tcCarOutpass.setOutRecordId(outRecordId);
        tcCarOutpass.setOutDeviceId(outDeviceId);
        tcCarOutpass.setOutDeviceName(outDeviceName);
        tcCarOutpass.setOutTime(outTime);
        tcCarOutpass.setInTime(inTime);
        tcCarOutpass.setInRecordId(inRecordId);
        tcCarOutpass.setInDeviceId(inDeviceId);
        tcCarOutpass.setInDeviceName(inDeviceName);
        tcCarOutpass.setIntimg(inImage);
        tcCarOutpass.setOutimg(outImage);
        tcCarOutpass.setCarNum(carNum);
        tcCarOutpass.setCarColor(carColor);
        tcCarOutpass.setStationOperator(stationOperator);
        tcCarOutpass.setChargeTotal(chargeTotal);
        tcCarOutpass.setDiscountAmount(discountAmount);
        tcCarOutpass.setCharge(charge);
        tcCarOutpass.setSealName(sealName);
        return tcCarOutpassService.insertCarout(tcCarOutpass,condition);
    }


    /*
     * 捷顺
     * 第三方接口
     * 支付结果反查
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("paycheck")
    @ResponseBody
    public JsReturnJsonData paycheck(@RequestBody TcPaycheckData tcPaycheckData, HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        logger.info("支付结果反查："+JSONObject.fromObject(tcPaycheckData));
        //车场ID
        String parkId = tcPaycheckData.getParkId();
        //支付编号
        String payNo = tcPaycheckData.getPayNo();
        if(StringUtils.isEmpty(payNo)){
            return new JsReturnJsonData(DataCodeUtil.PARAM_ERROR+"","传入的支付编号为空",null);
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("parkId",parkId);
        condition.put("payNo",payNo);
        return sysOrderService.getOrderPaycheck(condition);
    }



    /**
     * 场内车辆查询
     * 场内所停的车辆
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("insidePage")
    @ResponseBody
    public ReturnJsonData insidePage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String carNum = ServletRequestUtils.getStringParameter(request, "carNum", "");
        if (StringUtils.isNotEmpty(carNum)) {
            condition.put("carNum", carNum);
        }
        //入场开始时间
        String inSdate = ServletRequestUtils.getStringParameter(request,"inSdate","");
        if(StringUtils.isNotEmpty(inSdate)){
            condition.put("inSdate",inSdate);
        }
        //入场结束时间
        String inEdate = ServletRequestUtils.getStringParameter(request,"inEdate","");
        if(StringUtils.isNotEmpty(inEdate)){
            condition.put("inEdate",inEdate);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcCarInpassService.getInsideJsList(condition,pageHelper);
    }

    /**
     * 场内车辆查询
     * 测试远程开闸
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("jsOpengate")
    @ResponseBody
    public JsReturnJsonData jsOpengate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String parkId = ServletRequestUtils.getStringParameter(request, "parkId", "");
        if (StringUtils.isNotEmpty(parkId)) {
            condition.put("parkId", parkId);
        }
        //入场开始时间
        String deviceId = ServletRequestUtils.getStringParameter(request,"deviceId","");
        if(StringUtils.isNotEmpty(deviceId)){
            condition.put("deviceId",deviceId);
        }
        //入场开始时间
        String openModel = ServletRequestUtils.getStringParameter(request,"openModel","");
        if(StringUtils.isNotEmpty(openModel)){
            condition.put("openModel",openModel);
        }

        TcCarInout tcCarInout=new TcCarInout();
        tcCarInout.setOutDeviceId(deviceId);
        tcCarInout.setParkId(parkId);
        tcCarInout.setType(Integer.valueOf(openModel));
        return tcCarInoutService.jsOpengate(tcCarInout,condition);
    }


    /**
     * 场内车辆查询
     * 测试远程开闸
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("jsCouponCharging")
    @ResponseBody
    public JsReturnJsonData jsCouponCharging(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String carNum = ServletRequestUtils.getStringParameter(request, "carNum", "");
        if (StringUtils.isNotEmpty(carNum)) {
            condition.put("carNum", carNum);
        }
        String extendCouponId = ServletRequestUtils.getStringParameter(request,"extendCouponId","");
        if(StringUtils.isNotEmpty(extendCouponId)){
            condition.put("extendCouponId",extendCouponId);
        }
        TcCarInout tcCarInout=new TcCarInout();
        tcCarInout.setCarNum(carNum);
        return tcCarInoutService.jsCouponCharging(tcCarInout,condition);
    }


    /**
     * 场内车辆查询
     * 测试远程开闸
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("jsChargingIsEntry")
    @ResponseBody
    public JsReturnJsonData jsChargingIsEntry(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String carNum = ServletRequestUtils.getStringParameter(request, "carNum", "");
        String tocNum = ServletRequestUtils.getStringParameter(request, "tocNum", "");
        Integer num= tcCarInpassService.updateBatchIsEntryByInsideJscarAbnormal(carNum,tocNum);
        if(num.equals(0)) {
            return new JsReturnJsonData(DataCodeUtil.PARAM_ERROR + "", "成功"+num, null);
        }
        return new JsReturnJsonData(DataCodeUtil.SUCCESS + "", "成功"+num, null);
    }



}
