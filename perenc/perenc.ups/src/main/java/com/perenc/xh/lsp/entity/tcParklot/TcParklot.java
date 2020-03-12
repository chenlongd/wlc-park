package com.perenc.xh.lsp.entity.tcParklot;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name="tc_parklot")
public class TcParklot implements java.io.Serializable{

	private static final long serialVersionUID = 7142536796599420158L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
    /**
     * 场地名称       db_column: name  
     * @Length(max=100)
     */	
	private String name;
	
    /**
     * 场地车位数       db_column: number  
     * @NotNull 
     */	
	private Integer number;

	/**
	 * 单价       db_column: unit_price
	 *
	 */
	private Integer unitPrice;

	/**
	 * 上限小时(最高收费时长)       db_column: max_hour
	 *
	 */
	private Integer maxHour;

	/**
	 * 最小小时(最小收费时长)       db_column: min_hour
	 *
	 */
	private Integer minHour;



	/**
	 * 描述      db_column: descp
	 * @Length(max=500)
	 */
	private String descp;
	
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
	public void setNumber(Integer value) {
		this.number = value;
	}
	
	public Integer getNumber() {
		return this.number;
	}

	public void setUnitPrice(Integer value) {
		this.unitPrice = value;
	}

	public Integer getUnitPrice() {
		return this.unitPrice;
	}
	public void setMaxHour(Integer value) {
		this.maxHour = value;
	}

	public Integer getMaxHour() {
		return this.maxHour;
	}
	public void setMinHour(Integer value) {
		this.minHour = value;
	}

	public Integer getMinHour() {
		return this.minHour;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
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

