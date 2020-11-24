package com.exc.street.light.sl.service;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.exc.street.light.sl.po.KafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author huangmin
 */
@Component
public class ConstructService {
    private static final Logger logger = LoggerFactory.getLogger(ConstructService.class);

    @Autowired
    private KafkaMessageService kafkaMessageService;
 
    private static ConstructService createListenerService;


    /**
     * 发送信息到消费者
     * @param msg
     */
    public static void sendMessage(String msg) {
      KafkaMessage kafkaMessage=new KafkaMessage();
      kafkaMessage.setType(1);
      kafkaMessage.setMessage(msg);
      kafkaMessage.setIs2All(2);
      kafkaMessage.setUserIds(null);
      createListenerService.kafkaMessageService.sendMessage("websocket",kafkaMessage);
    }

    
}