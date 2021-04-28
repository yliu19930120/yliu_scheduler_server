package com.yliu.controller;

import com.yliu.bean.Result;
import com.yliu.bean.Task;
import com.yliu.bean.TaskLog;
import com.yliu.service.TaskLogService;
import com.yliu.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "任务")
@RestController
@ResponseBody
@RequestMapping("/task")
public class TaskController {

    private final static Logger log = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskLogService taskLogService;

    @Autowired
    private TaskService taskService;

    @ApiOperation(value = "task回调的方法")
    @PostMapping("/callback")
    public Result callback(TaskLog taskLog) {
        taskLogService.updateLog(taskLog);
        return Result.ok();
    }

    @ApiOperation(value = "调用某个task的方法")
    @GetMapping("/run")
    public Result runTask(String taskId) {
        try {
            taskService.runTask(taskId);
        } catch (SchedulerException e) {
            log.error("任务{}触发失败", taskId);
            log.error("失败堆栈", e);
            return Result.failue(e.getMessage());
        }
        return Result.ok();
    }

    @ApiOperation(value = "刷新task")
    @GetMapping("/refresh")
    public Result refreshTasks() {
        List<String> msgs = taskService.refreshTasks();
        return Result.ok(msgs);
    }

    @ApiOperation(value = "增加或更新task")
    @PostMapping("/add")
    public Result addTask(Task task) {
        List<String> msg = taskService.addTask(task);
        return Result.ok(msg);
    }

    @ApiOperation(value = "删除task")
    @PostMapping("/delete")
    public Result deleteTasks(String... ids) {
        List<String> msg = taskService.deleteTask(ids);
        return Result.ok(msg);
    }

    @ApiOperation(value = "更新task")
    @PostMapping("/update")
    public Result updateTask(Task task) {
        List<String> msg = taskService.addTask(task);
        return Result.ok(msg);
    }
}
