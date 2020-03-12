package com.perenc.xh.commonUtils.DBRelevant;

import java.io.Serializable;

/**
 * 动态查询条件
 * @author Edward
 */
public class QueryCondition implements Serializable {
	private static final long serialVersionUID = 8280755752340939702L;
	private String property;//属性名
	private String operation;//操作符
	private Object value;//属性值
	
	public QueryCondition(String property, String operation, Object value) {
		this.property = property;
		this.operation = operation;
		this.value = value;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
}
