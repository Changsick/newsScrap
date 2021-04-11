package com.gridone.scraping.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gridone.scraping.model.Keyword;
import com.gridone.scraping.model.NewsData;
import com.gridone.scraping.model.NewsMonitoring;
import com.gridone.scraping.model.ScrapAttribute;
import com.gridone.scraping.model.SearchBase;

@Mapper
public interface NewsMapper {

	Integer insertNews(NewsData news);

	List<NewsData> newsList(SearchBase searchBase);

	Integer newsListCount(SearchBase searchBase);

	Integer getNewsByTitle(NewsData news);

	Integer deleteNewsByKeywordId(Keyword param);

	List<NewsMonitoring> getMonitoringNews(ScrapAttribute param);

	List<NewsData> getNewsForMail(ScrapAttribute param);

	List<NewsData> getNewsForMining(ScrapAttribute param);

}
