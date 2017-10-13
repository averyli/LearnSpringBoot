package com.avery.recuritcloud.common.quartz.job;

import com.avery.recuritcloud.common.enums.ConcurrentHashMapSingleton;
import com.avery.recuritcloud.common.enums.ExceutorServiceSingleton;
import com.avery.recuritcloud.common.enums.TalentStatusEnum;
import com.avery.recuritcloud.entity.domain.Member;
import com.avery.recuritcloud.entity.domain.Talent;
import com.avery.recuritcloud.entity.domain.User;
import com.avery.recuritcloud.repository.MemberRepository;
import com.avery.recuritcloud.repository.TalentRepository;
import com.avery.recuritcloud.repository.UserRepository;
import com.avery.recuritcloud.service.GrabService;
import com.avery.recuritcloud.service.listener.GrabTalentsThread;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateSaveTalentJob implements Job {
    public  static  final Logger logger= LoggerFactory.getLogger(CreateSaveTalentJob.class);
    
    @Autowired
    private TalentRepository talentRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GrabService grabService;
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("execute CreateTalentJob...");
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
        
        
        talent.setStatus(TalentStatusEnum.IN_POOL.getStatus());
        talent.setUser(savedUser);
        talentRepository.save(talent);
        
        listenTalent(talent);
    }
    
    public void listenTalent(Talent talent)
    {
        logger.info("ExceutorServiceSingleton Submit GrabTalentsThread To Listen The Talent:{}...",talent.getId());
        ConcurrentHashMapSingleton.INSTANCE.setCompanyQueue(talent.getId(),new ConcurrentLinkedQueue());
        ExceutorServiceSingleton.INSTANCE.submit(new GrabTalentsThread(talent.getId(),grabService));
    }
}
