package com.exc.street.light.sl.client;

import com.exc.street.light.sl.config.MqttConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xujiahao
 * @version 1.0
 * @date 2020/1/8 20:19
 */
@Component
public class MqttBean {
    @Autowired
    private MqttConfiguration mqttConfiguration;


//    @Bean("mqttPushClient")
//    public MqttPushClient getMqttPushClient() {
//        MqttPushClient mqttPushClient = new MqttPushClient();
//        mqttPushClient.connect(mqttConfiguration);
//
//        return mqttPushClient;
//    }
}
