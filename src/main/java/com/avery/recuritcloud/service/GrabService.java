package com.avery.recuritcloud.service;

import com.avery.recuritcloud.common.enums.CompanyStatusEnum;
import com.avery.recuritcloud.common.enums.ConcurrentHashMapSingleton;
import com.avery.recuritcloud.common.enums.ExceutorServiceSingleton;
import com.avery.recuritcloud.common.enums.TalentStatusEnum;
import com.avery.recuritcloud.entity.domain.Company;
import com.avery.recuritcloud.entity.domain.Talent;
import com.avery.recuritcloud.repository.CompanyRepository;
import com.avery.recuritcloud.repository.TalentRepository;
import com.avery.recuritcloud.service.listener.GrabTalentsThread;
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

    public void sendEmailGrabFail(ConcurrentLinkedQueue<Long> companyQueue)
    {
        for (Long companyId:companyQueue)
        {
            Company company=companyRepository.findOne(companyId);
            log.info("Send GrabFail Email To Company:{},mail:{}...",company.getId(),company.getEmail());
            emailService.sendEmailGrabFail(company.getEmail());
        }
    }

    public void sendEmailGrabSuccess(Long companyId)
    {
        Company company=companyRepository.findOne(companyId);
        log.info("Send GrabSuccess Email To Company:{}...",company.getId());
        emailService.sendEmailGrabSuccess(company.getEmail());
    }

    public void GrabTalents(Company company)
    {
        company=companyRepository.findOne(company.getId());
        if (company==null)
            return;
        ConcurrentLinkedQueue<Long> companyQueue=ConcurrentHashMapSingleton.INSTANCE.getCompanyQueue(company.getId());
        if(companyQueue==null)//表明这个人来迟了，人才已经被其他人抢走
        {
            emailService.sendEmailGrabFail(company.getEmail());
        }
        else
        {
            companyQueue.add(company.getId());
        }
    }
    
    public void CompanyToGrabTalents(Company company,Talent talent)
    {
        log.info("CompanyToGrabTalents start...");
        ConcurrentLinkedQueue companyQueue=ConcurrentHashMapSingleton.INSTANCE.getCompanyQueue(talent.getId());
        if(companyQueue==null)//当company去抢talent时，若talent已经不在池子了，就证明已被抢走，故抢夺失败
        {
            emailService.sendEmailGrabFail(company.getEmail());
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
