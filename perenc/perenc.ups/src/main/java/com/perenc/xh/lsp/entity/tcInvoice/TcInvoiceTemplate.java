package com.perenc.xh.lsp.entity.tcInvoice;

import javax.persistence.Table;


@Table(name="tc_invoice_template")
public class TcInvoiceTemplate implements java.io.Serializable{

	private static final long serialVersionUID = 8325386922299430478L;
	
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
     * 发票类型 1：单位;2：个人       db_column: type  
     * 
     */	
	private Integer type;
	
    /**
     * 税号       db_column: tax_num  
     * @Length(max=100)
     */	
	private String taxNum;
	
    /**
     * 抬头       db_column: title  
     * @Length(max=100)
     */	
	private String title;
	
    /**
     * 邮箱       db_column: email  
     * @Email @Length(max=100)
     */	
	private String email;
	
    /**
     * 身份证号       db_column: id_number  
     * @Length(max=100)
     */	
	private String idNumber;
	
    /**
     * 联系人       db_column: contact  
     * @Length(max=50)
     */	
	private String contact;
	
    /**
     * 地区省       db_column: province  
     * @Length(max=500)
     */	
	private String province;
	
    /**
     * 地区市       db_column: city  
     * @Length(max=500)
     */	
	private String city;
	
    /**
     * 地区县       db_column: county  
     * @Length(max=500)
     */	
	private String county;
	
    /**
     * 地址       db_column: address  
     * @Length(max=500)
     */	
	private String address;
	
    /**
     * 开户银行       db_column: bankName  
     * @Length(max=100)
     */	
	private String bankName;
	
    /**
     * 开户帐号       db_column: bankNumber  
     * @Length(max=100)
     */	
	private String bankNumber;
	
    /**
     * 手机号       db_column: phone  
     * @Length(max=50)
     */	
	private String phone;
	
    /**
     * 是否默认 1：是;2：否 关联sys_dict 表KIND值 等于YESORNOT       db_column: is_default  
     * 
     */	
	private Integer isDefault;



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

	public void setExtendId(Integer value) {
		this.extendId = value;
	}

	public Integer getExtendId() {
		return this.extendId;
	}

	public void setType(Integer value) {
		this.type = value;
	}
	
	public Integer getType() {
		return this.type;
	}
	public void setTaxNum(String value) {
		this.taxNum = value;
	}
	
	public String getTaxNum() {
		return this.taxNum;
	}
	public void setTitle(String value) {
		this.title = value;
	}
	
	public String getTitle() {
		return this.title;
	}
	public void setEmail(String value) {
		this.email = value;
	}
	
	public String getEmail() {
		return this.email;
	}
	public void setIdNumber(String value) {
		this.idNumber = value;
	}
	
	public String getIdNumber() {
		return this.idNumber;
	}
	public void setContact(String value) {
		this.contact = value;
	}
	
	public String getContact() {
		return this.contact;
	}
	public void setProvince(String value) {
		this.province = value;
	}
	
	public String getProvince() {
		return this.province;
	}
	public void setCity(String value) {
		this.city = value;
	}
	
	public String getCity() {
		return this.city;
	}
	public void setCounty(String value) {
		this.county = value;
	}
	
	public String getCounty() {
		return this.county;
	}
	public void setAddress(String value) {
		this.address = value;
	}
	
	public String getAddress() {
		return this.address;
	}
	public void setBankName(String value) {
		this.bankName = value;
	}
	
	public String getBankName() {
		return this.bankName;
	}
	public void setBankNumber(String value) {
		this.bankNumber = value;
	}
	
	public String getBankNumber() {
		return this.bankNumber;
	}
	public void setPhone(String value) {
		this.phone = value;
	}
	
	public String getPhone() {
		return this.phone;
	}
	public void setIsDefault(Integer value) {
		this.isDefault = value;
	}
	
	public Integer getIsDefault() {
		return this.isDefault;
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

