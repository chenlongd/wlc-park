package com.perenc.xh.lsp.service.tcIntegralConvertrle;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegralConvertrule;

import java.util.Map;


public interface TcIntegralConvertruleService {

    /**
     * 新增
     * @param tcIntegralConvertrule
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcIntegralConvertrule tcIntegralConvertrule)throws Exception;

    /**
     * 修改
     * @param tcIntegralConvertrule
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcIntegralConvertrule tcIntegralConvertrule)throws Exception;

    /**
     * 修改启用状态
     * @param tiConvertrule
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateIsEnabled(TcIntegralConvertrule tiConvertrule) throws Exception;

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
