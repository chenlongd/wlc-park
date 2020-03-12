package com.perenc.xh.lsp.controller.admin.tcCar;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcCar.TcCarInout;
import com.perenc.xh.lsp.service.tcCarInout.TcCarInoutService;
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
@RequestMapping("carOut")
public class AdminTcCarOutController {

    @Autowired(required = false)
    private TcCarInoutService tcCarInoutService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="车辆出场记录",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String parklotId = ServletRequestUtils.getStringParameter(request,"parklotId","");
        String carId = ServletRequestUtils.getStringParameter(request,"carId","");
        String carNum = ServletRequestUtils.getStringParameter(request,"carNum","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String sdate = ServletRequestUtils.getStringParameter(request,"sdate","");
        String edate = ServletRequestUtils.getStringParameter(request,"edate","");
        String parkHour = ServletRequestUtils.getStringParameter(request,"parkHour","");
        String intimg = ServletRequestUtils.getStringParameter(request,"intimg","");
        String outimg = ServletRequestUtils.getStringParameter(request,"outimg","");
        if(StringUtils.isEmpty(parklotId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车场ID为空",null);
        }
        if(StringUtils.isEmpty(carId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID为空",null);
        }
        if(StringUtils.isEmpty(carNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车牌号为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID为空",null);
        }
        if(StringUtils.isEmpty(sdate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的计费开始时间为空",null);
        }
        if(StringUtils.isEmpty(edate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的计费结束时间为空",null);
        }
        if(StringUtils.isEmpty(parkHour)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车时长为空",null);
        }
        if(StringUtils.isEmpty(intimg)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的进场图片为空",null);
        }
        if(StringUtils.isEmpty(outimg)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的出场图片为空",null);
        }
        TcCarInout tcCarInout=new TcCarInout();
        tcCarInout.setParklotId(parklotId);
        tcCarInout.setCarId(carId);
        tcCarInout.setCarNum(carNum);
        tcCarInout.setType(Integer.valueOf(type));
        tcCarInout.setSdate(sdate);
        tcCarInout.setEdate(edate);
        tcCarInout.setParkHour(Integer.valueOf(parkHour));
        tcCarInout.setIntimg(intimg);
        tcCarInout.setOutimg(outimg);
        return tcCarInoutService.insert(tcCarInout);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="车辆出场记录",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String parklotId = ServletRequestUtils.getStringParameter(request,"parklotId","");
        String carId = ServletRequestUtils.getStringParameter(request,"carId","");
        String carNum = ServletRequestUtils.getStringParameter(request,"carNum","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String sdate = ServletRequestUtils.getStringParameter(request,"sdate","");
        String edate = ServletRequestUtils.getStringParameter(request,"edate","");
        String parkHour = ServletRequestUtils.getStringParameter(request,"parkHour","");
        String intimg = ServletRequestUtils.getStringParameter(request,"intimg","");
        String outimg = ServletRequestUtils.getStringParameter(request,"outimg","");
        if(StringUtils.isEmpty(parklotId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车场ID为空",null);
        }
        if(StringUtils.isEmpty(carId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID为空",null);
        }
        if(StringUtils.isEmpty(carNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车牌号为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车辆ID为空",null);
        }
        if(StringUtils.isEmpty(sdate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的计费开始时间为空",null);
        }
        if(StringUtils.isEmpty(edate)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的计费结束时间为空",null);
        }
        if(StringUtils.isEmpty(parkHour)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车时长为空",null);
        }
        if(StringUtils.isEmpty(intimg)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的进场图片为空",null);
        }
        if(StringUtils.isEmpty(outimg)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的出场图片为空",null);
        }
        TcCarInout tcCarInout=new TcCarInout();
        tcCarInout.setId(id);
        tcCarInout.setParklotId(parklotId);
        tcCarInout.setCarId(carId);
        tcCarInout.setCarNum(carNum);
        tcCarInout.setType(Integer.valueOf(type));
        tcCarInout.setSdate(sdate);
        tcCarInout.setEdate(edate);
        tcCarInout.setParkHour(Integer.valueOf(parkHour));
        tcCarInout.setIntimg(intimg);
        tcCarInout.setOutimg(outimg);
        return tcCarInoutService.update(tcCarInout);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="车辆出场记录",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcCarInoutService.delete(strarray);
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
    @OperLog(operationType="车辆出场记录",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcCarInoutService.getById(id);
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
    @OperLog(operationType="车辆出场记录",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String carNum = ServletRequestUtils.getStringParameter(request, "carNum", "");
        if (StringUtils.isNotEmpty(carNum)) {
            condition.put("carNum", carNum);
        }
        //出场开始时间
        String outSdate = ServletRequestUtils.getStringParameter(request,"outSdate","");
        if(StringUtils.isNotEmpty(outSdate)){
            condition.put("outSdate",outSdate);
        }
        //出场结束时间
        String outEdate = ServletRequestUtils.getStringParameter(request,"outEdate","");
        if(StringUtils.isNotEmpty(outEdate)){
            condition.put("outEdate",outEdate);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcCarInoutService.getList(condition,pageHelper);
    }
}
