package com.perenc.xh.lsp.service.tcCarRecharge;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcCar.TcCarRecharge;

import java.util.Map;


public interface TcCarRechargeService {

    /**
     * 新增
     * @param tcCarRecharge
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcCarRecharge tcCarRecharge)throws Exception;


    /**
     * 修改
     * @param tcCarRecharge
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcCarRecharge tcCarRecharge)throws Exception;

    /**
     * 修改是否启用
     * @param tcCarRecharge
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateIsEnabled(TcCarRecharge tcCarRecharge) throws Exception;

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
     * 查询所有充值项
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getAllList(Map<String, Object> map) throws Exception;

}
