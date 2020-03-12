package com.perenc.xh.lsp.dao.user;



import com.perenc.xh.commonUtils.dao.BaseDao;
import com.perenc.xh.lsp.entity.user.User;

import java.util.List;
import java.util.Map;


public interface UserDao extends BaseDao<User> {
    //这里添加自定义方法

    List<User> getByCompanyId(Map<String, Object> param);

    /*wm_user_company_contents 和 wm_user 表联合查询*/
    User getUserInfoByCompanyIdAndArticleId(Map<String, Object> map);

    List<User> getByUserId(Map<String, Object> param);

    List<User> getBySectionId(Map<String, Object> map);

    Integer getBySectionIdCount(Map<String, Object> map);

    /**
     * 根据用户名模糊查询
     * @param map
     * @return
     */
    List<User> listByLikeUserName(Map<String, Object> map);
}
