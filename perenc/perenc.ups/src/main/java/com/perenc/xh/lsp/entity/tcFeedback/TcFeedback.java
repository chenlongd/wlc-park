package com.perenc.xh.lsp.entity.tcFeedback;


import javax.persistence.Table;


@Table(name="tc_feedback")
public class TcFeedback implements java.io.Serializable{

	private static final long serialVersionUID = 2174250748413992773L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;
	
    /**
     * 用户ID 关联usr_extend表的id       db_column: extend_id  
     * @NotNull 
     */	
	private String extendId;
	
    /**
     * 类型（1=改进意见；2=充值问题）关联sys_dict 表KIND值 等于FEEDBACK_TYPE       db_column: type  
     * 
     */	
	private Integer type;
	
    /**
     * 反馈内容       db_column: content  
     * @Length(max=500)
     */	
	private String content;
	
    /**
     * 反馈图片       db_column: content_image  
     * @Length(max=500)
     */	
	private String contentImage;
	
    /**
     * 回复       db_column: reply  
     * @Length(max=500)
     */	
	private String reply;
	
    /**
     * 回复时间       db_column: reply_time  
     * @Length(max=50)
     */	
	private String replyTime;
	
    /**
     * 联系方式       db_column: tel  
     * @Length(max=100)
     */	
	private String tel;
	
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

	public String getExtendId() {
		return extendId;
	}

	public void setExtendId(String extendId) {
		this.extendId = extendId;
	}

	public void setType(Integer value) {
		this.type = value;
	}
	
	public Integer getType() {
		return this.type;
	}
	public void setContent(String value) {
		this.content = value;
	}
	
	public String getContent() {
		return this.content;
	}
	public void setContentImage(String value) {
		this.contentImage = value;
	}
	
	public String getContentImage() {
		return this.contentImage;
	}
	public void setReply(String value) {
		this.reply = value;
	}
	
	public String getReply() {
		return this.reply;
	}
	public void setReplyTime(String value) {
		this.replyTime = value;
	}
	
	public String getReplyTime() {
		return this.replyTime;
	}
	public void setTel(String value) {
		this.tel = value;
	}
	
	public String getTel() {
		return this.tel;
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

