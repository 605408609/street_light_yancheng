 package com.exc.street.light.sl.config;

 import com.corundumstudio.socketio.Configuration;
 import com.corundumstudio.socketio.SocketConfig;
 import com.corundumstudio.socketio.SocketIOServer;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.context.annotation.Bean;
 import org.springframework.stereotype.Component;

 /**
  * @author huangmin
  * @date 2020/04/03
  */
 @org.springframework.context.annotation.Configuration
 @Component
 public class RegisterBean {
     @Autowired
     private SocketIOConfig socketIOConfig;
     @Autowired
     private KafkaTopicConfig kafkaTopicConfig;
     /**
     *
     * @return 注册生成Bean
     */
    @Bean
    public SocketIOServer socketIOServer() {
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        Configuration config = new Configuration();
        config.setSocketConfig(socketConfig);
        config.setHostname(socketIOConfig.getHost());
        config.setPort(socketIOConfig.getPort());
        config.setBossThreads(socketIOConfig.getBossCount());
        config.setWorkerThreads(socketIOConfig.getWorkCount());
        config.setAllowCustomRequests(socketIOConfig.isAllowCustomRequests());
        config.setUpgradeTimeout(socketIOConfig.getUpgradeTimeout());
        config.setPingTimeout(socketIOConfig.getPingTimeout());
        config.setPingInterval(socketIOConfig.getPingInterval());
        return new SocketIOServer(config);
    }

 //   @Bean
 //   public String[] topicGroupId() {
 //       return kafkaTopicConfig.getKafkaTopicName();
 //   }
    @Bean
    public String[] kafkaTopicName() {
        return kafkaTopicConfig.getKafkaTopicName();
    }

    @Bean
    public String topicGroupId() {
        return kafkaTopicConfig.getTopicGroupId();
    }
 }
