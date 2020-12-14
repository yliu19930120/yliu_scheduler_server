package com.yliu.bean;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "taskLog")
public class TaskLog extends Bean{

    private String taskId;
    private String taskName;
    private String msg;
    private String objectName;
    /**
     * 状态 1成功,0失败
     */
    private String status;

    public static final String SUCC = "1";
    public static final String FAIL = "0";

    public TaskLog() {
    }

    public TaskLog(String taskId, String msg, String objectName) {
        this.taskId = taskId;
        this.msg = msg;
        this.objectName = objectName;
    }

    public TaskLog(String taskId, String taskName, String msg, String objectName) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.msg = msg;
        this.objectName = objectName;
    }

    public TaskLog(String taskId, String taskName, String msg, String objectName, String status) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.msg = msg;
        this.objectName = objectName;
        this.status = status;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

}
