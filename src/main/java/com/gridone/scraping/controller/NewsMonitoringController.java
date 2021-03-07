package com.gridone.scraping.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gridone.scraping.model.Keyword;
import com.gridone.scraping.model.LoginUserDetails;
import com.gridone.scraping.model.NewsData;
import com.gridone.scraping.model.ResultList;
import com.gridone.scraping.model.SearchBase;
import com.gridone.scraping.service.NewsMonitoringService;
import com.gridone.scraping.service.NewsService;

@Controller
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
	
	@PostMapping(value = "/monitoringScrap.json")
	public @ResponseBody Map<String, Object> monitoringScrap() {
		LoginUserDetails user = (LoginUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return newsMonitoringService.executeNewsMonitoring(user);
	}
	
//	@Scheduled(cron="0 30 8 ? * *") 
	@Scheduled(cron="0 30 8 ? * FRI") 
	public void monitoringSendEmail() {
		newsMonitoringService.monitoringSendEmail();
	}
	
	@GetMapping(value = "/monitoringNews")
	public String monitoringNews() {
		return "monitoringNews";
	}
	
	@RequestMapping(value = "/monitoringNewsList.json", method = RequestMethod.POST)
	public @ResponseBody ResultList newsList(@ModelAttribute("searchBaseNews") SearchBase searchBase){
		return newsMonitoringService.newsList(searchBase);
	}
	
	@PostMapping(value = "/deleteMonitoringNewsByKeyword.json")
	public @ResponseBody Map<String, Object> deleteNewsByKeyword(Keyword param){
		return newsMonitoringService.deleteByKeywordId(param);
	}
	
	@PostMapping(value = "/checkMonitoringScrapStatus")
	public @ResponseBody boolean checkMonitoringScrapStatus(){
		return newsMonitoringService.checkScrapStatus();
	}
	
}
