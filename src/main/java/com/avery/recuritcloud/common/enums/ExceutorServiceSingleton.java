package com.avery.recuritcloud.common.enums;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public enum ExceutorServiceSingleton {
    INSTANCE;
    
    static ExecutorService executorService = null;
    
    public void init(int threadCount) {
        //ThreadFactory threadFactory=Executors.defaultThreadFactory();
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread newThread = new Thread(r);
                newThread.setDaemon(true);
                newThread.setName("fixed-thread-pool");
                newThread.setPriority(Integer.valueOf(10));
                return newThread;
            }
        };
        executorService = Executors.newFixedThreadPool(threadCount,threadFactory);
    }
    
    public void submit(Runnable r)
    {
        if(executorService!=null)
        {
            executorService.submit(r);
        }
    }
    
    public void close()
    {
        if(executorService!=null)
        {
            executorService.shutdown();
        }
    }
}
