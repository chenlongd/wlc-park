package com.perenc.xh.lsp.service.tcMessage.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.tcMessage.TcMessage;
import com.perenc.xh.lsp.service.tcMessage.TcMessageService;
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


@Service("tcMessageService")
@Transactional(rollbackFor = Exception.class)
public class TcMessageServiceImpl implements TcMessageService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;

    @Override
    public ReturnJsonData insert(TcMessage tcMessage) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcMessage.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcMessage.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcMessage);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcMessage tcMessage) throws Exception {
        TcMessage returnTcMessage = mongoComDAO.executeForObjectById(tcMessage.getId(), TcMessage.class);
        if(returnTcMessage != null){
            returnTcMessage.setTitle(tcMessage.getTitle());
            returnTcMessage.setContent(tcMessage.getContent());
            returnTcMessage.setExtendId(tcMessage.getExtendId());
            returnTcMessage.setType(tcMessage.getType());
            int flag = mongoComDAO.executeUpdate(returnTcMessage);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcMessage.class);
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
        TcMessage tcMessage = mongoComDAO.executeForObjectById(id, TcMessage.class);
        if (tcMessage!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcMessage.getId());
            condition.put("title",tcMessage.getTitle());
            condition.put("content",tcMessage.getContent());
            condition.put("extendId",tcMessage.getExtendId());
            ExtendUser extendUser = extendUserDao.getById(tcMessage.getExtendId());
            if(extendUser != null){
                condition.put("extendPhone",extendUser.getPhone());
            }else {
                condition.put("extendPhone","");
            }
            condition.put("type",tcMessage.getType());
            condition.put("status",tcMessage.getStatus());
            condition.put("createTime",tcMessage.getCreateTime());
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
        List<TcMessage> tcMessages = mongoComDAO.executeForObjectList(criteriasa,TcMessage.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcMessage.class);
        List<Map<String,Object>> tcMessagelist = new ArrayList<>();
        for(TcMessage tcMessage : tcMessages){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcMessage.getId());
            condition.put("title",tcMessage.getTitle());
            condition.put("content",tcMessage.getContent());
            condition.put("extendId",tcMessage.getExtendId());
            ExtendUser extendUser = extendUserDao.getById(tcMessage.getExtendId());
            if(extendUser != null){
                condition.put("extendPhone",extendUser.getPhone());
            }else {
                condition.put("extendPhone","");
            }
            condition.put("type",tcMessage.getType());
            condition.put("status",tcMessage.getStatus());
            condition.put("createTime",tcMessage.getCreateTime());
            tcMessagelist.add(condition);
        }
        map.clear();
        map.put("list",tcMessagelist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }



}
