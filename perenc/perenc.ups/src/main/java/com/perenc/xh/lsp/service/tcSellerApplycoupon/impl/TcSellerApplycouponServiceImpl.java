package com.perenc.xh.lsp.service.tcSellerApplycoupon.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.dao.tcSeller.TcSellerDao;
import com.perenc.xh.lsp.entity.tcCoupon.TcCoupon;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCoupon;
import com.perenc.xh.lsp.entity.tcSeller.TcSeller;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplyc;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplycData;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplycoupon;
import com.perenc.xh.lsp.service.tcSellerApplycoupon.TcSellerApplycouponService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Service("tcSellerApplycouponService")
@Transactional(rollbackFor = Exception.class)
public class TcSellerApplycouponServiceImpl implements TcSellerApplycouponService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;

    @Autowired(required = false)
    private TcSellerDao tcSellerDao;

    @Override
    public ReturnJsonData insert(TcSellerApplycoupon tcSellerApplycoupon) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        //有效期
        tcSellerApplycoupon.setSdate("");
        tcSellerApplycoupon.setEdate("");
        tcSellerApplycoupon.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcSellerApplycoupon.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcSellerApplycoupon);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    /**
     * 新增
     * 申领票券
     * 申领票明细
     * @param data
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertSellerApply(TcSellerApplycData data) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        //申领主表
        TcSellerApplyc tcSellerApplyc =new TcSellerApplyc();
        tcSellerApplyc.setSellerId(data.getSellerId());
        tcSellerApplyc.setIsApproval(1);
        tcSellerApplyc.setUseStatus(1); //待使用
        //有效期
        tcSellerApplyc.setSdate("");
        tcSellerApplyc.setEdate("");
        tcSellerApplyc.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcSellerApplyc.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcSellerApplyc);
        if(flag > 0){
            List<TcSellerApplycoupon> sellerApplycouponList = data.getSellerApplycouponList();

            if(sellerApplycouponList != null && sellerApplycouponList.size() >0) {
                for (TcSellerApplycoupon sellerApplycoupon : sellerApplycouponList) {
                    if (StringUtils.isEmpty(sellerApplycoupon.getId())) {
                        sellerApplycoupon.setId(null);
                    }
                    sellerApplycoupon.setSellerId(data.getSellerId());
                    sellerApplycoupon.setSellerApplyId(tcSellerApplyc.getId());
                    sellerApplycoupon.setKnumber(sellerApplycoupon.getNumber());
                    sellerApplycoupon.setYnumber(0);
                    sellerApplycoupon.setSdate("");
                    sellerApplycoupon.setEdate("");
                    sellerApplycoupon.setIsApproval(1);
                    sellerApplycoupon.setUseStatus(1); //待使用
                    sellerApplycoupon.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                    sellerApplycoupon.setStatus(1);
                }
                int i = mongoComDAO.executeInserts(sellerApplycouponList, TcSellerApplycoupon.class);
                if (i > 0) {
                    return new ReturnJsonData(DataCodeUtil.SUCCESS, "添加成功", null);
                }
            }
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
    }


    @Override
    public ReturnJsonData update(TcSellerApplycoupon tcSellerApplycoupon) throws Exception {
        TcSellerApplycoupon returnTcSellerApplycoupon = mongoComDAO.executeForObjectById(tcSellerApplycoupon.getId(), TcSellerApplycoupon.class);
        if(returnTcSellerApplycoupon != null){
            returnTcSellerApplycoupon.setCouponId(tcSellerApplycoupon.getCouponId());
            returnTcSellerApplycoupon.setSellerId(tcSellerApplycoupon.getSellerId());
            returnTcSellerApplycoupon.setNumber(tcSellerApplycoupon.getNumber());
            returnTcSellerApplycoupon.setSdate(tcSellerApplycoupon.getSdate());
            returnTcSellerApplycoupon.setEdate(tcSellerApplycoupon.getEdate());
            int flag = mongoComDAO.executeUpdate(returnTcSellerApplycoupon);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 商家申领停车券审批
     * @param tcSellerApplycoupon
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateIsApproval(TcSellerApplycoupon tcSellerApplycoupon) throws Exception {
        TcSellerApplycoupon returnTcSellerApplycoupon = mongoComDAO.executeForObjectById(tcSellerApplycoupon.getId(), TcSellerApplycoupon.class);
        if(returnTcSellerApplycoupon != null){
            returnTcSellerApplycoupon.setIsApproval(tcSellerApplycoupon.getIsApproval());
            int flag = mongoComDAO.executeUpdate(returnTcSellerApplycoupon);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcSellerApplycoupon.class);
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
        TcSellerApplycoupon tcSellerApplycoupon = mongoComDAO.executeForObjectById(id, TcSellerApplycoupon.class);
        if (tcSellerApplycoupon!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcSellerApplycoupon.getId());
            condition.put("couponId",tcSellerApplycoupon.getCouponId());
            TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcSellerApplycoupon.getCouponId(), TcCoupon.class);
            if(tcCoupon != null){
                condition.put("couponName",tcCoupon.getName());
            }else {
                condition.put("couponName","");
            }
            condition.put("sellerId",tcSellerApplycoupon.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcSellerApplycoupon.getSellerId());
            if (tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("sellerUserId",tcSellerApplycoupon.getSellerUserId());
            condition.put("sellerApplyId",tcSellerApplycoupon.getSellerApplyId());
            condition.put("type",tcSellerApplycoupon.getType());
            condition.put("name",tcSellerApplycoupon.getName());
            condition.put("ticketImg",tcSellerApplycoupon.getTicketImg());
            condition.put("number",tcSellerApplycoupon.getNumber());
            condition.put("knumber",tcSellerApplycoupon.getKnumber());
            condition.put("ynumber",tcSellerApplycoupon.getYnumber());
            condition.put("sdate",tcSellerApplycoupon.getSdate());
            condition.put("edate",tcSellerApplycoupon.getEdate());
            condition.put("isApproval",tcSellerApplycoupon.getIsApproval());
            condition.put("useStatus",tcSellerApplycoupon.getUseStatus());
            condition.put("status",tcSellerApplycoupon.getStatus());
            condition.put("createTime",tcSellerApplycoupon.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String sellerId =  MapUtils.getString(map, "sellerId");
        String type =  MapUtils.getString(map, "type");
        String name =  MapUtils.getString(map, "name");
        String isApproval =  MapUtils.getString(map, "isApproval");
        String useStatus =  MapUtils.getString(map, "useStatus");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        // 商家ID
        if(StringUtils.isNotEmpty(sellerId)){
            Criteria criteria = Criteria.where("sellerId").is(Integer.valueOf(sellerId));
            criteriasa.add(criteria);
        }
        // 名称模糊查询
        if(StringUtils.isNotEmpty(name)){
            Pattern pattern = Pattern.compile("^.*" + name + ".*$");
            Criteria criteria = Criteria.where("name").regex(pattern);
            criteriasa.add(criteria);
        }
        // 申请类型 1=商家发放；2=婚宴、会议发放
        if(StringUtils.isNotEmpty(type)){
            Criteria criteria = Criteria.where("type").is(Integer.valueOf(type));
            criteriasa.add(criteria);
        }
        // 使用状态
        if(StringUtils.isNotEmpty(isApproval)){
            Criteria criteria = Criteria.where("isApproval").is(Integer.valueOf(isApproval));
            criteriasa.add(criteria);
        }
        // 使用状态
        if(StringUtils.isNotEmpty(useStatus)){
            Criteria criteria = Criteria.where("useStatus").is(Integer.valueOf(useStatus));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        Criteria criterib = Criteria.where("knumber").gt(0);
        criteriasa.add(criterib);
        List<TcSellerApplycoupon> tcSellerApplycoupons = mongoComDAO.executeForObjectList(criteriasa,TcSellerApplycoupon.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcSellerApplycoupon.class);
        List<Map<String,Object>> tcSellerApplycouponlist = new ArrayList<>();
        for(TcSellerApplycoupon tcSellerApplycoupon : tcSellerApplycoupons){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcSellerApplycoupon.getId());
            condition.put("couponId",tcSellerApplycoupon.getCouponId());
            TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcSellerApplycoupon.getCouponId(), TcCoupon.class);
            if(tcCoupon != null){
                condition.put("couponName",tcCoupon.getName());
            }else {
                condition.put("couponName","");
            }
            condition.put("sellerId",tcSellerApplycoupon.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcSellerApplycoupon.getSellerId());
            if (tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("sellerUserId",tcSellerApplycoupon.getSellerUserId());
            condition.put("sellerApplyId",tcSellerApplycoupon.getSellerApplyId());
            condition.put("type",tcSellerApplycoupon.getType());
            condition.put("name",tcSellerApplycoupon.getName());
            condition.put("ticketImg",tcSellerApplycoupon.getTicketImg());
            condition.put("number",tcSellerApplycoupon.getNumber());
            condition.put("knumber",tcSellerApplycoupon.getKnumber());
            condition.put("ynumber",tcSellerApplycoupon.getYnumber());
            condition.put("sdate",tcSellerApplycoupon.getSdate());
            condition.put("edate",tcSellerApplycoupon.getEdate());
            condition.put("isApproval",tcSellerApplycoupon.getIsApproval());
            condition.put("useStatus",tcSellerApplycoupon.getUseStatus());
            condition.put("status",tcSellerApplycoupon.getStatus());
            condition.put("createTime",tcSellerApplycoupon.getCreateTime());
            tcSellerApplycouponlist.add(condition);
        }
        map.clear();
        map.put("list",tcSellerApplycouponlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

    /**
     * 查询所有
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getAllList(Map<String, Object> map) throws Exception {
        String sellerId =  MapUtils.getString(map, "sellerId");
        String type =  MapUtils.getString(map, "type");
        String name =  MapUtils.getString(map, "name");
        String isApproval =  MapUtils.getString(map, "isApproval");
        String useStatus =  MapUtils.getString(map, "useStatus");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.ASC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        // 商家ID
        if(StringUtils.isNotEmpty(sellerId)){
            Criteria criteria = Criteria.where("sellerId").is(Integer.valueOf(sellerId));
            criteriasa.add(criteria);
        }
        // 名称模糊查询
        if(StringUtils.isNotEmpty(name)){
            Pattern pattern = Pattern.compile("^.*" + name + ".*$");
            Criteria criteria = Criteria.where("name").regex(pattern);
            criteriasa.add(criteria);
        }
        // 申请类型 1=商家发放；2=婚宴、会议发放
        if(StringUtils.isNotEmpty(type)){
            Criteria criteria = Criteria.where("type").is(Integer.valueOf(type));
            criteriasa.add(criteria);
        }
        // 审核状态
        if(StringUtils.isNotEmpty(isApproval)){
            Criteria criteria = Criteria.where("isApproval").is(Integer.valueOf(isApproval));
            criteriasa.add(criteria);
        }
        // 使用状态
        if(StringUtils.isNotEmpty(useStatus)){
            Criteria criteria = Criteria.where("useStatus").is(Integer.valueOf(useStatus));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        Criteria criterib = Criteria.where("knumber").gt(0);
        criteriasa.add(criterib);
        List<TcSellerApplycoupon> tcSellerApplycoupons = mongoComDAO.executeForObjectList(criteriasa,TcSellerApplycoupon.class,orders);
        List<Map<String,Object>> tcSellerApplycouponlist = new ArrayList<>();
        for(TcSellerApplycoupon tcSellerApplycoupon : tcSellerApplycoupons){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcSellerApplycoupon.getId());
            condition.put("couponId",tcSellerApplycoupon.getCouponId());
            TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcSellerApplycoupon.getCouponId(), TcCoupon.class);
            if(tcCoupon != null){
                condition.put("couponName",tcCoupon.getName());
            }else {
                condition.put("couponName","");
            }
            condition.put("sellerId",tcSellerApplycoupon.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcSellerApplycoupon.getSellerId());
            if (tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("sellerUserId",tcSellerApplycoupon.getSellerUserId());
            condition.put("sellerApplyId",tcSellerApplycoupon.getSellerApplyId());
            condition.put("type",tcSellerApplycoupon.getType());
            condition.put("name",tcSellerApplycoupon.getName());
            condition.put("ticketImg",tcSellerApplycoupon.getTicketImg());
            condition.put("number",tcSellerApplycoupon.getNumber());
            condition.put("knumber",tcSellerApplycoupon.getKnumber());
            condition.put("ynumber",tcSellerApplycoupon.getYnumber());
            condition.put("sdate",tcSellerApplycoupon.getSdate());
            condition.put("edate",tcSellerApplycoupon.getEdate());
            condition.put("isApproval",tcSellerApplycoupon.getIsApproval());
            condition.put("useStatus",tcSellerApplycoupon.getUseStatus());
            condition.put("status",tcSellerApplycoupon.getStatus());
            condition.put("createTime",tcSellerApplycoupon.getCreateTime());
            tcSellerApplycouponlist.add(condition);
        }
        map.clear();
        map.put("list",tcSellerApplycouponlist);//返回前端集合命名为list
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }




    /**
     * 查询停车券明细
     * 停车券总数 :总申请张数，总发放数，总剩余数
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getAllTotalNumber(Map<String, Object> map) throws Exception {
        Map<String,Object> resultmap = new HashMap<>();
        String sellerId =  MapUtils.getString(map, "sellerId");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //商家ID
        if(StringUtils.isNotEmpty(sellerId)){
            Criteria criteria = Criteria.where("sellerId").is(Integer.valueOf(sellerId));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcSellerApplyc> tcSellerApplycs = mongoComDAO.executeForObjectList(criteriasa,TcSellerApplyc.class,orders);
        //总申请数
        Integer totalApplyNumber=0;
        //总发放数
        Integer totalSendNumber=0;
        //总剩余数
        Integer totalsxNumber=0;
        List<TcSellerApplycoupon> tcSellerApplycouponalls = mongoComDAO.executeForObjectList(criteriasa,TcSellerApplycoupon.class,orders);
        for(TcSellerApplycoupon tcSellerApplycoupon : tcSellerApplycouponalls){
            totalApplyNumber=totalApplyNumber+tcSellerApplycoupon.getNumber();
            totalSendNumber=totalSendNumber+tcSellerApplycoupon.getYnumber();
        }
        resultmap.put("totalApplyNumber",totalApplyNumber);
        resultmap.put("totalSendNumber",totalSendNumber);
        resultmap.put("totalsxNumber",totalApplyNumber-totalSendNumber);
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",resultmap);
    }


    /**
     * 查询停车券明细
     * 每种停车券申领数量统计
     * 停车券申领记录
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getApplyCoupon(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        Map<String,Object> resultmap = new HashMap<>();
        String sellerId =  MapUtils.getString(map, "sellerId");
        //类型：1:总计，2：申领，3：发放
        //String subType =  MapUtils.getString(map, "subType");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //商家ID
        if(StringUtils.isNotEmpty(sellerId)){
            Criteria criteria = Criteria.where("sellerId").is(Integer.valueOf(sellerId));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("isApproval").is(2);
        criteriasa.add(criteria);
        Criteria criterib = Criteria.where("status").is(1);
        criteriasa.add(criterib);
        //分类查询
        //每种停车券，申领数量
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("sellerId").is(Integer.valueOf(sellerId))),
                Aggregation.match(Criteria.where("isApproval").is(2)),//已审核通过
                Aggregation.match(Criteria.where("status").is(1)),
                Aggregation.group("couponId").sum("number").as("number")
                //Aggregation.group("couponId").sum("knumber").as("knumber")
                //Aggregation.group("couponId").sum("knumber").as("totalknumber")
                //Aggregation.sort(orders),//排序
                //Aggregation.skip(page.getFirstResult()),//过滤
                //Aggregation.limit(pageSize)//页数
        );
        List<Map<String,Object>> tcSellerApplycoupongrouplist = new ArrayList<>();
        List<TcSellerApplycoupon> applycouponGroups=mongoComDAO.executeForObjectAggregateList(agg,"tcSellerApplycoupon",TcSellerApplycoupon.class);
        for(TcSellerApplycoupon tcSellerApplycoupon : applycouponGroups){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcSellerApplycoupon.getId());
            TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcSellerApplycoupon.getId(), TcCoupon.class);
            if(tcCoupon != null){
                condition.put("couponName",tcCoupon.getName());
            }else {
                condition.put("couponName","");
            }
            condition.put("number",tcSellerApplycoupon.getNumber());
            tcSellerApplycoupongrouplist.add(condition);
        }
        resultmap.put("totlalist",tcSellerApplycoupongrouplist);
        //申请记录列表
        List<TcSellerApplycoupon> tcSellerApplycouponpages = mongoComDAO.executeForObjectList(criteriasa,TcSellerApplycoupon.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcSellerApplycoupon.class);
        List<Map<String,Object>> tcSellerApplycouponlist = new ArrayList<>();
        for(TcSellerApplycoupon tcSellerApplycoupon : tcSellerApplycouponpages){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcSellerApplycoupon.getId());
            condition.put("couponId",tcSellerApplycoupon.getCouponId());
            TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcSellerApplycoupon.getCouponId(), TcCoupon.class);
            if(tcCoupon != null){
                condition.put("couponName",tcCoupon.getName());
            }else {
                condition.put("couponName","");
            }
            condition.put("sellerId",tcSellerApplycoupon.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcSellerApplycoupon.getSellerId());
            if (tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("sellerUserId",tcSellerApplycoupon.getSellerUserId());
            condition.put("number",tcSellerApplycoupon.getNumber());
            condition.put("knumber",tcSellerApplycoupon.getKnumber());
            condition.put("ynumber",tcSellerApplycoupon.getYnumber());
            condition.put("sdate",tcSellerApplycoupon.getSdate());
            condition.put("edate",tcSellerApplycoupon.getEdate());
            condition.put("isApproval",tcSellerApplycoupon.getIsApproval());
            condition.put("useStatus",tcSellerApplycoupon.getUseStatus());
            condition.put("status",tcSellerApplycoupon.getStatus());
            condition.put("createTime",tcSellerApplycoupon.getCreateTime());
            tcSellerApplycouponlist.add(condition);
        }
        resultmap.put("list",tcSellerApplycouponlist);
        resultmap.put("total",count);//总条数
        resultmap.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",resultmap);
    }

    /**
     * 用户发放停车券
     * 查询停车券发放总数
     * 停车券发放记录
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getExtendCoupon(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        Map<String,Object> resultmap = new HashMap<>();
        String sellerId =  MapUtils.getString(map, "sellerId");
        //类型：1:总计，2：申领，3：发放
        String subType =  MapUtils.getString(map, "subType");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //商家ID
        if(StringUtils.isNotEmpty(sellerId)){
            Criteria criteria = Criteria.where("sellerId").is(Integer.valueOf(sellerId));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        //每种发放停车券总数量统计
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("sellerId").is(Integer.valueOf(sellerId))),
                Aggregation.match(Criteria.where("isApproval").is(2)),
                Aggregation.match(Criteria.where("status").is(1)),
                Aggregation.group("couponId").sum("ynumber").as("ynumber")
                //Aggregation.group("couponId").sum("knumber").as("knumber")
                //Aggregation.group("couponId").sum("knumber").as("totalknumber")
                //Aggregation.sort(orders),//排序
                //Aggregation.skip(page.getFirstResult()),//过滤
                //Aggregation.limit(pageSize)//页数
        );
        List<Map<String,Object>> tcSellerApplycoupongrouplist = new ArrayList<>();
        List<TcSellerApplycoupon> applycouponGroups=mongoComDAO.executeForObjectAggregateList(agg,"tcSellerApplycoupon",TcSellerApplycoupon.class);
        for(TcSellerApplycoupon tcSellerApplycoupon : applycouponGroups){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcSellerApplycoupon.getId());
            TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcSellerApplycoupon.getId(), TcCoupon.class);
            if(tcCoupon != null){
                condition.put("couponName",tcCoupon.getName());
            }else {
                condition.put("couponName","");
            }
            condition.put("ynumber",tcSellerApplycoupon.getYnumber());
            tcSellerApplycoupongrouplist.add(condition);
        }
        resultmap.put("totlalist",tcSellerApplycoupongrouplist);
        //发放停车券记录
        List<TcExtendCoupon> tcExtendCoupons = mongoComDAO.executeForObjectList(criteriasa,TcExtendCoupon.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcExtendCoupon.class);
        List<Map<String,Object>> tcExtendCouponlist = new ArrayList<>();
        for(TcExtendCoupon tcExtendCoupon : tcExtendCoupons){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcExtendCoupon.getId());
            condition.put("couponId",tcExtendCoupon.getCouponId());
            TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcExtendCoupon.getCouponId(), TcCoupon.class);
            if(tcCoupon != null){
                condition.put("couponName",tcCoupon.getName());
                condition.put("couponDesc",tcCoupon.getDesc());
                condition.put("couponDuration",tcCoupon.getDuration());
                condition.put("couponamount",tcCoupon.getAmount());
            }else {
                condition.put("couponName","");
                condition.put("couponDesc","");
                condition.put("couponDuration","");
                condition.put("couponamount","");
            }
            condition.put("extendId",tcExtendCoupon.getExtendId());
            condition.put("carId",tcExtendCoupon.getCarId());
            condition.put("carNum",tcExtendCoupon.getCarNum());
            condition.put("sellerId",tcExtendCoupon.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcExtendCoupon.getSellerId());
            if (tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("ticketImg",tcExtendCoupon.getTicketImg());
            condition.put("useStatus",tcExtendCoupon.getUseStatus());
            condition.put("status",tcExtendCoupon.getStatus());
            condition.put("createTime",tcExtendCoupon.getCreateTime());
            tcExtendCouponlist.add(condition);
        }
        resultmap.put("list",tcExtendCouponlist);//返回前端集合命名为list
        resultmap.put("total",count);//总条数
        resultmap.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",resultmap);
    }



}
