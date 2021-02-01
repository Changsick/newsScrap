package com.gridone.scraping.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gridone.scraping.wordcloud.WordCount;

public class SendMinigNews {

	private String enterprise;
	
	private HashMap<String, Object> miningText;
	
	private List<NewsMonitoring> newsList;

	public String getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}

	public HashMap<String, Object> getMiningText() {
		return miningText;
	}

	public void setMiningText(HashMap<String, Object> miningText) {
		this.miningText = miningText;
	}

	public List<NewsMonitoring> getNewsList() {
		return newsList;
	}

	public void setNewsList(List<NewsMonitoring> newsList) {
		this.newsList = newsList;
	}

	@Override
	public String toString() {
		return "SendMinigNews [enterprise=" + enterprise + ", miningText=" + miningText + ", newsList=" + newsList
				+ "]";
	}
	
	
	
}
