package com.perenc.xh.lsp.dao.tcSellerUser;

import com.perenc.xh.commonUtils.dao.BaseDao;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerUser;


import java.util.List;


public interface TcSellerUserDao extends BaseDao<TcSellerUser> {

    /**
     * 通过Phone查询除本身外是否存在重复
     * @param tcSellerUser
     * @return
     */
    TcSellerUser getByIdAndPhone(TcSellerUser tcSellerUser);

    /**
     * 批量删除更改状态
     * @param list
     * @return
     */
    int deleteBatch(List list);

}
