package com.exc.street.light.dlm.netty;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xujiahaoxixi
 * @date 2017/10/20 16:12
 * @description 线程工厂
 */
public class NamedThreadFactory implements ThreadFactory {
//    private final static Logger logger = LoggerFactory.getLogger(NamedThreadFactory.class);

    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    private final AtomicInteger mThreadNum = new AtomicInteger(1);

    private final String prefix;//前缀

    private final boolean daemoThread;//是否为守护线程

    private final ThreadGroup threadGroup;//线程组

    public NamedThreadFactory() {
        this("excserver-threadpool-" + threadNumber.getAndIncrement(), false);
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    //生成的线程为守护线程还是业务线程daemo为true为守护
    public NamedThreadFactory(String prefix, boolean daemo) {
        this.prefix = prefix + "-thread-";
        daemoThread = daemo;
        SecurityManager s = System.getSecurityManager();//安全管理器，管理threadGroup
        threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = prefix + mThreadNum.getAndIncrement();
        Thread ret = new Thread(threadGroup, runnable, name, 0);
        ret.setDaemon(daemoThread);
//        logger.debug("已经创建业务守护线程:{} id为 {}", name, ret.getId());
//        StaticUtils.map.put(ret.getId(), 0);
        return ret;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }
}

