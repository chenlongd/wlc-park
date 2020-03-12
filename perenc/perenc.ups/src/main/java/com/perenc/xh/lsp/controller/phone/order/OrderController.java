package com.perenc.xh.lsp.controller.phone.order;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.utils.wxUtil.XmlUtil;
import com.perenc.xh.lsp.service.order.SysOrderService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 订单系统
 * @Author xiaobai
 * @Date 2019/5/22 15:55
 **/
@Controller
@RequestMapping("api")
public class OrderController {

    private static final Logger logger = Logger.getLogger(OrderController.class);

    @Autowired(required = false)
    private SysOrderService orderService;



    /**
     * 支付微信异步通知
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "notify_wechat",produces = "application/json; charset=utf-8")
    public String notify_wechat(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml> ";
        String xml = getXmlParam(request);
        logger.info("微信异步通知请求返回的结果"+xml);
        // 转成对象
        Map<String, String> resMap = XmlUtil.xmlToMap(xml);
        logger.info("微信异步通知请求返回解析后结果"+resMap);
        // ----------------------------成功
        if ("SUCCESS".equals(resMap.get("result_code"))) {
            String orderNo = resMap.get("out_trade_no");// 订单号
            String amount = resMap.get("total_fee");//交易金额
            String transactionId = resMap.get("transaction_id");// 微信给的交易号
            Map<String, Object> map = new HashMap<>();
            map.put("orderNo", orderNo);
            map.put("amount", amount);
            map.put("transactionId", transactionId);
            ReturnJsonData returnJson = orderService.notifyWechat(map);
            if (0 == returnJson.getCode()) {
                logger.info("异步通知返回成功");
                resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml> ";
                return resXml;
            }else {
                logger.info("异步通知返回失败");
                return resXml;
            }
        }else {
            return resXml;
        }
    }

    /**
     * 支付微信异步通知
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
//    @ResponseBody
//    @RequestMapping(value = "refundNotifyWechat",produces = "application/json; charset=utf-8")
//    public String refundNotifyWechat(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String resXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml> ";
//        String xml = getXmlParam(request);
//        logger.info("微信异步通知请求返回的结果"+xml);
//        // 转成对象
//        Map<String, String> resMap = XmlUtil.xmlToMap(xml);
//        logger.info("微信异步通知请求返回解析后结果"+resMap);
//        if ("SUCCESS".equals(resMap.get("result_code"))) {// ----------------------------成功
//            String orderNo = resMap.get("out_trade_no");// 订单号
//            String transactionId = resMap.get("transaction_id");// 微信给的交易号
//            Map<String, Object> map = new HashMap<>();
//            map.put("orderNo", orderNo);
//            map.put("transactionId", transactionId);
//            ReturnJsonData returnJson = orderService.refundNotifyWechat(map);
//            if (0 == returnJson.getCode()) {
//                logger.info("异步通知返回成功");
//                resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml> ";
//                return resXml;
//            }else {
//                logger.info("异步通知返回失败");
//                return resXml;
//            }
//        }else {
//            return resXml;
//        }
//    }

    /**
     * 取XML参数
     * @param req
     * @return
     */
    protected String getXmlParam(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder("");
        BufferedReader in = null;
        try {
            in = req.getReader();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if(StringUtils.isNotBlank(ip)) {
            ip = ip.split(",")[0];
        }
        return ip;

    }
}
