package com.perenc.xh.lsp.controller.admin.tcSeller;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplyc;
import com.perenc.xh.lsp.service.tcSellerApplyc.TcSellerApplycService;
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
@RequestMapping("sellerApplyc")
public class AdminTcSellerApplycController {

    @Autowired(required = false)
    private TcSellerApplycService tcSellerApplycService;


    /**
     * 商家申请停车券审批
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateIsApproval")
    @ResponseBody
    @OperLog(operationType="商家申请停车券",operationName="审核")
    public ReturnJsonData updateIsApproval(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String isApproval = ServletRequestUtils.getStringParameter(request,"isApproval","");
        String couponId = ServletRequestUtils.getStringParameter(request,"couponId","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID为空",null);
        }
        if(StringUtils.isEmpty(isApproval)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的审核状态为空",null);
        }
        /*if(StringUtils.isEmpty(couponId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的票券ID为空",null);
        }*/
        Map<String, Object> condition = new HashMap<>();
        condition.put("couponId",couponId);
        TcSellerApplyc tcSellerApplyc =new TcSellerApplyc();
        tcSellerApplyc.setId(id);
        tcSellerApplyc.setIsApproval(Integer.valueOf(isApproval));
        return tcSellerApplycService.updateIsApprovalApply(tcSellerApplyc,condition);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="商家申请停车券",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcSellerApplycService.delete(strarray);
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
    @OperLog(operationType="商家申请停车券",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
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
        Map<String, Object> condition = new HashMap<>();
        String sellerId = ServletRequestUtils.getStringParameter(request, "sellerId", "");
        if (StringUtils.isNotEmpty(sellerId)) {
            condition.put("sellerId", sellerId);
        }
        String sellerName = ServletRequestUtils.getStringParameter(request, "sellerName", "");
        if (StringUtils.isNotEmpty(sellerName)) {
            condition.put("sellerName", sellerName);
        }
        String isApproval = ServletRequestUtils.getStringParameter(request, "isApproval", "");
        if (StringUtils.isNotEmpty(isApproval)) {
            condition.put("isApproval", isApproval);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcSellerApplycService.getAdminList(condition,pageHelper);
    }
}
