package com.avery.recuritcloud.common.quartz;

import com.avery.recuritcloud.common.quartz.job.CompanyGrabTalentJob;
import com.avery.recuritcloud.common.quartz.job.CreateSaveCompanyJob;
import com.avery.recuritcloud.common.quartz.job.CreateSaveTalentJob;
import com.avery.recuritcloud.repository.CompanyRepository;
import com.avery.recuritcloud.service.GrabService;
import org.joda.time.DateTime;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableAutoConfiguration
public class InitScheduleJob implements InitializingBean {
    
    public static Logger logger= LoggerFactory.getLogger(InitScheduleJob.class);
    
    @Autowired
    GrabService grabService;
    
    @Autowired
    private Scheduler scheduler;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // grabService.init();
        initJob();
    }
    
    public void initJob()
    {
        createSaveTalentJob();
    
        ceateSaveCompanyJob();
        
        createGrabTalentJob();
    }
    
    @Transactional
    public void ceateSaveCompanyJob()
    {
        logger.info("create company job start...");
        JobDetail jobDetail = JobBuilder.newJob(CreateSaveCompanyJob.class).withIdentity(new JobKey("CreateCompanyJob")).build();
        SimpleTriggerImpl simpleTrigger=new SimpleTriggerImpl();
        simpleTrigger.setStartTime(DateTime.now().toDate());
        simpleTrigger.setRepeatCount(20);
        simpleTrigger.setRepeatInterval(5000);
        simpleTrigger.setName("company");
    
        try {
            scheduler.scheduleJob(jobDetail,simpleTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        logger.info("create company job end...");
    }
    
    @Transactional
    public void createSaveTalentJob() {
        logger.info("create talent job start...");
        JobDetail jobDetail = JobBuilder.newJob(CreateSaveTalentJob.class).withIdentity(new JobKey("CreateTalentJob")).build();
        SimpleTriggerImpl simpleTrigger=new SimpleTriggerImpl();
        simpleTrigger.setStartTime(DateTime.now().toDate());
        simpleTrigger.setRepeatCount(20);
        simpleTrigger.setRepeatInterval(5000);
        simpleTrigger.setName("talent");
        
        try {
            scheduler.scheduleJob(jobDetail,simpleTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        logger.info("create talent job end...");
    }
    
    
    
    //对于这些公司，给予特权，每1分钟查看有无人才可抢
    @Transactional
    public void createGrabTalentJob()
    {
        logger.info("create grab talent job start...");
        JobDetail jobDetail = JobBuilder.newJob(CompanyGrabTalentJob.class).withIdentity(new JobKey("CompanyGrabTalent")).build();
        SimpleTriggerImpl simpleTrigger=new SimpleTriggerImpl();
        simpleTrigger.setStartTime(DateTime.now().toDate());
        simpleTrigger.setRepeatCount(20);
        simpleTrigger.setRepeatInterval(1000*60*1);//1分钟看看有没有人才可抢
        simpleTrigger.setName("grab");
    
        try {
            scheduler.scheduleJob(jobDetail,simpleTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        logger.info("create grab talent job end...");
    }
    
    
  
}
