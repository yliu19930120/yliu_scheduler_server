package com.yliu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ObjectMapper objectMapper;

    private final static Logger log = LoggerFactory.getLogger(TaskService.class);

    private List<Task> readTasks(){

        try {
            File file = ResourceUtils.getFile("classpath:tasks.json");
            String json = FileUtils.readFileToString(file);
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

    public void restoreTasks(){
        List<Task> tasks = readTasks();

        taskDao.deleteAll();;

        taskDao.saveAll(tasks);
    }

    public List<Task> validTasks(){
        return taskDao.findAllByValidEquals("0");
    }
}