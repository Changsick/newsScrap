package com.gridone.scraping.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;

import org.quartz.CronExpression;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.gridone.scraping.mapper.ScheduleMapper;
import com.gridone.scraping.model.ScheduleModel;

@Service
public class ScheduleService {

	protected Logger logger = LoggerFactory.getLogger(ScheduleService.class);

	@Autowired
	private SchedulerFactoryBean schedulerFactory;
	
	@Autowired
	ScheduleMapper scheduleMapper;
	
	//@Scheduled(cron = "${automateone.cron.deleteExpiredTrigger}")
	public void checkFireTime() {
		logger.debug("checkFireTime invokrd!");
		Date today = Calendar.getInstance().getTime();
		try {
			List<String> jobGroupNames = schedulerFactory.getScheduler().getJobGroupNames();
		    for (String groupName : jobGroupNames) {
		        Set<JobKey> jobKeys = schedulerFactory.getScheduler().getJobKeys(GroupMatcher.<JobKey>groupEquals(groupName));
		        for (JobKey jobKey : jobKeys) {
		            for (Trigger triggerKey : schedulerFactory.getScheduler().getTriggersOfJob(jobKey)) {
		            	if (triggerKey.getEndTime() != null && triggerKey.getEndTime().compareTo(today) == -1) {
	            			logger.debug("Fire Time Trigger Delete ->"+today);
	            			schedulerFactory.getScheduler().unscheduleJob(triggerKey.getKey());
	            			schedulerFactory.getScheduler().deleteJob(jobKey);								
						}
					}
		        }
		    }
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.debug("checkFireTime error ->"+e.getMessage());
		}
	}// .checkFireTime
	
	public Date checkNextTime(String cronExpress) throws ParseException {
		final Cron cron = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ)).parse(cronExpress);

		Date quartzNextTime = null;
		
		ZonedDateTime now = ZonedDateTime.now();
		Date currDate = new Date();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
	    
	    final ZoneId utc = ZoneId.of("Asia/Seoul");
	    final Optional<ZonedDateTime> nextExecution = ExecutionTime.forCron(cron).nextExecution(now);
	    
	    if (nextExecution.isPresent()) {
	        final ZonedDateTime cronUtilsNextTime = nextExecution.get();// 2016-12-30T00:00:00Z

	        final CronExpression cronExpression = new CronExpression(cron.asString());
	        cronExpression.setTimeZone(TimeZone.getTimeZone(utc));
	        quartzNextTime = cronExpression.getNextValidTimeAfter(Date.from(now.toInstant()));// 2016-12-24T00:00:00Z
	    }
	    
		return quartzNextTime;
	}
	
	public void textmining() {
		
	}

	public void insertMailSchedule(ScheduleModel mailSchedule) {
		scheduleMapper.insertMailSchedule(mailSchedule);
	}

	public void insertTextminingSchedule(ScheduleModel textminingSchedule) {
		scheduleMapper.insertTextminingSchedule(textminingSchedule);
	}

	public List<ScheduleModel> getCurrNextTime(String date) {
//		date = "2021-04-16 09:00:00";
		return scheduleMapper.getCurrNextTime(date);
	}

	public void updateSchedule(ScheduleModel schedule) {
		scheduleMapper.updateSchedule(schedule);
	}
 
}
