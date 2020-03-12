package com.perenc.xh.lsp.controller.phone.userExtend;

import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.model.mongoPage.PageHelper;
import com.perenc.xh.commonUtils.utils.qrcode.QRCodeUtil;
import com.perenc.xh.commonUtils.utils.validate.ValidateUtils;
import com.perenc.xh.lsp.entity.extendUser.ExtendUser;
import com.perenc.xh.lsp.entity.operLog.OperLog;
import com.perenc.xh.lsp.service.extendUser.ExtendUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("api/userExtend")
public class UserExtendController {

    @Autowired(required = false)
    private ExtendUserService extendUserService;





    @ResponseBody
    @RequestMapping("/update")
    @OperLog(operationType="客户",operationName="修改")
    public ReturnJsonData update(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request, "extendId", "");
        String sex = ServletRequestUtils.getStringParameter(request, "sex", "");
        String email = ServletRequestUtils.getStringParameter(request, "email", "");
        String contact = ServletRequestUtils.getStringParameter(request, "contact", "");
        String provinceId = ServletRequestUtils.getStringParameter(request, "province", "");
        String cityId = ServletRequestUtils.getStringParameter(request, "city", "");
        String countyId = ServletRequestUtils.getStringParameter(request, "county", "");
        String address = ServletRequestUtils.getStringParameter(request, "address", "");
        String headImage = ServletRequestUtils.getStringParameter(request, "headImage", "");
        String nickname = ServletRequestUtils.getStringParameter(request, "nickname", "");
        String username = ServletRequestUtils.getStringParameter(request, "username", "");
        String birthday = ServletRequestUtils.getStringParameter(request, "birthday", "");
        String driveAge = ServletRequestUtils.getStringParameter(request, "driveAge", "");
        //String area = ServletRequestUtils.getStringParameter(request, "area", "");
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
        if(StringUtils.isNotEmpty(provinceId)) {
            extendUser.setProvinceId(provinceId);
        }
        if(StringUtils.isNotEmpty(cityId)) {
            extendUser.setCityId(cityId);
        }
        if(StringUtils.isNotEmpty(countyId)) {
            extendUser.setCountyId(countyId);
        }
        if(StringUtils.isNotEmpty(address)) {
            extendUser.setAddress(address);
        }
        //头像
        if(StringUtils.isNotEmpty(headImage)) {
            extendUser.setHeadImage(headImage);
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
       return extendUserService.update(extendUser);
    }

    /**
     * 设置支付密码
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/updatePayPsword")
    @OperLog(operationType="客户",operationName="修改")
    public ReturnJsonData updatePayPsword(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String extendId = ServletRequestUtils.getStringParameter(request, "extendId", "");
        String payPsword = ServletRequestUtils.getStringParameter(request, "payPsword", "");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户Id为空",null);
        }
        if(StringUtils.isEmpty(payPsword)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的支付密码为空",null);
        }
        if(payPsword.length()!=6) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的支付密码",null);
        }
        if(!ValidateUtils.isSixNumber(payPsword)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的支付密码",null);
        }
        //查询数据是否存在
        ExtendUser extendUser=new ExtendUser();
        extendUser.setId(Integer.valueOf(extendId));
        extendUser.setPayPsword(payPsword);
        return extendUserService.updatePayPsword(extendUser);
    }

    /**
     * 设置支付密码
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/forgetPayPsword")
    @OperLog(operationType="客户",operationName="修改")
    public ReturnJsonData forgetPayPsword(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String extendId = ServletRequestUtils.getStringParameter(request, "extendId", "");
        String phone = ServletRequestUtils.getStringParameter(request, "phone", "");
        String code = ServletRequestUtils.getStringParameter(request, "code", "");
        String payPsword = ServletRequestUtils.getStringParameter(request, "payPsword", "");
        String confmpayPsword = ServletRequestUtils.getStringParameter(request, "confmpayPsword", "");
        if(StringUtils.isEmpty(extendId)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的客户Id为空",null);
        }
        if(StringUtils.isEmpty(phone)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的手机号为空",null);
        }
        if(StringUtils.isEmpty(code)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的手机验证码为空",null);
        }
        if(StringUtils.isEmpty(payPsword)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的新支付密码为空",null);
        }
        if(payPsword.length()!=6) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的支付密码",null);
        }
        if(!ValidateUtils.isSixNumber(payPsword)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的支付密码",null);
        }
        if(StringUtils.isEmpty(confmpayPsword)){
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入的确认支付密码为空",null);
        }
        if(confmpayPsword.length()!=6) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的确认支付密码",null);
        }
        if(!ValidateUtils.isSixNumber(confmpayPsword)) {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"请输入正确的确认支付密码",null);
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
        condition.put("phone",phone);
        condition.put("code",code);
        condition.put("confmpayPsword",confmpayPsword);
        //查询数据是否存在
        ExtendUser extendUser=new ExtendUser();
        extendUser.setId(Integer.valueOf(extendId));
        extendUser.setPayPsword(payPsword);
        return extendUserService.forgetPayPsword(extendUser,condition);
    }

    /**
     * 根据Id查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/getQrcodeById")
    @OperLog(operationType="客户",operationName="二维码查询")
    public void getQrcodeById(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setContentType("image/jpeg");  //设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");  //设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        ServletOutputStream sos =null; // 将图像输出到Servlet输出流中。;// 将图像输出到Servlet输出流中。
        try {
            sos=response.getOutputStream();
            if(StringUtils.isNotEmpty(id)) {
                //BufferedImage image = QRCodeUtil.zxingCodeImageCreate(id, 300, 300, "png");
                BufferedImage image = QRCodeUtil.zxingQRImageCreate(id, 300, 300, "png");
                // 将内存中的图片通过流动形式输出到客户端
                ImageIO.write(image, "png", sos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request, "extendId", "");
        if(StringUtils.isNotEmpty(id)) {
            return extendUserService.getById(Integer.valueOf(id));
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入Id为空",null);
        }
    }

    /**
     * 根据Id查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/getUserCenter")
    @OperLog(operationType="客户",operationName="单个查询")
    public ReturnJsonData getUserCenter(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return extendUserService.getUserCenter(Integer.valueOf(id));
        }else {
            return new ReturnJsonData(DataCodeUtil.PARAM_ERROR,"传入Id为空",null);
        }
    }

    /**
     * 根据Id查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/getIntegral")
    @OperLog(operationType="客户",operationName="单个查询")
    public ReturnJsonData getIntegral(HttpServletRequest request, HttpServletResponse response)throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        if(StringUtils.isNotEmpty(id)) {
            return extendUserService.getUserIntegral(Integer.valueOf(id));
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
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/json; charset=utf-8");
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
