package com.perenc.xh.lsp.controller.phone.tcSeller;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplyc;
import com.perenc.xh.lsp.service.tcSellerApplyc.TcSellerApplycService;
import com.perenc.xh.lsp.service.tcSellerApplycoupon.TcSellerApplycouponService;
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
@RequestMapping("api/sellerApplycoupon")
public class TcSellerApplyCouponController {

    @Autowired(required = false)
    private TcSellerApplycouponService tcSellerApplycouponService;

    @Autowired(required = false)
    private TcSellerApplycService tcSellerApplycService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="商家申请停车券",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String type = ServletRequestUtils.getStringParameter(request,"type","1");
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String sellerId = ServletRequestUtils.getStringParameter(request,"sellerId","");
        String sellerUserId = ServletRequestUtils.getStringParameter(request,"sellerUserId","");
        String number = ServletRequestUtils.getStringParameter(request,"number","1");
        String ticketImg = ServletRequestUtils.getStringParameter(request,"ticketImg","");
        String sdate = ServletRequestUtils.getStringParameter(request,"sdate","");
        String edate = ServletRequestUtils.getStringParameter(request,"edate","");
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的申请类型为空",null);
        }
        if(StringUtils.isEmpty(sellerId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家ID为空",null);
        }
        if(StringUtils.isEmpty(sellerUserId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家用户ID为空",null);
        }
        if(Integer.valueOf(type).equals(2)) {
            if(StringUtils.isEmpty(name)){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的申请名称为空",null);
            }
            if(StringUtils.isEmpty(ticketImg)){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的凭证为空",null);
            }
            if(StringUtils.isEmpty(sdate)){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的有效开始时间为空",null);
            }
            if(StringUtils.isEmpty(edate)){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的有效结束时间为空",null);
            }
        }
        TcSellerApplyc tcSellerApplyc =new TcSellerApplyc();
        tcSellerApplyc.setType(Integer.valueOf(type));
        tcSellerApplyc.setName(name);
        tcSellerApplyc.setSellerId(Integer.valueOf(sellerId));
        tcSellerApplyc.setSellerUserId(Integer.valueOf(sellerUserId));
        tcSellerApplyc.setNumber(Integer.valueOf(number));
        tcSellerApplyc.setTicketImg(ticketImg);
        tcSellerApplyc.setSdate(sdate);
        tcSellerApplyc.setEdate(edate);
        return tcSellerApplycService.insertDetail(tcSellerApplyc);
    }

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("addMeeting")
    @ResponseBody
    @OperLog(operationType="商家申请停车券",operationName="新增")
    public ReturnJsonData addMeeting(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String sellerId = ServletRequestUtils.getStringParameter(request,"sellerId","");
        String sellerUserId = ServletRequestUtils.getStringParameter(request,"sellerUserId","");
        String number = ServletRequestUtils.getStringParameter(request,"number","");
        String ticketImg = ServletRequestUtils.getStringParameter(request,"ticketImg","");
        String sdate = ServletRequestUtils.getStringParameter(request,"sdate","");
        String edate = ServletRequestUtils.getStringParameter(request,"edate","");
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的申请名称为空",null);
        }
        if(StringUtils.isEmpty(sellerId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家ID为空",null);
        }
        if(StringUtils.isEmpty(sellerUserId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家用户ID为空",null);
        }
        if(StringUtils.isEmpty(number)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的张数为空",null);
        }
        if(StringUtils.isEmpty(ticketImg)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的凭证为空",null);
        }
        if(StringUtils.isEmpty(sdate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的有效开始时间为空",null);
        }
        if(StringUtils.isEmpty(edate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的有效结束时间为空",null);
        }
        TcSellerApplyc tcSellerApplyc =new TcSellerApplyc();
        tcSellerApplyc.setName(name);
        tcSellerApplyc.setSellerId(Integer.valueOf(sellerId));
        tcSellerApplyc.setSellerUserId(Integer.valueOf(sellerUserId));
        tcSellerApplyc.setNumber(Integer.valueOf(number));
        tcSellerApplyc.setTicketImg(ticketImg);
        tcSellerApplyc.setSdate(sdate);
        tcSellerApplyc.setEdate(edate);
        return tcSellerApplycService.insertMeeting(tcSellerApplyc);
    }

    /**
     * 根据Id查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/getById")
    @OperLog(operationType="商家申请停车券",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcSellerApplycouponService.getById(id);
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
    @OperLog(operationType="商家申请停车券",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        String sellerId = ServletRequestUtils.getStringParameter(request, "sellerId", "");
        if (StringUtils.isNotEmpty(sellerId)) {
            condition.put("sellerId", sellerId);
        }
        // 申请类型 1=商家发放；2=婚宴、会议发放
        String type = ServletRequestUtils.getStringParameter(request, "type", "1");
        if (StringUtils.isNotEmpty(type)) {
            condition.put("type", type);
        }
        // 名称
        String name = ServletRequestUtils.getStringParameter(request, "name", "");
        if (StringUtils.isNotEmpty(name)) {
            condition.put("name", name);
        }
        String isApproval = ServletRequestUtils.getStringParameter(request, "isApproval", "2");
        if (StringUtils.isNotEmpty(isApproval)) {
            condition.put("isApproval", isApproval);
        }
        String useStatus = ServletRequestUtils.getStringParameter(request, "useStatus", "1");
        if (StringUtils.isNotEmpty(useStatus)) {
            condition.put("useStatus", useStatus);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcSellerApplycouponService.getList(condition,pageHelper);
    }


    /**
     * 后台列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    @ResponseBody
    @OperLog(operationType="商家申请停车券",operationName="列表查询")
    public ReturnJsonData list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        // 商家ID
        String sellerId = ServletRequestUtils.getStringParameter(request, "sellerId", "");
        if (StringUtils.isNotEmpty(sellerId)) {
            condition.put("sellerId", sellerId);
        }
        // 申请类型 1=商家发放；2=婚宴、会议发放
        String type = ServletRequestUtils.getStringParameter(request, "type", "1");
        if (StringUtils.isNotEmpty(type)) {
            condition.put("type", type);
        }
        // 名称
        String name = ServletRequestUtils.getStringParameter(request, "name", "");
        if (StringUtils.isNotEmpty(name)) {
            condition.put("name", name);
        }
        // 是否审核通过（1:待审核，2:通过，3:未通过）
        String isApproval = ServletRequestUtils.getStringParameter(request, "isApproval", "2");
        if (StringUtils.isNotEmpty(isApproval)) {
            condition.put("isApproval", isApproval);
        }
        // 申使用状态 1:待使用，4:已过期
        String useStatus = ServletRequestUtils.getStringParameter(request, "useStatus", "1");
        if (StringUtils.isNotEmpty(useStatus)) {
            condition.put("useStatus", useStatus);
        }
        return tcSellerApplycouponService.getAllList(condition);
    }

    /**
     * 停车券明细
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("detail")
    @ResponseBody
    @OperLog(operationType="商家申请停车券",operationName="列表查询")
    public ReturnJsonData detail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        String sellerId = ServletRequestUtils.getStringParameter(request, "sellerId", "");
        if (StringUtils.isNotEmpty(sellerId)) {
            condition.put("sellerId", sellerId);
        }
        String type = ServletRequestUtils.getStringParameter(request, "type", "1");
        if (StringUtils.isNotEmpty(type)) {
            condition.put("type", type);
        }
        String isApproval = ServletRequestUtils.getStringParameter(request, "isApproval", "2");
        if (StringUtils.isNotEmpty(isApproval)) {
            condition.put("isApproval", isApproval);
        }
        String useStatus = ServletRequestUtils.getStringParameter(request, "useStatus", "1");
        if (StringUtils.isNotEmpty(useStatus)) {
            condition.put("useStatus", useStatus);
        }
        return tcSellerApplycouponService.getAllList(condition);
    }


    /**
     * 停车券明细
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("totalNumber")
    @ResponseBody
    @OperLog(operationType="商家申请停车券",operationName="列表查询")
    public ReturnJsonData totalNumber(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        String sellerId = ServletRequestUtils.getStringParameter(request, "sellerId", "");
        if (StringUtils.isNotEmpty(sellerId)) {
            condition.put("sellerId", sellerId);
        }
        return tcSellerApplycouponService.getAllTotalNumber(condition);
    }

    /**
     * 后台列表
     * 申领票券记录
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("applyCoupon")
    @ResponseBody
    @OperLog(operationType="商家申请停车券",operationName="列表查询")
    public ReturnJsonData applyCoupon(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        String sellerId = ServletRequestUtils.getStringParameter(request, "sellerId", "");
        if (StringUtils.isNotEmpty(sellerId)) {
            condition.put("sellerId", sellerId);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcSellerApplycouponService.getApplyCoupon(condition,pageHelper);
    }

    /**
     * 后台列表
     * 用户发放
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("extendCoupon")
    @ResponseBody
    @OperLog(operationType="商家申请停车券",operationName="列表查询")
    public ReturnJsonData extendCoupon(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        String sellerId = ServletRequestUtils.getStringParameter(request, "sellerId", "");
        if (StringUtils.isNotEmpty(sellerId)) {
            condition.put("sellerId", sellerId);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcSellerApplycouponService.getExtendCoupon(condition,pageHelper);
    }

}
