package com.perenc.xh.lsp.controller.admin.tcIntegral;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegral;
import com.perenc.xh.lsp.service.tcIntegral.TcIntegralService;
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
@RequestMapping("integral")
public class AdminTcIntegralController {

    @Autowired(required = false)
    private TcIntegralService tcIntegralService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="积分记录",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String number = ServletRequestUtils.getStringParameter(request,"number","");
        String goodsId = ServletRequestUtils.getStringParameter(request,"goodsId","");
        String remark = ServletRequestUtils.getStringParameter(request,"remark","");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(number)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的数量为空",null);
        }
        if(StringUtils.isEmpty(remark)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的备注为空",null);
        }

        TcIntegral tcIntegral=new TcIntegral();
        tcIntegral.setExtendId(Integer.valueOf(extendId));
        tcIntegral.setType(Integer.valueOf(type));
        tcIntegral.setNumber(Integer.valueOf(number));
        tcIntegral.setGoodsId(goodsId);
        tcIntegral.setRemark(remark);
        return tcIntegralService.insert(tcIntegral);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="积分记录",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String number = ServletRequestUtils.getStringParameter(request,"number","");
        String goodsId = ServletRequestUtils.getStringParameter(request,"goodsId","");
        String remark = ServletRequestUtils.getStringParameter(request,"remark","");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(number)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的数量为空",null);
        }
        if(StringUtils.isEmpty(remark)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的备注为空",null);
        }
        TcIntegral tcIntegral=new TcIntegral();
        tcIntegral.setId(id);
        tcIntegral.setExtendId(Integer.valueOf(extendId));
        tcIntegral.setType(Integer.valueOf(type));
        tcIntegral.setNumber(Integer.valueOf(number));
        tcIntegral.setGoodsId(goodsId);
        tcIntegral.setRemark(remark);
        return tcIntegralService.update(tcIntegral);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="积分记录",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcIntegralService.delete(strarray);
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
    @OperLog(operationType="积分记录",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcIntegralService.getById(id);
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
    @OperLog(operationType="积分记录",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String extendId = ServletRequestUtils.getStringParameter(request, "extendId", "");
        if (StringUtils.isNotEmpty(extendId)) {
            condition.put("extendId", extendId);
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
        return tcIntegralService.getList(condition,pageHelper);
    }
}
