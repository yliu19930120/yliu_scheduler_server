package com.yliu.tasks;

import ch.qos.logback.classic.Level;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class TaskConfig {

	private final static Logger log = LoggerFactory.getLogger(TaskConfig.class);

	@Bean
	public Scheduler getScheduler() throws SchedulerException {
		//屏蔽部分日志
		ch.qos.logback.classic.Logger offLog = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger("org.quartz");
		offLog.setLevel(Level.OFF);

		return StdSchedulerFactory.getDefaultScheduler();
	}


}
