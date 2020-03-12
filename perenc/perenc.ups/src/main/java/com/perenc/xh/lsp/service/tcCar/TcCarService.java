package com.perenc.xh.lsp.service.tcCar;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcCar.TcCar;

import java.util.Map;


public interface TcCarService {

    /**
     * 新增
     * @param tcCar
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcCar tcCar)throws Exception;

    /**
     * 绑定车辆
     * 添加车辆同时，建立客户车辆关系
     * @param tcCar
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertExtendCar(TcCar tcCar, Map<String, Object> map) throws Exception;

    /**
     * 修改
     * @param tcCar
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcCar tcCar)throws Exception;

    /**
     * 车辆实名制认证，
     * 上传行车证图片
     * @param tcCar
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateTravelImg(TcCar tcCar, Map<String, Object> map) throws Exception;

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
     * 查询捷顺设备
     * 查询所有控制设备
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    public ReturnJsonData getJsDevices(Map<String, Object> map, PageHelper pageHelper) throws Exception;


    /**
     * 定时任务
     * 根据当前时间判断vip时间判断
     * viptype(VIP车类型1:临时车；2:Vip车；3:免费车)
     * @return
     * @throws Exception
     */
    public Integer updateBatchVipByedate() throws Exception;

    /**
     * 定时任务 免费车
     * 根据当前时间判断免费车时间判断
     * viptype(VIP车类型1:临时车；2:Vip车；3:免费车)
     * @return
     * @throws Exception
     */
    public Integer updateBatchFreeByedate() throws Exception;

    /**
     * 根据车牌号查询
     * 查询车辆信息 ，最近的一条
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData findOneCarOrder(Map<String, Object> map) throws Exception;


}
