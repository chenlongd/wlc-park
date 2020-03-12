package com.perenc.xh.commonUtils.DBRelevant;

import com.perenc.xh.commonUtils.model.Paging;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用查询参数
 * @author Edward
 */
public class QueryParam implements Serializable{
	private static final long serialVersionUID = 5597604273743288917L;
	private Integer pageNum = 1;//分页参数-页码 从1 开始
	private Integer begin = 0;//分页参数-记录起始位置
	private Integer pageSize = 100;//分页参数-页大小
	private String manual;//人工sql条件，用于复杂SQL情形
	private String order = "id desc";//排序方式
	private String resultCols;//查询时自定义的结果字段

	private String groupby;
	
	private List<QueryCondition> conditions = new ArrayList<QueryCondition>();//动态添加的条件

	private Boolean relate = false;//是否关联查询
	
	private Boolean includeDel = false;//是否关联查询

	private Boolean queryall = false;//是否查询所有数据
	
	private String keyword;
	
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public QueryParam(){
		
	}
	 
	

	public String getGroupby() {
		return groupby;
	}

	public QueryParam setGroupby(String groupby) {
		this.groupby = groupby;
		return this;
	}

	public QueryParam(Paging paging){
		this.setPageNum(paging.getCurPage());
		this.setPageSize(paging.getPageSize());
		this.setBegin((paging.getCurPage()-1)*paging.getPageSize());
		if (StringUtils.isNotBlank(paging.getGroupByField())) {
			this.setOrder(paging.getGroupByField());
//			if (StringUtils.isNotBlank(paging.get)) {
//				paging.setOrder(paging.getGroupByField() + " " + order);
//			}
		}

	}
	
	/**
	 * 计算begin
	 */
	public void figureBegin(Integer total) {
		Integer pn = this.pageNum;
		Integer ps = this.pageSize;
		
		Integer maxPn = (total-1)/ps + 1;
		pn = (pn > maxPn) ? maxPn : pn;
		this.begin = (pn - 1) * ps;
	}

	/**
	 * 添加动态条件，如 "age" ">" 18
	 * @param property 属性名
	 * @param operation 操作符
	 * @param value 值
	 */
	public QueryParam addCondition(String property, String operation, Object value) {
	
		if(value!=null&&StringUtils.isNotBlank(String.valueOf(value))){
			if(!property.contains(".")) {
				property = "t."+property;
			}
			conditions.add(new QueryCondition(property, operation, value));
		}
		
		return this;
	}
	
	public QueryParam put(String property, Object value) {
		
		return addCondition(property, "=", value);
	}

	public Boolean getIncludeDel() {
		return includeDel;
	}

	public QueryParam setIncludeDel(Boolean includeDel) {
		this.includeDel = includeDel;
		return this;
	}

	public Boolean getQueryall() {
		return queryall;
	}

	public void setQueryall(Boolean queryall) {
		this.queryall = queryall;
	}

	//getters and setters
	public QueryParam setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public QueryParam setBegin(Integer begin) {
		this.begin = begin;
		return this;
	}
	public Integer getBegin() {
		return (pageNum - 1) * pageSize;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public QueryParam setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
		return this;
	}
	public String getManual() {
		return manual;
	}
	public QueryParam setManual(String manual) {
		this.manual = manual;
		return this;
	}
	public List<QueryCondition> getConditions() {
		return conditions;
	}
	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}
	public String getOrder() {
		return order;
	}
	public QueryParam setOrder(String order) {
		this.order = order;
		return this;
	}
	public Boolean getRelate() {
		return relate;
	}
	public QueryParam setRelate(Boolean relate) {
		this.relate = relate;
		return this;
	}
	public String getResultCols() {
		return resultCols;
	}
	public QueryParam setResultCols(String resultCols) {
//		StringBuffer sb = new StringBuffer();
//		if(resultCols != null && !"".equals(resultCols)) {
//			String[] arr = resultCols.split(",");
//			for(int i=0; i<arr.length; i++) {
//				String str = arr[i];
//				if(!str.contains(".")) {
//					str = "t."+str;
//				}
//				if(i != 0) {
//					sb.append(",");
//				}
//				sb.append(str);
//			}
//		}
		this.resultCols =   resultCols;//resultCols.toString();
		return this;
	}


}
