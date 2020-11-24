package com.exc.street.light.sl.netty.shuncom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author xujiahaoxixi
 * @date 2017/10/20 16:09
 */
public class ExcThreadPool {
    private final static Logger logger = LoggerFactory.getLogger(ExcThreadPool.class);

    /**
     * 独立出线程池主要是为了应对复杂耗I/O操作的业务，不阻塞netty的handler线程而引入
     * 当然如果业务足够简单，把处理逻辑写入netty的handler（ChannelInboundHandlerAdapter）也未尝不可
     * @param threads
     * @param queues
     * @return
     */
    public static Executor getExecutor(int threads, int queues) {
        String name = "ExcThreadPool";
        //@thread 核心线程数
        //@captacity最大线程数
        //@keepAliveTIme空余的线程存活的时间，当多余的线程完成任务的时候，需要多长时间进行回收，时间单位是unit 去控制
        //TimeUnit时间的单位
        //@workQueue存放Runnable对象
        //ThreadFactory自定义的线程工厂
        //DefaultHandle异常处理的策略
        logger.debug("Creat the thread pool succeess ,the theads num {},the thead pool mode {}", threads, queues);
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
                queues == 0 ? new SynchronousQueue<Runnable>()
                        : (queues < 0 ? new LinkedBlockingQueue<Runnable>()
                        : new LinkedBlockingQueue<Runnable>(queues)),
                new NamedThreadFactory(name, true), new AbortPolicyWithReport(name));
    }
}
