package com.perenc.xh.lsp.controller.admin.weChatInfo;


import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.entity.weChatInfo.WeChatInfo;
import com.perenc.xh.lsp.service.weChatInfo.WeChatInfoService;
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
 * @Description 微信相关信息
 * @Author xiaobai
 * @Date 2019/5/20 17:05
 **/
@Controller
@RequestMapping("weChatInfo")
public class AdminWeChatInfoController {

    @Autowired(required = false)
    private WeChatInfoService weChatInfoService;

    /**
     * 添加
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        WeChatInfo weChatInfo = new WeChatInfo();

        String weCahtName = ServletRequestUtils.getStringParameter(request, "weCahtName", "");
        if(StringUtils.isEmpty(weCahtName)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的微信名称为空",null);
        }
        weChatInfo.setWeCahtName(weCahtName);

        String mchAccountId = ServletRequestUtils.getStringParameter(request, "mchAccountId", "");
        if(StringUtils.isEmpty(mchAccountId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商户账号为空",null);
        }
        weChatInfo.setMchAccountId(Integer.valueOf(mchAccountId));

        String appId = ServletRequestUtils.getStringParameter(request, "appId", "");
        if(StringUtils.isEmpty(appId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的appId为空",null);
        }
        weChatInfo.setAppId(appId);

        String appSecret = ServletRequestUtils.getStringParameter(request, "appSecret", "");
        if(StringUtils.isEmpty(appSecret)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的app秘钥为空",null);
        }
        weChatInfo.setAppSecret(appSecret);

        String mchId = ServletRequestUtils.getStringParameter(request, "mchId", "");
        weChatInfo.setMchId(mchId);

        String merchantSecretKey = ServletRequestUtils.getStringParameter(request, "merchantSecretKey", "");
        weChatInfo.setMerchantSecretKey(merchantSecretKey);

        String notifyUrl = ServletRequestUtils.getStringParameter(request, "notifyUrl", "");
        weChatInfo.setNotifyUrl(notifyUrl);

        String refundNotifyUrl = ServletRequestUtils.getStringParameter(request, "refundNotifyUrl", "");
        weChatInfo.setRefundNotifyUrl(refundNotifyUrl);

        String apiclientCertP12 = ServletRequestUtils.getStringParameter(request, "apiclientCertP12", "");
        weChatInfo.setApiclientCertP12(apiclientCertP12);

        String apiclientCertPem = ServletRequestUtils.getStringParameter(request, "apiclientCertPem", "");
        weChatInfo.setApiclientCertPem(apiclientCertPem);

        String apiclientKeyPem = ServletRequestUtils.getStringParameter(request, "apiclientKeyPem", "");
        weChatInfo.setApiclientKeyPem(apiclientKeyPem);

        String wxPublicCheckFile = ServletRequestUtils.getStringParameter(request, "wxPublicCheckFile", "");
        weChatInfo.setWxPublicCheckFile(wxPublicCheckFile);

        String miniCheckFile = ServletRequestUtils.getStringParameter(request, "miniCheckFile", "");
        weChatInfo.setMiniCheckFile(miniCheckFile);

        int useType = ServletRequestUtils.getIntParameter(request, "useType", 0);
        weChatInfo.setUseType(useType);

        return weChatInfoService.insert(weChatInfo);
    }

    /**
     * 修改
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        WeChatInfo weChatInfo = new WeChatInfo();
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的id为空",null);
        }
        weChatInfo.setId(id);

        String weCahtName = ServletRequestUtils.getStringParameter(request, "weCahtName", "");
        if(StringUtils.isEmpty(weCahtName)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的微信名称为空",null);
        }
        weChatInfo.setWeCahtName(weCahtName);

        String mchAccountId = ServletRequestUtils.getStringParameter(request, "mchAccountId", "");
        if(StringUtils.isEmpty(mchAccountId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商户账号为空",null);
        }
        weChatInfo.setMchAccountId(Integer.valueOf(mchAccountId));

        String appId = ServletRequestUtils.getStringParameter(request, "appId", "");
        if(StringUtils.isEmpty(appId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的appId为空",null);
        }
        weChatInfo.setAppId(appId);

        String appSecret = ServletRequestUtils.getStringParameter(request, "appSecret", "");
        if(StringUtils.isEmpty(appSecret)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的app秘钥为空",null);
        }
        weChatInfo.setAppSecret(appSecret);

        String mchId = ServletRequestUtils.getStringParameter(request, "mchId", "");
        weChatInfo.setMchId(mchId);

        String merchantSecretKey = ServletRequestUtils.getStringParameter(request, "merchantSecretKey", "");
        weChatInfo.setMerchantSecretKey(merchantSecretKey);

        String notifyUrl = ServletRequestUtils.getStringParameter(request, "notifyUrl", "");
        weChatInfo.setNotifyUrl(notifyUrl);

        String refundNotifyUrl = ServletRequestUtils.getStringParameter(request, "refundNotifyUrl", "");
        weChatInfo.setRefundNotifyUrl(refundNotifyUrl);

        String apiclientCertP12 = ServletRequestUtils.getStringParameter(request, "apiclientCertP12", "");
        weChatInfo.setApiclientCertP12(apiclientCertP12);

        String apiclientCertPem = ServletRequestUtils.getStringParameter(request, "apiclientCertPem", "");
        weChatInfo.setApiclientCertPem(apiclientCertPem);

        String apiclientKeyPem = ServletRequestUtils.getStringParameter(request, "apiclientKeyPem", "");
        weChatInfo.setApiclientKeyPem(apiclientKeyPem);

        String wxPublicCheckFile = ServletRequestUtils.getStringParameter(request, "wxPublicCheckFile", "");
        weChatInfo.setWxPublicCheckFile(wxPublicCheckFile);

        String miniCheckFile = ServletRequestUtils.getStringParameter(request, "miniCheckFile", "");
        weChatInfo.setMiniCheckFile(miniCheckFile);

        int useType = ServletRequestUtils.getIntParameter(request, "useType", 0);
        weChatInfo.setUseType(useType);

        return weChatInfoService.update(weChatInfo);
    }

    /**
     * 删除
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    public ReturnJsonData delete(HttpServletRequest request, HttpServletResponse response) throws Exception{
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
        return weChatInfoService.delete(idList);
    }

    /**
     * 根据条件查询
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("page")
    @ResponseBody
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, Object> condition = new HashMap<>();
        String appId = ServletRequestUtils.getStringParameter(request, "appId", "");
        if (StringUtils.isNotEmpty(appId)) {
            condition.put("appId", appId);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", 20);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return weChatInfoService.getWeChatInfoList(condition,pageHelper);
    }
}
