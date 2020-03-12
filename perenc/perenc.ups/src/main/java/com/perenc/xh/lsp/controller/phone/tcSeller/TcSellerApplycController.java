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
@RequestMapping("api/sellerApplyc")
public class TcSellerApplycController {

    @Autowired(required = false)
    private TcSellerApplycouponService tcSellerApplycouponService;

    @Autowired(required = false)
    private TcSellerApplycService tcSellerApplycService;


    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="商家申请停车券",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String number = ServletRequestUtils.getStringParameter(request,"number","1");
        String sdate = ServletRequestUtils.getStringParameter(request,"sdate","");
        String edate = ServletRequestUtils.getStringParameter(request,"edate","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主id为空",null);
        }
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的申请名称为空",null);
        }
        if(StringUtils.isEmpty(number)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的张数为空",null);
        }
        if(StringUtils.isEmpty(sdate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的有效开始时间为空",null);
        }
        if(StringUtils.isEmpty(edate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的有效结束时间为空",null);
        }
        TcSellerApplyc tcSellerApplyc =new TcSellerApplyc();
        tcSellerApplyc.setId(id);
        tcSellerApplyc.setName(name);
        tcSellerApplyc.setNumber(Integer.valueOf(number));
        tcSellerApplyc.setSdate(sdate);
        tcSellerApplyc.setEdate(edate);
        return tcSellerApplycService.updateDetail(tcSellerApplyc);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateTime")
    @ResponseBody
    @OperLog(operationType="商家申请停车券",operationName="修改时间")
    public ReturnJsonData updateTime(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request,"sellerApplycId","");
        String sdate = ServletRequestUtils.getStringParameter(request,"sdate","");
        String edate = ServletRequestUtils.getStringParameter(request,"edate","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主键ID为空",null);
        }
        if(StringUtils.isEmpty(sdate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的有效期开始时间为空",null);
        }
        if(StringUtils.isEmpty(edate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的有效期结束时间为空",null);
        }
        TcSellerApplyc tcSellerApplyc =new TcSellerApplyc();
        tcSellerApplyc.setId(id);
        tcSellerApplyc.setSdate(sdate);
        tcSellerApplyc.setEdate(edate);
        return tcSellerApplycService.updateTime(tcSellerApplyc);
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
            return tcSellerApplycService.getById(id);
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
        // 是否审核通过（1:待审核，2:通过，3:未通过）
        String isApproval = ServletRequestUtils.getStringParameter(request, "isApproval", "2");
        if (StringUtils.isNotEmpty(isApproval)) {
            condition.put("isApproval", isApproval);
            if(isApproval.equals("0")) {
                condition.put("isApproval", 2);
            }
        }
        // 申使用状态 1:待使用，4:已过期
        String useStatus = ServletRequestUtils.getStringParameter(request, "useStatus", "1");
        if (StringUtils.isNotEmpty(useStatus)) {
            condition.put("useStatus", useStatus);
            if(useStatus.equals("0")) {
                condition.put("useStatus", 1);
            }
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcSellerApplycService.getList(condition,pageHelper);
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
            if(isApproval.equals("0")) {
                condition.put("isApproval", 2);
            }
        }
        // 申使用状态 1:待使用，4:已过期
        String useStatus = ServletRequestUtils.getStringParameter(request, "useStatus", "1");
        if (StringUtils.isNotEmpty(useStatus)) {
            condition.put("useStatus", useStatus);
            if(useStatus.equals("0")) {
                condition.put("useStatus", 1);
            }
        }
        return tcSellerApplycouponService.getAllList(condition);
    }



}
