package com.perenc.xh.lsp.entity.tcVip;

import javax.persistence.Table;


@Table(name="tc_vip")
public class TcVip implements java.io.Serializable{

	private static final long serialVersionUID = -828561777376489175L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;
	
    /**
     * 会员名称       db_column: name  
     * @Length(max=100)
     */	
	private String name;

	/**
	 *  VIP车类型（1:月卡车,2:季卡车，3:半年卡车，4:年卡车）       db_column: type
	 */
	private Integer type;
	
    /**
     * 有效期月数（1月，3月，6月，12月）关联sys_dict 表KIND值 等于VIP_NUMBER       db_column: number  
     * @NotNull 
     */	
	private Integer number;
	
    /**
     * 现价       db_column: cost_price  
     * 
     */	
	private Integer costPrice;
	
    /**
     * 折扣价       db_column: discount_price  
     * 
     */	
	private Integer discountPrice;
	
    /**
     * 图片       db_column: image  
     * @Length(max=200)
     */	
	private String image;

	/**
	 * 是否启用 1：是,2否      db_column: isEnabled
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
	
	//columns END

	public void setId(String value) {
		this.id = value;
	}
	
	public String getId() {
		return this.id;
	}
	public void setName(String value) {
		this.name = value;
	}
	
	public String getName() {
		return this.name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setNumber(Integer value) {
		this.number = value;
	}
	
	public Integer getNumber() {
		return this.number;
	}
	public void setCostPrice(Integer value) {
		this.costPrice = value;
	}
	
	public Integer getCostPrice() {
		return this.costPrice;
	}
	public void setDiscountPrice(Integer value) {
		this.discountPrice = value;
	}
	
	public Integer getDiscountPrice() {
		return this.discountPrice;
	}
	public void setImage(String value) {
		this.image = value;
	}
	
	public String getImage() {
		return this.image;
	}

	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
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

