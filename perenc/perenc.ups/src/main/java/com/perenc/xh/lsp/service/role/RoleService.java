package com.perenc.xh.lsp.service.role;


import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.entity.role.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {

    ReturnJsonData insertRole(Role adminRole);

    ReturnJsonData updateWmRole(Role adminRole);

    ReturnJsonData delWmRole(int id);

    Role getWmRoleById(int id);

    List<Role> getAllWmRole(QueryParam param);

    List<Map<String,Object>> getWmRoleList(QueryParam param);
}
