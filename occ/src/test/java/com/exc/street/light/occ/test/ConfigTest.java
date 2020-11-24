//package com.exc.street.light.occ.test;
//
//import com.exc.street.light.occ.config.AlarmConfig;
//import com.exc.street.light.occ.config.OccConfigs;
//import com.exc.street.light.occ.schedulers.SchedulerTask;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
////import org.junit.runner.RunWith;
//
///**
// * @author Huang Min
// * @date 2020/9/4 16:59
// * @description
// */
//@RunWith(SpringRunner.class)  //测试启动器，并加载spring boot测试注解
////标记该类为spring boot单元测试类，并加载项目的applicationContext上下文环境
//
//@SpringBootTest
//public class ConfigTest {
//
//    @Autowired
//    private OccConfigs occConfigs;
//    @Test
//    void occConfigsTest(){
//        System.out.println("occConfigs:"+occConfigs);
//    }
//
//    @Autowired
//    private AlarmConfig alarmConfig;
//    @Test
//    void alarmConfigTest(){
//        System.out.println("alarmConfig:"+alarmConfig);
//    }
//
////    @KafkaListener(topics = "#{kafkaTopicName}", groupId = "#{topicGroupId}")
//    @Value("#{kafkaTopicName}")
//    private String kafkaTopicName;
//    @Value("#{topicGroupId}")
//    private String topicGroupId;
//    @Test
//    void kafkaTest(){
//        System.out.println("***************kafkaTopicName:"+kafkaTopicName);
//        System.out.println("***************topicGroupId:"+topicGroupId);
//    }
//
//    @Autowired
//    private SchedulerTask schedulerTask;
//    @Test
//    void schedulerTest(){
//        System.out.println("***************schedulerTask:"+schedulerTask);
//
//    }
//}
