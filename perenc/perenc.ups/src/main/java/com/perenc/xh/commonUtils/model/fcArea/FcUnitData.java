package com.perenc.xh.commonUtils.model.fcArea;

import java.io.Serializable;
import java.util.List;

public class FcUnitData implements Serializable {


    //名称
    private String name;


    //栋数组
    private List<FcHouseData> lc;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FcHouseData> getLc() {
        return lc;
    }

    public void setLc(List<FcHouseData> lc) {
        this.lc = lc;
    }


}
