package com.perenc.xh.lsp.entity.urlLog;

import javax.persistence.Table;


@Table(name="url_log")
public class UrlLog implements java.io.Serializable{

	private static final long serialVersionUID = -4104791777479323201L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;


	/**
	 * opId       db_column: opId
	 */
	private String opId;

	/**
	 * opName       db_column: opName
	 */
	private String opName;


	/**
	 * 用户ID 关联usr_extend表的id       db_column: extend_id
	 * @NotNull
	 */
	private Integer extendId;

	/**
	 * 商家ID 关联tc_seller表的id       db_column: seller_id
	 * @NotNull
	 */
	private Integer sellerId;

	/**
	 * 商家用户ID 关联tc_seller表的id       db_column: seller_id
	 * @NotNull
	 */
	private Integer sellerUserId;

	/**
     * 访问url
     */	
	private String requestUrl;

	/**
	 * 访问方法
	 */
	private String requestMethod;

	/**
	 * 访问参数
	 */
	private String requestParams;

	/**
	 * 返回状态
	 */
	private String responseStatus;


	/**
	 * 返回状态
	 */
	private String responseBody;



	/**
     * 内容名
     */	
	private String controllerName;
	
    /**
     * 内容方法
     * 
     */	
	private String controllerMethod;



	/**
	 * 备用字段       db_column: spare
	 */
	private String spare;


	/**
	 * 备注
	 */
	private String remark;


	/**
     * 响应时间
     * 
     */	
	private String costTime;

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

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public Integer getExtendId() {
		return extendId;
	}

	public void setExtendId(Integer extendId) {
		this.extendId = extendId;
	}

	public Integer getSellerId() {
		return sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	public Integer getSellerUserId() {
		return sellerUserId;
	}

	public void setSellerUserId(Integer sellerUserId) {
		this.sellerUserId = sellerUserId;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public String getControllerMethod() {
		return controllerMethod;
	}

	public void setControllerMethod(String controllerMethod) {
		this.controllerMethod = controllerMethod;
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

	public String getCostTime() {
		return costTime;
	}

	public void setCostTime(String costTime) {
		this.costTime = costTime;
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

