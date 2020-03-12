package com.perenc.xh.commonUtils.model.fcArea;

import java.io.Serializable;

public class FcHouseData implements Serializable {


    //名称
    private String name;


    //栋数组
    private String [] fh;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String[] getFh() {
        return fh;
    }

    public void setFh(String[] fh) {
        this.fh = fh;
    }


}
