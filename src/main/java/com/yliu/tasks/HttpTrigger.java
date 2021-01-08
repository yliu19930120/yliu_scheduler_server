package com.yliu.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yliu.bean.BaseResult;
import com.yliu.bean.Task;
import com.yliu.bean.TaskLog;
import com.yliu.service.TaskLogService;
import com.yliu.utils.SpringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpTrigger implements Job {

    private final static Logger log = LoggerFactory.getLogger(HttpTrigger.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
        TaskLogService taskLogService = SpringUtils.getBean(TaskLogService.class);
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String logId = UUID.randomUUID().toString();

        try {
            Task task = objectMapper.convertValue(jobDataMap,Task.class);
            log.info("执行 task {} 发送请求到 {}",task.getTaskName(),task.getUrl());
            RestTemplate restTemplate = new RestTemplate();

            String url = String.format("%s?%s&%s",task.getUrl(),task.getId(),logId);

            BaseResult result = restTemplate.getForObject(url, BaseResult.class);

            if(result.getCode()!=200){
                log.error("任务触发失败 {}",result.getMessage());
                taskLogService.writeLog(TaskLog.of(task.getId(),logId)
                        .msg(result.getMessage())
                        .status(TaskLog.FAIL)
                .objectName(HttpTrigger.class.getName()));
            }
        } catch (Exception e) {
            log.error("任务触发失败",e);
            taskLogService.writeLog(TaskLog.of(jobDataMap.getString("id"),logId)
                    .msg(e.getMessage())
                    .status(TaskLog.FAIL)
                    .objectName(HttpTrigger.class.getName()));
        }

    }
}
