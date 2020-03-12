package com.perenc.xh.commonUtils.DBRelevant;

import java.io.Serializable;
import java.util.List;

public class InsertParam implements Serializable{
	private static final long serialVersionUID = 3332246276875933673L;
	private List<Property> properties;
	private String id;

	public InsertParam() {
	}
	public InsertParam(List<Property> properties, String id) {
		this.properties = properties;
		this.id = id;
	}

	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
