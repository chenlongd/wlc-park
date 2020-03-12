package com.perenc.xh.lsp.controller.admin.tcCar;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcCar.TcCarRecharge;
import com.perenc.xh.lsp.service.tcCarRecharge.TcCarRechargeService;
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

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/19 15:28
 **/
@Controller
@RequestMapping("carRecharge")
public class AdminTcCarRechargeController {

    @Autowired(required = false)
    private TcCarRechargeService tcCarRechargeService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="充值金额设置",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String oldPrice = ServletRequestUtils.getStringParameter(request,"oldPrice","");
        String getPrice = ServletRequestUtils.getStringParameter(request,"getPrice","");
        String remark = ServletRequestUtils.getStringParameter(request,"remark","");
        if(StringUtils.isEmpty(oldPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的充值金额为空",null);
        }
        if(StringUtils.isEmpty(getPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的送金额为空",null);
        }
        //判断充值金额
        if(!ValidateUtils.isAmount(oldPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的充值金额",null);
        }
        //判断送金额
        if(!ValidateUtils.isAmount(getPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的送金额",null);
        }
        TcCarRecharge tcCarRecharge=new TcCarRecharge();
        tcCarRecharge.setOldPrice(ToolUtil.getDoubleToInt(Double.valueOf(oldPrice)));
        tcCarRecharge.setGetPrice(ToolUtil.getDoubleToInt(Double.valueOf(getPrice)));
        tcCarRecharge.setRemark(remark);
        return tcCarRechargeService.insert(tcCarRecharge);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="充值金额设置",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String oldPrice = ServletRequestUtils.getStringParameter(request,"oldPrice","");
        String getPrice = ServletRequestUtils.getStringParameter(request,"getPrice","");
        String remark = ServletRequestUtils.getStringParameter(request,"remark","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的Id为空",null);
        }
        if(StringUtils.isEmpty(oldPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的充值金额为空",null);
        }
        if(StringUtils.isEmpty(getPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的送金额为空",null);
        }
        //判断充值金额
        if(!ValidateUtils.isAmount(oldPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的充值金额",null);
        }
        //判断送金额
        if(!ValidateUtils.isAmount(getPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的送金额",null);
        }
        TcCarRecharge tcCarRecharge=new TcCarRecharge();
        tcCarRecharge.setId(id);
        tcCarRecharge.setOldPrice(ToolUtil.getDoubleToInt(Double.valueOf(oldPrice)));
        tcCarRecharge.setGetPrice(ToolUtil.getDoubleToInt(Double.valueOf(getPrice)));
        tcCarRecharge.setRemark(remark);
        return tcCarRechargeService.update(tcCarRecharge);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateIsEnabled")
    @ResponseBody
    @OperLog(operationType="充值金额设置",operationName="修改启用状态")
    public ReturnJsonData updateIsEnabled(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String isEnabled = ServletRequestUtils.getStringParameter(request,"isEnabled","");
          if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的Id为空",null);
        }
        if(StringUtils.isEmpty(isEnabled)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的是否启用为空",null);
        }
        TcCarRecharge tcCarRecharge=new TcCarRecharge();
        tcCarRecharge.setId(id);
        tcCarRecharge.setIsEnabled(Integer.valueOf(isEnabled));
        return tcCarRechargeService.updateIsEnabled(tcCarRecharge);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="充值金额设置",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcCarRechargeService.delete(strarray);
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
    @OperLog(operationType="充值金额设置",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcCarRechargeService.getById(id);
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
    @OperLog(operationType="充值金额设置",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcCarRechargeService.getList(condition,pageHelper);
    }
}
