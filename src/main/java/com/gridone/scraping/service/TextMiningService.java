package com.gridone.scraping.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gridone.scraping.mapper.TextMiningMapper;
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

}
