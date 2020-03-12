package com.perenc.xh.lsp.service.weChatInfo.impl;


import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.dao.merchat.MchAccountDao;
import com.perenc.xh.lsp.entity.merchat.MchAccount;
import com.perenc.xh.lsp.entity.weChatInfo.WeChatInfo;
import com.perenc.xh.lsp.service.weChatInfo.WeChatInfoService;
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
import java.util.regex.Pattern;

/**
 * @Description 微信相关信息
 * @Author xiaobai
 * @Date 2019/5/20 16:22
 **/
@Service("weChatInfoService")
@Transactional(rollbackFor = Exception.class)
public class WeChatInfoServiceImpl implements WeChatInfoService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private MchAccountDao mchAccountDao;

    /**
     * 添加
     * @param weChatInfo
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insert(WeChatInfo weChatInfo) throws Exception {
        //先判断APPID是否存在
        Map<String,Object> condition = new HashMap<>();
        condition.put("appId",weChatInfo.getAppId());
        WeChatInfo chatInfo = mongoComDAO.executeForObjectByCon(condition, WeChatInfo.class);
        if(chatInfo == null){
            weChatInfo.setStatus(1);
            weChatInfo.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = mongoComDAO.executeInsert(weChatInfo);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加微信息成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"该微信已经添加",null);
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加微信信息失败",null);
    }

    /**
     * 修改
     * @param weChatInfo
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData update(WeChatInfo weChatInfo) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        condition.put("id",weChatInfo.getId());
        WeChatInfo returnWeChatInfo = mongoComDAO.executeForObjectByCon(condition, WeChatInfo.class);
        if(returnWeChatInfo != null){
            returnWeChatInfo.setMchAccountId(weChatInfo.getMchAccountId());
            returnWeChatInfo.setWeCahtName(weChatInfo.getWeCahtName());
            returnWeChatInfo.setAppId(weChatInfo.getAppId());
            returnWeChatInfo.setAppSecret(weChatInfo.getAppSecret());
            returnWeChatInfo.setMchId(weChatInfo.getMchId());
            returnWeChatInfo.setMerchantSecretKey(weChatInfo.getMerchantSecretKey());
            returnWeChatInfo.setNotifyUrl(weChatInfo.getNotifyUrl());
            returnWeChatInfo.setRefundNotifyUrl(weChatInfo.getRefundNotifyUrl());
            returnWeChatInfo.setApiclientCertP12(weChatInfo.getApiclientCertP12());
            returnWeChatInfo.setApiclientCertPem(weChatInfo.getApiclientCertPem());
            returnWeChatInfo.setApiclientKeyPem(weChatInfo.getApiclientKeyPem());
            returnWeChatInfo.setWxPublicCheckFile(weChatInfo.getWxPublicCheckFile());
            returnWeChatInfo.setMiniCheckFile(weChatInfo.getMiniCheckFile());
            returnWeChatInfo.setStatus(weChatInfo.getStatus());
            returnWeChatInfo.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = mongoComDAO.executeUpdate(returnWeChatInfo);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改数据成功",null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改数据失败",null);
    }

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData delete(List<String> ids) throws Exception {
        boolean flag = true;
        for(String id : ids){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",id);
            int i = mongoComDAO.executeDelete(condition, WeChatInfo.class);
            if(i <= 0){
                flag = false;
            }
        }
        if(flag){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"删除数据成功",null);
        }else {
            return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE,"删除数据失败",null);
        }
    }

    /**
     * 获取单个详情
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getWechatInfo(String id) throws Exception {
        Map<String,Object> map = new HashMap<>();
        WeChatInfo weChatInfo = mongoComDAO.executeForObjectById(id, WeChatInfo.class);
        if(weChatInfo != null){
            map.put("id",weChatInfo.getId());
            //商家账号信息
            int accountId = weChatInfo.getMchAccountId();
            if(accountId > 0) {
                MchAccount account = mchAccountDao.getById(accountId);
                if (account != null) {
                    map.put("accountId", weChatInfo.getMchAccountId());
                    map.put("mchName", account.getMchName());
                } else {
                    map.put("accountId", "");
                    map.put("mchName", "");
                }
            }else{
                map.put("accountId", "");
                map.put("mchName", "");
            }
            map.put("weCahtName", weChatInfo.getWeCahtName());
            map.put("appId",weChatInfo.getAppId());
            map.put("appSecret",weChatInfo.getAppSecret());
            map.put("mchId",weChatInfo.getMchId());
            map.put("merchantSecretKey",weChatInfo.getMerchantSecretKey());
            map.put("notifyUrl",weChatInfo.getNotifyUrl());
            map.put("refundNotifyUrl",weChatInfo.getRefundNotifyUrl());
            map.put("apiclientCertP12",weChatInfo.getApiclientCertP12());
            map.put("apiclientCertPem",weChatInfo.getApiclientCertPem());
            map.put("apiclientKeyPem",weChatInfo.getApiclientKeyPem());;
            map.put("wxPublicCheckFile",weChatInfo.getWxPublicCheckFile());
            map.put("miniCheckFile",weChatInfo.getMiniCheckFile());
            map.put("status",weChatInfo.getStatus());
            map.put("createTime",weChatInfo.getCreateTime());
        }
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

    /**
     * 获取微信相关信息
     * @param condition
     * @param pageHelper
     * @return
     */
    @Override
    public ReturnJsonData getWeChatInfoList(Map<String, Object> condition, PageHelper pageHelper) {
        String appId =  MapUtils.getString(condition, "appId");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //小程序或者微信公众号APPID
        if(StringUtils.isNotEmpty(appId)) {
            Pattern pattern = Pattern.compile("^.*" + appId + ".*$");
            Criteria criteria = Criteria.where("appId").regex(pattern);
            criteriasa.add(criteria);
        }
        List<WeChatInfo> weChatInfos = mongoComDAO.executeForObjectList(criteriasa, WeChatInfo.class, pageHelper, orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, WeChatInfo.class);
        List<Map<String,Object>> weChatInfoList = new ArrayList<>();
        for(WeChatInfo weChatInfo : weChatInfos){
            Map<String,Object> map = new HashMap<>();
            map.put("id",weChatInfo.getId());
            //商家账号信息
            int accountId = weChatInfo.getMchAccountId();
            if(accountId > 0) {
                MchAccount account = mchAccountDao.getById(accountId);
                if (account != null) {
                    map.put("accountId", weChatInfo.getMchAccountId());
                    map.put("mchName", account.getMchName());
                } else {
                    map.put("accountId", "");
                    map.put("mchName", "");
                }
            }else{
                map.put("accountId", "");
                map.put("mchName", "");
            }
            map.put("weCahtName", weChatInfo.getWeCahtName());
            map.put("appId",weChatInfo.getAppId());
            map.put("appSecret",weChatInfo.getAppSecret());
            map.put("mchId",weChatInfo.getMchId());
            map.put("merchantSecretKey",weChatInfo.getMerchantSecretKey());
            map.put("notifyUrl",weChatInfo.getNotifyUrl());
            map.put("refundNotifyUrl",weChatInfo.getRefundNotifyUrl());
            map.put("apiclientCertP12",weChatInfo.getApiclientCertP12());
            map.put("apiclientCertPem",weChatInfo.getApiclientCertPem());
            map.put("apiclientKeyPem",weChatInfo.getApiclientKeyPem());;
            map.put("wxPublicCheckFile",weChatInfo.getWxPublicCheckFile());
            map.put("miniCheckFile",weChatInfo.getMiniCheckFile());
            map.put("status",weChatInfo.getStatus());
            map.put("createTime",weChatInfo.getCreateTime());
            weChatInfoList.add(map);
        }
        condition.clear();
        condition.put("list",weChatInfoList);//返回前端集合命名为list
        condition.put("total",count);//总条数
        condition.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
    }
}
