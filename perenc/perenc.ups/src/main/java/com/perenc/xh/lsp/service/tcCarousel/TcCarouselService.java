package com.perenc.xh.lsp.service.tcCarousel;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcCarousel.TcCarousel;

import java.util.Map;


public interface TcCarouselService {

    /**
     * 新增
     * @param tcCarousel
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcCarousel tcCarousel)throws Exception;

    /**
     * 修改
     * @param tcCarousel
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcCarousel tcCarousel)throws Exception;

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
     * 获取轮播图列表
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getList(Map<String, Object> map, PageHelper pageHelper) throws Exception;

    /**
     * 前端查询
     * 轮播图
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getTcCarOuselList(Map<String, Object> map) throws Exception;


}
