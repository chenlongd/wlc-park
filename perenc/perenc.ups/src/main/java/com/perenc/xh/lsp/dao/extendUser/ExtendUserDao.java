package com.perenc.xh.lsp.dao.extendUser;

import com.perenc.xh.commonUtils.dao.BaseDao;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;

import java.util.Map;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/4/11 17:06
 **/
public interface ExtendUserDao extends BaseDao<ExtendUser> {

    /**
     * 总的用户数统计
     * @param map
     * @return
     */
    long findCountUser(Map<String, Object> map);
}
