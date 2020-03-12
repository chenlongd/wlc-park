package com.perenc.xh.lsp.service.readExcel;

import com.perenc.xh.commonUtils.model.PhoneReturnJson;
import com.perenc.xh.commonUtils.model.ReturnJsonData;

import java.util.List;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/20 11:05
 **/
public interface ReadExcelService {



    /**
     * 酒店会员数据excel保存
     * @param excleList
     * @return
     * @throws Exception
     */
    public ReturnJsonData saveMemberData(List<List<Object>> excleList) throws Exception;
}
