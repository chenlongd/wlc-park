package com.perenc.xh.lsp.entity.tcExtend;

import javax.persistence.Table;


@Table(name="tc_extend_message")
public class TcExtendMessage implements java.io.Serializable{

	private static final long serialVersionUID = -6210044800440445816L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;
	
    /**
     * 消息ID 关联tc_message表的id       db_column: message_id  
     * 
     */	
	private String messageId;
	
    /**
     * 发送用户ID 关联usr_extend表的id       db_column: sender_id  
     * 
     */	
	private Integer senderId;
	
    /**
     * 接收用户ID 关联usr_extend表的id       db_column: receiver_id  
     * 
     */	
	private Integer receiverId;
	
    /**
     * 类型（1：充值提醒，2：停车提醒）       db_column: type  
     * 
     */	
	private Integer type;
	
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
	public void setMessageId(String value) {
		this.messageId = value;
	}
	
	public String getMessageId() {
		return this.messageId;
	}
	public void setSenderId(Integer value) {
		this.senderId = value;
	}
	
	public Integer getSenderId() {
		return this.senderId;
	}
	public void setReceiverId(Integer value) {
		this.receiverId = value;
	}
	
	public Integer getReceiverId() {
		return this.receiverId;
	}
	public void setType(Integer value) {
		this.type = value;
	}
	
	public Integer getType() {
		return this.type;
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

