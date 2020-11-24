package com.exc.street.light.sl.service.impl;

import com.alibaba.fastjson.JSON;
import com.exc.street.light.sl.service.KafkaMessageService;
import com.exc.street.light.sl.service.SocketIOService;
import com.exc.street.light.sl.po.KafkaMessage;
import com.exc.street.light.sl.po.PushMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

/**
 * @Author: XuJiaHao
 * @Description:
 * @Date: Created in 18:22 2020/4/2
 * @Modified:
 */
@Service(value = "KafkaMessageService")
public class KafkaMessageServiceImpl implements KafkaMessageService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageServiceImpl.class);
    private KafkaTemplate<Integer,Object> kafkaTemplate;

    @Autowired
    private SocketIOService socketIOService;

    @Autowired
    public KafkaMessageServiceImpl(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    } 
    
//    @KafkaListener(topics = "${kafka.topic-group-id}", groupId = "${kafka.kafka-topic-name}")
    @KafkaListener(topics = "#{kafkaTopicName}", groupId = "#{topicGroupId}")
//    @KafkaListener(topics = "websocket", groupId = "topicGroupId")
    public void processMessage(List<ConsumerRecord<?, ?>> records) throws UnsupportedEncodingException {
        logger.info("kafka processMessage start");
        logger.info("收集的数据长度为:{}",records.size());
        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            if(kafkaMessage.isPresent()){
                Object message = kafkaMessage.get();
                KafkaMessage kafkaTest = JSON.parseObject(message.toString(), KafkaMessage.class);
                logger.info("id:{},name:{}",kafkaTest.getType(),kafkaTest.getMessage());
                logger.info("processMessage,recode={}, topic = {}, msg = {}, key = {}",record, record.topic(), record.value(),record.key());
                PushMessage pushMessage = new PushMessage();
                pushMessage.setUserId("11212");
                pushMessage.setContent(kafkaTest.getMessage());
                pushMessage.setUserToken("dwafawfwafwafwaf");
                socketIOService.pushMessage2AllClient(pushMessage.toString());
            }
        }

        logger.info("kafka processMessage end");
    }

    @Override
    public void sendMessage(String topic, KafkaMessage kafkaMessage) {
        ListenableFuture<SendResult<Integer, Object>> future = kafkaTemplate.send(topic, kafkaMessage);
        future.addCallback(new ListenableFutureCallback<SendResult<Integer, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                logger.error("kafka sendMessage error, ex = {}, topic = {}, data = {}", ex, topic, kafkaMessage);
            }

            @Override
            public void onSuccess(SendResult<Integer, Object> result) {
                logger.info("kafka sendMessage success topic = {}, data = {}",topic, kafkaMessage);
            }
        });
    }
}
