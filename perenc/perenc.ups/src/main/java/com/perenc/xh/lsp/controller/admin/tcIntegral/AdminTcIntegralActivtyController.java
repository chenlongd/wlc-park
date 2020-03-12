package com.perenc.xh.lsp.controller.admin.tcIntegral;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegralActivty;
import com.perenc.xh.lsp.service.tcIntegralActivty.TcIntegralActivtyService;
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
@RequestMapping("integralActivty")
public class AdminTcIntegralActivtyController {

    @Autowired(required = false)
    private TcIntegralActivtyService tcIntegralActivtyService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="积分活动",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String descp = ServletRequestUtils.getStringParameter(request,"descp","");
        String ratio = ServletRequestUtils.getStringParameter(request,"ratio","");
        String number = ServletRequestUtils.getStringParameter(request,"number","");
        String sdate = ServletRequestUtils.getStringParameter(request,"sdate","");
        String edate = ServletRequestUtils.getStringParameter(request,"edate","");
        String couponId = ServletRequestUtils.getStringParameter(request,"couponId","");
        String cdays = ServletRequestUtils.getStringParameter(request,"cdays","");
        String remark = ServletRequestUtils.getStringParameter(request,"remark","");
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的名称为空",null);
        }
        if(StringUtils.isEmpty(descp)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的简介为空",null);
        }
        TcIntegralActivty tcIntegralActivty = new TcIntegralActivty();
        tcIntegralActivty.setType(Integer.valueOf(type));
        tcIntegralActivty.setName(name);
        tcIntegralActivty.setDescp(descp);
        tcIntegralActivty.setRatio(Integer.valueOf(ratio));
        tcIntegralActivty.setNumber(Integer.valueOf(number));
        tcIntegralActivty.setSdate(sdate);
        tcIntegralActivty.setEdate(edate);
        tcIntegralActivty.setCouponId(couponId);
        tcIntegralActivty.setCdays(Integer.valueOf(cdays));
        tcIntegralActivty.setRemark(remark);
        return tcIntegralActivtyService.insert(tcIntegralActivty);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="积分活动",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String descp = ServletRequestUtils.getStringParameter(request,"descp","");
        String ratio = ServletRequestUtils.getStringParameter(request,"ratio","");
        String number = ServletRequestUtils.getStringParameter(request,"number","");
        String sdate = ServletRequestUtils.getStringParameter(request,"sdate","");
        String edate = ServletRequestUtils.getStringParameter(request,"edate","");
        String couponId = ServletRequestUtils.getStringParameter(request,"couponId","");
        String cdays = ServletRequestUtils.getStringParameter(request,"cdays","");
        String remark = ServletRequestUtils.getStringParameter(request,"remark","");
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的名称为空",null);
        }
        if(StringUtils.isEmpty(descp)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的简介为空",null);
        }
        TcIntegralActivty tcIntegralActivty = new TcIntegralActivty();
        tcIntegralActivty.setId(id);
        tcIntegralActivty.setType(Integer.valueOf(type));
        tcIntegralActivty.setName(name);
        tcIntegralActivty.setDescp(descp);
        tcIntegralActivty.setRatio(Integer.valueOf(ratio));
        tcIntegralActivty.setNumber(Integer.valueOf(number));
        tcIntegralActivty.setSdate(sdate);
        tcIntegralActivty.setEdate(edate);
        tcIntegralActivty.setCouponId(couponId);
        tcIntegralActivty.setCdays(Integer.valueOf(cdays));
        tcIntegralActivty.setRemark(remark);
        return tcIntegralActivtyService.update(tcIntegralActivty);
    }


    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateIsEnabled")
    @ResponseBody
    @OperLog(operationType="积分活动",operationName="修改")
    public ReturnJsonData updateIsEnabled(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String isEnabled = ServletRequestUtils.getStringParameter(request,"isEnabled","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主ID为空",null);
        }
        if(StringUtils.isEmpty(isEnabled)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的启用状态为空",null);
        }
        TcIntegralActivty tcIntegralActivty = new TcIntegralActivty();
        tcIntegralActivty.setId(id);
        tcIntegralActivty.setIsEnabled(Integer.valueOf(isEnabled));
        return tcIntegralActivtyService.updateIsEnabled(tcIntegralActivty);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="积分活动",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcIntegralActivtyService.delete(strarray);
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
    @OperLog(operationType="积分活动",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcIntegralActivtyService.getById(id);
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
    @OperLog(operationType="积分活动",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String type = ServletRequestUtils.getStringParameter(request, "type", "");
        if (StringUtils.isNotEmpty(type)) {
            condition.put("type", type);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcIntegralActivtyService.getList(condition,pageHelper);
    }
}
