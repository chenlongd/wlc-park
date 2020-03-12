package com.perenc.xh.commonUtils.model;
public class Paging {
	private Integer curPage = 1;//当前页
	private Integer totalPage = 1;//总的页数
	private Integer pageSize = 10;//每页大小
	private Integer totalNum = 0;//总共的数目
	
	private String groupByField;//排序字段
	
	private String loginfo;//日志信息
	
	private Double percentDouble;
	private String percent;
	private int offset;
	private int endset;
	
	public Integer getCurPage() {
		if(curPage < 1){
			curPage = 1;
		}
		return curPage;
	}
	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}
	public Integer getTotalPage() {
		totalPage = 1;
		if(this.getTotalNum() >= 1){
			totalPage = (this.getTotalNum() + this.getPageSize() - 1) / this.getPageSize();
		}
		if(totalPage < 0){
			totalPage = 0;
		}
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * 得到百分比的小数描述
	 * @return
	 */
	public Double getPercentDouble(){
		if(this.getTotalPage() < 1){
			return 1.0;
		}
		percentDouble = (curPage - 1) * 1.0 / totalPage;
		
		return percentDouble >= 1?1.0:percentDouble;
	}
	
	/**
	 * 得到百分比string
	 * @return
	 */
	public String getPercent(){
		Double percent = this.getPercentDouble();
		percent = percent * 100;
		if(percent > 100){
			percent = 100.0;
		}
		this.percent = percent.toString();
		if(this.percent.length() > 4){
			return this.percent.substring(0,4) + "%";
		}else{
			return this.percent + "%";
		}
	}
	
	/**
	 * 得到开始偏移量
	 * @return
	 */
	public Integer getOffset(){
		offset = (curPage - 1) * pageSize;//当前开始记录
		if(offset < 0){
			offset = 0;
		}
		return offset;
	}
	
	/**
	 * 得到开始偏移量
	 * @return
	 */
	public Integer getEndset(){
		endset = getOffset() + pageSize;
		return endset;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public String getLoginfo() {
		return loginfo;
	}
	public void setLoginfo(String loginfo) {
		this.loginfo = loginfo;
	}
	public void setPercentDouble(Double percentDouble) {
		this.percentDouble = percentDouble;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public void setEndset(int endset) {
		this.endset = endset;
	}
	public String getGroupByField() {
		return groupByField;
	}
	public void setGroupByField(String groupByField) {
		this.groupByField = groupByField;
	}
	
	
}