package com.perenc.xh.commonUtils.model;

import java.io.Serializable;

/**
 *捷顺返回结果集
 * code为String
 * msg为String
 **/
public class JsReturnJsonData implements Serializable {

    private static final long serialVersionUID = 1254483054765649630L;
    //编码
    private String code;
    //消息
    private String msg;
    //数据
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public JsReturnJsonData(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsReturnJsonData() {

    }
}
