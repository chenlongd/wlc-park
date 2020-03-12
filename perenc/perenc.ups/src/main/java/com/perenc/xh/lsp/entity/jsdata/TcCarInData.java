package com.perenc.xh.lsp.entity.jsdata;


public class TcCarInData implements java.io.Serializable{

	private static final long serialVersionUID = -1391676417787444811L;

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
	 * 入场识别时间
	 */
	private String recognitionTime;


	
    /**
     * 入场照片
     * @Length(max=100)
     */	
	private String inImage;

	/**
	 * 车牌号
	 * @Length(max=50)
	 */
	private String plateNumber;


	/**
	 * 车辆颜色
	 */
	private String plateColor;

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

	public String getRecognitionTime() {
		return recognitionTime;
	}

	public void setRecognitionTime(String recognitionTime) {
		this.recognitionTime = recognitionTime;
	}

	public String getInImage() {
		return inImage;
	}

	public void setInImage(String inImage) {
		this.inImage = inImage;
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


}

