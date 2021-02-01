package com.gridone.scraping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gridone.scraping.component.WebScraping;
import com.gridone.scraping.model.Keyword;
import com.gridone.scraping.model.ResultList;
import com.gridone.scraping.model.SearchBase;
import com.gridone.scraping.service.KeywordService;
import com.gridone.scraping.service.NewsService;

@Controller
@RequestMapping("/newsScrapingTest")
public class TestController {

	@Autowired
	WebScraping webScraping;
	
	@Autowired
	KeywordService keywordService;
	
	@Autowired
	NewsService newsService;
	
	@RequestMapping("/test")
	public String test() {
		/*List<Keyword> list = keywordService.selectAll();
		System.err.println("list : "+list);
		for (Keyword k : list) {
			String keywords = k.getEnterprise() + " \"" + k.getKeywords().split(",")[0] + "\"" + " \"" + k.getKeywords().split(",")[1]+ "\"";
//			System.err.println("keywords : "+keywords);
		}
		webScraping.shceduledItemList();*/
//		webScraping.sendEmail();
		return "dailyNews";
	}
	
	@RequestMapping("/test2")
	public String test2() {
		/*List<Keyword> list = keywordService.selectAll();
		System.err.println("list : "+list);
		for (Keyword k : list) {
			String keywords = k.getEnterprise() + " \"" + k.getKeywords().split(",")[0] + "\"" + " \"" + k.getKeywords().split(",")[1]+ "\"";
//			System.err.println("keywords : "+keywords);
		}
		Keyword uiPath = list.get(list.size()-1);
		String keywords = uiPath.getEnterprise() + "%20\"" + uiPath.getKeywords().split(",")[0] + "\"" + "%20\"" + uiPath.getKeywords().split(",")[1]+ "\"";
		System.err.println("uiPath : "+uiPath);
		System.err.println("keywords : "+keywords);
		try {
			newsService.getNewsScraping(keywords, uiPath);
			Thread.sleep((int)(Math.random()*3000)+500);
		} catch (Exception e) {
			e.printStackTrace();
		}	*/	
		
		return "dailyNews";
	}
	
	
	
	
	
}
