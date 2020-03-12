package com.perenc.xh.lsp.controller.admin.tcSeller;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerFreecar;
import com.perenc.xh.lsp.service.tcSellerFreecar.TcSellerFreecarService;
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
@RequestMapping("sellerFreecar")
public class AdminTcSellerFreecarController {

    @Autowired(required = false)
    private TcSellerFreecarService tcSellerFreecarService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="商家指定免费车",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String carNum = ServletRequestUtils.getStringParameter(request,"carNum","");
        String sdate = ServletRequestUtils.getStringParameter(request,"sdate","");
        String edate = ServletRequestUtils.getStringParameter(request,"edate","");
        String ticketImg = ServletRequestUtils.getStringParameter(request,"ticketImg","");
        if(StringUtils.isEmpty(carNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车牌号为空",null);
        }
        if(StringUtils.isEmpty(sdate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的免费开始时间为空",null);
        }
        if(StringUtils.isEmpty(edate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的免费结束时间为空",null);
        }
        if(StringUtils.isEmpty(ticketImg)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的凭证图片为空",null);
        }
        if(!ValidateUtils.isCarnumberNO(carNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的车牌号",null);
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("carNum",carNum);
        TcSellerFreecar tcSellerFreecar=new TcSellerFreecar();
        tcSellerFreecar.setSellerId(null);
        tcSellerFreecar.setSdate(sdate);
        tcSellerFreecar.setEdate(edate);
        tcSellerFreecar.setTicketImg(ticketImg);
        return tcSellerFreecarService.insertFreecar(tcSellerFreecar,condition);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="商家指定免费车",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String sellerId = ServletRequestUtils.getStringParameter(request,"sellerId","");
        String carId = ServletRequestUtils.getStringParameter(request,"carId","");
        String sdate = ServletRequestUtils.getStringParameter(request,"sdate","");
        String edate = ServletRequestUtils.getStringParameter(request,"edate","");
        String ticketImg = ServletRequestUtils.getStringParameter(request,"ticketImg","");
        if(StringUtils.isEmpty(sellerId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家ID为空",null);
        }
        if(StringUtils.isEmpty(carId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID为空",null);
        }
        if(StringUtils.isEmpty(sdate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的免费开始时间为空",null);
        }
        if(StringUtils.isEmpty(edate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的免费结束时间为空",null);
        }
        if(StringUtils.isEmpty(ticketImg)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的凭证图片为空",null);
        }
        TcSellerFreecar tcSellerFreecar=new TcSellerFreecar();
        tcSellerFreecar.setId(id);
        tcSellerFreecar.setSellerId(Integer.valueOf(sellerId));
        tcSellerFreecar.setCarId(carId);
        tcSellerFreecar.setSdate(sdate);
        tcSellerFreecar.setEdate(edate);
        tcSellerFreecar.setTicketImg(ticketImg);
        return tcSellerFreecarService.update(tcSellerFreecar);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="商家指定免费车",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        //把String转换成数据
        if(StringUtils.isNotEmpty(id)){
            return tcSellerFreecarService.deleteFreecar(id);
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
    @OperLog(operationType="商家指定免费车",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcSellerFreecarService.getById(id);
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
    @OperLog(operationType="商家指定免费车",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String sellerId = ServletRequestUtils.getStringParameter(request, "sellerId", "");
        if (StringUtils.isNotEmpty(sellerId)) {
            condition.put("sellerId", sellerId);
        }
        String sellerName = ServletRequestUtils.getStringParameter(request, "sellerName", "");
        if (StringUtils.isNotEmpty(sellerName)) {
            condition.put("sellerName", sellerName);
        }
        String carNum = ServletRequestUtils.getStringParameter(request, "carNum", "");
        if (StringUtils.isNotEmpty(carNum)) {
            condition.put("carNum", carNum);
        }
        String useStatus = ServletRequestUtils.getStringParameter(request, "useStatus", "");
        if (StringUtils.isNotEmpty(useStatus)) {
            condition.put("useStatus", useStatus);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcSellerFreecarService.getList(condition,pageHelper);
    }
}
