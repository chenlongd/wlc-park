package com.perenc.xh.lsp.service.tcCarJsclientlog;

import com.alibaba.fastjson.JSONObject;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcCar.TcCarJsclientlog;

import java.util.Map;


public interface TcCarJsclientlogService {

    /**
     * 新增
     * @param tcCarJsclientlog
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcCarJsclientlog tcCarJsclientlog)throws Exception;


    /**
     * 增加捷顺访问日志
     * @param url
     * @param rparameter
     * @param jsonResut
     * @return
     * @throws Exception
     */
    public Integer insertJsClientLog(String url, String rparameter, JSONObject jsonResut) throws Exception;


    /**
     * 修改
     * @param tcCarJsclientlog
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcCarJsclientlog tcCarJsclientlog)throws Exception;

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
     * 根据当前时间，日志60天前的日志删除
     */
    public Integer removeBatchTcCarJsclientlogByEdate() throws Exception;


}
