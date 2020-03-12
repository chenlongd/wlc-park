package com.perenc.xh.lsp.service.tcOrderInvoice.impl;

import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.dao.order.SysOrderDao;
import com.perenc.xh.lsp.entity.order.SysOrder;
import com.perenc.xh.lsp.entity.tcInvoice.TcInvoice;
import com.perenc.xh.lsp.entity.tcOrder.TcOrderInvoice;
import com.perenc.xh.lsp.service.tcOrderInvoice.TcOrderInvoiceService;
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


@Service("tcOrderInvoiceService")
@Transactional(rollbackFor = Exception.class)
public class TcOrderInvoiceServiceImpl implements TcOrderInvoiceService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired
    private ExtendUserDao extendUserDao;

    @Autowired
    private SysOrderDao orderDao;

    @Override
    public ReturnJsonData insert(TcOrderInvoice tcOrderInvoice) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcOrderInvoice.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcOrderInvoice.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcOrderInvoice);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcOrderInvoice tcOrderInvoice) throws Exception {
        TcOrderInvoice returnTcOrderInvoice = mongoComDAO.executeForObjectById(tcOrderInvoice.getId(), TcOrderInvoice.class);
        if(returnTcOrderInvoice != null){
            returnTcOrderInvoice.setOrderId(tcOrderInvoice.getOrderId());
            returnTcOrderInvoice.setInvoiceId(tcOrderInvoice.getInvoiceId());
            int flag = mongoComDAO.executeUpdate(returnTcOrderInvoice);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 发票上传图片
     * @param tcOrderInvoice
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateFile(TcOrderInvoice tcOrderInvoice,Map<String, Object> map) throws Exception {
        String invoiceImg =  MapUtils.getString(map, "invoiceImg");
        TcOrderInvoice returnTcOrderInvoice = mongoComDAO.executeForObjectById(tcOrderInvoice.getId(), TcOrderInvoice.class);
        if(returnTcOrderInvoice != null){
            TcInvoice tcInvoice = mongoComDAO.executeForObjectById(returnTcOrderInvoice.getInvoiceId(), TcInvoice.class);
            int flagin=0;
            int flagor=0;
            if(tcInvoice != null) {
                tcInvoice.setFile(invoiceImg);
                flagin = mongoComDAO.executeUpdate(tcInvoice);
            }
            //查询订单是否存在
           SysOrder order = orderDao.getById(returnTcOrderInvoice.getOrderId());
            if(order != null) {
                order.setInvoiceStatus(3);
                flagor = orderDao.update(DBUtil.toUpdateParam(order, "id"));
            }
            if(flagin > 0 && flagor>0){
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcOrderInvoice.class);
        if (flag > 0) {
            int flagd=0;
            for (int i = 0; i <ids.length ; i++) {
                //发票表也需要删除
                Map<String,Object> conditiond=new HashMap<>();
                conditiond.put("invoiceId",ids[i]);
                conditiond.put("status",1);
                Map<String,Object> content=new HashMap<>();
                content.put("status",2);
                flagd+= mongoComDAO.executeUpdate(conditiond,content, TcInvoice.class);
            }
            if(flagd>0) {
                return new ReturnJsonData(DataCodeUtil.SUCCESS, "删除成功", null);
            }else {
                return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE, "删除失败", null);
            }
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
        TcOrderInvoice tcOrderInvoice = mongoComDAO.executeForObjectById(id, TcOrderInvoice.class);
        if (tcOrderInvoice!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcOrderInvoice.getId());
            condition.put("orderId",tcOrderInvoice.getOrderId());
            SysOrder order = orderDao.getById(tcOrderInvoice.getOrderId());
            if(order!=null) {
                condition.put("orderNo",order.getOrderNo());
                condition.put("totalPrice", ToolUtil.getIntToDouble(order.getTotalPrice()));
                condition.put("invoiceStatus",order.getInvoiceStatus());
            }else {
                condition.put("orderNo","");
                condition.put("totalPrice","");
                condition.put("invoiceStatus","");
            }
            condition.put("invoiceId",tcOrderInvoice.getInvoiceId());
            TcInvoice tcInvoice = mongoComDAO.executeForObjectById(tcOrderInvoice.getInvoiceId(), TcInvoice.class);
            if(tcInvoice!=null) {
                condition.put("type",tcInvoice.getType());
                condition.put("title",tcInvoice.getTitle());
                condition.put("idNumber",tcInvoice.getIdNumber());
                condition.put("email",tcInvoice.getEmail());
                condition.put("contact",tcInvoice.getContact());
                condition.put("province",tcInvoice.getProvince());
                condition.put("city",tcInvoice.getCity());
                condition.put("county",tcInvoice.getCounty());
                condition.put("address",tcInvoice.getTitle());
                condition.put("bankName",tcInvoice.getBankName());
                condition.put("bankNumber",tcInvoice.getBankNumber());
                condition.put("phone",tcInvoice.getPhone());
                condition.put("invoiceNum",tcInvoice.getInvoiceNum());
                condition.put("amount",ToolUtil.getIntToDouble(tcInvoice.getAmount()));
                condition.put("file",tcInvoice.getFile());
                condition.put("isInvoice",tcInvoice.getIsInvoice());
                condition.put("remark",tcInvoice.getRemark());
            }else {
                condition.put("type","");
                condition.put("title","");
                condition.put("idNumber","");
                condition.put("email","");
                condition.put("contact","");
                condition.put("province","");
                condition.put("city","");
                condition.put("county","");
                condition.put("address","");
                condition.put("bankName","");
                condition.put("bankNumber","");
                condition.put("phone","");
                condition.put("invoiceNum","");
                condition.put("amount",0);
                condition.put("file","");
                condition.put("isInvoice","");
                condition.put("remark","");
            }
            condition.put("status",tcOrderInvoice.getStatus());
            condition.put("createTime",tcOrderInvoice.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String extendId =  MapUtils.getString(map, "extendId");
        String invoiceId =  MapUtils.getString(map, "invoiceId");
        //String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //客户ID
        if(StringUtils.isNotEmpty(extendId)){
            Criteria criteria = Criteria.where("extendId").is(Integer.valueOf(extendId));
            criteriasa.add(criteria);
        }
        //发票Id
        if(StringUtils.isNotEmpty(invoiceId)){
            Criteria criteria = Criteria.where("invoiceId").is(Integer.valueOf(invoiceId));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcOrderInvoice> tcOrderInvoices = mongoComDAO.executeForObjectList(criteriasa,TcOrderInvoice.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcOrderInvoice.class);
        List<Map<String,Object>> tcOrderInvoicelist = new ArrayList<>();
        for(TcOrderInvoice tcOrderInvoice : tcOrderInvoices){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcOrderInvoice.getId());
            condition.put("orderId",tcOrderInvoice.getOrderId());
            SysOrder order = orderDao.getById(tcOrderInvoice.getOrderId());
            if(order!=null) {
                condition.put("orderNo",order.getOrderNo());
                condition.put("totalPrice", ToolUtil.getIntToDouble(order.getTotalPrice()));
                condition.put("invoiceStatus",order.getInvoiceStatus());
            }else {
                condition.put("orderNo","");
                condition.put("totalPrice","");
                condition.put("invoiceStatus","");
            }
            condition.put("invoiceId",tcOrderInvoice.getInvoiceId());
            TcInvoice tcInvoice = mongoComDAO.executeForObjectById(tcOrderInvoice.getInvoiceId(), TcInvoice.class);
            if(tcInvoice!=null) {
                condition.put("type",tcInvoice.getType());
                condition.put("title",tcInvoice.getTitle());
                condition.put("idNumber",tcInvoice.getIdNumber());
                condition.put("email",tcInvoice.getEmail());
                condition.put("contact",tcInvoice.getContact());
                condition.put("province",tcInvoice.getProvince());
                condition.put("city",tcInvoice.getCity());
                condition.put("county",tcInvoice.getCounty());
                condition.put("address",tcInvoice.getTitle());
                condition.put("bankName",tcInvoice.getBankName());
                condition.put("bankNumber",tcInvoice.getBankNumber());
                condition.put("phone",tcInvoice.getPhone());
                condition.put("invoiceNum",tcInvoice.getInvoiceNum());
                condition.put("amount",ToolUtil.getIntToDouble(tcInvoice.getAmount()));
                condition.put("file",tcInvoice.getFile());
                condition.put("isInvoice",tcInvoice.getIsInvoice());
                condition.put("remark",tcInvoice.getRemark());
            }else {
                condition.put("type","");
                condition.put("title","");
                condition.put("idNumber","");
                condition.put("email","");
                condition.put("contact","");
                condition.put("province","");
                condition.put("city","");
                condition.put("county","");
                condition.put("address","");
                condition.put("bankName","");
                condition.put("bankNumber","");
                condition.put("phone","");
                condition.put("invoiceNum","");
                condition.put("amount",0);
                condition.put("file","");
                condition.put("isInvoice","");
                condition.put("remark","");
            }
            condition.put("status",tcOrderInvoice.getStatus());
            condition.put("createTime",tcOrderInvoice.getCreateTime());
            tcOrderInvoicelist.add(condition);
        }
        map.clear();
        map.put("list",tcOrderInvoicelist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

    /**
     * 订单发票明细
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getAllList(Map<String, Object> map) throws Exception {
        String invoiceId =  MapUtils.getString(map, "invoiceId");
        //String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //所属类型
        if(StringUtils.isNotEmpty(invoiceId)){
            Criteria criteria = Criteria.where("invoiceId").is(Integer.valueOf(invoiceId));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcOrderInvoice> tcOrderInvoices = mongoComDAO.executeForObjectList(criteriasa,TcOrderInvoice.class,orders);
        List<Map<String,Object>> tcOrderInvoicelist = new ArrayList<>();
        for(TcOrderInvoice tcOrderInvoice : tcOrderInvoices){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcOrderInvoice.getId());
            condition.put("orderId",tcOrderInvoice.getOrderId());
            SysOrder order = orderDao.getById(tcOrderInvoice.getOrderId());
            if(order!=null) {
                condition.put("orderNo",order.getOrderNo());
                condition.put("totalPrice", ToolUtil.getIntToDouble(order.getTotalPrice()));
                condition.put("invoiceStatus",order.getInvoiceStatus());
            }else {
                condition.put("orderNo","");
                condition.put("totalPrice","");
                condition.put("invoiceStatus","");
            }
            condition.put("invoiceId",tcOrderInvoice.getInvoiceId());
            TcInvoice tcInvoice = mongoComDAO.executeForObjectById(tcOrderInvoice.getInvoiceId(), TcInvoice.class);
            if(tcInvoice!=null) {
                condition.put("type",tcInvoice.getType());
                condition.put("title",tcInvoice.getTitle());
                condition.put("idNumber",tcInvoice.getIdNumber());
                condition.put("email",tcInvoice.getEmail());
                condition.put("contact",tcInvoice.getContact());
                condition.put("province",tcInvoice.getProvince());
                condition.put("city",tcInvoice.getCity());
                condition.put("county",tcInvoice.getCounty());
                condition.put("address",tcInvoice.getTitle());
                condition.put("bankName",tcInvoice.getBankName());
                condition.put("bankNumber",tcInvoice.getBankNumber());
                condition.put("phone",tcInvoice.getPhone());
                condition.put("invoiceNum",tcInvoice.getInvoiceNum());
                condition.put("amount",ToolUtil.getIntToDouble(tcInvoice.getAmount()));
                condition.put("file",tcInvoice.getFile());
                condition.put("isInvoice",tcInvoice.getIsInvoice());
                condition.put("remark",tcInvoice.getRemark());
            }else {
                condition.put("type","");
                condition.put("title","");
                condition.put("idNumber","");
                condition.put("email","");
                condition.put("contact","");
                condition.put("province","");
                condition.put("city","");
                condition.put("county","");
                condition.put("address","");
                condition.put("bankName","");
                condition.put("bankNumber","");
                condition.put("phone","");
                condition.put("invoiceNum","");
                condition.put("amount",0);
                condition.put("file","");
                condition.put("isInvoice","");
                condition.put("remark","");
            }
            condition.put("status",tcOrderInvoice.getStatus());
            condition.put("createTime",tcOrderInvoice.getCreateTime());
            tcOrderInvoicelist.add(condition);
        }
        map.clear();
        map.put("list",tcOrderInvoicelist);//返回前端集合命名为list
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }



}
