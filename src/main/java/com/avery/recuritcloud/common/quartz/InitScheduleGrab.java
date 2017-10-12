package com.avery.recuritcloud.common.quartz;

import com.avery.recuritcloud.service.GrabService;
import java.text.ParseException;
import org.joda.time.DateTime;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class InitScheduleGrab implements InitializingBean {
    
    @Autowired
    GrabService grabService;
    
    @Autowired
    private Scheduler scheduler;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // grabService.init();
        initJob();
        
    }
    
    public void initJob()
    {
        createSaveTalentJob();
    }
    
    @Transactional
    public void ceateSaveCompanyJob()
    {
        JobDetail jobDetail= JobBuilder.newJob(CreateTalentJob.class).build();
        CronTrigger cronTrigger=new CronTriggerImpl();
        
        CronTriggerImpl cronTrigger1=new CronTriggerImpl();
        try {
            cronTrigger1.setCronExpression("0/5 * * * ? * *");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    @Transactional
    public void createSaveTalentJob() {
        JobDetail jobDetail = JobBuilder.newJob(CreateTalentJob.class).withIdentity(new JobKey("who")).build();
        SimpleTriggerImpl simpleTrigger=new SimpleTriggerImpl();
        simpleTrigger.setStartTime(DateTime.now().toDate());
        simpleTrigger.setRepeatCount(20);
        simpleTrigger.setRepeatInterval(5000);
        simpleTrigger.setName("hhhhh");
        
        try {
            scheduler.scheduleJob(jobDetail,simpleTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
  
}
