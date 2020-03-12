package com.perenc.xh.lsp.entity.tcExtend;


import javax.persistence.Table;


@Table(name="tc_extend_coupon")
public class
TcExtendCoupon implements java.io.Serializable{

	private static final long serialVersionUID = -6663130261421279192L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;


	/**
	 * 来源类型 1=商家发放；2=活动发放;3:积分兑换;4:会议、宴会       db_column: type
	 *
	 */
	private Integer type;
	
    /**
	 * 优惠券ID 关联tc_coupon表的id       db_column: coupon_id
	 * @NotNull
	 */
	private String couponId;
	//停车券小时数
	private int couponDuration;
	//停车优惠券金额
	private int couponAmount;
	
    /**
     * 客户扩展ID 关联usr_extend表的id       db_column: extend_id  
     * @NotNull 
     */	
	private Integer extendId;
	
    /**
     * 商家ID  关联tc_seller表的id       db_column: seller_id  
     * @NotNull 
     */	
	private Integer sellerId;

	/**
	 * 商家用户ID  关联tc_seller_user表的id       db_column: seller_user_id
	 * @NotNull
	 */
	private Integer sellerUserId;


	/**
	 * 商家申请停车券ID  关联tc_seller_applycoupon表的id       db_column: tc_seller_applycoupon_id
	 * @NotNull
	 */
	private String sellerApplycouponId;

	/**
	 * 活动Id
	 * @NotNull
	 */
	private String objectId;

	/**
	 * 捷顺 车辆ID  关联tc_car表的id       db_column: tc_car_id
	 * @NotNull
	 */
	private String carId;


	/**
	 * 捷顺 车牌号       db_column: car_num
	 * @Length(max=100)
	 */
	private String carNum;

	/**
	 * 商家发放停车券时拍照小票     db_column: ticket_img
	 * @NotNull
	 */
	private String ticketImg;

	/**
	 * 发码唯一标识     db_column: scode
	 * @NotNull
	 */
	private String scode;
	
    /**
     * 开始时间       db_column: sdate  
     * @Length(max=50)
     */	
	private String sdate;
	
    /**
     * 结束时间       db_column: edate  
     * @Length(max=50)
     */	
	private String edate;
	
    /**
     * 使用状态 1:待使用，3:已使用，4:已过期 关联sys_dict 表KIND值 等于COUPON_STATUS       db_column: use_status
     * 
     */	
	private Integer useStatus;


	/**
	 * 备注       db_column: remark
	 * @Length(max=500)
	 */
	private String remark;
	
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setCouponId(String value) {
		this.couponId = value;
	}
	
	public String getCouponId() {
		return this.couponId;
	}

	public int getCouponDuration() {
		return couponDuration;
	}

	public void setCouponDuration(int couponDuration) {
		this.couponDuration = couponDuration;
	}

	public int getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(int couponAmount) {
		this.couponAmount = couponAmount;
	}

	public void setExtendId(Integer value) {
		this.extendId = value;
	}
	
	public Integer getExtendId() {
		return this.extendId;
	}
	public void setSellerId(Integer value) {
		this.sellerId = value;
	}
	
	public Integer getSellerId() {
		return this.sellerId;
	}

	public Integer getSellerUserId() {
		return sellerUserId;
	}

	public void setSellerUserId(Integer sellerUserId) {
		this.sellerUserId = sellerUserId;
	}

	public String getSellerApplycouponId() {
		return sellerApplycouponId;
	}

	public void setSellerApplycouponId(String sellerApplycouponId) {
		this.sellerApplycouponId = sellerApplycouponId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getTicketImg() {
		return ticketImg;
	}

	public void setTicketImg(String ticketImg) {
		this.ticketImg = ticketImg;
	}

	public String getScode() {
		return scode;
	}

	public void setScode(String scode) {
		this.scode = scode;
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
	public void setUseStatus(Integer value) {
		this.useStatus = value;
	}
	
	public Integer getUseStatus() {
		return this.useStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

