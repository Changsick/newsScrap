package com.gridone.scraping.model;

import java.util.Date;

public class NewsMonitoring extends Keyword{
	
	private Integer monitoringId;
	private Integer keywordId;
	private String title;
	private String link;
	private String content;
	private Date regitdate;
	private Date newsdate;
	private String press;
	
	public NewsMonitoring() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NewsMonitoring(Integer monitoringId, Integer keywordId, String title, String link, String content, Date regitdate,
			Date newsdate, String press) {
		super();
		this.monitoringId = monitoringId;
		this.keywordId = keywordId;
		this.title = title;
		this.link = link;
		this.content = content;
		this.regitdate = regitdate;
		this.newsdate = newsdate;
		this.press = press;
	}
	
	public Integer getMonitoringId() {
		return monitoringId;
	}
	public void setMonitoringId(Integer monitoringId) {
		this.monitoringId = monitoringId;
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

	@Override
	public String toString() {
		return "NewsMonitoring [monitoringId=" + monitoringId + ", keywordId=" + keywordId + ", title=" + title
				+ ", link=" + link + ", content=" + content + ", regitdate=" + regitdate + ", newsdate=" + newsdate
				+ ", press=" + press + "]";
	}
	
	

	
	
}
