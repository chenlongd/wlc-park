package com.perenc.xh.lsp.service.extendUser;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;

import java.util.List;
import java.util.Map;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/4/12 9:37
 **/
public interface ExtendUserService {

    /**
     * 新增
     * @param extendUser
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(ExtendUser extendUser)throws Exception;

    /**
     * 修改
     * @param extendUser
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(ExtendUser extendUser)throws Exception;

    /**
     * 设置支付密码
     * @param extendUser
     * @return
     * @throws Exception
     */
    public ReturnJsonData updatePayPsword(ExtendUser extendUser) throws Exception;

    /**
     * 找回支付密码
     * @param extendUser
     * @return
     * @throws Exception
     */
    public ReturnJsonData forgetPayPsword(ExtendUser extendUser, Map<String, Object> map) throws Exception;

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    public ReturnJsonData delete(List<String> ids) throws Exception;

    /**
     * 获取详情
     * @param id
     * @return
     * @throws Exception
     */
    public ReturnJsonData getById(Integer id) throws Exception;

    /**
     * 获取用户中心数据
     * @param id
     * @return
     * @throws Exception
     */
    public ReturnJsonData getUserCenter(Integer id) throws Exception;

    /**
     * 获取用户积分
     * @param id
     * @return
     * @throws Exception
     */
    public ReturnJsonData getUserIntegral(Integer id) throws Exception;

    /**
     * 获取扩展用户信息
     * @param condition
     * @return
     */
    ReturnJsonData getExtendUserList(Map<String, Object> condition, PageHelper pageHelper);
}
