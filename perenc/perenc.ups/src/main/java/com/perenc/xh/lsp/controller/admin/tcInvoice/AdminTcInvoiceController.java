package com.perenc.xh.lsp.controller.admin.tcInvoice;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
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


@Controller
@RequestMapping("invoice")
public class AdminTcInvoiceController {

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
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String taxNum = ServletRequestUtils.getStringParameter(request,"taxNum","");
        String title = ServletRequestUtils.getStringParameter(request,"title","");
        String email = ServletRequestUtils.getStringParameter(request,"email","");
        String contact = ServletRequestUtils.getStringParameter(request,"contact","");
        String address = ServletRequestUtils.getStringParameter(request,"address","");
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        String invoiceNum = ServletRequestUtils.getStringParameter(request,"invoiceNum","");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(StringUtils.isEmpty(taxNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的税号为空",null);
        }
        if(StringUtils.isEmpty(title)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发票抬头为空",null);
        }
        if(StringUtils.isEmpty(email)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的邮箱为空",null);
        }
        if(StringUtils.isEmpty(contact)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的联系人为空",null);
        }
        if(StringUtils.isEmpty(address)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的地址为空",null);
        }
        if(StringUtils.isEmpty(phone)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的电话为空",null);
        }
        TcInvoice tcInvoice=new TcInvoice();
        tcInvoice.setExtendId(Integer.valueOf(extendId));
        tcInvoice.setTaxNum(taxNum);
        tcInvoice.setTitle(title);
        tcInvoice.setEmail(email);
        tcInvoice.setContact(contact);
        tcInvoice.setAddress(address);
        tcInvoice.setPhone(phone);
        tcInvoice.setInvoiceNum(invoiceNum);
        tcInvoice.setIsInvoice(1);//是否开发票状态
        return tcInvoiceService.insert(tcInvoice);
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
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String taxNum = ServletRequestUtils.getStringParameter(request,"taxNum","");
        String title = ServletRequestUtils.getStringParameter(request,"title","");
        String email = ServletRequestUtils.getStringParameter(request,"email","");
        String contact = ServletRequestUtils.getStringParameter(request,"contact","");
        String address = ServletRequestUtils.getStringParameter(request,"address","");
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        String invoiceNum = ServletRequestUtils.getStringParameter(request,"invoiceNum","");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(StringUtils.isEmpty(taxNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的税号为空",null);
        }
        if(StringUtils.isEmpty(title)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发票抬头为空",null);
        }
        if(StringUtils.isEmpty(email)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的邮箱为空",null);
        }
        if(StringUtils.isEmpty(contact)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的联系人为空",null);
        }
        if(StringUtils.isEmpty(address)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的地址为空",null);
        }
        if(StringUtils.isEmpty(phone)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的电话为空",null);
        }
        TcInvoice tcInvoice=new TcInvoice();
        tcInvoice.setId(id);
        tcInvoice.setExtendId(Integer.valueOf(extendId));
        tcInvoice.setTaxNum(taxNum);
        tcInvoice.setTitle(title);
        tcInvoice.setEmail(email);
        tcInvoice.setContact(contact);
        tcInvoice.setAddress(address);
        tcInvoice.setPhone(phone);
        tcInvoice.setInvoiceNum(invoiceNum);
        //tcInvoice.setIsInvoice(1);//是否开发票状态
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
