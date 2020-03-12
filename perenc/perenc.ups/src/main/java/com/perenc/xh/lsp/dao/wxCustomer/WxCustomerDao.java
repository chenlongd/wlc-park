package com.perenc.xh.lsp.dao.wxCustomer;



import com.perenc.xh.commonUtils.dao.BaseDao;
import com.perenc.xh.lsp.entity.wxCustomer.WmCustomer;

import java.util.List;
import java.util.Map;


public interface WxCustomerDao extends BaseDao<WmCustomer> {

    List<WmCustomer> selectCustomerInfo(Map<String, Object> map);

    /**
     * 层级用户
     * @param map
     * @return
     */
    List<WmCustomer> selectHierarchyWmCustomerInfo(Map<String, Object> map);

}
