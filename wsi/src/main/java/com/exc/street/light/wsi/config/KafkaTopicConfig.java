package com.exc.street.light.wsi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: XuJiaHao
 * @Description: kafka主题配置类
 * @Date: Created in 18:04 2020/4/2
 * @Modified:
 */
@Configuration
public class KafkaTopicConfig {
    @Value("${kafka.topic.group-id}")
    private String topicGroupId;

    @Value("${kafka.topic1.topic-name}")
    private String[] kafkaTopicName;

    @Bean
    public String[] kafkaTopicName() {
        return kafkaTopicName;
    }

    @Bean
    public String topicGroupId() {
        return topicGroupId;
    }
}
