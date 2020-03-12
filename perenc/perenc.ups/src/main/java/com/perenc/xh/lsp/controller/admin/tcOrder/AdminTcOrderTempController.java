package com.perenc.xh.lsp.controller.admin.tcOrder;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.service.order.SysOrderService;
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
@RequestMapping("orderTemp")
public class AdminTcOrderTempController {


    @Autowired(required = false)
    private SysOrderService orderService;


    /**
     * 停车定单查询
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("page")
    @ResponseBody
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String,Object> map = new HashMap<>();
        //支付类型 商品、停车场
        String payType = ServletRequestUtils.getStringParameter(request,"payType","");
        if(StringUtils.isNotEmpty(payType)){
            map.put("payType",payType);
        }
        //订单号
        String orderNo = ServletRequestUtils.getStringParameter(request,"orderNo","");
        if(StringUtils.isNotEmpty(orderNo)){
            map.put("orderNo",orderNo);
        }
        //车牌号
        String carNum = ServletRequestUtils.getStringParameter(request,"carNum","");
        if(StringUtils.isNotEmpty(carNum)){
            map.put("carNum",carNum);
        }
        //创建开始时间
        String createSdate = ServletRequestUtils.getStringParameter(request,"createSdate","");
        if(StringUtils.isNotEmpty(createSdate)){
            map.put("createSdate",createSdate);
        }
        //创建结束时间
        String createEdate = ServletRequestUtils.getStringParameter(request,"createEdate","");
        if(StringUtils.isNotEmpty(createEdate)){
            map.put("createEdate",createEdate);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", 10);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return orderService.getTcList(map,pageHelper);
    }
}
