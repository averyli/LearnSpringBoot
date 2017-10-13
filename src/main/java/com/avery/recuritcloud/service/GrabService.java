package com.avery.recuritcloud.service;

import com.avery.recuritcloud.common.enums.CompanyStatusEnum;
import com.avery.recuritcloud.common.enums.ConcurrentHashMapSingleton;
import com.avery.recuritcloud.common.enums.ExceutorServiceSingleton;
import com.avery.recuritcloud.common.enums.StatusEnum;
import com.avery.recuritcloud.common.enums.TalentStatusEnum;
import com.avery.recuritcloud.entity.domain.Company;
import com.avery.recuritcloud.entity.domain.MessageSending;
import com.avery.recuritcloud.entity.domain.Talent;
import com.avery.recuritcloud.repository.CompanyRepository;
import com.avery.recuritcloud.repository.MessageSendingRepository;
import com.avery.recuritcloud.repository.TalentRepository;
import com.avery.recuritcloud.service.listener.GrabTalentsThread;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.PostConstruct;
import javax.mail.internet.AddressException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

@Service
@Transactional
public class GrabService implements EnvironmentAware,InitializingBean {
    
    public static Logger log= LoggerFactory.getLogger(GrabService.class);
    
    Environment env;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private MessageSendingRepository messageSendingRepository;
    
    @PostConstruct
    public void init()
    {
        Integer threadCount=env.getProperty("threadpool.threadCount",Integer.class);
        ExceutorServiceSingleton.INSTANCE.init(threadCount);
        //将所有的draft都放入在池子中，等待被抢
        pushDraftToPool(talentRepository.findAllByStatus(TalentStatusEnum.IN_POOL.getStatus()));
    }


    public void pushDraftToPool(List<Talent> talentList)
    {
        for(Talent talent : talentList)
        {
            ConcurrentLinkedQueue<Long> companyQueue=new ConcurrentLinkedQueue<>();
            log.info("talent:{} is pushed in pool...",talent.getId());
            ConcurrentHashMapSingleton.INSTANCE.setCompanyQueue(talent.getId(),companyQueue);
            
            log.info("ExceutorServiceSingleton Submit GrabTalentsThread...");
            ExceutorServiceSingleton.INSTANCE.submit(new GrabTalentsThread(talent.getId(),this));
        }
    }

    @Async
    public void sendEmailGrabFail(ConcurrentLinkedQueue<Long> companyQueue,Long talentId)
    {
        for (Long companyId:companyQueue)
        {
            Company company=companyRepository.findOne(companyId);
            Talent talent=talentRepository.findOne(talentId);
            
            MessageSending messageSending=createGrabMessageSending(company,talent,false);
            messageSendingRepository.save(messageSending);
            // log.info("Send GrabFail Email To Company:{},mail:{}...",company.getId(),company.getEmail());
            // emailService.sendEmailGrabFail(company.getEmail(),talentRepository.findOne(talentId));
        }
    }

    public void sendEmailGrabSuccess(Long companyId,Long talentId) {
        Company company=companyRepository.findOne(companyId);
        Talent talent=talentRepository.findOne(talentId);
    
        MessageSending messageSending=createGrabMessageSending(company,talent,true);
        
        messageSendingRepository.save(messageSending);
        //log.info("Send GrabSuccess Email To Company:{}...",company.getId());
        //emailService.sendEmailGrabSuccess(company.getEmail(),talentRepository.findOne(talentId));
    }

    public MessageSending createGrabMessageSending(Company company,Talent talent,boolean isSuccess)
    {
        MessageSending messageSending=new MessageSending();
    
        messageSending.setSendTo(company.getEmail());
        messageSending.setSubject("Grab Talent");
        messageSending.setStatus(StatusEnum.VALID.getStatus());
    
        String message=null;
        if(isSuccess)
        {
            messageSending.setRecord("grab talent success");
            message= MessageFormat.format("您成功招聘了此天才:{0}",talent.toString());
        }
        else
        {
            messageSending.setRecord("grab talent fail");
            message=MessageFormat.format("您失败招聘此天才:{0}",talent.toString());
        }
       
        messageSending.setMessage(message);
    
    
        ObjectMapper objectMapper=new ObjectMapper();
        String talentInfo= null;
        try {
            talentInfo = objectMapper.writeValueAsString(talent);
        } catch (JsonProcessingException e) {
            log.info("convert to JSON String error: ",e.getMessage());
        }
        messageSending.setTalentInfo(talentInfo);
    
        messageSendingRepository.save(messageSending);
        return messageSending;
    }
    
    // public void GrabTalents(Company company,Talent talent)
    // {
    //     if (talent==null || company==null)
    //         return;
    //     ConcurrentLinkedQueue<Long> companyQueue=ConcurrentHashMapSingleton.INSTANCE.getCompanyQueue(talent.getId());
    //     if(companyQueue==null)//表明这个人来迟了，人才已经被其他人抢走
    //     {
    //         emailService.sendEmailGrabFail(company.getEmail(),talent);
    //     }
    //     else
    //     {
    //         companyQueue.add(talent.getId());
    //     }
    // }
    
    public void CompanyToGrabTalents(Company company,Talent talent)
    {
        log.info("CompanyToGrabTalents start...");
        ConcurrentLinkedQueue companyQueue=ConcurrentHashMapSingleton.INSTANCE.getCompanyQueue(talent.getId());
        if(companyQueue==null)//当company去抢talent时，若talent已经不在池子了，就证明已被抢走，故抢夺失败
        {
            MessageSending messageSending=createGrabMessageSending(company,talent,false);
            messageSendingRepository.save(messageSending);
           // emailService.sendEmailGrabFail(company.getEmail(),talent);
        }
        else
        {
            companyQueue.add(company.getId());
            company.setStatus(CompanyStatusEnum.IN_POOL.getStatus());
            companyRepository.save(company);
        }
        log.info("CompanyToGrabTalents end...");
    }
    
    public void pushTalentsToCompany(Long talentsId)
    {
        List<Company> companyList=companyRepository.findByIsAcceptPushTalents(true);
        for (Company company:companyList)
        {
            emailService.sendPushTalents(company.getEmail());
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        env=environment;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
    
    }
}
