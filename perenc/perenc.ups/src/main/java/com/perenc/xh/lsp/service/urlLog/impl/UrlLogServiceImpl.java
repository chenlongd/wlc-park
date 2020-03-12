package com.perenc.xh.lsp.service.urlLog.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.entity.urlLog.UrlLog;
import com.perenc.xh.lsp.service.urlLog.UrlLogService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


@Service("urlLogService")
@Transactional(rollbackFor = Exception.class)
public class UrlLogServiceImpl implements UrlLogService {

    @Autowired
    private MongoComDAO mongoComDAO;


    @Override
    public ReturnJsonData insert(UrlLog urlLog) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        urlLog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        urlLog.setStatus(1);
        int flag = mongoComDAO.executeInsert(urlLog);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    /**
     * 返回对象
     * @param urlLog
     * @return
     * @throws Exception
     */
    @Override
    public UrlLog insertLog(UrlLog urlLog) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        urlLog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        urlLog.setStatus(1);
        int flag = mongoComDAO.executeInsert(urlLog);
        return urlLog;
    }


    @Override
    public ReturnJsonData update(UrlLog urlLog) throws Exception {
        UrlLog returnUrlLog = mongoComDAO.executeForObjectById(urlLog.getId(), UrlLog.class);
        if(returnUrlLog != null){
            returnUrlLog.setResponseStatus(urlLog.getResponseStatus());
            returnUrlLog.setResponseBody(urlLog.getResponseBody());
            returnUrlLog.setCostTime(urlLog.getCostTime());
            int flag = mongoComDAO.executeUpdate(returnUrlLog);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    @Override
    public ReturnJsonData delete(String[] ids) throws Exception {
         Map<String,Object> condition=new HashMap<>();
         condition.put("ids",ids);
         condition.put("status",2);
        int flag= mongoComDAO.executeUpdateByIds(condition, UrlLog.class);
        if (flag > 0) {
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "删除成功", null);
        } else {
            return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE, "删除失败", null);
        }
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public ReturnJsonData getById(String id) throws Exception {
        UrlLog urlLog = mongoComDAO.executeForObjectById(id, UrlLog.class);
        if (urlLog!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",urlLog.getId());
            condition.put("opId",urlLog.getOpId());
            condition.put("opName",urlLog.getOpId());
            condition.put("requestUrl",urlLog.getRequestUrl());
            condition.put("requestMethod",urlLog.getRequestMethod());
            condition.put("requestParams",urlLog.getRequestParams());
            condition.put("responseStatus",urlLog.getResponseStatus());
            condition.put("responseBody",urlLog.getResponseBody());
            condition.put("controllerName",urlLog.getControllerName());
            condition.put("controllerMethod",urlLog.getControllerMethod());
            condition.put("costTime",urlLog.getCostTime());
            condition.put("status",urlLog.getStatus());
            condition.put("createTime",urlLog.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String requestUrl =  MapUtils.getString(map, "requestUrl");
        String startTime =  MapUtils.getString(map, "startTime");
        String endTime =  MapUtils.getString(map, "endTime");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //车牌模糊查询
        if(StringUtils.isNotEmpty(requestUrl)){
            Pattern pattern = Pattern.compile("^.*" + requestUrl + ".*$");
            Criteria criteria = Criteria.where("requestUrl").regex(pattern);
            criteriasa.add(criteria);
        }
        //创建开始时间-创建结束时间
        if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
            Criteria criteria = Criteria.where("createTime").gte(startTime).lte(endTime);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<UrlLog> urlLogs = mongoComDAO.executeForObjectList(criteriasa,UrlLog.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, UrlLog.class);
        List<Map<String,Object>> urlLoglist = new ArrayList<>();
        for(UrlLog urlLog : urlLogs){
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",urlLog.getId());
            condition.put("opId",urlLog.getOpId());
            condition.put("opName",urlLog.getOpId());
            condition.put("requestUrl",urlLog.getRequestUrl());
            condition.put("requestMethod",urlLog.getRequestMethod());
            condition.put("requestParams",urlLog.getRequestParams());
            condition.put("responseStatus",urlLog.getResponseStatus());
            condition.put("responseBody",urlLog.getResponseBody());
            condition.put("controllerName",urlLog.getControllerName());
            condition.put("controllerMethod",urlLog.getControllerMethod());
            condition.put("costTime",urlLog.getCostTime());
            condition.put("status",urlLog.getStatus());
            condition.put("createTime",urlLog.getCreateTime());
            urlLoglist.add(condition);
        }
        map.clear();
        map.put("list",urlLoglist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }


    /**
     * 根据当前时间
     * 删除数据
     * 根据当前时间，日志1天前的日志删除
     */
    @Override
    public Integer removeBatchUrlLogByEdate() throws Exception {
        Integer num=0;
        SimpleDateFormat  sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar =null;
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);//当前时间前去天数,
        String  endTime=sdfhms.format(calendar.getTime()); //当前时间
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //结束时间小于等于当前时间
        Criteria criteria = Criteria.where("createTime").lte(endTime);
        criteriasa.add(criteria);
        num =+ mongoComDAO.executeDelete(criteriasa,UrlLog.class);
        return num;
    }



}
