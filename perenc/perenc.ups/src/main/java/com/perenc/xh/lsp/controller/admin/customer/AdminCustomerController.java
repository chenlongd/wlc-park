package com.perenc.xh.lsp.controller.admin.customer;

import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.DataGrid;
import com.perenc.xh.commonUtils.model.PageHelper;
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
import java.util.List;
import java.util.Map;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/9 10:15
 **/
@Controller
@RequestMapping("customer")
public class AdminCustomerController {

    @Autowired(required = false)
    private WxCustomerService wmCustomerService;


    @RequestMapping("getCustomerList")
    @ResponseBody
    public PhoneReturnJson getCustomerList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //页数
        int pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
        //条数
        int rows = ServletRequestUtils.getIntParameter(request, "rows", 1);
        //微信昵称
        String nickName = ServletRequestUtils.getStringParameter(request, "nickName", "");
        PageHelper page = new PageHelper();
        page.setPage(pageNo);
        page.setRows(rows);
        DataGrid dataGrid = new DataGrid();
        QueryParam param = new QueryParam();
        param.setPageNum(page.getPage());
        param.setPageSize(page.getRows());
        if(StringUtils.isNotEmpty(nickName)){
            param.put("nick_name","%"+nickName+"%");
        }
        List<Map<String,Object>> customerList = wmCustomerService.getWmCustomerList(param);
        dataGrid.setRows(customerList);
        if(customerList.size() <= 0){
            dataGrid.setTotal(0);
        }else {
            dataGrid.setTotal(Long.valueOf(String.valueOf(customerList.get(0).get("count"))));
        }

        dataGrid.setPageNo(pageNo);
        dataGrid.setPageSize(rows);
        Map<String,Object> condition = new HashMap<>();
        condition.put("customerList",dataGrid);
        return new PhoneReturnJson(true,"获取数据成功",condition);
    }

    @RequestMapping("updateCustomer")
    @ResponseBody
    public PhoneReturnJson updateCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception{
        WmCustomer customer = new WmCustomer();
        String customerId = ServletRequestUtils.getStringParameter(request, "customerId", "");
        if(StringUtils.isNotEmpty(customerId)){
            customer.setId(Integer.valueOf(customerId));
        }else{
            return new PhoneReturnJson(false,"传入客户ID信息为空",null);
        }
        String isUseWebSocket = ServletRequestUtils.getStringParameter(request, "isUseWebSocket");
        if(StringUtils.isNotEmpty(isUseWebSocket)){
            customer.setIsUseWebSocket(Integer.valueOf(isUseWebSocket));
        }
        return wmCustomerService.update(customer);
    }
}
