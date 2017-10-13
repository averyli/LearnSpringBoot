package com.avery.recuritcloud.service.listener;

import com.avery.recuritcloud.common.arithmetic.GrabTalentsArthmetic;
import com.avery.recuritcloud.common.enums.ConcurrentHashMapSingleton;
import com.avery.recuritcloud.entity.domain.Company;
import com.avery.recuritcloud.entity.domain.Talent;
import com.avery.recuritcloud.service.GrabService;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GrabTalentsThread implements Runnable {

    public static Logger logger= LoggerFactory.getLogger(GrabTalentsThread.class);
    
    @Autowired
    GrabService grabService;

    private Long talentsId;

    public GrabTalentsThread(Long talentsId,GrabService grabService) {
        this.talentsId = talentsId;
        this.grabService=grabService;
    }

    @Override
    public void run() {
        while (true) {
            threadSleep(5000L);
            if (!talentsInPool()) {
                break;
            }
            if(startGrabTalents())
            {
                break;
            }
            else
            {
                continue;
            }
        }
        logger.info("GrabTalentsThread shutdown...");
    }

    private boolean talentsInPool() {
        logger.info("the talent:{} is  pool's queue:{}...",talentsId,ConcurrentHashMapSingleton.INSTANCE.getCompanyQueue(talentsId));
        if (ConcurrentHashMapSingleton.INSTANCE.getCompanyQueue(talentsId) != null) {
            return true;
        }
        logger.info("the talent:{} is not  already pool...",talentsId);
        return false;
    }

    private Boolean startGrabTalents() {
        logger.info("company start grab talent...");
        Long companyId = GrabTalentsArthmetic.firstComeGrabTalentsArthmetic(ConcurrentHashMapSingleton.INSTANCE.getCompanyQueue(talentsId));
        logger.info("The Company:{} Grab Talent:{} Success... ",companyId,talentsId);
        if(companyId!=null)
        {
            ConcurrentHashMapSingleton.INSTANCE.removeKey(talentsId);
            //发送给抢单车成功邮件
            grabService.sendEmailGrabSuccess(companyId);
            //给抢单失败的发送邮件
            logger.info("GrabFail CompanyQueue:{} ",ConcurrentHashMapSingleton.INSTANCE.getCompanyQueue(talentsId));
            grabService.sendEmailGrabFail(ConcurrentHashMapSingleton.INSTANCE.getCompanyQueue(talentsId));
            logger.info("Grab talents end...");
            return true;
        }
        return false;
    }


    
    private void threadSleep(Long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.interrupted();
        }
    }
}
