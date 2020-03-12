package com.perenc.xh.lsp.service.tcExtendCoupon;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCoupon;

import java.util.Map;


public interface TcExtendCouponService {

    /**
     * 新增
     * @param tcExtendCoupon
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcExtendCoupon tcExtendCoupon)throws Exception;

    /**
     * 添加多张停车券
     * @param tcExtendCoupon
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertBatch(TcExtendCoupon tcExtendCoupon, Map<String, Object> map) throws Exception;

    /**
     * 添加会议，宴会停车券
     * @param tcExtendCoupon
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertMeeting(TcExtendCoupon tcExtendCoupon, Map<String, Object> map) throws Exception;

    /**
     * 用户第一次注册
     * 首次发绑定车辆
     * 给车辆发券2小时的停车券
     * @param tcExtendCoupon
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertFirst(TcExtendCoupon tcExtendCoupon, Map<String, Object> map) throws Exception;
    /**
     * 修改
     * @param tcExtendCoupon
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcExtendCoupon tcExtendCoupon)throws Exception;

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    public ReturnJsonData delete(String[] ids) throws Exception;

    /**
     * 根据extendId查询
     * 首次发放停车券判断
     * @param extendId
     * @return
     */
    public ReturnJsonData getIsFirst(String extendId) throws Exception;

    /**
     * 轮询查询发券
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getPolling(Map<String, Object> map) throws Exception;

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
     * 票券状态已过期处理
     * 根据当前时间，把票券状态更改
     * @param
     * @return
     */
    public Integer updateBatchCouponUseStatusByEdate() throws Exception;
}
