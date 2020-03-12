package com.perenc.xh.lsp.controller.admin.userExtend;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.service.extendUser.ExtendUserService;
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
@RequestMapping("userExtend")
public class AdminUserExtendController {

    @Autowired(required = false)
    private ExtendUserService extendUserService;


    /**
     * 后台添加
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    @OperLog(operationType="客户",operationName="新增")
    public ReturnJsonData add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String sex = ServletRequestUtils.getStringParameter(request, "sex", "");
        String email = ServletRequestUtils.getStringParameter(request, "email", "");
        String contact = ServletRequestUtils.getStringParameter(request, "contact", "");
        String address = ServletRequestUtils.getStringParameter(request, "address", "");
        String nickname = ServletRequestUtils.getStringParameter(request, "nickname", "");
        String username = ServletRequestUtils.getStringParameter(request, "username", "");
        String birthday = ServletRequestUtils.getStringParameter(request, "birthday", "");
        String driveAge = ServletRequestUtils.getStringParameter(request, "driveAge", "");
        String area = ServletRequestUtils.getStringParameter(request, "area", "");
        //查询数据是否存在
        ExtendUser extendUser=new ExtendUser();
        //性别
        if(StringUtils.isNotEmpty(sex)) {
            extendUser.setSex(Integer.valueOf(sex));
        }
        if(StringUtils.isNotEmpty(email)) {
            extendUser.setEmail(email);
        }
        //联系人
        if(StringUtils.isNotEmpty(contact)) {
            extendUser.setContact(contact);
        }
        if(StringUtils.isNotEmpty(address)) {
            extendUser.setAddress(address);
        }
        //昵称
        if(StringUtils.isNotEmpty(nickname)) {
            extendUser.setNickname(nickname);
        }
        if(StringUtils.isNotEmpty(username)) {
            extendUser.setUsername(username);
        }
        //生日
        if(StringUtils.isNotEmpty(birthday)) {
            extendUser.setBirthday(birthday);
        }
        if(StringUtils.isNotEmpty(driveAge)) {
            extendUser.setDriveAge(Integer.valueOf(driveAge));
        }
        //地区
        if(StringUtils.isNotEmpty(area)) {
            extendUser.setArea(area);
        }
        return extendUserService.insert(extendUser);
    }



    @ResponseBody
    @RequestMapping("/update")
    @OperLog(operationType="客户",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        String sex = ServletRequestUtils.getStringParameter(request, "sex", "");
        String email = ServletRequestUtils.getStringParameter(request, "email", "");
        String contact = ServletRequestUtils.getStringParameter(request, "contact", "");
        String address = ServletRequestUtils.getStringParameter(request, "address", "");
        String nickname = ServletRequestUtils.getStringParameter(request, "nickname", "");
        String username = ServletRequestUtils.getStringParameter(request, "username", "");
        String birthday = ServletRequestUtils.getStringParameter(request, "birthday", "");
        String driveAge = ServletRequestUtils.getStringParameter(request, "driveAge", "");
        String area = ServletRequestUtils.getStringParameter(request, "area", "");
        if(StringUtils.isEmpty(id)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的主Id为空",null);
        }
        //查询数据是否存在
        ExtendUser extendUser=new ExtendUser();
        extendUser.setId(Integer.valueOf(id));
        //性别
        if(StringUtils.isNotEmpty(sex)) {
            extendUser.setSex(Integer.valueOf(sex));
        }
        if(StringUtils.isNotEmpty(email)) {
            extendUser.setEmail(email);
        }
        //联系人
        if(StringUtils.isNotEmpty(contact)) {
            extendUser.setContact(contact);
        }
        if(StringUtils.isNotEmpty(address)) {
            extendUser.setAddress(address);
        }
        //昵称
        if(StringUtils.isNotEmpty(nickname)) {
            extendUser.setNickname(nickname);
        }
        if(StringUtils.isNotEmpty(username)) {
            extendUser.setUsername(username);
        }
        //生日
        if(StringUtils.isNotEmpty(birthday)) {
            extendUser.setBirthday(birthday);
        }
        if(StringUtils.isNotEmpty(driveAge)) {
            extendUser.setDriveAge(Integer.valueOf(driveAge));
        }
        //地区
        if(StringUtils.isNotEmpty(area)) {
            extendUser.setArea(area);
        }
       return extendUserService.update(extendUser);
    }

    /**
     * 后台删除
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("remove")
    @ResponseBody
    @OperLog(operationType="计费规则",operationName="删除")
    public ReturnJsonData remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
        //把String转换成数据
        List<String> list =new ArrayList<>();
        if(ids!=null && ids!=""){
            String[] strarray=ids.split(",");
            for (int i = 0; i < strarray.length; i++){
                String id=strarray[i];
                list.add(id);
            }
         return extendUserService.delete(list);
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
    @OperLog(operationType="客户",operationName="单个查询")
    public ReturnJsonData getById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return extendUserService.getById(Integer.valueOf(id));
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
    @OperLog(operationType="客户",operationName="列表查询")
    public ReturnJsonData page(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        String extendId = ServletRequestUtils.getStringParameter(request, "extendId", "");
        if (StringUtils.isNotEmpty(extendId)) {
            condition.put("extendId", extendId);
        }
        String phone = ServletRequestUtils.getStringParameter(request, "phone", "");
        if (StringUtils.isNotEmpty(phone)) {
            condition.put("phone", phone);
        }
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize",20);
        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(current);
        pageHelper.setRows(pageSize);
        return extendUserService.getExtendUserList(condition,pageHelper);
    }




}
