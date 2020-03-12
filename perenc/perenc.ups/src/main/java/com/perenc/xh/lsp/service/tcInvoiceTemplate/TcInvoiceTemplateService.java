package com.perenc.xh.lsp.service.tcInvoiceTemplate;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcInvoice.TcInvoiceTemplate;

import java.util.Map;


public interface TcInvoiceTemplateService {

    /**
     * 新增
     * @param tcInvoiceTemplate
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcInvoiceTemplate tcInvoiceTemplate)throws Exception;


    /**
     * 修改
     * @param tcInvoiceTemplate
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcInvoiceTemplate tcInvoiceTemplate)throws Exception;

    /**
     * 设置默认模板
     * @param tcInvoiceTemplate
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateIsDefault(TcInvoiceTemplate tcInvoiceTemplate) throws Exception;

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
