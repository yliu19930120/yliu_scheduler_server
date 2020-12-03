package com.yliu.bean;

import javax.persistence.Id;

public class Task extends Bean{

	private String url;
	private String cron;
	private String taskName;
	/**
	 * 是否有效，0有效，1无效
	 */
	private String valid;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}
}
