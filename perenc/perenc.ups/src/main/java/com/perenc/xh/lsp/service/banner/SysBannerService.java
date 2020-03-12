package com.perenc.xh.lsp.service.banner;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.banner.SysBanner;
import java.util.Map;

/**
 * @Description 系统轮播图
 * @Author xiaobai
 * @Date 2019/5/23 15:01
 **/
public interface SysBannerService {

    /**
     * 新增
     * @param banner
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(SysBanner banner)throws Exception;

    /**
     * 修改
     * @param banner
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(SysBanner banner)throws Exception;

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    public ReturnJsonData delete(String[] ids) throws Exception;

    /**
     * 获取详情
     * @param id
     * @return
     * @throws Exception
     */
    public ReturnJsonData getBannerInfo(String id) throws Exception;


    /**
     * 获取列表
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getBannerList(Map<String, Object> map, PageHelper pageHelper) throws Exception;

    /**
     * 前端查询
     * 轮播图
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getAllBannerList(Map<String, Object> map) throws Exception;
}
