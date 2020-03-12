package com.perenc.xh.lsp.entity.miniapp;

import com.google.gson.Gson;
import lombok.Data;

import java.io.Serializable;

@Data
public class WxMaUserInfo implements Serializable {
  private static final long serialVersionUID = 6719822331555402137L;

  private String openId;
  private String nickName;
  private String gender;
  private String language;
  private String city;
  private String province;
  private String country;
  private String avatarUrl;
  private String unionId;
  private String phoneNumber;
  private String purePhoneNumber;
  private String countryCode;
  private Watermark watermark;


  public static WxMaUserInfo fromJson(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, WxMaUserInfo.class);
  }

  @Data
  public static class Watermark {
    private String timestamp;
    private String appid;

    public String getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(String timestamp) {
      this.timestamp = timestamp;
    }

    public String getAppid() {
      return appid;
    }

    public void setAppid(String appid) {
      this.appid = appid;
    }
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public String getUnionId() {
    return unionId;
  }

  public void setUnionId(String unionId) {
    this.unionId = unionId;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPurePhoneNumber() {
    return purePhoneNumber;
  }

  public void setPurePhoneNumber(String purePhoneNumber) {
    this.purePhoneNumber = purePhoneNumber;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public Watermark getWatermark() {
    return watermark;
  }

  public void setWatermark(Watermark watermark) {
    this.watermark = watermark;
  }



}
