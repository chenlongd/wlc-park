package com.perenc.xh.lsp.entity.tcSeller;

import javax.persistence.Table;


@Table(name="tc_seller_applycoupon")
public class TcSellerApplycoupon implements java.io.Serializable{

	private static final long serialVersionUID = -2682894179668791623L;
	
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
	 * 申请名称       db_column: name
	 */
	private String name;

	/**
	 * 申请类型 1=商家发放；2=婚宴、会议发放 db_column: type
	 *
	 */
	private Integer type;
	
    /**
     * 商家ID 关联tc_seller表的id       db_column: seller_id  
     * @NotNull 
     */	
	private Integer sellerId;

	/**
	 * 商家用户ID 关联tc_seller_user表的id       db_column: seller_user_id
	 * @NotNull
	 */
	private Integer sellerUserId;


	/**
	 * 商家申领ID 关联tc_seller_apply表的id       db_column: seller_apply_id
	 * @NotNull
	 */
	private String sellerApplyId;
	
    /**
     * 张数/小时数       db_column: number  
     * @NotNull 
     */	
	private Integer number;


	/**
	 * 可使用张数/小时数       db_column: knumber
	 * @NotNull
	 */
	private Integer knumber;


	/**
	 * 已发的张数/小时数       db_column: ynumber
	 * @NotNull
	 */
	private Integer ynumber;

	/**
	 * 商家发放停车券时拍照小票     db_column: ticket_img
	 */
	private String ticketImg;

	/**
     * 有效开始时间       db_column: sdate  
     * @Length(max=50)
     */	
	private String sdate;
	
    /**
     * 有效结束时间       db_column: edate  
     * @Length(max=50)
     */	
	private String edate;

	/**
	 * 是否审核通过（1:待审核，2:通过，3:未通过） 关联sys_dict 表KIND值 等于APPLY_STATUS       db_column: is_approval
	 *
	 */
	private Integer isApproval;


	/**
	 * 使用状态 1:待使用，2:被冻结，3:已使用，4:已过期 关联sys_dict 表KIND值 等于COUPON_STATUS       db_column: use_status
	 *
	 */
	private Integer useStatus;


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
	public void setCouponId(String value) {
		this.couponId = value;
	}
	
	public String getCouponId() {
		return this.couponId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getSellerApplyId() {
		return sellerApplyId;
	}

	public void setSellerApplyId(String sellerApplyId) {
		this.sellerApplyId = sellerApplyId;
	}

	public void setNumber(Integer value) {
		this.number = value;
	}
	
	public Integer getNumber() {
		return this.number;
	}

	public Integer getKnumber() {
		return knumber;
	}

	public void setKnumber(Integer knumber) {
		this.knumber = knumber;
	}

	public Integer getYnumber() {
		return ynumber;
	}

	public void setYnumber(Integer ynumber) {
		this.ynumber = ynumber;
	}

	public String getTicketImg() {
		return ticketImg;
	}

	public void setTicketImg(String ticketImg) {
		this.ticketImg = ticketImg;
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

	public Integer getIsApproval() {
		return isApproval;
	}

	public void setIsApproval(Integer isApproval) {
		this.isApproval = isApproval;
	}

	public Integer getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(Integer useStatus) {
		this.useStatus = useStatus;
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

