package com.perenc.xh.commonUtils.model;

import java.io.Serializable;

/**
 * 后台向前台返回的Json对象
 * 
 * @author cjp
 * 
 */
@SuppressWarnings("serial")
public class Json implements Serializable {

	public Json() {
	}

	public Json(boolean success, String msg, Object context) {
		super();
		this.success = success;
		this.msg = msg;
		this.context = context;
	}

	private boolean success = false;

	private String msg = "";

	private Object context = null;

	private Object obj = null;

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

	public Object getContext() {
		return context;
	}

	public void setContext(Object context) {
		this.context = context;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
}
