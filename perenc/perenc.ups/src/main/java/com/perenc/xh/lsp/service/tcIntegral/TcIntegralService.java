package com.perenc.xh.lsp.service.tcIntegral;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegral;

import java.util.Map;


public interface TcIntegralService {

    /**
     * 新增
     * @param tcIntegral
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcIntegral tcIntegral)throws Exception;

    /**
     * 积分兑换
     * @param tcIntegral
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertConvert(TcIntegral tcIntegral, Map<String, Object> map) throws Exception;

    /**
     * 修改
     * @param tcIntegral
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcIntegral tcIntegral)throws Exception;

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
