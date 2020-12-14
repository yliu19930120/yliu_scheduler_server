package com.yliu.controller;

import com.yliu.bean.Result;
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

    private final static Logger log = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @ApiOperation(value = "task运行中")
    @PostMapping("/running")
    public Result taskRunning(@RequestParam("id") String id){
        taskService.updateStatus(id,"1");
        return Result.ok();
    }

    @ApiOperation(value = "task空闲")
    @PostMapping("/freed")
    public Result taskFreed(@RequestParam("id") String id){
        taskService.updateStatus(id,"0");
        return Result.ok();
    }


}
