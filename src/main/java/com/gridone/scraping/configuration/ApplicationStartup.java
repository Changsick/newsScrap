package com.gridone.scraping.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.gridone.scraping.model.ScheduleModel;
import com.gridone.scraping.model.UserModel;
import com.gridone.scraping.service.ScheduleService;
import com.gridone.scraping.service.UserService;
import com.gridone.scraping.type.EnumScheduleType;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	UserService userService;
	
	private static String DEFAULTCRON = "0 0 9 ? * FRI *";
	private static String MININGCRON = "0 0 0 ? * FRI *";

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		try {
			//0 0 9 ? * FRI *
			List<UserModel> userList = userService.getUsers();
			List<UserModel> adminList = userService.getAllAdmins();
			
			for (UserModel u : adminList) {
				ScheduleModel adminSchedule = new ScheduleModel(); // only admin
				adminSchedule.setCron(DEFAULTCRON);
				adminSchedule.setUserId(u.getId());
				adminSchedule.setType(EnumScheduleType.MONITORING);
				adminSchedule.setNextTime(scheduleService.checkNextTime(DEFAULTCRON)); //scheduleService.checkNextTime(DEFAULTCRON)
				scheduleService.insertMailSchedule(adminSchedule);
				
				adminSchedule.setCron(MININGCRON);
				adminSchedule.setType(EnumScheduleType.TEXTMINING); 
				adminSchedule.setNextTime(scheduleService.checkNextTime(MININGCRON)); //scheduleService.checkNextTime(DEFAULTCRON)
				scheduleService.insertTextminingSchedule(adminSchedule);
			}
			
			for (UserModel u : userList) {				
				ScheduleModel textminingSchedule = new ScheduleModel();
				textminingSchedule.setCron(DEFAULTCRON);
				textminingSchedule.setUserId(u.getId());
				textminingSchedule.setType(EnumScheduleType.TEXTMINING);
				textminingSchedule.setNextTime(scheduleService.checkNextTime(DEFAULTCRON));
				scheduleService.insertTextminingSchedule (textminingSchedule);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
}
