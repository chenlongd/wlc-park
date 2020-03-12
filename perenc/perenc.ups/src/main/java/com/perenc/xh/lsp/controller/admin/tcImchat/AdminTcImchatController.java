package com.perenc.xh.lsp.controller.admin.tcImchat;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcImchat.TcImchat;
import com.perenc.xh.lsp.service.tcImchat.TcImchatService;
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
@RequestMapping("imchat")
public class AdminTcImchatController {

    @Autowired(required = false)
    private TcImchatService tcImchatService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="IM聊天记录",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String sendExtendId = ServletRequestUtils.getStringParameter(request,"sendExtendId","");
        String receiveExtendId = ServletRequestUtils.getStringParameter(request,"receiveExtendId","");
        String content = ServletRequestUtils.getStringParameter(request,"content","");
        if(StringUtils.isEmpty(sendExtendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发送人ID为空",null);
        }
        if(StringUtils.isEmpty(receiveExtendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的接收人ID为空",null);
        }
        if(StringUtils.isEmpty(content)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的内容为空",null);
        }
        TcImchat tcImchat=new TcImchat();
        tcImchat.setSendExtendId(Integer.valueOf(sendExtendId));
        tcImchat.setReceiveExtendId(Integer.valueOf(receiveExtendId));
        tcImchat.setContent(content);
        return tcImchatService.insert(tcImchat);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="IM聊天记录",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String sendExtendId = ServletRequestUtils.getStringParameter(request,"sendExtendId","");
        String receiveExtendId = ServletRequestUtils.getStringParameter(request,"receiveExtendId","");
        String content = ServletRequestUtils.getStringParameter(request,"content","");
        if(StringUtils.isEmpty(sendExtendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的发送人ID为空",null);
        }
        if(StringUtils.isEmpty(receiveExtendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的接收人ID为空",null);
        }
        if(StringUtils.isEmpty(content)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的内容为空",null);
        }
        TcImchat tcImchat=new TcImchat();
        tcImchat.setId(id);
        tcImchat.setSendExtendId(Integer.valueOf(sendExtendId));
        tcImchat.setReceiveExtendId(Integer.valueOf(receiveExtendId));
        tcImchat.setContent(content);
        return tcImchatService.update(tcImchat);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="IM聊天记录",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcImchatService.delete(strarray);
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
    @OperLog(operationType="IM聊天记录",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcImchatService.getById(id);
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
    @OperLog(operationType="IM聊天记录",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String sendExtendId = ServletRequestUtils.getStringParameter(request, "sendExtendId", "");
        if (StringUtils.isNotEmpty(sendExtendId)) {
            condition.put("sendExtendId", sendExtendId);
        }
        String receiveExtendId = ServletRequestUtils.getStringParameter(request, "receiveExtendId", "");
        if (StringUtils.isNotEmpty(receiveExtendId)) {
            condition.put("receiveExtendId", receiveExtendId);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcImchatService.getList(condition,pageHelper);
    }
}
