package com.perenc.xh.lsp.service.tcImchat.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.tcImchat.TcImchat;
import com.perenc.xh.lsp.service.tcImchat.TcImchatService;
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


@Service("tcImchatService")
@Transactional(rollbackFor = Exception.class)
public class TcImchatServiceImpl implements TcImchatService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;

    @Override
    public ReturnJsonData insert(TcImchat tcImchat) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcImchat.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcImchat.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcImchat);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcImchat tcImchat) throws Exception {
        TcImchat returnTcImchat = mongoComDAO.executeForObjectById(tcImchat.getId(), TcImchat.class);
        if(returnTcImchat != null){
            returnTcImchat.setSendExtendId(tcImchat.getSendExtendId());
            returnTcImchat.setReceiveExtendId(tcImchat.getReceiveExtendId());
            returnTcImchat.setContent(tcImchat.getContent());
            int flag = mongoComDAO.executeUpdate(returnTcImchat);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcImchat.class);
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
        TcImchat tcImchat = mongoComDAO.executeForObjectById(id, TcImchat.class);
        if (tcImchat!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcImchat.getId());
            condition.put("sendExtendId",tcImchat.getSendExtendId());
            ExtendUser sendExtendUser = extendUserDao.getById(tcImchat.getSendExtendId());
            if(sendExtendUser != null){
                condition.put("sendExtendPhone",sendExtendUser.getPhone());
            }else {
                condition.put("sendExtendPhone","");
            }
            condition.put("receiveExtendId",tcImchat.getReceiveExtendId());
            ExtendUser receiveExtendUser = extendUserDao.getById(tcImchat.getReceiveExtendId());
            if(receiveExtendUser != null){
                condition.put("receiveExtendPhone",receiveExtendUser.getPhone());
            }else {
                condition.put("receiveExtendPhone","");
            }
            condition.put("content",tcImchat.getContent());
            condition.put("status",tcImchat.getStatus());
            condition.put("createTime",tcImchat.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String sendExtendId =  MapUtils.getString(map, "sendExtendId");
        String receiveExtendId =  MapUtils.getString(map, "receiveExtendId");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //发送人
        if(StringUtils.isNotEmpty(sendExtendId)){
            Criteria criteria = Criteria.where("sendExtendId").is(Integer.valueOf(sendExtendId));
            criteriasa.add(criteria);
        }
        //接收人
        if(StringUtils.isNotEmpty(receiveExtendId)){
            Criteria criteria = Criteria.where("receiveExtendId").is(Integer.valueOf(receiveExtendId));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcImchat> tcImchats = mongoComDAO.executeForObjectList(criteriasa,TcImchat.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcImchat.class);
        List<Map<String,Object>> tcImchatlist = new ArrayList<>();
        for(TcImchat tcImchat : tcImchats){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcImchat.getId());
            condition.put("sendExtendId",tcImchat.getSendExtendId());
            ExtendUser sendExtendUser = extendUserDao.getById(tcImchat.getSendExtendId());
            if(sendExtendUser != null){
                condition.put("sendExtendPhone",sendExtendUser.getPhone());
            }else {
                condition.put("sendExtendPhone","");
            }
            condition.put("receiveExtendId",tcImchat.getReceiveExtendId());
            ExtendUser receiveExtendUser = extendUserDao.getById(tcImchat.getReceiveExtendId());
            if(receiveExtendUser != null){
                condition.put("receiveExtendPhone",receiveExtendUser.getPhone());
            }else {
                condition.put("receiveExtendPhone","");
            }
            condition.put("content",tcImchat.getContent());
            condition.put("status",tcImchat.getStatus());
            condition.put("createTime",tcImchat.getCreateTime());
            tcImchatlist.add(condition);
        }
        map.clear();
        map.put("list",tcImchatlist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }

}
