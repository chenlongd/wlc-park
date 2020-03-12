package com.perenc.xh.lsp.service.tcCarJsclientlog.impl;

import com.alibaba.fastjson.JSONObject;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.entity.tcCar.TcCarJsclientlog;
import com.perenc.xh.lsp.service.tcCarJsclientlog.TcCarJsclientlogService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


@Service("tcCarJsclientlogService")
@Transactional(rollbackFor = Exception.class)
public class TcCarJsclientlogServiceImpl implements TcCarJsclientlogService {

    private static final Logger logger = Logger.getLogger(TcCarJsclientlogServiceImpl.class);

    @Autowired
    private MongoComDAO mongoComDAO;

    @Override
    public ReturnJsonData insert(TcCarJsclientlog tcCarJsclientlog) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcCarJsclientlog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcCarJsclientlog.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcCarJsclientlog);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    /**
     * 增加捷顺访问日志
     * @param url
     * @param rparameter
     * @param jsonResut
     * @return
     * @throws Exception
     */
    @Override
    public Integer insertJsClientLog(String url,String rparameter,JSONObject jsonResut) throws Exception {
        Integer num=0;
        TcCarJsclientlog tjsclientlog=new TcCarJsclientlog();
        tjsclientlog.setType(1);
        tjsclientlog.setUrl(url);
        tjsclientlog.setRmode(1);
        tjsclientlog.setRparameter(rparameter);
        tjsclientlog.setContent(jsonResut.toString());
        tjsclientlog.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tjsclientlog.setStatus(1);
        num=mongoComDAO.executeInsert(tjsclientlog);
        return num;
    }


    @Override
    public ReturnJsonData update(TcCarJsclientlog tcCarJsclientlog) throws Exception {
        TcCarJsclientlog returnTclientlog = mongoComDAO.executeForObjectById(tcCarJsclientlog.getId(), TcCarJsclientlog.class);
        if(returnTclientlog != null){
            returnTclientlog.setType(tcCarJsclientlog.getType());
            returnTclientlog.setUrl(tcCarJsclientlog.getUrl());
            returnTclientlog.setRmode(tcCarJsclientlog.getRmode());
            returnTclientlog.setRparameter(tcCarJsclientlog.getRparameter());
            returnTclientlog.setIsNormal(tcCarJsclientlog.getIsNormal());
            returnTclientlog.setContent(tcCarJsclientlog.getContent());
            int flag = mongoComDAO.executeUpdate(returnTclientlog);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcCarJsclientlog.class);
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
        TcCarJsclientlog tJslientlog = mongoComDAO.executeForObjectById(id, TcCarJsclientlog.class);
        if (tJslientlog!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tJslientlog.getId());
            condition.put("type",tJslientlog.getType());
            condition.put("url",tJslientlog.getUrl());
            condition.put("rmode",tJslientlog.getRmode());
            condition.put("rparameter",tJslientlog.getRparameter());
            condition.put("isNormal",tJslientlog.getIsNormal());
            condition.put("content",tJslientlog.getContent());
            condition.put("carNum",tJslientlog.getCarNum());
            condition.put("remark",tJslientlog.getRemark());
            condition.put("status",tJslientlog.getStatus());
            condition.put("createTime",tJslientlog.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String carNum =  MapUtils.getString(map, "carNum");
        String startTime =  MapUtils.getString(map, "startTime");
        String endTime =  MapUtils.getString(map, "endTime");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //车牌模糊查询
        if(StringUtils.isNotEmpty(carNum)){
            Pattern pattern = Pattern.compile("^.*" + carNum + ".*$");
            Criteria criteria = Criteria.where("carNum").regex(pattern);
            criteriasa.add(criteria);
        }
        //创建开始时间-创建结束时间
        if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
            Criteria criteria = Criteria.where("createTime").gte(startTime).lte(endTime);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcCarJsclientlog> tcCarJsclientlogs = mongoComDAO.executeForObjectList(criteriasa,TcCarJsclientlog.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcCarJsclientlog.class);
        List<Map<String,Object>> TcCarJsclientloglist = new ArrayList<>();
        for(TcCarJsclientlog tJslientlog : tcCarJsclientlogs){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tJslientlog.getId());
            condition.put("type",tJslientlog.getType());
            condition.put("url",tJslientlog.getUrl());
            condition.put("rmode",tJslientlog.getRmode());
            condition.put("rparameter",tJslientlog.getRparameter());
            condition.put("isNormal",tJslientlog.getIsNormal());
            condition.put("content",tJslientlog.getContent());
            condition.put("carNum",tJslientlog.getCarNum());
            condition.put("remark",tJslientlog.getRemark());
            condition.put("status",tJslientlog.getStatus());
            condition.put("createTime",tJslientlog.getCreateTime());
            TcCarJsclientloglist.add(condition);
        }
        map.clear();
        map.put("list",TcCarJsclientloglist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }


    /**
     * 根据当前时间
     * 删除数据
     * 根据当前时间，日志15天前的日志删除
     */
    @Override
    public Integer removeBatchTcCarJsclientlogByEdate() throws Exception {
        Integer num=0;
        SimpleDateFormat sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar =null;
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -15);//当前时间前去天数,
        String  endTime=sdfhms.format(calendar.getTime()); //当前时间
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //结束时间小于等于当前时间
        Criteria criteria = Criteria.where("createTime").lte(endTime);
        criteriasa.add(criteria);
        num =+ mongoComDAO.executeDelete(criteriasa,TcCarJsclientlog.class);
        return num;
    }



}
