package com.perenc.xh.lsp.entity.jsdata;


public class TcPaycheckData implements java.io.Serializable{

	private static final long serialVersionUID = -6043129097483609157L;


	/**
	 * 捷顺车场ID
	 * @NotNull
	 */
	private String parkId;


	/**
	 * 入场设备标识
	 */
	private String payNo;

	public String getParkId() {
		return parkId;
	}

	public void setParkId(String parkId) {
		this.parkId = parkId;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}



}

