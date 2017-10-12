package com.recurit.averyrecuritcloud.mail;

import com.avery.recuritcloud.AveryRecuritCloudApplication;
import com.avery.recuritcloud.entity.model.PublisherModel;
import com.avery.recuritcloud.mail.PublisherMail;
import com.avery.recuritcloud.service.EmailService;
import com.avery.recuritcloud.util.EmailUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AveryRecuritCloudApplication.class)
public class PublisherMailTest {
    
    @Autowired
    PublisherMail publisherMail;
    
    @Autowired
    private EmailService emailService;
    
    // @Test
    // public void testSend()
    // {
    //     publisherMail.sendEmail();
    // }
    //
    @Test
    public void sendEmailTemplate()
    {
        PublisherModel publisherModel= EmailUtil.getPublisherEmailModel();
        emailService.sendEmailTemplate(publisherModel,"3056765795@qq.com","Email Template Test!");
    }
}
