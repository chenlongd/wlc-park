package com.perenc.xh.lsp.entity.tcExtend;

import javax.persistence.Table;


@Table(name="tc_extend_car")
public class TcExtendCar implements java.io.Serializable{

	private static final long serialVersionUID = -3330618994523031803L;
	
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
     * 车辆ID 关联tc_car表的id       db_column: car_id  
     * @NotNull 
     */	
	private String carId;
	
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
	public void setCarId(String value) {
		this.carId = value;
	}
	
	public String getCarId() {
		return this.carId;
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

