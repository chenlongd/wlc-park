package com.perenc.xh.lsp.dao.tcOrder;

import com.perenc.xh.commonUtils.dao.BaseDao;
import com.perenc.xh.lsp.entity.tcOrder.TcOrderTemp;

import java.util.List;


public interface TcOrderTempDao extends BaseDao<TcOrderTemp> {


    /**
     * 批量删除更改状态
     * @param list
     * @return
     */
    int deleteBatch(List list);

}
