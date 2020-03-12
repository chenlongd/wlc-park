package com.perenc.xh.lsp.service.userToken;


import com.perenc.xh.lsp.entity.userToken.UserToken;

public interface UserTokenService {

    /**
     * 返回
     * @param userId
     * @param type
     * @param deviceToken
     * @return
     */
    String updateUsrToken(Integer userId, Integer sellerId, Integer type, String deviceToken);

    /**
     * 商家用户下更改
     * @param userId
     * @param sellerId
     * @param sellerUserId
     * @param type
     * @param deviceToken
     * @return
     */
    public String updateUsrTokenByselerUserId(Integer userId, Integer sellerId, Integer sellerUserId, Integer type, String deviceToken);


    /**
     * 根据商家Id查询token
     * @param sellerId
     * @return
     */
    public UserToken selectUsrTokenBySellerId(Integer sellerId);

    /**
     * 查询商家用户id
     * @param sellerUserId
     * @return
     */
    public UserToken selectUsrTokenBySellerUserId(Integer sellerUserId);

    /**
     * 验证
     * @param sellerId
     * @param token
     * @return
     */
    public boolean checkToken(Integer userId, Integer sellerId, Integer sellerUserId, String token);






}
