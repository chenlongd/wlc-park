package com.perenc.xh.lsp.controller.admin.operLog;


import com.perenc.xh.commonUtils.model.PhoneReturnJson;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.service.operLog.OperLogRecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/25 10:39
 **/

@Controller
@RequestMapping("operLogRecord")
public class OperLogRecordController {

    @Autowired(required = false)
    private OperLogRecordService operLogRecordService;


    /**
     * 后台列表a
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("page")
    @ResponseBody
    public PhoneReturnJson getNoticeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String username = ServletRequestUtils.getStringParameter(request, "username", "");
        if (StringUtils.isNotEmpty(username)) {
            condition.put("username", username);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", 20);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return operLogRecordService.getOperLogRecordList(condition,pageHelper);
    }

}
