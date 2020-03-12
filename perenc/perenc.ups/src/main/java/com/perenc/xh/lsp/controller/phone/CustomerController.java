package com.perenc.xh.lsp.controller.phone;

import com.perenc.xh.commonUtils.model.PhoneReturnJson;
import com.perenc.xh.lsp.entity.wxCustomer.WmCustomer;
import com.perenc.xh.lsp.service.wxCustomer.WxCustomerService;
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
 * @Date 2019/3/9 10:15
 **/
@Controller
@RequestMapping("api")
public class CustomerController {

    @Autowired(required = false)
    private WxCustomerService wmCustomerService;

    @RequestMapping("isUseWebSocket")
    @ResponseBody
    public PhoneReturnJson isUseWebSocket(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String,Object> condition = new HashMap<>();
        String customerId = ServletRequestUtils.getStringParameter(request, "customerId", "");
        if(StringUtils.isNotEmpty(customerId)){
            WmCustomer wmCustomer = wmCustomerService.getById(Integer.valueOf(customerId));
            if(wmCustomer != null && wmCustomer.getIsUseWebSocket() == 1){
                condition.put("flag",true);
                return new PhoneReturnJson(true,"有演示权限",condition);
            }
        }else{
            return new PhoneReturnJson(false,"传入客户ID信息为空",null);
        }
        condition.put("flag",false);
        return new PhoneReturnJson(false,"请联系后台管理人员，开通演示权限",condition);
    }
}
