package com.perenc.xh.lsp.controller.admin.tcVip;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcVip.TcVip;
import com.perenc.xh.lsp.service.tcVip.TcVipService;
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
@RequestMapping("vip")
public class AdminTcVipController {

    @Autowired(required = false)
    private TcVipService tcVipService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="会员",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String costPrice = ServletRequestUtils.getStringParameter(request,"costPrice","");
        String discountPrice = ServletRequestUtils.getStringParameter(request,"discountPrice","");
        String image = ServletRequestUtils.getStringParameter(request,"image","");
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的名称为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(costPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的现价为空",null);
        }
        if(StringUtils.isEmpty(discountPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的折扣价为空",null);
        }
        //判断现价
        if(!ValidateUtils.isAmount(costPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的现价",null);
        }
        //判断折扣价
        if(!ValidateUtils.isAmount(discountPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的折扣价",null);
        }
        TcVip tcVip=new TcVip();
        tcVip.setName(name);
        tcVip.setType(Integer.valueOf(type));
        tcVip.setCostPrice(ToolUtil.getDoubleToInt(Double.valueOf(costPrice)));
        tcVip.setDiscountPrice(ToolUtil.getDoubleToInt(Double.valueOf(discountPrice)));
        tcVip.setImage(image);
        return tcVipService.insert(tcVip);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="会员",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String costPrice = ServletRequestUtils.getStringParameter(request,"costPrice","");
        String discountPrice = ServletRequestUtils.getStringParameter(request,"discountPrice","");
        String image = ServletRequestUtils.getStringParameter(request,"image","");
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的会员名称为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(costPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的现价为空",null);
        }
        if(StringUtils.isEmpty(discountPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的折扣价为空",null);
        }
        //判断现价
        if(!ValidateUtils.isAmount(costPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的现价",null);
        }
        //判断折扣价
        if(!ValidateUtils.isAmount(discountPrice)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的折扣价",null);
        }
        TcVip tcVip=new TcVip();
        tcVip.setId(id);
        tcVip.setName(name);
        tcVip.setType(Integer.valueOf(type));
        tcVip.setCostPrice(ToolUtil.getDoubleToInt(Double.valueOf(costPrice)));
        tcVip.setDiscountPrice(ToolUtil.getDoubleToInt(Double.valueOf(discountPrice)));
        tcVip.setImage(image);
        return tcVipService.update(tcVip);
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
        TcVip tcVip=new TcVip();
        tcVip.setId(id);
        tcVip.setIsEnabled(Integer.valueOf(isEnabled));
        return tcVipService.updateIsEnabled(tcVip);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="会员",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcVipService.delete(strarray);
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
    @OperLog(operationType="会员",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcVipService.getById(id);
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
    @OperLog(operationType="会员",operationName="列表查询")
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
        return tcVipService.getList(condition,pageHelper);
    }
}
