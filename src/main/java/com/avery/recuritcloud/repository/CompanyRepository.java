package com.avery.recuritcloud.repository;

import com.avery.recuritcloud.entity.domain.Company;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository  extends JpaRepository<Company,Long> {
    List<Company> findByIsAcceptPushTalents(Boolean isAcceptPushTalents);
    
    List<Company> findByStatus(Integer status);
}
