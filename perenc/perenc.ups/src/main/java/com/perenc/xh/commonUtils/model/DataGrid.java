package com.perenc.xh.commonUtils.model;

import java.util.ArrayList;
import java.util.List;

/**
 * easyui的datagrid向后台传递参数使用的model
 * 
 * @author cjp
 * 
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class DataGrid implements java.io.Serializable {

	private long total = 0L;

	private List rows = new ArrayList();

	private List footer;

	private int pageNo;

	private int pageSize;

	public List getFooter() {
		return footer;
	}

	public void setFooter(List footer) {
		this.footer = footer;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
