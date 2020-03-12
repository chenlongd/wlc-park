package com.perenc.xh.lsp.service.tcParklot.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.entity.tcParklot.TcCostRule;
import com.perenc.xh.lsp.entity.tcParklot.TcParklot;
import com.perenc.xh.lsp.service.tcParklot.TcParklotService;
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


@Service("tcParklotService")
@Transactional(rollbackFor = Exception.class)
public class TcParklotServiceImpl implements TcParklotService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Override
    public ReturnJsonData insert(TcParklot tcParklot) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcParklot.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcParklot.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcParklot);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcParklot tcParklot) throws Exception {
        TcParklot returnTcParklot = mongoComDAO.executeForObjectById(tcParklot.getId(), TcParklot.class);
        if(returnTcParklot != null){
            returnTcParklot.setName(tcParklot.getName());
            returnTcParklot.setNumber(tcParklot.getNumber());
            returnTcParklot.setUnitPrice(tcParklot.getUnitPrice());
            returnTcParklot.setMaxHour(tcParklot.getMaxHour());
            returnTcParklot.setMinHour(tcParklot.getMinHour());
            returnTcParklot.setDescp(tcParklot.getDescp());
            int flag = mongoComDAO.executeUpdate(returnTcParklot);
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
        //判断是否存在设置计费规则
        for (int i = 0; i <ids.length ; i++) {
            Map<String,Object> conditioncr = new HashMap<>();
            conditioncr.put("parklotId",ids[i]);
            conditioncr.put("status",1);
            TcCostRule tcCostRule = mongoComDAO.executeForObjectByCon(conditioncr, TcCostRule.class);
            if(tcCostRule != null){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"计费规则已设置无法删除",null);
            }
        }
         Map<String,Object> condition=new HashMap<>();
         condition.put("ids",ids);
         condition.put("status",2);
        int flag= mongoComDAO.executeUpdateByIds(condition, TcParklot.class);
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
        TcParklot tcParklot = mongoComDAO.executeForObjectById(id, TcParklot.class);
        if (tcParklot!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcParklot.getId());
            condition.put("name",tcParklot.getName());
            condition.put("number",tcParklot.getNumber());
            condition.put("unitPrice", ToolUtil.getIntToDouble(tcParklot.getUnitPrice()));
            condition.put("maxHour", ToolUtil.getIntToDouble(tcParklot.getMaxHour()));
            condition.put("minHour", ToolUtil.getIntToDouble(tcParklot.getMinHour()));
            condition.put("descp",tcParklot.getDescp());
            condition.put("status",tcParklot.getStatus());
            condition.put("createTime",tcParklot.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String name =  MapUtils.getString(map, "name");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //场地名模糊查询
        if(StringUtils.isNotEmpty(name)){
            Pattern pattern = Pattern.compile("^.*" + name + ".*$");
            Criteria criteria = Criteria.where("name").regex(pattern);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcParklot> tcParklots = mongoComDAO.executeForObjectList(criteriasa,TcParklot.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcParklot.class);
        List<Map<String,Object>> tcParklotlist = new ArrayList<>();
        for(TcParklot tcParklot : tcParklots){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcParklot.getId());
            condition.put("name",tcParklot.getName());
            condition.put("number",tcParklot.getNumber());
            condition.put("unitPrice", ToolUtil.getIntToDouble(tcParklot.getUnitPrice()));
            condition.put("maxHour", ToolUtil.getIntToDouble(tcParklot.getMaxHour()));
            condition.put("minHour", ToolUtil.getIntToDouble(tcParklot.getMinHour()));
            condition.put("descp",tcParklot.getDescp());
            condition.put("status",tcParklot.getStatus());
            condition.put("createTime",tcParklot.getCreateTime());
            tcParklotlist.add(condition);
        }
        map.clear();
        map.put("list",tcParklotlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }




}
