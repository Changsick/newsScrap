package com.gridone.scraping.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gridone.scraping.model.TextMiningModel;


@Mapper
public interface TextMiningMapper {

	List<TextMiningModel> getMiningDataByUserIds(List<Integer> userIds);

	TextMiningModel getMiningDataByUserId(Integer userId);

	int insert(TextMiningModel data);

	
}
