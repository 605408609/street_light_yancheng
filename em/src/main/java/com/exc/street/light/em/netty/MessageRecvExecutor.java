package com.exc.street.light.em.netty;

import com.exc.street.light.em.config.HttpApi;
import com.exc.street.light.em.mapper.MeteorologicalHistoryDao;
import com.exc.street.light.em.mapper.MeteorologicalRealDao;
import com.exc.street.light.em.service.MeteorologicalDeviceService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.spi.SelectorProvider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xujiahao
 * @data 2017/10/19 14:55
 */
public class MessageRecvExecutor {
    private final static Logger logger = LoggerFactory.getLogger(MessageRecvExecutor.class);

    private int localPort;

    private MeteorologicalDeviceService meteorologicalDeviceService;

    private MeteorologicalRealDao meteorologicalRealDao;

    private MeteorologicalHistoryDao meteorologicalHistoryDao;

    private HttpApi httpApi;

    private Map<String, Object> handlerMap = new ConcurrentHashMap<String, Object>();

    private static ThreadPoolExecutor threadPoolExecutor;

    public MessageRecvExecutor(int localPort, MeteorologicalDeviceService meteorologicalDeviceService, MeteorologicalRealDao meteorologicalRealDao, MeteorologicalHistoryDao meteorologicalHistoryDao, HttpApi httpApi) {
        this.localPort = localPort;
        this.meteorologicalDeviceService = meteorologicalDeviceService;
        this.meteorologicalRealDao = meteorologicalRealDao;
        this.meteorologicalHistoryDao = meteorologicalHistoryDao;
        this.httpApi = httpApi;
    }

    /**
     * 创建static线程task加入线程池
     *
     * @param task
     */
    public static void submit(Runnable task) {
        if (threadPoolExecutor == null) {
            //添加了锁以后只可能同时有一个MessageRecvExecutor类的对象对方法内的内容操作，保持原子性，也就是确保只创建一个线程池。
            synchronized (MessageRecvExecutor.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = (ThreadPoolExecutor) ExcThreadPool.getExecutor(100, -1);
                    logger.debug("创建ThreadPoolExecutor线程池成功");
                }
            }
        }
        threadPoolExecutor.submit(task);
    }

    public void afterPropertiesSet() throws Exception {
        //netty的线程池模型设置成主从线程池模式，这样可以应对高并发请求
        //当然netty还支持单线程、多线程网络IO模型，可以根据业务需求灵活配置
        ThreadFactory threadRpcFactory = new NamedThreadFactory("NettyEXC ThreadFactory");

        //方法返回到Java虚拟机的可用的处理器数量
        //int parallel = Runtime.getRuntime().availableProcessors() * 2;

        /**
         * NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器，
         * Netty提供了许多不同的EventLoopGroup的实现用来处理不同传输协议。
         * 在这个例子中我们实现了一个服务端的应用，
         * 因此会有2个NioEventLoopGroup会被使用。
         * 第一个经常被叫做‘boss’，用来接收进来的连接。
         * 第二个经常被叫做‘worker’，用来处理已经被接收的连接，
         * 一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上。
         * 如何知道多少个线程已经被使用，如何映射到已经创建的Channels上都需要依赖于EventLoopGroup的实现，
         * 并且可以通过构造函数来配置他们的关系。
         */
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup(8, threadRpcFactory, SelectorProvider.provider());

        try {
            /**
             * ServerBootstrap 是一个启动NIO服务的辅助启动类
             * 你可以在这个服务中直接使用Channel
             */
            ServerBootstrap bootstrap = new ServerBootstrap();
            /**
             * 这一步是必须的，如果没有设置group将会报java.lang.IllegalStateException: group not set异常
             */
            bootstrap.group(boss, worker)
                    /***
                     * ServerSocketChannel以NIO的selector为基础进行实现的，用来接收新的连接
                     * 这里告诉Channel如何获取新的连接.
                     */
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MessageRecvChannelInitializer(handlerMap, meteorologicalDeviceService, meteorologicalRealDao, meteorologicalHistoryDao, httpApi))
                    //.option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true);

            /***
             * 绑定端口并启动去接收进来的连接
             */
            ChannelFuture future = bootstrap.bind(localPort).sync();
            logger.info("[author xujiahao] Netty EXC Server start success port:{}\n", localPort);
            /*
             * 这里会一直等待，直到socket被关闭
             */
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.info("[author xujiahao] Netty EXC Server start fail!\n");
        } finally {
            /**
             * 优雅关闭
             */
            worker.shutdownGracefully();
            boss.shutdownGracefully();
            logger.info("[author xujiahao] Netty EXC Server Close success!");
        }
    }
}
