package com.perenc.xh.lsp.entity.jsdata;

import javax.persistence.Table;


@Table(name="tc_car_outpass")
public class TcCarOutpassData implements java.io.Serializable{

	private static final long serialVersionUID = 1519727525609985175L;


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
	 * 出场记录唯一标识
	 */
	private String outRecordId;

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
	private String outTime;

	/**
     * 车辆ID  关联tc_car表的id       db_column: car_id  
     * @NotNull 
     */	
	private String carId;
	
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

	//操作员
	private String stationOperator;

	//捷顺单位分，车辆停车应收金额
	private int chargeTotal;
	//捷顺打折金额
	private int discountAmount;
	//捷顺实际缴费金额
	private int charge;
    //套餐名称
	private  String sealName;



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

	public String getOutRecordId() {
		return outRecordId;
	}

	public void setOutRecordId(String outRecordId) {
		this.outRecordId = outRecordId;
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

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
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

	public String getStationOperator() {
		return stationOperator;
	}

	public void setStationOperator(String stationOperator) {
		this.stationOperator = stationOperator;
	}

	public int getChargeTotal() {
		return chargeTotal;
	}

	public void setChargeTotal(int chargeTotal) {
		this.chargeTotal = chargeTotal;
	}

	public int getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(int discountAmount) {
		this.discountAmount = discountAmount;
	}

	public int getCharge() {
		return charge;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	public String getSealName() {
		return sealName;
	}

	public void setSealName(String sealName) {
		this.sealName = sealName;
	}

}

