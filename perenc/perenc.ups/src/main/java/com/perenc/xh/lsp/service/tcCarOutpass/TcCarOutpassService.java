package com.perenc.xh.lsp.service.tcCarOutpass;

import com.perenc.xh.commonUtils.model.JsReturnJsonData;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcCar.TcCarOutpass;

import java.util.Map;


public interface TcCarOutpassService {

    /**
     * 新增
     * @param tcCarOutpass
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcCarOutpass tcCarOutpass)throws Exception;


    /**
     * 捷顺
     * 第三方接口
     * 接收车辆出场过闸记录
     * @param tcCarOutpass
     * @param map
     * @return
     * @throws Exception
     */
    public JsReturnJsonData insertCarout(TcCarOutpass tcCarOutpass, Map<String, Object> map) throws Exception;

    /**
     * 修改
     * @param tcCarOutpass
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcCarOutpass tcCarOutpass)throws Exception;

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
    public Integer removeBatchTcCarOutpassByEdate() throws Exception;

}
