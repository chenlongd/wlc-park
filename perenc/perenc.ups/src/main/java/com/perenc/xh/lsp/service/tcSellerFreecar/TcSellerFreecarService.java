package com.perenc.xh.lsp.service.tcSellerFreecar;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerFreecar;

import java.util.Map;


public interface TcSellerFreecarService {

    /**
     * 新增
     * @param tcSellerFreecar
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcSellerFreecar tcSellerFreecar)throws Exception;

    /**
     * 自营商家指定免费车辆
     * @param tcSellerFreecar
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertFreecar(TcSellerFreecar tcSellerFreecar, Map<String, Object> map) throws Exception;

    /**
     * 修改
     * @param tcSellerFreecar
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcSellerFreecar tcSellerFreecar)throws Exception;

    /**
     * 修改免费车
     * 开始时间-结束时间
     * @param tcSellerFreecar
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateTime(TcSellerFreecar tcSellerFreecar) throws Exception;


    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    public ReturnJsonData delete(String[] ids) throws Exception;

    /**
     * 删除免费车
     * @param id
     * @return
     * @throws Exception
     */
    public ReturnJsonData deleteFreecar(String id) throws Exception;

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
    public Integer updateBatchFreecarUseStatusByEdate() throws Exception;

}
