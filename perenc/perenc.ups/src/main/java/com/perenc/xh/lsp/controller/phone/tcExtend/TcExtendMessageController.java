package com.perenc.xh.lsp.controller.phone.tcExtend;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcExtend.TcExtendMessage;
import com.perenc.xh.lsp.service.tcExtendMessage.TcExtendMessageService;
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


@Controller
@RequestMapping("api/extendMessage")
public class TcExtendMessageController {

    @Autowired(required = false)
    private TcExtendMessageService tcExtendMessageService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="客户消息",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String messageId = ServletRequestUtils.getStringParameter(request,"messageId","");
        String senderId = ServletRequestUtils.getStringParameter(request,"senderId","");
        String receiverId = ServletRequestUtils.getStringParameter(request,"receiverId","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        if(StringUtils.isEmpty(messageId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的消息ID为空",null);
        }
        if(StringUtils.isEmpty(senderId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户发送ID为空",null);
        }
        if(StringUtils.isEmpty(receiverId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户接收ID为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        TcExtendMessage tcExtendMessage=new TcExtendMessage();
        tcExtendMessage.setMessageId(messageId);
        tcExtendMessage.setSenderId(Integer.valueOf(senderId));
        tcExtendMessage.setReceiverId(Integer.valueOf(receiverId));
        tcExtendMessage.setType(Integer.valueOf(type));
        return tcExtendMessageService.insert(tcExtendMessage);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="客户消息",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcExtendMessageService.delete(strarray);
            }else {
                return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE,"删除失败",null);
            }
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入Id为空",null);
        }

    }


    /**
     * 根据Id查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/getById")
    @OperLog(operationType="客户消息",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcExtendMessageService.getById(id);
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
    @OperLog(operationType="客户消息",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        Map<String, Object> condition = new HashMap<>();
        String senderId = ServletRequestUtils.getStringParameter(request, "senderId", "");
        if (StringUtils.isNotEmpty(senderId)) {
            condition.put("senderId", senderId);
        }
        String receiverId = ServletRequestUtils.getStringParameter(request, "receiverId", "");
        if (StringUtils.isNotEmpty(receiverId)) {
            condition.put("receiverId", receiverId);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcExtendMessageService.getList(condition,pageHelper);
    }




}
