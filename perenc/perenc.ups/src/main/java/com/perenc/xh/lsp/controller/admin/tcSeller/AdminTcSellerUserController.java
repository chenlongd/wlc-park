package com.perenc.xh.lsp.controller.admin.tcSeller;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.entity.tcSeller.TcSellerUser;
import com.perenc.xh.lsp.service.tcSellerUser.TcSellerUserService;
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
@RequestMapping("sellerUser")
public class AdminTcSellerUserController {

    @Autowired(required = false)
    private TcSellerUserService tcSellerUserService;

    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="商家用户",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String sellerId = ServletRequestUtils.getStringParameter(request,"sellerId","");
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        if(StringUtils.isEmpty(sellerId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的商家ID为空",null);
        }
        if(StringUtils.isEmpty(phone)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的手机号为空",null);
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
        Map<String, Object> condition = new HashMap<>();
        TcSellerUser tcSellerUser=new TcSellerUser();
        tcSellerUser.setPhone(phone);
        tcSellerUser.setSellerId(Integer.valueOf(sellerId));
        return tcSellerUserService.insertSellerUser(tcSellerUser,condition);
    }


    /**
     * 后台修改
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("update")
    @ResponseBody
    @OperLog(operationType="商家用户",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String phone = ServletRequestUtils.getStringParameter(request,"phone","");
        String province = ServletRequestUtils.getStringParameter(request,"province","");
        String city = ServletRequestUtils.getStringParameter(request,"city","");
        String county = ServletRequestUtils.getStringParameter(request,"country","");
        String address = ServletRequestUtils.getStringParameter(request,"address","");
        String headImage = ServletRequestUtils.getStringParameter(request,"headImage","");
        String sex = ServletRequestUtils.getStringParameter(request,"sex","");
        String nickname = ServletRequestUtils.getStringParameter(request,"nickname","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主Id为空",null);
        }
        //更改其它一项数据
        TcSellerUser tcSellerUser=new TcSellerUser();
        tcSellerUser.setId(Integer.valueOf(id));
        tcSellerUser.setPhone(phone);
        tcSellerUser.setProvinceId(province);
        tcSellerUser.setCityId(city);
        tcSellerUser.setCountyId(county);
        tcSellerUser.setAddress(address);
        tcSellerUser.setHeadImage(headImage);
        if(StringUtils.isNotEmpty(sex)) {
            tcSellerUser.setSex(Integer.valueOf(sex));
        }
        tcSellerUser.setNickname(nickname);
        return tcSellerUserService.updateOne(tcSellerUser);
    }


    /**
     * 修改禁用状态
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateIsEnabled")
    @ResponseBody
    @OperLog(operationType="商家用户",operationName="修改禁用状态")
    public ReturnJsonData updateIsEnabled(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String id = ServletRequestUtils.getStringParameter(request,"id","");
        String isEnabled = ServletRequestUtils.getStringParameter(request,"isEnabled","");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主Id为空",null);
        }
        if(StringUtils.isEmpty(isEnabled)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的禁用状态为空",null);
        }
        //更改其它一项数据
        TcSellerUser tcSellerUser=new TcSellerUser();
        tcSellerUser.setId(Integer.valueOf(id));
        tcSellerUser.setIsEnabled(Integer.valueOf(isEnabled));
        return tcSellerUserService.updateIsEnabled(tcSellerUser);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="商家用户",operationName="删除")
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
            return tcSellerUserService.deleteBatch(list);
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
    @OperLog(operationType="商家用户",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return tcSellerUserService.getById(Integer.valueOf(id));
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
    @OperLog(operationType="商家用户",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String phone = ServletRequestUtils.getStringParameter(request, "phone", "");
        if (StringUtils.isNotEmpty(phone)) {
            condition.put("phone", phone);
        }
        String sellerId = ServletRequestUtils.getStringParameter(request, "sellerId", "");
        if (StringUtils.isNotEmpty(sellerId)) {
            condition.put("sellerId", sellerId);
        }
        String isEnabled = ServletRequestUtils.getStringParameter(request, "isEnabled", "");
        if (StringUtils.isNotEmpty(isEnabled)) {
            condition.put("isEnabled", isEnabled);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", PageHelper.pageSize);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return tcSellerUserService.getList(condition,pageHelper);
    }
}
