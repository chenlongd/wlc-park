package com.perenc.xh.lsp.service.banner.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.entity.banner.SysBanner;
import com.perenc.xh.lsp.service.banner.SysBannerService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 系统轮播图
 * @Author xiaobai
 * @Date 2019/5/23 16:26
 **/
@Service("sysBannerService")
@Transactional(rollbackFor = Exception.class)
public class SysBannerServiceImpl implements SysBannerService {

    @Autowired
    private MongoComDAO mongoComDAO;

    /**
     * 添加
     * @param banner
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insert(SysBanner banner) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        if(StringUtils.isNotEmpty(banner.getObjId())) {
            condition.put("objId", banner.getObjId());
        }else{
            condition.put("objId", "0");//平台
        }
        condition.put("type",banner.getType());
        condition.put("status",1);
        List<SysBanner> bannerList = mongoComDAO.executeForObjectList(condition, SysBanner.class);
        if(bannerList.size() >= 5){
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"该轮播图已经达到上限",null);
        }else{
            if(StringUtils.isEmpty(banner.getObjId())) {
                banner.setObjId("0");//平台
            }
            banner.setStatus(1);
            banner.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            banner.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = mongoComDAO.executeInsert(banner);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加轮播图成功",null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加轮播图失败",null);
    }

    /**
     * 修改
     * @param banner
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData update(SysBanner banner) throws Exception {
        SysBanner returnBanner = mongoComDAO.executeForObjectById(banner.getId(), SysBanner.class);
        if(returnBanner != null){
            returnBanner.setImageUrl(banner.getImageUrl());
            returnBanner.setType(banner.getType());
            returnBanner.setBannerType(banner.getBannerType());
            returnBanner.setObjId(banner.getObjId());
            returnBanner.setRedirectType(banner.getRedirectType());
            returnBanner.setSort(banner.getSort());
            returnBanner.setTitle(banner.getTitle());
            returnBanner.setDescribe(banner.getDescribe());
            returnBanner.setLinkUrl(banner.getLinkUrl());
//            returnBanner.setStatus(banner.getStatus());
            returnBanner.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = mongoComDAO.executeUpdate(returnBanner);
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
    public ReturnJsonData delete(String[] ids) throws Exception {
        Map<String,Object> condition=new HashMap<>();
        condition.put("ids",ids);
        condition.put("status",2);
        int flag= mongoComDAO.executeUpdateByIds(condition, SysBanner.class);
        if (flag > 0) {
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "删除成功", null);
        } else {
            return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE, "删除失败", null);
        }
    }


    /**
     * 获取详情
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getBannerInfo(String id) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        SysBanner banner = mongoComDAO.executeForObjectById(id, SysBanner.class);
        if(banner != null){
            condition.put("id",banner.getId());
            condition.put("imageUrl",banner.getImageUrl());
            condition.put("type",banner.getType());
            condition.put("bannerType",banner.getBannerType());
            condition.put("objId", banner.getObjId());
            condition.put("redirectType",banner.getRedirectType());
            condition.put("sort",banner.getSort());
            condition.put("title",banner.getTitle());
            condition.put("describe",banner.getDescribe());
            condition.put("createTime",banner.getCreateTime());
            condition.put("updateTime",banner.getUpdateTime());
            condition.put("linkUrl",banner.getLinkUrl());
            condition.put("status",banner.getStatus());
        }else{
            condition.put("goodsPrice","");
            condition.put("goodsDiscountPrice","");
            condition.put("id","");
            condition.put("imageUrl","");
            condition.put("type","");
            condition.put("bannerType","");
            condition.put("objId","");
            condition.put("redirectType","");
            condition.put("sort","");
            condition.put("title","");
            condition.put("describe","");
            condition.put("createTime","");
            condition.put("updateTime","");
            condition.put("linkUrl","");
            condition.put("status","");
        }
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",condition);
    }

    @Override
    public ReturnJsonData getBannerList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        //创建对象查询
        String objId = MapUtils.getString(map, "objId", "");
        if(StringUtils.isNotEmpty(objId)) {
            Criteria criteria = Criteria.where("objId").is(objId);
            criteriasa.add(criteria);
        }else{
            Criteria criteria = Criteria.where("objId").is("0");
            criteriasa.add(criteria);
        }
        //所属类型
        String type =  MapUtils.getString(map, "type");
        if(StringUtils.isNotEmpty(type)){
            Criteria criteria = Criteria.where("type").is(Integer.valueOf(type));
            criteriasa.add(criteria);
        }
        //轮播类型
        String bannerType =  MapUtils.getString(map, "bannerType");
        if(StringUtils.isNotEmpty(bannerType)){
            Criteria criteria = Criteria.where("bannerType").is(Integer.valueOf(bannerType));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<SysBanner> sysBanners = mongoComDAO.executeForObjectList(criteriasa,SysBanner.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, SysBanner.class);
        List<Map<String,Object>> bannerlist = new ArrayList<>();
        for(SysBanner banner : sysBanners){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",banner.getId());
            condition.put("imageUrl",banner.getImageUrl());
            condition.put("type",banner.getType());
            condition.put("bannerType",banner.getBannerType());
            condition.put("objId",banner.getObjId());
            condition.put("redirectType",banner.getRedirectType());
            condition.put("sort",banner.getSort());
            condition.put("title",banner.getTitle());
            condition.put("describe",banner.getDescribe());
            condition.put("createTime",banner.getCreateTime());
            condition.put("updateTime",banner.getUpdateTime());
            condition.put("linkUrl",banner.getLinkUrl());
            condition.put("status",banner.getStatus());
            bannerlist.add(condition);
        }
        map.clear();
        map.put("list",bannerlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

    /**
     * 条件筛选列表
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getAllBannerList(Map<String, Object> map) throws Exception {
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        //创建对象查询
        String objId = MapUtils.getString(map, "objId", "");
        if(StringUtils.isNotEmpty(objId)) {
            Criteria criteria = Criteria.where("objId").is(objId);
            criteriasa.add(criteria);
        }
        //所属类型
        String type =  MapUtils.getString(map, "type");
        if(StringUtils.isNotEmpty(type)){
            Criteria criteria = Criteria.where("type").is(Integer.valueOf(type));
            criteriasa.add(criteria);
        }
        //轮播类型
        String bannerType =  MapUtils.getString(map, "bannerType");
        if(StringUtils.isNotEmpty(bannerType)){
            Criteria criteria = Criteria.where("bannerType").is(Integer.valueOf(bannerType));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<SysBanner> sysBanners = mongoComDAO.executeForObjectList(criteriasa,SysBanner.class,orders);
        List<Map<String,Object>> bannerlist = new ArrayList<>();
        for(SysBanner banner : sysBanners){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",banner.getId());
            condition.put("imageUrl",banner.getImageUrl());
            condition.put("type",banner.getType());
            condition.put("bannerType",banner.getBannerType());
            condition.put("objId",banner.getObjId());
            condition.put("redirectType",banner.getRedirectType());
            condition.put("sort",banner.getSort());
            condition.put("title",banner.getTitle());
            condition.put("describe",banner.getDescribe());
            condition.put("createTime",banner.getCreateTime());
            condition.put("updateTime",banner.getUpdateTime());
            condition.put("linkUrl",banner.getLinkUrl());
            condition.put("status",banner.getStatus());
            bannerlist.add(condition);
        }
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",bannerlist);
    }

}
