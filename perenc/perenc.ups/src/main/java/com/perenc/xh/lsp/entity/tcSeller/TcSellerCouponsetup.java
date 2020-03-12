package com.perenc.xh.lsp.entity.tcSeller;

import javax.persistence.Table;


@Table(name="tc_seller_couponsetup")
public class TcSellerCouponsetup implements java.io.Serializable{

	private static final long serialVersionUID = -6440532192832872741L;
	
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
     * 商家ID 关联tc_seller表的id       db_column: seller_id  
     * @NotNull 
     */	
	private Integer sellerId;
	
    /**
     * 限制申请张数/小时数       db_column: limit_number  
     * @NotNull 
     */	
	private Integer limitNumber;
	
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
	public void setSellerId(Integer value) {
		this.sellerId = value;
	}
	
	public Integer getSellerId() {
		return this.sellerId;
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

