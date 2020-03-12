package com.perenc.xh.lsp.dao.UserRole;

import com.perenc.xh.commonUtils.dao.BaseDao;
import com.perenc.xh.lsp.entity.userRole.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserRoleDao extends BaseDao<UserRole> {
    //这里添加自定义方法

    int addWmUserRoles(@Param("userRoles") List<UserRole> userRoleList);
}
