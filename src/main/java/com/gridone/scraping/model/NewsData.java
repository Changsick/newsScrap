package com.gridone.scraping.model;

import java.util.Date;

public class NewsData extends SearchBase {

	private Integer newsId;
	private Integer keywordId;
	private String title;
	private String link;
	private String content;
	private Date regitdate;
	private Date newsdate;
	private String press;
	private String enterprise;
	private String keywords;
	
	public NewsData() {
		super();
		// TODO Auto-generated constructor stub
	}
		
	public NewsData(Integer newsId, Integer keywordId, String title, String link, String content, Date regitdate,
			Date newsdate, String press) {
		super();
		this.newsId = newsId;
		this.keywordId = keywordId;
		this.title = title;
		this.link = link;
		this.content = content;
		this.regitdate = regitdate;
		this.newsdate = newsdate;
		this.press = press;
	}
	
	public NewsData(NewsMonitoring data) {
		this.keywordId = data.getKeywordId();
		this.title = data.getTitle();
		this.link = data.getLink();
		this.content = data.getContent();
		this.regitdate = data.getRegitdate();
		this.newsdate = data.getNewsdate();
		this.press = data.getPress();
	}

	public Integer getNewsId() {
		return newsId;
	}
	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}
	public Integer getKeywordId() {
		return keywordId;
	}
	public void setKeywordId(Integer keywordId) {
		this.keywordId = keywordId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getRegitdate() {
		return regitdate;
	}
	public void setRegitdate(Date regitdate) {
		this.regitdate = regitdate;
	}
	public Date getNewsdate() {
		return newsdate;
	}
	public void setNewsdate(Date newsdate) {
		this.newsdate = newsdate;
	}
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
	
	public String getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Override
	public String toString() {
		return "NewsData [newsId=" + newsId + ", keywordId=" + keywordId + ", title=" + title + ", link=" + link
				+ ", content=" + content + ", regitdate=" + regitdate + ", newsdate=" + newsdate + ", press=" + press
				+ "]";
	}
	
}
