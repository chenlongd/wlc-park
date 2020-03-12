package com.perenc.xh.lsp.service.tcIntegralActivty.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegralActivty;
import com.perenc.xh.lsp.service.tcIntegralActivty.TcIntegralActivtyService;
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


@Service("tcIntegralActivtyService")
@Transactional(rollbackFor = Exception.class)
public class TcIntegralActivtyServiceImpl implements TcIntegralActivtyService {

    @Autowired
    private MongoComDAO mongoComDAO;


    @Override
    public ReturnJsonData insert(TcIntegralActivty tcIntegralActivty) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcIntegralActivty.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcIntegralActivty.setIsEnabled(1); //启用状态：1：是
        tcIntegralActivty.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcIntegralActivty);
        if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加成功", null);
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
    }

    @Override
    public ReturnJsonData update(TcIntegralActivty tcIntegralActivty) throws Exception {
        TcIntegralActivty returnigralActivty = mongoComDAO.executeForObjectById(tcIntegralActivty.getId(), TcIntegralActivty.class);
        if(returnigralActivty != null){
            returnigralActivty.setName(tcIntegralActivty.getName());
            returnigralActivty.setNumber(tcIntegralActivty.getNumber());
            returnigralActivty.setType(tcIntegralActivty.getType());
            returnigralActivty.setRemark(tcIntegralActivty.getRemark());
            int flag = mongoComDAO.executeUpdate(returnigralActivty);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 修改启用状态
     * @param tcIntegralActivty
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateIsEnabled(TcIntegralActivty tcIntegralActivty) throws Exception {
        TcIntegralActivty returnigralActivty = mongoComDAO.executeForObjectById(tcIntegralActivty.getId(), TcIntegralActivty.class);
        if(returnigralActivty != null){
            returnigralActivty.setIsEnabled(tcIntegralActivty.getIsEnabled());
            int flag = mongoComDAO.executeUpdate(returnigralActivty);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcIntegralActivty.class);
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
        TcIntegralActivty tiActivty = mongoComDAO.executeForObjectById(id, TcIntegralActivty.class);
        if (tiActivty!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tiActivty.getId());
            condition.put("type",tiActivty.getType());
            condition.put("name",tiActivty.getName());
            condition.put("descp",tiActivty.getDescp());
            condition.put("ratio",tiActivty.getRatio());
            condition.put("number",tiActivty.getNumber());
            condition.put("sdate",tiActivty.getSdate());
            condition.put("edate",tiActivty.getEdate());
            condition.put("couponId",tiActivty.getCouponId());
            condition.put("cdays",tiActivty.getCdays());
            condition.put("remark",tiActivty.getRemark());
            condition.put("isEnabled",tiActivty.getIsEnabled());
            condition.put("status",tiActivty.getStatus());
            condition.put("createTime",tiActivty.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
         String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //所属类型
        if(StringUtils.isNotEmpty(type)){
            Criteria criteria = Criteria.where("type").is(Integer.valueOf(type));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcIntegralActivty> tcIntegralActivties = mongoComDAO.executeForObjectList(criteriasa,TcIntegralActivty.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcIntegralActivty.class);
        List<Map<String,Object>> tcIntegralActivtielist = new ArrayList<>();
        for(TcIntegralActivty tiActivty : tcIntegralActivties){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tiActivty.getId());
            condition.put("type",tiActivty.getType());
            condition.put("name",tiActivty.getName());
            condition.put("descp",tiActivty.getDescp());
            condition.put("ratio",tiActivty.getRatio());
            condition.put("number",tiActivty.getNumber());
            condition.put("sdate",tiActivty.getSdate());
            condition.put("edate",tiActivty.getEdate());
            condition.put("couponId",tiActivty.getCouponId());
            condition.put("cdays",tiActivty.getCdays());
            condition.put("remark",tiActivty.getRemark());
            condition.put("isEnabled",tiActivty.getIsEnabled());
            condition.put("status",tiActivty.getStatus());
            condition.put("createTime",tiActivty.getCreateTime());
            tcIntegralActivtielist.add(condition);
        }
        map.clear();
        map.put("list",tcIntegralActivtielist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }



}
