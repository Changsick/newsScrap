package com.gridone.scraping.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gridone.scraping.mapper.KeywordMapper;
import com.gridone.scraping.mapper.NewsMapper;
import com.gridone.scraping.model.Keyword;
import com.gridone.scraping.model.LoginUserDetails;
import com.gridone.scraping.model.ResultList;
import com.gridone.scraping.model.SearchBase;

@Service
public class KeywordService {
	
	@Autowired
	KeywordMapper keywordMapper;
	
	@Autowired
	NewsMapper newsMapper;
	
	public ResultList selectSearchList(SearchBase searchBase) {
		searchBase.setRecordCountPerPage(20);
		ResultList resultList = new ResultList(searchBase);
		resultList.setResultList(keywordMapper.selectSearchList(searchBase));
		resultList.setTotalRecordCount(keywordMapper.selectSearchListCount(searchBase));
		return resultList;
	}
	
	public List<Keyword> selectAll() {
		return keywordMapper.selectAll();
	}
	
	public Keyword selectById(Integer id) {
		return keywordMapper.selectById(id);
	}
	
	public String convertKeyword(Keyword keyword) {
		String result = "";
		String[] keywords = keyword.getKeywords().split(",");
		for(int i = 0; i < keywords.length; i++) {
			result += "%20" + "\"" + keywords[i].trim() + "\""; 
		}
		String enterprise = "";
		try {
			enterprise = URLEncoder.encode(keyword.getEnterprise(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		result = enterprise + result;
		return result;
	}

	public Map<String, Object> addEnterprise(Keyword param) {
		Map<String, Object> resultVal = new HashMap<>();
		boolean result = false;
		try {			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(!auth.getPrincipal().equals("anonymousUser")) {				
				LoginUserDetails userDetails = (LoginUserDetails) auth.getPrincipal();
				param.setUserId(userDetails.getId());
				Integer insert = keywordMapper.addEnterprise(param);
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();			
		}
		resultVal.put("result", result);
		return resultVal;
	}
	
	public void insertCsv() {
		List<Keyword> list = new ArrayList<>();
		ClassPathResource resource = new ClassPathResource("csv/keywords.csv");
		
		try {
			File file = resource.getFile();
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF8"));
			String line = "";
			int i = 0;
			while ((line = br.readLine()) != null) {
				String enterprise = line.split("\"")[0];
				enterprise = enterprise.substring(0, enterprise.indexOf(","));
				String keywords = line.split("\"")[1];
//				System.out.println("keywords : "+keywords);
				Keyword data = new Keyword();
				data.setEnterprise(enterprise);
				data.setKeywords(keywords);
				keywordMapper.addEnterprise(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Keyword modifyEnterprise(Keyword param) {
		Map<String, Object> resultVal = new HashMap<>();
		boolean result = true;
		Integer update = keywordMapper.modifyEnterprise(param);
		resultVal.put("result", result);
		return param;
	}

	public Keyword deleteEnterprise(Keyword param) {
		Map<String, Object> resultVal = new HashMap<>();
		boolean result = true;
		Integer deleteNews = newsMapper.deleteNewsByKeywordId(param);
		Integer delete = keywordMapper.deleteEnterprise(param);
		resultVal.put("result", result);
		return param;
	}
}
