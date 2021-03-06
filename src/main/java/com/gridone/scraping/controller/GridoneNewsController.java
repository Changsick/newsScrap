package com.gridone.scraping.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gridone.scraping.model.Keyword;
import com.gridone.scraping.model.ResultList;
import com.gridone.scraping.model.ScrapAttribute;
import com.gridone.scraping.model.SearchBase;
import com.gridone.scraping.model.TextMiningModel;
import com.gridone.scraping.service.KeywordService;
import com.gridone.scraping.service.NewsMonitoringService;
import com.gridone.scraping.service.NewsService;
import com.gridone.scraping.service.TextMiningService;

@Controller
public class GridoneNewsController {
	
	@Autowired
	KeywordService keywordService;
	
	@Autowired
	NewsService newsService;
	
	@Autowired
	NewsMonitoringService newsMonitoringService;
	
	@Autowired
	TextMiningService textMiningService;

	@RequestMapping("/dailyNews")
	public String dailyNews() {
//		keywordService.insertCsv(); // 144번 vm과 같은 내용의 csv
//		newsMonitoringService.isSimilaritySample(); // 유사도 테스트
//		newsMonitoringService.executeNewsMonitoring(); // 모니터링 집계 테스트
//		newsMonitoringService.monitoringSendEmail(); // 모니터링데이터 메일전송 테스트
//		ClusteringEngine ce = new ClusteringEngine(10, 10, 10); // 군집 테스트,,,, 아 왜안됨
//		  EnPosta posta = new EnPosta(); // 영어 형태소 테스트 중
//
//	       posta.load("model");
//
//	       //사용자 사전 추가
//
//	       posta.appendUserDic("dic.user");
//
//	       posta.buildFailLink();
//
//	      
//
//	       List<String> resultList = posta.analyze("Launch a new institute at the University of Washington to conduct independent, rigorous evaluations of health programs worldwide.");
//
//	       for (String result : resultList) {
//
//	             System.out.println(result);
//
//	       }
		return "dailyNews";
	}
	
	@RequestMapping("/main")
	public String main(){
		return "main";
	}
	
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
	
	@RequestMapping("/register")
	public String register(){
		return "register";
	}
	
	@RequestMapping("/test")
	public String test(){
		return "email2";
	}
	
	@RequestMapping(value = "/enterpriseList.json", method = RequestMethod.POST)
	public @ResponseBody ResultList enterpriseList(@ModelAttribute("searchBaseEnter") SearchBase searchBase){
		return keywordService.selectSearchList(searchBase);
	}
	
	@RequestMapping(value = "/newsList.json", method = RequestMethod.POST)
	public @ResponseBody ResultList newsList(@ModelAttribute("searchBaseNews") SearchBase searchBase){
		return newsService.newsList(searchBase);
	}
	
	@RequestMapping(value = "/certainNewsInsert", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> certainNewsInsert(ScrapAttribute param) {
		return newsService.certainNewsInsert(param);
	}
	
	@RequestMapping(value = "/allNewsInsert", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> allNewsInsert(ScrapAttribute param) {
		return newsService.allNewsInsert(param);
//		return null;
	}
	
	@RequestMapping(value = "/addEnterprise.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> addEnterprise(Keyword param){
		return keywordService.addEnterprise(param);
	}
	
	@RequestMapping(value = "/modifyEnterprise.json", method = RequestMethod.POST)
	public @ResponseBody Keyword modifyEnterprise(Keyword param){
		return keywordService.modifyEnterprise(param);
	}
	
	@RequestMapping(value = "/deleteEnterprise.json", method = RequestMethod.POST)
	public @ResponseBody Keyword deleteEnterprise(Keyword param){
		return keywordService.deleteEnterprise(param);
	}
	
	@RequestMapping(value = "/deleteEnterpriseAll.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteEnterpriseAll(){
		return keywordService.deleteEnterpriseAll();
	}
	
	@RequestMapping(value = "/getEnterprise.json", method = RequestMethod.POST)
	public @ResponseBody Keyword getEnterprise(@RequestParam Integer id){
		return keywordService.selectById(id);
	}
	
	@PostMapping(value = "/sendMail")
	public @ResponseBody Map<String, Object> sendHistoryMail(ScrapAttribute param){
		System.out.println("send Mail : "+param);
		return newsService.sendHistoryMail(param);
//		return null;
	}
	
	@PostMapping( value = "/checkScrapStatus")
	public @ResponseBody boolean checkScrapStatus(){
		return newsService.checkScrapStatus();
	}
	
	@PostMapping(value = "/deleteNewsByKeyword.json")
	public @ResponseBody Map<String, Object> deleteNewsByKeyword(Keyword param){
		return newsService.deleteNewsByKeyword(param);
	}
	
	@PostMapping(value = "/textminingData.json")
	public @ResponseBody Map<String, Object> textminingData(TextMiningModel param){
		return textMiningService.textminingData(param);
	}
	
	@PostMapping(value = "/getNewsByNewsDate.json")
	public @ResponseBody ResultList getNewsByNewsDate(@ModelAttribute("searchBaseNews") SearchBase searchBase){
		return newsService.getNewsByNewsDate(searchBase);
	}
	
}
