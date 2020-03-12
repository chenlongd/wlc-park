package com.perenc.xh.commonUtils.model.fcArea;

import java.io.Serializable;
import java.util.List;

public class FcAreaData implements Serializable {


    //名称
    private String name;

    //栋数组
    private List<FcTungData> ld;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FcTungData> getLd() {
        return ld;
    }

    public void setLd(List<FcTungData> ld) {
        this.ld = ld;
    }



}
