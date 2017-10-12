package com.avery.recuritcloud.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class PublisherMail {
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    public void sendEmail()
    {
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom("15591772025@163.com");
        simpleMailMessage.setTo("519492353@qq.com");
        simpleMailMessage.setSubject("你哥哥我");
        simpleMailMessage.setText("做什么着呢？");
        javaMailSender.send(simpleMailMessage);
    }
}
