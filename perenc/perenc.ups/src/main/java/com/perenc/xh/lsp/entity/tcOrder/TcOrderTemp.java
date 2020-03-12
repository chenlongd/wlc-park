package com.perenc.xh.lsp.entity.tcOrder;

import javax.persistence.Table;


@Table(name="tc_order_temp")
public class TcOrderTemp implements java.io.Serializable{



	private static final long serialVersionUID = 6160469888252426433L;
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private Integer id;
	
    /**
     * 订单号       db_column: order_no  
     * @Length(max=100)
     */	
	private String orderNo;
	
    /**
     * 用户ID 关联usr_extend表的id       db_column: extend_id  
     * @NotNull 
     */	
	private Integer extendId;
	
    /**
     * 优惠券ID 关联tc_coupon表的id       db_column: coupon_id  
     * @NotNull 
     */	
	private String couponId;
	
    /**
     * 停车场ID 关联tc_parklot表的id       db_column: parklot_id  
     * @NotNull 
     */	
	private String parklotId;
	
    /**
     * 车辆ID        db_column: car_id  
     * @NotNull 
     */	
	private String carId;
	
    /**
     * 车牌号       db_column: car_num  
     * @NotBlank @Length(max=100)
     */	
	private String carNum;
	
    /**
     * 支付金额       db_column: pay_price  
     * @NotNull 
     */	
	private Integer payPrice;
	
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

	public void setId(Integer value) {
		this.id = value;
	}
	
	public Integer getId() {
		return this.id;
	}
	public void setOrderNo(String value) {
		this.orderNo = value;
	}
	
	public String getOrderNo() {
		return this.orderNo;
	}
	public void setExtendId(Integer value) {
		this.extendId = value;
	}
	
	public Integer getExtendId() {
		return this.extendId;
	}
	public void setCouponId(String value) {
		this.couponId = value;
	}
	
	public String getCouponId() {
		return this.couponId;
	}
	public void setParklotId(String value) {
		this.parklotId = value;
	}
	
	public String getParklotId() {
		return this.parklotId;
	}
	public void setCarId(String value) {
		this.carId = value;
	}
	
	public String getCarId() {
		return this.carId;
	}
	public void setCarNum(String value) {
		this.carNum = value;
	}
	
	public String getCarNum() {
		return this.carNum;
	}
	public void setPayPrice(Integer value) {
		this.payPrice = value;
	}
	
	public Integer getPayPrice() {
		return this.payPrice;
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

