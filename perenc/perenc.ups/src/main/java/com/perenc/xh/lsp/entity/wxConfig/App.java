package com.perenc.xh.lsp.entity.wxConfig;


import java.io.Serializable;
import java.util.Date;

public class App implements Serializable{

	private static final long serialVersionUID = -5744094033379300106L;

	private String id;
	private Long uuid;
	private String name;
	private String appId;
	private String appSecret;
	private String appMchId;
	private String appMchKey;
	
	private String appRoot;
	
	private Integer state;
	private Integer delflag;
	private Date createTime;//创建时间
	
	public String getAppRoot() {
		return appRoot;
	}
	public void setAppRoot(String appRoot) {
		this.appRoot = appRoot;
	}
	public Long getUuid() {
		return uuid;
	}
	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getAppMchId() {
		return appMchId;
	}
	public void setAppMchId(String appMchId) {
		this.appMchId = appMchId;
	}
	public String getAppMchKey() {
		return appMchKey;
	}
	public void setAppMchKey(String appMchKey) {
		this.appMchKey = appMchKey;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getDelflag() {
		return delflag;
	}
	public void setDelflag(Integer delflag) {
		this.delflag = delflag;
	}
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public Date getCreateTime() { return createTime; }
	public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
