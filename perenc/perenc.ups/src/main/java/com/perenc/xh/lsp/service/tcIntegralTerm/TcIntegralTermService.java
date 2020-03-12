package com.perenc.xh.lsp.service.tcIntegralTerm;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcIntegral.TcIntegralTerm;

import java.util.Map;


public interface TcIntegralTermService {

    /**
     * 新增
     * @param tcIntegralTerm
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcIntegralTerm tcIntegralTerm)throws Exception;

    /**
     * 修改
     * @param tcIntegralTerm
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcIntegralTerm tcIntegralTerm)throws Exception;

    /**
     * 修改启用状态
     * @param tcIntegralTerm
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateIsEnabled(TcIntegralTerm tcIntegralTerm) throws Exception;

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
