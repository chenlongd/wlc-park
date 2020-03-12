package com.perenc.xh.lsp.controller.admin.tcParklot;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcParklot.TcCostRule;
import com.perenc.xh.lsp.service.tcCostRule.TcCostRuleService;
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
@RequestMapping("costRule")
public class AdminTcCostRuleController {

    @Autowired(required = false)
    private TcCostRuleService tcCostRuleService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="计费规则",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String parklotId = ServletRequestUtils.getStringParameter(request,"parklotId","");
        String unitPrice = ServletRequestUtils.getStringParameter(request,"unitPrice","");
        String maxHour = ServletRequestUtils.getStringParameter(request,"maxHour","");
        String minHour = ServletRequestUtils.getStringParameter(request,"minHour","");
        String descp = ServletRequestUtils.getStringParameter(request,"descp","");
        if(StringUtils.isEmpty(parklotId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车场ID为空",null);
        }
        if(StringUtils.isEmpty(unitPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的单价为空",null);
        }
        if(StringUtils.isEmpty(maxHour)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的上限小时为空",null);
        }
        if(StringUtils.isEmpty(minHour)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的最小小时为空",null);
        }
        //判断单价
        if(!ValidateUtils.isAmount(unitPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的单价",null);
        }
        //判断上限小时数
        if(!ValidateUtils.isAmount(maxHour)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的上限小时数",null);
        }
        //判断最小小时数
        if(!ValidateUtils.isAmount(minHour)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的最小小时数",null);
        }
        TcCostRule tcCostRule=new TcCostRule();
        tcCostRule.setParklotId(parklotId);
        tcCostRule.setUnitPrice(ToolUtil.getDoubleToInt(Double.valueOf(unitPrice)));
        tcCostRule.setMaxHour(ToolUtil.getDoubleToInt(Double.valueOf(maxHour)));
        tcCostRule.setMinHour(ToolUtil.getDoubleToInt(Double.valueOf(minHour)));
        tcCostRule.setDescp(descp);
        return tcCostRuleService.insert(tcCostRule);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="计费规则",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String parklotId = ServletRequestUtils.getStringParameter(request,"parklotId","");
        String unitPrice = ServletRequestUtils.getStringParameter(request,"unitPrice","");
        String maxHour = ServletRequestUtils.getStringParameter(request,"maxHour","");
        String minHour = ServletRequestUtils.getStringParameter(request,"minHour","");
        String descp = ServletRequestUtils.getStringParameter(request,"descp","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的Id为空",null);
        }
        if(StringUtils.isEmpty(parklotId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车场ID为空",null);
        }
        if(StringUtils.isEmpty(unitPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的单价为空",null);
        }
        if(StringUtils.isEmpty(maxHour)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的上限小时为空",null);
        }
        if(StringUtils.isEmpty(minHour)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的最小小时为空",null);
        }
        //判断单价
        if(!ValidateUtils.isAmount(unitPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的单价",null);
        }
        //判断上限小时数
        if(!ValidateUtils.isAmount(maxHour)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的上限小时数",null);
        }
        //判断最小小时数
        if(!ValidateUtils.isAmount(minHour)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的最小小时数",null);
        }
        TcCostRule tcCostRule=new TcCostRule();
        tcCostRule.setId(id);
        tcCostRule.setParklotId(parklotId);
        tcCostRule.setUnitPrice(ToolUtil.getDoubleToInt(Double.valueOf(unitPrice)));
        tcCostRule.setMaxHour(ToolUtil.getDoubleToInt(Double.valueOf(maxHour)));
        tcCostRule.setMinHour(ToolUtil.getDoubleToInt(Double.valueOf(minHour)));
        tcCostRule.setDescp(descp);
        return tcCostRuleService.update(tcCostRule);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="计费规则",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcCostRuleService.delete(strarray);
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
    @OperLog(operationType="计费规则",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcCostRuleService.getById(id);
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
    @OperLog(operationType="计费规则",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String parklotId = ServletRequestUtils.getStringParameter(request, "parklotId", "");
        if (StringUtils.isNotEmpty(parklotId)) {
            condition.put("parklotId", parklotId);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcCostRuleService.getList(condition,pageHelper);
    }
}
