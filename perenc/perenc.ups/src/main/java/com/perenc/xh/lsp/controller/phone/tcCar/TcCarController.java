package com.perenc.xh.lsp.controller.phone.tcCar;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.qrcode.QRCodeUtil;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcCar.TcCar;
import com.perenc.xh.lsp.service.tcCar.TcCarService;
import com.perenc.xh.lsp.service.tcCarInpass.TcCarInpassService;
import com.perenc.xh.lsp.service.tcExtendCar.TcExtendCarService;
import com.perenc.xh.lsp.service.tcVipCar.TcVipCarService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/19 15:28
 **/
@Controller
@RequestMapping("api/car")
public class TcCarController {

    @Autowired(required = false)
    private TcCarService tcCarService;

    //客户车辆关系
    @Autowired(required = false)
    private TcExtendCarService tcExtendCarService;

    //VIP车辆关系
    @Autowired(required = false)
    private TcVipCarService tcVipCarService;



    @Autowired(required = false)
    private TcCarInpassService tcCarInpassService;



    @ResponseBody
    @RequestMapping("/add")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String carNum = ServletRequestUtils.getStringParameter(request,"carNum","");
        //类型（默认：1:小车，2：中型车，3:大车）
        String type = ServletRequestUtils.getStringParameter(request,"type","1");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        //String parklotId = ServletRequestUtils.getStringParameter(request,"parklotId","");
        if(StringUtils.isEmpty(carNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车牌号为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车类型为空",null);
        }
        /*if(StringUtils.isEmpty(parklotId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的停车场ID为空",null);
        }*/
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(!ValidateUtils.isCarnumberNO(carNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的车牌号",null);
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("extendId",extendId);
        TcCar tcCar=new TcCar();
        tcCar.setCarNum(carNum);
        tcCar.setType(Integer.valueOf(type));
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
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String carNum = ServletRequestUtils.getStringParameter(request,"carNum","");
        //String type = ServletRequestUtils.getStringParameter(request,"type","");
        if(StringUtils.isEmpty(carNum)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车牌号为空",null);
        }
        /*if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的车类型为空",null);
        }*/
        /*if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }*/
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
        //tcCar.setType(Integer.valueOf(type));
        //tcCar.setParklotId(tcCar.getParklotId());
        //tcCar.setExtendId(Integer.valueOf(extendId));
        //tcCar.setCostSdate(costSdate);
        //tcCar.setCostEdate(costEdate);
        //tcCar.setIntimg(tcCar.getIntimg());
        //tcCar.setOutimg(tcCar.getOutimg());
        //tcCar.setIsEntry(2);//1:已入场，2未入场
        return tcCarService.update(tcCar);
    }

    /**
     * 车辆认证
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateTravelImg")
    @ResponseBody
    @OperLog(operationType="车辆",operationName="行车证认证")
    public ReturnJsonData updateTravelImg(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String travelImg = ServletRequestUtils.getStringParameter(request,"travelImg","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主ID为空",null);
        }
        if(StringUtils.isEmpty(travelImg)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的行车证为空",null);
        }
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("extendId",extendId);
        TcCar tcCar=new TcCar();
        tcCar.setId(id);
        tcCar.setTravelImg(travelImg);
        return tcCarService.updateTravelImg(tcCar,condition);
    }

    /**
     * 根据Id查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/getQrcodeById")
    @OperLog(operationType="客户",operationName="二维码查询")
    public void getQrcodeById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setContentType("image/jpeg");  //设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");  //设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        String carId = ServletRequestUtils.getStringParameter(request, "carId", "");
        String scode = ServletRequestUtils.getStringParameter(request, "scode", "");
        ServletOutputStream sos =null; // 将图像输出到Servlet输出流中。;// 将图像输出到Servlet输出流中。
        try {
            sos=response.getOutputStream();
            String strUrl="https://www.wlc-huish.com/?getQrcodeById&carId="+carId+"&scode="+scode;
            if(StringUtils.isNotEmpty(carId)) {
                //BufferedImage image = QRCodeUtil.zxingCodeImageCreate(id, 300, 300, "png");
                BufferedImage image = QRCodeUtil.zxingQRImageCreate(strUrl, 300, 300, "png");
                // 将内存中的图片通过流动形式输出到客户端
                ImageIO.write(image, "png", sos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    /**
     * 查询所有控制设备
     * @return
     */
    @ResponseBody
    @RequestMapping("/getJsDevices")
    @OperLog(operationType="捷顺",operationName="查询所有控制设备")
    public ReturnJsonData getJsDevices(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcCarService.getJsDevices(condition,pageHelper);
    }

    /**
     * 后台列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("findOneCarOrder")
    @ResponseBody
    @OperLog(operationType="车辆订单",operationName="列表查询")
    public ReturnJsonData findOneCarOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        String carNum = ServletRequestUtils.getStringParameter(request, "carNum", "");
        if(StringUtils.isNotEmpty(carNum)) {
            condition.put("carNum",carNum);
            return tcCarService.findOneCarOrder(condition);
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的车牌号",null);
        }
    }

    /**
     * 更改
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateBatchIsEntryByInsideJscar")
    @OperLog(operationType="捷顺",operationName="查询所有控制设备")
    public ReturnJsonData updateBatchIsEntryByInsideJscar(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        int num= tcCarInpassService.updateBatchIsEntryByInsideJscar();

        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的车牌号",null);
    }

}
