package com.perenc.xh.lsp.entity.tcIntegral;

import javax.persistence.Table;


@Table(name="tc_integral")
public class TcIntegral implements java.io.Serializable{

	private static final long serialVersionUID = 5485347706902558035L;
	
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
	 *积分兑换规则ID 关联tc_integral_convertrule表的id       db_column: integral_convertrule_id
	 * @NotNull
	 */
	private String integralConvertruleId;


	/**
	 *积分项ID 关联tc_integral_term表的id       db_column: integral_trem_id
	 * @NotNull
	 */
	private String integralTermId;


	/**
	 *活动ID 关联tc_integral_term表的id
	 * @NotNull
	 */
	private String objectId;
	
    /**
     * 类型（1=增加；2=减少）关联sys_dict 表KIND值 等于INTEGRAL_TYPE       db_column: type  
     * 
     */	
	private Integer type;
	
    /**
     * 数量        db_column: number  
     * @NotNull 
     */	
	private Integer number;
	
    /**
     * 商品ID 关联tc_goods表的id       db_column: goods_id  
     * @NotNull 
     */	
	private String goodsId;
	
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

	public String getIntegralConvertruleId() {
		return integralConvertruleId;
	}

	public void setIntegralConvertruleId(String integralConvertruleId) {
		this.integralConvertruleId = integralConvertruleId;
	}

	public String getIntegralTermId() {
		return integralTermId;
	}

	public void setIntegralTermId(String integralTermId) {
		this.integralTermId = integralTermId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public void setType(Integer value) {
		this.type = value;
	}
	
	public Integer getType() {
		return this.type;
	}
	public void setNumber(Integer value) {
		this.number = value;
	}
	
	public Integer getNumber() {
		return this.number;
	}
	public void setGoodsId(String value) {
		this.goodsId = value;
	}
	
	public String getGoodsId() {
		return this.goodsId;
	}
	public void setRemark(String value) {
		this.remark = value;
	}
	
	public String getRemark() {
		return this.remark;
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

