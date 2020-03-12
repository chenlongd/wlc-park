package com.perenc.xh.lsp.service.tcSellerApplycoupon;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplycData;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplycoupon;

import java.util.Map;


public interface TcSellerApplycouponService {

    /**
     * 新增
     * @param tcSellerApplycoupon
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcSellerApplycoupon tcSellerApplycoupon)throws Exception;

    /**
     * 新增
     * 申领票券
     * 申领票明细
     * @param tcSellerApplycData
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertSellerApply(TcSellerApplycData tcSellerApplycData)throws Exception;

    /**
     * 修改
     * @param tcSellerApplycoupon
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcSellerApplycoupon tcSellerApplycoupon)throws Exception;

    /**
     * 商家申领停车券审批
     * @param tcSellerApplycoupon
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateIsApproval(TcSellerApplycoupon tcSellerApplycoupon) throws Exception;

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

    /**
     * 查询停车券明细
     * 停车券总数 :总申请张数，总发放数，总剩余数
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getAllTotalNumber(Map<String, Object> map) throws Exception;

    /**
     * 查询停车券明细
     * 每种停车券申领数量统计
     * 停车券申领记录
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getApplyCoupon(Map<String, Object> map, PageHelper pageHelper) throws Exception;


    /**
     * 用户发放停车券
     * 查询停车券发放总数
     * 停车券发放记录
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getExtendCoupon(Map<String, Object> map, PageHelper pageHelper) throws Exception;

}
