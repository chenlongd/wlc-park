package com.perenc.xh.lsp.controller.phone.tcVip;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcVip.TcVipCar;
import com.perenc.xh.lsp.service.tcVipCar.TcVipCarService;
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
@RequestMapping("api/vipCar")
public class TcVipCarController {

    @Autowired(required = false)
    private TcVipCarService tcVipCarService;

    /**
     * 添加
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/add")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String carNum = ServletRequestUtils.getStringParameter(request,"carNum","");
        String vipId = ServletRequestUtils.getStringParameter(request,"vipId","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        if(StringUtils.isEmpty(carNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车牌号为空",null);
        }
        if(StringUtils.isEmpty(vipId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的VipID为空",null);
        }
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(!ValidateUtils.isCarnumberNO(carNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的车牌号",null);
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("carNum",carNum);
        TcVipCar tcVipCar=new TcVipCar();
        tcVipCar.setVipId(vipId);
        tcVipCar.setExtendId(Integer.valueOf(extendId));
        return tcVipCarService.insertCarVip(tcVipCar,condition);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateUseStatus")
    @ResponseBody
    @OperLog(operationType="会员",operationName="修改使用状态")
    public ReturnJsonData updateUseStatus(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主ID为空",null);
        }
        Map<String, Object> condition = new HashMap<>();
        TcVipCar tcVipCar=new TcVipCar();
        tcVipCar.setId(id);
        return tcVipCarService.updateCarVip(tcVipCar,condition);
    }

    /**
     * 根据Id查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/getById")
    @OperLog(operationType="会员车辆",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcVipCarService.getById(id);
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
    @OperLog(operationType="会员车辆",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String extendId = ServletRequestUtils.getStringParameter(request, "extendId", "");
        if (StringUtils.isNotEmpty(extendId)) {
            condition.put("extendId", extendId);
        }
        String carId = ServletRequestUtils.getStringParameter(request, "carId", "");
        if (StringUtils.isNotEmpty(carId)) {
            condition.put("carId", carId);
        }
        String useStatus = ServletRequestUtils.getStringParameter(request, "useStatus", "");
        if (StringUtils.isNotEmpty(useStatus)) {
            condition.put("useStatus", useStatus);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcVipCarService.getList(condition,pageHelper);
    }
}
