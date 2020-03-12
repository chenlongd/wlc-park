package com.perenc.xh.lsp.service.user;



import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.entity.role.Role;
import com.perenc.xh.lsp.entity.user.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User userLoginPage(Map<String, Object> param) throws Exception;

    ReturnJsonData insertUser(User mp) throws Exception;

    ReturnJsonData updateUser(User mp);

    ReturnJsonData updateMapperUserPwd(User mp);

    ReturnJsonData updatePWd(Map<String, Object> map);

    ReturnJsonData mapperUserAll(QueryParam param);

    ReturnJsonData mapperUserList(QueryParam param);

    User getUserById(int id);

    List<Map<String,Object>> getUserList(QueryParam param);

    Role getUserRole(QueryParam param);

    ReturnJsonData removeUser(int id);

    ReturnJsonData resetPassword(int id);

}
