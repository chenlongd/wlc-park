package com.perenc.xh.lsp.service.tcCarPaycheck;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcCar.TcCarPaycheck;

import java.util.Map;


public interface TcCarPaycheckService {

    /**
     * 新增
     * @param tcCarPaycheck
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcCarPaycheck tcCarPaycheck)throws Exception;


    /**
     * 修改
     * @param tcCarPaycheck
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcCarPaycheck tcCarPaycheck)throws Exception;

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
     * 根据当前时间，日志30天前的日志删除
     */
    public Integer removeBatchTcCarPaycheckByEdate() throws Exception;

}
