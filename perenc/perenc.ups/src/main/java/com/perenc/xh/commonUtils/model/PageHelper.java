package com.perenc.xh.commonUtils.model;

import java.io.Serializable;

/**
 * easyui分页Model
 * 
 * @author cjp
 * 
 */
@SuppressWarnings({ "serial", "unused" })
public class PageHelper implements Serializable {

	private int page = 1;// 当前页
	private int rows = 20;// 每页显示记录数
	private String sort = null;// 排序字段名
	private String order = "asc";// 按什么排序(asc,desc)

	private int start = 0;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getStart() {
		return (page - 1) * rows;
	}

	public void setStart(int start) {
		this.start = start;
	}

}
