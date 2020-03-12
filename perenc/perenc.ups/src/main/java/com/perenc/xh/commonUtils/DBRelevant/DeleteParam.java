package com.perenc.xh.commonUtils.DBRelevant;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用查询参数
 * @author Edward
 */
public class DeleteParam {
 
	private String manual;//人工sql条件，用于复杂SQL情形 
	private String resultCols;//查询时自定义的结果字段
	
	private List<QueryCondition> conditions = new ArrayList<QueryCondition>();//动态添加的条件
	
	   
	private Boolean includeDel = false;//是否关联查询
	
	private String keyword;
	
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public DeleteParam(){
		
	}
	
	  

	/**
	 * 添加动态条件，如 "age" ">" 18
	 * @param property 属性名
	 * @param operation 操作符
	 * @param value 值
	 */
	public DeleteParam addCondition(String property, String operation, Object value) {
//		if(!property.contains(".")) {
//			property = "0"+property;
//		}
		
		conditions.add(new QueryCondition(property, operation, value));
		return this;
	}
	
	public DeleteParam put(String property, Object value) {
		return addCondition(property, "=", value);
	}

	public Boolean getIncludeDel() {
		return includeDel;
	}

	public DeleteParam setIncludeDel(Boolean includeDel) {
		this.includeDel = includeDel;
		return this;
	}
	
	public String getManual() {
		return manual;
	}
	public DeleteParam setManual(String manual) {
		this.manual = manual;
		return this;
	}
	public List<QueryCondition> getConditions() {
		return conditions;
	}
	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}
 
	public String getResultCols() {
		return resultCols;
	}
	public DeleteParam setResultCols(String resultCols) {
		StringBuffer sb = new StringBuffer();
		if(resultCols != null && !"".equals(resultCols)) {
			String[] arr = resultCols.split(",");
			for(int i=0; i<arr.length; i++) {
				String str = arr[i];
 
				if(i != 0) {
					sb.append(",");
				}
				sb.append(str);
			}
		}
		this.resultCols = sb.toString();
		return this;
	}
}
