package com.yliu.dao;

import com.yliu.bean.TaskLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskLogDao extends MongoRepository<TaskLog,String> {

}
