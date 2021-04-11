package com.gridone.scraping.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gridone.scraping.type.EnumScheduleType;

public class ScheduleModel {
	
	private Integer id;
	private Integer userId;
	private EnumScheduleType type;
	private String cron;
	private String nextTime;
	
	
	public ScheduleModel() {}
	public ScheduleModel(ScheduleModel schedule) {
		this.userId = schedule.getUserId();
		this.type = schedule.getType();
		this.cron = schedule.getCron();
		this.nextTime = schedule.getNextTime();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public EnumScheduleType getType() {
		return type;
	}
	public void setType(EnumScheduleType type) {
		this.type = type;
	}
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public String getNextTime() {
		return nextTime;
	}
	public void setNextTime(Date nextTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.nextTime = sdf.format(nextTime);
	}
	@Override
	public String toString() {
		return "ScheduleModel [id=" + id + ", userId=" + userId + ", type=" + type + ", cron=" + cron + ", nextTime="
				+ nextTime + "]";
	}
	
	

}
