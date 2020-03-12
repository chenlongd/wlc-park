package com.perenc.xh.lsp.service.tcVipCar;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcVip.TcVipCar;

import java.util.Map;


public interface TcVipCarService {

    /**
     * 新增
     * @param tcVipCar
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcVipCar tcVipCar)throws Exception;

    /**
     * 车辆开通vip
     * Vip充值
     * @param tcVipCar
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertCarVip(TcVipCar tcVipCar, Map<String, Object> map) throws Exception;


    /**
     * 车辆开通vip
     * 点击开始使用vip
     * Vip充值
     * @param tcVipCar
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateCarVip(TcVipCar tcVipCar, Map<String, Object> map) throws Exception;

    /**
     * 修改
     * @param tcVipCar
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcVipCar tcVipCar)throws Exception;

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
