package com.perenc.xh.lsp.service.readExcel.impl;


import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.dao.wxCustomer.WxCustomerDao;
import com.perenc.xh.lsp.service.readExcel.ReadExcelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/20 11:10
 **/
@Service("readExcelService")
@Transactional(rollbackFor = Exception.class)
public class ReadExcelServiceImpl implements ReadExcelService {



    @Autowired(required = false)
    private WxCustomerDao customerDao;

    @Autowired
    private MongoComDAO mongoComDAO;




    @Override
    public ReturnJsonData saveMemberData(List<List<Object>> excleList) throws Exception {

        return new ReturnJsonData(DataCodeUtil.FALSE,"导入数据失败",null);
    }



    public String getNumber(String data){
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(data);
        return m.replaceAll("").trim();
    }
}
