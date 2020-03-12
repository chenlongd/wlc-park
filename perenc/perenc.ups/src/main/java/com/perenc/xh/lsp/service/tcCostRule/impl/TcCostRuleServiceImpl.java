package com.perenc.xh.lsp.service.tcCostRule.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.entity.tcParklot.TcCostRule;
import com.perenc.xh.lsp.entity.tcParklot.TcParklot;
import com.perenc.xh.lsp.service.tcCostRule.TcCostRuleService;
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


@Service("tcCostRuleService")
@Transactional(rollbackFor = Exception.class)
public class TcCostRuleServiceImpl implements TcCostRuleService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Override
    public ReturnJsonData insert(TcCostRule tcCostRule) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcCostRule.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcCostRule.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcCostRule);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcCostRule tcCostRule) throws Exception {
        TcCostRule returnTcCostRule = mongoComDAO.executeForObjectById(tcCostRule.getId(), TcCostRule.class);
        if(returnTcCostRule != null){
            returnTcCostRule.setParklotId(tcCostRule.getParklotId());
            returnTcCostRule.setUnitPrice(tcCostRule.getUnitPrice());
            returnTcCostRule.setMaxHour(tcCostRule.getMaxHour());
            returnTcCostRule.setMinHour(tcCostRule.getMinHour());
            int flag = mongoComDAO.executeUpdate(returnTcCostRule);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcCostRule.class);
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
        TcCostRule tcCostRule = mongoComDAO.executeForObjectById(id, TcCostRule.class);
        if (tcCostRule!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcCostRule.getId());
            condition.put("parklotId",tcCostRule.getParklotId());
            TcParklot tcParklot = mongoComDAO.executeForObjectById(tcCostRule.getParklotId(), TcParklot.class);
            if(tcParklot != null){
                condition.put("parklotName",tcParklot.getName());
                condition.put("parklotNumber",tcParklot.getNumber());
            }else{
                condition.put("parklotName","");
                condition.put("parklotNumber","");
            }
            condition.put("unitPrice", ToolUtil.getIntToDouble(tcCostRule.getUnitPrice()));
            condition.put("maxHour", ToolUtil.getIntToDouble(tcCostRule.getMaxHour()));
            condition.put("minHour", ToolUtil.getIntToDouble(tcCostRule.getMinHour()));
            condition.put("descp",tcParklot.getDescp());
            condition.put("status",tcCostRule.getStatus());
            condition.put("createTime",tcCostRule.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String parklotId =  MapUtils.getString(map, "parklotId");
        //String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //所属类型
        if(StringUtils.isNotEmpty(parklotId)){
            Criteria criteria = Criteria.where("parklotId").is(Integer.valueOf(parklotId));
            criteriasa.add(criteria);
        }
        //跳转类型
        /*if(StringUtils.isNotEmpty(type)){
            Criteria criteria = Criteria.where("type").is(Integer.valueOf(type));
            criteriasa.add(criteria);
        }*/
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcCostRule> tcCostRules = mongoComDAO.executeForObjectList(criteriasa, TcCostRule.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcCostRule.class);
        List<Map<String,Object>> tcCostRulelist = new ArrayList<>();
        for(TcCostRule tcCostRule : tcCostRules){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcCostRule.getId());
            condition.put("parklotId",tcCostRule.getParklotId());
            TcParklot tcParklot = mongoComDAO.executeForObjectById(tcCostRule.getParklotId(), TcParklot.class);
            if(tcParklot != null){
                condition.put("parklotName",tcParklot.getName());
                condition.put("parklotNumber",tcParklot.getNumber());
            }else{
                condition.put("parklotName","");
                condition.put("parklotNumber","");
            }
            condition.put("unitPrice", ToolUtil.getIntToDouble(tcCostRule.getUnitPrice()));
            condition.put("maxHour", ToolUtil.getIntToDouble(tcCostRule.getMaxHour()));
            condition.put("minHour", ToolUtil.getIntToDouble(tcCostRule.getMinHour()));
            condition.put("descp",tcParklot.getDescp());
            condition.put("status",tcCostRule.getStatus());
            condition.put("createTime",tcCostRule.getCreateTime());
            tcCostRulelist.add(condition);
        }
        map.clear();
        map.put("list",tcCostRulelist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

    /**
     * 前端查询
     * 轮播图
     * @param map
     * @return
     * @throws Exception
     *//*
    @Override
    public ReturnJsonData getTcCarOuselList(Map<String, Object> map) throws Exception {
        String subType =  MapUtils.getString(map, "subType");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"sort"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //所属类型
        if(StringUtils.isNotEmpty(subType)){
            Criteria criteria = Criteria.where("subType").is(subType);
            criteriasa.add(criteria);
        }
        String status =  MapUtils.getString(map, "status");
        //状态
        if(StringUtils.isNotEmpty(status)){
            Criteria criteria = Criteria.where("status").is(Integer.valueOf(status));
            criteriasa.add(criteria);
        }
        List<TcCarousel> TcCarousels = mongoComDAO.executeForObjectList(criteriasa,TcCarousel.class,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, Notice.class);
        List<Map<String,Object>> tcCarousellist = new ArrayList<>();
        for(TcCarousel tcCarousel : TcCarousels){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcCarousel.getId());
            condition.put("subType",tcCarousel.getSubType());
            condition.put("type",tcCarousel.getType());
            condition.put("image",tcCarousel.getImage());
            condition.put("url",tcCarousel.getUrl());
            condition.put("sort",tcCarousel.getSort());
            condition.put("status",tcCarousel.getStatus());
            condition.put("createTime",tcCarousel.getCreateTime());
            tcCarousellist.add(condition);
        }
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",tcCarousellist);
    }
*/

}
