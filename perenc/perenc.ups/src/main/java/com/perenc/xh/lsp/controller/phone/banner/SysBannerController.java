package com.perenc.xh.lsp.controller.phone.banner;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.banner.SysBanner;
import com.perenc.xh.lsp.service.banner.SysBannerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 轮播图
 * @Author xiaobai
 * @Date 2019/5/30 13:42
 **/
@Controller
@RequestMapping("api")
public class SysBannerController {


    @Autowired(required = false)
    private SysBannerService bannerService;


    /**
     * 添加轮播图
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("addBannerInfo")
    @ResponseBody
    public ReturnJsonData addBannerInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");

        SysBanner banner = new SysBanner();
        String imageUrl = ServletRequestUtils.getStringParameter(request, "imageUrl", "");
        if(StringUtils.isEmpty(imageUrl)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的图片为空",null);
        }
        banner.setImageUrl(imageUrl);
        int type = ServletRequestUtils.getIntParameter(request, "type", 0);
        if(0 == type){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        banner.setType(type);
        int bannerType = ServletRequestUtils.getIntParameter(request, "bannerType", 0);
        if(0 == bannerType){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的轮播图类型为空",null);
        }
        banner.setBannerType(bannerType);
        String objId = ServletRequestUtils.getStringParameter(request, "storeId", "");
//        if(StringUtils.isEmpty(objId)){
//            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的创建对象ID为空",null);
//        }
        banner.setObjId(objId);
        int redirectType = ServletRequestUtils.getIntParameter(request, "redirectType", 0);
        banner.setRedirectType(redirectType);
        String linkUrl = ServletRequestUtils.getStringParameter(request, "linkUrl", "");
        banner.setLinkUrl(linkUrl);
        int sort = ServletRequestUtils.getIntParameter(request, "sort", -1);
        if(-1 == sort){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的排序参数错误",null);
        }
        banner.setSort(sort);
        String title = ServletRequestUtils.getStringParameter(request, "title", "");
        banner.setTitle(title);
        String describe = ServletRequestUtils.getStringParameter(request, "describe", "");
        banner.setDescribe(describe);
        return bannerService.insert(banner);
    }

    /**
     * 修改轮播图
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateBannerInfo")
    @ResponseBody
    public ReturnJsonData updateBannerInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");

        SysBanner banner = new SysBanner();
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的图片ID为空",null);
        }
        banner.setId(id);
        String imageUrl = ServletRequestUtils.getStringParameter(request, "imageUrl", "");
        if(StringUtils.isEmpty(imageUrl)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的图片为空",null);
        }
        banner.setImageUrl(imageUrl);
        int type = ServletRequestUtils.getIntParameter(request, "type", 0);
        if(0 == type){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        banner.setType(type);
        int bannerType = ServletRequestUtils.getIntParameter(request, "bannerType", 0);
        if(0 == bannerType){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        banner.setBannerType(bannerType);
        String objId = ServletRequestUtils.getStringParameter(request, "storeId", "");
        if(StringUtils.isEmpty(objId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的创建对象ID为空",null);
        }
        banner.setObjId(objId);
        int redirectType = ServletRequestUtils.getIntParameter(request, "redirectType", 0);
        banner.setRedirectType(redirectType);
        String linkUrl = ServletRequestUtils.getStringParameter(request, "linkUrl", "");
        banner.setLinkUrl(linkUrl);
        int sort = ServletRequestUtils.getIntParameter(request, "sort", -1);
        if(-1 == sort){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的排序参数错误",null);
        }
        banner.setSort(sort);
        String title = ServletRequestUtils.getStringParameter(request, "title", "");
        banner.setTitle(title);
        String describe = ServletRequestUtils.getStringParameter(request, "describe", "");
        banner.setDescribe(describe);
        return bannerService.update(banner);
    }


    /**
     * 删除
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("deleteBanner")
    @ResponseBody
    public ReturnJsonData deleteBanner(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        if(StringUtils.isEmpty(ids)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ids为空",null);
        }
        List<String> idList = new ArrayList<>();
        if(ids.length() > 0){
            String[] idArray = ids.split(",");
            for (int i = 0; i < idArray.length; i++){
                idList.add(idArray[i]);
            }
        }
        return bannerService.delete(idList.toArray(new String[idList.size()]));
    }

    /**
     * 详情
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getBannerInfo")
    @ResponseBody
    public ReturnJsonData getBannerInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(org.apache.commons.lang.StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的id为空",null);
        }
        return bannerService.getBannerInfo(id);
    }


    /**
     * 根据条件查询
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getBannerList")
    @ResponseBody
    public ReturnJsonData getBannerList(HttpServletRequest request, HttpServletResponse response) throws Exception{
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
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", 20);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return bannerService.getBannerList(condition,pageHelper);
    }

}
