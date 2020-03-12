package com.perenc.xh.lsp.service.tcInvoiceTemplate.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.tcInvoice.TcInvoiceTemplate;
import com.perenc.xh.lsp.service.tcInvoiceTemplate.TcInvoiceTemplateService;
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


@Service("tcInvoiceTemplateService")
@Transactional(rollbackFor = Exception.class)
public class TcInvoiceTemplateServiceImpl implements TcInvoiceTemplateService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;

    @Override
    public ReturnJsonData insert(TcInvoiceTemplate tcInvoiceTemplate) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcInvoiceTemplate.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcInvoiceTemplate.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcInvoiceTemplate);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcInvoiceTemplate tcInvoiceTemplate) throws Exception {
        TcInvoiceTemplate returnTiTemplate = mongoComDAO.executeForObjectById(tcInvoiceTemplate.getId(), TcInvoiceTemplate.class);
        if(returnTiTemplate != null){
            returnTiTemplate.setTaxNum(tcInvoiceTemplate.getTaxNum());
            returnTiTemplate.setTitle(tcInvoiceTemplate.getTitle());
            returnTiTemplate.setEmail(tcInvoiceTemplate.getEmail());
            returnTiTemplate.setContact(tcInvoiceTemplate.getContact());
            returnTiTemplate.setProvince(tcInvoiceTemplate.getProvince());
            returnTiTemplate.setCity(tcInvoiceTemplate.getCity());
            returnTiTemplate.setCounty(tcInvoiceTemplate.getCounty());
            returnTiTemplate.setAddress(tcInvoiceTemplate.getAddress());
            returnTiTemplate.setPhone(tcInvoiceTemplate.getPhone());
            int flag = mongoComDAO.executeUpdate(returnTiTemplate);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 设置默认模板
     * @param tcInvoiceTemplate
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateIsDefault(TcInvoiceTemplate tcInvoiceTemplate) throws Exception {
        //查询用户所有的默认
        Map<String,Object> conditiona=new HashMap<>();
        conditiona.put("ids",tcInvoiceTemplate.getExtendId());
        conditiona.put("status",1);
        Map<String,Object> conditionb=new HashMap<>();
        conditionb.put("isDefault",2); //是否默认设置为2：否
        int flagup = mongoComDAO.executeUpdate(conditiona,conditionb,TcInvoiceTemplate.class);
        TcInvoiceTemplate returnTiTemplate = mongoComDAO.executeForObjectById(tcInvoiceTemplate.getId(), TcInvoiceTemplate.class);
        if(returnTiTemplate != null){
            returnTiTemplate.setTaxNum(tcInvoiceTemplate.getTaxNum());
            returnTiTemplate.setIsDefault(1);//是否默认设置为是
            int flag = mongoComDAO.executeUpdate(returnTiTemplate);
            if(flagup>0 && flag > 0){
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcInvoiceTemplate.class);
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
        TcInvoiceTemplate tiTemplate = mongoComDAO.executeForObjectById(id, TcInvoiceTemplate.class);
        if (tiTemplate!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tiTemplate.getId());
            condition.put("extendId",tiTemplate.getExtendId());
            ExtendUser extendUser = extendUserDao.getById(tiTemplate.getExtendId());
            if(extendUser != null){
                condition.put("extendPhone",extendUser.getPhone());
            }else {
                condition.put("extendPhone","");
            }
            condition.put("type",tiTemplate.getType());
            condition.put("title",tiTemplate.getTitle());
            condition.put("idNumber",tiTemplate.getIdNumber());
            condition.put("email",tiTemplate.getEmail());
            condition.put("contact",tiTemplate.getContact());
            condition.put("province",tiTemplate.getProvince());
            condition.put("city",tiTemplate.getCity());
            condition.put("county",tiTemplate.getCounty());
            condition.put("address",tiTemplate.getTitle());
            condition.put("bankName",tiTemplate.getBankName());
            condition.put("bankNumber",tiTemplate.getBankNumber());
            condition.put("phone",tiTemplate.getPhone());
            condition.put("isDefault",tiTemplate.getIsDefault());
            condition.put("remark",tiTemplate.getRemark());
            condition.put("status",tiTemplate.getStatus());
            condition.put("createTime",tiTemplate.getCreateTime());
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
        List<TcInvoiceTemplate> tcInvoiceTemplates = mongoComDAO.executeForObjectList(criteriasa,TcInvoiceTemplate.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcInvoiceTemplate.class);
        List<Map<String,Object>> tcInvoicelist = new ArrayList<>();
        for(TcInvoiceTemplate tiTemplate : tcInvoiceTemplates){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tiTemplate.getId());
            ExtendUser extendUser = extendUserDao.getById(tiTemplate.getExtendId());
            if(extendUser != null){
                condition.put("extendPhone",extendUser.getPhone());
            }else {
                condition.put("extendPhone","");
            }
            condition.put("type",tiTemplate.getType());
            condition.put("title",tiTemplate.getTitle());
            condition.put("idNumber",tiTemplate.getIdNumber());
            condition.put("email",tiTemplate.getEmail());
            condition.put("contact",tiTemplate.getContact());
            condition.put("province",tiTemplate.getProvince());
            condition.put("city",tiTemplate.getCity());
            condition.put("county",tiTemplate.getCounty());
            condition.put("address",tiTemplate.getTitle());
            condition.put("bankName",tiTemplate.getBankName());
            condition.put("bankNumber",tiTemplate.getBankNumber());
            condition.put("phone",tiTemplate.getPhone());
            condition.put("isDefault",tiTemplate.getIsDefault());
            condition.put("remark",tiTemplate.getRemark());
            condition.put("status",tiTemplate.getStatus());
            condition.put("createTime",tiTemplate.getCreateTime());
            tcInvoicelist.add(condition);
        }
        map.clear();
        map.put("list",tcInvoicelist);//返回前端集合命名为list
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
        List<TcInvoiceTemplate> tcInvoiceTemplates = mongoComDAO.executeForObjectList(criteriasa,TcInvoiceTemplate.class,orders);
        List<Map<String,Object>> tcInvoicelist = new ArrayList<>();
        for(TcInvoiceTemplate tiTemplate : tcInvoiceTemplates){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tiTemplate.getId());
            ExtendUser extendUser = extendUserDao.getById(tiTemplate.getExtendId());
            if(extendUser != null){
                condition.put("extendPhone",extendUser.getPhone());
            }else {
                condition.put("extendPhone","");
            }
            condition.put("type",tiTemplate.getType());
            condition.put("title",tiTemplate.getTitle());
            condition.put("idNumber",tiTemplate.getIdNumber());
            condition.put("email",tiTemplate.getEmail());
            condition.put("contact",tiTemplate.getContact());
            condition.put("province",tiTemplate.getProvince());
            condition.put("city",tiTemplate.getCity());
            condition.put("county",tiTemplate.getCounty());
            condition.put("address",tiTemplate.getTitle());
            condition.put("bankName",tiTemplate.getBankName());
            condition.put("bankNumber",tiTemplate.getBankNumber());
            condition.put("phone",tiTemplate.getPhone());
            condition.put("isDefault",tiTemplate.getIsDefault());
            condition.put("remark",tiTemplate.getRemark());
            condition.put("status",tiTemplate.getStatus());
            condition.put("createTime",tiTemplate.getCreateTime());
            tcInvoicelist.add(condition);
        }
        map.clear();
        map.put("list",tcInvoicelist);//返回前端集合命名为list
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }


}
