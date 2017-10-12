package com.avery.recuritcloud.service;

import com.avery.recuritcloud.common.enums.ConcurrentHashMapSingleton;
import com.avery.recuritcloud.common.enums.ExceutorServiceSingleton;
import com.avery.recuritcloud.entity.domain.Company;
import com.avery.recuritcloud.entity.domain.Talent;
import com.avery.recuritcloud.repository.CompanyRepository;
import com.avery.recuritcloud.repository.TalentRepository;
import com.avery.recuritcloud.service.listener.GrabTalentsThread;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GrabService implements EnvironmentAware {

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
        //ExceutorServiceSingleton.INSTANCE.init(env.getProperty("threadpool.threadCount",Integer.class));
        //将所有的draft都放入在池子中，等待被抢
        pushDraftToPool(talentRepository.findAll());
    }

    private void pushDraftToPool(List<Talent> talentList)
    {
        for(Talent talent : talentList)
        {
            ConcurrentLinkedQueue<Long> companyQueue=new ConcurrentLinkedQueue<>();
            ConcurrentHashMapSingleton.INSTANCE.setCompanyQueue(talent.getId(),companyQueue);
            ExceutorServiceSingleton.INSTANCE.submit(new GrabTalentsThread(talent.getId(),this));
        }
    }

    public void sendEmailGrabFail(ConcurrentLinkedQueue<Long> companyQueue)
    {
        for (Long companyId:companyQueue)
        {
            Company company=companyRepository.findOne(companyId);
            emailService.sendEmailGrabFail(company.getEmail());
        }
    }

    public void sendEmailGrabSuccess(Long companyId)
    {
        Company company=companyRepository.findOne(companyId);
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
}
