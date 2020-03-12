package com.perenc.xh.lsp.service.tcVip;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcVip.TcVip;

import java.util.Map;


public interface TcVipService {

    /**
     * 新增
     * @param tcVip
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcVip tcVip)throws Exception;

    /**
     * 修改
     * @param tcVip
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcVip tcVip)throws Exception;

    /**
     * 更改启用状态
     * @param tcVip
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateIsEnabled(TcVip tcVip) throws Exception;

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
     * 查询所有
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getAllList(Map<String, Object> map) throws Exception;




}
