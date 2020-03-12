package com.perenc.xh.lsp.service.tcSellerApplyc.impl;


import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.ConstantType;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.dao.tcSeller.TcSellerDao;
import com.perenc.xh.lsp.entity.tcCoupon.TcCoupon;
import com.perenc.xh.lsp.entity.tcSeller.TcSeller;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplyc;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplycData;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplycoupon;
import com.perenc.xh.lsp.service.tcSellerApplyc.TcSellerApplycService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


@Service("tcSellerApplycService")
@Transactional(rollbackFor = Exception.class)
public class TcSellerApplycServiceImpl implements TcSellerApplycService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private TcSellerDao tcSellerDao;

    @Override
    public ReturnJsonData insert(TcSellerApplyc tcSellerApplyc) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        // 申请类型 1=商家发放；2=婚宴、会议发放
        tcSellerApplyc.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcSellerApplyc.setIsApproval(1);
        tcSellerApplyc.setUseStatus(1); //待使用
        tcSellerApplyc.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcSellerApplyc);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    /**
     * 添加主表及明细表
     * @param tcSellerApplyc
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertDetail(TcSellerApplyc tcSellerApplyc) throws Exception {
        // 申请类型 1=商家发放；2=婚宴、会议发放
        tcSellerApplyc.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcSellerApplyc.setIsApproval(1);
        tcSellerApplyc.setUseStatus(1); //待使用
        tcSellerApplyc.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcSellerApplyc);
        // 添加明细表
        TcSellerApplycoupon sellerApplycoupon =new TcSellerApplycoupon();
        sellerApplycoupon.setType(tcSellerApplyc.getType());
        sellerApplycoupon.setCouponId("");
        sellerApplycoupon.setName(tcSellerApplyc.getName());
        sellerApplycoupon.setSellerId(tcSellerApplyc.getSellerId());
        sellerApplycoupon.setSellerApplyId(tcSellerApplyc.getId());
        sellerApplycoupon.setNumber(tcSellerApplyc.getNumber());
        sellerApplycoupon.setKnumber(tcSellerApplyc.getNumber()); //张数
        sellerApplycoupon.setYnumber(0);
        sellerApplycoupon.setTicketImg(tcSellerApplyc.getTicketImg());
        sellerApplycoupon.setSdate(tcSellerApplyc.getSdate());
        sellerApplycoupon.setEdate(tcSellerApplyc.getEdate());
        sellerApplycoupon.setIsApproval(1); //待审核
        sellerApplycoupon.setUseStatus(1); //待使用
        sellerApplycoupon.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        sellerApplycoupon.setStatus(1);
        int flagd = mongoComDAO.executeInsert(sellerApplycoupon);
        if(flag > 0 && flagd>0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }


    /**
     * 会议宴会申请停车券
     * @param tcSellerApplyc
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertMeeting(TcSellerApplyc tcSellerApplyc) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        // 申请类型 1=商家发放；2=婚宴、会议发放
        tcSellerApplyc.setType(2);
        // 有效期
        tcSellerApplyc.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcSellerApplyc.setIsApproval(1);
        tcSellerApplyc.setUseStatus(1); //待使用
        tcSellerApplyc.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcSellerApplyc);
        if(flag > 0 ){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcSellerApplyc tcSellerApplyc) throws Exception {
        TcSellerApplyc returnTcSellerApplyc = mongoComDAO.executeForObjectById(tcSellerApplyc.getId(), TcSellerApplyc.class);
        if(returnTcSellerApplyc != null){
            returnTcSellerApplyc.setSellerId(tcSellerApplyc.getSellerId());
            returnTcSellerApplyc.setSdate(tcSellerApplyc.getSdate());
            returnTcSellerApplyc.setEdate(tcSellerApplyc.getEdate());
            int flag = mongoComDAO.executeUpdate(returnTcSellerApplyc);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 更改主表及明细表
     * @param tcSellerApplyc
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateDetail(TcSellerApplyc tcSellerApplyc) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer number=tcSellerApplyc.getNumber();
        Integer ynumber=0;
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        Date nowTime = calendar.getTime();
        Date eDate = sdf.parse(tcSellerApplyc.getEdate());
        //免费车的时间判断
        if (eDate.before(nowTime)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "结束时间不能小于当前时间", null);
        }
        List<Criteria> criteriasnuma = new ArrayList<Criteria>();
        Criteria criterina = Criteria.where("sellerApplyId").is(tcSellerApplyc.getId());
        criteriasnuma.add(criterina);
        List<TcSellerApplycoupon> sellerApplycoupons = mongoComDAO.executeForObjectList(criteriasnuma, TcSellerApplycoupon.class);
        for(TcSellerApplycoupon tcSellerApplycouponn : sellerApplycoupons){
            ynumber=ynumber+tcSellerApplycouponn.getYnumber();
        }
        if(number<ynumber) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"张数不能小于已发放张数",null);
        }
        TcSellerApplyc returnTcSellerApplyc = mongoComDAO.executeForObjectById(tcSellerApplyc.getId(), TcSellerApplyc.class);
        if(returnTcSellerApplyc != null){
            returnTcSellerApplyc.setName(tcSellerApplyc.getName());
            returnTcSellerApplyc.setNumber(tcSellerApplyc.getNumber());
            //returnTcSellerApplyc.setTicketImg(tcSellerApplyc.getTicketImg());
            returnTcSellerApplyc.setSdate(tcSellerApplyc.getSdate());
            returnTcSellerApplyc.setEdate(tcSellerApplyc.getEdate());
            returnTcSellerApplyc.setUseStatus(1);
            int flag = mongoComDAO.executeUpdate(returnTcSellerApplyc);
            if(flag > 0){
                Map<String,Object> conditiond=new HashMap<>();
                conditiond.put("sellerApplyId",tcSellerApplyc.getId());
                conditiond.put("status",1);
                Map<String,Object> content=new HashMap<>();
                content.put("name",tcSellerApplyc.getName());
                content.put("number",tcSellerApplyc.getNumber());
                //content.put("ticketImg",tcSellerApplyc.getTicketImg());
                content.put("sdate",tcSellerApplyc.getSdate());
                content.put("edate",tcSellerApplyc.getEdate());
                returnTcSellerApplyc.setUseStatus(1);
                mongoComDAO.executeUpdate(conditiond,content, TcSellerApplycoupon.class);
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }


    /**
     * 商家申领停车券审批
     * 新增
     * 申领票券
     * 申领票明细
     * @param data
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertSellerApply(TcSellerApplycData data) throws Exception {
        TcSellerApplyc returnTcSellerApplyc = mongoComDAO.executeForObjectById(data.getId(), TcSellerApplyc.class);
        List<TcSellerApplycoupon> sellerApplycouponList = data.getSellerApplycouponList();
        Integer tcaNumber=0;
        for (TcSellerApplycoupon sellerApplycoupon : sellerApplycouponList) {
            tcaNumber=tcaNumber+sellerApplycoupon.getNumber();
        }
        if(!returnTcSellerApplyc.getNumber().equals(tcaNumber)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"输入明细张数与总张数不一致",null);
        }
        if(returnTcSellerApplyc != null){
            returnTcSellerApplyc.setIsApproval(data.getIsApproval());
            int flag = mongoComDAO.executeUpdate(returnTcSellerApplyc);
            if(flag > 0){
                //审核通过
                if(data.getIsApproval().equals(2)){
                    for (TcSellerApplycoupon sellerApplycoupon : sellerApplycouponList) {
                        if (StringUtils.isEmpty(sellerApplycoupon.getId())) {
                            sellerApplycoupon.setId(null);
                        }
                        sellerApplycoupon.setType(returnTcSellerApplyc.getType());
                        sellerApplycoupon.setSellerId(returnTcSellerApplyc.getSellerId());
                        sellerApplycoupon.setSellerApplyId(data.getId());
                        sellerApplycoupon.setKnumber(sellerApplycoupon.getNumber());
                        sellerApplycoupon.setYnumber(0);
                        sellerApplycoupon.setSdate("");
                        sellerApplycoupon.setEdate("");
                        sellerApplycoupon.setIsApproval(2); //审核通过
                        sellerApplycoupon.setUseStatus(1); //待使用
                        sellerApplycoupon.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
                        sellerApplycoupon.setStatus(1);
                    }
                    mongoComDAO.executeInserts(sellerApplycouponList, TcSellerApplycoupon.class);
                }
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "修改成功", null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 商家申领停车券审批
     * @param tcSellerApplyc
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateIsApproval(TcSellerApplyc tcSellerApplyc) throws Exception {
        TcSellerApplyc returnTcSellerApplyc = mongoComDAO.executeForObjectById(tcSellerApplyc.getId(), TcSellerApplyc.class);
        if(returnTcSellerApplyc != null){
            returnTcSellerApplyc.setIsApproval(tcSellerApplyc.getIsApproval());
            int flag = mongoComDAO.executeUpdate(returnTcSellerApplyc);
            if(flag > 0){
                //明细也需要更改状态
                Map<String,Object> condition=new HashMap<>();
                condition.put("sellerApplyId",tcSellerApplyc.getId());
                condition.put("status",1);
                Map<String,Object> content=new HashMap<>();
                content.put("isApproval",tcSellerApplyc.getIsApproval());
                int flagd= mongoComDAO.executeUpdate(condition,content, TcSellerApplycoupon.class);
                if(flagd>0) {
                    return new ReturnJsonData(DataCodeUtil.SUCCESS, "修改成功", null);
                }else {
                    return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
                }
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }


    /**
     * 商家申领停车券审批
     * 选择票券id（1、2、3小时）,张数
     * @param tcSellerApplyc
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateIsApprovalApply(TcSellerApplyc tcSellerApplyc,Map<String, Object> map) throws Exception {
        String couponId =  MapUtils.getString(map, "couponId");
        TcSellerApplyc returnTcSellerApplyc = mongoComDAO.executeForObjectById(tcSellerApplyc.getId(), TcSellerApplyc.class);
        if(returnTcSellerApplyc != null){
            returnTcSellerApplyc.setIsApproval(tcSellerApplyc.getIsApproval());
            int flag = mongoComDAO.executeUpdate(returnTcSellerApplyc);
            //明细也需要更改状态
            Map<String,Object> condition=new HashMap<>();
            condition.put("sellerApplyId",tcSellerApplyc.getId());
            condition.put("status",1);
            Map<String,Object> content=new HashMap<>();
            if(StringUtils.isNotEmpty(couponId)) {
                content.put("couponId", couponId);
            }
            content.put("isApproval",tcSellerApplyc.getIsApproval());
            int flagd= mongoComDAO.executeUpdate(condition,content, TcSellerApplycoupon.class);
            if(flag > 0 && flagd>0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "修改成功", null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 修改有效期时间
     * 开始时间-结束时间
     * @param tcSellerApplyc
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateTime(TcSellerApplyc tcSellerApplyc) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Calendar calendar = null;
            calendar = Calendar.getInstance();
            Date nowTime = calendar.getTime();
            Date eDate = sdf.parse(tcSellerApplyc.getEdate());
            //免费车的时间判断
            if (eDate.before(nowTime)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "结束时间不能小于当前时间", null);
            }
            //停车时长（小时）：当前时间-开始时间
            double duration = ToolUtil.getDateHourNum(tcSellerApplyc.getSdate(), tcSellerApplyc.getEdate());
            if (duration < 1) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "有效期时间至少大于1小时", null);
            }
            TcSellerApplyc rTcSellerApplyc = mongoComDAO.executeForObjectById(tcSellerApplyc.getId(), TcSellerApplyc.class);
            if(rTcSellerApplyc == null){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "传入的ID错误", null);
            }
            rTcSellerApplyc.setSdate(tcSellerApplyc.getSdate());
            rTcSellerApplyc.setEdate(tcSellerApplyc.getEdate());
            int flagupc= mongoComDAO.executeUpdate(rTcSellerApplyc);
            if(flagupc>0) {
                //明细也需要更改状态
                Map<String,Object> condition=new HashMap<>();
                condition.put("sellerApplyId",tcSellerApplyc.getId());
                condition.put("status",1);
                Map<String,Object> content=new HashMap<>();
                content.put("sdate",tcSellerApplyc.getSdate());
                content.put("edate",tcSellerApplyc.getEdate());
                int flagd= mongoComDAO.executeUpdate(condition,content, TcSellerApplycoupon.class);
                if (flagd>0) {
                    return new ReturnJsonData(DataCodeUtil.SUCCESS, "修改成功", null);
                } else {
                    return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
                }
            }else {
                return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
            }
        }catch (Exception e) {
            System.out.println("执行错误"+e.getMessage());
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }


    @Override
    public ReturnJsonData delete(String[] ids) throws Exception {
        Map<String,Object> condition=new HashMap<>();
        condition.put("ids",ids);
        condition.put("status",2);
        int flag= mongoComDAO.executeUpdateByIds(condition, TcSellerApplyc.class);
        if (flag > 0) {
            int flagd=0;
            for (int i = 0; i <ids.length ; i++) {
                //明细也需要删除
                Map<String,Object> conditiond=new HashMap<>();
                conditiond.put("sellerApplyId",ids[i]);
                conditiond.put("status",1);
                Map<String,Object> content=new HashMap<>();
                content.put("status",2);
                flagd+= mongoComDAO.executeUpdate(conditiond,content, TcSellerApplycoupon.class);
            }
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
        TcSellerApplyc tcSellerApplyc = mongoComDAO.executeForObjectById(id, TcSellerApplyc.class);
        if (tcSellerApplyc!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcSellerApplyc.getId());
            condition.put("sellerId",tcSellerApplyc.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcSellerApplyc.getSellerId());
            if (tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            //condition.put("sdate",tcSellerApplyc.getSdate());
            //condition.put("edate",tcSellerApplyc.getEdate());
            condition.put("number",tcSellerApplyc.getNumber());
            condition.put("isApproval",tcSellerApplyc.getIsApproval());
            condition.put("useStatus",tcSellerApplyc.getUseStatus());
            Map<String,Object> param = new HashMap<>();
            param.put("sellerApplyId",id);
            List<TcSellerApplycoupon> sellerApplycoupons = mongoComDAO.executeForObjectList(param, TcSellerApplycoupon.class);
            List<Map<String,Object>> sellerApplycouponList = new ArrayList<>();
            if(sellerApplycoupons.size() > 0){
                for(TcSellerApplycoupon tcSellerApplycoupon : sellerApplycoupons){
                    Map<String,Object> attrMap = new HashMap<>();
                    attrMap.put("id",tcSellerApplycoupon.getId());
                    attrMap.put("couponId",tcSellerApplycoupon.getCouponId());
                    TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcSellerApplycoupon.getCouponId(), TcCoupon.class);
                    if(tcCoupon != null){
                        attrMap.put("couponName",tcCoupon.getName());
                        attrMap.put("couponDesc",tcCoupon.getDesc());
                        attrMap.put("couponDuration",tcCoupon.getDuration());
                        attrMap.put("couponAmount",tcCoupon.getAmount());
                    }else {
                        attrMap.put("couponName","");
                        attrMap.put("couponDesc","");
                        attrMap.put("couponDuration","");
                        attrMap.put("couponAmount","");
                    }
                    attrMap.put("number",tcSellerApplycoupon.getNumber());
                    attrMap.put("knumber",tcSellerApplycoupon.getKnumber());
                    attrMap.put("ynumber",tcSellerApplycoupon.getYnumber());
                    //attrMap.put("sdate",tcSellerApplyc.getSdate());
                    //attrMap.put("edate",tcSellerApplyc.getEdate());
                    attrMap.put("isApproval",tcSellerApplycoupon.getIsApproval());
                    attrMap.put("useStatus",tcSellerApplycoupon.getUseStatus());
                    attrMap.put("status",tcSellerApplycoupon.getStatus());
                    attrMap.put("createTime",tcSellerApplycoupon.getCreateTime());
                    sellerApplycouponList.add(attrMap);
                }
            }
            condition.put("sellerApplycouponList",sellerApplycouponList);
            condition.put("status",tcSellerApplyc.getStatus());
            condition.put("createTime",tcSellerApplyc.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String name =  MapUtils.getString(map, "name");
        String sellerId =  MapUtils.getString(map, "sellerId");
        String isApproval =  MapUtils.getString(map, "isApproval");
        String useStatus =  MapUtils.getString(map, "useStatus");
        String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //商家ID
        if(StringUtils.isNotEmpty(sellerId)){
            Criteria criteria = Criteria.where("sellerId").is(Integer.valueOf(sellerId));
            criteriasa.add(criteria);
        }
        // 申请名称查询
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
        // 是否审核(1:待审核2:已通过，3:未通过)
        if(StringUtils.isNotEmpty(isApproval)){
            Criteria criteria = Criteria.where("isApproval").is(Integer.valueOf(isApproval));
            criteriasa.add(criteria);
        }
        // 使用状态 1:待使用，3:已使用，4:已过期
        if(StringUtils.isNotEmpty(useStatus)){
            Criteria criteria = Criteria.where("useStatus").is(Integer.valueOf(useStatus));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcSellerApplyc> tcSellerApplycs = mongoComDAO.executeForObjectList(criteriasa,TcSellerApplyc.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcSellerApplyc.class);
        List<Map<String,Object>> tcSellerApplycouponlist = new ArrayList<>();
        for(TcSellerApplyc tcSellerApplyc : tcSellerApplycs){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcSellerApplyc.getId());
            condition.put("name",tcSellerApplyc.getName());
            condition.put("type",tcSellerApplyc.getType());
            condition.put("sellerId",tcSellerApplyc.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcSellerApplyc.getSellerId());
            if (tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("sellerUserId",tcSellerApplyc.getSellerUserId());
            condition.put("ticketImg",tcSellerApplyc.getTicketImg());
            condition.put("number",tcSellerApplyc.getNumber());
            condition.put("sdate",tcSellerApplyc.getSdate());
            condition.put("edate",tcSellerApplyc.getEdate());
            condition.put("isApproval",tcSellerApplyc.getIsApproval());
            condition.put("useStatus",tcSellerApplyc.getUseStatus());
            condition.put("status",tcSellerApplyc.getStatus());
            condition.put("createTime",tcSellerApplyc.getCreateTime());
            tcSellerApplycouponlist.add(condition);
        }
        map.clear();
        map.put("list",tcSellerApplycouponlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }


    /**
     * 后台使用分页使用
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getAdminList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String sellerId =  MapUtils.getString(map, "sellerId");
        String sellerName =  MapUtils.getString(map, "sellerName");
        String isApproval =  MapUtils.getString(map, "isApproval");
        //String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //商家ID
        if(StringUtils.isNotEmpty(sellerId)){
            Criteria criteria = Criteria.where("sellerId").is(Integer.valueOf(sellerId));
            criteriasa.add(criteria);
        }
        //商家名称
        if(StringUtils.isNotEmpty(sellerName)){
            //筛选
            QueryParam param = new QueryParam();
            param.addCondition("name","like","%"+sellerName+"%");
            param.addCondition("status","=",1);
            List<TcSeller> tcSellers = tcSellerDao.list(param);
            //查询
            List<Integer> listSellerIds=new ArrayList<>();
            for(TcSeller tcSeller : tcSellers){
                listSellerIds.add(tcSeller.getId());
            }
            Criteria criteria = Criteria.where("sellerId").in(listSellerIds);
            criteriasa.add(criteria);
        }
        // 是否审核(1:待审核2:已通过，3:未通过)
        if(StringUtils.isNotEmpty(isApproval)){
            Criteria criteria = Criteria.where("isApproval").is(Integer.valueOf(isApproval));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcSellerApplyc> tcSellerApplycs = mongoComDAO.executeForObjectList(criteriasa,TcSellerApplyc.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcSellerApplyc.class);
        List<Map<String,Object>> tcSellerApplycouponlist = new ArrayList<>();
        for(TcSellerApplyc tcSellerApplyc : tcSellerApplycs){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcSellerApplyc.getId());
            condition.put("name",tcSellerApplyc.getName());
            condition.put("type",tcSellerApplyc.getType());
            condition.put("sellerId",tcSellerApplyc.getSellerId());
            TcSeller tcSeller = tcSellerDao.getById(tcSellerApplyc.getSellerId());
            if (tcSeller!=null) {
                condition.put("sellerName",tcSeller.getName());
            }else {
                condition.put("sellerName","");
            }
            condition.put("sellerUserId",tcSellerApplyc.getSellerUserId());
            condition.put("ticketImg",tcSellerApplyc.getTicketImg());
            condition.put("number",tcSellerApplyc.getNumber());
            condition.put("sdate",tcSellerApplyc.getSdate());
            condition.put("edate",tcSellerApplyc.getEdate());
            condition.put("isApproval",tcSellerApplyc.getIsApproval());
            condition.put("useStatus",tcSellerApplyc.getUseStatus());
            Map<String,Object> param = new HashMap<>();
            param.put("sellerApplyId",tcSellerApplyc.getId());
            TcSellerApplycoupon tcSellerApplycoupon = mongoComDAO.executeForObjectByCon(param, TcSellerApplycoupon.class);
            if(tcSellerApplycoupon!=null && tcSellerApplycoupon.getIsApproval().equals(2)){
                condition.put("couponId",tcSellerApplycoupon.getCouponId());
                TcCoupon tcCoupon = mongoComDAO.executeForObjectById(tcSellerApplycoupon.getCouponId(), TcCoupon.class);
                if(tcCoupon != null){
                    condition.put("couponName",tcCoupon.getName());
                }else {
                    condition.put("couponName","");
                }
            }else {
                condition.put("couponId","");
                condition.put("couponName","");
            }
            //查询剩下的张数
            Integer sxnumber=0;
            List<Criteria> criteriasnuma = new ArrayList<Criteria>();
            //商家ID
            Criteria criterina = Criteria.where("sellerId").is(tcSellerApplyc.getSellerId());
            criteriasnuma.add(criterina);
            Criteria criterinb = Criteria.where("knumber").gt(0);
            criteriasnuma.add(criterinb);
            List<TcSellerApplycoupon> sellerApplycoupons = mongoComDAO.executeForObjectList(criteriasnuma, TcSellerApplycoupon.class);
            for(TcSellerApplycoupon tcSellerApplycouponn : sellerApplycoupons){
                Map<String,Object> attrMap = new HashMap<>();
                sxnumber=sxnumber+tcSellerApplycouponn.getKnumber();
            }
            condition.put("sxnumber",sxnumber);
            condition.put("status",tcSellerApplyc.getStatus());
            condition.put("createTime",tcSellerApplyc.getCreateTime());
            tcSellerApplycouponlist.add(condition);
        }
        map.clear();
        map.put("list",tcSellerApplycouponlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }


    /**
     * 根据当前时间
     * 使用状态更改已过期处理
     * 根据当前时间，把使用状态更改
     * @param
     * @return
     */
    @Override
    public Integer updateBatchSellerApplycUseStatusByEdate() throws Exception {
        Integer num=0;
        SimpleDateFormat  sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar =null;
        calendar = Calendar.getInstance();
        String  strnowDate=sdfhms.format(calendar.getTime()); //当前时间
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //结束时间小于等于当前时间
        Criteria criteria = Criteria.where("edate").lte(strnowDate);
        criteriasa.add(criteria);
        //票券状态为待使用
        Criteria criterib = Criteria.where("useStatus").is(ConstantType.USE_STATUS_WAITFOR);
        criteriasa.add(criterib);
        // 申请类型 2=婚宴、会议发放
        Criteria criteric = Criteria.where("type").is(2);
        criteriasa.add(criteric);
        List<TcSellerApplyc> tcSellerApplycs = mongoComDAO.executeForObjectList(criteriasa,TcSellerApplyc.class);
        for(TcSellerApplyc tcSellerApplyc : tcSellerApplycs){
            TcSellerApplyc returnTcSellerApplyc = mongoComDAO.executeForObjectById(tcSellerApplyc.getId(), TcSellerApplyc.class);
            if(returnTcSellerApplyc != null) {
                returnTcSellerApplyc.setUseStatus(ConstantType.USE_STATUS_EXPIRED);
                // 明细也需要删除
                Map<String,Object> conditiond=new HashMap<>();
                conditiond.put("sellerApplyId",returnTcSellerApplyc.getId());
                conditiond.put("status",1);
                Map<String,Object> content=new HashMap<>();
                content.put("useStatus",ConstantType.USE_STATUS_EXPIRED);
                mongoComDAO.executeUpdate(conditiond,content, TcSellerApplycoupon.class);
                num =+ mongoComDAO.executeUpdate(returnTcSellerApplyc);
            }
        }
        return num;
    }



}
