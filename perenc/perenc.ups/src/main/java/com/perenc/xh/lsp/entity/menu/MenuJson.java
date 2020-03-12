package com.perenc.xh.lsp.entity.menu;

import java.io.Serializable;
import java.util.List;

public class MenuJson implements Serializable {
    private static final long serialVersionUID = -2891165337345996767L;

    private String menuName;

    private String menuImage;

    private String menuUrl;

    private Integer selected ;

    private Integer menuType;

    private Integer menuID;

    private List<MenuJson> childlist;

    private List<MenuJson> buttonList;

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public Integer getMenuID() {
        return menuID;
    }

    public void setMenuID(Integer menuID) {
        this.menuID = menuID;
    }

    public List<MenuJson> getChildlist() {
        return childlist;
    }

    public void setChildlist(List<MenuJson> childlist) {
        this.childlist = childlist;
    }

    public List<MenuJson> getButtonList() {
        return buttonList;
    }

    public void setButtonList(List<MenuJson> buttonList) {
        this.buttonList = buttonList;
    }
}
