package com.avery.recuritcloud.common.quartz;

import java.io.InputStream;
import java.util.Properties;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @CreateTime Created by Elv Zuo on 2017/3/13.
 * @Function <p></p>
 */
@Configuration
@EnableAutoConfiguration
public class QuartzConfig {
    
    private static Logger logger = LoggerFactory.getLogger(QuartzConfig.class);
    
    @Autowired
    private BaseJobFactory baseJobFactory;
    
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(ApplicationContext applicationContext) {
        SchedulerFactoryBean schedulerFactoryBean = null;
        try {
            logger.info("Init scheduler factory bean...");
            schedulerFactoryBean = new SchedulerFactoryBean();
            schedulerFactoryBean.setJobFactory(baseJobFactory);
            Properties properties = new Properties();
            InputStream input = this.getClass().getClassLoader().getResourceAsStream("./quartz.properties");
            schedulerFactoryBean.setApplicationContext(applicationContext);
            properties.load(input);
            schedulerFactoryBean.setQuartzProperties(properties);
        } catch (Exception e) {
            logger.info("Init scheduler factory bean error...", e);
        }
        return schedulerFactoryBean;
    }
    
    @Bean
    public Scheduler scheduler(ApplicationContext applicationContext) {
        return schedulerFactoryBean(applicationContext).getObject();
    }
}
