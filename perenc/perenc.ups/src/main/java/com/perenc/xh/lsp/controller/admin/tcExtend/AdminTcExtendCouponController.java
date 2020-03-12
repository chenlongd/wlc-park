package com.perenc.xh.lsp.controller.admin.tcExtend;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCoupon;
import com.perenc.xh.lsp.service.tcExtendCoupon.TcExtendCouponService;
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
@RequestMapping("extendCoupon")
public class AdminTcExtendCouponController {

    @Autowired(required = false)
    private TcExtendCouponService tcExtendCouponService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="客户优惠券",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String couponId = ServletRequestUtils.getStringParameter(request,"couponId","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String sellerId = ServletRequestUtils.getStringParameter(request,"sellerId","");
        if(StringUtils.isEmpty(couponId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车券ID为空",null);
        }
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(StringUtils.isEmpty(sellerId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家ID为空",null);
        }
        TcExtendCoupon tcExtendCoupon=new TcExtendCoupon();
        tcExtendCoupon.setCouponId(couponId);
        tcExtendCoupon.setExtendId(Integer.valueOf(extendId));
        tcExtendCoupon.setSellerId(Integer.valueOf(sellerId));
        tcExtendCoupon.setSdate(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcExtendCoupon.setEdate(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcExtendCoupon.setUseStatus(1);
        return tcExtendCouponService.insert(tcExtendCoupon);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="客户优惠券",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String couponId = ServletRequestUtils.getStringParameter(request,"couponId","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String sellerId = ServletRequestUtils.getStringParameter(request,"sellerId","");
        if(StringUtils.isEmpty(couponId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车券ID为空",null);
        }
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(StringUtils.isEmpty(sellerId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家ID为空",null);
        }
        TcExtendCoupon tcExtendCoupon=new TcExtendCoupon();
        tcExtendCoupon.setId(id);
        tcExtendCoupon.setCouponId(couponId);
        tcExtendCoupon.setExtendId(Integer.valueOf(extendId));
        tcExtendCoupon.setSellerId(Integer.valueOf(sellerId));
        return tcExtendCouponService.update(tcExtendCoupon);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="客户优惠券",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcExtendCouponService.delete(strarray);
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
    @OperLog(operationType="客户优惠券",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcExtendCouponService.getById(id);
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
    @OperLog(operationType="客户优惠券",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String phone = ServletRequestUtils.getStringParameter(request, "phone", "");
        if (StringUtils.isNotEmpty(phone)) {
            condition.put("phone", phone);
        }
        //商家名称
        String sellerName = ServletRequestUtils.getStringParameter(request, "sellerName", "");
        if (StringUtils.isNotEmpty(sellerName)) {
            condition.put("sellerName", sellerName);
        }
        String carNum = ServletRequestUtils.getStringParameter(request, "carNum", "");
        if (StringUtils.isNotEmpty(carNum)) {
            condition.put("carNum", carNum);
        }
        //客户ID
        String extendId = ServletRequestUtils.getStringParameter(request, "extendId", "");
        if (StringUtils.isNotEmpty(extendId)) {
            condition.put("extendId", extendId);
        }
        //车辆ID
        String carId = ServletRequestUtils.getStringParameter(request, "carId", "");
        if (StringUtils.isNotEmpty(carId)) {
            condition.put("carId", carId);
        }
        //商家ID
        String sellerId = ServletRequestUtils.getStringParameter(request, "sellerId", "");
        if (StringUtils.isNotEmpty(sellerId)) {
            condition.put("sellerId", sellerId);
        }
        //类型
        String type = ServletRequestUtils.getStringParameter(request, "type", "");
        if (StringUtils.isNotEmpty(type)) {
            condition.put("type", type);
        }
        //使用状态
        String useStatus = ServletRequestUtils.getStringParameter(request, "useStatus", "");
        if (StringUtils.isNotEmpty(useStatus)) {
            condition.put("useStatus", useStatus);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcExtendCouponService.getList(condition,pageHelper);
    }
}
