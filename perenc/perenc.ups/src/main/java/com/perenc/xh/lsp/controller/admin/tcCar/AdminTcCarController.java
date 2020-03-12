package com.perenc.xh.lsp.controller.admin.tcCar;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcCar.TcCar;
import com.perenc.xh.lsp.service.tcCar.TcCarService;
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
@RequestMapping("car")
public class AdminTcCarController {

    @Autowired(required = false)
    private TcCarService tcCarService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="车辆",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String carNum = ServletRequestUtils.getStringParameter(request,"carNum","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String parklotId = ServletRequestUtils.getStringParameter(request,"parklotId","");
        String intimg = ServletRequestUtils.getStringParameter(request,"intimg","");
        if(StringUtils.isEmpty(carNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车牌号为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车类型为空",null);
        }
        if(StringUtils.isEmpty(parklotId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车场ID为空",null);
        }
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        //车辆号为7位数
        /*if(carNum.length()!=7) {
            return new PhoneReturnJson(false, "请输入正确的购房编码", null);
        }*/
        if(!ValidateUtils.isCarnumberNO(carNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的车牌号",null);
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("extendId",extendId);
        TcCar tcCar=new TcCar();
        tcCar.setCarNum(carNum);
        tcCar.setType(Integer.valueOf(type));
        tcCar.setParklotId(tcCar.getParklotId());
        //tcCar.setIntimg(tcCar.getIntimg());
        //tcCar.setOutimg(tcCar.getOutimg());
        tcCar.setIsEntry(2);//1:已入场，2未入场
        return tcCarService.insertExtendCar(tcCar,condition);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="车辆",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String carNum = ServletRequestUtils.getStringParameter(request,"carNum","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String parklotId = ServletRequestUtils.getStringParameter(request,"parklotId","");
        String intimg = ServletRequestUtils.getStringParameter(request,"intimg","");
        if(StringUtils.isEmpty(carNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车牌号为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车类型为空",null);
        }
        if(StringUtils.isEmpty(parklotId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车场ID为空",null);
        }
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        //车辆号为7位数
        /*if(carNum.length()!=7) {
            return new PhoneReturnJson(false, "请输入正确的购房编码", null);
        }*/
        if(!ValidateUtils.isCarnumberNO(carNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的车牌号",null);
        }
        TcCar tcCar=new TcCar();
        tcCar.setId(id);
        tcCar.setCarNum(carNum);
        tcCar.setType(Integer.valueOf(type));
        tcCar.setParklotId(tcCar.getParklotId());
        //tcCar.setExtendId(Integer.valueOf(extendId));
        //tcCar.setCostSdate(costSdate);
        //tcCar.setCostEdate(costEdate);
        //tcCar.setIntimg(tcCar.getIntimg());
        //tcCar.setOutimg(tcCar.getOutimg());
        //tcCar.setIsEntry(2);//1:已入场，2未入场
        return tcCarService.update(tcCar);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="车辆",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcCarService.delete(strarray);
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
    @OperLog(operationType="车辆",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcCarService.getById(id);
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
    @OperLog(operationType="车辆",operationName="列表查询")
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
        return tcCarService.getList(condition,pageHelper);
    }
}
