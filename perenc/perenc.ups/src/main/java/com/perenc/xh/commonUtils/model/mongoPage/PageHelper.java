package com.perenc.xh.commonUtils.model.mongoPage;

import java.io.Serializable;

/**
 * 
 * 功能描述：表格分页实体模型
 *
 */
public class PageHelper implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4310164405268350373L;

    public static final int pageSize = 20;

	// 当前页
	private int page = 1;
	// 每页显示记录数
	private int rows = 20;
	// 排序字段名
	private String sort = null;
	// 按什么排序(asc,desc)
	private String order = "asc";

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
	}
}
