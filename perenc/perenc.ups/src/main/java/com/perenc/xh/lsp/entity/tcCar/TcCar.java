package com.perenc.xh.lsp.entity.tcCar;

import javax.persistence.Table;


@Table(name="tc_car")
public class TcCar implements java.io.Serializable{

	private static final long serialVersionUID = 3006844834497963775L;

	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;
	
    /**
     * 车牌号       db_column: car_num  
     * @Length(max=100)
     */	
	private String carNum;


	/**
	 * 车辆颜色
	 */
	private String carColor;

	/**
	 * 车类型（大车，小车）关联sys_dict 表KIND值 等于CAR_TYPE       db_column: type
	 *
	 */
	private Integer type;


	/**
	 * VIP车类型（临时车，月卡车,季卡车，半年卡车，年卡车）关联sys_dict 表KIND值 等于CAR_VIP_TYPE       db_column: vip_type
	 *
	 */
	private Integer vipType;
	/**
	 * VIPID关联tc_vip 表ID      db_column: vip_id
	 *
	 */
	private String vipId;

	/**
     * 行车证ID 关联tc_travel表的id       db_column: travel_id  
     * @NotNull 
     */	
	private String travelId;

	/**
	 * 是否行车证认证 1：是;2：否 关联sys_dict 表KIND值 等于YESORNOT       db_column: is_travel
	 *
	 */
	private Integer isTravel;

	/**
	 * 行车证图片    db_column: travel_Img
	 * @NotNull
	 */
	private String travelImg;
	
    /**
     * 停车场ID 关联tc_parklot表的id       db_column: parklot_id  
     * @NotNull 
     */	
	private String parklotId;
	
    /**
     * 计费开始时间       db_column: cost_sdate  
     * @Length(max=50)
     */	
	private String costSdate;
	
    /**
     * 计费结束时间       db_column: cost_edate  
     * @Length(max=50)
     */	
	private String costEdate;
	
    /**
     * 车辆进场图片       db_column: intimg  
     * @Length(max=500)
     */	
	private String intimg;
	
    /**
     * 车辆出场图片       db_column: outimg  
     * @Length(max=500)
     */	
	private String outimg;
	
    /**
     * 会员开始时间       db_column: vip_sdate  
     * @Length(max=50)
     */	
	private String vipSdate;
	
    /**
     * 会员结束时间       db_column: vip_edate  
     * @Length(max=50)
     */	
	private String vipEdate;
	
    /**
	 * 是否入场（1:已入场，2:未入场）  关联sys_dict 表KIND值 等于ENTYR_STATUS       db_column: is_entry
     * 
     */	
	private Integer isEntry;
	
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
	public void setCarNum(String value) {
		this.carNum = value;
	}
	
	public String getCarNum() {
		return this.carNum;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public void setType(Integer value) {
		this.type = value;
	}
	
	public Integer getType() {
		return this.type;
	}


	public Integer getVipType() {
		return vipType;
	}

	public void setVipType(Integer vipType) {
		this.vipType = vipType;
	}
	public String getVipId() {
		return vipId;
	}

	public void setVipId(String vipId) {
		this.vipId = vipId;
	}
	public void setTravelId(String value) {
		this.travelId = value;
	}
	
	public String getTravelId() {
		return this.travelId;
	}


	public Integer getIsTravel() {
		return isTravel;
	}

	public void setIsTravel(Integer isTravel) {
		this.isTravel = isTravel;
	}

	public String getTravelImg() {
		return travelImg;
	}

	public void setTravelImg(String travelImg) {
		this.travelImg = travelImg;
	}
	public void setParklotId(String value) {
		this.parklotId = value;
	}

	public String getParklotId() {
		return this.parklotId;
	}
	public void setCostSdate(String value) {
		this.costSdate = value;
	}
	
	public String getCostSdate() {
		return this.costSdate;
	}
	public void setCostEdate(String value) {
		this.costEdate = value;
	}
	
	public String getCostEdate() {
		return this.costEdate;
	}
	public void setIntimg(String value) {
		this.intimg = value;
	}
	
	public String getIntimg() {
		return this.intimg;
	}
	public void setOutimg(String value) {
		this.outimg = value;
	}
	
	public String getOutimg() {
		return this.outimg;
	}
	public void setVipSdate(String value) {
		this.vipSdate = value;
	}
	
	public String getVipSdate() {
		return this.vipSdate;
	}
	public void setVipEdate(String value) {
		this.vipEdate = value;
	}
	
	public String getVipEdate() {
		return this.vipEdate;
	}
	public void setIsEntry(Integer value) {
		this.isEntry = value;
	}
	
	public Integer getIsEntry() {
		return this.isEntry;
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

