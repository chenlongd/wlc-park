package com.perenc.xh.lsp.entity.tcIntegral;

import javax.persistence.Table;


@Table(name="tc_integral_activty")
public class TcIntegralActivty implements java.io.Serializable{

	private static final long serialVersionUID = -8251525494718472649L;
	
	//columns START


	/**
     * 序号       db_column: id  
     * 
     */
	private String id;


	/**
	 * 类型（1：首次注册送停车券，2：消费金额换取积分,3:充值金额换取积分）       db_column: type
	 *
	 */
	private Integer type;

	/**
	 * 活动名称       db_column: name
	 * @Length(max=500)
	 */
	private String name;

	/**
	 * 活动简介       db_column: descp
	 * @Length(max=500)
	 */
	private String descp;


	/**
	 * 兑换比例       db_column: ratio
	 * @NotNull
	 */
	private Integer ratio;

	/**
	 * 可兑换积分       db_column: number
	 * @NotNull
	 */
	private Integer number;


    /**
     * 活动有效期开始时间       db_column: sdate
     * @Length(max=50)
     */	
	private String sdate;
	
    /**
     * 活动有效期结束时间       db_column: edate
     * @Length(max=50)
     */	
	private String edate;

	/**
	 * 优惠券ID 关联tc_coupon表的id       db_column: coupon_id
	 * @NotNull
	 */
	private String couponId;


	/**
	 * 停车券有效期 单位：天       db_column: cdays
	 * @Length(max=50)
	 */
	private Integer cdays;


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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}

	public Integer getRatio() {
		return ratio;
	}

	public void setRatio(Integer ratio) {
		this.ratio = ratio;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public String getEdate() {
		return edate;
	}

	public void setEdate(String edate) {
		this.edate = edate;
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

