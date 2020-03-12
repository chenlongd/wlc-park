package com.perenc.xh.lsp.service.tcInvoice;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcInvoice.TcInvoice;

import java.util.Map;


public interface TcInvoiceService {

    /**
     * 新增
     * @param tcInvoice
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcInvoice tcInvoice)throws Exception;

    /**
     * 开发票
     * @param tcInvoice
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertOrderInvoice(TcInvoice tcInvoice, Map<String, Object> map) throws Exception;

    /**
     * 修改
     * @param tcInvoice
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcInvoice tcInvoice)throws Exception;

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
