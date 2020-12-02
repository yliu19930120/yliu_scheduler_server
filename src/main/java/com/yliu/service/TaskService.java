package com.yliu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yliu.bean.Task;
import com.yliu.dao.TaskDao;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskDao taskDao;

    private final static Logger log = LoggerFactory.getLogger(TaskService.class);

    private List<Task> readTasks(){
        String json = null;

        try {
            File file = ResourceUtils.getFile("classpath:tasks.json");
            json = FileUtils.readFileToString(file);
            JSONObject jsonObj = JSON.parseObject(json);
            List<Task> tasks = jsonObj.getJSONArray("tasks").toJavaList(Task.class);
            log.info("读取task数量={}",tasks.size());
            return tasks;
        } catch (IOException e) {
            log.error("配置文件读取错误",e);
            return Collections.emptyList();
        }
    }

    public void restoreTasks(){
        List<Task> tasks = readTasks();

        taskDao.deleteAll();;

        taskDao.saveAll(tasks);
    }

    public List<Task> validTasks(){
        return taskDao.findAllByValidEquals("0");
    }
}
