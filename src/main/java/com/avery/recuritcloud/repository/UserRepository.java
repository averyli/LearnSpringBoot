package com.avery.recuritcloud.repository;

import com.avery.recuritcloud.entity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    
}
