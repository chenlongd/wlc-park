package com.perenc.xh.lsp.service.wxCustomer.impl;



import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.DBRelevant.InsertParam;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.model.PhoneReturnJson;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.dao.wxCustomer.WxCustomerDao;
import com.perenc.xh.lsp.entity.wxCustomer.WmCustomer;
import com.perenc.xh.lsp.service.wxCustomer.WxCustomerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("wmCustomerService")
@Transactional(rollbackFor=Exception.class)
public class WxCustomerServiceImpl implements WxCustomerService {

    private static Logger logger = Logger.getLogger(WxCustomerServiceImpl.class);

    @Autowired(required = false)
    private WxCustomerDao wmCustomerDao;



    @Override
    public int insertWmCustomer(WmCustomer customer) {
        InsertParam param = DBUtil.toInsertParam(customer);
        int flag = wmCustomerDao.add(param);
        return flag;
    }


    @Override
    public WmCustomer getWmCustomer(QueryParam param) {
        return wmCustomerDao.getOne(param);
    }

    @Override
    public WmCustomer getById(int id) {
        QueryParam param = new QueryParam();
        param.put("id",id);
        return wmCustomerDao.getOne(param);
    }

    @Override
    public PhoneReturnJson update(WmCustomer wmCustomer) {
        WmCustomer customer = wmCustomerDao.getById(wmCustomer.getId());
        if(customer != null) {
            customer.setIsUseWebSocket(wmCustomer.getIsUseWebSocket());
            customer.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = wmCustomerDao.update(DBUtil.toUpdateParam(customer, "id"));
            if(flag > 0){
                return new PhoneReturnJson(true,"添加远程演示小慧权限成功",null);
            }
        }
        return new PhoneReturnJson(false,"添加远程演示小慧权限失败",null);
    }

    @Override
    public List<Map<String,Object>> getWmCustomerList(QueryParam param) {
        List<WmCustomer> customers = wmCustomerDao.list(param);
        int count = wmCustomerDao.count(param);
        List<Map<String,Object>> customerList = new ArrayList<>();
        for(WmCustomer customer : customers){
            Map<String,Object> condition = new HashMap<>();
            condition.put("customerId",customer.getId());
            condition.put("nickName",customer.getNickName());
            condition.put("headImgUrl",customer.getHeadImgUrl());
            condition.put("sex",customer.getSex());
            condition.put("isUseWebSocket",customer.getIsUseWebSocket());
            condition.put("createTime",customer.getCreateTime());
            condition.put("count",count);
            customerList.add(condition);
        }
        return customerList;
    }


}