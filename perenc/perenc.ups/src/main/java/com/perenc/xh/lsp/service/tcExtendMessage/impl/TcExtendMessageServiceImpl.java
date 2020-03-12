package com.perenc.xh.lsp.service.tcExtendMessage.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendMessage;
import com.perenc.xh.lsp.entity.tcMessage.TcMessage;
import com.perenc.xh.lsp.service.tcExtendMessage.TcExtendMessageService;
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


@Service("tcExtendMessageService")
@Transactional(rollbackFor = Exception.class)
public class TcExtendMessageServiceImpl implements TcExtendMessageService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;

    @Override
    public ReturnJsonData insert(TcExtendMessage tcExtendMessage) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        tcExtendMessage.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcExtendMessage.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcExtendMessage);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcExtendMessage tcExtendMessage) throws Exception {
        TcExtendMessage returnTcExtendMessage = mongoComDAO.executeForObjectById(tcExtendMessage.getId(), TcExtendMessage.class);
        if(returnTcExtendMessage != null){
            returnTcExtendMessage.setMessageId(tcExtendMessage.getMessageId());
            returnTcExtendMessage.setSenderId(tcExtendMessage.getSenderId());
            returnTcExtendMessage.setReceiverId(tcExtendMessage.getReceiverId());
            returnTcExtendMessage.setType(tcExtendMessage.getType());
            int flag = mongoComDAO.executeUpdate(returnTcExtendMessage);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcExtendMessage.class);
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
        TcExtendMessage tcExtendMessage = mongoComDAO.executeForObjectById(id, TcExtendMessage.class);
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        if (tcExtendMessage!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcExtendMessage.getId());
            condition.put("messageId",tcExtendMessage.getMessageId());
            TcMessage tcMessage = mongoComDAO.executeForObjectById(tcExtendMessage.getMessageId(), TcMessage.class);
            if(tcMessage != null){
                condition.put("title",tcMessage.getTitle());
                condition.put("content",tcMessage.getContent());
            }
            condition.put("senderId",tcExtendMessage.getSenderId());
            ExtendUser senderExtendUser = extendUserDao.getById(tcExtendMessage.getSenderId());
            if(senderExtendUser != null){
                condition.put("senderExtendPhone",senderExtendUser.getPhone());
            }else {
                condition.put("senderExtendPhone","");
            }
            condition.put("receiverId",tcExtendMessage.getReceiverId());
            ExtendUser receiverExtendUser = extendUserDao.getById(tcExtendMessage.getSenderId());
            if(receiverExtendUser != null){
                condition.put("receiverExtendPhone",receiverExtendUser.getPhone());
            }else {
                condition.put("receiverExtendPhone","");
            }
            condition.put("type",tcExtendMessage.getType());
            condition.put("status",tcExtendMessage.getStatus());
            condition.put("createTime",tcExtendMessage.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String senderId =  MapUtils.getString(map, "senderId");
        String receiverId =  MapUtils.getString(map, "receiverId");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //发送ID
        if(StringUtils.isNotEmpty(senderId)){
            Criteria criteria = Criteria.where("senderId").is(Integer.valueOf(senderId));
            criteriasa.add(criteria);
        }
        //接收ID
        if(StringUtils.isNotEmpty(receiverId)){
            Criteria criteria = Criteria.where("receiverId").is(Integer.valueOf(receiverId));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcExtendMessage> tcExtendMessages = mongoComDAO.executeForObjectList(criteriasa,TcExtendMessage.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcExtendMessage.class);
        List<Map<String,Object>> tcExtendMessagelist = new ArrayList<>();
        for(TcExtendMessage tcExtendMessage : tcExtendMessages){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcExtendMessage.getId());
            condition.put("messageId",tcExtendMessage.getMessageId());
            TcMessage tcMessage = mongoComDAO.executeForObjectById(tcExtendMessage.getMessageId(), TcMessage.class);
            if(tcMessage != null){
                condition.put("title",tcMessage.getTitle());
                condition.put("content",tcMessage.getContent());
            }
            condition.put("senderId",tcExtendMessage.getSenderId());
            ExtendUser senderExtendUser = extendUserDao.getById(tcExtendMessage.getSenderId());
            if(senderExtendUser != null){
                condition.put("senderExtendPhone",senderExtendUser.getPhone());
            }else {
                condition.put("senderExtendPhone","");
            }
            condition.put("receiverId",tcExtendMessage.getReceiverId());
            ExtendUser receiverExtendUser = extendUserDao.getById(tcExtendMessage.getSenderId());
            if(receiverExtendUser != null){
                condition.put("receiverExtendPhone",receiverExtendUser.getPhone());
            }else {
                condition.put("receiverExtendPhone","");
            }
            condition.put("type",tcExtendMessage.getType());
            condition.put("status",tcExtendMessage.getStatus());
            condition.put("createTime",tcExtendMessage.getCreateTime());
            tcExtendMessagelist.add(condition);
        }
        map.clear();
        map.put("list",tcExtendMessagelist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }




}
