package com.perenc.xh.lsp.dao.menu;



import com.perenc.xh.commonUtils.dao.BaseDao;
import com.perenc.xh.lsp.entity.menu.Menu;

import java.util.List;
import java.util.Map;

public interface MenuDao extends BaseDao<Menu> {

    List<Menu> queryTreeChlid(Map<String, Object> map);

    List<String> getUserMenu(Map<String, Object> map);

    List<Menu> queryMenuTreeByUserId(Integer userId);

    List<Menu> queryMenuTreeChildByUserId(Map<String, Object> map);

    List<Menu> queryMenuByMenuUrl(Map<String, Object> map);
}
