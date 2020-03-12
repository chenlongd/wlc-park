package com.perenc.xh.lsp.controller.admin.tcParklot;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcParklot.TcParklot;
import com.perenc.xh.lsp.service.tcParklot.TcParklotService;
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
@RequestMapping("parklot")
public class AdminTcParklotController {

    @Autowired(required = false)
    private TcParklotService tcParklotService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="场地",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String number = ServletRequestUtils.getStringParameter(request,"number","");
        String unitPrice = ServletRequestUtils.getStringParameter(request,"unitPrice","");
        String maxHour = ServletRequestUtils.getStringParameter(request,"maxHour","");
        String minHour = ServletRequestUtils.getStringParameter(request,"minHour","");
        String descp = ServletRequestUtils.getStringParameter(request,"descp","");
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的场地名称为空",null);
        }
        if(StringUtils.isEmpty(number)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的场地车位数为空",null);
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
        if(StringUtils.isEmpty(descp)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的描述为空",null);
        }
        //判断车位数
        if(!ValidateUtils.isNumber(number)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的车位数",null);
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
        TcParklot tcParklot=new TcParklot();
        tcParklot.setName(name);
        tcParklot.setNumber(Integer.valueOf(number));
        tcParklot.setUnitPrice(ToolUtil.getDoubleToInt(Double.valueOf(unitPrice)));
        tcParklot.setMaxHour(ToolUtil.getDoubleToInt(Double.valueOf(maxHour)));
        tcParklot.setMinHour(ToolUtil.getDoubleToInt(Double.valueOf(minHour)));
        tcParklot.setDescp(descp);
        return tcParklotService.insert(tcParklot);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="场地",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String number = ServletRequestUtils.getStringParameter(request,"number","");
        String unitPrice = ServletRequestUtils.getStringParameter(request,"unitPrice","");
        String maxHour = ServletRequestUtils.getStringParameter(request,"maxHour","");
        String minHour = ServletRequestUtils.getStringParameter(request,"minHour","");
        String descp = ServletRequestUtils.getStringParameter(request,"descp","");
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的场地名称为空",null);
        }
        if(StringUtils.isEmpty(number)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的场地车位数为空",null);
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
        if(StringUtils.isEmpty(descp)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的描述为空",null);
        }
        //判断车位数
        if(!ValidateUtils.isNumber(number)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的车位数",null);
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
        TcParklot tcParklot=new TcParklot();
        tcParklot.setId(id);
        tcParklot.setName(name);
        tcParklot.setNumber(Integer.valueOf(number));
        tcParklot.setUnitPrice(ToolUtil.getDoubleToInt(Double.valueOf(unitPrice)));
        tcParklot.setMaxHour(ToolUtil.getDoubleToInt(Double.valueOf(maxHour)));
        tcParklot.setMinHour(ToolUtil.getDoubleToInt(Double.valueOf(minHour)));
        tcParklot.setDescp(descp);
        return tcParklotService.update(tcParklot);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="场地",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcParklotService.delete(strarray);
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
    @OperLog(operationType="场地",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcParklotService.getById(id);
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
    @OperLog(operationType="场地",operationName="列表查询")
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
        return tcParklotService.getList(condition,pageHelper);
    }
}
