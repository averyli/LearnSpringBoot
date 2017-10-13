package com.avery.recuritcloud.common.enums;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ExceutorServiceSingleton {
    INSTANCE;
    
    public static Logger logger= LoggerFactory.getLogger(ExceutorServiceSingleton.class);
    static ExecutorService executorService = null;

    public void init(int threadCount) {
        logger.info("init executorService:{}",executorService);
        if(executorService==null)
        {
            logger.info("init executorService:{},threadcount:{}",executorService,threadCount);
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
    }
    
    public void submit(Runnable r)
    {
        logger.info("submit runnable:{}...",executorService);
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
