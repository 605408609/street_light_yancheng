package com.exc.street.light.sl.service;

import com.exc.street.light.sl.po.KafkaMessage;

/**
 * @Author: XuJiaHao
 * @Description: kafka 消息通讯服务
 * @Date: Created in 18:18 2020/4/2
 * @Modified:
 */
public interface KafkaMessageService {

    void sendMessage(String topic, KafkaMessage kafkaMessage);
}
