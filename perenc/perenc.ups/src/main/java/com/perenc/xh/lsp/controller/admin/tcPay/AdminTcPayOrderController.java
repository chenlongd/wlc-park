package com.perenc.xh.lsp.controller.admin.tcPay;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.excel.ExportExcelUtil;
import com.perenc.xh.lsp.entity.order.SysOrder;
import com.perenc.xh.lsp.service.order.SysOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@Controller
@RequestMapping("pay")
public class AdminTcPayOrderController {


    @Autowired(required = false)
    private SysOrderService orderService;


    /**
     * 车辆
     * 缴费明细
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("detail")
    @ResponseBody
    public ReturnJsonData detail(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String,Object> map = new HashMap<>();
        //订单号
        String orderNo = ServletRequestUtils.getStringParameter(request,"orderNo","");
        if(StringUtils.isNotEmpty(orderNo)){
            map.put("orderNo",orderNo);
        }
        //车牌号
        String carNum = ServletRequestUtils.getStringParameter(request,"carNum","");
        if(StringUtils.isNotEmpty(carNum)){
            map.put("carNum",carNum);
        }
        // 手机号
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        if(StringUtils.isNotEmpty(phone)){
            map.put("phone",phone);
        }
        //支付开始时间
        String startTime = ServletRequestUtils.getStringParameter(request,"startTime","");
        if(StringUtils.isEmpty(startTime)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的开始时间为空",null);
        }
        map.put("startTime",startTime);

        //支付结束时间
        String endTime = ServletRequestUtils.getStringParameter(request,"endTime","");
        if(StringUtils.isEmpty(endTime)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的结束时间为空",null);
        }
        map.put("endTime",endTime);
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", 20);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return orderService.getTcListDetail(map,pageHelper);
    }

    /**
     * 车辆
     * 缴费明细 导出
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("exportDetailExcel")
    public void exportDetailExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDD) + "购房者excel导出.xls");
        //EXCEL的头
        List<LinkedHashMap<String, Object>> dataSet = new ArrayList<>();
        LinkedHashMap<String, Object> title = new LinkedHashMap<>();
        title.put("datetime", "缴费时间");
        title.put("carNum", "车牌号");
        title.put("hours", "停车时长");
        title.put("wechat", "微信支付金额（元）");
        title.put("balance", "余额支付金额（元）");
        title.put("couponMoney", "优惠券抵扣金额（元）");
        title.put("couponHours", "优惠券抵扣小时数");
        title.put("total", "总费用（元）");
        title.put("actualMoney", "实际缴费（元）");
        dataSet.add(title);
        //条件筛选的数据
        Map<String,Object> map = new HashMap<>();
        //订单号
        String orderNo = ServletRequestUtils.getStringParameter(request,"orderNo","");
        if(StringUtils.isNotEmpty(orderNo)){
            map.put("orderNo",orderNo);
        }
        //车牌号
        String carNum = ServletRequestUtils.getStringParameter(request,"carNum","");
        if(StringUtils.isNotEmpty(carNum)){
            map.put("carNum",carNum);
        }
        // 手机号
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        if(StringUtils.isNotEmpty(phone)){
            map.put("phone",phone);
        }
        //支付开始时间
        String startTime = ServletRequestUtils.getStringParameter(request,"startTime","");
        if(StringUtils.isNotEmpty(startTime)){
            map.put("startTime",startTime);
        }


        //支付结束时间
        String endTime = ServletRequestUtils.getStringParameter(request,"endTime","");
        if(StringUtils.isNotEmpty(endTime)){
            map.put("endTime",endTime);
        }
        List<Map<String,Object>> sysOrderList = orderService.getTcListAllDetail(map);
        for (Map<String,Object>  order: sysOrderList) {
            LinkedHashMap<String, Object> content = new LinkedHashMap<String, Object>();
            content.put("datetime", order.get("datetime"));
            content.put("carNum", order.get("carNum"));
            content.put("hours", order.get("hours"));
            content.put("wechat", order.get("wechat"));
            content.put("balance", order.get("balance"));
            content.put("couponMoney",order.get("couponMoney").toString());
            content.put("couponHours", order.get("couponHours"));
            content.put("total", order.get("total").toString());
            content.put("actualMoney",order.get("actualMoney").toString());
            dataSet.add(content);
        }
        try {
            ExportExcelUtil.exportExcel("停车缴费明细导出", dataSet, response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 车辆
     * 充值明细
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("rechargedetail")
    @ResponseBody
    public ReturnJsonData rechargedetail(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String,Object> map = new HashMap<>();
        //订单号
        String orderNo = ServletRequestUtils.getStringParameter(request,"orderNo","");
        if(StringUtils.isNotEmpty(orderNo)){
            map.put("orderNo",orderNo);
        }
        //手机号
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        if(StringUtils.isNotEmpty(phone)){
            map.put("phone",phone);
        }
        //支付开始时间
        String startTime = ServletRequestUtils.getStringParameter(request,"startTime","");
        if(StringUtils.isEmpty(startTime)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的开始时间为空",null);
        }
        map.put("startTime",startTime);

        //支付结束时间
        String endTime = ServletRequestUtils.getStringParameter(request,"endTime","");
        if(StringUtils.isEmpty(endTime)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的结束时间为空",null);
        }
        map.put("endTime",endTime);
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", 20);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return orderService.getTcListRechargeDetail(map,pageHelper);
    }

    /**
     * 车辆
     * 充值明细 导出
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("exportRechargedetailExcel")
    public void exportRechargedetailExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDD) + "购房者excel导出.xls");
        //EXCEL的头
        List<LinkedHashMap<String, Object>> dataSet = new ArrayList<>();
        LinkedHashMap<String, Object> title = new LinkedHashMap<>();
        title.put("phone", "手机号");
        title.put("chargeMoney", "充值金额（元）");
        title.put("datetime", "缴费时间");
        dataSet.add(title);
        //条件筛选的数据
        Map<String,Object> map = new HashMap<>();
        //订单号
        String orderNo = ServletRequestUtils.getStringParameter(request,"orderNo","");
        if(StringUtils.isNotEmpty(orderNo)){
            map.put("orderNo",orderNo);
        }
        //手机号
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        if(StringUtils.isNotEmpty(phone)){
            map.put("phone",phone);
        }
        //支付开始时间
        String startTime = ServletRequestUtils.getStringParameter(request,"startTime","");
        if(StringUtils.isNotEmpty(startTime)){
            map.put("startTime",startTime);
        }
        //支付结束时间
        String endTime = ServletRequestUtils.getStringParameter(request,"endTime","");
        if(StringUtils.isNotEmpty(endTime)){
            map.put("endTime",endTime);
        }
        List<Map<String,Object>> sysOrderList = orderService.getTcListAllRechargeDetail(map);
        for (Map<String,Object>  order: sysOrderList) {
            LinkedHashMap<String, Object> content = new LinkedHashMap<String, Object>();
            content.put("phone", order.get("phone"));
            content.put("chargeMoney", order.get("chargeMoney"));
            content.put("datetime", order.get("datetime"));
            dataSet.add(content);
        }
        try {
            ExportExcelUtil.exportExcel("停车充值明细导出", dataSet, response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 车辆
     * vip购卡明细
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("vipdetail")
    @ResponseBody
    public ReturnJsonData vipdetail(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String,Object> map = new HashMap<>();
        //订单号
        String orderNo = ServletRequestUtils.getStringParameter(request,"orderNo","");
        if(StringUtils.isNotEmpty(orderNo)){
            map.put("orderNo",orderNo);
        }
        //手机号
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        if(StringUtils.isNotEmpty(phone)){
            map.put("phone",phone);
        }
        //支付开始时间
        String startTime = ServletRequestUtils.getStringParameter(request,"startTime","");
        if(StringUtils.isEmpty(startTime)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的开始时间为空",null);
        }
        map.put("startTime",startTime);

        //支付结束时间
        String endTime = ServletRequestUtils.getStringParameter(request,"endTime","");
        if(StringUtils.isEmpty(endTime)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的结束时间为空",null);
        }
        map.put("endTime",endTime);
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", 20);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return orderService.getTcListvipDetail(map,pageHelper);
    }

    /**
     * 车辆
     * vip购卡明细 导出
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("exportVipdetailExcel")
    public void exportVipdetailExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDD) + "购房者excel导出.xls");
        //EXCEL的头
        List<LinkedHashMap<String, Object>> dataSet = new ArrayList<>();
        LinkedHashMap<String, Object> title = new LinkedHashMap<>();
        title.put("phone", "手机号");
        title.put("vipName", "VIP名称");
        title.put("type", "VIP类型");
        title.put("buytime", "购买时间");
        title.put("money", "购买金额（元）");
        title.put("carNum", "使用车牌号");
        title.put("useTime", "使用时间");
        dataSet.add(title);
        //条件筛选的数据
        Map<String,Object> map = new HashMap<>();
        //订单号
        String orderNo = ServletRequestUtils.getStringParameter(request,"orderNo","");
        if(StringUtils.isNotEmpty(orderNo)){
            map.put("orderNo",orderNo);
        }
        //手机号
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        if(StringUtils.isNotEmpty(phone)){
            map.put("phone",phone);
        }
        //支付开始时间
        String startTime = ServletRequestUtils.getStringParameter(request,"startTime","");
        if(StringUtils.isNotEmpty(startTime)){
            map.put("startTime",startTime);
        }
        //支付结束时间
        String endTime = ServletRequestUtils.getStringParameter(request,"endTime","");
        if(StringUtils.isNotEmpty(endTime)){
            map.put("endTime",endTime);
        }
        List<Map<String,Object>> sysOrderList = orderService.getTcListAllVipDetail(map);
        for (Map<String,Object>  order: sysOrderList) {
            LinkedHashMap<String, Object> content = new LinkedHashMap<String, Object>();
            content.put("phone", order.get("phone"));
            content.put("vipName", order.get("vipName"));
            content.put("type", order.get("type"));
            content.put("buytime", order.get("buytime"));
            content.put("money", order.get("money"));
            content.put("carNum", order.get("carNum"));
            content.put("useTime", order.get("useTime"));
            dataSet.add(content);
        }
        try {
            ExportExcelUtil.exportExcel("停车购卡明细导出", dataSet, response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 车辆
     * 缴费统计
     * 总收益，总支付笔数，总的入场车次，入场车辆
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("totalData")
    @ResponseBody
    public ReturnJsonData totalData(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String,Object> map = new HashMap<>();
        SysOrder sysOrder = new SysOrder();
        return orderService.findTcOrderTotalData(sysOrder,map);
    }


    /**
     * 车辆
     * 缴费统计
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("statistics")
    @ResponseBody
    public ReturnJsonData statistics(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String,Object> map = new HashMap<>();
        //支付开始时间
        String startTime = ServletRequestUtils.getStringParameter(request,"startTime","");
        if(StringUtils.isEmpty(startTime)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的开始时间为空",null);
        }
        map.put("startTime",startTime);
        //支付结束时间
        String endTime = ServletRequestUtils.getStringParameter(request,"endTime","");
        if(StringUtils.isEmpty(endTime)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的结束时间为空",null);
        }
        map.put("endTime",endTime);
        SysOrder sysOrder = new SysOrder();
        return orderService.findTcOrderStatistics(sysOrder,map);
    }
}
