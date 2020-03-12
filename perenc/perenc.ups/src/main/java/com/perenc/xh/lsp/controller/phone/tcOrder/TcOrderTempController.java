package com.perenc.xh.lsp.controller.phone.tcOrder;

import com.perenc.xh.commonUtils.model.DataCodeUtil;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcOrder.TcOrderTemp;
import com.perenc.xh.lsp.service.order.SysOrderService;
import com.perenc.xh.lsp.service.tcOrderTemp.TcOrderTempService;
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
@RequestMapping("api/orderTemp")
public class TcOrderTempController {

    @Autowired(required = false)
    private TcOrderTempService tcOrderTempService;


    @Autowired(required = false)
    private SysOrderService orderService;


    /**
     * 微信充值
     * 订单支付金额
     * @return
     */
    @ResponseBody
    @RequestMapping("/payCheck")
    @OperLog(operationType="临时订单",operationName="充值")
    public ReturnJsonData payCheck(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String carRechargeId = ServletRequestUtils.getStringParameter(request,"carRechargeId","");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(StringUtils.isEmpty(carRechargeId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的充值项ID为空",null);
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("carRechargeId",carRechargeId);
        TcOrderTemp tcOrderTemp=new TcOrderTemp();
        tcOrderTemp.setExtendId(Integer.valueOf(extendId));
        return tcOrderTempService.payCheck(condition,tcOrderTemp);
    }




    /**
     * 停车缴费生成定单
     * 查询定单信息
     * @return
     */
    @ResponseBody
    @RequestMapping("/addOrderPay")
    @OperLog(operationType="临时定单",operationName="生成定单")
    public ReturnJsonData addOrderPay(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String carId = ServletRequestUtils.getStringParameter(request,"carId","");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(StringUtils.isEmpty(carId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID为空",null);
        }
        Map<String, Object> condition = new HashMap<>();
        TcOrderTemp tcOrderTemp=new TcOrderTemp();
        tcOrderTemp.setExtendId(Integer.valueOf(extendId));
        tcOrderTemp.setCarId(carId);
        return tcOrderTempService.addOrderTempPay(condition,tcOrderTemp);
    }


    /**
     * 停车缴费
     * 确认订单（预支付）
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("confirmOrder")
    @ResponseBody
    public ReturnJsonData confirmOrder(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String,Object> map = new HashMap<>();
        //SysOrder order = null;
        String orderNo = ServletRequestUtils.getStringParameter(request,"orderNo","");
        if(StringUtils.isNotEmpty(orderNo)){
            map.put("orderNo",orderNo);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的订单号为空",null);
        }

        int payType = ServletRequestUtils.getIntParameter(request,"payType",0);
        if(payType == 0) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的支付类型为空",null);
        }else{
            map.put("payType",payType);
        }
        int extendId = ServletRequestUtils.getIntParameter(request,"extendId",0);
        if(extendId == 0) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }else{
            map.put("extendId",extendId);
        }
        if(payType==4 || payType==3) {
            int payMode = ServletRequestUtils.getIntParameter(request, "payMode", 0);
            if (payMode == 0) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "传入的支付方式为空", null);
            } else {
                map.put("payMode", payMode);
            }
        }
        String extendCouponId = ServletRequestUtils.getStringParameter(request,"extendCouponId","");
        if(StringUtils.isNotEmpty(extendCouponId)){
            map.put("extendCouponId",extendCouponId);
        }
        //余额支付密码
        String payPsword = ServletRequestUtils.getStringParameter(request,"payPsword","");
        if(StringUtils.isNotEmpty(payPsword)){
            map.put("payPsword",payPsword);
        }
        String remark = ServletRequestUtils.getStringParameter(request,"remark","");
        map.put("remark",remark);

        double totalPrice = ServletRequestUtils.getDoubleParameter(request, "totalPrice", 0d);
        if(totalPrice >= 0d){
            map.put("totalPrice",totalPrice);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的总金额为空",null);
        }
        map.put("spbillCreateIp",getClientIp(request));
        return orderService.confirmOrder(map);
    }




    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if(StringUtils.isNotBlank(ip)) {
            ip = ip.split(",")[0];
        }
        return ip;

    }


    /**
     * 停车定单查询
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("page")
    @ResponseBody
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String,Object> map = new HashMap<>();
        //用户Id
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        if(StringUtils.isNotEmpty(extendId)){
            map.put("extendId",extendId);
        }
        //支付类型 商品、停车场
        String payType = ServletRequestUtils.getStringParameter(request,"payType","");
        if(StringUtils.isNotEmpty(payType)){
            map.put("payType",payType);
        }
        //订单时间
        String orderTime = ServletRequestUtils.getStringParameter(request,"orderTime","");
        if(StringUtils.isNotEmpty(orderTime)){
            map.put("orderTime",orderTime);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", 10);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return orderService.getTcList(map,pageHelper);
    }
}
