package com.perenc.xh.lsp.entity.tcCar;

import javax.persistence.Table;


@Table(name="tc_car_paycheck")
public class TcCarPaycheck implements java.io.Serializable{

	private static final long serialVersionUID = -7533855103624894719L;
	
	//columns START


	/**
     * 序号       db_column: id  
     * 
     */
	private String id;
	
    /**
     * 停车场ID 关联tc_parklot表的id       db_column: parklot_id  
     * @NotNull 
     */	
	private String parklotId;


	/**
	 * 捷顺车场ID
	 * @NotNull
	 */
	private String parkId;

	/**
	 * 支付编号
	 */
	private String payNo;

	/**
	 * 入场设备名称
	 */
	private String payType;

	/**
	 * 缴费时间
	 */
	private String chargeTime;


	/**
     * 支付交易 id
     * @NotNull 
     */	
	private String transactionId;
	

	/**
	 * 支付状态
	 *
	 */
	private Integer payStatus;
	
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

	public String getParklotId() {
		return parklotId;
	}

	public void setParklotId(String parklotId) {
		this.parklotId = parklotId;
	}

	public String getParkId() {
		return parkId;
	}

	public void setParkId(String parkId) {
		this.parkId = parkId;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getChargeTime() {
		return chargeTime;
	}

	public void setChargeTime(String chargeTime) {
		this.chargeTime = chargeTime;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
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

