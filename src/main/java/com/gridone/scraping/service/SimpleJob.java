package com.gridone.scraping.service;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gridone.scraping.utils.Utils;

public class SimpleJob implements Job {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("set execute");
		
		int scheduleId = Utils.toInt(context.getJobDetail().getKey().getName());
		//20180810T100000Z
    	Date roundupTime =  new Date( Math.round((double) context.getScheduledFireTime().getTime() / (1*60*1000) ) * (1*60*1000) );
    	
    	String exception  = context.getMergedJobDataMap().getString("Exception");
	}
}
