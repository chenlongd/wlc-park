package com.perenc.xh.commonUtils.model;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 树模型
 * @Author xiaobai
 * @Date 2019/2/23 10:35
 **/
public class Tree implements Serializable {

	private static final long serialVersionUID = 1671414335279652251L;

	public Tree() {
		// TODO Auto-generated constructor stub
	}
	
	public Tree(String id, String text, String iconCls) {
		this.id = id;
		this.text = text;
		this.iconCls = iconCls;
	}
	
	
	private String id;
	private String text;
	private String state = "open";// open,closed
	private boolean checked = false;
	private Object attributes;
	private List<Tree> children;
	private String iconCls;
	private String pid;
	private int sort;
	private String status;
	
	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Object getAttributes() {
		return attributes;
	}

	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}

	public List<Tree> getChildren() {
		return children;
	}

	public void setChildren(List<Tree> children) {
		this.children = children;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

