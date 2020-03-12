package com.perenc.xh.lsp.dao.tcSeller;


import com.perenc.xh.commonUtils.dao.BaseDao;
import com.perenc.xh.lsp.entity.tcSeller.TcSeller;

import java.util.List;


public interface TcSellerDao extends BaseDao<TcSeller> {

    /**
     * 通过Phone查询除本身外是否存在重复
     * @param tcSeller
     * @return
     */
    TcSeller getByIdAndPhone(TcSeller tcSeller);

    /**
     * 批量删除更改状态
     * @param list
     * @return
     */
    int deleteBatch(List list);

}
