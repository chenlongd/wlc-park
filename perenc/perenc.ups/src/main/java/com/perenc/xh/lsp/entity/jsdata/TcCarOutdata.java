package com.perenc.xh.lsp.entity.jsdata;

import javax.persistence.Table;


@Table(name="tc_car_inout")
public class TcCarOutdata implements java.io.Serializable{

	private static final long serialVersionUID = 1892079661579359872L;

	/**
	 * 捷顺车场ID
	 * @NotNull
	 */
	private String parkId;

	/**
	 * 入场记录唯一标识
	 */
	private String inRecordId;

	/**
	 * 入场设备标识
	 */
	private String inDeviceId;

	/**
	 * 入场设备名称
	 */
	private String inDeviceName;


	/**
	 * 出场设备标识
	 */
	private String outDeviceId;

	/**
	 * 出场设备名称
	 */
	private String outDeviceName;

	/**
	 * 出场识别时间
	 */
	private String recognitionTime;

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
	
    /**
     * 车辆出场图片       db_column: outimg  
     * @Length(max=500)
     */	
	private String outImage;



	public String getParkId() {
		return parkId;
	}

	public void setParkId(String parkId) {
		this.parkId = parkId;
	}

	public String getInRecordId() {
		return inRecordId;
	}

	public void setInRecordId(String inRecordId) {
		this.inRecordId = inRecordId;
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

	public String getOutDeviceId() {
		return outDeviceId;
	}

	public void setOutDeviceId(String outDeviceId) {
		this.outDeviceId = outDeviceId;
	}

	public String getOutDeviceName() {
		return outDeviceName;
	}

	public void setOutDeviceName(String outDeviceName) {
		this.outDeviceName = outDeviceName;
	}

	public String getRecognitionTime() {
		return recognitionTime;
	}

	public void setRecognitionTime(String recognitionTime) {
		this.recognitionTime = recognitionTime;
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

	public String getOutImage() {
		return outImage;
	}

	public void setOutImage(String outImage) {
		this.outImage = outImage;
	}


}

