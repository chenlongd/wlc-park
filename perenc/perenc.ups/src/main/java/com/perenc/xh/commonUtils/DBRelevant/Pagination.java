package com.perenc.xh.commonUtils.DBRelevant;

import java.util.List;

/**
 * 分页查询结果
 * @author Edward
 */
public class Pagination {
	private Integer total;
	private List<?> data;
	private List<?> rows;
	private String type = "1";
	
	public Pagination(Integer total, List<?> data) {
		this.total = total;
		this.rows = this.data = data;
	} 
	
	public List<?> getRows() {
		return rows;
	}


	public void setRows(List<?> rows) {
		this.rows = rows;
	}


	public String getType() {
		return type;
	}
 

	public void setType(String type) {
		this.type = type;
	}



	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

 
	
}
