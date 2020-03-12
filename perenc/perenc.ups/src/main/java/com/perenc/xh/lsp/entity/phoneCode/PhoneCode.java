package com.perenc.xh.lsp.entity.phoneCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name="phone_code")
public class PhoneCode implements java.io.Serializable{

	private static final long serialVersionUID = 1014118489695523974L;

	//columns START
    /**
     * 序号       db_column: id  
     * 
     */	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
    /**
     * 手机号       db_column: phone  
     * @Length(max=50)
     */	
	private String phone;

	/**
	 * 类型
	 * 1：绑定号码
	 * @Length(max=10)
	 */
	private Integer type;
	
    /**
     * 验证码       db_column: code  
     * @Length(max=10)
     */	
	private String code;
	/**
	 * 有效期开始时间       db_column: sdate
	 * @Length(max=50)
	 */
	private String sdate;

	/**
	 * 有效期结束时间       db_column: edate
	 * @Length(max=50)
	 */
	private String edate;

	/**
	 * 是否有效（1:有效，2:无效）
	 *
	 */
	private Integer isValid;


	/**
     * 创建时间       db_column: create_time  
     * 
     */	
	private String createTime;
	

	//columns END

	public void setId(String value) {
		this.id = value;
	}
	
	public String getId() {
		return this.id;
	}
	public void setPhone(String value) {
		this.phone = value;
	}
	
	public String getPhone() {
		return this.phone;
	}
	public void setCode(String value) {
		this.code = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getCode() {
		return this.code;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public String getEdate() {
		return edate;
	}

	public void setEdate(String edate) {
		this.edate = edate;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public void setCreateTime(String value) {
		this.createTime = value;
	}
	
	public String getCreateTime() {
		return this.createTime;
	}



}

