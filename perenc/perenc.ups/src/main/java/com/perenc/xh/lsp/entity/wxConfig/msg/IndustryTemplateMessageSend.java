package com.perenc.xh.lsp.entity.wxConfig.msg;

/**
 * 取多媒体文件
 * 
 * @author sfli.sir
 * 
 */
 
public class IndustryTemplateMessageSend extends WeixinReqParam {

	private String touser;
	
	private String template_id;
	
	private String url;
	
	private Miniprogram miniprogram;

	private TemplateMessage data;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Miniprogram getMiniprogram() {
		return miniprogram;
	}

	public void setMiniprogram(Miniprogram miniprogram) {
		this.miniprogram = miniprogram;
	}

	public TemplateMessage getData() {
		return data;
	}

	public void setData(TemplateMessage data) {
		this.data = data;
	}
	
	
}
