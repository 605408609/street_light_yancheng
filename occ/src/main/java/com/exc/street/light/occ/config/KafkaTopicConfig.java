package com.exc.street.light.occ.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @Author: XuJiaHao
 * @Description: kafka主题配置类
 * @Date: Created in 18:04 2020/4/2
 * @Modified:
 */
@Configuration
@Data
@Component
public class KafkaTopicConfig {
    @Value("${kafka.topic.group-id}")
    private String topicGroupId;

    @Value("${kafka.topic.topic-name}")
    private String[] kafkaTopicName;
}
