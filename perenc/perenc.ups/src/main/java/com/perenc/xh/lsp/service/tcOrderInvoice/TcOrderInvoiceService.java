package com.perenc.xh.lsp.service.tcOrderInvoice;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcOrder.TcOrderInvoice;

import java.util.Map;


public interface TcOrderInvoiceService {

    /**
     * 新增
     * @param tcOrderInvoice
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcOrderInvoice tcOrderInvoice)throws Exception;

    /**
     * 修改
     * @param tcOrderInvoice
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcOrderInvoice tcOrderInvoice)throws Exception;

    /**
     * 发票上传图片
     * @param tcOrderInvoice
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateFile(TcOrderInvoice tcOrderInvoice, Map<String, Object> map) throws Exception;

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
     * 订单发票明细
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getAllList(Map<String, Object> map) throws Exception;




}
