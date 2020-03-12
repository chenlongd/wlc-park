package com.perenc.xh.lsp.entity.userToken;

import javax.persistence.Table;


@Table(name="user_token")
public class UserToken implements java.io.Serializable{

	private static final long serialVersionUID = -4824874267341071868L;
	
	//columns START
    /**
     * 主键       db_column: id  
     * 
     */
	private Integer id;
	
    /**
     * 用户ID       db_column: extend_id  
     * 
     */	
	private Integer extendId;
	
    /**
	 * 商家ID       db_column: seller_id
	 *
	 */
	private Integer sellerId;


	/**
	 * 商家用户ID       db_column: seller_user_id
	 *
	 */
	private Integer sellerUserId;
	
    /**
     * 类型（1web，2IOS，3Android）       db_column: type  
     * 
     */	
	private Integer type;
	
    /**
     * TOKEN       db_column: token  
     * @Length(max=200)
     */	
	private String token;
	
    /**
     * 设备token       db_column: device_token  
     * @Length(max=200)
     */	
	private String deviceToken;


	/**
     * 创建时间       db_column: create_time  
     * 
     */	
	private String createTime;
	

    /**
     * 修改时间       db_column: update_time  
     * 
     */	
	private String updateTime;
	

	//columns END

	public void setId(Integer value) {
		this.id = value;
	}
	
	public Integer getId() {
		return this.id;
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

	public void setType(Integer value) {
		this.type = value;
	}
	
	public Integer getType() {
		return this.type;
	}
	public void setToken(String value) {
		this.token = value;
	}
	
	public String getToken() {
		return this.token;
	}
	public void setDeviceToken(String value) {
		this.deviceToken = value;
	}
	
	public String getDeviceToken() {
		return this.deviceToken;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}



}

