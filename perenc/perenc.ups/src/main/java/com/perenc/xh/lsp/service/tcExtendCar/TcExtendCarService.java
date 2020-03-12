package com.perenc.xh.lsp.service.tcExtendCar;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendCar;

import java.util.Map;


public interface TcExtendCarService {

    /**
     * 新增
     * @param tcExtendCar
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcExtendCar tcExtendCar)throws Exception;

    /**
     * 修改
     * @param tcExtendCar
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcExtendCar tcExtendCar)throws Exception;

    /**
     * 解绑
     * @param tcExtendCar
     * @return
     * @throws Exception
     */
    public ReturnJsonData deleteCar(TcExtendCar tcExtendCar) throws Exception;

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
     * 查询我的车辆
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getMyList(Map<String, Object> map) throws Exception;


}
