package com.recurit.averyrecuritcloud.service;

import com.avery.recuritcloud.AveryRecuritCloudApplication;
import com.avery.recuritcloud.common.enums.CompanyStatusEnum;
import com.avery.recuritcloud.entity.domain.Company;
import com.avery.recuritcloud.repository.CompanyRepository;
import com.avery.recuritcloud.service.GrabService;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AveryRecuritCloudApplication.class)
public class GrabServiceTest {
    
    
    @Autowired
    private  GrabService grabService;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Test
    public void testSendFailMailToCompany()
    {
        List<Company> companyList=companyRepository.findByStatus(CompanyStatusEnum.CREATED.getStatus());
        Company company=companyList.get(0);
        
        grabService.sendEmailGrabSuccess(company.getId());
    }
}
