package com.perenc.xh.lsp.controller.admin.tcSeller;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerCouponsetup;
import com.perenc.xh.lsp.service.tcSellerCouponsetup.TcSellerCouponsetupService;
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
@RequestMapping("sellerCouponsetup")
public class AdminTcSellerCouponsetupController {

    @Autowired(required = false)
    private TcSellerCouponsetupService tcSellerCouponsetupService;

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
        String couponId = ServletRequestUtils.getStringParameter(request,"couponId","");
        String sellerId = ServletRequestUtils.getStringParameter(request,"sellerId","");
        String limitNumber = ServletRequestUtils.getStringParameter(request,"limitNumber","");
        if(StringUtils.isEmpty(couponId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的票券ID为空",null);
        }
        if(StringUtils.isEmpty(sellerId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家ID为空",null);
        }
        if(StringUtils.isEmpty(limitNumber)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的张数为空",null);
        }
        //判断张数
        if(!ValidateUtils.isNumber(limitNumber)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的张数",null);
        }
        TcSellerCouponsetup tcSellerCouponsetup=new TcSellerCouponsetup();
        tcSellerCouponsetup.setCouponId(couponId);
        tcSellerCouponsetup.setSellerId(Integer.valueOf(sellerId));
        tcSellerCouponsetup.setLimitNumber(Integer.valueOf(limitNumber));
        tcSellerCouponsetup.setSdate(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcSellerCouponsetup.setEdate(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        return tcSellerCouponsetupService.insert(tcSellerCouponsetup);
    }

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
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String couponId = ServletRequestUtils.getStringParameter(request,"couponId","");
        String sellerId = ServletRequestUtils.getStringParameter(request,"sellerId","");
        String limitNumber = ServletRequestUtils.getStringParameter(request,"limitNumber","");
        if(StringUtils.isEmpty(couponId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的票券ID为空",null);
        }
        if(StringUtils.isEmpty(sellerId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家ID为空",null);
        }
        if(StringUtils.isEmpty(limitNumber)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的张数为空",null);
        }
        //判断张数
        if(!ValidateUtils.isNumber(limitNumber)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的张数",null);
        }
        TcSellerCouponsetup tcSellerCouponsetup=new TcSellerCouponsetup();
        tcSellerCouponsetup.setId(id);
        tcSellerCouponsetup.setCouponId(couponId);
        tcSellerCouponsetup.setSellerId(Integer.valueOf(sellerId));
        tcSellerCouponsetup.setLimitNumber(Integer.valueOf(limitNumber));
        return tcSellerCouponsetupService.update(tcSellerCouponsetup);
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
                return tcSellerCouponsetupService.delete(strarray);
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
            return tcSellerCouponsetupService.getById(id);
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
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcSellerCouponsetupService.getList(condition,pageHelper);
    }
}
