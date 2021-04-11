package com.gridone.scraping.model;

import java.util.Date;

public class TextMiningModel {
	
	private Integer id;
	private Integer userId;
	private Integer keywordId;
	private String top5;
	private String wordCloud;
	private Date newsDate;
	private Date registerDate;
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
	@Override
	public String toString() {
		return "TextMiningModel [id=" + id + ", userId=" + userId + ", keywordId=" + keywordId + ", top5=" + top5
				+ ", wordCloud=" + wordCloud + ", newsDate=" + newsDate + ", registerDate=" + registerDate + "]";
	}
	
}
