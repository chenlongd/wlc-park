package com.perenc.xh.lsp.controller.admin.tcSeller;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcSeller.TcSeller;
import com.perenc.xh.lsp.service.tcSeller.TcSellerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("seller")
public class AdminTcSellerController {

    @Autowired(required = false)
    private TcSellerService tcSellerService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="商家",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String descp = ServletRequestUtils.getStringParameter(request,"descp","");
        String logo = ServletRequestUtils.getStringParameter(request,"logo","");
        String licenseimg = ServletRequestUtils.getStringParameter(request,"licenseimg","");
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家名称为空",null);
        }
        if(StringUtils.isEmpty(licenseimg)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的营业执照为空",null);
        }
        if(StringUtils.isEmpty(phone)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的手机号为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家类型为空",null);
        }
        //判断手机号
        if(StringUtils.isNotEmpty(phone)) {
            if (phone.length() != 11) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "手机号应为11位数", null);
            }
            if (!ValidateUtils.isPhoneCheck(phone)) {
                return new ReturnJsonData(DataCodeUtil.PARAM_ERROR, "请输入正确的手机号", null);
            }
        }
        TcSeller tcSeller=new TcSeller();
        tcSeller.setName(name);
        tcSeller.setDescp(descp);
        tcSeller.setLogo(logo);
        tcSeller.setLicenseImg(licenseimg);
        tcSeller.setPhone(phone);
        tcSeller.setType(Integer.valueOf(type));
        tcSeller.setIsApproval(1);
        tcSeller.setIsWork(1);
        return tcSellerService.insert(tcSeller);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="商家",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String name = ServletRequestUtils.getStringParameter(request,"name","");
        String descp = ServletRequestUtils.getStringParameter(request,"descp","");
        String logo = ServletRequestUtils.getStringParameter(request,"logo","");
        String licenseimg = ServletRequestUtils.getStringParameter(request,"licenseimg","");
        String username = ServletRequestUtils.getStringParameter(request,"username","");
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        String password = ServletRequestUtils.getStringParameter(request,"password","");
        String email = ServletRequestUtils.getStringParameter(request,"email","");
        String type = ServletRequestUtils.getStringParameter(request,"type","");
        //String extendId = ServletRequestUtils.getStringParameter(request,"extendId","");
        if(StringUtils.isEmpty(name)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家名称为空",null);
        }
        if(StringUtils.isEmpty(descp)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家简介为空",null);
        }
        if(StringUtils.isEmpty(logo)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家Logo为空",null);
        }
        if(StringUtils.isEmpty(licenseimg)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的营业执照为空",null);
        }
        if(StringUtils.isEmpty(username)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家用户名为空",null);
        }
        if(StringUtils.isEmpty(phone)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家手机号为空",null);
        }
        if(StringUtils.isEmpty(password)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家密码为空",null);
        }
        if(StringUtils.isEmpty(type)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家类型为空",null);
        }
        TcSeller tcSeller=new TcSeller();
        tcSeller.setId(Integer.valueOf(id));
        tcSeller.setName(name);
        tcSeller.setDescp(descp);
        tcSeller.setLogo(logo);
        tcSeller.setLicenseImg(licenseimg);
        tcSeller.setPhone(phone);
        tcSeller.setPassword(password);
        tcSeller.setEmail(email);
        tcSeller.setType(Integer.valueOf(type));
        tcSeller.setIsApproval(1);
        return tcSellerService.update(tcSeller);
    }

    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateIsApproval")
    @ResponseBody
    @OperLog(operationType="商家",operationName="审核")
    public ReturnJsonData updateIsApproval(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String isApproval = ServletRequestUtils.getStringParameter(request,"isApproval","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的Id为空",null);
        }
        if(StringUtils.isEmpty(isApproval)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的审核状态为空",null);
        }
        TcSeller tcSeller=new TcSeller();
        tcSeller.setId(Integer.valueOf(id));
        tcSeller.setIsApproval(Integer.valueOf(isApproval));
        return tcSellerService.updateIsApproval(tcSeller);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="商家",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        List<Integer> list =new ArrayList<>();
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            for (int i = 0; i < strarray.length; i++){
                list.add(Integer.parseInt(strarray[i]));
            }
        }
        if(list!=null && list.size()>0){
            return tcSellerService.deleteBatch(list);
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
    @OperLog(operationType="商家",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcSellerService.getById(Integer.valueOf(id));
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
    @OperLog(operationType="商家",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String name = ServletRequestUtils.getStringParameter(request, "name", "");
        if (StringUtils.isNotEmpty(name)) {
            condition.put("name", name);
        }
        String phone = ServletRequestUtils.getStringParameter(request, "phone", "");
        if (StringUtils.isNotEmpty(phone)) {
            condition.put("phone", phone);
        }
        String type = ServletRequestUtils.getStringParameter(request, "type", "");
        if (StringUtils.isNotEmpty(type)) {
            condition.put("type", type);
        }
        String isApproval = ServletRequestUtils.getStringParameter(request, "isApproval", "");
        if (StringUtils.isNotEmpty(isApproval)) {
            condition.put("isApproval", isApproval);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcSellerService.getList(condition,pageHelper);
    }

    /**
     * 商家发放统计分页
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("stat")
    @ResponseBody
    @OperLog(operationType="商家",operationName="列表查询")
    public ReturnJsonData stat(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String sellerName = ServletRequestUtils.getStringParameter(request, "sellerName", "");
        if (StringUtils.isNotEmpty(sellerName)) {
            condition.put("sellerName", sellerName);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcSellerService.getListStat(condition,pageHelper);
    }


    /**
     * 酒店集团统计分页
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("statGroup")
    @ResponseBody
    @OperLog(operationType="商家",operationName="列表查询")
    public ReturnJsonData statGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String sellerName = ServletRequestUtils.getStringParameter(request, "sellerName", "");
        if (StringUtils.isNotEmpty(sellerName)) {
            condition.put("sellerName", sellerName);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcSellerService.getListStatGroup(condition,pageHelper);
    }
}
