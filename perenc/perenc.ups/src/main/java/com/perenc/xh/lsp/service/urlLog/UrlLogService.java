package com.perenc.xh.lsp.service.urlLog;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.urlLog.UrlLog;

import java.util.Map;


public interface UrlLogService {

    /**
     * 新增
     * @param urlLog
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(UrlLog urlLog)throws Exception;

    /**
     * 返回对象
     * @param urlLog
     * @return
     * @throws Exception
     */
    public UrlLog insertLog(UrlLog urlLog) throws Exception;


    /**
     * 修改
     * @param urlLog
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(UrlLog urlLog)throws Exception;

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


    /**
     * 根据当前时间
     * 删除数据
     * 根据当前时间，日志15天前的日志删除
     */
    public Integer removeBatchUrlLogByEdate() throws Exception;

}
