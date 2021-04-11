package com.gridone.scraping.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gridone.scraping.model.ScheduleModel;
import com.gridone.scraping.model.ScrapAttribute;
import com.gridone.scraping.model.TextMiningModel;
import com.gridone.scraping.service.NewsMonitoringService;
import com.gridone.scraping.service.NewsService;
import com.gridone.scraping.service.ScheduleService;
import com.gridone.scraping.service.TextMiningService;
import com.gridone.scraping.type.EnumScheduleType;

@Controller
public class ScheduleController {
	
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	NewsMonitoringService newsMonitoringService;
	
	@Autowired
	NewsService newsService;
	
	@Autowired
	TextMiningService textMiningService;
	
	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void scheduleSet() throws ParseException {
//		System.err.println("#####scheduleSet######");
		
		// 1. schedule list 중 nextTime이 현재 시간과 같은 애를 뽑는다.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
		List<ScheduleModel> currSchedule = scheduleService.getCurrNextTime(sdf.format(new Date()));
		
		if(currSchedule == null || currSchedule.size() == 0) {
			return;
		}
		
		List<ScheduleModel> mails = currSchedule.stream().filter(a -> a.getType() == EnumScheduleType.MONITORING).collect(Collectors.toList());
		
		// 2. schedule nextTime update
		for (ScheduleModel s : currSchedule) {
			s.setNextTime(scheduleService.checkNextTime(s.getCron()));
			scheduleService.updateSchedule(s);
		}
		
		// 3. 1에서 type이 메일인 친구를 뽑아서 해당 서비스 호출
		if(mails == null || mails.size() == 0) {
			return;
		}
		System.out.println("mails : "+mails);
		mails.forEach(a -> newsMonitoringService.monitoringSendEmail(a.getUserId()));
	}
	
	@Scheduled(cron = "0 0 0 1/1 * ?")
//	@RequestMapping(value = "/testtest")
	public void textmining() {
		System.err.println("#####textmining######");
		// 일, 주단위, 시간은 매 자정만임,,
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-16 00:00:00");
		Date currDate = new Date();
		List<ScheduleModel> currSchedule = scheduleService.getCurrNextTime(sdf.format(currDate));
		if(currSchedule == null || currSchedule.size() == 0) {
			return;
		}
		
		List<ScheduleModel> minings = currSchedule.stream().filter(a -> a.getType() == EnumScheduleType.TEXTMINING).collect(Collectors.toList());
		System.out.println("minings : "+minings);
		
		if(minings == null || minings.size() == 0) {
			return;
		}
		
		minings.parallelStream().forEach(m -> {
			TextMiningModel data = textMiningService.getMiningDataByUserId(m.getUserId());
			ScrapAttribute param = new ScrapAttribute();
			SimpleDateFormat paramFormat = new SimpleDateFormat("yyyy-MM-dd");
			param.setUserId(m.getUserId());
			param.setEndDate(paramFormat.format(currDate));
			if(data == null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.YEAR, -2); // 없을 경우 현재일 기준 2년 전 기준 startdate
				param.setStartDate(sdf.format(cal.getTime()));
			}else {
				param.setStartDate(sdf.format(data.getNewsDate()));
			}
			newsService.textmining(param);
		});
		System.err.println("#####textmining end######");
	}
	
	
}
