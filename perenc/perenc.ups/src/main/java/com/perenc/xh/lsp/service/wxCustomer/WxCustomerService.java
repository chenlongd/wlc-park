package com.perenc.xh.lsp.service.wxCustomer;


import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.PhoneReturnJson;
import com.perenc.xh.lsp.entity.wxCustomer.WmCustomer;


import java.util.List;
import java.util.Map;


public interface WxCustomerService {

    /*插入客户信息*/
    int insertWmCustomer(WmCustomer customer);

    /*获取客户信息*/
    WmCustomer getWmCustomer(QueryParam param);

    /*根据Id获取客户信息*/
    WmCustomer getById(int id);

    PhoneReturnJson update(WmCustomer wmCustomer);

    List<Map<String,Object>> getWmCustomerList(QueryParam param);

}
