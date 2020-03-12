package com.perenc.xh.lsp.controller.admin.tcFeedback;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcFeedback.TcFeedback;
import com.perenc.xh.lsp.service.tcFeedback.TcFeedbackService;
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
@RequestMapping("feedback")
public class AdminTcFeedbackController {

    @Autowired(required = false)
    private TcFeedbackService tcFeedbackService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="意见反馈",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String content = ServletRequestUtils.getStringParameter(request,"content","");
        String contentImage = ServletRequestUtils.getStringParameter(request,"contentImage","");
        String tel = ServletRequestUtils.getStringParameter(request,"tel","");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(content)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的内容为空",null);
        }
        if(StringUtils.isEmpty(tel)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的电话为空",null);
        }
        TcFeedback tcFeedback=new TcFeedback();
        tcFeedback.setExtendId(extendId);
        tcFeedback.setType(Integer.valueOf(type));
        tcFeedback.setContent(content);
        tcFeedback.setContentImage(contentImage);
        tcFeedback.setTel(tel);
        //tcFeedback.setReply(tcFeedback.getReply());
        //tcFeedback.setReplyTime(tcFeedback.getReplyTime());
        tcFeedback.setTel(tcFeedback.getTel());
        return tcFeedbackService.insert(tcFeedback);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="意见反馈",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String content = ServletRequestUtils.getStringParameter(request,"content","");
        String contentImage = ServletRequestUtils.getStringParameter(request,"contentImage","");
        String tel = ServletRequestUtils.getStringParameter(request,"tel","");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户ID为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的类型为空",null);
        }
        if(StringUtils.isEmpty(content)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的内容为空",null);
        }
        if(StringUtils.isEmpty(tel)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的电话为空",null);
        }
        TcFeedback tcFeedback=new TcFeedback();
        tcFeedback.setId(id);
        tcFeedback.setExtendId(extendId);
        tcFeedback.setType(Integer.valueOf(type));
        tcFeedback.setContent(content);
        tcFeedback.setContentImage(contentImage);
        tcFeedback.setTel(tel);
        return tcFeedbackService.update(tcFeedback);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateReply")
    @ResponseBody
    @OperLog(operationType="意见反馈",operationName="回复")
    public ReturnJsonData updateReply(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String reply = ServletRequestUtils.getStringParameter(request,"reply","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主键ID为空",null);
        }
        if(StringUtils.isEmpty(reply)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的回复内容为空",null);
        }
        TcFeedback tcFeedback=new TcFeedback();
        tcFeedback.setId(id);
        tcFeedback.setReply(reply);
        return tcFeedbackService.updateReply(tcFeedback);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="意见反馈",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            if(strarray.length>0){
                return tcFeedbackService.delete(strarray);
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
    @OperLog(operationType="意见反馈",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcFeedbackService.getById(id);
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
    @OperLog(operationType="意见反馈",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String extendId = ServletRequestUtils.getStringParameter(request, "extendId", "");
        if (StringUtils.isNotEmpty(extendId)) {
            condition.put("extendId", extendId);
        }
        //意见反馈类型
        String type = ServletRequestUtils.getStringParameter(request, "type", "");
        if (StringUtils.isNotEmpty(type)) {
            condition.put("type", type);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcFeedbackService.getList(condition,pageHelper);
    }
}
