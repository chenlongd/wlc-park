package com.perenc.xh.lsp.service.tcFeedback.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.dao.extendUser.ExtendUserDao;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.tcFeedback.TcFeedback;
import com.perenc.xh.lsp.service.tcFeedback.TcFeedbackService;
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


@Service("tcFeedbackService")
@Transactional(rollbackFor = Exception.class)
public class TcFeedbackServiceImpl implements TcFeedbackService {

    @Autowired
    private MongoComDAO mongoComDAO;

    @Autowired(required = false)
    private ExtendUserDao extendUserDao;

    @Override
    public ReturnJsonData insert(TcFeedback tcFeedback) throws Exception {
        tcFeedback.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
        tcFeedback.setStatus(1);
        int flag = mongoComDAO.executeInsert(tcFeedback);
        if(flag > 0){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加成功",null);
        } else{
            return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加失败",null);
        }
    }

    @Override
    public ReturnJsonData update(TcFeedback tcFeedback) throws Exception {
        TcFeedback returnTcFeedback = mongoComDAO.executeForObjectById(tcFeedback.getId(), TcFeedback.class);
        if(returnTcFeedback != null){
            returnTcFeedback.setExtendId(tcFeedback.getExtendId());
            returnTcFeedback.setType(tcFeedback.getType());
            returnTcFeedback.setContent(tcFeedback.getContent());
            returnTcFeedback.setContentImage(tcFeedback.getContentImage());
            returnTcFeedback.setReply(tcFeedback.getReply());
            returnTcFeedback.setReplyTime(tcFeedback.getReplyTime());
            int flag = mongoComDAO.executeUpdate(returnTcFeedback);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改成功",null);
            }
        }else{
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的ID错误",null);
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改失败",null);
    }

    /**
     * 意见反馈回复
     * @param tcFeedback
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateReply(TcFeedback tcFeedback) throws Exception {
        TcFeedback returnTcFeedback = mongoComDAO.executeForObjectById(tcFeedback.getId(), TcFeedback.class);
        if(returnTcFeedback != null){
            returnTcFeedback.setReply(tcFeedback.getReply());
            returnTcFeedback.setReplyTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = mongoComDAO.executeUpdate(returnTcFeedback);
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
        int flag= mongoComDAO.executeUpdateByIds(condition, TcFeedback.class);
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
        TcFeedback tcFeedback = mongoComDAO.executeForObjectById(id, TcFeedback.class);
        if (tcFeedback!=null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("id",tcFeedback.getId());
            condition.put("extendId",tcFeedback.getExtendId());
            ExtendUser extendUser = extendUserDao.getById(tcFeedback.getExtendId());
            if(extendUser != null){
                condition.put("extendPhone",extendUser.getPhone());
            }else {
                condition.put("extendPhone","");
            }
            condition.put("type",tcFeedback.getType());
            condition.put("content",tcFeedback.getContent());
            condition.put("contentImage",tcFeedback.getContentImage());
            condition.put("reply",tcFeedback.getReply());
            condition.put("replyTime",tcFeedback.getReplyTime());
            condition.put("status",tcFeedback.getStatus());
            condition.put("createTime",tcFeedback.getCreateTime());
            return new ReturnJsonData(DataCodeUtil.SUCCESS, "查询成功", condition);
        } else {
            return new ReturnJsonData(DataCodeUtil.SELECT_DATABASE, "查询失败", null);
        }
    }


    @Override
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception {
        String extendId =  MapUtils.getString(map, "extendId");
        String type =  MapUtils.getString(map, "type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"createTime"));
        List<Criteria> criteriasa = new ArrayList<Criteria>();
        //所属类型
        if(StringUtils.isNotEmpty(extendId)){
            Criteria criteria = Criteria.where("extendId").is(Integer.valueOf(extendId));
            criteriasa.add(criteria);
        }
        //意见反馈类型
        if(StringUtils.isNotEmpty(type)){
            Criteria criteria = Criteria.where("type").is(Integer.valueOf(type));
            criteriasa.add(criteria);
        }
        Criteria criteria = Criteria.where("status").is(1);
        criteriasa.add(criteria);
        List<TcFeedback> tcFeedbacks = mongoComDAO.executeForObjectList(criteriasa,TcFeedback.class,pageHelper,orders);
        long count = mongoComDAO.executeForObjectListCount(criteriasa, TcFeedback.class);
        List<Map<String,Object>> tcFeedbacklist = new ArrayList<>();
        for(TcFeedback tcFeedback : tcFeedbacks){
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",tcFeedback.getId());
            condition.put("extendId",tcFeedback.getExtendId());
            ExtendUser extendUser = extendUserDao.getById(tcFeedback.getExtendId());
            if(extendUser != null){
                condition.put("extendPhone",extendUser.getPhone());
            }else {
                condition.put("extendPhone","");
            }
            condition.put("type",tcFeedback.getType());
            condition.put("content",tcFeedback.getContent());
            condition.put("contentImage",tcFeedback.getContentImage());
            condition.put("reply",tcFeedback.getReply());
            condition.put("replyTime",tcFeedback.getReplyTime());
            condition.put("status",tcFeedback.getStatus());
            condition.put("createTime",tcFeedback.getCreateTime());
            tcFeedbacklist.add(condition);
        }
        map.clear();
        map.put("list",tcFeedbacklist);//返回前端集合命名为list
        map.put("total",count);//总条数
        map.put("current",pageHelper.getPage());//当前页数
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",map);
    }



}
