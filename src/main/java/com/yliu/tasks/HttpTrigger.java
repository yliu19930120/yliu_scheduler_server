package com.yliu.tasks;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yliu.bean.BaseResult;
import com.yliu.bean.Task;
import com.yliu.utils.JsonUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class HttpTrigger implements Job {

    private final static Logger log = LoggerFactory.getLogger(HttpTrigger.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Task task = JSONObject.toJavaObject(new JSONObject(jobDataMap),Task.class);
        log.info("执行 task {} 发送请求到 {}",task.getTaskName(),task.getUrl());
        RestTemplate restTemplate = new RestTemplate();
        BaseResult result = restTemplate.getForObject(task.getUrl(), BaseResult.class);
        log.info("result = {}",JsonUtils.toJson(result));
    }
}
