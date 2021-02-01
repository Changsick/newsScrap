package com.gridone.scraping.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gridone.scraping.mapper.NewsMapper;
import com.gridone.scraping.model.Keyword;
import com.gridone.scraping.model.NewsData;
import com.gridone.scraping.model.ResultList;
import com.gridone.scraping.model.ScrapAttribute;
import com.gridone.scraping.model.SearchBase;

@Service
public class NewsService {
	
	private static String KOREA_COVID_DATAS_URL = "https://search.naver.com/search.naver?query=keyword&where=news&pd=2&sort=1";//&field=1
	private static String KOREA_COVID_DATAS_URL2 = "https://search.naver.com/search.naver?query=keyword&where=news&pd=3&ds=startDate&de=endDate&sort=1";//&field=1
	//pd=3&ds=2020.01.01&de=2021.01.13
	
	private List<Map<String, Object>> errList = new ArrayList<>();
	
	@Autowired
	NewsMapper newsMapper;
	
	@Autowired
	KeywordService keywordService;
	
	public void getNewsScraping(String item, Keyword k, ScrapAttribute param, int interval) {
		String sendUrl =  null;
		
		if(interval == 0) {
			return;
		}
		
		try {
			sendUrl = KOREA_COVID_DATAS_URL2.replace("keyword", item);
			sendUrl = sendUrl.replace("startDate", param.getStartDate());
			sendUrl = sendUrl.replace("endDate", param.getEndDate());
			
			Document document = Jsoup.connect(sendUrl).timeout(5000).get(); // read time out 일 때 catch에서 재귀로 2번 더 시도함
			Elements elements = document.select(".list_news > li");
			Elements nextPageElement = document.select(".btn_next");
			
			if(elements.size() == 0) { // 검색결과가 없을 때
				return;
			}
			
			boolean isNext = true;
			int i = 0;
			while (isNext) {
				if(i > 0) {					
					String nextUrl = sendUrl.split("\\?")[0] + nextPageElement.attr("href");
//					document = Jsoup.connect(nextUrl).timeout((int)(Math.random()*5000)+500).get();
					document = Jsoup.connect(nextUrl).timeout(5000).get();
					elements = document.select(".list_news > li");
					nextPageElement = document.select(".btn_next");
				}
				
				if(nextPageElement.attr("aria-disabled").length() == 0) {
					isNext =false;
				}else {					
					isNext = !Boolean.parseBoolean(nextPageElement.attr("aria-disabled"));
				}
				
				for(Element element : elements) {
					NewsData news = new NewsData();
					Elements tmp = element.getElementsByClass("news_tit");
					Elements newsDay = element.select("span.info");
					Elements press = element.select("a.info");
					
					String day = null;
					news.setTitle(tmp.text());
					news.setLink(tmp.attr("href"));
					news.setContent(element.text());
					news.setKeywordId(k.getId()); 
					
					for (Element date : newsDay) {
						day = date.text();
					}

					news.setNewsdate(toDate(day));
					news.setPress(press.text());
					Integer isExist = newsMapper.getNewsByTitle(news);
					if(isExist == 0) {						
						newsMapper.insertNews(news);
					}
					i++;
				}
				document.clearAttributes();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			if(interval - 1 == 0) {				
				Map<String, Object> map = new HashMap<>();
				map.put("url", sendUrl);
				map.put("errMsg", e.getMessage());
				map.put("errPrint", e.getStackTrace().toString());
				map.put("item", item);
				map.put("keyword", k);
				map.put("param", param);
				errList.add(map);
			}else {
				try {					
					Thread.sleep((int)(Math.random()*1000)+500);
					getNewsScraping(item, k, param, interval - 1);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	public ResultList newsList(SearchBase searchBase) {
		ResultList resultList = new ResultList(searchBase);
		resultList.setResultList(newsMapper.newsList(searchBase));
		resultList.setTotalRecordCount(newsMapper.newsListCount(searchBase));
		return resultList;
	}
	
	public Map<String, Object> certainNewsInsert(ScrapAttribute param) {
		Map<String, Object> resultVal = new HashMap<>();
		boolean result = true;
		String msg = null;
		try {
			Keyword keyword = keywordService.selectById(param.getId());
			String item = keywordService.convertKeyword(keyword);
			
			getNewsScraping(item, keyword, param, 3);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			result = false;
			msg = e.getMessage();
		}
		resultVal.put("result", result);
		resultVal.put("msg", msg);
		return resultVal;
	}
	
	public Map<String, Object> allNewsInsert(ScrapAttribute param) {
		Map<String, Object> resultVal = new HashMap<>();
		boolean result = true;
		String msg = null;
		try {
			System.err.println("start all News Scrap");
			List<Keyword> keywords = keywordService.selectAll();

			long startTime = System.currentTimeMillis();
			
			/*Stream<Keyword> stream = keywords.parallelStream();
			stream.forEach(k -> {
				try {
					String item = keywordService.convertKeyword(k);
					getNewsScraping(item, k, param);
					Thread.sleep(300);
//					System.out.println("##Thread Name : "+Thread.currentThread().getName());
				} catch (InterruptedException e) {
					System.err.println("Thread Exception");
//					e.printStackTrace();
				}
			});*/
			
			ForkJoinPool forkjoinPool = new ForkJoinPool(20);
			try {
				forkjoinPool.submit(() -> {
					keywords.parallelStream().forEach(k -> {
						String item = keywordService.convertKeyword(k);
						getNewsScraping(item, k, param, 3);
						try {
							Thread.sleep((int)(Math.random()*1000)+500);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
//						
					});
				}).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			
			long endTime = System.currentTimeMillis();
			System.err.println("스크랩시간(초) : "+((endTime-startTime)/1000));
			System.err.println("스크랩시간(분) : "+((endTime-startTime)/1000/60));
			System.err.println("스크랩시간(시간) : "+((endTime-startTime)/1000/60/60));
			System.out.println("errList size : "+errList.size());
			for (Map<String, Object> e : errList) {
				Keyword k = (Keyword)e.get("keyword");
				System.err.println("회사 : "+k.getEnterprise());
				System.err.println("url : "+e.get("url"));
				System.err.println("msg : "+e.get("errMsg"));
				System.err.println("trace : "+e.get("errPrint"));
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			result = false;
		}
		errList.clear();
		resultVal.put("result", result);
		resultVal.put("msg", msg);
		return resultVal;
	}
	
	public Date toDate(String date) {
		Date result = null;
		Calendar cal = Calendar.getInstance();

		if(date.contains("일")) {			
			cal.setTime(new Date());
			cal.add(Calendar.DATE, -1);
			result = cal.getTime();
		}else if(date.contains("시간") || date.contains("분")) {			
			result = new Date();
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.");
			try {
				result = sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
