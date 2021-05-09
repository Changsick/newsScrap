package com.gridone.scraping.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.gridone.scraping.model.LoginUserDetails;
import com.gridone.scraping.model.ResultList;
import com.gridone.scraping.model.ScheduleModel;
import com.gridone.scraping.model.SearchBase;
import com.gridone.scraping.model.TextMiningModel;
import com.gridone.scraping.service.ScheduleService;
import com.gridone.scraping.service.TextMiningService;
import com.gridone.scraping.type.EnumScheduleType;

@Controller
public class TextMiningController {

	@Autowired
	TextMiningService textMiningService;
	
	@Autowired
	ScheduleService scheduleService;
	
	@RequestMapping(value = "/getScheduleTime", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getMiningTime(ScheduleModel param){
		Map<String, Object> retVal = new HashMap<String, Object>();
		boolean result = false;
		try {
//			ScheduleModel sm = new ScheduleModel();
			LoginUserDetails user = (LoginUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			param.setUserId(user.getId());
//			sm.setType(EnumScheduleType.TEXTMINING);
			
			ScheduleModel getSchedule = scheduleService.getUserTypeSchedule(param);
			retVal.put("data", getSchedule);
			
			final Cron cron = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ)).parse(getSchedule.getCron());
			
//			System.err.println("cron.asString() : "+cron.asString());
//			System.err.println("cron.getCronDefinition() : "+cron.getCronDefinition().isMatchDayOfWeekAndDayOfMonth());
//			System.err.println("cron.validate() : "+cron.validate());
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		retVal.put("result", result);
		return retVal;
	}
	
	@RequestMapping(value = "/editScheduleTime", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> editScheduleTime(ScheduleModel param){
		Map<String, Object> retVal = new HashMap<String, Object>();
		boolean result = false;
		try {
			LoginUserDetails user = (LoginUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			param.setUserId(user.getId());
			ScheduleModel userSchedule = scheduleService.getUserTypeSchedule(param);
			userSchedule.setCron(param.getCron());
			userSchedule.setNextTime(scheduleService.checkNextTime(param.getCron()));
			System.err.println("update userSchedule --> "+userSchedule);
			scheduleService.updateSchedule(userSchedule);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		retVal.put("result", result);
		return retVal;
	}
	
}
