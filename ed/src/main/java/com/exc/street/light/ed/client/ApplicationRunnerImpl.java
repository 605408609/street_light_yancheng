package com.exc.street.light.ed.client;

import com.exc.street.light.co.client.CoverMqttClient;
import com.exc.street.light.co.config.MqttConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author tanhonghang
 * @create 2020/10/13 14:08
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {


    @Bean
    @ConfigurationProperties(prefix = "mqtt")
    public MqttConfig mqttConfig(){
        return new MqttConfig();
    }

    @Autowired
    private CoverMqttClient coverMqttClient;

    @Autowired
    private MqttConfig mqttConfig;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        coverMqttClient.connect(mqttConfig);
    }
}
