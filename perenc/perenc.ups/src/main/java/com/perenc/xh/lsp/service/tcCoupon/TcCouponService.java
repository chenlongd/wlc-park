package com.perenc.xh.lsp.service.tcCoupon;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcCoupon.TcCoupon;

import java.util.Map;


public interface TcCouponService {

    /**
     * 新增
     * @param tcCoupon
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcCoupon tcCoupon)throws Exception;

    /**
     * 修改
     * @param tcCoupon
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcCoupon tcCoupon)throws Exception;

    /**
     * 更改启用状态
     * @param tcCoupon
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateIsEnabled(TcCoupon tcCoupon) throws Exception;

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
