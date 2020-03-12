package com.perenc.xh.lsp.controller.phone.tcExtend;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCoupon;
import com.perenc.xh.lsp.service.tcExtendCoupon.TcExtendCouponService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("api/extendCoupon")
public class TcExtendCouponController {

    @Autowired(required = false)
    private TcExtendCouponService tcExtendCouponService;

    /**
     * 发放停车券添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="客户优惠券",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String sellerUserId = ServletRequestUtils.getStringParameter(request,"sellerUserId","");
        String sellerApplycouponId = ServletRequestUtils.getStringParameter(request,"sellerApplycouponId","");
        String carId = ServletRequestUtils.getStringParameter(request,"carId","");
        String number = ServletRequestUtils.getStringParameter(request,"number","1");
        String ticketImg = ServletRequestUtils.getStringParameter(request,"ticketImg","");
        String scode = ServletRequestUtils.getStringParameter(request,"scode","");
        if(StringUtils.isEmpty(sellerUserId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家用户ID为空",null);
        }
        if(StringUtils.isEmpty(sellerApplycouponId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的申请停车券ID为空",null);
        }
        if(StringUtils.isEmpty(carId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID为空",null);
        }
        if(StringUtils.isEmpty(number)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的张数为空",null);
        }
        if(StringUtils.isEmpty(ticketImg)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的小票为空",null);
        }
        if(StringUtils.isEmpty(scode)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发码唯一标识为空",null);
        }
        //判断积分数
        if(!ValidateUtils.isNumber(number)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的张数",null);
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("sellerApplycouponId",sellerApplycouponId);
        condition.put("number",number);
        TcExtendCoupon tcExtendCoupon=new TcExtendCoupon();
        tcExtendCoupon.setSellerUserId(Integer.valueOf(sellerUserId));
        tcExtendCoupon.setCarId(carId);
        tcExtendCoupon.setTicketImg(ticketImg);
        tcExtendCoupon.setScode(scode);
        tcExtendCoupon.setUseStatus(1);
        return tcExtendCouponService.insertBatch(tcExtendCoupon,condition);
    }

    /**
     * 发放停车券添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("addMeeting")
    @ResponseBody
    @OperLog(operationType="客户优惠券",operationName="新增")
    public ReturnJsonData addMeeting(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String sellerApplycouponId = ServletRequestUtils.getStringParameter(request,"sellerApplycouponId","");
        String carId = ServletRequestUtils.getStringParameter(request,"carId","");
        String number = ServletRequestUtils.getStringParameter(request,"number","1");
        String scode = ServletRequestUtils.getStringParameter(request,"scode","");
        if(StringUtils.isEmpty(sellerApplycouponId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的申请停车券ID为空",null);
        }
        if(StringUtils.isEmpty(carId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID为空",null);
        }
        if(StringUtils.isEmpty(scode)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发码唯一标识为空",null);
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("sellerApplycouponId",sellerApplycouponId);
        condition.put("number",number);
        TcExtendCoupon tcExtendCoupon=new TcExtendCoupon();
        tcExtendCoupon.setCarId(carId);
        tcExtendCoupon.setScode(scode);
        /*JSONObject jsonObject=new JSONObject();
        jsonObject.put("To",2);
        jsonObject.put("message","已过期");
        MyWebSocket.onMessage(jsonObject.toString(),null);*/
        return tcExtendCouponService.insertMeeting(tcExtendCoupon,condition);
    }


    /**
     * 用户第一次注册
     * 首次发绑定车辆
     * 给车辆发券
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("addFirst")
    @ResponseBody
    @OperLog(operationType="客户优惠券",operationName="新增")
    public ReturnJsonData addFirst(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String carId = ServletRequestUtils.getStringParameter(request,"carId","");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的用户ID为空",null);
        }
        if(StringUtils.isEmpty(carId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID为空",null);
        }
        Map<String, Object> condition = new HashMap<>();
        TcExtendCoupon tcExtendCoupon=new TcExtendCoupon();
        tcExtendCoupon.setExtendId(Integer.valueOf(extendId));
        tcExtendCoupon.setCarId(carId);
        tcExtendCoupon.setUseStatus(1);
        return tcExtendCouponService.insertFirst(tcExtendCoupon,condition);
    }

    /**
     * 根据Id查询
     * 首次发放停车券判断
     * @return
     */
    @ResponseBody
    @RequestMapping("/getIsFirst")
    @OperLog(operationType="客户优惠券",operationName="单个查询")
    public ReturnJsonData getIsFirst(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String extendId = ServletRequestUtils.getStringParameter(request, "extendId", "");
        if(StringUtils.isNotEmpty(extendId)) {
            return tcExtendCouponService.getIsFirst(extendId);
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入Id为空",null);
        }
    }

    /**
     * 根据Id查询
     * 首次发放停车券判断
     * @return
     */
    @ResponseBody
    @RequestMapping("/getPolling")
    @OperLog(operationType="客户优惠券",operationName="单个查询")
    public ReturnJsonData getPolling(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String carId = ServletRequestUtils.getStringParameter(request, "carId", "");
        String scode = ServletRequestUtils.getStringParameter(request, "scode", "");
        if(StringUtils.isEmpty(carId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID为空",null);
        }
        if(StringUtils.isEmpty(scode)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发码唯一标识为空",null);
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("carId",carId);
        condition.put("scode",scode);
        return tcExtendCouponService.getPolling(condition);
    }


    /**
     * 根据Id查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/getById")
    @OperLog(operationType="客户优惠券",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcExtendCouponService.getById(id);
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入Id为空",null);
        }
    }


    /**
     * 后台列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("page")
    @ResponseBody
    @OperLog(operationType="客户优惠券",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        //商家名称
        String sellerName = ServletRequestUtils.getStringParameter(request, "sellerName", "");
        if (StringUtils.isNotEmpty(sellerName)) {
            condition.put("sellerName", sellerName);
        }
        //客户ID
        String extendId = ServletRequestUtils.getStringParameter(request, "extendId", "");
        if (StringUtils.isNotEmpty(extendId)) {
            condition.put("extendId", extendId);
        }
        //车辆ID
        String carId = ServletRequestUtils.getStringParameter(request, "carId", "");
        if (StringUtils.isNotEmpty(carId)) {
            condition.put("carId", carId);
        }
        //商家ID
        String sellerId = ServletRequestUtils.getStringParameter(request, "sellerId", "");
        if (StringUtils.isNotEmpty(sellerId)) {
            condition.put("sellerId", sellerId);
        }
        //使用状态
        String useStatus = ServletRequestUtils.getStringParameter(request, "useStatus", "");
        if (StringUtils.isNotEmpty(useStatus)) {
            condition.put("useStatus", useStatus);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcExtendCouponService.getList(condition,pageHelper);
    }
}
