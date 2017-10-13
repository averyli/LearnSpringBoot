package com.avery.recuritcloud.repository;

import com.avery.recuritcloud.entity.domain.Talent;
import java.util.List;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalentRepository extends JpaRepository<Talent, Long> {
    List<Talent> findAllByStatus(Integer status);
}
