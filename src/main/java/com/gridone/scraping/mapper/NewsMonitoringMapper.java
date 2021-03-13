package com.gridone.scraping.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gridone.scraping.model.Keyword;
import com.gridone.scraping.model.NewsData;
import com.gridone.scraping.model.NewsMonitoring;
import com.gridone.scraping.model.SearchBase;

@Mapper
public interface NewsMonitoringMapper {

	List<NewsMonitoring> selectByKeywordId(Integer keyId);

	void insertNewsMonitoring(NewsMonitoring news);

	List<NewsMonitoring> getMonitoringNews();

	void deleteAll();

	Integer deleteByKeywordId(Keyword param);

	List<?> newsList(SearchBase searchBase);

	Integer newsListCount(SearchBase searchBase);

}
