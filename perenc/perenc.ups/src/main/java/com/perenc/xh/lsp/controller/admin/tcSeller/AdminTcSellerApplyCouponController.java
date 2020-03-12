package com.perenc.xh.lsp.controller.admin.tcSeller;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplycoupon;
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
@RequestMapping("sellerApplycoupon")
public class AdminTcSellerApplyCouponController {

    @Autowired(required = false)
    private TcSellerApplycouponService tcSellerApplycouponService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="商家申请停车券明细",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String couponId = ServletRequestUtils.getStringParameter(request,"couponId","");
        String sellerId = ServletRequestUtils.getStringParameter(request,"sellerId","");
        String number = ServletRequestUtils.getStringParameter(request,"number","");
        if(StringUtils.isEmpty(couponId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车券ID为空",null);
        }
        if(StringUtils.isEmpty(sellerId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家ID为空",null);
        }
        if(StringUtils.isEmpty(number)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的张数为空",null);
        }

        TcSellerApplycoupon tcSellerApplycoupon=new TcSellerApplycoupon();
        tcSellerApplycoupon.setCouponId(couponId);
        tcSellerApplycoupon.setSellerId(Integer.valueOf(sellerId));
        tcSellerApplycoupon.setNumber(Integer.valueOf(number));
        tcSellerApplycoupon.setSdate(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcSellerApplycoupon.setEdate(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcSellerApplycoupon.setIsApproval(1);
        return tcSellerApplycouponService.insert(tcSellerApplycoupon);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="商家申请停车券明细",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String couponId = ServletRequestUtils.getStringParameter(request,"couponId","");
        String sellerId = ServletRequestUtils.getStringParameter(request,"sellerId","");
        String number = ServletRequestUtils.getStringParameter(request,"number","");
        if(StringUtils.isEmpty(couponId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车券ID为空",null);
        }
        if(StringUtils.isEmpty(sellerId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家ID为空",null);
        }
        if(StringUtils.isEmpty(number)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的张数为空",null);
        }
        TcSellerApplycoupon tcSellerApplycoupon=new TcSellerApplycoupon();
        tcSellerApplycoupon.setId(id);
        tcSellerApplycoupon.setCouponId(couponId);
        tcSellerApplycoupon.setSellerId(Integer.valueOf(sellerId));
        tcSellerApplycoupon.setNumber(Integer.valueOf(number));
        return tcSellerApplycouponService.update(tcSellerApplycoupon);
    }

    /**
     * 商家申请停车券审批
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateIsApproval")
    @ResponseBody
    @OperLog(operationType="商家申请停车券明细",operationName="审核")
    public ReturnJsonData updateIsApproval(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String isApproval = ServletRequestUtils.getStringParameter(request,"isApproval","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的Id为空",null);
        }
        if(StringUtils.isEmpty(isApproval)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的审核状态为空",null);
        }
        TcSellerApplycoupon tcSellerApplycoupon=new TcSellerApplycoupon();
        tcSellerApplycoupon.setId(id);
        tcSellerApplycoupon.setIsApproval(Integer.valueOf(isApproval));
        return tcSellerApplycouponService.updateIsApproval(tcSellerApplycoupon);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="商家申请停车券明细",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcSellerApplycouponService.delete(strarray);
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
    @OperLog(operationType="商家申请停车券明细",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
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
    @OperLog(operationType="商家申请停车券明细",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
        return tcSellerApplycouponService.getList(condition,pageHelper);
    }
}
