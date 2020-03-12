package com.perenc.xh.lsp.service.menu;



import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.Tree;
import com.perenc.xh.lsp.entity.menu.Menu;
import com.perenc.xh.lsp.entity.user.User;

import java.util.List;
import java.util.Map;

public interface MenuService {

    ReturnJsonData insertWmMenu(Menu wmMenu);

    ReturnJsonData updateWmMenu(Menu wmMenu);

    ReturnJsonData delWmMenu(int id);

    Menu getWmMenuById(int id);

    List<Tree> getWmMenuList(String id) throws Exception;

    List<Tree> queryMenuTreeByManagerId(User user, String id) throws Exception;

    ReturnJsonData queryUserMenuByUserPhone(Map<String, Object> map);

    List<String> getUserMenu(Map<String, Object> map);

    List<Menu> queryMenuByMenuUrl(Map<String, Object> map) throws Exception;

    List<Tree> queryAllMenu(User user) throws Exception;
}
