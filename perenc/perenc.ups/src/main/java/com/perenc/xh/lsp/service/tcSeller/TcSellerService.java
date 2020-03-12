package com.perenc.xh.lsp.service.tcSeller;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcSeller.TcSeller;

import java.util.List;
import java.util.Map;


public interface TcSellerService {

    /**
     * 新增
     * @param tcSeller
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcSeller tcSeller)throws Exception;

    /**
     * 商家注册
     * 验证码验证
     * @param tcSeller
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertCheckCode(TcSeller tcSeller, Map<String, Object> map) throws Exception;

    /**
     * 商家登录
     * @param param
     * @return
     * @throws Exception
     */
    public TcSeller sellerLogin(Map<String, Object> param) throws Exception;

    /**
     * 商家登录
     * 手机号验证码
     * @param param
     * @return
     * @throws Exception
     */
    public ReturnJsonData phoneLogin(Map<String, Object> param) throws Exception;

    /**
     * 商家登录
     * 退出
     * @param param
     * @return
     * @throws Exception
     */
    public ReturnJsonData loginOut(Map<String, Object> param) throws Exception;

    /**
     * 修改
     * @param tcSeller
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcSeller tcSeller) throws Exception;

    /**
     * 商家重置密码
     * @param id
     * @return
     */
    public ReturnJsonData resetPassword(Integer id) throws Exception;


    /**
     * 修改密码
     * @param param
     * @return
     */
    public ReturnJsonData updatePWd(Map<String, Object> param)  throws Exception;

    /**
     * 找回密码
     * 通过手机号验证码找回密码
     * @param param
     * @return
     * @throws Exception
     */
    public ReturnJsonData forgetPassword(Map<String, Object> param) throws Exception;

    /**
     * 修改一个属性
     * @param tcSeller
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateOne(TcSeller tcSeller) throws Exception;


    /**
     * 修改审核状态
     * @param tcSeller
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateIsApproval(TcSeller tcSeller)throws Exception;

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    public ReturnJsonData delete(List<String> ids) throws Exception;

    /**
     * 批量删除
     * @param list
     * @return
     * @throws Exception
     */
    public ReturnJsonData deleteBatch(List list) throws Exception;

    /**
     * 根据id查询返回对象
     * @param id
     * @return
     */
    public TcSeller findById(Integer id);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public ReturnJsonData getById(Integer id) throws Exception;


    /**
     * 获取列表
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception;


    /**
     * 商家发放统计分页
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    public ReturnJsonData getListStat(Map<String, Object> map, PageHelper pageHelper) throws Exception;


    /**
     * 酒店集团统计分页
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    public ReturnJsonData getListStatGroup(Map<String, Object> map, PageHelper pageHelper) throws Exception;

    /**
     * 更改订单商家ID
     * @return
     * @throws Exception
     */
    public void updateOrderSellerId() throws Exception;

}
