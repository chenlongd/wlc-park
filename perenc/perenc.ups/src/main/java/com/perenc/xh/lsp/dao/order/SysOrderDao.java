package com.perenc.xh.lsp.dao.order;

import com.perenc.xh.commonUtils.dao.BaseDao;
import com.perenc.xh.lsp.entity.order.SysOrder;

import java.util.List;
import java.util.Map;

/**
 * @Description 系统订单
 * @Author xiaobai
 * @Date 2019/5/22 11:29
 **/
public interface SysOrderDao extends BaseDao<SysOrder> {

    public List<String> getStoreList(Map<String, Object> param);

    public List<String> getOrderNoList(Map<String, Object> param);

    public List<String> countOrderData(Map<String, Object> map);

    public List<String> countOrderDataInfo(Map<String, Object> map);

    /**
     * 停车费用总统计：总支付笔数，总收益 ，查询条件，微信支付
     * @param map
     * @return
     */
    public Map findTcOrderTotalData(Map<String, Object> map);

    /**
     * 停车费用统计接口
     * @param map
     * @return
     */
    public Map findTcOrderStatistics(Map<String, Object> map);


    /**
     * 商家免费车费用统计接口
     * @param map
     * @return
     */
    public Map findTcOrderStatBySellerId(Map<String, Object> map);

    /**
     * 删除停车临时定单
     * 根据当前时间
     * @param map
     * @return
     */
    public int deleteTcTempOrderByEdate(Map map);


}
