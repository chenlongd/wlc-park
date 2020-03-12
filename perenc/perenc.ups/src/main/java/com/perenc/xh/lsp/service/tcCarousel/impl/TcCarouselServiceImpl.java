package com.perenc.xh.lsp.service.tcCarousel.impl;


import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.entity.tcCarousel.TcCarousel;
import com.perenc.xh.lsp.service.tcCarousel.TcCarouselService;
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


@Service("tcCarouselService")
@Transactional(rollbackFor = Exception.class)
public class TcCarouselServiceImpl implements TcCarouselService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Override
    public ReturnJsonData insert(TcCarousel tcCarousel) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcCarousel.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcCarousel.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcCarousel);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcCarousel tcCarousel) throws Exception {
        TcCarousel returnTcCarousel = mongoComDAO.executeForObjectById(tcCarousel.getId(), TcCarousel.class);
        if(returnTcCarousel != null){
            returnTcCarousel.setSubType(tcCarousel.getSubType());
            returnTcCarousel.setType(tcCarousel.getType());
            returnTcCarousel.setImage(tcCarousel.getImage());
            returnTcCarousel.setUrl(tcCarousel.getUrl());
            returnTcCarousel.setSort(tcCarousel.getSort());
            int flag = mongoComDAO.executeUpdate(returnTcCarousel);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    @Override
    public ReturnJsonData delete(String[] ids) throws Exception {
         Map<String,Object> condition=new HashMap<>();
         condition.put("ids",ids);
         condition.put("status",2);
        int flag= mongoComDAO.executeUpdateByIds(condition, TcCarousel.class);
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
        TcCarousel tcCarousel = mongoComDAO.executeForObjectById(id, TcCarousel.class);
        if (tcCarousel != null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcCarousel.getId());
            condition.put("subType",tcCarousel.getSubType());
            condition.put("type",tcCarousel.getType());
            condition.put("image",tcCarousel.getImage());
            condition.put("url",tcCarousel.getUrl());
            condition.put("sort",tcCarousel.getSort());
            condition.put("status",tcCarousel.getStatus());
            condition.put("createTime",tcCarousel.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String subType =  MapUtils.getString(map, "subType");
        String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"sort"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //所属类型
        if(StringUtils.isNotEmpty(subType)){
            Criteria criteria = Criteria.where("subType").is(Integer.valueOf(subType));
            criteriasa.add(criteria);
        }
        //跳转类型
        if(StringUtils.isNotEmpty(type)){
            Criteria criteria = Criteria.where("type").is(Integer.valueOf(type));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcCarousel> tcCarousels = mongoComDAO.executeForObjectList(criteriasa,TcCarousel.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcCarousel.class);
        List<Map<String,Object>> tcCarousellist = new ArrayList<>();
        for(TcCarousel tcCarousel : tcCarousels){
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
        map.clear();
        map.put("list",tcCarousellist);//返回前端集合命名为list
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
     */
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
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcCarousel.class);
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


}
