package com.perenc.xh.lsp.controller.admin.menu;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.Tree;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.entity.menu.Menu;
import com.perenc.xh.lsp.entity.user.User;
import com.perenc.xh.lsp.service.menu.MenuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired(required = false)
    private MenuService wmMenuService;

    /**
     * 跳转添加菜单的页面
     * @param request
     * @param response
     * @return
     */
//    @RequestMapping("add")
//    public String add(HttpServletRequest request, HttpServletResponse response){
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");
//
//        return "redirect:"+ PropertiesGetValue.getProperty("PAGE.URL")+"menu_add.html";
//    }

    /**
     * 跳转修改菜单的页面
     * @param request
     * @param response
     * @return
     */
//    @RequestMapping("update")
//    public String update(HttpServletRequest request,HttpServletResponse response){
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");
//
//        String id = request.getParameter("id");
//
//        return "redirect:"+ PropertiesGetValue.getProperty("PAGE.URL")+"menu_update.html?menuId="+id;
//    }

    /**
     * 左边的树形结构
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryMenuTree")
    @ResponseBody
    public ReturnJsonData queryMenuTree(HttpServletRequest request, HttpServletResponse response) throws Exception{
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");

        String id = request.getParameter("superId");
        User user = (User) request.getSession().getAttribute("user");
        List<Tree> treeList = wmMenuService.queryMenuTreeByManagerId(user,id);
        Map<String, Object> condition = new HashMap<>();
        condition.put("treeList",treeList);
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
    }

    /**
     * 获取所有的菜单列表（菜单和按钮）
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getMenuList")
    @ResponseBody
    public ReturnJsonData getMenuList(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");

        String id = request.getParameter("id");
        List<Tree> treeList = wmMenuService.getWmMenuList(id);

        Map<String, Object> condition = new HashMap<>();
        condition.put("treeList",treeList);
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
    }

    /**
     * 后台删除
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("delMenuById")
    @ResponseBody
    public ReturnJsonData delWmMenuById(HttpServletRequest request, HttpServletResponse response) throws Exception{
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");

        String id = request.getParameter("menuId");
        if(StringUtils.isNotEmpty(id)){
            return wmMenuService.delWmMenu(Integer.valueOf(id));
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的参数为空",null);
        }
    }

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("addMenu")
    @ResponseBody
    public ReturnJsonData addMenu(HttpServletRequest request,HttpServletResponse response) throws Exception{
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");

        Menu wmMenu = new Menu();
        String menuName = ServletRequestUtils.getStringParameter(request,"menuName","");
        if(StringUtils.isNotEmpty(menuName)){
            wmMenu.setMenuName(menuName);
        }
        String menuUrl = ServletRequestUtils.getStringParameter(request,"menuUrl","");
        if(StringUtils.isNotEmpty(menuUrl)){
            wmMenu.setMenuUrl(menuUrl);
        }
        Integer superId = ServletRequestUtils.getIntParameter(request,"superId",0);
        if(superId != 0){
            wmMenu.setSuperId(superId);
        }else{
            wmMenu.setSuperId(0);
        }
        String headImageUrl = ServletRequestUtils.getStringParameter(request,"headImageUrl","");
        if(StringUtils.isNotEmpty(headImageUrl)){
            wmMenu.setHeadImageUrl(headImageUrl);
        }
        int menuType = ServletRequestUtils.getIntParameter(request,"menuType",1);
        if(menuType != 0){
            wmMenu.setMenuType(menuType);
        }else{
            wmMenu.setMenuType(0);
        }
        int menuSort = ServletRequestUtils.getIntParameter(request,"menuSort",0);
        if(menuSort != 0){
            wmMenu.setMenuSort(menuSort);
        }else{
            wmMenu.setMenuSort(0);
        }
        wmMenu.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        return wmMenuService.insertWmMenu(wmMenu);
    }

    /**
     * 后台 修改
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("updateMenu")
    public ReturnJsonData updateMenu(HttpServletRequest request,HttpServletResponse response)throws Exception{
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");

        Menu wmMenu = new Menu();
        String id = request.getParameter("menuId");
        if(StringUtils.isNotEmpty(id)){
            wmMenu.setId(Integer.valueOf(id));
        }
        String menuName = ServletRequestUtils.getStringParameter(request,"menuName","");
        if(StringUtils.isNotEmpty(menuName)){
            wmMenu.setMenuName(menuName);
        }
        String menuUrl = ServletRequestUtils.getStringParameter(request,"menuUrl","");
        if(StringUtils.isNotEmpty(menuUrl)){
            wmMenu.setMenuUrl(menuUrl);
        }
        Integer superId = ServletRequestUtils.getIntParameter(request,"superId",0);
        if(superId != 0){
            wmMenu.setSuperId(superId);
        }else{
            wmMenu.setSuperId(0);
        }
        String headImageUrl = ServletRequestUtils.getStringParameter(request,"headImageUrl","");
        if(StringUtils.isNotEmpty(headImageUrl)){
            wmMenu.setHeadImageUrl(headImageUrl);
        }
        int menuType = ServletRequestUtils.getIntParameter(request,"menuType",1);
        if(menuType != 0){
            wmMenu.setMenuType(menuType);
        }else{
            wmMenu.setMenuType(0);
        }
        int menuSort = ServletRequestUtils.getIntParameter(request,"menuSort",0);
        if(menuSort != 0){
            wmMenu.setMenuSort(menuSort);
        }else{
            wmMenu.setMenuSort(0);
        }
        return wmMenuService.updateWmMenu(wmMenu);
    }

    /**
     * 后台 单个信息
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/getMenuInfo")
    public ReturnJsonData getMenuInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");

        String id = request.getParameter("menuId");
        if(StringUtils.isNotEmpty(id)) {
            Menu menu = wmMenuService.getWmMenuById(Integer.valueOf(id));
            Map<String,Object> condition = new HashMap<>();
            condition.put("menu",menu);
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
        }
        return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE,"获取数据失败",null);
    }

    /**
     * 根据路径查询按钮
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getMenuByMenuUrl")
    @ResponseBody
    public ReturnJsonData getMenuByMenuUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");
        String menuUrl = ServletRequestUtils.getStringParameter(request, "menuUrl", "");
        if(StringUtils.isEmpty(menuUrl)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的路径为空",null);
        }
        User user =(User)request.getSession().getAttribute("user");
        if(StringUtils.isNotEmpty(menuUrl) && user!=null){
            Map<String,Object> param = new HashMap<>();
            param.put("menuUrl",menuUrl);
            param.put("userId",user.getId());
            param.put("userType",user.getUserType());
            List<Menu> wmMenuList = wmMenuService.queryMenuByMenuUrl(param);
            Map<String, Object> condition = new HashMap<>();
            condition.put("list",wmMenuList);
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
        }
        return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE,"获取数据失败",null);
    }

    /**
     * 根据路径查询按钮
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("queryAllMenu")
    @ResponseBody
    public ReturnJsonData queryAllMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");
        User user =(User)request.getSession().getAttribute("user");
        if(user == null){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"登录已超时，请重新登录",null);
        }
        List<Tree> trees = wmMenuService.queryAllMenu(user);
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",trees);
    }


}
