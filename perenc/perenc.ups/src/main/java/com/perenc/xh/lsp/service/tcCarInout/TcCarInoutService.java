package com.perenc.xh.lsp.service.tcCarInout;

import com.perenc.xh.commonUtils.model.JsReturnJsonData;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcCar.TcCarInout;

import java.util.Map;


public interface TcCarInoutService {

    /**
     * 新增
     * @param tcCarInout
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcCarInout tcCarInout)throws Exception;


    /**
     * 捷顺
     * 第三方接口
     * 接收车辆出场识别记录
     * @param tcCarInout
     * @param map
     * @return
     * @throws Exception
     */
    public JsReturnJsonData insertOutrecognition(TcCarInout tcCarInout, Map<String, Object> map) throws Exception;

    /**
     * 捷顺
     * 第三方接口
     * 测试远程开闸
     * @param tcCarInout
     * @param map
     * @return
     * @throws Exception
     */
    public JsReturnJsonData jsOpengate(TcCarInout tcCarInout, Map<String, Object> map) throws Exception;


    /**
     * 捷顺
     * 第三方接口
     * 存在优惠券计算
     * @param tcCarInout
     * @param map
     * @return
     * @throws Exception
     */
    public JsReturnJsonData jsCouponCharging(TcCarInout tcCarInout, Map<String, Object> map) throws Exception;


    /**
     * 修改
     * @param tcCarInout
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcCarInout tcCarInout)throws Exception;

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
     * 根据当前时间
     * 删除数据
     * 根据当前时间，日志30天前的日志删除
     */
    public Integer removeBatchTcCarInoutByEdate() throws Exception;
}
