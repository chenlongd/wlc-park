package com.perenc.xh.lsp.controller.admin.tcOrder;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcOrder.TcOrderInvoice;
import com.perenc.xh.lsp.service.tcOrderInvoice.TcOrderInvoiceService;
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
@RequestMapping("orderInvoice")
public class AdminTcOrderInvoiceController {

    @Autowired(required = false)
    private TcOrderInvoiceService tcOrderInvoiceService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="订单发票",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String orderId = ServletRequestUtils.getStringParameter(request,"orderId","");
        String invoiceId = ServletRequestUtils.getStringParameter(request,"invoiceId","");
        if(StringUtils.isEmpty(orderId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的订单ID为空",null);
        }
        if(StringUtils.isEmpty(invoiceId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发票ID为空",null);
        }
        TcOrderInvoice tcOrderInvoice=new TcOrderInvoice();
        tcOrderInvoice.setOrderId(Integer.valueOf(orderId));
        tcOrderInvoice.setInvoiceId(invoiceId);
        return tcOrderInvoiceService.insert(tcOrderInvoice);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="订单发票",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String orderId = ServletRequestUtils.getStringParameter(request,"orderId","");
        String invoiceId = ServletRequestUtils.getStringParameter(request,"invoiceId","");
        if(StringUtils.isEmpty(orderId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的订单ID为空",null);
        }
        if(StringUtils.isEmpty(invoiceId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发票ID为空",null);
        }
        TcOrderInvoice tcOrderInvoice=new TcOrderInvoice();
        tcOrderInvoice.setId(id);
        tcOrderInvoice.setOrderId(Integer.valueOf(orderId));
        tcOrderInvoice.setInvoiceId(invoiceId);
        return tcOrderInvoiceService.update(tcOrderInvoice);
    }


    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateFile")
    @ResponseBody
    @OperLog(operationType="发票",operationName="修改")
    public ReturnJsonData updateFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String invoiceImg = ServletRequestUtils.getStringParameter(request,"invoiceImg","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主ID为空",null);
        }
        if(StringUtils.isEmpty(invoiceImg)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发票图片为空",null);
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("invoiceImg",invoiceImg);
        TcOrderInvoice tcOrderInvoice=new TcOrderInvoice();
        tcOrderInvoice.setId(id);
        return tcOrderInvoiceService.updateFile(tcOrderInvoice,condition);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="订单发票",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcOrderInvoiceService.delete(strarray);
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
    @OperLog(operationType="订单发票",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcOrderInvoiceService.getById(id);
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
    @OperLog(operationType="订单发票",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String invoiceId = ServletRequestUtils.getStringParameter(request, "invoiceId", "");
        if (StringUtils.isNotEmpty(invoiceId)) {
            condition.put("invoiceId", invoiceId);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcOrderInvoiceService.getList(condition,pageHelper);
    }
}
