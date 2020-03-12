package com.perenc.xh.lsp.service.phoneCode.impl;


import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.exceptions.ClientException;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.aliyunSms.AliyunSmsUtils;
import com.perenc.xh.lsp.entity.phoneCode.PhoneCode;
import com.perenc.xh.lsp.service.phoneCode.PhoneCodeService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("phoneCodeService")
@Transactional(rollbackFor = Exception.class)
public class PhoneCodeServiceImpl implements PhoneCodeService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Override
    public ReturnJsonData getPhoneCode(Map<String,Object> map) throws ClientException {
        Map<String,Object> result = new HashMap<>();
        String phone = MapUtils.getString(map, "phone", "");
        try {
            if(StringUtils.isNotEmpty(phone)){
                String code = Integer.toString(AliyunSmsUtils.getSixNewcode());
                CommonResponse response= AliyunSmsUtils.sendSms(phone,code);
                if(response!=null && response.getHttpStatus()==200) {
                    String data = response.getData();
                    JSONObject jsondate = (JSONObject) JSONObject.parseObject(data);
                    if(jsondate.get("Code")!= null && jsondate.get("Code").equals("OK")){
                    /*System.out.println("Code=" + jsondate.getString("Code"));
                    System.out.println("Message=" + jsondate.getString("Message"));
                    System.out.println("RequestId=" + jsondate.getString("RequestId"));
                    System.out.println("BizId=" + jsondate.getString("BizId"));*/
                        PhoneCode phoneCode=new PhoneCode();
                        phoneCode.setPhone(phone);
                        phoneCode.setType(1);
                        phoneCode.setCode(code);
                        phoneCode.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                        mongoComDAO.executeInsert(phoneCode);
                        result.put("code",code);
                        System.out.println("短信发送成功！");
                        return new ReturnJsonData(DataCodeUtil.SUCCESS,"短信发送成功",result);
                    }else {
                        return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE,"短信发送失败",null);
                    }
                }else  {
                    return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE,"短信发送失败",null);
                }
            }
        }catch (Exception e) {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE,"短信发送失败",null);
        }
        return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE,"短信发送失败",null);
    }



    /**
     * 手机号验证
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getPhoneValidate(Map<String, Object> map) throws Exception {
        String phone = MapUtils.getString(map, "phone", "");
        String code = MapUtils.getString(map, "code", "");
        List<Criteria> criterias = new ArrayList<Criteria>();
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        try {
            orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
            if(StringUtils.isNotEmpty(phone)){
                criterias.add(Criteria.where("phone").is(phone));
            }
            List<PhoneCode> phoneCodeList = mongoComDAO.executeForObjectList(criterias,PhoneCode.class,orders);
            if(phoneCodeList.size()>0) {
                PhoneCode phoneCode=phoneCodeList.get(0);
                if (code.equals(phoneCode.getCode())) {
                    return new ReturnJsonData(DataCodeUtil.SUCCESS,"验证码正确",null);
                }else {
                    return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的验证码",null);
                }
            }
        }catch (Exception e) {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE,"验证码失败",null);
        }
        return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE,"验证码失败",null);
    }







}
