package com.exc.street.light.ed.service.impl;

import com.exc.street.light.ed.po.KafkaMessage;
import com.exc.street.light.ed.service.KafkaMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

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
    public KafkaMessageServiceImpl(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
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
