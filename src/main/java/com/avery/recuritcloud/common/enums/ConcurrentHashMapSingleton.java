package com.avery.recuritcloud.common.enums;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public enum ConcurrentHashMapSingleton {
    INSTANCE;
    
    static ConcurrentMap<Long, ConcurrentLinkedQueue<Long>> talentsToCompanyQueueMap;
    
    static {
        talentsToCompanyQueueMap = new ConcurrentHashMap<>();
    }
    
    public static ConcurrentLinkedQueue<Long> getCompanyQueue(Long talentsId) {
        return talentsToCompanyQueueMap.get(talentsId);
    }
    
    public static void setCompanyQueue(Long talentsId, ConcurrentLinkedQueue companyQueue) {
        talentsToCompanyQueueMap.put(talentsId, companyQueue);
    }
    
    public void removeKey(Long talentsId)
    {
        talentsToCompanyQueueMap.remove(talentsId);
    }
}
