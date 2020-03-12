package com.perenc.xh.lsp.service.tcCostRule;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcParklot.TcCostRule;

import java.util.Map;


public interface TcCostRuleService {

    /**
     * 新增
     * @param tcCostRule
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcCostRule tcCostRule)throws Exception;

    /**
     * 修改
     * @param tcCostRule
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcCostRule tcCostRule)throws Exception;

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
