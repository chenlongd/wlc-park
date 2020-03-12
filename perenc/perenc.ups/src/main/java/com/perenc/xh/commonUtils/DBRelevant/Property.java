package com.perenc.xh.commonUtils.DBRelevant;

import java.io.Serializable;

/**
 * 对象属性
 * @author Edward
 *
 */
public class Property implements Serializable{
	private static final long serialVersionUID = 5390344930837387520L;
	private String name;//属性名
	private Object value;//属性值

	public Property(String name, Object value) {
		this.name = underscoreName(name);
		this.value = value;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}


	public static String underscoreName(String camelCaseName) {
		StringBuilder result = new StringBuilder();
		if (camelCaseName != null && camelCaseName.length() > 0) {
			result.append(camelCaseName.substring(0, 1).toLowerCase());
			for (int i = 1; i < camelCaseName.length(); i++) {
				char ch = camelCaseName.charAt(i);
				if (Character.isUpperCase(ch)) {
					result.append("_");
					result.append(Character.toLowerCase(ch));
				} else {
					result.append(ch);
				}
			}
		}
		return result.toString();
	}

}
