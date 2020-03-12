package com.perenc.xh.lsp.entity.tcCarousel;


import javax.persistence.Table;


@Table(name="tc_carousel")
public class TcCarousel implements java.io.Serializable{

	private static final long serialVersionUID = 3491095185786125562L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;
	
    /**
     * 客户商家类型（1=客户端；2=商家端）关联sys_dict 表KIND值 等于CAROUSEL_SUB_TYPE       db_column: sub_type  
     * 
     */	
	private Integer subType;
	
    /**
     * 跳转类型（1=外部链接；2=内部跳转）关联sys_dict 表KIND值 等于CAROUSEL_TYPE       db_column: type  
     * 
     */	
	private Integer type;
	
    /**
	 * 图片路径       db_column: image
	 * @Length(max=200)
	 */
	private String image;

	/**
	 * 跳转路径       db_column: url
	 * @Length(max=200)
	 */
	private String url;
	
    /**
     * 排序（数字越小，排序越前）       db_column: sort  
     * @NotNull 
     */	
	private Integer sort;
	
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
	public void setSubType(Integer value) {
		this.subType = value;
	}
	
	public Integer getSubType() {
		return this.subType;
	}
	public void setType(Integer value) {
		this.type = value;
	}
	
	public Integer getType() {
		return this.type;
	}
	public void setImage(String value) {
		this.image = value;
	}
	
	public String getImage() {
		return this.image;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setSort(Integer value) {
		this.sort = value;
	}
	
	public Integer getSort() {
		return this.sort;
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

