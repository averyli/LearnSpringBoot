package com.avery.recuritcloud.service.listener;

import com.avery.recuritcloud.common.arithmetic.GrabTalentsArthmetic;
import com.avery.recuritcloud.common.enums.ConcurrentHashMapSingleton;
import com.avery.recuritcloud.service.GrabService;

public class GrabTalentsThread implements Runnable {

    GrabService grabService;

    private Long talentsId;

    public GrabTalentsThread(Long talentsId,GrabService grabService) {
        this.talentsId = talentsId;
        this.grabService=grabService;
    }

    @Override
    public void run() {
        while (true) {
            threadSleep(500L);
            if (!companyInPool()) {
                continue;
            }
            startGrabTalents();
        }
    }

    private boolean companyInPool() {
        if (ConcurrentHashMapSingleton.INSTANCE.getCompanyQueue(talentsId) != null) {
            return true;
        }
        return false;
    }

    private void startGrabTalents() {
        Long companyId = GrabTalentsArthmetic.priceGrabTalentsArthmetic(ConcurrentHashMapSingleton.INSTANCE.getCompanyQueue(talentsId));
        //发送给抢单车成功邮件
        grabService.sendEmailGrabSuccess(companyId);
        //给抢单失败的发送邮件
        grabService.sendEmailGrabFail(ConcurrentHashMapSingleton.INSTANCE.getCompanyQueue(talentsId));

        ConcurrentHashMapSingleton.INSTANCE.removeKey(talentsId);
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
