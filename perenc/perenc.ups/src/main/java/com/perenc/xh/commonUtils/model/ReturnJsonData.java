package com.perenc.xh.commonUtils.model;

import java.io.Serializable;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/4/9 17:10
 **/
public class ReturnJsonData implements Serializable {

    private static final long serialVersionUID = 1254483054765649630L;
    //编码
    private int code;
    //消息
    private String msg;
    //数据
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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

    public ReturnJsonData(int code,String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ReturnJsonData() {

    }
}
