package com.avery.recuritcloud.common.quartz;

import com.avery.recuritcloud.common.enums.TalentStatusEnum;
import com.avery.recuritcloud.entity.domain.Member;
import com.avery.recuritcloud.entity.domain.Talent;
import com.avery.recuritcloud.entity.domain.User;
import com.avery.recuritcloud.repository.MemberRepository;
import com.avery.recuritcloud.repository.TalentRepository;
import com.avery.recuritcloud.repository.UserRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateTalentJob implements Job {
    public  static  final Logger logger= LoggerFactory.getLogger(CreateTalentJob.class);
    
    @Autowired
    private TalentRepository talentRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("create talent and save... start");
        Talent talent=new Talent();
        
        User user=new User();
        Member member=new Member();
        member.setLevel(999999);
        member.setName("super");
        
        Member savedMember=memberRepository.save(member);
        
        user.setMember(savedMember);
        user.setEmail("15591772025@163.com");
        user.setLogin("admin");
        user.setIsMember(true);
        user.setPassword("123456");
        
        User savedUser=userRepository.save(user);
        
        
        talent.setStatus(TalentStatusEnum.CREATED.getStatus());
        talent.setUser(savedUser);
        talentRepository.save(talent);
        logger.info("create talent and save... end");
    }
}
