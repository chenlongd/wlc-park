package com.perenc.xh.lsp.entity.tcCar;

import javax.persistence.Table;


@Table(name="tc_car_jsclientlog")
public class TcCarJsclientlog implements java.io.Serializable{

	private static final long serialVersionUID = -3949928273973262634L;
	
	//columns START

	/**
     * 序号       db_column: id  
     * 
     */
	private String id;
	
    /**
     * 日志类型（1：捷顺访问日志）
     */	
	private Integer type;


	/**
	 * 请求地址
	 */
	private String url;

	/**
	 * 请求类型：1：POST,2:GET
	 */
	private Integer rmode;


	/**
	 * 参数内容
	 */
	private String rparameter;

	/**
	 * 是否正常访问 1：是,2否      db_column: isEnabled
	 * @Length(max=500)
	 */
	private Integer isNormal;

	/**
	 * 返回结果
	 */
	private String content;

	/**
	 * 车牌号       db_column: car_num
	 */
	private String carNum;


	/**
	 * 备用字段       db_column: spare
	 */
	private String spare;

	/**
	 * 备注
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
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getRmode() {
		return rmode;
	}

	public void setRmode(Integer rmode) {
		this.rmode = rmode;
	}

	public String getRparameter() {
		return rparameter;
	}

	public void setRparameter(String rparameter) {
		this.rparameter = rparameter;
	}

	public Integer getIsNormal() {
		return isNormal;
	}

	public void setIsNormal(Integer isNormal) {
		this.isNormal = isNormal;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getSpare() {
		return spare;
	}

	public void setSpare(String spare) {
		this.spare = spare;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}

