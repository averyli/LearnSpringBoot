package com.avery.recuritcloud.service;

import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

// @ConfigurationProperties(prefix = "spring.mail",value="mail.properties)
// 读取mail.properties中的配置,并将其注入到这个类的属性中来，给予其赋值
//不填value的值，会自动加载applicatio.properties
@Service
public class EmailService{
    
    public static final String PUBBLISHER_EMAIL_TEMPLATE = "publisher";
    
    public static final Logger logger= LoggerFactory.getLogger(EmailService.class);
    
    @Value("${spring.mail.username}")
    private String emailFrom;
    // @Autowired
    
    @Autowired
    private SpringTemplateEngine springTemplateEngine;
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    //@PostConstruct注解在程序加载servlet时调用，且只调用一次
    //@PreConstruct注解在程序卸载servlet时调用，且只调用一次
    // @PostConstruct
    // public void init()
    // {
    //     emailFrom=env.getProperty("spring.mail.username");
    // }
    
    public void sendEmailWY(SimpleMailMessage message) {
        javaMailSender.send(message);
    }
    
    public void sendEmailTemplate(final Object data, String sendTo, String subject) {
        MimeMessagePreparator mimeMessagePreparator = getMimeMessagePreparator(data, sendTo, subject);
        javaMailSender.send(mimeMessagePreparator);
        
    }
    
    public void sendEmailGrabFail(String sendTo) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        
        simpleMailMessage.setFrom(emailFrom);
        simpleMailMessage.setTo(sendTo);
        simpleMailMessage.setText("抢单失败");
        try
        {
            javaMailSender.send(simpleMailMessage);
        }
        catch (Exception e)
        {
            logger.info("Email Address error:{},e.message():{}",sendTo,e.getMessage());
        }
    }
    
    public void sendEmailGrabSuccess(String sendTo) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        
        simpleMailMessage.setFrom(emailFrom);
        simpleMailMessage.setTo(sendTo);
        simpleMailMessage.setText("抢单成功");
        
        javaMailSender.send(simpleMailMessage);
    }
    
    public void sendPushTalents(String sendTo) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    
        simpleMailMessage.setFrom(emailFrom);
        simpleMailMessage.setTo(sendTo);
        simpleMailMessage.setText("您有一条新的单子，请注意查收");
    
        javaMailSender.send(simpleMailMessage);
    }
    
    private MimeMessagePreparator getMimeMessagePreparator(final Object data, String sendTo, String subject) {
        return new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
                mimeMessageHelper.setFrom(emailFrom);
                mimeMessageHelper.setTo(sendTo);
                mimeMessageHelper.setSubject(subject);
                Map<String, Object> model = new HashMap<>();
                model.put("customers", data);
                mimeMessageHelper.setText(getText(model), true);
                mimeMessageHelper.addAttachment("0c26eb09-4375-4adb-a458-6f451792f432.jpg", new ClassPathResource
                    ("static/0c26eb09-4375-4adb-a458-6f451792f432.jpg"));
            }
        };
    }
    
    private String getText(Map<String, ?> params) {
        Context context = new Context(LocaleContextHolder.getLocale());
        context.setVariables(params);
        return springTemplateEngine.process(PUBBLISHER_EMAIL_TEMPLATE, context);
    }
    
}
