package com.perenc.xh.lsp.service.weChatInfo;



import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.entity.weChatInfo.WeChatInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description 微信相关信息
 * @Author xiaobai
 * @Date 2019/5/20 16:05
 **/
public interface WeChatInfoService {

    /**
     * 新增
     * @param weChatInfo
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(WeChatInfo weChatInfo)throws Exception;

    /**
     * 修改
     * @param weChatInfo
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(WeChatInfo weChatInfo)throws Exception;

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    public ReturnJsonData delete(List<String> ids) throws Exception;

    /**
     * 获取单个详情
     * @param id
     * @return
     * @throws Exception
     */
    public ReturnJsonData getWechatInfo(String id) throws Exception;

    /**
     * 获取微信信息
     * @param condition
     * @return
     */
    ReturnJsonData getWeChatInfoList(Map<String, Object> condition, PageHelper pageHelper);
}
