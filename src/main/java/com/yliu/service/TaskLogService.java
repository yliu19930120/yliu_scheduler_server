package com.yliu.service;

import com.yliu.bean.TaskLog;
import com.yliu.dao.TaskLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskLogService {

    @Autowired
    private TaskLogDao taskLogDao;

    public void writeLog(TaskLog taskLog){
        taskLogDao.save(taskLog);
    }
}
