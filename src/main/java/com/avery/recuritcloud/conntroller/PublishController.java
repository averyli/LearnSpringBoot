package com.avery.recuritcloud.conntroller;

import com.avery.recuritcloud.mail.PublisherMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/frontweb")
public class PublishController {
    
    @Autowired
    private PublisherMail publisherMail;
    
    
}
