package com.perenc.xh.lsp.entity.tcMessage;

import javax.persistence.Table;


@Table(name="tc_message")
public class TcMessage implements java.io.Serializable{

	private static final long serialVersionUID = -2847061404972698095L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;
	
    /**
     * 标题       db_column: title  
     * @Length(max=500)
     */	
	private String title;
	
    /**
     * 内容       db_column: content  
     * @Length(max=500)
     */	
	private String content;
	
    /**
     * 创建用户ID 关联usr_extend表的id       db_column: extend_id  
     * @NotNull 
     */	
	private Integer extendId;
	
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
	public void setTitle(String value) {
		this.title = value;
	}
	
	public String getTitle() {
		return this.title;
	}
	public void setContent(String value) {
		this.content = value;
	}
	
	public String getContent() {
		return this.content;
	}
	public void setExtendId(Integer value) {
		this.extendId = value;
	}
	
	public Integer getExtendId() {
		return this.extendId;
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

