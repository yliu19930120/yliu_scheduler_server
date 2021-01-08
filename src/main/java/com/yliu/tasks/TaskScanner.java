package com.yliu.tasks;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yliu.bean.Task;
import com.yliu.service.TaskService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Component
public class TaskScanner {

	private final static Logger log = LoggerFactory.getLogger(TaskScanner.class);


	@Autowired
	private TaskService taskService;

	@Autowired
	private ObjectMapper objectMapper;


	@PostConstruct
	public void startup(){
		//屏蔽部分日志
		ch.qos.logback.classic.Logger offLog = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger("org.quartz");
		offLog.setLevel(Level.OFF);

		taskService.restoreTasks();

		List<Task> tasks = taskService.validTasks();

		log.info("数据库读出task数量={}",tasks.size());

		try {

			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

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

				Date date = cronTrigger.getFireTimeAfter(new Date());
				scheduler.scheduleJob(jobDetail,cronTrigger);
				log.info("任务 [{}]下个运行时间为 {}",task.getTaskName(), DateFormatUtils.format(date,"yyyy-MM-dd HH:mm:ss", Locale.CHINA));
			}

			scheduler.start();
			log.info("调度程序启动完毕");
		} catch (Exception e) {
			log.error("启动失败", e);
		}
	}

}
