package com.perenc.xh.lsp.service.tcVip.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.ConstantType;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.entity.tcCar.TcCar;
import com.perenc.xh.lsp.entity.tcVip.TcVip;
import com.perenc.xh.lsp.service.tcVip.TcVipService;
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


@Service("tcVipService")
@Transactional(rollbackFor = Exception.class)
public class TcVipServiceImpl implements TcVipService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Override
    public ReturnJsonData insert(TcVip tcVip) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        //判断类型:VIP车类型（1:月卡车,2:季卡车，3:半年卡车，4:年卡车）
        if(tcVip.getType().equals(ConstantType.VIP_TYPE_ONE)) {
            tcVip.setNumber(ConstantType.VIP_MONTH_ONE);
        }else if(tcVip.getType().equals(ConstantType.VIP_TYPE_THREE)) {
            tcVip.setNumber(ConstantType.VIP_MONTH_THREE);
        }else if(tcVip.getType().equals(ConstantType.VIP_TYPE_HALFAYEAR)) {
            tcVip.setNumber(ConstantType.VIP_MONTH_HALFAYEAR);
        }else if(tcVip.getType().equals(ConstantType.VIP_TYPE_AYEAR)) {
            tcVip.setNumber(ConstantType.VIP_MONTH_AYEAR);
        }
        tcVip.setIsEnabled(1);
        tcVip.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcVip.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcVip);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcVip tcVip) throws Exception {
        TcVip returnTcVip = mongoComDAO.executeForObjectById(tcVip.getId(), TcVip.class);
        if(returnTcVip != null){
            returnTcVip.setName(tcVip.getName());
            returnTcVip.setType(tcVip.getType());
            //判断类型:VIP车类型（1:月卡车,2:季卡车，3:半年卡车，4:年卡）
            if(tcVip.getType().equals(ConstantType.VIP_TYPE_ONE)) {
                tcVip.setNumber(ConstantType.VIP_MONTH_ONE);
            }else if(tcVip.getType().equals(ConstantType.VIP_TYPE_THREE)) {
                tcVip.setNumber(ConstantType.VIP_MONTH_THREE);
            }else if(tcVip.getType().equals(ConstantType.VIP_TYPE_HALFAYEAR)) {
                tcVip.setNumber(ConstantType.VIP_MONTH_HALFAYEAR);
            }else if(tcVip.getType().equals(ConstantType.VIP_TYPE_AYEAR)) {
                tcVip.setNumber(ConstantType.VIP_MONTH_AYEAR);
            }
            returnTcVip.setCostPrice(tcVip.getCostPrice());
            returnTcVip.setDiscountPrice(tcVip.getDiscountPrice());
            returnTcVip.setImage(tcVip.getImage());
            int flag = mongoComDAO.executeUpdate(returnTcVip);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 更改启用状态
     * @param tcVip
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateIsEnabled(TcVip tcVip) throws Exception {
        TcVip returnTcVip = mongoComDAO.executeForObjectById(tcVip.getId(), TcVip.class);
        if(returnTcVip != null){
            returnTcVip.setIsEnabled(tcVip.getIsEnabled());
            int flag = mongoComDAO.executeUpdate(returnTcVip);
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
        //判断是否存在子表
        for (int i = 0; i <ids.length ; i++) {
            Map<String,Object> conditioncr = new HashMap<>();
            conditioncr.put("vipId",ids[i]);
            conditioncr.put("status",1);
            TcCar tcCar = mongoComDAO.executeForObjectByCon(conditioncr, TcCar.class);
            if(tcCar != null){
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"车辆已绑定无法删除",null);
            }
        }
         Map<String,Object> condition=new HashMap<>();
         condition.put("ids",ids);
         condition.put("status",2);
        int flag= mongoComDAO.executeUpdateByIds(condition, TcVip.class);
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
        TcVip tcVip = mongoComDAO.executeForObjectById(id, TcVip.class);
        if (tcVip!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcVip.getId());
            condition.put("name",tcVip.getName());
            condition.put("type",tcVip.getType());
            condition.put("number",tcVip.getNumber());
            condition.put("costPrice", ToolUtil.getIntToDouble(tcVip.getCostPrice()));
            condition.put("discountPrice",ToolUtil.getIntToDouble(tcVip.getDiscountPrice()));
            condition.put("image",tcVip.getImage());
            condition.put("isEnabled",tcVip.getIsEnabled());
            condition.put("status",tcVip.getStatus());
            condition.put("createTime",tcVip.getCreateTime());
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
        //VIP名模糊查询
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
        List<TcVip> tcVips = mongoComDAO.executeForObjectList(criteriasa,TcVip.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcVip.class);
        List<Map<String,Object>> tcCarlist = new ArrayList<>();
        for(TcVip tcVip : tcVips){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcVip.getId());
            condition.put("name",tcVip.getName());
            condition.put("type",tcVip.getType());
            condition.put("number",tcVip.getNumber());
            condition.put("costPrice", ToolUtil.getIntToDouble(tcVip.getCostPrice()));
            condition.put("discountPrice",ToolUtil.getIntToDouble(tcVip.getDiscountPrice()));
            condition.put("image",tcVip.getImage());
            condition.put("isEnabled",tcVip.getIsEnabled());
            condition.put("status",tcVip.getStatus());
            condition.put("createTime",tcVip.getCreateTime());
            tcCarlist.add(condition);
        }
        map.clear();
        map.put("list",tcCarlist);//返回前端集合命名为list
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
        String name =  MapUtils.getString(map, "name");
        //String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //VIP名模糊查询
        if(StringUtils.isNotEmpty(name)){
            Pattern pattern = Pattern.compile("^.*" + name + ".*$");
            Criteria criteria = Criteria.where("name").regex(pattern);
            criteriasa.add(criteria);
        }
        //前端查询为启用状态
        Criteria criteria = Criteria.where("isEnabled").is(1);
        criteriasa.add(criteria);
        Criteria criterib = Criteria.where("status").is(1);
        criteriasa.add(criterib);
        List<TcVip> tcVips = mongoComDAO.executeForObjectList(criteriasa,TcVip.class,orders);
        List<Map<String,Object>> tcCarlist = new ArrayList<>();
        for(TcVip tcVip : tcVips){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcVip.getId());
            condition.put("name",tcVip.getName());
            condition.put("type",tcVip.getType());
            condition.put("number",tcVip.getNumber());
            condition.put("costPrice", ToolUtil.getIntToDouble(tcVip.getCostPrice()));
            condition.put("discountPrice",ToolUtil.getIntToDouble(tcVip.getDiscountPrice()));
            condition.put("image",tcVip.getImage());
            condition.put("isEnabled",tcVip.getIsEnabled());
            condition.put("status",tcVip.getStatus());
            condition.put("createTime",tcVip.getCreateTime());
            tcCarlist.add(condition);
        }
        map.clear();
        map.put("list",tcCarlist);//返回前端集合命名为list
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }



}
