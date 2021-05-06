package com.gridone.scraping.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gridone.scraping.mapper.TextMiningMapper;
import com.gridone.scraping.model.LoginUserDetails;
import com.gridone.scraping.model.ResultList;
import com.gridone.scraping.model.TextMiningModel;

@Service
public class TextMiningService {
	
	@Autowired
	TextMiningMapper textMiningMapper;

	public List<TextMiningModel> getMiningDataByUserIds(List<Integer> userIds) {
		return textMiningMapper.getMiningDataByUserIds(userIds);
	}

	public TextMiningModel getMiningDataByUserId(Integer userId) {
		return textMiningMapper.getMiningDataByUserId(userId);
	}

	public Map<String, Object> textminingData(TextMiningModel param) {
		Map<String, Object> resultVal = new HashMap<>();
		boolean result = false;
		try {			
			TextMiningModel tmm = textMiningMapper.getTextminingData(param);
			if(tmm != null) {
				String txt = tmm.getTop5().substring(1, tmm.getTop5().length()-1);
				List<String> names = new ArrayList<String>();
				List<String> cnt = new ArrayList<String>();
				for (String t : txt.split(",")) {
					t = t.replace(")", "").replace("(", ",");
					names.add(t.split(",")[0]);
					cnt.add(t.split(",")[1]);
				}
				tmm.setTop5Names(names);
				tmm.setTop5Cnt(cnt);
			}
			resultVal.put("data", tmm);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();			
			resultVal.put("msg", e.getMessage());
		}
		resultVal.put("result", result);
		return resultVal;
	}

}
