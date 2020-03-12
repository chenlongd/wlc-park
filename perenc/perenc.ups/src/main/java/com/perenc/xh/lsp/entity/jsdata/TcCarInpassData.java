package com.perenc.xh.lsp.entity.jsdata;

import javax.persistence.Table;


@Table(name="tc_car_inpass")
public class TcCarInpassData implements java.io.Serializable{

	private static final long serialVersionUID = -5323035970044602605L;


	/**
	 * 入场记录唯一标识
	 */
	private String inRecordId;

	/**
	 * 捷顺车场ID
	 * @NotNull
	 */
	private String parkId;

	/**
	 * 入场设备标识
	 */
	private String inDeviceId;

	/**
	 * 入场设备名称
	 */
	private String inDeviceName;

	/**
	 * 入场时间
	 */
	private String inTime;


    /**
     * 车牌号       db_column: car_num  
     * @Length(max=100)
     */	
	private String plateNumber;


	/**
	 * 车辆颜色
	 */
	private String plateColor;

    /**
     * 车辆进场图片       db_column: intimg  
     * @Length(max=500)
     */	
	private String inImage;

	//操作员
	private String stationOperator;

	//套餐名称
	private  String sealName;



	public String getInRecordId() {
		return inRecordId;
	}

	public void setInRecordId(String inRecordId) {
		this.inRecordId = inRecordId;
	}

	public String getParkId() {
		return parkId;
	}

	public void setParkId(String parkId) {
		this.parkId = parkId;
	}

	public String getInDeviceId() {
		return inDeviceId;
	}

	public void setInDeviceId(String inDeviceId) {
		this.inDeviceId = inDeviceId;
	}

	public String getInDeviceName() {
		return inDeviceName;
	}

	public void setInDeviceName(String inDeviceName) {
		this.inDeviceName = inDeviceName;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getPlateColor() {
		return plateColor;
	}

	public void setPlateColor(String plateColor) {
		this.plateColor = plateColor;
	}

	public String getInImage() {
		return inImage;
	}

	public void setInImage(String inImage) {
		this.inImage = inImage;
	}

	public String getStationOperator() {
		return stationOperator;
	}

	public void setStationOperator(String stationOperator) {
		this.stationOperator = stationOperator;
	}

	public String getSealName() {
		return sealName;
	}

	public void setSealName(String sealName) {
		this.sealName = sealName;
	}


}

