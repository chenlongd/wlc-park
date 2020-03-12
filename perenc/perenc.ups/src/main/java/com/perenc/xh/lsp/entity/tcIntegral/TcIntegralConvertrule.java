package com.perenc.xh.lsp.entity.tcIntegral;

import javax.persistence.Table;


@Table(name="tc_integral_convertrule")
public class TcIntegralConvertrule implements java.io.Serializable{

	private static final long serialVersionUID = 1221168030163999266L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;

	/**
	 * 优惠券ID 关联tc_coupon表的id       db_column: coupon_id
	 * @NotNull
	 */
	private String couponId;


	/**
	 * 停车券有效期 单位：天       db_column: cdays
	 */
	private Integer cdays;


	/**
	 * 兑换简介       db_column: descp
	 * @Length(max=500)
	 */
	private String descp;
	
    /**
     * 规则名称       db_column: name  
     * @Length(max=500)
     */	
	private String name;
	
    /**
     * 兑换比例       db_column: ratio  
     * @NotNull 
     */	
	private Integer ratio;
	
    /**
     * 兑换公式       db_column: formula  
     * @Length(max=500)
     */	
	private String formula;
	
    /**
     * 可兑换数量       db_column: number  
     * @NotNull 
     */	
	private Integer number;
	
    /**
     * 兑换限制数量/人       db_column: limit_number  
     * @NotNull 
     */	
	private Integer limitNumber;
	
    /**
     * 有效期开始时间       db_column: sdate  
     * @Length(max=50)
     */	
	private String sdate;
	
    /**
     * 有效期结束时间       db_column: edate  
     * @Length(max=50)
     */	
	private String edate;
	
    /**
     * 备注       db_column: remark  
     * @Length(max=500)
     */	
	private String remark;

	/**
	 * 是否启用 1：是,2否      db_column: isEnabled
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


	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}


	public Integer getCdays() {
		return cdays;
	}

	public void setCdays(Integer cdays) {
		this.cdays = cdays;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}
	public void setName(String value) {
		this.name = value;
	}
	
	public String getName() {
		return this.name;
	}
	public void setRatio(Integer value) {
		this.ratio = value;
	}
	
	public Integer getRatio() {
		return this.ratio;
	}
	public void setFormula(String value) {
		this.formula = value;
	}
	
	public String getFormula() {
		return this.formula;
	}
	public void setNumber(Integer value) {
		this.number = value;
	}
	
	public Integer getNumber() {
		return this.number;
	}
	public void setLimitNumber(Integer value) {
		this.limitNumber = value;
	}
	
	public Integer getLimitNumber() {
		return this.limitNumber;
	}
	public void setSdate(String value) {
		this.sdate = value;
	}
	
	public String getSdate() {
		return this.sdate;
	}
	public void setEdate(String value) {
		this.edate = value;
	}
	
	public String getEdate() {
		return this.edate;
	}
	public void setRemark(String value) {
		this.remark = value;
	}
	
	public String getRemark() {
		return this.remark;
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

