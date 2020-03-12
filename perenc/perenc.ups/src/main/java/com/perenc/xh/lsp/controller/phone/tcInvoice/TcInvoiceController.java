package com.perenc.xh.lsp.controller.phone.tcInvoice;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcInvoice.TcInvoice;
import com.perenc.xh.lsp.service.tcInvoice.TcInvoiceService;
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

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/19 15:28
 **/
@Controller
@RequestMapping("api/invoice")
public class TcInvoiceController {

    //发票
    @Autowired(required = false)
    private TcInvoiceService tcInvoiceService;


    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="发票",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String taxNum = ServletRequestUtils.getStringParameter(request,"taxNum","");
        String title = ServletRequestUtils.getStringParameter(request,"title","");
        String idNumber = ServletRequestUtils.getStringParameter(request,"idNumber","");
        String email = ServletRequestUtils.getStringParameter(request,"email","");
        String contact = ServletRequestUtils.getStringParameter(request,"contact","");
        String province = ServletRequestUtils.getStringParameter(request,"province","");
        String city = ServletRequestUtils.getStringParameter(request,"city","");
        String county = ServletRequestUtils.getStringParameter(request,"county","");
        String address = ServletRequestUtils.getStringParameter(request,"address","");
        String bankName = ServletRequestUtils.getStringParameter(request,"bankName","");
        String bankNumber = ServletRequestUtils.getStringParameter(request,"bankNumber","");
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        String amount = ServletRequestUtils.getStringParameter(request,"amount","");
        String remark = ServletRequestUtils.getStringParameter(request,"remark","");
        String orderIds = ServletRequestUtils.getStringParameter(request,"orderIds","");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发票类型为空",null);
        }
        if(StringUtils.isEmpty(taxNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的税号为空",null);
        }
        if(StringUtils.isEmpty(title)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发票抬头为空",null);
        }
        //判断身份证号
        if(type.equals("2")) {
            if(StringUtils.isEmpty(idNumber)){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的身份证号为空",null);
            }
        }
        if(StringUtils.isEmpty(email)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的邮箱为空",null);
        }
        if(StringUtils.isEmpty(contact)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的联系人为空",null);
        }
        if(StringUtils.isEmpty(province)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的省为空",null);
        }
        if(StringUtils.isEmpty(city)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的市为空",null);
        }
        if(StringUtils.isEmpty(county)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的县为空",null);
        }
        if(StringUtils.isEmpty(address)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的地址为空",null);
        }
        if(StringUtils.isEmpty(phone)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的电话为空",null);
        }
        if(StringUtils.isEmpty(amount)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的金额为空",null);
        }
        if(StringUtils.isEmpty(orderIds)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的订单ID为空",null);
        }
        //判断手机号
        if(StringUtils.isNotEmpty(phone)) {
            if (phone.length() != 11) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "手机号应为11位数", null);
            }
            if (!ValidateUtils.isPhoneCheck(phone)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请输入正确的手机号", null);
            }
        }
        //判断邮箱
        if(StringUtils.isNotEmpty(email)) {
            if (!ValidateUtils.isEmail(email)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请输入正确的邮箱", null);
            }
        }
        //判断金额
        if(!ValidateUtils.isAmount(amount)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的金额",null);
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderIds",orderIds);
        TcInvoice tcInvoice=new TcInvoice();
        tcInvoice.setExtendId(Integer.valueOf(extendId));
        tcInvoice.setType(Integer.valueOf(type));
        tcInvoice.setTaxNum(taxNum);
        tcInvoice.setTitle(title);
        tcInvoice.setIdNumber(idNumber);
        tcInvoice.setEmail(email);
        tcInvoice.setContact(contact);
        tcInvoice.setProvince(province);
        tcInvoice.setCity(city);
        tcInvoice.setCounty(county);
        tcInvoice.setAddress(address);
        tcInvoice.setBankName(bankName);
        tcInvoice.setBankNumber(bankNumber);
        tcInvoice.setPhone(phone);
        tcInvoice.setAmount(ToolUtil.getDoubleToInt(Double.valueOf(amount)));
        tcInvoice.setRemark(remark);
        tcInvoice.setIsInvoice(1);//是否开发票状态
        return tcInvoiceService.insertOrderInvoice(tcInvoice,condition);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="发票",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String taxNum = ServletRequestUtils.getStringParameter(request,"taxNum","");
        String title = ServletRequestUtils.getStringParameter(request,"title","");
        String idNumber = ServletRequestUtils.getStringParameter(request,"idNumber","");
        String email = ServletRequestUtils.getStringParameter(request,"email","");
        String contact = ServletRequestUtils.getStringParameter(request,"contact","");
        String province = ServletRequestUtils.getStringParameter(request,"province","");
        String city = ServletRequestUtils.getStringParameter(request,"city","");
        String county = ServletRequestUtils.getStringParameter(request,"county","");
        String address = ServletRequestUtils.getStringParameter(request,"address","");
        String bankName = ServletRequestUtils.getStringParameter(request,"bankName","");
        String bankNumber = ServletRequestUtils.getStringParameter(request,"bankNumber","");
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        String amount = ServletRequestUtils.getStringParameter(request,"amount","");
        String remark = ServletRequestUtils.getStringParameter(request,"remark","");
        String orderIds = ServletRequestUtils.getStringParameter(request,"orderIds","");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发票类型为空",null);
        }
        if(StringUtils.isEmpty(taxNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的税号为空",null);
        }
        if(StringUtils.isEmpty(title)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发票抬头为空",null);
        }
        //判断身份证号
        if(type.equals("2")) {
            if(StringUtils.isEmpty(idNumber)){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的身份证号为空",null);
            }
        }
        if(StringUtils.isEmpty(email)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的邮箱为空",null);
        }
        if(StringUtils.isEmpty(contact)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的联系人为空",null);
        }
        if(StringUtils.isEmpty(province)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的省为空",null);
        }
        if(StringUtils.isEmpty(city)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的市为空",null);
        }
        if(StringUtils.isEmpty(county)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的县为空",null);
        }
        if(StringUtils.isEmpty(address)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的地址为空",null);
        }
        if(StringUtils.isEmpty(phone)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的电话为空",null);
        }
        if(StringUtils.isEmpty(amount)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的金额为空",null);
        }
        if(StringUtils.isEmpty(orderIds)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的订单ID为空",null);
        }
        //判断手机号
        if(StringUtils.isNotEmpty(phone)) {
            if (phone.length() != 11) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "手机号应为11位数", null);
            }
            if (!ValidateUtils.isPhoneCheck(phone)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请输入正确的手机号", null);
            }
        }
        //判断邮箱
        if(StringUtils.isNotEmpty(email)) {
            if (!ValidateUtils.isEmail(email)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请输入正确的邮箱", null);
            }
        }
        //判断金额
        if(!ValidateUtils.isAmount(amount)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的金额",null);
        }
        TcInvoice tcInvoice=new TcInvoice();
        tcInvoice.setId(id);
        tcInvoice.setType(Integer.valueOf(type));
        tcInvoice.setTaxNum(taxNum);
        tcInvoice.setTitle(title);
        tcInvoice.setIdNumber(idNumber);
        tcInvoice.setEmail(email);
        tcInvoice.setContact(contact);
        tcInvoice.setProvince(province);
        tcInvoice.setCity(city);
        tcInvoice.setCounty(county);
        tcInvoice.setAddress(address);
        tcInvoice.setBankName(bankName);
        tcInvoice.setBankNumber(bankNumber);
        tcInvoice.setPhone(phone);
        tcInvoice.setRemark(remark);
        return tcInvoiceService.update(tcInvoice);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="发票",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcInvoiceService.delete(strarray);
            }else {
                return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE,"删除失败",null);
            }
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入Id为空",null);
        }

    }

    /**
     * 根据Id查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/getById")
    @OperLog(operationType="发票",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcInvoiceService.getById(id);
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
    @OperLog(operationType="发票",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        String extendId = ServletRequestUtils.getStringParameter(request, "extendId", "");
        if (StringUtils.isNotEmpty(extendId)) {
            condition.put("extendId", extendId);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcInvoiceService.getList(condition,pageHelper);
    }







}
