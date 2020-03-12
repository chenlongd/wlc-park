package com.perenc.xh.lsp.controller.admin.tcExtend;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCar;
import com.perenc.xh.lsp.service.tcExtendCar.TcExtendCarService;
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
@RequestMapping("extendCar")
public class AdminTcExtendCarController {

    @Autowired(required = false)
    private TcExtendCarService tcExtendCarService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="客户车辆",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String carId = ServletRequestUtils.getStringParameter(request,"carId","");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(StringUtils.isEmpty(carId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID为空",null);
        }
        TcExtendCar tcExtendCar=new TcExtendCar();
        tcExtendCar.setExtendId(Integer.valueOf(extendId));
        tcExtendCar.setCarId(carId);
        return tcExtendCarService.insert(tcExtendCar);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="客户车辆",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String carId = ServletRequestUtils.getStringParameter(request,"carId","");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(StringUtils.isEmpty(carId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID为空",null);
        }
        TcExtendCar tcExtendCar=new TcExtendCar();
        tcExtendCar.setId(id);
        tcExtendCar.setExtendId(Integer.valueOf(extendId));
        tcExtendCar.setCarId(carId);
        return tcExtendCarService.update(tcExtendCar);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="客户车辆",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcExtendCarService.delete(strarray);
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
    @OperLog(operationType="客户车辆",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcExtendCarService.getById(id);
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
    @OperLog(operationType="客户车辆",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String extendId = ServletRequestUtils.getStringParameter(request, "extendId", "");
        if (StringUtils.isNotEmpty(extendId)) {
            condition.put("extendId", extendId);
        }
        String carId = ServletRequestUtils.getStringParameter(request, "carId", "");
        if (StringUtils.isNotEmpty(extendId)) {
            condition.put("carId", extendId);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcExtendCarService.getList(condition,pageHelper);
    }

    /**
     * 客户车辆列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    @ResponseBody
    @OperLog(operationType="客户车辆",operationName="列表查询")
    public ReturnJsonData list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String extendId = ServletRequestUtils.getStringParameter(request, "extendId", "");
        if (StringUtils.isNotEmpty(extendId)) {
            condition.put("extendId", extendId);
        }
        String carId = ServletRequestUtils.getStringParameter(request, "carId", "");
        if (StringUtils.isNotEmpty(carId)) {
            condition.put("carId", carId);
        }
        return tcExtendCarService.getAllList(condition);
    }

}
