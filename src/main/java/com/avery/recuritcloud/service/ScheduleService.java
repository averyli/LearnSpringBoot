package com.avery.recuritcloud.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class ScheduleService {
    
    
    @Scheduled(cron = "0 0/5 0 * * ?")
    public void checkAllCompanyStatus()
    {
    
    }
}
