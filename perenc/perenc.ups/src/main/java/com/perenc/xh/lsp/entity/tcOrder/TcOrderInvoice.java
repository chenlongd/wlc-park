package com.perenc.xh.lsp.entity.tcOrder;

import javax.persistence.Table;


@Table(name="tc_order_invoice")
public class TcOrderInvoice implements java.io.Serializable{


	private static final long serialVersionUID = 4760220886934244885L;
	
	//columns START
    /**
     * 序号       db_column: id  
     * 
     */
	private String id;

	/**
	 * 客户ID 关联tc_extend表的id       db_column: extend_id
	 * @NotNull
	 */
	private Integer extendId;
	
    /**
     * 订单ID 关联tc_order表的id       db_column: order_id  
     * @NotNull 
     */	
	private Integer orderId;
	
    /**
     * 发票ID 关联tc_invoice表的id       db_column: invoice_id  
     * @NotNull 
     */	
	private String invoiceId;
	
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

	public Integer getExtendId() {
		return extendId;
	}

	public void setExtendId(Integer extendId) {
		this.extendId = extendId;
	}

	public void setOrderId(Integer value) {
		this.orderId = value;
	}
	
	public Integer getOrderId() {
		return this.orderId;
	}
	public void setInvoiceId(String value) {
		this.invoiceId = value;
	}
	
	public String getInvoiceId() {
		return this.invoiceId;
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

