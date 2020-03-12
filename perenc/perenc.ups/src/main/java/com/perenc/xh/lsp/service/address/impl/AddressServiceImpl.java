package com.perenc.xh.lsp.service.address.impl;

import com.perenc.xh.commonUtils.dao.MongoComDAO;
import com.perenc.xh.commonUtils.model.DataCodeUtil;
import com.perenc.xh.commonUtils.model.ReturnJsonData;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.lsp.entity.address.Address;
import com.perenc.xh.lsp.service.address.AddressService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 地址
 * @Author xiaobai
 * @Date 2019/6/13 11:30
 **/
@Service("addressService")
@Transactional(rollbackFor = Exception.class)
public class AddressServiceImpl implements AddressService {

    @Autowired
    private MongoComDAO mongoComDAO;

    /**
     * 添加
     * @param address
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData addAddressInfo(Address address) throws Exception {
        Map<String,Object> condition = new HashMap<>();
        condition.put("customerId",address.getCustomerId());
        List<Address> addressList = mongoComDAO.executeForObjectList(condition, Address.class);
        if(addressList.size() > 0) {
            address.setUseType(0);
            address.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = mongoComDAO.executeInsert(address);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加地址成功",null);
            }
        }else{
            address.setUseType(1);
            address.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            int flag = mongoComDAO.executeInsert(address);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"添加地址成功",null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.INSERT_DATABASE,"添加地址失败",null);
    }

    /**
     * 修改
     * @param address
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData updateAddressInfo(Address address) throws Exception {
        Address returnAddress = mongoComDAO.executeForObjectById(address.getId(), Address.class);
        if(returnAddress != null){
            if(StringUtils.isNotEmpty(String.valueOf(address.getCustomerId()))){
                returnAddress.setCustomerId(address.getCustomerId());
            }
            if(StringUtils.isNotEmpty(address.getUsername())){
                returnAddress.setUsername(address.getUsername());
            }
            if(StringUtils.isNotEmpty(address.getPhone())){
                returnAddress.setPhone(address.getPhone());
            }
            if(StringUtils.isNotEmpty(address.getProvince())){
                returnAddress.setProvince(address.getProvince());
            }
            if(StringUtils.isNotEmpty(address.getCity())){
                returnAddress.setCity(address.getCity());
            }
            if(StringUtils.isNotEmpty(address.getArea())){
                returnAddress.setArea(address.getArea());
            }
            if(StringUtils.isNotEmpty(address.getDetailedAddress())){
                returnAddress.setDetailedAddress(address.getDetailedAddress());
            }
//            if(StringUtils.isNotEmpty(String.valueOf(address.getUseType()))){
//                returnAddress.setUseType(address.getUseType());
//            }
            int flag = mongoComDAO.executeUpdate(returnAddress);
            if(flag > 0){
                return new ReturnJsonData(DataCodeUtil.SUCCESS,"修改地址成功",null);
            }
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"修改地址失败",null);
    }

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData deleteAddress(String[] ids) throws Exception {
        int useType = 0;
        boolean flag = true;
        Address address = null;
        for(String id : ids){
            address = mongoComDAO.executeForObjectById(id, Address.class);
            Map<String,Object> condition = new HashMap<>();
            condition.put("id",id);
            int i = mongoComDAO.executeDelete(condition, Address.class);
            if(i < 0){
                flag = false;
                break;
            }
        }
        //系统选择默认地址
        Map<String,Object> condition = new HashMap<>();
        condition.put("customerId",address.getCustomerId());
        List<Address> addressList = mongoComDAO.executeForObjectList(condition, Address.class);
        if(addressList.size() > 0){
            for(Address add : addressList){
                if(add.getUseType() == 1){
                    useType = 1;
                    break;
                }
            }
        }
        if(0 == useType){
            if(addressList.size() > 0){
                addressList.get(0).setUseType(1);
                mongoComDAO.executeUpdate(addressList.get(0));
            }
        }
        if(flag){
            return new ReturnJsonData(DataCodeUtil.SUCCESS,"删除数据成功",null);
        }else {
            return new ReturnJsonData(DataCodeUtil.DELETE_DATABASE,"删除数据失败",null);
        }
    }

    /**
     * 获取地址详情
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getAddressInfo(String id) throws Exception {
        Address address = mongoComDAO.executeForObjectById(id, Address.class);
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",address);
    }

    /**
     * 选择默认地址
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData seleteAddress(Map<String, Object> map) throws Exception {
        String customerId = MapUtils.getString(map, "customerId", "");
        String addressId = MapUtils.getString(map, "addressId", "");
        map.clear();
        map.put("customerId",customerId);
        map.put("useType",1);
        Address address = mongoComDAO.executeForObjectByCon(map, Address.class);
        if(address != null){
            address.setUseType(0);
            int i = mongoComDAO.executeUpdate(address);
            if(i > 0){
                Address returnAddress = mongoComDAO.executeForObjectById(addressId, Address.class);
                if(returnAddress != null){
                    returnAddress.setUseType(1);
                    int flag = mongoComDAO.executeUpdate(returnAddress);
                    if(flag > 0){
                        return new ReturnJsonData(DataCodeUtil.SUCCESS,"选择默认地址成功",null);
                    }
                }

            }
        }
        return new ReturnJsonData(DataCodeUtil.UPDATE_DATABASE,"选择默认地址失败",null);
    }

    /**
     *  获取默认地址或者地址列表
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public ReturnJsonData getAddressList(Map<String, Object> map) throws Exception {
        String customerId = MapUtils.getString(map, "customerId", "");
        String useType = MapUtils.getString(map, "useType", "");
        map.clear();
        //用户ID
        if(StringUtils.isNotEmpty(customerId)){
            map.put("customerId",Integer.valueOf(customerId));
        }
        //是否选择默认地址
        if(StringUtils.isNotEmpty(useType)){
            map.put("useType",Integer.valueOf(useType));
        }
        List<Address> addressList = mongoComDAO.executeForObjectList(map, Address.class);
        return new ReturnJsonData(DataCodeUtil.SUCCESS,"获取数据成功",addressList);
    }
}
