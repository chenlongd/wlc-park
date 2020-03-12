package com.perenc.xh.lsp.controller.admin.tcCoupon;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcCoupon.TcCoupon;
import com.perenc.xh.lsp.service.tcCoupon.TcCouponService;
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
@RequestMapping("coupon")
public class AdminTcCouponController {

    @Autowired(required = false)
    private TcCouponService tcCouponService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="停车券",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String useType = ServletRequestUtils.getStringParameter(request,"useType","");
        String desc = ServletRequestUtils.getStringParameter(request,"desc","");
        String duration = ServletRequestUtils.getStringParameter(request,"duration","");
        String amount = ServletRequestUtils.getStringParameter(request,"amount","");
        String fullAmount = ServletRequestUtils.getStringParameter(request,"fullAmount","");
        String sdate = ServletRequestUtils.getStringParameter(request,"sdate","");
        String edate = ServletRequestUtils.getStringParameter(request,"edate","");
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的名称为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(useType)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的使用类型为空",null);
        }
        if(StringUtils.isEmpty(desc)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的描述为空",null);
        }
        if(StringUtils.isEmpty(duration)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车小时数为空",null);
        }
        //判断金额
        if(StringUtils.isNotEmpty(amount)) {
            if (!ValidateUtils.isAmount(amount)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请输入正确的金额", null);
            }
        }
        if(StringUtils.isNotEmpty(fullAmount)){
            //判断满减金额
            if(!ValidateUtils.isAmount(fullAmount)){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的满减金额",null);
            }
        }
        TcCoupon tcCoupon=new TcCoupon();
        tcCoupon.setName(name);
        tcCoupon.setType(Integer.valueOf(type));
        tcCoupon.setUseType(Integer.valueOf(useType));
        tcCoupon.setDesc(desc);
        tcCoupon.setDuration(Integer.valueOf(duration));
        tcCoupon.setAmount(ToolUtil.getDoubleToInt(Double.valueOf(amount)));
        if(StringUtils.isNotEmpty(fullAmount)){
            tcCoupon.setAmount(ToolUtil.getDoubleToInt(Double.valueOf(fullAmount)));
        }
        tcCoupon.setSdate(sdate);
        tcCoupon.setEdate(edate);
        return tcCouponService.insert(tcCoupon);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="停车券",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String useType = ServletRequestUtils.getStringParameter(request,"useType","3");
        String desc = ServletRequestUtils.getStringParameter(request,"desc","");
        String duration = ServletRequestUtils.getStringParameter(request,"duration","");
        String amount = ServletRequestUtils.getStringParameter(request,"amount","");
        String fullAmount = ServletRequestUtils.getStringParameter(request,"fullAmount","");
        String sdate = ServletRequestUtils.getStringParameter(request,"sdate","");
        String edate = ServletRequestUtils.getStringParameter(request,"edate","");
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的名称为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(useType)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的使用类型为空",null);
        }
        if(StringUtils.isEmpty(desc)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的描述为空",null);
        }
        if(StringUtils.isEmpty(duration)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车小时数为空",null);
        }
        //判断金额
        if(StringUtils.isNotEmpty(amount)) {
            if (!ValidateUtils.isAmount(amount)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请输入正确的金额", null);
            }
        }
        if(StringUtils.isNotEmpty(fullAmount)){
            //判断满减金额
            if(!ValidateUtils.isAmount(fullAmount)){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的满减金额",null);
            }
        }
        TcCoupon tcCoupon=new TcCoupon();
        tcCoupon.setId(id);
        tcCoupon.setName(name);
        tcCoupon.setType(Integer.valueOf(type));
        tcCoupon.setUseType(Integer.valueOf(useType));
        tcCoupon.setDesc(desc);
        tcCoupon.setDuration(Integer.valueOf(duration));
        tcCoupon.setAmount(ToolUtil.getDoubleToInt(Double.valueOf(amount)));
        if(StringUtils.isNotEmpty(fullAmount)){
            tcCoupon.setAmount(ToolUtil.getDoubleToInt(Double.valueOf(fullAmount)));
        }
        tcCoupon.setSdate(sdate);
        tcCoupon.setEdate(edate);
        return tcCouponService.update(tcCoupon);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateIsEnabled")
    @ResponseBody
    @OperLog(operationType="会员",operationName="修改")
    public ReturnJsonData updateIsEnabled(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String isEnabled = ServletRequestUtils.getStringParameter(request,"isEnabled","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主ID为空",null);
        }
        if(StringUtils.isEmpty(isEnabled)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的启用状态为空",null);
        }
        TcCoupon tcCoupon=new TcCoupon();
        tcCoupon.setId(id);
        tcCoupon.setIsEnabled(Integer.valueOf(isEnabled));
        return tcCouponService.updateIsEnabled(tcCoupon);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="停车券",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcCouponService.delete(strarray);
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
    @OperLog(operationType="停车券",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcCouponService.getById(id);
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
    @OperLog(operationType="停车券",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String name = ServletRequestUtils.getStringParameter(request, "name", "");
        if (StringUtils.isNotEmpty(name)) {
            condition.put("name", name);
        }
        String type = ServletRequestUtils.getStringParameter(request, "type", "");
        if (StringUtils.isNotEmpty(type)) {
            condition.put("type", type);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcCouponService.getList(condition,pageHelper);
    }

    /**
     * 后台列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    @ResponseBody
    @OperLog(operationType="停车券",operationName="列表查询")
    public ReturnJsonData list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String name = ServletRequestUtils.getStringParameter(request, "name", "");
        if (StringUtils.isNotEmpty(name)) {
            condition.put("name", name);
        }
        String type = ServletRequestUtils.getStringParameter(request, "type", "");
        if (StringUtils.isNotEmpty(type)) {
            condition.put("type", type);
        }
        return tcCouponService.getAllList(condition);
    }


}
