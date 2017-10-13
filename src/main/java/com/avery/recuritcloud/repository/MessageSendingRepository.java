package com.avery.recuritcloud.repository;

import com.avery.recuritcloud.entity.domain.MessageSending;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageSendingRepository extends JpaRepository<MessageSending,Long> {
    public List<MessageSending> findByStatus(Integer status);
    
    @Query(value="select * from AVERY_MESSAGE_SENDING ms where ms.status=:status limit 10",nativeQuery = true)
    List<MessageSending> findAllByStatus(@Param(value = "status") Integer status);
}
