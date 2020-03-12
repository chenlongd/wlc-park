package com.perenc.xh.lsp.service.tcInvoice.impl;

import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.ConstantType;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.dao.order.SysOrderDao;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.order.SysOrder;
import com.perenc.xh.lsp.entity.tcInvoice.TcInvoice;
import com.perenc.xh.lsp.entity.tcOrder.TcOrderInvoice;
import com.perenc.xh.lsp.service.tcInvoice.TcInvoiceService;
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


@Service("tcInvoiceService")
@Transactional(rollbackFor = Exception.class)
public class TcInvoiceServiceImpl implements TcInvoiceService {

    @Autowired(required = false)
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;

    @Autowired(required = false)
    private SysOrderDao orderDao;

    @Override
    public ReturnJsonData insert(TcInvoice tcInvoice) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcInvoice.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcInvoice.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcInvoice);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    /**
     * 开发票
     * @param tcInvoice
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData insertOrderInvoice(TcInvoice tcInvoice,Map<String, Object> map) throws Exception {
        String orderIds =  MapUtils.getString(map, "orderIds");
        String[] arrayOid =  orderIds.split(",");
        Map<String,Object> condition = new HashMap<>();
        //发票编号自动生成
        tcInvoice.setInvoiceNum(ToolUtil.getNumber(ConstantType.INVOICE_NUMBER_PREFIX));
        tcInvoice.setFile(""); //发票链接地址
        tcInvoice.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcInvoice.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcInvoice);
        int flago=0;
        SysOrder sysOrder =null;
        for (int i = 0; i <arrayOid.length ; i++) {
            Integer orderId=Integer.valueOf(arrayOid[i]);//订单Id
            //查询订单是否存在
            sysOrder = orderDao.getById(orderId);
            if(sysOrder==null) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的订单ID错误",null);
            }
            if(tcInvoice.getAmount()>sysOrder.getTotalPrice()) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"发票金额大于定单金额",null);
            }
            Map<String,Object> conditionsec = new HashMap<>();
            conditionsec.put("orderId",orderId);
            conditionsec.put("status",1);
            TcOrderInvoice retcOrderInvoice = mongoComDAO.executeForObjectByCon(conditionsec, TcOrderInvoice.class);
            if(retcOrderInvoice != null){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"该发票已申请",null);
            }
            TcOrderInvoice tcOrderInvoice=new TcOrderInvoice();
            tcOrderInvoice.setExtendId(tcInvoice.getExtendId());
            tcOrderInvoice.setOrderId(orderId);
            tcOrderInvoice.setInvoiceId(tcInvoice.getId());
            tcOrderInvoice.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            tcOrderInvoice.setStatus(1);
            flago+= mongoComDAO.executeInsert(tcOrderInvoice);
            tcOrderInvoice.setId(null);
        }
        if(flag > 0 && flago>0){
            //更新开发票状态：开发票中
            sysOrder.setInvoiceStatus(2);
            int i = orderDao.update(DBUtil.toUpdateParam(sysOrder, "id"));
            if(i>0) {
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
            }else {
                return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);

    }

    @Override
    public ReturnJsonData update(TcInvoice tcInvoice) throws Exception {
        TcInvoice returnTcInvoice = mongoComDAO.executeForObjectById(tcInvoice.getId(), TcInvoice.class);
        if(returnTcInvoice != null){
            returnTcInvoice.setExtendId(tcInvoice.getExtendId());
            returnTcInvoice.setTaxNum(tcInvoice.getTaxNum());
            returnTcInvoice.setTitle(tcInvoice.getTitle());
            returnTcInvoice.setEmail(tcInvoice.getEmail());
            returnTcInvoice.setContact(tcInvoice.getContact());
            returnTcInvoice.setProvince(tcInvoice.getProvince());
            returnTcInvoice.setCity(tcInvoice.getCity());
            returnTcInvoice.setCounty(tcInvoice.getCounty());
            returnTcInvoice.setAddress(tcInvoice.getAddress());
            returnTcInvoice.setPhone(tcInvoice.getPhone());
            returnTcInvoice.setInvoiceNum(tcInvoice.getInvoiceNum());
            int flag = mongoComDAO.executeUpdate(returnTcInvoice);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcInvoice.class);
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
        TcInvoice tcInvoice = mongoComDAO.executeForObjectById(id, TcInvoice.class);
        if (tcInvoice!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcInvoice.getId());
            condition.put("extendId",tcInvoice.getExtendId());
            ExtendUser extendUser = extendUserDao.getById(tcInvoice.getExtendId());
            if(extendUser != null){
                condition.put("extendPhone",extendUser.getPhone());
            }else {
                condition.put("extendPhone","");
            }
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
            condition.put("status",tcInvoice.getStatus());
            condition.put("createTime",tcInvoice.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String extendId =  MapUtils.getString(map, "extendId");
        String title =  MapUtils.getString(map, "title");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //所属类型
        if(StringUtils.isNotEmpty(extendId)){
            Criteria criteria = Criteria.where("extendId").is(Integer.valueOf(extendId));
            criteriasa.add(criteria);
        }
        //抬头模糊查询
        if(StringUtils.isNotEmpty(title)){
            Pattern pattern = Pattern.compile("^.*" + title + ".*$");
            Criteria criteria = Criteria.where("title").regex(pattern);
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcInvoice> tcInvoices = mongoComDAO.executeForObjectList(criteriasa,TcInvoice.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcInvoice.class);
        List<Map<String,Object>> tcInvoicelist = new ArrayList<>();
        for(TcInvoice tcInvoice : tcInvoices){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcInvoice.getId());
            condition.put("extendId",tcInvoice.getExtendId());
            ExtendUser extendUser = extendUserDao.getById(tcInvoice.getExtendId());
            if(extendUser != null){
                condition.put("extendPhone",extendUser.getPhone());
            }else {
                condition.put("extendPhone","");
            }
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
            condition.put("status",tcInvoice.getStatus());
            condition.put("createTime",tcInvoice.getCreateTime());
            tcInvoicelist.add(condition);
        }
        map.clear();
        map.put("list",tcInvoicelist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }



}
