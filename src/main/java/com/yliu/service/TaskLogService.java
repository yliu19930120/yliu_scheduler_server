package com.yliu.service;

import com.yliu.bean.Task;
import com.yliu.bean.TaskLog;
import com.yliu.dao.TaskLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskLogService {

    @Autowired
    private TaskLogDao taskLogDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void writeLog(TaskLog taskLog){
        taskLogDao.save(taskLog);
    }

    public void updateLog(TaskLog taskLog){
        Query query=Query.query(Criteria.where("taskId").is(taskLog.getTaskId()).andOperator(Criteria.where("logId").is(taskLog.getLogId())));

        Update update = new Update()
                .set("status", taskLog.getStatus())
                .set("msg",taskLog.getMsg())
                .set("updateDate", LocalDateTime.now())
                ;

        mongoTemplate.updateFirst(query,update,TaskLog.class);
    }
}
