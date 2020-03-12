package com.perenc.xh.commonUtils.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PhoneReturnJson implements Serializable {

	private static final long serialVersionUID = 6157371120334177206L;
	/**
	 * 成功失败 success , failure
	 */
	private boolean success = false;

	/**
	 * 消息
	 */
	private String msg = "";

	/**
	 * 内容
	 */
	private Map<String, Object> context = new HashMap<String, Object>();

	public PhoneReturnJson() {
	}

	public  PhoneReturnJson(boolean success, String msg, Map<String, Object> context) {
		this.success = success;
		this.msg = msg;
		this.context = context;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}
}
