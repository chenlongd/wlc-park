package com.perenc.xh.lsp.controller.phone.banner;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.service.banner.SysBannerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/19 15:28
 **/
@Controller
@RequestMapping("api/banner")
public class BannerController {

    @Autowired(required = false)
    private SysBannerService sysBannerService;

    /**
     * 查询
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    @ResponseBody
    public ReturnJsonData list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        String type = ServletRequestUtils.getStringParameter(request, "type", "");
        if (StringUtils.isNotEmpty(type)) {
            condition.put("type", type);
        }
        String bannerType = ServletRequestUtils.getStringParameter(request, "bannerType", "");
        if (StringUtils.isNotEmpty(bannerType)) {
            condition.put("bannerType", bannerType);
        }
        String objId = ServletRequestUtils.getStringParameter(request, "objId", "");
        if (StringUtils.isNotEmpty(objId)) {
            condition.put("objId", objId);
        }
        return sysBannerService.getAllBannerList(condition);
    }
}
