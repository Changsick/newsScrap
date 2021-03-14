package com.gridone.scraping.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScrapAttribute {
	
	private Integer id;
	private String startDate;
	private String endDate;
	private Integer userId;
	private String emailTo;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate.replaceAll("-", ".");
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate.replaceAll("-", ".");
	}
	public String getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	@Override
	public String toString() {
		return "ScrapAttribute [id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", userId=" + userId
				+ ", emailTo=" + emailTo + "]";
	}
	
	
	
}
