package com.yliu.bean;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "taskLog")
public class TaskLog extends Bean{

    private String taskId;
    private String logId;
    private String taskName;
    private String msg;

    /**
     * 状态 0空闲,1处理中,2处理成功,3处理失败
     */
    private String status;

    public static final String FREED = "0";
    public static final String RUNNING = "1";
    public static final String SUCC = "2";
    public static final String FAIL = "3";


    public TaskLog() {
    }


    public TaskLog(String taskId, String logId) {
        this.taskId = taskId;
        this.logId = logId;
    }



    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public static TaskLog of(String taskId, String logId){
        TaskLog taskLog = new TaskLog(taskId,logId);
        return taskLog;
    }

    public TaskLog msg(String msg){
        this.setMsg(msg);
        return this;
    }

    public TaskLog status(String status){
        this.setStatus(status);
        return this;
    }

}
