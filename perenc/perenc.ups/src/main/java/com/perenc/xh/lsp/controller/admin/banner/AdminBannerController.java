package com.perenc.xh.lsp.controller.admin.banner;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.banner.SysBanner;
import com.perenc.xh.lsp.entity.operLog.OperLog;
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


@Controller
@RequestMapping("banner")
public class AdminBannerController {

    @Autowired(required = false)
    private SysBannerService sysBannerService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="轮播图",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String imageUrl = ServletRequestUtils.getStringParameter(request,"imageUrl","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String bannerType = ServletRequestUtils.getStringParameter(request,"bannerType","");
        String objId = ServletRequestUtils.getStringParameter(request,"objId","");
        int redirectType = ServletRequestUtils.getIntParameter(request,"redirectType",0);
        String sort = ServletRequestUtils.getStringParameter(request,"sort","1");
        String title = ServletRequestUtils.getStringParameter(request,"title","");
        String describe = ServletRequestUtils.getStringParameter(request,"describe","");
        String linkUrl = ServletRequestUtils.getStringParameter(request,"linkUrl","");
        if(StringUtils.isEmpty(imageUrl)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的图片地址为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(bannerType)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的轮播类型为空",null);
        }
        if(StringUtils.isEmpty(title)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的标题为空",null);
        }
        if(StringUtils.isEmpty(describe)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的描述为空",null);
        }
        SysBanner sysBanner=new SysBanner();
        sysBanner.setImageUrl(imageUrl);
        sysBanner.setType(Integer.valueOf(type));
        sysBanner.setBannerType(Integer.valueOf(bannerType));
        if(StringUtils.isNotEmpty(objId)) {
            sysBanner.setObjId(objId);
        }else{
            sysBanner.setObjId("0");
        }
        sysBanner.setRedirectType(redirectType);
        sysBanner.setSort(Integer.valueOf(sort));
        sysBanner.setTitle(title);
        sysBanner.setDescribe(describe);
        sysBanner.setLinkUrl(linkUrl);
        return sysBannerService.insert(sysBanner);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="轮播图",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String imageUrl = ServletRequestUtils.getStringParameter(request,"imageUrl","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String bannerType = ServletRequestUtils.getStringParameter(request,"bannerType","");
        String objId = ServletRequestUtils.getStringParameter(request,"objId","");
        int redirectType = ServletRequestUtils.getIntParameter(request,"redirectType",0);
        String sort = ServletRequestUtils.getStringParameter(request,"sort","1");
        String title = ServletRequestUtils.getStringParameter(request,"title","");
        String describe = ServletRequestUtils.getStringParameter(request,"describe","");
        String linkUrl = ServletRequestUtils.getStringParameter(request,"linkUrl","");
        if(StringUtils.isEmpty(imageUrl)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的图片地址为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(bannerType)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的轮播类型为空",null);
        }
        if(StringUtils.isEmpty(title)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的标题为空",null);
        }
        if(StringUtils.isEmpty(describe)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的描述为空",null);
        }
        SysBanner sysBanner=new SysBanner();
        sysBanner.setId(id);
        sysBanner.setImageUrl(imageUrl);
        sysBanner.setType(Integer.valueOf(type));
        sysBanner.setBannerType(Integer.valueOf(bannerType));
        if(StringUtils.isNotEmpty(objId)) {
            sysBanner.setObjId(objId);
        }else{
            sysBanner.setObjId("0");
        }
        sysBanner.setRedirectType(redirectType);
        sysBanner.setSort(Integer.valueOf(sort));
        sysBanner.setTitle(title);
        sysBanner.setDescribe(describe);
        sysBanner.setLinkUrl(linkUrl);
        return sysBannerService.update(sysBanner);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="轮播图",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return sysBannerService.delete(strarray);
            }else {
                return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE,"删除失败",null);
            }
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入Id为空",null);
        }

    }


    /**
     * 根据Id查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/getById")
    @OperLog(operationType="轮播图",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return sysBannerService.getBannerInfo(id);
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入Id为空",null);
        }
    }


    /**
     * 后台列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("page")
    @ResponseBody
    @OperLog(operationType="轮播图",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
        return sysBannerService.getBannerList(condition,pageHelper);
    }
}
