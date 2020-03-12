package com.perenc.xh.lsp.service.order;


import com.perenc.xh.commonUtils.model.JsReturnJsonData;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.data.CreateOrderData;
import com.perenc.xh.lsp.entity.order.SysOrder;

import java.util.List;
import java.util.Map;

/**
 * @Description 系统订单
 * @Author xiaobai
 * @Date 2019/5/22 11:14
 **/
public interface SysOrderService {

    /**
     * 创建订单
     * @param orderData
     * @return
     */
    ReturnJsonData createOrder(CreateOrderData orderData) throws Exception;

    /**
     * 确认订单
     * @param map
     * @return
     */
    ReturnJsonData confirmOrder(Map<String, Object> map) throws Exception;

    /**
     * 微信支付通知回调
     * @param map
     * @return
     * @throws Exception
     */
    ReturnJsonData notifyWechat(Map<String, Object> map) throws Exception;




    /**
     * 订单详情
     * @param orderNo
     * @return
     */
    SysOrder getOrderInfo(String orderNo) throws Exception;

    /**
     * 停车定单查询
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    public ReturnJsonData getTcList(Map<String, Object> map, PageHelper pageHelper) throws Exception;

    /**
     * 车辆
     * 缴费明细
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    public ReturnJsonData getTcListDetail(Map<String, Object> map, PageHelper pageHelper) throws Exception;

    /**
     * 车辆费用
     *  缴费明细查询所有
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getTcListAllDetail(Map<String, Object> map) throws Exception;

    /**
     * 车辆
     * 充值明细
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    public ReturnJsonData getTcListRechargeDetail(Map<String, Object> map, PageHelper pageHelper) throws Exception;

    /**
     * 车辆费用
     * 充值明细查询所有
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getTcListAllRechargeDetail(Map<String, Object> map) throws Exception;

    /**
     * 车辆
     * 购卡明细
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    public ReturnJsonData getTcListvipDetail(Map<String, Object> map, PageHelper pageHelper) throws Exception;

    /**
     * 车辆费用
     * 购卡明细 查询所有
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getTcListAllVipDetail(Map<String, Object> map) throws Exception;

    /**
     * 绑定车辆
     * 停车费用总统计：总支付笔数，总收益 ，查询条件，微信支付
     * @param sysOrder
     * @return
     * @throws Exception
     */
    public ReturnJsonData findTcOrderTotalData(SysOrder sysOrder, Map<String, Object> map) throws Exception;

    /**
     * 绑定车辆
     * 添加车辆同时，建立客户车辆关系
     * @param sysOrder
     * @return
     * @throws Exception
     */
    public ReturnJsonData findTcOrderStatistics(SysOrder sysOrder, Map<String, Object> map) throws Exception;

    /**
     * 停车场删除临时定单
     * 根据当前时间
     * @return
     */
    public Integer deleteTcTempOrderByEdate() throws Exception;

    /**
     * 停车定单
     * 支付结果反查
     * @param map
     * @return
     * @throws Exception
     */
    public JsReturnJsonData getOrderPaycheck(Map<String, Object> map) throws Exception;

    /**
     * 修改订单地址
     * @param map
     * @return
     */
    ReturnJsonData updateOrderAddressInfo(Map<String, Object> map) throws Exception;





    /**
     * 半个小时  删掉未支付的订单
     */
    void deleteOrder();

    /**
     * 客户端删除订单
     * @param map
     * @return
     */
    ReturnJsonData deleteOrderInfo(Map<String, Object> map);


}
