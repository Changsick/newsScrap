package com.gridone.scraping.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

	protected Logger logger = LoggerFactory.getLogger(ScheduleService.class);

	@Autowired
	private SchedulerFactoryBean schedulerFactory;
	
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

}
