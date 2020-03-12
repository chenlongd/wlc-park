package com.perenc.xh.lsp.entity.tcSeller;

import javax.persistence.Table;


@Table(name="tc_seller_freecar")
public class TcSellerFreecar implements java.io.Serializable{

	private static final long serialVersionUID = -5359531663388936042L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;
	
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
	 * 上传免费凭证图片
	 */
	private String ticketImg;


	/**
	 * 是否审核通过（1:待审核，2:通过，3:未通过） 关联sys_dict 表KIND值 等于APPLY_STATUS       db_column: is_approval
	 *
	 */
	private Integer isApproval;

	/**
	 * 使用状态 3:使用中，4:已过期      db_column: use_status
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

	public String getTicketImg() {
		return ticketImg;
	}

	public void setTicketImg(String ticketImg) {
		this.ticketImg = ticketImg;
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

