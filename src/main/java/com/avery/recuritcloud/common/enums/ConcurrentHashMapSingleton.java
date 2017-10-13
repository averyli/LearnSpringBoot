package com.avery.recuritcloud.common.enums;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ConcurrentHashMapSingleton {
    
    INSTANCE;
    public static Logger logger=LoggerFactory.getLogger(ConcurrentHashMapSingleton.class);
    static ConcurrentMap<Long, ConcurrentLinkedQueue<Long>> talentsToCompanyQueueMap;
    
    static {
        talentsToCompanyQueueMap = new ConcurrentHashMap<>();
    }
    
    public static ConcurrentLinkedQueue<Long> getCompanyQueue(Long talentsId) {
        return talentsToCompanyQueueMap.get(talentsId);
    }
    
    public static void setCompanyQueue(Long talentsId, ConcurrentLinkedQueue companyQueue) {
        logger.info("setCompanyQueue:{}...",companyQueue);
        talentsToCompanyQueueMap.put(talentsId, companyQueue);
    }
    
    public void removeKey(Long talentsId)
    {
        talentsToCompanyQueueMap.remove(talentsId);
    }
}
