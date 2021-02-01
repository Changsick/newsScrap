package com.gridone.scraping.model;

public class Keyword {
	
	private Integer id;
	private String enterprise;
	private String keywords;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
		return "Keyword [id=" + id + ", enterprise=" + enterprise + ", keywords=" + keywords + "]";
	}

}
