package com.avery.recuritcloud.common.quartz;

import com.avery.recuritcloud.common.enums.CompanyStatusEnum;
import com.avery.recuritcloud.common.enums.TalentStatusEnum;
import com.avery.recuritcloud.entity.domain.Company;
import com.avery.recuritcloud.entity.domain.Talent;
import com.avery.recuritcloud.repository.CompanyRepository;
import com.avery.recuritcloud.repository.TalentRepository;
import com.avery.recuritcloud.service.GrabService;
import java.util.List;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CompanyGrabTalentJob implements Job {
    public static Logger logger= LoggerFactory.getLogger(CompanyGrabTalentJob.class);
    
    @Autowired
    private TalentRepository talentRepository;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private GrabService grabService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    
       // List<Company> companyList=companyRepository.findByIsAcceptPushTalents(true);
        List<Company> companyList=companyRepository.findByStatus(CompanyStatusEnum.CREATED.getStatus());
        for (Company company:companyList) {
            logger.info("prepare to grab talents for these companies:{}",companyList.toString());
            List<Talent> talentList=talentRepository.findAllByStatus(TalentStatusEnum.IN_POOL.getStatus());
            logger.info("The company:{} prepare to grab these talents:{}",company.getId(),talentList.toString());
            for (Talent talent:talentList) {
                grabService.CompanyToGrabTalents(company,talent);
            }
        }
        
    }
}
