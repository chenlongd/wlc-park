package com.perenc.xh.lsp.entity.weChatInfo;


import java.io.Serializable;

/**
 * @Description 微信商户信息
 * @Author xiaobai
 * @Date 2019/5/18 17:05
 **/
public class WeChatInfo implements Serializable {
    private static final long serialVersionUID = -8185919386311212761L;

    private String id;
    //微信名称
    private String weCahtName;
    //商家账号ID
    private int mchAccountId;
    //小程序或者微信公众号APPID
    private String appId;
    //小程序或者微信公招APP秘钥
    private String appSecret;
    //商户ID
    private String mchId;
    //商户秘钥
    private String merchantSecretKey;
    //通知支付回调地址
    private  String notifyUrl;
    //退款支付回调地址
    private  String refundNotifyUrl;
    //API证书文件
    private String apiclientCertP12;
    //API证书文件
    private String apiclientCertPem;
    //API证书文件
    private String apiclientKeyPem;
    //微信公众号检测文件
    private String wxPublicCheckFile;
    //小程序校验文件
    private String miniCheckFile;
    //类型 1=公众号;2=小程序
    private int type;
    //是否启用 1=小程启用;2=公众号启用;0=未启用
    private int useType;
    //状态 0=使用;1=未使用
    private int status;
    //创建时间
    private String createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeCahtName() {
        return weCahtName;
    }

    public void setWeCahtName(String weCahtName) {
        this.weCahtName = weCahtName;
    }

    public int getMchAccountId() {
        return mchAccountId;
    }

    public void setMchAccountId(int mchAccountId) {
        this.mchAccountId = mchAccountId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMerchantSecretKey() {
        return merchantSecretKey;
    }

    public void setMerchantSecretKey(String merchantSecretKey) {
        this.merchantSecretKey = merchantSecretKey;
    }

    public String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }

    public void setRefundNotifyUrl(String refundNotifyUrl) {
        this.refundNotifyUrl = refundNotifyUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getApiclientCertP12() {
        return apiclientCertP12;
    }

    public void setApiclientCertP12(String apiclientCertP12) {
        this.apiclientCertP12 = apiclientCertP12;
    }

    public String getApiclientCertPem() {
        return apiclientCertPem;
    }

    public void setApiclientCertPem(String apiclientCertPem) {
        this.apiclientCertPem = apiclientCertPem;
    }

    public String getApiclientKeyPem() {
        return apiclientKeyPem;
    }

    public void setApiclientKeyPem(String apiclientKeyPem) {
        this.apiclientKeyPem = apiclientKeyPem;
    }

    public String getWxPublicCheckFile() {
        return wxPublicCheckFile;
    }

    public void setWxPublicCheckFile(String wxPublicCheckFile) {
        this.wxPublicCheckFile = wxPublicCheckFile;
    }

    public String getMiniCheckFile() {
        return miniCheckFile;
    }

    public void setMiniCheckFile(String miniCheckFile) {
        this.miniCheckFile = miniCheckFile;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
