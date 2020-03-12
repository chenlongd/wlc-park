package com.perenc.xh.lsp.entity.tcVip;

import javax.persistence.Table;


@Table(name="tc_vip_car")
public class TcVipCar implements java.io.Serializable{

	private static final long serialVersionUID = 8262321349649596964L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;

	/**
	 * 用户ID 关联usr_extend表的id       db_column: extend_id
	 * @NotNull
	 */
	private Integer extendId;

	/**
	 * 定单ID 关联费用定单表的id       db_column: order_id
	 * @NotNull
	 */
	private Integer orderId;

	/**
     * 会员ID 关联tc_vip表的id       db_column: vip_id  
     * @NotNull 
     */	
	private String vipId;
	
    /**
     * 车辆ID 关联tc_car表的id       db_column: car_id  
     * @NotNull 
     */	
	private String carId;

	/**
	 * 车牌号       db_column: car_num
	 * @Length(max=100)
	 */
	private String carNum;


	/**
	 * 有效期月数（1月，3月，6月，12月）关联sys_dict 表KIND值 等于VIP_NUMBER       db_column: number
	 * @NotNull
	 */
	private Integer number;
	
    /**
     * 会员开始时间       db_column: sdate  
     * @Length(max=50)
     */	
	private String sdate;
	
    /**
     * 会员结束时间       db_column: edate  
     * @Length(max=50)
     */	
	private String edate;

	/**
	 * 使用状态 1:待使用，2:已过期 关联sys_dict 表KIND值 等于COUPON_STATUS       db_column: use_status
	 *
	 */
	private Integer useStatus;

	/**
	 * 使用时间       db_column: use_time
	 * @Length(max=50)
	 */
	private String useTime;


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

	public Integer getExtendId() {
		return extendId;
	}

	public void setExtendId(Integer extendId) {
		this.extendId = extendId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public void setVipId(String value) {
		this.vipId = value;
	}
	
	public String getVipId() {
		return this.vipId;
	}
	public void setCarId(String value) {
		this.carId = value;
	}
	
	public String getCarId() {
		return this.carId;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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

	public Integer getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(Integer useStatus) {
		this.useStatus = useStatus;
	}

	public String getUseTime() {
		return useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
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

