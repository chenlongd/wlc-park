package com.perenc.xh.lsp.service.operLog;

import com.perenc.xh.commonUtils.model.PhoneReturnJson;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLogRecord;


import java.util.Map;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/25 10:04
 **/
public interface OperLogRecordService {

    /**
     * 新增
     * @param operLogRecord
     * @return
     * @throws Exception
     */
    public PhoneReturnJson insert(OperLogRecord operLogRecord)throws Exception;


    /**
     * 删除
     * @param id
     * @return
     * @throws Exception
     */
    public PhoneReturnJson delete(String id) throws Exception;

    /**
     * 获取操作日志列表
     * @param map
     * @param pageHelper
     * @return
     * @throws Exception
     */
    public PhoneReturnJson getOperLogRecordList(Map<String, Object> map, PageHelper pageHelper)throws Exception;
}
