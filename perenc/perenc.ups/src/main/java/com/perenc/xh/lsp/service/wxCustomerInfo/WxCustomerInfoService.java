package com.perenc.xh.lsp.service.wxCustomerInfo;

import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.entity.wxCustomerInfo.WmCustomerInfo;


import java.util.Map;

public interface WxCustomerInfoService {

    ReturnJsonData insertWmCustomerInfo(WmCustomerInfo wmCustomerInfo) throws Exception;

    WmCustomerInfo getOne(QueryParam param) throws Exception;

    ReturnJsonData loginMiniWxUser(Map<String, Object> map) throws Exception;

    ReturnJsonData authMiniWxPhone(Map<String, Object> map) throws Exception;

    /**
     * 停车场小程序认证电话号码
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData authTcMiniWxPhone(Map<String, Object> map) throws Exception;

    /**
     * 停车场获取微信小程序获取用户信息
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData loginTcMiniWxUser(Map<String, Object> map) throws Exception;

    /**
     * 停车场商户端小程序认证电话号码
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData authTcSellerMiniWxPhone(Map<String, Object> map) throws Exception;

}
