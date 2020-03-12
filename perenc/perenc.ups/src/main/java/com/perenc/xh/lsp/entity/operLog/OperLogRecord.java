package com.perenc.xh.lsp.entity.operLog;

import java.io.Serializable;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/3/25 9:53
 **/
public class OperLogRecord implements Serializable {

    private static final long serialVersionUID = -3645896965467885240L;

    private String id;

    private String operationType;

    private String operationName;

    private String operationUser;

    private String operationStartTime;

    private String operationEndTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }

    public String getOperationStartTime() {
        return operationStartTime;
    }

    public void setOperationStartTime(String operationStartTime) {
        this.operationStartTime = operationStartTime;
    }

    public String getOperationEndTime() {
        return operationEndTime;
    }

    public void setOperationEndTime(String operationEndTime) {
        this.operationEndTime = operationEndTime;
    }
}
