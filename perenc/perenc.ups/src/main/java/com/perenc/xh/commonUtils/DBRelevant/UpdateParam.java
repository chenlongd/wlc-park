package com.perenc.xh.commonUtils.DBRelevant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态update参数
 * @author Edward
 */
public class UpdateParam implements Serializable{
	private static final long serialVersionUID = 4158676644310255949L;
	private List<Property> properties;//需更新的属性S
	private List<Property> priKeys;//主键S
	
	private String manual;//手动条件
	
	private String updateManual;//手动更新属性
	 
	public UpdateParam(){
		this.properties = new ArrayList<Property>();
	}
	 
	public UpdateParam put(String name, Object value){
		this.properties.add(new Property(name, value));
		return this;
	} 
	 
	public String getUpdateManual() {
		return updateManual;
	}
	public void setUpdateManual(String updateManual) {
		this.updateManual = updateManual;
	}
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	public List<Property> getPriKeys() {
		return priKeys;
	}
	public void setPriKeys(List<Property> priKeys) {
		this.priKeys = priKeys;
	}
	public String getManual() {
		return manual;
	}
	public UpdateParam setConditionManual(String manual) {
		this.manual = manual;
		return this;
	}
	
	
}