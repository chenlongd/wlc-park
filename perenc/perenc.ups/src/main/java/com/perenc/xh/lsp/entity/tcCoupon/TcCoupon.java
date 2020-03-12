package com.perenc.xh.lsp.entity.tcCoupon;


import javax.persistence.Table;


@Table(name="tc_coupon")
public class TcCoupon implements java.io.Serializable{

	private static final long serialVersionUID = 2254647706911619450L;

	//columns START
	/**
	 * 序号       db_column: id
	 *
	 */
	private String id;

	//店铺ID
	private String storeId;

	/**
	 * 优惠券名称       db_column: name
	 * @Length(max=100)
	 */
	private String name;

	/**
	 * 类型 1=停车卷；2=商品 关联sys_dict 表KIND值 等于COUPON_TYPE       db_column: type
	 *
	 */
	private Integer type;


	/**
	 * 类型 1=减免金额；2=减免小时 ; 3：百分比       db_column: useType
	 *
	 */
	private Integer useType;

	/**
	 * 描述       db_column: desc
	 * @Length(max=500)
	 */
	private String desc;

	/**
	 * 停车小时数       db_column: duration
	 *
	 */
	private Integer duration;

	/**
	 * 金额       db_column: amount
	 *
	 */
	private Integer amount;


	/**
	 * 捷顺 优惠方式 （值：
	 * MINUSMONEY：减免金额
	 * MINUSHOUR：减免小时
	 * MINPERCENT：百分比，例
	 * 如:0.9,则表示实收金额=应
	 * 收金额*90%
	 * ALLFREE：全免）
	 */
	private  String couponWay;


	/**
	 * 捷顺 优惠面值 （
	 * 优惠方式为减免金额，单位为
	 * 元；
	 * 优惠方式为减免小时，单位为
	 * 小时；
	 * 优惠方式为减免百分比；
	 * 优惠方式为全免，值无意义）
	 */
	private  Integer couponMoney;



	/**
	 * 满减金额       db_column: full_amount
	 *
	 */
	private Integer fullAmount;

	/**
	 * 开始时间       db_column: sdate
	 * @Length(max=50)
	 */
	private String sdate;

	/**
	 * 结束时间       db_column: edate
	 * @Length(max=50)
	 */
	private String edate;


	/**
	 * 是否启用 1：是,2否      db_column: isEnabled
	 */
	private Integer isEnabled;

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

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public void setName(String value) {
		this.name = value;
	}

	public String getName() {
		return this.name;
	}

	public void setType(Integer value) {
		this.type = value;
	}

	public Integer getType() {
		return this.type;
	}

	public Integer getUseType() {
		return useType;
	}

	public void setUseType(Integer useType) {
		this.useType = useType;
	}

	public void setDesc(String value) {
		this.desc = value;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDuration(Integer value) {
		this.duration = value;
	}

	public Integer getDuration() {
		return this.duration;
	}

	public void setAmount(Integer value) {
		this.amount = value;
	}

	public Integer getAmount() {
		return this.amount;
	}

	public String getCouponWay() {
		return couponWay;
	}

	public void setCouponWay(String couponWay) {
		this.couponWay = couponWay;
	}

	public Integer getCouponMoney() {
		return couponMoney;
	}

	public void setCouponMoney(Integer couponMoney) {
		this.couponMoney = couponMoney;
	}

	public Integer getFullAmount() {
		return fullAmount;
	}

	public void setFullAmount(Integer fullAmount) {
		this.fullAmount = fullAmount;
	}

	public void setSdate(String value) {
		this.sdate = value;
	}

	public String getSdate() {
		return this.sdate;
	}

	public void setEdate(String value) {
		this.edate = value;
	}

	public String getEdate() {
		return this.edate;
	}

	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
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

