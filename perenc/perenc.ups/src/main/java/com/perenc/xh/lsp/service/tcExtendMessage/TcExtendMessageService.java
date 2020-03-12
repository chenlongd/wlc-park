package com.perenc.xh.lsp.service.tcExtendMessage;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendMessage;

import java.util.Map;


public interface TcExtendMessageService {

    /**
     * 新增
     * @param tcExtendMessage
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcExtendMessage tcExtendMessage)throws Exception;

    /**
     * 修改
     * @param tcExtendMessage
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcExtendMessage tcExtendMessage)throws Exception;

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
