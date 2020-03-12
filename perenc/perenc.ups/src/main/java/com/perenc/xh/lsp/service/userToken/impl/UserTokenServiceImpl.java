package com.perenc.xh.lsp.service.userToken.impl;


import com.perenc.xh.commonUtils.DBRelevant.DBUtil;
import com.perenc.xh.commonUtils.DBRelevant.InsertParam;
import com.perenc.xh.commonUtils.DBRelevant.QueryParam;
import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.tcUtil.ToolUtil;
import com.perenc.xh.lsp.dao.userToken.UserTokenDao;
import com.perenc.xh.lsp.entity.userToken.UserToken;
import com.perenc.xh.lsp.service.userToken.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("userTokenService")
@Transactional(rollbackFor = Exception.class)
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired(required = false)
    private UserTokenDao userTokenDao;


    @Override
    public String updateUsrToken(Integer userId,Integer sellerId, Integer type, String deviceToken) {
        UserToken userToken = selectUsrTokenBySellerId(sellerId);
        int num = 0;
        String token = ToolUtil.getToken();
        if (null != userToken) {
            userToken.setType(type);
            userToken.setToken(token);
            if(type != 1){
                userToken.setDeviceToken(deviceToken);
            }
            userToken.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            num = userTokenDao.update(DBUtil.toUpdateParam(userToken, "id"));
        } else {
            userToken = new UserToken();
            userToken.setSellerId(sellerId);
            userToken.setType(type);
            userToken.setToken(token);
            userToken.setDeviceToken(deviceToken);
            userToken.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            InsertParam insertParam = DBUtil.toInsertParam(userToken);
            num = userTokenDao.add(insertParam);
        }
        return token;
    }

    @Override
    public String updateUsrTokenByselerUserId(Integer userId,Integer sellerId,Integer sellerUserId, Integer type, String deviceToken) {
        UserToken userToken = selectUsrTokenBySellerUserId(sellerUserId);
        int num = 0;
        String token = ToolUtil.getToken();
        if (null != userToken) {
            userToken.setType(type);
            userToken.setToken(token);
            if(type != 1){
                userToken.setDeviceToken(deviceToken);
            }
            userToken.setUpdateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            num = userTokenDao.update(DBUtil.toUpdateParam(userToken, "id"));
        } else {
            userToken = new UserToken();
            userToken.setSellerId(sellerId);
            userToken.setSellerUserId(sellerUserId);
            userToken.setType(type);
            userToken.setToken(token);
            userToken.setDeviceToken(deviceToken);
            userToken.setCreateTime(DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSS));
            InsertParam insertParam = DBUtil.toInsertParam(userToken);
            num = userTokenDao.add(insertParam);
        }
        return token;
    }

    @Override
    public UserToken selectUsrTokenBySellerId(Integer sellerId) {
        QueryParam paramsle = new QueryParam();
        paramsle.put("seller_id",sellerId);
        return userTokenDao.getOne(paramsle);
    }

    @Override
    public UserToken selectUsrTokenBySellerUserId(Integer sellerUserId) {
        QueryParam paramsle = new QueryParam();
        paramsle.put("seller_user_id",sellerUserId);
        return userTokenDao.getOne(paramsle);
    }

    @Override
    public boolean checkToken(Integer userId,Integer sellerId,Integer sellerUserId, String token) {
        QueryParam paramsle = new QueryParam();
        paramsle.put("seller_user_id",sellerUserId);
        paramsle.put("token",token);
        return userTokenDao.list(paramsle).size() > 0;
    }



}
