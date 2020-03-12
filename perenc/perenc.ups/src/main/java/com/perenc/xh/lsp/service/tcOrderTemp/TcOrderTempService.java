package com.perenc.xh.lsp.service.tcOrderTemp;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcOrder.TcOrderTemp;

import java.util.List;
import java.util.Map;


public interface TcOrderTempService {

    /**
     * 新增
     * @param tcOrderTemp
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcOrderTemp tcOrderTemp)throws Exception;

    /**
     * 修改
     * @param tcOrderTemp
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcOrderTemp tcOrderTemp)throws Exception;

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    public ReturnJsonData delete(String[] ids) throws Exception;

    /**
     * 批量修改状态删除
     * @param list
     * @return
     * @throws Exception
     */
    public ReturnJsonData deleteBatch(List list) throws Exception;

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public ReturnJsonData getById(Integer id) throws Exception;


    /**
     * 获取列表
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception;

    /**
     * 用户充值
     * 支付生成定单
     * @param map
     * @param tcOrderTemp
     * @return
     * @throws Exception
     */
    public ReturnJsonData payCheck(Map<String, Object> map, TcOrderTemp tcOrderTemp) throws Exception;

    /**
     * 查询车辆停车缴费信息
     * 创建临时定单
     * @param map
     * @param tcOrderTemp
     * @return
     * @throws Exception
     */
    public ReturnJsonData addOrderTempPay(Map<String, Object> map, TcOrderTemp tcOrderTemp) throws Exception;




}
