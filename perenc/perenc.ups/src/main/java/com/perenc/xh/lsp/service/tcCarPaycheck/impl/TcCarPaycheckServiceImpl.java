package com.perenc.xh.lsp.service.tcCarPaycheck.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.entity.tcCar.TcCarPaycheck;
import com.perenc.xh.lsp.service.tcCarPaycheck.TcCarPaycheckService;
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


@Service("tcCarPaycheckService")
@Transactional(rollbackFor = Exception.class)
public class TcCarPaycheckServiceImpl implements TcCarPaycheckService {

    private static final Logger logger = Logger.getLogger(TcCarPaycheckServiceImpl.class);

    @Autowired
    private MongoComDAO mongoComDAO;

    @Override
    public ReturnJsonData insert(TcCarPaycheck tcCarPaycheck) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcCarPaycheck.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcCarPaycheck.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcCarPaycheck);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }


    @Override
    public ReturnJsonData update(TcCarPaycheck tcCarPaycheck) throws Exception {
        TcCarPaycheck returnTpaycheck = mongoComDAO.executeForObjectById(tcCarPaycheck.getId(), TcCarPaycheck.class);
        if(returnTpaycheck != null){
            returnTpaycheck.setParklotId(tcCarPaycheck.getParklotId());
            returnTpaycheck.setParkId(tcCarPaycheck.getParkId());
            returnTpaycheck.setPayNo(tcCarPaycheck.getPayNo());
            returnTpaycheck.setPayType(tcCarPaycheck.getPayType());
            returnTpaycheck.setChargeTime(tcCarPaycheck.getChargeTime());
            returnTpaycheck.setTransactionId(tcCarPaycheck.getTransactionId());
            returnTpaycheck.setPayStatus(tcCarPaycheck.getPayStatus());
            int flag = mongoComDAO.executeUpdate(returnTpaycheck);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcCarPaycheck.class);
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
        TcCarPaycheck tPaycheck = mongoComDAO.executeForObjectById(id, TcCarPaycheck.class);
        if (tPaycheck!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tPaycheck.getId());
            condition.put("parklotId",tPaycheck.getParklotId());
            condition.put("parkId",tPaycheck.getParkId());
            condition.put("payNo",tPaycheck.getPayNo());
            condition.put("payType",tPaycheck.getPayType());
            condition.put("chargeTime",tPaycheck.getChargeTime());
            condition.put("transactionId",tPaycheck.getTransactionId());
            condition.put("payStatus",tPaycheck.getPayStatus());
            condition.put("status",tPaycheck.getStatus());
            condition.put("createTime",tPaycheck.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String paySdate =  MapUtils.getString(map, "paySdate");
        String payEdate =  MapUtils.getString(map, "payEdate");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //出场开始时间-出场结束时间
        if(StringUtils.isNotEmpty(paySdate) && StringUtils.isNotEmpty(payEdate)){
            Criteria criteria = Criteria.where("chargeTime").gte(paySdate).lte(payEdate);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcCarPaycheck> tcCarPaychecks = mongoComDAO.executeForObjectList(criteriasa,TcCarPaycheck.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcCarPaycheck.class);
        List<Map<String,Object>> TcCarInlist = new ArrayList<>();
        for(TcCarPaycheck tPaycheck : tcCarPaychecks){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tPaycheck.getId());
            condition.put("parklotId",tPaycheck.getParklotId());
            condition.put("parkId",tPaycheck.getParkId());
            condition.put("payNo",tPaycheck.getPayNo());
            condition.put("payType",tPaycheck.getPayType());
            condition.put("chargeTime",tPaycheck.getChargeTime());
            condition.put("transactionId",tPaycheck.getTransactionId());
            condition.put("payStatus",tPaycheck.getPayStatus());
            condition.put("status",tPaycheck.getStatus());
            condition.put("createTime",tPaycheck.getCreateTime());
            TcCarInlist.add(condition);
        }
        map.clear();
        map.put("list",TcCarInlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }


    /**
     * 根据当前时间
     * 删除数据
     * 根据当前时间，日志30天前的日志删除
     */
    @Override
    public Integer removeBatchTcCarPaycheckByEdate() throws Exception {
        Integer num=0;
        SimpleDateFormat sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar =null;
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);//当前时间前去天数,
        String  endTime=sdfhms.format(calendar.getTime()); //当前时间
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //结束时间小于等于当前时间
        Criteria criteria = Criteria.where("createTime").lte(endTime);
        criteriasa.add(criteria);
        num =+ mongoComDAO.executeDelete(criteriasa,TcCarPaycheck.class);
        return num;
    }




}
