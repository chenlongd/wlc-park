package com.perenc.xh.lsp.service.tcCarRecharge.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.entity.tcCar.TcCarRecharge;
import com.perenc.xh.lsp.service.tcCarRecharge.TcCarRechargeService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("tcCarRechargeService")
@Transactional(rollbackFor = Exception.class)
public class TcCarRechargeServiceImpl implements TcCarRechargeService {

    private static final Logger logger = Logger.getLogger(TcCarRechargeServiceImpl.class);

    @Autowired
    private MongoComDAO mongoComDAO;

    @Override
    public ReturnJsonData insert(TcCarRecharge tcCarRecharge) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        //是否启用状态
        tcCarRecharge.setIsEnabled(1);
        tcCarRecharge.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcCarRecharge.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcCarRecharge);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }


    @Override
    public ReturnJsonData update(TcCarRecharge tcCarRecharge) throws Exception {
        TcCarRecharge returnTrecharge = mongoComDAO.executeForObjectById(tcCarRecharge.getId(), TcCarRecharge.class);
        if(returnTrecharge != null){
            returnTrecharge.setOldPrice(tcCarRecharge.getOldPrice());
            returnTrecharge.setGetPrice(tcCarRecharge.getOldPrice());
            returnTrecharge.setRemark(tcCarRecharge.getRemark());
            int flag = mongoComDAO.executeUpdate(returnTrecharge);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 修改是否启用
     * @param tcCarRecharge
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateIsEnabled(TcCarRecharge tcCarRecharge) throws Exception {
        TcCarRecharge returnTrecharge = mongoComDAO.executeForObjectById(tcCarRecharge.getId(), TcCarRecharge.class);
        if(returnTrecharge != null){
            returnTrecharge.setIsEnabled(tcCarRecharge.getIsEnabled());
            int flag = mongoComDAO.executeUpdate(returnTrecharge);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcCarRecharge.class);
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
        TcCarRecharge tRecharge = mongoComDAO.executeForObjectById(id, TcCarRecharge.class);
        if (tRecharge!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tRecharge.getId());
            condition.put("oldPrice", ToolUtil.getIntToDouble(tRecharge.getOldPrice()));
            condition.put("getPrice",ToolUtil.getIntToDouble(tRecharge.getGetPrice()));
            condition.put("remark",tRecharge.getRemark());
            condition.put("isEnabled",tRecharge.getIsEnabled());
            condition.put("status",tRecharge.getStatus());
            condition.put("createTime",tRecharge.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String isEnabled =  MapUtils.getString(map, "isEnabled");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //启用状态
        if(StringUtils.isNotEmpty(isEnabled)){
            Criteria criteria = Criteria.where("isEnabled").is(Integer.valueOf(isEnabled));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcCarRecharge> tcCarRecharges = mongoComDAO.executeForObjectList(criteriasa,TcCarRecharge.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcCarRecharge.class);
        List<Map<String,Object>> tcCarRechargelist = new ArrayList<>();
        for(TcCarRecharge tRecharge : tcCarRecharges){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tRecharge.getId());
            condition.put("oldPrice",ToolUtil.getIntToDouble(tRecharge.getOldPrice()));
            condition.put("getPrice",ToolUtil.getIntToDouble(tRecharge.getGetPrice()));
            condition.put("remark",tRecharge.getRemark());
            condition.put("isEnabled",tRecharge.getIsEnabled());
            condition.put("status",tRecharge.getStatus());
            condition.put("createTime",tRecharge.getCreateTime());
            tcCarRechargelist.add(condition);
        }
        map.clear();
        map.put("list",tcCarRechargelist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

    /**
     * 查询所有充值项
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getAllList(Map<String, Object> map) throws Exception {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.ASC,"oldPrice"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //是否启用
        Criteria criteria = Criteria.where("isEnabled").is(1);
        criteriasa.add(criteria);
        Criteria criterib = Criteria.where("status").is(1);
        criteriasa.add(criterib);
        List<TcCarRecharge> tcCarRecharges = mongoComDAO.executeForObjectList(criteriasa,TcCarRecharge.class,orders);
        List<Map<String,Object>> tcCarRechargelist = new ArrayList<>();
        for(TcCarRecharge tRecharge : tcCarRecharges){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tRecharge.getId());
            condition.put("oldPrice",ToolUtil.getIntToDouble(tRecharge.getOldPrice()));
            condition.put("getPrice",ToolUtil.getIntToDouble(tRecharge.getGetPrice()));
            condition.put("remark",tRecharge.getRemark());
            condition.put("isEnabled",tRecharge.getIsEnabled());
            condition.put("status",tRecharge.getStatus());
            condition.put("createTime",tRecharge.getCreateTime());
            tcCarRechargelist.add(condition);
        }
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",tcCarRechargelist);
    }



}
