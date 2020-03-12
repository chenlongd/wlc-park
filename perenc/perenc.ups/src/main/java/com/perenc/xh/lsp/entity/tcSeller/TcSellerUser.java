package com.perenc.xh.lsp.entity.tcSeller;

import javax.persistence.Table;


@Table(name="tc_seller_user")
public class TcSellerUser implements java.io.Serializable{

	private static final long serialVersionUID = 3447227229321513356L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private Integer id;
	
    /**
     * appID       db_column: app_id  
     * @Length(max=100)
     */	
	private String appId;
	
    /**
     * appSecrert       db_column: app_secrert  
     * @Length(max=100)
     */	
	private String appSecrert;
	
    /**
     * 微信客户ID  关联wm_customer表的id       db_column: customer_id  
     * 
     */	
	private Integer customerId;
	
    /**
     * 来源：1=小程序；2=公众号 关联sys_dict 表KIND值 等于COMEFROMO       db_column: come_from  
     * 
     */	
	private Integer comeFrom;
	
    /**
     * 所属商家ID 关联tc_seller表的id       db_column: seller_id  
     * @Length(max=200)
     */	
	private Integer sellerId;
	
    /**
     * 父级（对应tc_seller_user的id）       db_column: parent_id  
     * 
     */	
	private Integer parentId;
	
    /**
     * 用户名       db_column: username  
     * @Length(max=50)
     */	
	private String username;
	
    /**
     * 手机号       db_column: phone  
     * @Length(max=100)
     */	
	private String phone;
	
    /**
     * 密码       db_column: password  
     * @Length(max=100)
     */	
	private String password;
	
    /**
     * 邮箱       db_column: email  
     * @Email @Length(max=50)
     */	
	private String email;
	
    /**
     * 联系人       db_column: contact  
     * @Length(max=50)
     */	
	private String contact;
	
    /**
     * 省ID 关联tc_province表的id       db_column: province_id  
     * @Length(max=200)
     */	
	private String provinceId;
	
    /**
     * 市ID 关联tc_city表的id       db_column: city_id  
     * @Length(max=200)
     */	
	private String cityId;
	
    /**
     * 县ID 关联tc_county表的id       db_column: county_id  
     * @Length(max=200)
     */	
	private String countyId;
	
    /**
     * 地址       db_column: address  
     * @Length(max=200)
     */	
	private String address;
	
    /**
     * 头像       db_column: head_image  
     * @Length(max=200)
     */	
	private String headImage;
	
    /**
     * 性别       db_column: sex  
     * 
     */	
	private Integer sex;
	
    /**
     * 昵称       db_column: nickname  
     * @Length(max=50)
     */	
	private String nickname;
	
    /**
     * 生日       db_column: birthday  
     * @Length(max=50)
     */	
	private String birthday;
	
    /**
     * 是否主账号 1：是,2否       db_column: is_master  
     * 
     */	
	private Integer isMaster;
	
    /**
     * 是否禁用 1：是,2否       db_column: is_enabled
     * 
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
	
    /**
     * 更新时间       db_column: update_time  
     * @Length(max=50)
     */	
	private String updateTime;
	
	//columns END

	public void setId(Integer value) {
		this.id = value;
	}
	
	public Integer getId() {
		return this.id;
	}
	public void setAppId(String value) {
		this.appId = value;
	}
	
	public String getAppId() {
		return this.appId;
	}
	public void setAppSecrert(String value) {
		this.appSecrert = value;
	}
	
	public String getAppSecrert() {
		return this.appSecrert;
	}
	public void setCustomerId(Integer value) {
		this.customerId = value;
	}
	
	public Integer getCustomerId() {
		return this.customerId;
	}
	public void setComeFrom(Integer value) {
		this.comeFrom = value;
	}
	
	public Integer getComeFrom() {
		return this.comeFrom;
	}
	public void setSellerId(Integer value) {
		this.sellerId = value;
	}
	
	public Integer getSellerId() {
		return this.sellerId;
	}
	public void setParentId(Integer value) {
		this.parentId = value;
	}
	
	public Integer getParentId() {
		return this.parentId;
	}
	public void setUsername(String value) {
		this.username = value;
	}
	
	public String getUsername() {
		return this.username;
	}
	public void setPhone(String value) {
		this.phone = value;
	}
	
	public String getPhone() {
		return this.phone;
	}
	public void setPassword(String value) {
		this.password = value;
	}
	
	public String getPassword() {
		return this.password;
	}
	public void setEmail(String value) {
		this.email = value;
	}
	
	public String getEmail() {
		return this.email;
	}
	public void setContact(String value) {
		this.contact = value;
	}
	
	public String getContact() {
		return this.contact;
	}
	public void setProvinceId(String value) {
		this.provinceId = value;
	}
	
	public String getProvinceId() {
		return this.provinceId;
	}
	public void setCityId(String value) {
		this.cityId = value;
	}
	
	public String getCityId() {
		return this.cityId;
	}
	public void setCountyId(String value) {
		this.countyId = value;
	}
	
	public String getCountyId() {
		return this.countyId;
	}
	public void setAddress(String value) {
		this.address = value;
	}
	
	public String getAddress() {
		return this.address;
	}
	public void setHeadImage(String value) {
		this.headImage = value;
	}
	
	public String getHeadImage() {
		return this.headImage;
	}
	public void setSex(Integer value) {
		this.sex = value;
	}
	
	public Integer getSex() {
		return this.sex;
	}
	public void setNickname(String value) {
		this.nickname = value;
	}
	
	public String getNickname() {
		return this.nickname;
	}
	public void setBirthday(String value) {
		this.birthday = value;
	}
	
	public String getBirthday() {
		return this.birthday;
	}
	public void setIsMaster(Integer value) {
		this.isMaster = value;
	}
	
	public Integer getIsMaster() {
		return this.isMaster;
	}
	public void setIsEnabled(Integer value) {
		this.isEnabled = value;
	}
	
	public Integer getIsEnabled() {
		return this.isEnabled;
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
	public void setUpdateTime(String value) {
		this.updateTime = value;
	}
	
	public String getUpdateTime() {
		return this.updateTime;
	}


}

