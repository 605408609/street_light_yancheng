package com.exc.street.light.sl.netty.zkzl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @aythor xujiahaoxixi
 * @data 2017/10/20 16:12
 * @description线程工厂
 */
public class NamedThreadFactory implements ThreadFactory {
    private final static Logger logger = LoggerFactory.getLogger(NamedThreadFactory.class);

    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    private final AtomicInteger mThreadNum = new AtomicInteger(1);

    /**
     * 前缀
     */
    private final String prefix;

    /**
     * 是否为守护线程
     */
    private final boolean daemoThread;

    /**
     * 线程组
     */
    private final ThreadGroup threadGroup;

    public NamedThreadFactory() {
        this("excserver-threadpool-" + threadNumber.getAndIncrement(), false);
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    /**
     * 生成的线程为守护线程还是业务线程daemo为true为守护
     * @param prefix
     * @param daemo
     */
    public NamedThreadFactory(String prefix, boolean daemo) {
        this.prefix = prefix + "-thread-";
        daemoThread = daemo;
        //安全管理器，管理threadGroup
        SecurityManager s = System.getSecurityManager();
        threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = prefix + mThreadNum.getAndIncrement();
        Thread ret = new Thread(threadGroup, runnable, name, 0);
        ret.setDaemon(daemoThread);
        logger.debug("已经创建业务守护线程:{} id为 {}", name, ret.getId());
        return ret;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }
}

