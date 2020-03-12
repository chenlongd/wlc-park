package com.perenc.xh.lsp.service.tcSellerUser;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerUser;

import java.util.List;
import java.util.Map;


public interface TcSellerUserService {

    /**
     * 新增
     * @param tcSellerUser
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcSellerUser tcSellerUser)throws Exception;

    /**
     * 商家注册
     * 验证码验证
     * @param tcSellerUser
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertCheckCode(TcSellerUser tcSellerUser, Map<String, Object> map) throws Exception;

    /**
     * 添加商家用户
     * @param tcSellerUser
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertSellerUser(TcSellerUser tcSellerUser, Map<String, Object> map) throws Exception;

    /**
     * 商家登录
     * @param param
     * @return
     * @throws Exception
     */
    public TcSellerUser sellerLogin(Map<String, Object> param) throws Exception;

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
     * 手机号验证码
     * @param param
     * @return
     * @throws Exception
     */
    public ReturnJsonData phonePasswordLogin(Map<String, Object> param) throws Exception;

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
     * @param tcSellerUser
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcSellerUser tcSellerUser) throws Exception;

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
     * @param tcSellerUser
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateOne(TcSellerUser tcSellerUser) throws Exception;


    /**
     * 修改启用状态
     * @param tcSellerUser
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateIsEnabled(TcSellerUser tcSellerUser)throws Exception;

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
     * 查询所有
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getAllList(Map<String, Object> map) throws Exception;

}
