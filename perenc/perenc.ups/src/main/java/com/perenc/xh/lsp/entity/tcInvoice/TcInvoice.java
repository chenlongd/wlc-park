package com.perenc.xh.lsp.entity.tcInvoice;

import javax.persistence.Table;


@Table(name="tc_invoice")
public class TcInvoice implements java.io.Serializable{

	private static final long serialVersionUID = 5589186781769614989L;
	
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
	 * 发票类型 发票类型（1:单位，2:个人）         db_column: type
	 * @NotNull
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
	 * @Length(max=50)
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
	 * 开户银行       db_column: bank_name
	 * @Length(max=100)
	 */
	private String bankName;

	/**
	 * 开户帐号       db_column: address
	 * @Length(max=100)
	 */
	private String bankNumber;
	
    /**
     * 手机号       db_column: phone  
     * @Length(max=50)
     */	
	private String phone;
	
    /**
     * 发票编号       db_column: invoice_num  
     * @Length(max=200)
     */	
	private String invoiceNum;



	/**
	 * 发票金额　    db_column: amount
	 *
	 */
	private Integer amount;


	/**
	 * 发票地址      db_column: file
	 * @Length(max=200)
	 */
	private String file;

	/**
     * 是否开发票 1：是;2：否 关联sys_dict 表KIND值 等于YESORNOT       db_column: is_invoice  
     * 
     */	
	private Integer isInvoice;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNumber() {
		return bankNumber;
	}

	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}

	public void setPhone(String value) {
		this.phone = value;
	}
	
	public String getPhone() {
		return this.phone;
	}
	public void setInvoiceNum(String value) {
		this.invoiceNum = value;
	}
	
	public String getInvoiceNum() {
		return this.invoiceNum;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setIsInvoice(Integer value) {
		this.isInvoice = value;
	}
	
	public Integer getIsInvoice() {
		return this.isInvoice;
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

