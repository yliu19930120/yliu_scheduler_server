package com.yliu.dao;

import com.yliu.bean.Task;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskDao extends MongoRepository<Task,String> {

    List<Task> findAllByValidEquals(String valid);

    void deleteAllByIdIn(List<String> ids);
}
