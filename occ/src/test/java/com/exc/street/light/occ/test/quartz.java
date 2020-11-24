//package com.exc.street.light.occ.test;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import com.exc.street.light.occ.po.KafkaMessage;
//import com.exc.street.light.occ.service.KafkaMessageService;
//
///**
// * @Author: XuJiaHao
// * @Description:
// * @Date: Created in 14:03 2020/4/3
// * @Modified:
// */
//@Configuration
//@EnableScheduling
//public class quartz {
//    @Autowired
//    KafkaMessageService kafkaMessageService;
//
//    @Scheduled(cron = "* 0/1 * * * ?")
//    private void configureTask(){
//        KafkaMessage kafkaMessage=new KafkaMessage();
//        kafkaMessage.setType(1);
//        kafkaMessage.setMessage("{username:'111',password:'2222'}");
//        kafkaMessage.setIs2All(2);
//        kafkaMessage.setUserIds(null);
//        for (int i=0 ;i<100;++i){
//            kafkaMessageService.sendMessage("websocket",kafkaMessage);
//        }
//    }
//}
