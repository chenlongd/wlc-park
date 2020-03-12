package com.perenc.xh.lsp.entity.tcSeller;

import javax.persistence.Table;


@Table(name="tc_seller_apply")
public class TcSellerApplyc implements java.io.Serializable{

	private static final long serialVersionUID = -7397604662504582535L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;

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
	 * 商家发放停车券时拍照小票     db_column: ticket_img
	 */
	private String ticketImg;

	/**
	 * 总的张数/小时数       db_column: number
	 * @NotNull
	 */
	private Integer number;
	
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
	 * 使用状态 1:待使用，3:已使用，4:已过期 关联sys_dict 表KIND值 等于COUPON_STATUS       db_column: use_status
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

	public String getTicketImg() {
		return ticketImg;
	}

	public void setTicketImg(String ticketImg) {
		this.ticketImg = ticketImg;
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
	public void setIsApproval(Integer value) {
		this.isApproval = value;
	}
	
	public Integer getIsApproval() {
		return this.isApproval;
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

