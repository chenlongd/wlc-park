package com.perenc.xh.lsp.service.operLog.impl;


import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.PhoneReturnJson;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLogRecord;
import com.perenc.xh.lsp.service.operLog.OperLogRecordService;
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
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/25 10:06
 **/
@Service("operLogRecordService")
@Transactional(rollbackFor = Exception.class)
public class OperLogRecordServiceImpl implements OperLogRecordService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Override
    public PhoneReturnJson insert(OperLogRecord operLogRecord) throws Exception {
        int flag = mongoComDAO.executeInsert(operLogRecord);
        if(flag > 0) {
            return new PhoneReturnJson(true,"添加操作日志成功",null);
        }
        return new PhoneReturnJson(false,"添加操作日志失败",null);
    }

    @Override
    public PhoneReturnJson delete(String id) throws Exception {
        return null;
    }

    @Override
    public PhoneReturnJson getOperLogRecordList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String username =  MapUtils.getString(map, "username");
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //操作者
        if(StringUtils.isNotEmpty(username)) {
            Pattern pattern = Pattern.compile("^.*" + username + ".*$");
            Criteria criteria = Criteria.where("operationUser").regex(pattern);
            criteriasa.add(criteria);
        }
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"operationStartTime"));
        List<Map<String,Object>> list = new ArrayList<>();
        long count = mongoComDAO.executeForObjectListCount(criteriasa, OperLogRecord.class);
        List<OperLogRecord> operLogRecords = mongoComDAO.executeForObjectList(criteriasa, OperLogRecord.class, pageHelper, orders);
        for(OperLogRecord record : operLogRecords){
            Map<String,Object> returnMap = new HashMap<>();
            returnMap.put("id",record.getId());
            returnMap.put("username",record.getOperationUser());
            returnMap.put("operClassName",record.getOperationType());
            returnMap.put("operClassMethod",record.getOperationName());
            returnMap.put("operStartTime",record.getOperationStartTime());
            returnMap.put("operEndTime",record.getOperationEndTime());
            list.add(returnMap);
        }
        map.clear();
        map.put("list",list);
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new PhoneReturnJson(true,"获取数据成功",map);
    }
}
