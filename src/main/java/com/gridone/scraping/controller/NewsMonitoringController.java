package com.gridone.scraping.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gridone.scraping.model.NewsData;
import com.gridone.scraping.service.NewsMonitoringService;
import com.gridone.scraping.service.NewsService;

@Controller
@RequestMapping("/newsMonitoring")
public class NewsMonitoringController {

	@Autowired
	NewsMonitoringService newsMonitoringService;
	
	HashMap<Integer, HashMap<String, Object>> result = new HashMap<>();
	
//	@Scheduled(cron="0 0/5 * * * *")
	@Scheduled(cron="0 0 0/1 ? * *") 
	public void scrapMonitoringData() {
		newsMonitoringService.executeNewsMonitoring();
		System.err.println("######end");
	}
	
//	@Scheduled(cron="0 30 8 ? * *") 
	@Scheduled(cron="0 30 8 ? * FRI") 
	public void monitoringSendEmail() {
		newsMonitoringService.monitoringSendEmail();
	}
	
}
