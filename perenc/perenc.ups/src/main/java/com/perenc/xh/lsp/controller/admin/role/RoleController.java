package com.perenc.xh.lsp.controller.admin.role;


import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.DataGrid;
import com.perenc.xh.commonUtils.model.PageHelper;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.entity.role.Role;
import com.perenc.xh.lsp.entity.roleMenu.RoleMenu;
import com.perenc.xh.lsp.service.role.RoleService;
import com.perenc.xh.lsp.service.roleMenu.RoleMenuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController {

    @Autowired(required = false)
    private RoleService wmRoleService;

    @Autowired(required = false)
    private RoleMenuService wmRoleMenuService;


    /**
     * 跳转角色添加页面
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
//        //跳转添加页面
//        return "redirect:"+ PropertiesGetValue.getProperty("PAGE.URL")+"role_add.html";
//    }

    /**
     * 跳转角色更新页面
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
//        //获取更新用户id
//        String id = request.getParameter("id");
//        //跳转更新页面
//        return "redirect:"+ PropertiesGetValue.getProperty("PAGE.URL")+"role_update.html?roleId="+id;
//    }

    /**
     * 获取角色列表
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    @ResponseBody
    public ReturnJsonData getWxRoleList(HttpServletRequest request, HttpServletResponse response, PageHelper page) throws Exception{
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");

        DataGrid dataGrid = new DataGrid();
        QueryParam param = new QueryParam();
        param.setPageNum(page.getPage());
        param.setPageSize(page.getRows());
        List<Map<String,Object>> roleList = wmRoleService.getWmRoleList(param);
        dataGrid.setRows(roleList);
        if(roleList.size() <= 0){
            dataGrid.setTotal(0);
        }else {
            dataGrid.setTotal(Long.valueOf(String.valueOf(roleList.get(0).get("count"))));
        }
        Map<String,Object> condition = new HashMap<>();
        condition.put("roleList",dataGrid);
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
    }

    /**
     * 删除角色
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("delRoleById")
    @ResponseBody
    public ReturnJsonData delRoleById(HttpServletRequest request, HttpServletResponse response) throws Exception{
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");

        String id = request.getParameter("roleId");
        if(StringUtils.isNotEmpty(id)){
            return wmRoleService.delWmRole(Integer.valueOf(id));
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
    @RequestMapping("addRole")
    @ResponseBody
    public ReturnJsonData addRole(HttpServletRequest request, HttpServletResponse response) throws Exception{
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");

        Role role = new Role();
        String roleName = ServletRequestUtils.getStringParameter(request, "roleName", "");
        if(StringUtils.isNotEmpty(roleName)){
            role.setRoleName(roleName);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的角色名称为空",null);
        }
        String roleDescrip = ServletRequestUtils.getStringParameter(request, "roleDescrip", "");
        if(StringUtils.isNotEmpty(roleDescrip)){
            role.setRoleDescrip(roleDescrip);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的角色描述为空",null);
        }
        int roleType = ServletRequestUtils.getIntParameter(request, "roleType", 1);
        role.setRoleType(roleType);
        role.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        return wmRoleService.insertRole(role);
    }

    /**
     * 后台 修改
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("updateRole")
    public ReturnJsonData updateRole(HttpServletRequest request, HttpServletResponse response)throws Exception{
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");


        Role role = new Role();
        String roleId = ServletRequestUtils.getStringParameter(request, "roleId", "");
        if(StringUtils.isNotEmpty(roleId)){
            role.setId(Integer.valueOf(roleId));
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的角色ID为空",null);
        }
        String roleName = ServletRequestUtils.getStringParameter(request, "roleName", "");
        if(StringUtils.isNotEmpty(roleName)){
            role.setRoleName(roleName);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的角色名称为空",null);
        }
        String roleDescrip = ServletRequestUtils.getStringParameter(request, "roleDescrip", "");
        if(StringUtils.isNotEmpty(roleDescrip)){
            role.setRoleDescrip(roleDescrip);
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的角色描述为空",null);
        }
        int roleType = ServletRequestUtils.getIntParameter(request, "roleType", 1);
        role.setRoleType(roleType);
        role.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        return wmRoleService.updateWmRole(role);
    }

    /**
     * 后台 单个信息
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/getRoleInfo")
    public ReturnJsonData getRoleInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");

        String id = request.getParameter("roleId");
        if(StringUtils.isNotEmpty(id)) {
            Role role = wmRoleService.getWmRoleById(Integer.valueOf(id));
            Map<String,Object> condition = new HashMap<>();
            condition.put("role",role);
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
        }
        return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"获取数据失败",null);
    }

    /**
     * 后台 所有角色信息
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/getAllRoleList")
    public ReturnJsonData getAllRoleList(HttpServletRequest request,HttpServletResponse response) throws Exception {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");

        QueryParam param = new QueryParam();
        param.setQueryall(true);
        List<Role> roleList = wmRoleService.getAllWmRole(param);

        Map<String,Object> condition = new HashMap<>();
        condition.put("roleList",roleList);
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
    }

    /**
     *  查询所有的菜单
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
//    @RequestMapping("/getAllMenus")
//    public String getAllMenus(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");
//
//        //获取角色id
//        String roleId = request.getParameter("id");
//
//        //跳转授权菜单
//        return "redirect:"+ PropertiesGetValue.getProperty("PAGE.URL")+"role_authMenu.html?roleId="+roleId;
//    }

    /**
     * 查看该角色拥有菜单
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getRoleMenusByRoleId")
    @ResponseBody
    public ReturnJsonData getRoleMenusByRoleId(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");

        String roleId = ServletRequestUtils.getStringParameter(request,"roleId","");
        if(StringUtils.isEmpty(roleId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的角色ID为空",null);
        }
        //查询数据
        QueryParam condition = new QueryParam();
        condition.addCondition("role_id","=",roleId);
        condition.setQueryall(true);
        List<Map<String,Object>> roleMenus = wmRoleMenuService.getWmRoleMenuList(condition);

        //返回数据给前端
        Map<String,Object> param = new HashMap<>();
        param.put("roleMenuList",roleMenus);
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",param);
    }

    /**
     * 是否给角色菜单授权
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/isAuthMenu")
    @ResponseBody
    public ReturnJsonData isAuthMenu(HttpServletRequest request,HttpServletResponse response) throws Exception {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setContentType("text/json; charset=utf-8");

        String roleId = ServletRequestUtils.getStringParameter(request,"roleId","");
        if(StringUtils.isEmpty(roleId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的角色ID为空",null);
        }
        List<RoleMenu> wmRoleMenuList = new ArrayList<RoleMenu>();
        String menuIds = ServletRequestUtils.getStringParameter(request,"menuIds","");
        if (StringUtils.isNotEmpty(menuIds)) {
            String[] menuId = menuIds.split(",");
            if (menuId != null && menuId.length > 0) {
                for (int i = 0; i < menuId.length; i++) {
                    RoleMenu wmRoleMenu = new RoleMenu();
                    wmRoleMenu.setRoleId(Integer.valueOf(roleId));
                    wmRoleMenu.setMenuId(Integer.valueOf(menuId[i]));
                    wmRoleMenuList.add(wmRoleMenu);
                }
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请选择菜单",null);
        }
        return wmRoleMenuService.addWmRoleMenus(wmRoleMenuList);
    }
}
