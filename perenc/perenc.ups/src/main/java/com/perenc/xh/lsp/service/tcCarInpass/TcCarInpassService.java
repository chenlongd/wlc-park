package com.perenc.xh.lsp.service.tcCarInpass;

import com.perenc.xh.commonUtils.model.JsReturnJsonData;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcCar.TcCarInpass;

import java.util.Map;


public interface TcCarInpassService {

    /**
     * 新增
     * @param tcCarInpass
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcCarInpass tcCarInpass)throws Exception;

    /**
     * 捷顺
     * 第三方接口
     * 接收车辆入场过闸记录
     * @param tcCarInpass
     * @param map
     * @return
     * @throws Exception
     */
    public JsReturnJsonData insertCarin(TcCarInpass tcCarInpass, Map<String, Object> map) throws Exception;



    /**
     * 修改
     * @param tcCarInpass
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcCarInpass tcCarInpass)throws Exception;

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
     * 场内车辆查询
     * 场内所停的车辆
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    public ReturnJsonData getInsideList(Map<String, Object> map, PageHelper pageHelper) throws Exception;


    /**
     * 场内车辆查询
     * 场内所停的车辆_调用捷顺
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    public ReturnJsonData getInsideJsList(Map<String, Object> map, PageHelper pageHelper) throws Exception;

    /**
     * 定时任务每天晚上12点执行
     * 根据捷顺查询场内车辆，更改入场状态
     * 是否入场（1:已入场，2:未入场）
     * @return
     * @throws Exception
     */
    public Integer updateBatchIsEntryByInsideJscar() throws Exception;

    /**
     * 根据捷顺查询场内车辆非正常，更改入场状态
     * 是否入场（1:已入场，2:未入场）
     * @return
     * @throws Exception
     */
    public Integer updateBatchIsEntryByInsideJscarAbnormal(String carNum, String tocNum) throws Exception;


        /**
         * 根据当前时间
         * 删除数据
         * 根据当前时间，日志30天前的日志删除
         */
    public Integer removeBatchTcCarInpassByEdate() throws Exception;
}
