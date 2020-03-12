package com.perenc.xh.lsp.service.tcSellerCouponsetup.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.dao.tcSeller.TcSellerDao;
import com.perenc.xh.lsp.entity.tcCoupon.TcCoupon;
import com.perenc.xh.lsp.entity.tcSeller.TcSeller;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerCouponsetup;
import com.perenc.xh.lsp.service.tcSellerCouponsetup.TcSellerCouponsetupService;
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


@Service("tcSellerCouponsetupService")
@Transactional(rollbackFor = Exception.class)
public class TcSellerCouponsetupServiceImpl implements TcSellerCouponsetupService {

    @Autowired
    private MongoComDAO mongoComDAO;
    @Autowired(required = false)
    private TcSellerDao tcSellerDao;

    @Override
    public ReturnJsonData insert(TcSellerCouponsetup tcSellerCouponsetup) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcSellerCouponsetup.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcSellerCouponsetup.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcSellerCouponsetup);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcSellerCouponsetup tcSellerCouponsetup) throws Exception {
        TcSellerCouponsetup returnTcSellerCouponsetup = mongoComDAO.executeForObjectById(tcSellerCouponsetup.getId(), TcSellerCouponsetup.class);
        if(returnTcSellerCouponsetup != null){
            returnTcSellerCouponsetup.setCouponId(tcSellerCouponsetup.getCouponId());
            returnTcSellerCouponsetup.setSellerId(tcSellerCouponsetup.getSellerId());
            returnTcSellerCouponsetup.setLimitNumber(tcSellerCouponsetup.getLimitNumber());
            returnTcSellerCouponsetup.setSdate(tcSellerCouponsetup.getSdate());
            returnTcSellerCouponsetup.setEdate(tcSellerCouponsetup.getEdate());
            int flag = mongoComDAO.executeUpdate(returnTcSellerCouponsetup);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcSellerCouponsetup.class);
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
        TcSellerCouponsetup tcSellerCouponsetup = mongoComDAO.executeForObjectById(id, TcSellerCouponsetup.class);
        if (tcSellerCouponsetup!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcSellerCouponsetup.getId());
            condition.put("couponId",tcSellerCouponsetup.getCouponId());
            TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcSellerCouponsetup.getCouponId(), TcCoupon.class);
            if(tcCoupon != null){
                condition.put("couponName",tcCoupon.getName());
            }else {
                condition.put("couponName","");
            }
            condition.put("sellerId",tcSellerCouponsetup.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcSellerCouponsetup.getSellerId());
            if (tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("limitNumber",tcSellerCouponsetup.getLimitNumber());
            //condition.put("sdate",tcSellerCouponsetup.getSdate());
            //condition.put("edate",tcSellerCouponsetup.getEdate());
            condition.put("status",tcSellerCouponsetup.getStatus());
            condition.put("createTime",tcSellerCouponsetup.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String name =  MapUtils.getString(map, "name");
        //String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //商家名模糊查询
        if(StringUtils.isNotEmpty(name)){
            Pattern pattern = Pattern.compile("^.*" + name + ".*$");
            Criteria criteria = Criteria.where("name").regex(pattern);
            criteriasa.add(criteria);
        }
        //跳转类型
        /*if(StringUtils.isNotEmpty(type)){
            Criteria criteria = Criteria.where("type").is(Integer.valueOf(type));
            criteriasa.add(criteria);
        }*/
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcSellerCouponsetup> tcSellerCouponsetups = mongoComDAO.executeForObjectList(criteriasa,TcSellerCouponsetup.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcSellerCouponsetup.class);
        List<Map<String,Object>> tcSellerCouponsetuplist = new ArrayList<>();
        for(TcSellerCouponsetup tcSellerCouponsetup : tcSellerCouponsetups){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcSellerCouponsetup.getId());
            condition.put("couponId",tcSellerCouponsetup.getCouponId());
            TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcSellerCouponsetup.getCouponId(), TcCoupon.class);
            if(tcCoupon != null){
                condition.put("couponName",tcCoupon.getName());
            }else {
                condition.put("couponName","");
            }
            condition.put("sellerId",tcSellerCouponsetup.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcSellerCouponsetup.getSellerId());
            if (tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("limitNumber",tcSellerCouponsetup.getLimitNumber());
            //condition.put("sdate",tcSellerCouponsetup.getSdate());
            //condition.put("edate",tcSellerCouponsetup.getEdate());
            condition.put("status",tcSellerCouponsetup.getStatus());
            condition.put("createTime",tcSellerCouponsetup.getCreateTime());
            tcSellerCouponsetuplist.add(condition);
        }
        map.clear();
        map.put("list",tcSellerCouponsetuplist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }



}
