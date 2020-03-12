package com.perenc.xh.lsp.entity.tcSeller;

import javax.persistence.Table;


@Table(name="tc_seller")
public class TcSeller implements java.io.Serializable{


	private static final long serialVersionUID = 6412152718110477912L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private Integer id;
	
    /**
     * 商家名称       db_column: name  
     * @Length(max=100)
     */
	private String name;


	/**
     * 商家简介       db_column: desc  
     * @Length(max=500)
     */
	private String descp;
	
    /**
     * 商家Logo       db_column: logo  
     * @Length(max=200)
     */
	private String logo;
	
    /**
     * 营业执照ID 关联tc_license表的id       db_column: license_id  
     * @NotNull 
     */	
	private String licenseId;


	/**
	 * 营业执照图片      db_column: license_img
	 * @NotNull
	 */
	private String licenseImg;


	/**
	 * 用户名       db_column: username
	 * @Length(max=50)
	 */
	private String username;


	/**
     * 手机号       db_column: phone  
     * @Length(max=50)
     */	
	private String phone;


	/**
	 * 密码     db_column: password
	 * @NotNull
	 */
	private String password;


	/**
	 * 邮箱     db_column: email
	 * @NotNull
	 */
	private String email;

	/**
	 * 地区省ID 关联tc_province表的id       db_column: province_id
	 * @NotNull
	 */
	private String provinceId;

	/**
	 * 地区市ID 关联tc_city表的id       db_column: city_id
	 * @NotNull
	 */
	private String cityId;

	/**
	 * 地区县ID 关联tc_county表的id       db_column: county_id
	 * @NotNull
	 */
	private String countyId;

	/**
	 * 位置     db_column: address
	 * @NotNull
	 */
	private String address;


	/**
     * 商家类型（1=自营；2=其他）关联sys_dict 表KIND值 等于SELLER_TYPE       db_column: type  
     * 
     */	
	private Integer type;
	
    /**
     * 用户id 关联usr_extend表的id       db_column: extend_id  
     * @NotNull 
     */	
	private Integer extendId;
	
    /**
     * 是否审核 1待审核，2：已通过，3：未通过 关联sys_dict 表KIND值 等于APPLY_STATUS       db_column: is_approval
     * 
     */	
	private Integer isApproval;
	
    /**
     * 可用停车小时数       db_column: duration  
     * 
     */	
	private Integer duration;

	/**
	 * 是否运营状态 1：是;2：否
	 *
	 */
	private Integer isWork;
	
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


	//更新时间
	private String updateTime;
	
	//columns END

	public void setId(Integer value) {
		this.id = value;
	}
	
	public Integer getId() {
		return this.id;
	}
	public void setName(String value) {
		this.name = value;
	}
	
	public String getName() {
		return this.name;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}
	public void setLogo(String value) {
		this.logo = value;
	}
	
	public String getLogo() {
		return this.logo;
	}
	public void setLicenseId(String value) {
		this.licenseId = value;
	}
	
	public String getLicenseId() {
		return this.licenseId;
	}
	public String getLicenseImg() {
		return licenseImg;
	}

	public void setLicenseImg(String licenseImg) {
		this.licenseImg = licenseImg;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPhone(String value) {
		this.phone = value;
	}
	
	public String getPhone() {
		return this.phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public void setType(Integer value) {
		this.type = value;
	}
	
	public Integer getType() {
		return this.type;
	}
	public void setExtendId(Integer value) {
		this.extendId = value;
	}
	
	public Integer getExtendId() {
		return this.extendId;
	}
	public void setIsApproval(Integer value) {
		this.isApproval = value;
	}
	
	public Integer getIsApproval() {
		return this.isApproval;
	}
	public void setDuration(Integer value) {
		this.duration = value;
	}
	
	public Integer getDuration() {
		return this.duration;
	}

	public Integer getIsWork() {
		return isWork;
	}

	public void setIsWork(Integer isWork) {
		this.isWork = isWork;
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

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}


}

