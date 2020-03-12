package com.perenc.xh.lsp.controller.admin.importExcel;


import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.PhoneReturnJson;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.utils.excel.ReadExcleUtil;
import com.perenc.xh.lsp.service.readExcel.ReadExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/20 10:59
 **/
@Controller
@RequestMapping("readExcel")
public class ReadExcelController {

    @Autowired(required = false)
    private ReadExcelService readExcelService;



    @RequestMapping("test")
    public String test(HttpServletRequest request, HttpServletResponse response)throws Exception {
        return "upload";
    }


    @RequestMapping("saveHotelMemberData")
    @ResponseBody
    public ReturnJsonData saveHotelMemberData(HttpServletRequest request, HttpServletResponse response)throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile newfile = multipartRequest.getFile("file");
        List<List<Object>> excelList = ReadExcleUtil.readExcel(newfile);
        if(excelList.size()>1) {
            excelList.remove(0);
            return readExcelService.saveMemberData(excelList);
        }
        return new ReturnJsonData(DataCodeUtil.FALSE,"导入excel失败",null);
    }
}
