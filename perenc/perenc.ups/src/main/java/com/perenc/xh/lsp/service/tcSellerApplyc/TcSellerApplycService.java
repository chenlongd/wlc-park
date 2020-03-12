package com.perenc.xh.lsp.service.tcSellerApplyc;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplyc;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerApplycData;

import java.util.Map;


public interface TcSellerApplycService {

    /**
     * 新增
     * @param tcSellerApplyc
     * @return
     * @throws Exception
     */
    public ReturnJsonData insert(TcSellerApplyc tcSellerApplyc)throws Exception;

    /**
     * 添加主表及明细表
     * @param tcSellerApplyc
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertDetail(TcSellerApplyc tcSellerApplyc) throws Exception;

    /**
     * 会议宴会申请停车券
     * @param tcSellerApplyc
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertMeeting(TcSellerApplyc tcSellerApplyc) throws Exception;

    /**
     * 修改
     * @param tcSellerApplyc
     * @return
     * @throws Exception
     */
    public ReturnJsonData update(TcSellerApplyc tcSellerApplyc)throws Exception;

    /**
     * 更改主表及明细表
     * @param tcSellerApplyc
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateDetail(TcSellerApplyc tcSellerApplyc) throws Exception;

    /**
     * 商家申领停车券审批
     * 新增
     * 申领票券
     * 申领票明细
     * @param data
     * @return
     * @throws Exception
     */
    public ReturnJsonData insertSellerApply(TcSellerApplycData data) throws Exception;

    /**
     * 商家申领停车券审批
     * @param tcSellerApplyc
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateIsApproval(TcSellerApplyc tcSellerApplyc) throws Exception;

    /**
     * 商家申领停车券审批
     * 选择票券id（1、2、3小时）,张数
     * @param tcSellerApplyc
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateIsApprovalApply(TcSellerApplyc tcSellerApplyc, Map<String, Object> map) throws Exception;

    /**
     * 修改有效期时间
     * 开始时间-结束时间
     * @param tcSellerApplyc
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateTime(TcSellerApplyc tcSellerApplyc) throws Exception;

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
     * 后台使用分页使用
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    public ReturnJsonData getAdminList(Map<String, Object> map, PageHelper pageHelper) throws Exception;



    /**
     * 根据当前时间
     * 使用状态更改已过期处理
     * 根据当前时间，把使用状态更改
     * @param
     * @return
     */
    public Integer updateBatchSellerApplycUseStatusByEdate() throws Exception;
}
