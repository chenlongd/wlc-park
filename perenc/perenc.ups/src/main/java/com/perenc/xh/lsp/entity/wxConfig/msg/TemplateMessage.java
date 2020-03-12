package com.perenc.xh.lsp.entity.wxConfig.msg;


/**
 * 取多媒体文件
 * 
 * @author sfli.sir
 * 
 */
public class TemplateMessage {


	private TemplateData first;

	private TemplateData keyword1;

	private TemplateData keyword2;

	private TemplateData keyword3;

	private TemplateData keyword4;

	private TemplateData showname;

	private TemplateData ticket_qty;

	private TemplateData showtime;


	private TemplateData remark;

	public TemplateData getFirst() {
		return first;
	}

	public void setFirst(TemplateData first) {
		this.first = first;
	}

	public TemplateData getShowname() {
		return showname;
	}

	public void setShowname(TemplateData showname) {
		this.showname = showname;
	}

	public TemplateData getTicket_qty() {
		return ticket_qty;
	}

	public void setTicket_qty(TemplateData ticket_qty) {
		this.ticket_qty = ticket_qty;
	}

	public TemplateData getShowtime() {
		return showtime;
	}

	public void setShowtime(TemplateData showtime) {
		this.showtime = showtime;
	}

	public TemplateData getKeyword1() {
		return keyword1;
	}

	public void setKeyword1(TemplateData keyword1) {
		this.keyword1 = keyword1;
	}

	public TemplateData getKeyword2() {
		return keyword2;
	}

	public void setKeyword2(TemplateData keyword2) {
		this.keyword2 = keyword2;
	}

	public TemplateData getKeyword3() {
		return keyword3;
	}

	public void setKeyword3(TemplateData keyword3) {
		this.keyword3 = keyword3;
	}

	public TemplateData getKeyword4() {
		return keyword4;
	}

	public void setKeyword4(TemplateData keyword4) {
		this.keyword4 = keyword4;
	}

	public TemplateData getRemark() {
		return remark;
	}

	public void setRemark(TemplateData remark) {
		this.remark = remark;
	}
}
