package com.perenc.xh.lsp.entity.tcImchat;

import javax.persistence.Table;


@Table(name="tc_imchat")
public class TcImchat implements java.io.Serializable{


	private static final long serialVersionUID = 6214810442297841833L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;
	
    /**
     * 发送用户ID 关联usr_extend表的id       db_column: send_extend_id  
     * @NotNull 
     */	
	private Integer sendExtendId;
	
    /**
     * 接收用户ID 关联usr_extend表的id       db_column: receive_extend_id  
     * @NotNull 
     */	
	private Integer receiveExtendId;
	
    /**
     * 聊天内容       db_column: content  
     * @Length(max=500)
     */	
	private String content;
	
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
	public void setSendExtendId(Integer value) {
		this.sendExtendId = value;
	}
	
	public Integer getSendExtendId() {
		return this.sendExtendId;
	}
	public void setReceiveExtendId(Integer value) {
		this.receiveExtendId = value;
	}
	
	public Integer getReceiveExtendId() {
		return this.receiveExtendId;
	}
	public void setContent(String value) {
		this.content = value;
	}
	
	public String getContent() {
		return this.content;
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

