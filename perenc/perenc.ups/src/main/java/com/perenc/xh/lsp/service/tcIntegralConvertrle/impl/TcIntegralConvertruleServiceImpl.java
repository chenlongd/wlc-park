package com.perenc.xh.lsp.service.tcIntegralConvertrle.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.entity.tcCoupon.TcCoupon;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegralConvertrule;
import com.perenc.xh.lsp.service.tcIntegralConvertrle.TcIntegralConvertruleService;
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


@Service("tcIntegralConvertruleService")
@Transactional(rollbackFor = Exception.class)
public class TcIntegralConvertruleServiceImpl implements TcIntegralConvertruleService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Override
    public ReturnJsonData insert(TcIntegralConvertrule tcIntegralConvertrule) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        //查询票券兑换规则是否已经添加
        Map<String, Object> conditioncvt = new HashMap<>();
        conditioncvt.put("id",tcIntegralConvertrule.getCouponId());
        conditioncvt.put("status",1);
        //查询车辆是否已经添加过
        TcIntegralConvertrule returnConvertrule = mongoComDAO.executeForObjectByCon(conditioncvt, TcIntegralConvertrule.class);
        if(returnConvertrule!=null) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"该票券兑换规则已经存在",null);
        }
        tcIntegralConvertrule.setIsEnabled(1);
        tcIntegralConvertrule.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcIntegralConvertrule.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcIntegralConvertrule);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcIntegralConvertrule tcIntegralConvertrule) throws Exception {
        TcIntegralConvertrule returnTcConvertrule = mongoComDAO.executeForObjectById(tcIntegralConvertrule.getId(), TcIntegralConvertrule.class);
        if(returnTcConvertrule != null){
            returnTcConvertrule.setName(tcIntegralConvertrule.getName());
            returnTcConvertrule.setRatio(tcIntegralConvertrule.getRatio());
            returnTcConvertrule.setFormula(tcIntegralConvertrule.getFormula());
            returnTcConvertrule.setNumber(tcIntegralConvertrule.getNumber());
            returnTcConvertrule.setLimitNumber(tcIntegralConvertrule.getLimitNumber());
            returnTcConvertrule.setSdate(tcIntegralConvertrule.getSdate());
            returnTcConvertrule.setEdate(tcIntegralConvertrule.getEdate());
            returnTcConvertrule.setRemark(tcIntegralConvertrule.getRemark());
            int flag = mongoComDAO.executeUpdate(returnTcConvertrule);
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
     * @param tiConvertrule
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateIsEnabled(TcIntegralConvertrule tiConvertrule) throws Exception {
        TcIntegralConvertrule returntiConvertrule = mongoComDAO.executeForObjectById(tiConvertrule.getId(), TcIntegralConvertrule.class);
        if(returntiConvertrule != null){
            returntiConvertrule.setIsEnabled(tiConvertrule.getIsEnabled());
            int flag = mongoComDAO.executeUpdate(returntiConvertrule);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcIntegralConvertrule.class);
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
        TcIntegralConvertrule tciConvertrule = mongoComDAO.executeForObjectById(id, TcIntegralConvertrule.class);
        if (tciConvertrule!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tciConvertrule.getId());
            condition.put("couponId",tciConvertrule.getCouponId());
            TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tciConvertrule.getCouponId(), TcCoupon.class);
            if(tcCoupon != null){
                condition.put("couponName",tcCoupon.getName());
                condition.put("couponDesc",tcCoupon.getDesc());
                condition.put("couponDuration",tcCoupon.getDuration());
                condition.put("couponAmount",tcCoupon.getAmount());
            }else {
                condition.put("couponName","");
                condition.put("couponDesc","");
                condition.put("couponDuration","");
                condition.put("couponAmount","");
            }
            condition.put("cdays",tciConvertrule.getCdays());
            condition.put("descp",tciConvertrule.getDescp());
            condition.put("name",tciConvertrule.getName());
            condition.put("ratio",tciConvertrule.getRatio());
            condition.put("formula",tciConvertrule.getFormula());
            condition.put("number",tciConvertrule.getNumber());
            condition.put("limitNumber",tciConvertrule.getLimitNumber());
            condition.put("sdate",tciConvertrule.getSdate());
            condition.put("edate",tciConvertrule.getEdate());
            condition.put("remark",tciConvertrule.getRemark());
            condition.put("status",tciConvertrule.getStatus());
            condition.put("createTime",tciConvertrule.getCreateTime());
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
        //规则名称
        if(StringUtils.isNotEmpty(name)){
            Pattern pattern = Pattern.compile("^.*" + name + ".*$");
            Criteria criteria = Criteria.where("name").regex(pattern);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcIntegralConvertrule> tcIntegralConvertrules = mongoComDAO.executeForObjectList(criteriasa,TcIntegralConvertrule.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcIntegralConvertrule.class);
        List<Map<String,Object>> tciConvertrulelist = new ArrayList<>();
        for(TcIntegralConvertrule tciConvertrule : tcIntegralConvertrules){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tciConvertrule.getId());
            condition.put("couponId",tciConvertrule.getCouponId());
            TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tciConvertrule.getCouponId(), TcCoupon.class);
            if(tcCoupon != null){
                condition.put("couponName",tcCoupon.getName());
                condition.put("couponDesc",tcCoupon.getDesc());
                condition.put("couponDuration",tcCoupon.getDuration());
                condition.put("couponAmount",tcCoupon.getAmount());
            }else {
                condition.put("couponName","");
                condition.put("couponDesc","");
                condition.put("couponDuration","");
                condition.put("couponAmount","");
            }
            condition.put("cdays",tciConvertrule.getCdays());
            condition.put("descp",tciConvertrule.getDescp());
            condition.put("name",tciConvertrule.getName());
            condition.put("ratio",tciConvertrule.getRatio());
            condition.put("formula",tciConvertrule.getFormula());
            condition.put("number",tciConvertrule.getNumber());
            condition.put("limitNumber",tciConvertrule.getLimitNumber());
            condition.put("sdate",tciConvertrule.getSdate());
            condition.put("edate",tciConvertrule.getEdate());
            condition.put("remark",tciConvertrule.getRemark());
            condition.put("status",tciConvertrule.getStatus());
            condition.put("createTime",tciConvertrule.getCreateTime());
            tciConvertrulelist.add(condition);
        }
        map.clear();
        map.put("list",tciConvertrulelist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }



}
