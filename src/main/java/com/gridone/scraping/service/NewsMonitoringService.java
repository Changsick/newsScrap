package com.gridone.scraping.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gridone.scraping.mapper.NewsMapper;
import com.gridone.scraping.mapper.NewsMonitoringMapper;
import com.gridone.scraping.model.Keyword;
import com.gridone.scraping.model.NewsData;
import com.gridone.scraping.model.NewsMonitoring;
import com.gridone.scraping.model.SendMinigNews;
import com.gridone.scraping.morpheme.MorphemeAnalysis;
import com.gridone.scraping.wordcloud.StringProcessor;
import com.gridone.scraping.wordcloud.WordCloud;
import com.gridone.scraping.wordcloud.WordCount;
import com.wcohen.ss.JaroWinkler;
import com.wcohen.ss.Level2JaroWinkler;
import com.wcohen.ss.Level2Levenstein;
import com.wcohen.ss.Levenstein;
import com.wcohen.ss.api.StringDistance;

@Service
public class NewsMonitoringService {
	
	@Autowired
	KeywordService keywordService;
	
	@Autowired
	NewsService newsService;
	
	@Autowired
	NewsMonitoringMapper newsMonitoringMapper;
	
	@Autowired
	MailClient mailClient;
	
	@Value("${email.to}")
	String emailTo;
	
	@Autowired
	NewsMapper newsMapper;
	
	private List<Map<String, Object>> errMonitoringList = new ArrayList<>();
	
	private static int INTERVAL = 3;

	public void executeNewsMonitoring() {
		List<Keyword> keywords = keywordService.selectAll();
		System.out.println("######executeNewsMonitoring");
		
		ForkJoinPool forkjoinPool = new ForkJoinPool(20);
		try {
			forkjoinPool.submit(() -> {
				keywords.parallelStream().forEach(k -> {
					String item = keywordService.convertKeyword(k);
					scrapNewsMonitoring(item, k, INTERVAL);
					try {
						Thread.sleep((int)(Math.random()*5000)+500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				});
			}).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
//		setWordCount();
		System.out.println("err Cnt : "+errMonitoringList.size());
		System.out.println("errList : "+errMonitoringList);
	}
	
	public void scrapNewsMonitoring(String item, Keyword k, int interval) {
		String url = "https://search.naver.com/search.naver?query=keyword&where=news&pd=4&sort=1";
//		String url = "https://search.naver.com/search.naver?query=keyword&where=news&pd=3&ds=2020.08.25&de=2021.01.26&sort=1";
		
		if(interval == 0) {
			return;
		}
		
		try {
			Document document = Jsoup.connect(url.replace("keyword", item)).timeout(5000).get();
//			Document document = Jsoup.connect(url.replace("keyword", item)).timeout((int)(Math.random()*5000)+500).get();
			Elements elements = document.select(".list_news > li");
			Elements nextPageElement = document.select(".btn_next");			
			
			if(elements.size() == 0) { // 검색결과가 없을 때
				return;
			}
			
			boolean isNext = true;
			int i = 0;
			while (isNext) {
				if(i > 0) {					
					String nextUrl = url.split("\\?")[0] + nextPageElement.attr("href");
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
					Elements tmp = element.getElementsByClass("news_tit");
					Elements newsDay = element.select("span.info");
					Elements press = element.select("a.info");
					
					String day = null;
					for (Element date : newsDay) {
						day = date.text();
					}
					
					String title = tmp.text();
					String link = tmp.attr("href");
					String content = element.text();
					Integer keyId = k.getId();
					Date newsDate = newsService.toDate(day);
					String newsPress = press.text();
					NewsMonitoring news = new NewsMonitoring(null, keyId, title, link, content, null, newsDate, newsPress);
					
					List<NewsMonitoring> monitoringData = newsMonitoringMapper.selectByKeywordId(keyId);
					if(monitoringData == null || monitoringData.size() == 0 || !isSimilarity(monitoringData, news)) {						
						newsMonitoringMapper.insertNewsMonitoring(news);
					}
					
					i++;
				}
				document.clearAttributes();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(interval - 1 == 0) {				
				Map<String, Object> errVal = new HashMap<>();
				errVal.put("url", url.replace("keyword", item));
				errVal.put("errMsg", e.getMessage());
				errVal.put("errPrint", e.getStackTrace().toString());
				errVal.put("item", item);
				errVal.put("keyword", k);
				errMonitoringList.add(errVal);
			}else {				
				try {
					Thread.sleep((int)(Math.random()*5000)+500);
					scrapNewsMonitoring(item, k, interval - 1);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	public boolean isSimilarity(List<NewsMonitoring> data, NewsMonitoring news) {
		boolean result = false;
		JaroWinkler jaro = new JaroWinkler();
//		Level2JaroWinkler jaro = new Level2JaroWinkler();
		StringDistance distanceChecker = jaro.getDistance();
		for (NewsMonitoring n : data) {
			double similarity = distanceChecker.score(news.getTitle(), n.getTitle());
			if(similarity >= 0.75) {
				System.out.println("similar percent : "+similarity);
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	public void isSimilaritySample() {
		Level2Levenstein leven = new Level2Levenstein();
		StringDistance distanceChecker = leven.getDistance();
		double similarity1 =  distanceChecker.score("0에서 얼만큼 떨어지는 정도를 파악하기 힘들군", "0에서 떨어지는 정도가 양수값은 없는건가 파악하기 힘들군");
		System.out.println("Level2Levenstein similarity : "+similarity1);

		Levenstein leven2 = new Levenstein();
		StringDistance dis = leven2.getDistance();
		double sim = dis.score("0에서 얼만큼 떨어지는 정도를 파악하기 힘들군", "0에서 떨어지는 정도가 양수값은 없는건가 파악하기 힘들군");
		System.out.println("Levenstein similarity : "+sim);

		JaroWinkler jaro = new JaroWinkler();
		StringDistance distanceChecker2 = jaro.getDistance();
		double jaroSimilarity =  distanceChecker2.score("그리드원, 자동화 협업 포털 '원팀' 출시", "\"사람과 기계 협업\"…그리드원, 자동화 협업 포털 '원팀' 출시");
//		double jaroSimilarity =  distanceChecker2.score("\"사람과 기계 협업\"…그리드원, 자동화 협업 포털 '원팀' 출시", "그리드원, 자동화 협업 포털 '원팀' 출시");
		System.out.println("JaroWinkler similarity : "+jaroSimilarity);
		double jaroSimilarity2 =  distanceChecker2.score("그리드원, 모바일 RPA 기능 제공...모바일에서도 RPA 구현 가능", "\"모바일 업무도 자동화\"…그리드원, 모바일 RPA 기능 제공");
		System.out.println("JaroWinkler similarity2 : "+jaroSimilarity2);
		double jaroSimilarity3 =  distanceChecker2.score("재정 투명성·국민 편의성 높여줄 차세대 지방재정시스템 2024년 개통", "지방재정 투명성·국민편의 높일 차세대 지방재정시스템, 2024년 개통 예정");
		System.out.println("JaroWinkler similarity3 : "+jaroSimilarity3);
//		double jaroSimilarity4 =  distanceChecker2.score("행안부, 차세대 지방재정시스템 2024년 개통 예정", "행안부, 차세대 지방재정시스템 2024년 개통…지방재정 투명성·국민편의 ↑");
		double jaroSimilarity4 =  distanceChecker2.score("행안부, 차세대 지방재정시스템 2024년 개통…지방재정 투명성·국민편의 ↑", "행안부, 차세대 지방재정시스템 2024년 개통 예정");
		System.out.println("JaroWinkler similarity4 : "+jaroSimilarity4);
		double jaroSimilarity5 =  distanceChecker2.score("1017억 투입해 '차세대 지방재정시스템' 구축…2024년 개통 목표", "재정 투명성·국민 편의성 높여줄 차세대 지방재정시스템 2024년 개통");
		System.out.println("JaroWinkler similarity5 : "+jaroSimilarity5);
		/**
		 * 재정 투명성·국민 편의성 높여줄 차세대 지방재정시스템 2024년 개통
		 * 지방재정 투명성·국민편의 높일 차세대 지방재정시스템, 2024년 개통 예정
		 * 
		 * 행안부, 차세대 지방재정시스템 2024년 개통 예정
		 * 행안부, 차세대 지방재정시스템 2024년 개통…지방재정 투명성·국민편의 ↑
		 * 
		 * 1017억 투입해 '차세대 지방재정시스템' 구축…2024년 개통 목표
		 * 재정 투명성·국민 편의성 높여줄 차세대 지방재정시스템 2024년 개통
		 */
		Level2JaroWinkler jaro2 = new Level2JaroWinkler();
		StringDistance distance = jaro2.getDistance();
//		double lev2JaroSimilarity = distance.score("그리드원, 자동화 협업 포털 '원팀' 출시", "\"사람과 기계 협업\"…그리드원, 자동화 협업 포털 '원팀' 출시");
		double lev2JaroSimilarity = distance.score("\"사람과 기계 협업\"…그리드원, 자동화 협업 포털 '원팀' 출시", "그리드원, 자동화 협업 포털 '원팀' 출시");
		System.out.println("Level2JaroWinkler similarity1 : "+lev2JaroSimilarity);
		double lev2JaroSimilarity2 = distance.score("그리드원, 모바일 RPA 기능 제공...모바일에서도 RPA 구현 가능", "\"모바일 업무도 자동화\"…그리드원, 모바일 RPA 기능 제공");
		System.out.println("Level2JaroWinkler similarity2 : "+lev2JaroSimilarity2);
		double lev2JaroSimilarity3 = distance.score("재정 투명성·국민 편의성 높여줄 차세대 지방재정시스템 2024년 개통", "지방재정 투명성·국민편의 높일 차세대 지방재정시스템, 2024년 개통 예정");
		System.out.println("Level2JaroWinkler similarity3 : "+lev2JaroSimilarity3);
		double lev2JaroSimilarity4 = distance.score("행안부, 차세대 지방재정시스템 2024년 개통 예정", "행안부, 차세대 지방재정시스템 2024년 개통…지방재정 투명성·국민편의 ↑");
//		double lev2JaroSimilarity4 = distance.score("행안부, 차세대 지방재정시스템 2024년 개통…지방재정 투명성·국민편의 ↑", "행안부, 차세대 지방재정시스템 2024년 개통 예정");
		System.out.println("Level2JaroWinkler similarity4 : "+lev2JaroSimilarity4);
//		double lev2JaroSimilarity5 = distance.score("1017억 투입해 '차세대 지방재정시스템' 구축…2024년 개통 목표", "재정 투명성·국민 편의성 높여줄 차세대 지방재정시스템 2024년 개통");
		double lev2JaroSimilarity5 = distance.score("재정 투명성·국민 편의성 높여줄 차세대 지방재정시스템 2024년 개통", "1017억 투입해 '차세대 지방재정시스템' 구축…2024년 개통 목표");
		System.out.println("Level2JaroWinkler similarity5 : "+lev2JaroSimilarity5);
		
	}
	
	public HashMap<String, Object> setWordCount(String titles, String contents) {
		HashMap<String, Object> resultData = new HashMap<>();
		
		if(titles != null && contents != null) {
//			StringProcessor title = new StringProcessor(titles);
//			StringProcessor content = new StringProcessor(contents,null);
			MorphemeAnalysis content = new MorphemeAnalysis(contents);
//			resultData.put("titleImage", WordCloud.getImageWordCloud(title));
			resultData.put("contentImage", WordCloud.getImageWordCloud(content));
			
//			ArrayList<WordCount> titleWord = title.getTopFileWords();
			ArrayList<WordCount> contentWord = content.getTopFileWords();
			ArrayList<WordCount> contentLowerWord = content.getLowFileWords();
//			resultData.put("title", titleWord);
			resultData.put("content", contentWord);
			resultData.put("contentLower", contentLowerWord);

		}
		return resultData;
	}
	
	public void monitoringSendEmail() {
		
		List<NewsMonitoring> monitoring = newsMonitoringMapper.getMonitoringNews();
		System.out.println("monitoring : "+monitoring);
		System.out.println("monitoring size : "+monitoring.size());
		List<SendMinigNews> sendEmailData = new ArrayList<>();
		try {
			if(monitoring != null && monitoring.size() > 0) {
				List<NewsMonitoring> sendData = null;
				
				List<String> title = new ArrayList<>();
				List<String> content = new ArrayList<>();
				
				int idx = 0;
				for(int i=0; i<monitoring.size(); i++) {
					
					title.add(monitoring.get(i).getTitle()+" ");
					content.add(monitoring.get(i).getContent()+" ");
					if(i > 0 && !( monitoring.get(i).getKeywordId().equals(monitoring.get(i-1).getKeywordId())) ) {
						SendMinigNews tempData = new SendMinigNews();
						sendData = new ArrayList<>(monitoring.subList(idx, i));
						HashMap<String, Object> textmining = setWordCount(title.subList(idx, i).toString(), content.subList(idx, i).toString());
						tempData.setEnterprise(monitoring.get(i-1).getEnterprise());
						tempData.setMiningText(textmining);
						tempData.setNewsList(sendData);
						sendEmailData.add(tempData);
						idx = i;
					}
					
					if(i == monitoring.size()-1) {
						SendMinigNews tempData = new SendMinigNews();
						sendData = new ArrayList<>(monitoring.subList(idx, i+1));
						HashMap<String, Object> textmining = setWordCount(title.subList(idx, i+1).toString(), content.subList(idx, i+1).toString());
						tempData.setEnterprise(monitoring.get(i).getEnterprise());
						tempData.setMiningText(textmining);
						tempData.setNewsList(sendData);
						sendEmailData.add(tempData);
					}
				}
				
			}
			
			sendEmail(sendEmailData);
			
//			movetoNewsTable(monitoring);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void movetoNewsTable(List<NewsMonitoring> monitoring) {
		try {			
			if(monitoring != null && monitoring.size() > 0) {
				Integer insert = 0;
				for (NewsMonitoring news : monitoring) {
					NewsData newsData = new NewsData(news);
					Integer isExist = newsMapper.getNewsByTitle(newsData);
					if(isExist == 0) {						
						insert = newsMapper.insertNews(newsData);
					}
				}
				if(insert > 0) {
					newsMonitoringMapper.deleteAll();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public String dateFormat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public void sendEmail(List<SendMinigNews> sendEmailData) {	
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
				List<NewsMonitoring> itemList = (List<NewsMonitoring>)item.getNewsList();
				temp.append("<div class='header'> Keyword : "+itemList.get(0).getKeywords()+"</div>");
				temp.append("<div class='content' style='"+content+"'>");
				
				temp.append("<div class='content_row' style='"+content_row+"clear: both;display: block;'><table>");
				
				temp.append("<thead><tr>");				
				temp.append("<th style='background-color: #FFC107;' colspan='3'>"+item.getEnterprise()+" 뉴스</th></tr>");
				temp.append("<tr><th scope='col' style='width: 200px;'>제목</th><th scope='col'>요약</th><th scope='col' style='width: 100px;'>언론사</th>");
				temp.append("</tr></thead>");
				
				temp.append("<tbody>");
				for (NewsMonitoring item2 : itemList) {
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
		String[] emails = emailTo.split(",");
		InternetAddress[] toAddr = new InternetAddress[emails.length];
		for(int i=0; i<emails.length; i++) {
			try {
				toAddr[i] = new InternetAddress(emails[i].trim());
			} catch (AddressException e) {
				e.printStackTrace();
			}
		}
//		System.out.println("temp : "+temp.toString());
		mailClient.prepareAndSend(toAddr, temp.toString());
	}
	
}
