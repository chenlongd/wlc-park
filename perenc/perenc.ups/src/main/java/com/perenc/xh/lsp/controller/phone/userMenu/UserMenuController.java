package com.perenc.xh.lsp.controller.phone.userMenu;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.service.menu.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
@RequestMapping("api")
public class UserMenuController {

    @Autowired(required = false)
    private MenuService wmMenuService;

    @ResponseBody
    @RequestMapping("getHomePageData")
    public ReturnJsonData selectUserMenu(HttpServletRequest request, HttpServletResponse response)throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String userID = request.getParameter("userID");
        String menuUrl = request.getParameter("menuUrl");
        String sonMenuUrl = request.getParameter("sonMenuUrl");
        int userId=-1;
        if(StringUtils.isNotEmpty(userID)){
            userId=Integer.valueOf(userID);
        }

        if(!StringUtils.isNotEmpty(menuUrl)){
            menuUrl="1";
        }

        if(!StringUtils.isNotEmpty(sonMenuUrl)){
            sonMenuUrl="1";
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("menuUrl",menuUrl);
        map.put("sonMenuUrl",sonMenuUrl);
        return wmMenuService.queryUserMenuByUserPhone(map);
    }

}
