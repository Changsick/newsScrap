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

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gridone.scraping.mapper.NewsMapper;
import com.gridone.scraping.mapper.TextMiningMapper;
import com.gridone.scraping.model.Keyword;
import com.gridone.scraping.model.LoginUserDetails;
import com.gridone.scraping.model.NewsData;
import com.gridone.scraping.model.NewsMonitoring;
import com.gridone.scraping.model.ResultList;
import com.gridone.scraping.model.ScrapAttribute;
import com.gridone.scraping.model.SearchBase;
import com.gridone.scraping.model.SendMinigNews;
import com.gridone.scraping.model.TextMiningModel;
import com.gridone.scraping.wordcloud.WordCount;

@Service
public class NewsService {
	
	private static String KOREA_COVID_DATAS_URL = "https://search.naver.com/search.naver?query=keyword&where=news&pd=2&sort=1";//&field=1
	private static String KOREA_COVID_DATAS_URL2 = "https://search.naver.com/search.naver?query=keyword&where=news&pd=3&ds=startDate&de=endDate&sort=1";//&field=1
	//pd=3&ds=2020.01.01&de=2021.01.13
	

	private static Map<String, Object> scrapToken = new HashMap<>();
	
	private List<Map<String, Object>> errList = new ArrayList<>();
	
	@Autowired
	NewsMapper newsMapper;
	
	@Autowired
	KeywordService keywordService;
	
	@Autowired
	NewsMonitoringService monitoringService;
	
	@Autowired
	MailClient mailClient;
	
	@Autowired
	TextMiningMapper textMiningMapper;
	
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
		LoginUserDetails user = (LoginUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(scrapToken.get(user.getId().toString()) != null) {
			result = false;
			msg = "스크랩이 진행중 입니다.";
			resultVal.put("result", result);
			resultVal.put("msg", msg);
			return resultVal;
		}
		scrapToken.put(user.getId().toString(), true);
		try {
			System.err.println("start all News Scrap");
			List<Keyword> keywords = keywordService.selectByLogin(user.getId());

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
		scrapToken.put(user.getId().toString(), null);
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

	public boolean checkScrapStatus() {
		LoginUserDetails user = (LoginUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return scrapToken.get(user.getId().toString()) == null ? false : true;
	}

	public Map<String, Object> deleteNewsByKeyword(Keyword param) {
		Map<String, Object> resultVal = new HashMap<String, Object>();
		boolean result = false;
		String msg = null;
		try {
			newsMapper.deleteNewsByKeywordId(param);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		resultVal.put("result", result);
		resultVal.put("msg", msg);
		return resultVal;
	}

	public Map<String, Object> sendHistoryMail(ScrapAttribute param) {
		LoginUserDetails user = (LoginUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		param.setUserId(user.getId());
		List<NewsData> monitoring = newsMapper.getNewsForMail(param);
		System.out.println("monitoring : "+monitoring);
		System.out.println("monitoring size : "+monitoring.size());
		List<SendMinigNews> sendEmailData = new ArrayList<>();
		try {
			if(monitoring != null && monitoring.size() > 0) {
				List<NewsData> sendData = null;
				
				List<String> title = new ArrayList<>();
				List<String> content = new ArrayList<>();
				
				int idx = 0;
				for(int i=0; i<monitoring.size(); i++) {
					
					title.add(monitoring.get(i).getTitle()+" ");
					content.add(monitoring.get(i).getContent()+" ");
					if(i > 0 && !( monitoring.get(i).getKeywordId().equals(monitoring.get(i-1).getKeywordId())) ) {
						SendMinigNews tempData = new SendMinigNews();
						sendData = new ArrayList<>(monitoring.subList(idx, i));
						HashMap<String, Object> textmining = monitoringService.setWordCount(title.subList(idx, i).toString(), content.subList(idx, i).toString());
						tempData.setEnterprise(monitoring.get(i-1).getEnterprise());
						tempData.setMiningText(textmining);
						tempData.setNewsList(sendData);
						sendEmailData.add(tempData);
						idx = i;
					}
					
					if(i == monitoring.size()-1) {
						SendMinigNews tempData = new SendMinigNews();
						sendData = new ArrayList<>(monitoring.subList(idx, i+1));
						HashMap<String, Object> textmining = monitoringService.setWordCount(title.subList(idx, i+1).toString(), content.subList(idx, i+1).toString());
						tempData.setEnterprise(monitoring.get(i).getEnterprise());
						tempData.setMiningText(textmining);
						tempData.setNewsList(sendData);
						sendEmailData.add(tempData);
					}
				}
				
			}
			
			sendEmail(sendEmailData, param);
			
//			movetoNewsTable(monitoring);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void textmining(ScrapAttribute param) {
		
		List<NewsData> monitoring = newsMapper.getNewsForMining(param);
		System.out.println("monitoring size : "+monitoring.size());
		List<SendMinigNews> sendEmailData = new ArrayList<>();
		try {
			if(monitoring != null && monitoring.size() > 0) {
				List<NewsData> sendData = null;
				
				List<String> title = new ArrayList<>();
				List<String> content = new ArrayList<>();
				
				int idx = 0;
				for(int i=0; i<monitoring.size(); i++) {
					
					title.add(monitoring.get(i).getTitle()+" ");
					content.add(monitoring.get(i).getContent()+" ");
					if(i > 0 && !( monitoring.get(i).getKeywordId().equals(monitoring.get(i-1).getKeywordId())) ) {
						SendMinigNews tempData = new SendMinigNews();
						sendData = new ArrayList<>(monitoring.subList(idx, i));
						HashMap<String, Object> textmining = monitoringService.setWordCount(title.subList(idx, i).toString(), content.subList(idx, i).toString());
						tempData.setEnterprise(monitoring.get(i-1).getEnterprise());
						tempData.setMiningText(textmining);
						tempData.setNewsList(sendData);
						sendEmailData.add(tempData);
						idx = i;
					}
					
					if(i == monitoring.size()-1) {
						SendMinigNews tempData = new SendMinigNews();
						sendData = new ArrayList<>(monitoring.subList(idx, i+1));
						HashMap<String, Object> textmining = monitoringService.setWordCount(title.subList(idx, i+1).toString(), content.subList(idx, i+1).toString());
						tempData.setEnterprise(monitoring.get(i).getEnterprise());
						tempData.setMiningText(textmining);
						tempData.setNewsList(sendData);
						sendEmailData.add(tempData);
					}
				}
				
			}
			
			insertMiningData(sendEmailData, param);
			
//			movetoNewsTable(monitoring);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		return null;
	}
	
	public void insertMiningData(List<SendMinigNews> sendEmailData, ScrapAttribute param) {
		System.err.println("####insertMiningData#####");
		if(sendEmailData == null || sendEmailData.size() == 0) {
			return;
		}
		try {
			
			for (SendMinigNews s : sendEmailData) {
				
				List<NewsData> list = (List<NewsData>)s.getNewsList();
				
				String wordcloud = s.getMiningText().get("contentImage").toString();
				Integer keywordId = list.get(0).getKeywordId();
				
				List<WordCount> top5List = (List<WordCount>)s.getMiningText().get("content");
				String top5 = top5List.toString();
				
				Date newsDate = list.get(0).getNewsdate();
				Integer userId = param.getUserId();
				
				TextMiningModel data = new TextMiningModel();
				data.setKeywordId(keywordId);
				data.setTop5(top5);
				data.setNewsDate(newsDate);
				data.setWordCloud(wordcloud);
				data.setUserId(userId);
				
				textMiningMapper.insert(data);
				
//				System.out.println(wordcloud.substring(0,50));
//				System.out.println("keywordId : "+keywordId);
//				System.out.println("top5 : "+top5);
//				System.out.println("newsDate : "+newsDate);
//				System.out.println("userId : "+userId);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendEmail(List<SendMinigNews> sendEmailData, ScrapAttribute user) {	
		String container = "width: fit-content;";
		String content = "position: relative;";
		String content_row = "display: inline-block;max-width: 800px;max-height: 600px;overflow-y: auto;";
		String elements = "float: left;";
		
		
		StringBuffer temp = new StringBuffer();
		temp.append("<div class='container' style='"+container+"'>");
		if(sendEmailData == null || sendEmailData.size() == 0) {
			temp.append("<div class='header'> 모니터링의 소식이 없습니다. </div>");
		}else {			
			for (SendMinigNews item : sendEmailData) {
//				for (Entry<String, ArrayList<WordCount>> entry  : item.getMiningText().entrySet()) {
//					System.out.println("key : "+entry.getKey() + ", value : " + entry.getValue().toString());
//				}
				List<NewsData> itemList = (List<NewsData>)item.getNewsList();
				temp.append("<div class='header'> Keyword : "+itemList.get(0).getKeywords()+"</div>");
				temp.append("<div class='content' style='"+content+"'>");
				
				temp.append("<div class='content_row' style='"+content_row+"clear: both;display: block;'><table>");
				
				temp.append("<thead><tr>");				
				temp.append("<th style='background-color: #FFC107;' colspan='3'>"+item.getEnterprise()+" 뉴스</th></tr>");
				temp.append("<tr><th scope='col' style='width: 200px;'>제목</th><th scope='col'>요약</th><th scope='col' style='width: 100px;'>언론사</th>");
				temp.append("</tr></thead>");
				
				temp.append("<tbody>");
				for (NewsData item2 : itemList) {
					temp.append("<tr >");
					temp.append("<td ><a href='"+item2.getLink()+"'>"+item2.getTitle()+"</a></td>");
					temp.append("<td>"+item2.getContent()+"</td>");
					temp.append("<td>"+item2.getPress()+"</td>");
					temp.append("</tr>");
				}
				temp.append("</tbody>");
				temp.append("</table></div>"); // end content_row(table)
				
				temp.append("<div class='content_row elements' style='"+content_row+elements+"width:200px;'>");
//				temp.append("");
				temp.append("<ul><b>Top5</b><br>");
				List<WordCount> list = (List<WordCount>)item.getMiningText().get("content");
				for (WordCount w : list) {					
					temp.append("<br><li>"+w.toString()+"</li>");
				}
				temp.append("</ul>");
				temp.append("</div>"); // end content_row(elements)
				temp.append("<div class='content_row elements' style='"+content_row+elements+"width:200px;'>");
//				temp.append("<h3>Low5</h3>");
				temp.append("<ul>Low5<br>");
				List<WordCount> list2 = (List<WordCount>)item.getMiningText().get("contentLower");
				for (WordCount w : list2) {					
					temp.append("<br><li>"+w.toString()+"</li>");
				}
				temp.append("</ul>");
				temp.append("</div>"); // end content_row(elements)

				temp.append("<div class='content_row elements' style='"+content_row+elements+"'>");
				temp.append("<ul>Word Cloud<br>");
				temp.append("<img width='300px;' src='data:image/png;base64,"+item.getMiningText().get("contentImage")+"'>");
				temp.append("</ul>");
				temp.append("</div>"); // end content_row(elements)
				
				temp.append("<div class='footer' style='clear: both;'></div>");
				
				temp.append("</div>"); // end content
			}
		}
		temp.append("</div>");
		String email = user.getEmailTo();
		
		InternetAddress[] toAddr = new InternetAddress[1];
		for(int i=0; i<toAddr.length; i++) {
			try {
				toAddr[i] = new InternetAddress(email.trim());
			} catch (AddressException e) {
				e.printStackTrace();
			}
		}
//		System.out.println("temp : "+temp.toString());
		mailClient.prepareAndSend(toAddr, temp.toString());
	}

	public ResultList getNewsByNewsDate(SearchBase searchBase) {
		System.err.println("searchBase.getEndDate() : "+searchBase.getEndDate());
		TextMiningModel tmm = textMiningMapper.getMaxBeforeDate(searchBase);
		if(tmm != null) {
			SimpleDateFormat foramt = new SimpleDateFormat("yyyy-MM-dd");
			searchBase.setStartDate(foramt.format(tmm.getNewsDate()));
		}
		ResultList resultList = new ResultList(searchBase);
		resultList.setResultList(newsMapper.newsListByNewsDate(searchBase));
		resultList.setTotalRecordCount(newsMapper.newsListByNewsDateCount(searchBase));
		return resultList;
	}

}
