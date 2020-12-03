package com.yliu.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yliu.bean.BaseResult;
import com.yliu.bean.Task;
import com.yliu.bean.TaskLog;
import com.yliu.service.TaskLogService;
import com.yliu.utils.SpringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class HttpTrigger implements Job {

    private final static Logger log = LoggerFactory.getLogger(HttpTrigger.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
        TaskLogService taskLogService = SpringUtils.getBean(TaskLogService.class);
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        try {
            Task task = objectMapper.convertValue(jobDataMap,Task.class);
            log.info("执行 task {} 发送请求到 {}",task.getTaskName(),task.getUrl());
            RestTemplate restTemplate = new RestTemplate();
            BaseResult result = restTemplate.getForObject(task.getUrl(), BaseResult.class);
            if(result.getCode()==200){
                log.info("任务触发成功");
                taskLogService.writeLog(new TaskLog(task.getId(),task.getTaskName(),"成功",HttpTrigger.class.getName(), TaskLog.SUCC));
            }else {
                log.error("任务触发失败 {}",result.getMessage());
                taskLogService.writeLog(new TaskLog(task.getId(),task.getTaskName(),result.getMessage(),HttpTrigger.class.getName(),TaskLog.FAIL));
            }
        } catch (Exception e) {
            log.error("任务触发失败",e);
            taskLogService.writeLog(new TaskLog(jobDataMap.getString("id"),jobDataMap.getString("taskName"),e.getMessage(),HttpTrigger.class.getName(),TaskLog.FAIL));
        }

    }
}
