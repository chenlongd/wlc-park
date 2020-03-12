package com.perenc.xh.commonUtils.model.fcArea;

import java.io.Serializable;
import java.util.List;

public class FcTungData implements Serializable {


    //名称
    private String name;

    //栋数组
    private List<FcUnitData> dy;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FcUnitData> getDy() {
        return dy;
    }

    public void setDy(List<FcUnitData> dy) {
        this.dy = dy;
    }

}
