package com.perenc.xh.lsp.service.tcParklot;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcParklot.TcParklot;

import java.util.Map;


public interface TcParklotService {

    /**
     * 新增
     * @param tcParklot
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcParklot tcParklot)throws Exception;

    /**
     * 修改
     * @param tcParklot
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcParklot tcParklot)throws Exception;

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
     * 获取轮播图列表
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception;




}
