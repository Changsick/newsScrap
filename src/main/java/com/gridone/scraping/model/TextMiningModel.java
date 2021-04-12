package com.gridone.scraping.model;

import java.util.Date;
import java.util.List;

public class TextMiningModel {
	
	private Integer id;
	private Integer userId;
	private Integer keywordId;
	private String top5;
	private String wordCloud;
	private Date newsDate;
	private Date registerDate;
	private List<String> top5Names;
	private List<String> top5Cnt;
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
	public Integer getKeywordId() {
		return keywordId;
	}
	public void setKeywordId(Integer keywordId) {
		this.keywordId = keywordId;
	}
	public String getTop5() {
		return top5;
	}
	public void setTop5(String top5) {
		this.top5 = top5;
	}
	public String getWordCloud() {
		return wordCloud;
	}
	public void setWordCloud(String wordCloud) {
		this.wordCloud = wordCloud;
	}
	public Date getNewsDate() {
		return newsDate;
	}
	public void setNewsDate(Date newsDate) {
		this.newsDate = newsDate;
	}
	
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public List<String> getTop5Names() {
		return top5Names;
	}
	public void setTop5Names(List<String> top5Names) {
		this.top5Names = top5Names;
	}
	public List<String> getTop5Cnt() {
		return top5Cnt;
	}
	public void setTop5Cnt(List<String> top5Cnt) {
		this.top5Cnt = top5Cnt;
	}
	@Override
	public String toString() {
		return "TextMiningModel [id=" + id + ", userId=" + userId + ", keywordId=" + keywordId + ", top5=" + top5
				+ ", wordCloud=" + wordCloud + ", newsDate=" + newsDate + ", registerDate=" + registerDate + "]";
	}
	
}
