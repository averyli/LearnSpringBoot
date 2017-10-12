package com.avery.recuritcloud.repository;

import com.avery.recuritcloud.entity.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
    
}
