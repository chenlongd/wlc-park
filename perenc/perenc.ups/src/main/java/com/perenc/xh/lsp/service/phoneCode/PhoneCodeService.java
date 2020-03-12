package com.perenc.xh.lsp.service.phoneCode;

import com.aliyuncs.exceptions.ClientException;
import com.perenc.xh.commonUtils.model.ReturnJsonData;

import java.util.Map;


public interface PhoneCodeService {


    public ReturnJsonData getPhoneCode(Map<String, Object> map) throws ClientException;


    /**
     * 手机号验证
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getPhoneValidate(Map<String, Object> map) throws Exception;

}
