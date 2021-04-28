package com.yliu.service;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yliu.bean.Task;
import com.yliu.dao.TaskDao;
import com.yliu.tasks.HttpTrigger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private Scheduler scheduler;

    private final static Logger log = LoggerFactory.getLogger(TaskService.class);

    private List<Task> readTasks(){

        try {
            ClassPathResource classPathResource = new ClassPathResource("tasks.json");
            InputStream inputStream = classPathResource.getInputStream();
            String json = IOUtils.toString(inputStream);
            JsonNode jsonNode = objectMapper.readTree(json);

            JsonNode taskNode = jsonNode.findValue("tasks");

            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, Task.class);

            List<Task> tasks = objectMapper.convertValue(taskNode,javaType);

            log.info("读取task数量={}",tasks.size());
            return tasks;
        } catch (IOException e) {
            log.error("配置文件读取错误",e);
            return Collections.emptyList();
        }
    }

    public List<String> refreshTasks(){

        List<Task> tasks = validTasks();

        log.info("数据库读出task数量={}",tasks.size());
        List<String> msgs = new ArrayList<>();
        try {

            //先全部清空
            scheduler.clear();


            for(Task task:tasks){
                HashMap hashMap = objectMapper.convertValue(task, HashMap.class);

                JobDetail jobDetail = JobBuilder.newJob(HttpTrigger.class)
                        .withIdentity(task.getId())
                        .setJobData(new JobDataMap(hashMap))
                        .build();

                CronTrigger cronTrigger = TriggerBuilder.
                        newTrigger().
                        withIdentity(task.getId()).
                        withSchedule(CronScheduleBuilder.cronSchedule(task.getCron())). //在任务调度器中，使用任务调度器的 CronScheduleBuilder 来生成一个具体的 CronTrigger 对象
                        build();

                Date date = scheduler.scheduleJob(jobDetail,cronTrigger);

                String msg = String.format("任务 [%s]:[%s]下个运行时间为 %s"
                        ,task.getId()
                        ,task.getTaskName()
                        , DateFormatUtils.format(date
                                ,"yyyy-MM-dd HH:mm:ss", Locale.CHINA));


                log.info(msg);
                msgs.add(msg);
            }

            scheduler.start();
            log.info("调度程序启动完毕");
        } catch (Exception e) {
            log.error("启动失败", e);
        }

        return msgs;
    }

    public List<Task> validTasks(){
        return taskDao.findAllByValidEquals("0");
    }

    public void updateStatus(String taskId,String status){
        Query query=Query.query(Criteria.where("id").is(taskId));
        Update update = new Update().set("status", status);
        mongoTemplate.updateFirst(query,update,Task.class);
    }

    public void runTask(String taskId) throws SchedulerException {
        scheduler.triggerJob(JobKey.jobKey(taskId));
    }

    public List<String> addTask(Task task){
        if(task.getValid()==null){
            task.setValid("0");
        }
        taskDao.save(task);

        return refreshTasks();
    }

    public List<String> deleteTask(String... taskId){

        taskDao.deleteAllByIdIn(Arrays.asList(taskId));

        return refreshTasks();
    }

}
