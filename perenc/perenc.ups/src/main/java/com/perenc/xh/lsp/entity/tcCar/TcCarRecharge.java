package com.perenc.xh.lsp.entity.tcCar;

import javax.persistence.Table;


@Table(name="tc_car_recharge")
public class TcCarRecharge implements java.io.Serializable{

	private static final long serialVersionUID = -2100225783595311851L;
	
	//columns START



	/**
     * 序号       db_column: id  
     * 
     */
	private String id;
	
    /**
     * 充值金额
     * @NotNull 
     */	
	private Integer oldPrice;


	/**
	 * 送金额
	 * @NotNull
	 */
	private Integer getPrice;

	/**
	 * 备注
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(Integer oldPrice) {
		this.oldPrice = oldPrice;
	}

	public Integer getGetPrice() {
		return getPrice;
	}

	public void setGetPrice(Integer getPrice) {
		this.getPrice = getPrice;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}



}

