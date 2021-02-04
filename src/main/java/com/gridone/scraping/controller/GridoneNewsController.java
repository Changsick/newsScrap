package com.gridone.scraping.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gridone.scraping.clusterAnalysis.ClusteringEngine;
import com.gridone.scraping.model.Keyword;
import com.gridone.scraping.model.ResultList;
import com.gridone.scraping.model.ScrapAttribute;
import com.gridone.scraping.model.SearchBase;
import com.gridone.scraping.morpheme.MorphemeAnalysis;
import com.gridone.scraping.service.KeywordService;
import com.gridone.scraping.service.NewsMonitoringService;
import com.gridone.scraping.service.NewsService;

@Controller
@RequestMapping("/newsScraping")
public class GridoneNewsController {
	
	@Autowired
	KeywordService keywordService;
	
	@Autowired
	NewsService newsService;
	
	@Autowired
	NewsMonitoringService newsMonitoringService;

	@RequestMapping("/dailyNews")
	public String dailyNews() {
//		keywordService.insertCsv();
//		newsMonitoringService.isSimilaritySample();
//		newsMonitoringService.executeNewsMonitoring();
//		newsMonitoringService.monitoringSendEmail();
		MorphemeAnalysis ma = new MorphemeAnalysis();
//		ma.analysisString();
		ma.extractor();
//		ClusteringEngine ce = new ClusteringEngine(10, 10, 10);
		return "dailyNews";
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
	
	@RequestMapping(value = "/getEnterprise.json", method = RequestMethod.POST)
	public @ResponseBody Keyword getEnterprise(@RequestParam Integer id){
		return keywordService.selectById(id);
	}
	
}
