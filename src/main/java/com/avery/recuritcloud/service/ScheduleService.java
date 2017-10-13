package com.avery.recuritcloud.service;

import com.avery.recuritcloud.common.enums.StatusEnum;
import com.avery.recuritcloud.entity.domain.MessageSending;
import com.avery.recuritcloud.repository.MessageSendingRepository;
import java.util.List;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

@Service
@Configurable
@EnableScheduling
public class ScheduleService implements ServletContextAware{
    
    public static Logger logger= LoggerFactory.getLogger(ScheduleService.class);
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private MessageSendingRepository messageSendingRepository;
    
    @Scheduled(cron = "0 0/1 0 * * ?")
    public void sendEmail()
    {
        logger.info("ScheduleService sendEmail() start...");
        List<MessageSending> messageSendingList=messageSendingRepository.findAllByStatus(StatusEnum.VALID.getStatus());
        
        for (MessageSending messageSending:messageSendingList)
        {
            emailService.sendEmailGrab(messageSending);
            
            messageSending.setStatus(StatusEnum.INVALID.getStatus());
            messageSendingRepository.save(messageSending);
        }
        logger.info("ScheduleService sendEmail() end...");
    }
    
    @Override
    public void setServletContext(ServletContext servletContext) {
    
    }
}
