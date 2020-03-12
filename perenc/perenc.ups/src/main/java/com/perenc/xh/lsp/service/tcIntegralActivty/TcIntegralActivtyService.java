package com.perenc.xh.lsp.service.tcIntegralActivty;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegralActivty;

import java.util.Map;


public interface TcIntegralActivtyService {

    /**
     * 新增
     * @param tcIntegralActivty
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcIntegralActivty tcIntegralActivty)throws Exception;

    /**
     * 修改
     * @param tcIntegralActivty
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcIntegralActivty tcIntegralActivty)throws Exception;

    /**
     * 修改启用状态
     * @param tcIntegralActivty
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateIsEnabled(TcIntegralActivty tcIntegralActivty) throws Exception;

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
