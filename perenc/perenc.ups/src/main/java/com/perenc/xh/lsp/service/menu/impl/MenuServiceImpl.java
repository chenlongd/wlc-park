package com.perenc.xh.lsp.service.menu.impl;

import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.DBRelevant.InsertParam;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.Tree;
import com.perenc.xh.lsp.dao.menu.MenuDao;
import com.perenc.xh.lsp.entity.menu.Menu;
import com.perenc.xh.lsp.entity.menu.MenuJson;
import com.perenc.xh.lsp.entity.user.User;
import com.perenc.xh.lsp.service.menu.MenuService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("wmMenuService")
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {

    @Autowired(required = false)
    private MenuDao wmMenuDao;


    @Override
    public ReturnJsonData insertWmMenu(Menu wmMenu) {
        QueryParam param = new QueryParam();
        param.addCondition("super_id","=",wmMenu.getSuperId());
        param.addCondition("menu_name","=",wmMenu.getMenuName());
        param.addCondition("menu_type","=",wmMenu.getMenuType());
        Menu menu = wmMenuDao.getOne(param);
        if(menu == null) {
            InsertParam ip = DBUtil.toInsertParam(wmMenu);
            int flag = wmMenuDao.add(ip);
            if (flag > 0) {
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加成功", null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"该菜单名称已经添加了",null);
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
    }

    @Override
    public ReturnJsonData updateWmMenu(Menu wmMenu) {
        int flag = wmMenuDao.update(DBUtil.toUpdateParam(wmMenu,"id"));
        if(flag>0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    @Override
    public ReturnJsonData delWmMenu(int id) {
        QueryParam condition = new QueryParam();
        condition.addCondition("super_id","=",id);
        condition.setQueryall(true);
        List<Menu> wmMenuList = wmMenuDao.list(condition);
        for(Menu menu : wmMenuList){
            wmMenuDao.delete(new Object[]{menu.getId()});
        }
        int flag = wmMenuDao.delete(new Object[]{id});
        if(flag>0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"删除成功",null);
        }
        return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE,"删除成功",null);
    }

    @Override
    public Menu getWmMenuById(int id) {
        return  wmMenuDao.getById(id);
    }

    @Override
    public List<Tree> getWmMenuList(String id) throws Exception{
        List<Menu> menuList = new ArrayList<Menu>();
        List<Tree> treeList = new ArrayList<Tree>();
        QueryParam condition = new QueryParam();
        condition.addCondition("super_id","=",0);
        condition.setOrder("menu_sort ASC");
        menuList = wmMenuDao.list(condition);
        if(id != null && !"0".equals(id.trim())){
            QueryParam param = new QueryParam();
            param.addCondition("super_id","=",Integer.valueOf(id));
            param.setOrder("menu_sort");
            menuList = wmMenuDao.list(param);
        }
        for (Menu menu : menuList) {
            Tree tree = new Tree();
            tree.setPid(menu.getSuperId()+"");
            tree.setId(menu.getId()+"");
            tree.setText(menu.getMenuName());
            tree.setIconCls(menu.getHeadImageUrl());
            tree.setSort(menu.getMenuSort());
            Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put("menuUrl", menu.getMenuUrl());
            attributes.put("menuType", menu.getMenuType());
            attributes.put("menuSort", menu.getMenuSort());
            attributes.put("status", "0");
            tree.setAttributes(attributes);
            if(queryTreeChlidren(menu.getId()+"")){
                tree.setState("closed");
                List<Menu> chlidList = queryTreeByChlid(menu.getId());
                List<Tree> chlidTreeList = new ArrayList<>();
                for(Menu chlidMenu : chlidList){
                    Tree chlidTree = new Tree();
                    chlidTree.setPid(chlidMenu.getSuperId()+"");
                    chlidTree.setId(chlidMenu.getId()+"");
                    chlidTree.setText(chlidMenu.getMenuName());
                    chlidTree.setIconCls(chlidMenu.getHeadImageUrl());
                    chlidTree.setSort(chlidMenu.getMenuSort());
                    Map<String, Object> chlidAttributes = new HashMap<String, Object>();
                    chlidAttributes.put("menuUrl", chlidMenu.getMenuUrl());
                    chlidAttributes.put("menuType", chlidMenu.getMenuType());
                    chlidAttributes.put("menuSort", chlidMenu.getMenuSort());
                    chlidAttributes.put("status", "0");
                    chlidTree.setAttributes(chlidAttributes);
                    chlidTreeList.add(chlidTree);
                    if(queryTreeChlidren(chlidMenu.getId()+"")){
                        chlidTree.setState("closed");
                        List<Menu> chlidrenList = queryTreeByChlid(chlidMenu.getId());
                        List<Tree> chlidrenTreeList = new ArrayList<>();
                        for(Menu chlidrenMenu : chlidrenList) {
                            Tree chlidrenTree = new Tree();
                            chlidrenTree.setPid(chlidrenMenu.getSuperId() + "");
                            chlidrenTree.setId(chlidrenMenu.getId() + "");
                            chlidrenTree.setText(chlidrenMenu.getMenuName());
                            chlidrenTree.setIconCls(chlidrenMenu.getHeadImageUrl());
                            chlidrenTree.setSort(chlidrenMenu.getMenuSort());
                            Map<String, Object> chlidrenAttributes = new HashMap<String, Object>();
                            chlidrenAttributes.put("menuUrl", chlidrenMenu.getMenuUrl());
                            chlidrenAttributes.put("menuType", chlidrenMenu.getMenuType());
                            chlidrenAttributes.put("menuSort", chlidrenMenu.getMenuSort());
                            chlidrenAttributes.put("status", "0");
                            chlidrenTree.setAttributes(chlidrenAttributes);
                            chlidrenTreeList.add(chlidrenTree);
                        }
                        chlidTree.setChildren(chlidrenTreeList);
                    }
                }
                tree.setChildren(chlidTreeList);
            }
            treeList.add(tree);
        }
        return treeList;
    }


    public boolean queryTreeChlidren(String id) throws Exception{
        QueryParam param = new QueryParam();
        param.addCondition("super_id","=",Integer.valueOf(id));
        List<Menu> list = wmMenuDao.list(param);
        return null != list && list.size()>0?true:false;
    }

    @Override
    public List<Tree> queryMenuTreeByManagerId(User user, String id) throws Exception {
        List<Tree> treeList = new ArrayList<Tree>();
        List<Menu> menuList = new ArrayList<Menu>();
        if(null != user){
            if("0".equals(user.getUserType().toString())){//超级管理员
                if(null != Integer.valueOf(user.getId()) && !"".equals(user.getId())){
                    QueryParam condition = new QueryParam();
                    condition.addCondition("super_id","=",0);
                    condition.setOrder("menu_sort ASC");
                    condition.setQueryall(true);
                    menuList = wmMenuDao.list(condition);
                    if(id != null && !"0".equals(id.trim())){
                        QueryParam param = new QueryParam();
                        param.addCondition("super_id","=",Integer.valueOf(id));
                        param.setOrder("menu_sort ASC");
                        menuList = wmMenuDao.list(param);
                    }
                }
            }else{//其它管理员
                menuList = wmMenuDao.queryMenuTreeByUserId(user.getId());
                if(id != null && !"0".equals(id.trim())){
                    Map<String,Object> map = new HashMap<>();
                    map.put("userId", user.getId());
                    map.put("superId", id);
                    menuList = wmMenuDao.queryMenuTreeChildByUserId(map);
                }
            }
            if(null != menuList && menuList.size()>0){
                for (Menu menu : menuList) {
                    Tree tree = new Tree();
                    tree.setPid(menu.getSuperId()+"");
                    tree.setId(menu.getId()+"");
                    tree.setText(menu.getMenuName());
                    tree.setIconCls(menu.getHeadImageUrl());
                    tree.setSort(menu.getMenuSort());
                    Map<String, Object> attributes = new HashMap<String, Object>();
                    attributes.put("menuUrl", menu.getMenuUrl());
                    attributes.put("menuType", menu.getMenuType());
                    attributes.put("menuSort", menu.getMenuSort());
                    attributes.put("status", "0");
                    tree.setAttributes(attributes);
                    if(queryTreeChlidren(menu.getId()+"")){
                        tree.setState("closed");
                        List<Menu> chlidList = null;
                        if("0".equals(user.getUserType().toString())) {//超级管理员{
                            chlidList = queryTreeByChlid(menu.getId());
                        }else{
                            chlidList = queryTreeChlid(menu.getId(), user.getId());
                        }
                        List<Tree> chlidTreeList = new ArrayList<>();
                        for(Menu chlidMenu : chlidList){
                            Tree chlidTree = new Tree();
                            chlidTree.setPid(chlidMenu.getSuperId()+"");
                            chlidTree.setId(chlidMenu.getId()+"");
                            chlidTree.setText(chlidMenu.getMenuName());
                            chlidTree.setIconCls(chlidMenu.getHeadImageUrl());
                            chlidTree.setSort(chlidMenu.getMenuSort());
                            Map<String, Object> chlidAttributes = new HashMap<String, Object>();
                            chlidAttributes.put("menuUrl", chlidMenu.getMenuUrl());
                            chlidAttributes.put("menuType", chlidMenu.getMenuType());
                            chlidAttributes.put("menuSort", chlidMenu.getMenuSort());
                            chlidAttributes.put("status", "0");
                            chlidTree.setAttributes(chlidAttributes);
                            chlidTreeList.add(chlidTree);
                        }
                        tree.setChildren(chlidTreeList);
                    }
                    treeList.add(tree);
                }
            }
        }
        return treeList;
    }

    @Override
    public ReturnJsonData queryUserMenuByUserPhone(Map<String,Object> map) {
        map.put("superId",0);
//        map.put("menuType",0);
        List<Menu> menus = wmMenuDao.queryTreeChlid(map);
        List<Menu> buttonMenus = new ArrayList<>();
        List<Menu> cMenus = new ArrayList<>();
        Map<String, Object> menuList = new HashMap<>();
        List<MenuJson> pList = new ArrayList<>();
        if (menus.size()>0){
            for (int i=0;i<menus.size();i++) {
                MenuJson menuJson1 = new MenuJson();
                List<MenuJson> cList = new ArrayList<>();
                map.put("superId",menus.get(i).getId());
//                map.put("menuType",1);
                cMenus = wmMenuDao.queryTreeChlid(map);
                int a=0;
                if (cMenus.size()>0){
                    for (int j=0;j<cMenus.size();j++) {
                        MenuJson menuJson = new MenuJson();
                        List<MenuJson> buttonList = new ArrayList<>();
                        map.put("superId", cMenus.get(j).getId());
                        buttonMenus = wmMenuDao.queryTreeChlid(map);
                        int b = 0;
                        if (buttonMenus.size() > 0) {
                            for (int k = 0; k < buttonMenus.size(); k++) {
                                MenuJson buttonMenuJson = new MenuJson();
                                if (!(map.get("menuUrl").equals("1"))) {
                                    if (buttonMenus.get(k).getMenuUrl().contains(MapUtils.getString(map, "menuUrl"))) {
                                        buttonMenuJson.setSelected(1);
                                        b = k;
                                    } else {
                                        buttonMenuJson.setSelected(0);
                                    }
                                } else {
                                    buttonMenuJson.setSelected(0);
                                }
                                if (buttonList.size() > 0) {
                                    if (map.get("menuUrl").equals("1")) {
                                        buttonList.get(0).setSelected(1);
                                        menuJson.setSelected(0);
                                    } else {
                                        if (cList.get(b).getSelected() == 1) {
                                            menuJson.setSelected(1);
                                        } else {
                                            cList.get(0).setSelected(1);
                                            menuJson.setSelected(0);
                                        }
                                    }
                                } else {
                                    if (!(map.get("menuUrl").equals("1"))) {
                                        if (menus.get(j).getMenuUrl().contains(MapUtils.getString(map, "menuUrl"))) {
                                            menuJson.setSelected(1);
                                        } else {
                                            menuJson.setSelected(0);
                                        }
                                    } else {
                                        menuJson.setSelected(0);
                                    }
                                }
                                buttonMenuJson.setMenuName(buttonMenus.get(k).getMenuName());
                                buttonMenuJson.setMenuImage(buttonMenus.get(k).getHeadImageUrl());
                                buttonMenuJson.setMenuUrl(buttonMenus.get(k).getMenuUrl());
                                buttonMenuJson.setMenuType(buttonMenus.get(k).getMenuType());
                                buttonMenuJson.setMenuID(buttonMenus.get(k).getId());
                                buttonList.add(buttonMenuJson);
                            }
                        }
                        if (!(map.get("menuUrl").equals("1"))) {
                            if (cMenus.get(j).getMenuUrl().contains(MapUtils.getString(map, "menuUrl"))) {
                                menuJson.setSelected(1);
                                a = j;
                            } else {
                                menuJson.setSelected(0);
                            }
                        } else {
                            menuJson.setSelected(0);
                        }
                        menuJson.setMenuName(cMenus.get(j).getMenuName());
                        menuJson.setMenuImage(cMenus.get(j).getHeadImageUrl());
                        menuJson.setMenuUrl(cMenus.get(j).getMenuUrl());
                        menuJson.setMenuType(cMenus.get(j).getMenuType());
                        menuJson.setMenuID(cMenus.get(j).getId());
                        menuJson.setButtonList(buttonList);
                        cList.add(menuJson);

                    }
                }
                if(cList.size()>0){
                    if(map.get("menuUrl").equals("1")){
                        cList.get(0).setSelected(1);
                        menuJson1.setSelected(0);
                    }else{
                        if(cList.get(a).getSelected()==1){
                            menuJson1.setSelected(1);
                        }else{
                            cList.get(0).setSelected(1);
                            menuJson1.setSelected(0);
                        }
                    }
                }else{
                    if(!(map.get("menuUrl").equals("1"))){
                        if(menus.get(i).getMenuUrl().contains(MapUtils.getString(map,"menuUrl"))){
                            menuJson1.setSelected(1);
                        }else {
                            menuJson1.setSelected(0);
                        }
                    }else{
                        menuJson1.setSelected(0);
                    }
                }
                menuJson1.setMenuName(menus.get(i).getMenuName());
                menuJson1.setMenuImage(menus.get(i).getHeadImageUrl());
                menuJson1.setMenuUrl(menus.get(i).getMenuUrl());
                menuJson1.setMenuType(menus.get(i).getMenuType());
                menuJson1.setMenuID(menus.get(i).getId());
                menuJson1.setChildlist(cList);
                pList.add(menuJson1);
            }
            if(map.get("menuUrl").equals("1")){
                pList.get(0).setSelected(1);
            }
            if(!map.get("sonMenuUrl").equals("1")){
                menuList.put("sonMenuUrl",map.get("sonMenuUrl"));
            }else{
                menuList.put("sonMenuUrl","");
            }
            menuList.put("menuList",pList);
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "获取成功", menuList);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "获取失败，该用户没有菜单！", null);
        }

    }

    @Override
    public List<String> getUserMenu(Map<String, Object> map) {
        return wmMenuDao.getUserMenu(map);
    }

    //菜单第二级
    public List<Menu> queryTreeChlid(int menuId, int userId) throws Exception{
//        QueryParam param = new QueryParam();
//        param.addCondition("super_id","=", menuId);
//        param.addCondition("menu_type","=",1);
//        param.setOrder("menu_sort ASC");
        Map<String,Object> param = new HashMap<>();
        param.put("superId",menuId);
        param.put("userId",userId);
        return wmMenuDao.queryTreeChlid(param);
    }

    //菜单第三级
    public List<Menu> queryTreeByChlid(int menuId) throws Exception{
        QueryParam param = new QueryParam();
        param.addCondition("super_id","=", menuId);
        param.setOrder("menu_sort ASC");
        return wmMenuDao.list(param);
    }

    /**
     * 根据路径查询按钮
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public List<Menu> queryMenuByMenuUrl(Map<String, Object> map) throws Exception{
        List<Menu> menuList = new ArrayList<Menu>();
        String userId = MapUtils.getString(map, "userId", "");
        String menuUrl = MapUtils.getString(map, "menuUrl", "");
        String userType = MapUtils.getString(map, "userType", "");
        if(userType.equals("0")) {//超级管理员
            Map<String, Object> param = new HashMap<>();
            param.put("userId", "");
            param.put("menuUrl", menuUrl);
            param.put("menuType", 2);
            menuList= wmMenuDao.queryMenuByMenuUrl(param);
        }else {
            Map<String, Object> param = new HashMap<>();
            param.put("userId", userId);
            param.put("menuUrl", menuUrl);
            param.put("menuType", 2);
            menuList= wmMenuDao.queryMenuByMenuUrl(param);
        }
        return menuList;
    }

    /**
     * 查询所有菜单
     * @return
     * @throws Exception
     */
    @Override
    public List<Tree> queryAllMenu(User user) throws Exception {
        List<Tree> treeList = new ArrayList<Tree>();
        List<Menu> menuList = new ArrayList<Menu>();
        if("0".equals(user.getUserType().toString())){//超级管理员
            if(null != Integer.valueOf(user.getId()) && !"".equals(user.getId())){
                QueryParam condition = new QueryParam();
                condition.addCondition("super_id","=",0);
                condition.setOrder("menu_sort ASC");
                condition.setQueryall(true);
                menuList = wmMenuDao.list(condition);
            }
        }else{//其它管理员
            menuList = wmMenuDao.queryMenuTreeByUserId(user.getId());
        }
        if(null != menuList && menuList.size()>0){
            for (Menu menu : menuList) {
                Tree tree = new Tree();
                tree.setPid(menu.getSuperId()+"");
                tree.setId(menu.getId()+"");
                tree.setText(menu.getMenuName());
                tree.setIconCls(menu.getHeadImageUrl());
                tree.setSort(menu.getMenuSort());
                Map<String, Object> attributes = new HashMap<String, Object>();
                attributes.put("menuUrl", menu.getMenuUrl());
                attributes.put("menuType", menu.getMenuType());
                attributes.put("menuSort", menu.getMenuSort());
                attributes.put("status", "0");
                tree.setAttributes(attributes);
                if(queryTreeChlidren(menu.getId()+"")){
                    tree.setState("closed");
                    List<Menu> chlidList = queryTreeByChlid(menu.getId());
                    List<Tree> chlidTreeList = new ArrayList<>();
                    for(Menu chlidMenu : chlidList){
                        if(chlidMenu.getMenuType() == 1) {
                            Tree chlidTree = new Tree();
                            chlidTree.setPid(chlidMenu.getSuperId() + "");
                            chlidTree.setId(chlidMenu.getId() + "");
                            chlidTree.setText(chlidMenu.getMenuName());
                            chlidTree.setIconCls(chlidMenu.getHeadImageUrl());
                            chlidTree.setSort(chlidMenu.getMenuSort());
                            Map<String, Object> chlidAttributes = new HashMap<String, Object>();
                            chlidAttributes.put("menuUrl", chlidMenu.getMenuUrl());
                            chlidAttributes.put("menuType", chlidMenu.getMenuType());
                            chlidAttributes.put("menuSort", chlidMenu.getMenuSort());
                            chlidAttributes.put("status", "0");
                            chlidTree.setAttributes(chlidAttributes);
                            chlidTreeList.add(chlidTree);
                        }
                    }
                    tree.setChildren(chlidTreeList);
                }
                treeList.add(tree);
            }
        }
        return treeList;
    }

}
