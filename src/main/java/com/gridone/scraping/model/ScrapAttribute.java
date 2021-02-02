package com.gridone.scraping.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScrapAttribute {
	
	private Integer id;
	private String startDate;
	private String endDate;
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
	@Override
	public String toString() {
		return "ScrapAttribute [id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}
	
	
	

}