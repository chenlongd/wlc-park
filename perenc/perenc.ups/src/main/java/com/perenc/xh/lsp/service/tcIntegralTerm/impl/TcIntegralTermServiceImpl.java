package com.perenc.xh.lsp.service.tcIntegralTerm.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegralTerm;
import com.perenc.xh.lsp.service.tcIntegralTerm.TcIntegralTermService;
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


@Service("tcIntegralTermService")
@Transactional(rollbackFor = Exception.class)
public class TcIntegralTermServiceImpl implements TcIntegralTermService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;


    @Override
    public ReturnJsonData insert(TcIntegralTerm tcIntegralTerm) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcIntegralTerm.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcIntegralTerm.setIsEnabled(1); //启用状态：1：是
        tcIntegralTerm.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcIntegralTerm);
        if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加成功", null);
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
    }

    @Override
    public ReturnJsonData update(TcIntegralTerm tcIntegralTerm) throws Exception {
        TcIntegralTerm returnigralTerm = mongoComDAO.executeForObjectById(tcIntegralTerm.getId(), TcIntegralTerm.class);
        if(returnigralTerm != null){
            returnigralTerm.setName(tcIntegralTerm.getName());
            returnigralTerm.setNumber(tcIntegralTerm.getNumber());
            returnigralTerm.setType(tcIntegralTerm.getType());
            returnigralTerm.setUrl(tcIntegralTerm.getUrl());
            returnigralTerm.setRemark(tcIntegralTerm.getRemark());
            int flag = mongoComDAO.executeUpdate(returnigralTerm);
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
     * @param tcIntegralTerm
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateIsEnabled(TcIntegralTerm tcIntegralTerm) throws Exception {
        TcIntegralTerm returnigralTerm = mongoComDAO.executeForObjectById(tcIntegralTerm.getId(), TcIntegralTerm.class);
        if(returnigralTerm != null){
            returnigralTerm.setIsEnabled(tcIntegralTerm.getIsEnabled());
            int flag = mongoComDAO.executeUpdate(returnigralTerm);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcIntegralTerm.class);
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
        TcIntegralTerm tcIntegralTerm = mongoComDAO.executeForObjectById(id, TcIntegralTerm.class);
        if (tcIntegralTerm!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcIntegralTerm.getId());
            condition.put("name",tcIntegralTerm.getName());
            condition.put("type",tcIntegralTerm.getType());
            condition.put("number",tcIntegralTerm.getNumber());
            condition.put("url",tcIntegralTerm.getUrl());
            condition.put("remark",tcIntegralTerm.getRemark());
            condition.put("status",tcIntegralTerm.getStatus());
            condition.put("createTime",tcIntegralTerm.getCreateTime());
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
        List<TcIntegralTerm> tcIntegralTerms = mongoComDAO.executeForObjectList(criteriasa,TcIntegralTerm.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcIntegralTerm.class);
        List<Map<String,Object>> tcIntegralTermlist = new ArrayList<>();
        for(TcIntegralTerm tcIntegralTerm : tcIntegralTerms){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcIntegralTerm.getId());
            condition.put("name",tcIntegralTerm.getName());
            condition.put("type",tcIntegralTerm.getType());
            condition.put("number",tcIntegralTerm.getNumber());
            condition.put("url",tcIntegralTerm.getUrl());
            condition.put("remark",tcIntegralTerm.getRemark());
            condition.put("status",tcIntegralTerm.getStatus());
            condition.put("createTime",tcIntegralTerm.getCreateTime());
            tcIntegralTermlist.add(condition);
        }
        map.clear();
        map.put("list",tcIntegralTermlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }



}
