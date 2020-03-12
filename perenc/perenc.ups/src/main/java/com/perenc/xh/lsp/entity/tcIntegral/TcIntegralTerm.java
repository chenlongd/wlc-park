package com.perenc.xh.lsp.entity.tcIntegral;

import javax.persistence.Table;


@Table(name="tc_integral_term")
public class TcIntegralTerm implements java.io.Serializable{

	private static final long serialVersionUID = 5485347706902558035L;

	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;
	
    /**
     * 标题       db_column: name  
     * @Length(max=500)
     */	
	private String name;
	
    /**
     * 类型（1：每日登录，2：绑定车辆，3：认证车辆，4：完善信息）       db_column: type  
     * 
     */	
	private Integer type;
	
    /**
     * 积分值       db_column: number  
     * 
     */	
	private Integer number;
	
    /**
     * 跳转链接       db_column: url  
     * @Length(max=500)
     */	
	private String url;


	/**
	 * 备注       db_column: remark
	 * @Length(max=500)
	 */
	private String remark;

	/**
	 * 是否启用 1：是,2否      db_column: isEnabled
	 * @Length(max=500)
	 */
	private Integer isEnabled;
	
    /**
     * 记录状态 1：可用;2：不可用 关联sys_dict 表KIND值 等于STATUS       db_column: status  
     * 
     */
	private Integer status;
	
    /**
     * 创建时间       db_column: create_time  
     * @Length(max=50)
     */	
	private String createTime;
	
	//columns END

	public void setId(String value) {
		this.id = value;
	}
	
	public String getId() {
		return this.id;
	}
	public void setName(String value) {
		this.name = value;
	}
	
	public String getName() {
		return this.name;
	}
	public void setType(Integer value) {
		this.type = value;
	}
	
	public Integer getType() {
		return this.type;
	}
	public void setNumber(Integer value) {
		this.number = value;
	}
	
	public Integer getNumber() {
		return this.number;
	}
	public void setUrl(String value) {
		this.url = value;
	}
	
	public String getUrl() {
		return this.url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}

	public void setStatus(Integer value) {
		this.status = value;
	}
	
	public Integer getStatus() {
		return this.status;
	}
	public void setCreateTime(String value) {
		this.createTime = value;
	}
	
	public String getCreateTime() {
		return this.createTime;
	}
}

