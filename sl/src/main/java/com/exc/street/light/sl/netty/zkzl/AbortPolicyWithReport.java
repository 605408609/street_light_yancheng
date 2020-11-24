package com.exc.street.light.sl.netty.zkzl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @aythor xujiahaoxixi
 * @data 2017/10/20 16:10
 * @contiune线程池异常处理策略
 */
public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {

    private final static Logger logger = LoggerFactory.getLogger(AbortPolicyWithReport.class);

    private final String threadName;

    public AbortPolicyWithReport(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        String msg = String.format("ExcServer["
                        + " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d),"
                        + " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)]",
                threadName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(),
                e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating());
        System.out.println(msg);
        logger.error("{}", msg);
        throw new RejectedExecutionException(msg);
    }
}
