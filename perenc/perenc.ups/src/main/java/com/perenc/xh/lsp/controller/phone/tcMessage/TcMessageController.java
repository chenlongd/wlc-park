package com.perenc.xh.lsp.controller.phone.tcMessage;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcMessage.TcMessage;
import com.perenc.xh.lsp.service.tcMessage.TcMessageService;
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
 * @Date 2019/3/19 15:28
 **/
@Controller
@RequestMapping("api/message")
public class TcMessageController {

    //发票
    @Autowired(required = false)
    private TcMessageService tcMessageService;


    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="消息",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String title = ServletRequestUtils.getStringParameter(request,"title","");
        String content = ServletRequestUtils.getStringParameter(request,"content","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        if(StringUtils.isEmpty(title)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的标题为空",null);
        }
        if(StringUtils.isEmpty(content)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的内容为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的创建Id为空",null);
        }
        TcMessage tcMessage=new TcMessage();
        tcMessage.setTitle(title);
        tcMessage.setContent(content);
        tcMessage.setType(Integer.valueOf(type));
        tcMessage.setExtendId(Integer.valueOf(extendId));
        return tcMessageService.insert(tcMessage);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="消息",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String title = ServletRequestUtils.getStringParameter(request,"title","");
        String content = ServletRequestUtils.getStringParameter(request,"content","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        if(StringUtils.isEmpty(title)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的标题为空",null);
        }
        if(StringUtils.isEmpty(content)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的内容为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的创建Id为空",null);
        }
        TcMessage tcMessage=new TcMessage();
        tcMessage.setId(id);
        tcMessage.setTitle(title);
        tcMessage.setContent(content);
        tcMessage.setType(Integer.valueOf(type));
        tcMessage.setExtendId(Integer.valueOf(extendId));
        return tcMessageService.update(tcMessage);
    }

    /**
     * 根据Id查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/getById")
    @OperLog(operationType="消息",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcMessageService.getById(id);
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入Id为空",null);
        }
    }

    /**
     * 后台列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("page")
    @ResponseBody
    @OperLog(operationType="消息",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        String title = ServletRequestUtils.getStringParameter(request, "title", "");
        if (StringUtils.isNotEmpty(title)) {
            condition.put("title", title);
        }
        String extendId = ServletRequestUtils.getStringParameter(request, "extendId", "");
        if (StringUtils.isNotEmpty(extendId)) {
            condition.put("extendId", extendId);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcMessageService.getList(condition,pageHelper);
    }







}
