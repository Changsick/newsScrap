package com.gridone.scraping.service;

import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TestScheduleService {
 
     public String getFixedCron() {
          return "0 0/1 * 1/1 * ?";
     }
    @Scheduled(cron = "#{@testScheduleService.getFixedCron()}")
     public void run() {
//          System.out.println("#####run###");
     }
    
    @Scheduled(cron = "0/1 * * 1/1 * ?")
    public void start() {
    	
    }
}
