package com.exc.street.light.sl.client;

import com.exc.street.light.sl.config.MqttConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author xujiahao
 * @version 1.0
 * @date 2020/1/8 15:30
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(ApplicationRunnerImpl.class);
    @Autowired
    private MqttConfiguration mqttConfiguration;

    private static MqttPushClient mqttPushClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        while (true) {
            try {

                mqttPushClient = new MqttPushClient();
                mqttPushClient.connect(mqttConfiguration);

                String[] TopicArray = mqttConfiguration.getTopic();
                int[] QosArray = mqttConfiguration.getQos();
                if (TopicArray.length != 0) {
                    mqttPushClient.subscribe(TopicArray, QosArray);
                }
                if (MqttPushClient.getClient().isConnected()) {
                    log.info("[START MQTT] 连接服务器成功!服务器地址为:{}", mqttConfiguration.getHost());
                    break;
                } else {
                    log.info("[START MQTT] 未连接上服务器，30S之后尝试重连...");
                    Thread.sleep(30000);
                }
            } catch (Exception e) {
                log.error("[MQTT] 连接断开，重连失败！");
                continue;
            }
        }

    }

    /**
     * mqttPushClient全局变量
     * @return
     */
    public static MqttPushClient getMqttClient(){
        return mqttPushClient;
    }

    /**
     * 重新连接
     * @return
     */
    public static boolean setMqttClient(MqttPushClient mqttPushClientRcv){
        if(mqttPushClient !=null){
            mqttPushClient=null;
        }
        mqttPushClient=mqttPushClientRcv;
        return true;
    }

}
