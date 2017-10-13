package com.avery.recuritcloud.common.quartz;

import com.avery.recuritcloud.common.enums.CompanyStatusEnum;
import com.avery.recuritcloud.entity.domain.Company;
import com.avery.recuritcloud.entity.domain.Member;
import com.avery.recuritcloud.entity.domain.User;
import com.avery.recuritcloud.repository.CompanyRepository;
import com.avery.recuritcloud.repository.MemberRepository;
import com.avery.recuritcloud.repository.UserRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateSaveCompanyJob implements Job{
    public static Logger logger= LoggerFactory.getLogger(CreateSaveCompanyJob.class);
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("execute CreateSaveCompanyJob...");
        Company company=new Company();
        User president=new User();
        Member member=new Member();
        
        member.setLevel(999999);
        member.setName("钻石");
        Member sacedMember=memberRepository.save(member);
        
        president.setEmail("3056765795@qq.com");
        president.setIsMember(true);
        president.setLogin("avery");
        president.setPassword("123456");
        president.setMember(sacedMember);
        User savedUser=userRepository.save(president);
        
        company.setAcceptPushTalents(true);
        company.setAddress("CA");
        company.setEmail("3056765795@qq.com");
        company.setName("next");
        company.setPersonCount(5000);
        company.setPresident(savedUser);
        company.setPrivilege(true);
        company.setStatus(CompanyStatusEnum.CREATED.getStatus());
        
        companyRepository.save(company);
    }
}
