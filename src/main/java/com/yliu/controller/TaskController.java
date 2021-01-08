package com.yliu.controller;

import com.yliu.bean.Result;
import com.yliu.bean.TaskLog;
import com.yliu.service.TaskLogService;
import com.yliu.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@Api(tags = "任务")
@RestController
@ResponseBody
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskLogService taskLogService;


    @ApiOperation(value = "task回调的方法")
    @PostMapping("/callback")
    public Result callback(TaskLog taskLog){
        taskLogService.updateLog(taskLog);
        return Result.ok();
    }

}
