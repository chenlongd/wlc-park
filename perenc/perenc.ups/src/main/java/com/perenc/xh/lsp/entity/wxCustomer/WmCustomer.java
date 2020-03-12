package com.perenc.xh.lsp.entity.wxCustomer;

import lombok.Data;

import java.io.Serializable;

@Data
public class WmCustomer implements Serializable{

    private static final long serialVersionUID = -8702756010317174665L;

    private Integer id;
    //微信ID
    private String openId;
    //微信昵称
    private String nickName;
    //微信头像
    private String headImgUrl;
    //性别
    private Integer sex;
    //国家
    private String country;
    //省
    private String province;
    //城市
    private String city;
    //电话号码
    private String phone;
    //真实姓名
    private String userName;
    //创建时间
    private String createTime;
    //更新时间
    private String updateTime;
    //是否启用 1=可以；0=不可用
    private int flag = 1;
    // 获取方式 1：微信小程序，2：公众号
    private Integer registerMode;
    // 是否可用socket :1=可以；0=不可用
    private int isUseWebSocket;
    //余额
    private double balance;
    //积分
    private int integral;
    //会员等级
    private String memberLevelId;
    //密码
    private String password;
    //属于类型
    private int belongToType;

}
