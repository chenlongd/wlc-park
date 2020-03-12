package com.perenc.xh.lsp.entity.miniapp;

import cn.binarywang.wx.miniapp.util.json.WxMaGsonBuilder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/10/15 10:15
 **/
@Data
public class WxMaPhoneNumberInfo implements Serializable {

    private static final long serialVersionUID = 1387134769822236128L;

    private String phoneNumber;
    private String purePhoneNumber;
    private String countryCode;
    private String openid;
    private Watermark watermark;

    public static WxMaPhoneNumberInfo fromJson(String json) {
        return WxMaGsonBuilder.create().fromJson(json, WxMaPhoneNumberInfo.class);
    }

    public WxMaPhoneNumberInfo() {
    }

    @Data
    public static class Watermark {
        private String timestamp;
        private String appid;

        public Watermark() {
        }

    }
}
