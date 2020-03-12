package com.perenc.xh.lsp.service.userRole;

import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.entity.userRole.UserRole;


import java.util.List;
import java.util.Map;

public interface UserRoleService {

    ReturnJsonData insertWmUserRole(UserRole wmUserRole);

    ReturnJsonData updateWmUserRole(UserRole wmUserRole);

    ReturnJsonData delWmUserRole(int id);

    UserRole getWmUserRoleById(int id);

    List<UserRole> getAllWmUserRole(QueryParam param);

    List<Map<String,Object>> getWmUserRoleList(QueryParam param);

    /**
     * 批量添加账号角色
     * @param userRoleList
     * @return
     */
    ReturnJsonData addWmUserRoles(List<UserRole> userRoleList);

}
