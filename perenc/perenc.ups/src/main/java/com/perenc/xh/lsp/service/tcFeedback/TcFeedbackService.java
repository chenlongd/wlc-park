package com.perenc.xh.lsp.service.tcFeedback;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcFeedback.TcFeedback;

import java.util.Map;


public interface TcFeedbackService {

    /**
     * 新增
     * @param feedback
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcFeedback feedback)throws Exception;

    /**
     * 修改
     * @param feedback
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcFeedback feedback)throws Exception;

    /**
     * 意见反馈回复
     * @param tcFeedback
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateReply(TcFeedback tcFeedback) throws Exception;

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    public ReturnJsonData delete(String[] ids) throws Exception;

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public ReturnJsonData getById(String id) throws Exception;


    /**
     * 获取列表
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception;




}
