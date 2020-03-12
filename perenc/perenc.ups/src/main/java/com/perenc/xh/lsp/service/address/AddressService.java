package com.perenc.xh.lsp.service.address;

import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.lsp.entity.address.Address;

import java.util.Map;

/**
 * @Description 地址
 * @Author xiaobai
 * @Date 2019/6/13 11:27
 **/
public interface AddressService {

    /**
     * 新增
     * @param address
     * @return
     * @throws Exception
     */
    public ReturnJsonData addAddressInfo(Address address)throws Exception;

    /**
     * 修改
     * @param address
     * @return
     * @throws Exception
     */
    public ReturnJsonData updateAddressInfo(Address address)throws Exception;

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    public ReturnJsonData deleteAddress(String[] ids) throws Exception;

    /**
     * 获取详情
     * @param id
     * @return
     * @throws Exception
     */
    public ReturnJsonData getAddressInfo(String id) throws Exception;

    /**
     * 选择默认地址
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData seleteAddress(Map<String, Object> map) throws Exception;


    /**
     * 获取列表
     * @param map
     * @return
     * @throws Exception
     */
    public ReturnJsonData getAddressList(Map<String, Object> map) throws Exception;
}
