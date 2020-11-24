package com.exc.street.light.sl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: XuJiaHao
 * @Description: socketIO相关配置类
 * @Date: Created in 13:44 2020/4/2
 * @Modified:
 */
//@org.springframework.context.annotation.Configuration
@Data
@Component
@ConfigurationProperties(prefix="socket-io")
public class SocketIOConfig {
//    @Value("${socket-io.host}")
    private String host;

//    @Value("${socket-io.port}")
    private Integer port;

//    @Value("${socket-io.boss-count}")
    private int bossCount;

//    @Value("${socket-io.work-count}")
    private int workCount;

//    @Value("${socket-io.allow-custom-requests}")
    private boolean allowCustomRequests;

//    @Value("${socket-io.upgrade-timeout}")
    private int upgradeTimeout;

//    @Value("${socket-io.ping-timeout}")
    private int pingTimeout;

//    @Value("${socket-io.ping-interval}")
    private int pingInterval;

//    /**
//     *
//     * @return 注册生成Bean
//     */
//    @Bean
//    public SocketIOServer socketIOServer() {
//        SocketConfig socketConfig = new SocketConfig();
//        socketConfig.setTcpNoDelay(true);
//        socketConfig.setSoLinger(0);
//        Configuration config = new Configuration();
//        config.setSocketConfig(socketConfig);
//        config.setHostname(host);
//        config.setPort(port);
//        config.setBossThreads(bossCount);
//        config.setWorkerThreads(workCount);
//        config.setAllowCustomRequests(allowCustomRequests);
//        config.setUpgradeTimeout(upgradeTimeout);
//        config.setPingTimeout(pingTimeout);
//        config.setPingInterval(pingInterval);
//        return new SocketIOServer(config);
//    }
}
