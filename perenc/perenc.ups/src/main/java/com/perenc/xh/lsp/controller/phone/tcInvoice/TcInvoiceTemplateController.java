package com.perenc.xh.lsp.controller.phone.tcInvoice;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcInvoice.TcInvoiceTemplate;
import com.perenc.xh.lsp.service.tcInvoiceTemplate.TcInvoiceTemplateService;
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
@RequestMapping("api/invoiceTemplate")
public class TcInvoiceTemplateController {

    @Autowired(required = false)
    private TcInvoiceTemplateService tcInvoiceTemplateService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="发票模板",operationName="新增")
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
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        String bankName = ServletRequestUtils.getStringParameter(request,"bankName","");
        String bankNumber = ServletRequestUtils.getStringParameter(request,"bankNumber","");
        String isDefault = ServletRequestUtils.getStringParameter(request,"isDefault","2");
        String remark = ServletRequestUtils.getStringParameter(request,"remark","");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的用户ID为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(taxNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的税号为空",null);
        }
        if(StringUtils.isEmpty(title)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发票抬头为空",null);
        }
        //个人：判断身份证号
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
        //单位
        if(type.equals("1")) {
            if(StringUtils.isEmpty(bankName)){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的开户银行为空",null);
            }
            if(StringUtils.isEmpty(bankNumber)){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的开户帐号为空",null);
            }
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
        TcInvoiceTemplate tiTemplate=new TcInvoiceTemplate();
        tiTemplate.setExtendId(Integer.valueOf(extendId));
        tiTemplate.setType(Integer.valueOf(type));
        tiTemplate.setTaxNum(taxNum);
        tiTemplate.setTitle(title);
        tiTemplate.setIdNumber(idNumber);
        tiTemplate.setEmail(email);
        tiTemplate.setContact(contact);
        tiTemplate.setProvince(province);
        tiTemplate.setCity(city);
        tiTemplate.setCounty(county);
        tiTemplate.setAddress(address);
        tiTemplate.setBankName(bankName);
        tiTemplate.setBankNumber(bankNumber);
        tiTemplate.setPhone(phone);
        tiTemplate.setIsDefault(Integer.valueOf(isDefault));//是否设置默认
        tiTemplate.setRemark(remark);
        return tcInvoiceTemplateService.insert(tiTemplate);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="发票模板",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request,"id","");
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
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        String bankName = ServletRequestUtils.getStringParameter(request,"bankName","");
        String bankNumber = ServletRequestUtils.getStringParameter(request,"bankNumber","");
        String remark = ServletRequestUtils.getStringParameter(request,"remark","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主ID为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
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
        //单位
        if(type.equals("1")) {
            if(StringUtils.isEmpty(bankName)){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的开户银行为空",null);
            }
            if(StringUtils.isEmpty(bankNumber)){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的开户帐号为空",null);
            }
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
        TcInvoiceTemplate tiTemplate=new TcInvoiceTemplate();
        tiTemplate.setId(id);
        tiTemplate.setType(Integer.valueOf(type));
        tiTemplate.setTaxNum(taxNum);
        tiTemplate.setTitle(title);
        tiTemplate.setIdNumber(idNumber);
        tiTemplate.setEmail(email);
        tiTemplate.setContact(contact);
        tiTemplate.setProvince(province);
        tiTemplate.setCity(city);
        tiTemplate.setCounty(county);
        tiTemplate.setAddress(address);
        tiTemplate.setBankName(bankName);
        tiTemplate.setBankNumber(bankNumber);
        tiTemplate.setPhone(phone);
        tiTemplate.setRemark(remark);
        return tcInvoiceTemplateService.update(tiTemplate);
    }


    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateIsDefault")
    @ResponseBody
    @OperLog(operationType="发票模板",operationName="修改")
    public ReturnJsonData updateIsDefault(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
           if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主ID为空",null);
        }
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        TcInvoiceTemplate tiTemplate=new TcInvoiceTemplate();
        tiTemplate.setId(id);
        tiTemplate.setExtendId(Integer.valueOf(extendId));
        return tcInvoiceTemplateService.updateIsDefault(tiTemplate);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="发票模板",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcInvoiceTemplateService.delete(strarray);
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
    @OperLog(operationType="发票模板",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcInvoiceTemplateService.getById(id);
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
    @OperLog(operationType="发票模板",operationName="列表查询")
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
        return tcInvoiceTemplateService.getList(condition,pageHelper);
    }

    /**
     * 后台列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    @ResponseBody
    @OperLog(operationType="发票模板",operationName="列表查询")
    public ReturnJsonData list(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
        return tcInvoiceTemplateService.getList(condition,pageHelper);
    }
}
