package com.perenc.xh.lsp.controller.phone.tcVip;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.service.tcVip.TcVipService;
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

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/19 15:28
 **/
@Controller
@RequestMapping("api/vip")
public class TcVipController {

    //VIP
    @Autowired(required = false)
    private TcVipService tcVipService;

    //VIP车辆关系
    @Autowired(required = false)
    private TcVipCarService tcVipCarService;




    /**
     * 后台列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    @ResponseBody
    @OperLog(operationType="会员",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String name = ServletRequestUtils.getStringParameter(request, "name", "");
        if (StringUtils.isNotEmpty(name)) {
            condition.put("name", name);
        }
        return tcVipService.getAllList(condition);
    }







}
