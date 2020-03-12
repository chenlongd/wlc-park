package com.perenc.xh.lsp.controller.admin.tcIntegral;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegralConvertrule;
import com.perenc.xh.lsp.service.tcIntegralConvertrle.TcIntegralConvertruleService;
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
@RequestMapping("integralConvertrule")
public class AdminTcIntegralConvertruleController {

    @Autowired(required = false)
    private TcIntegralConvertruleService tcIntegralConvertruleService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="积分兑换规则",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String couponId = ServletRequestUtils.getStringParameter(request,"couponId","");
        String cdays = ServletRequestUtils.getStringParameter(request,"cdays","");
        String descp = ServletRequestUtils.getStringParameter(request,"descp","");
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String ratio = ServletRequestUtils.getStringParameter(request,"ratio","");
        String formula = ServletRequestUtils.getStringParameter(request,"formula","");
        String number = ServletRequestUtils.getStringParameter(request,"number","");
        String limitNumber = ServletRequestUtils.getStringParameter(request,"limitNumber","");
        String sdate = ServletRequestUtils.getStringParameter(request,"sdate","");
        String edate = ServletRequestUtils.getStringParameter(request,"edate","");
        String remark = ServletRequestUtils.getStringParameter(request,"remark","");
        if(StringUtils.isEmpty(couponId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的票券为空",null);
        }
        if(StringUtils.isEmpty(cdays)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的票券有效期天数为空",null);
        }
        if(StringUtils.isEmpty(descp)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的兑换描述为空",null);
        }
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的名称为空",null);
        }
        if(StringUtils.isEmpty(ratio)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的兑换比例为空",null);
        }
        if(StringUtils.isEmpty(formula)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的兑换公式为空",null);
        }
        if(StringUtils.isEmpty(number)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的可兑换数量为空",null);
        }
        if(StringUtils.isEmpty(limitNumber)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的兑换限制数量为空",null);
        }
        if(StringUtils.isEmpty(sdate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的开始时间为空",null);
        }
        if(StringUtils.isEmpty(edate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的结束时间为空",null);
        }
        TcIntegralConvertrule tcConvertrule=new TcIntegralConvertrule();
        tcConvertrule.setCouponId(couponId);
        tcConvertrule.setCdays(Integer.valueOf(cdays));
        tcConvertrule.setDescp(descp);
        tcConvertrule.setName(name);
        tcConvertrule.setRatio(Integer.valueOf(ratio));
        tcConvertrule.setFormula(formula);
        tcConvertrule.setNumber(Integer.valueOf(number));
        tcConvertrule.setLimitNumber(Integer.valueOf(limitNumber));
        tcConvertrule.setSdate(sdate);
        tcConvertrule.setEdate(edate);
        tcConvertrule.setRemark(remark);
        return tcIntegralConvertruleService.insert(tcConvertrule);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="积分兑换规则",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String couponId = ServletRequestUtils.getStringParameter(request,"couponId","");
        String cdays = ServletRequestUtils.getStringParameter(request,"cdays","");
        String descp = ServletRequestUtils.getStringParameter(request,"descp","");
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String ratio = ServletRequestUtils.getStringParameter(request,"ratio","");
        String formula = ServletRequestUtils.getStringParameter(request,"formula","");
        String number = ServletRequestUtils.getStringParameter(request,"number","");
        String limitNumber = ServletRequestUtils.getStringParameter(request,"limitNumber","");
        String sdate = ServletRequestUtils.getStringParameter(request,"sdate","");
        String edate = ServletRequestUtils.getStringParameter(request,"edate","");
        String remark = ServletRequestUtils.getStringParameter(request,"remark","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主键ID为空",null);
        }
        if(StringUtils.isEmpty(couponId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的票券为空",null);
        }
        if(StringUtils.isEmpty(cdays)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的票券有效期天数为空",null);
        }
        if(StringUtils.isEmpty(descp)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的兑换描述为空",null);
        }
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的名称为空",null);
        }
        if(StringUtils.isEmpty(ratio)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的兑换比例为空",null);
        }
        if(StringUtils.isEmpty(formula)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的兑换公式为空",null);
        }
        if(StringUtils.isEmpty(number)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的可兑换数量为空",null);
        }
        if(StringUtils.isEmpty(limitNumber)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的兑换限制数量为空",null);
        }
        if(StringUtils.isEmpty(sdate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的开始时间为空",null);
        }
        if(StringUtils.isEmpty(edate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的结束时间为空",null);
        }
        TcIntegralConvertrule tcConvertrule=new TcIntegralConvertrule();
        tcConvertrule.setId(id);
        tcConvertrule.setCouponId(couponId);
        tcConvertrule.setCdays(Integer.valueOf(cdays));
        tcConvertrule.setDescp(descp);
        tcConvertrule.setName(name);
        tcConvertrule.setRatio(Integer.valueOf(ratio));
        tcConvertrule.setFormula(formula);
        tcConvertrule.setNumber(Integer.valueOf(number));
        tcConvertrule.setLimitNumber(Integer.valueOf(limitNumber));
        tcConvertrule.setSdate(sdate);
        tcConvertrule.setEdate(edate);
        tcConvertrule.setRemark(remark);
        return tcIntegralConvertruleService.update(tcConvertrule);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateIsEnabled")
    @ResponseBody
    @OperLog(operationType="积分兑换规则",operationName="修改")
    public ReturnJsonData updateIsEnabled(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String isEnabled = ServletRequestUtils.getStringParameter(request,"isEnabled","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主ID为空",null);
        }
        if(StringUtils.isEmpty(isEnabled)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的启用状态为空",null);
        }
        TcIntegralConvertrule tcConvertrule=new TcIntegralConvertrule();
        tcConvertrule.setId(id);
        tcConvertrule.setIsEnabled(Integer.valueOf(isEnabled));
        return tcIntegralConvertruleService.updateIsEnabled(tcConvertrule);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="积分兑换规则",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcIntegralConvertruleService.delete(strarray);
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
    @OperLog(operationType="积分兑换规则",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcIntegralConvertruleService.getById(id);
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
    @OperLog(operationType="积分兑换规则",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String name = ServletRequestUtils.getStringParameter(request, "name", "");
        if (StringUtils.isNotEmpty(name)) {
            condition.put("name", name);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcIntegralConvertruleService.getList(condition,pageHelper);
    }
}
