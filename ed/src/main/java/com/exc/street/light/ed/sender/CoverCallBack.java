package com.exc.street.light.ed.sender;

import com.exc.street.light.co.client.service.CoverMqttClientService;
import com.exc.street.light.co.client.service.ResolveService;
import com.exc.street.light.co.config.MqttConfig;
import com.starwsn.protocol.core.Starwsn;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author tanhonghang
 * @create 2020/10/12 10:27
 */
@Component
public class CoverCallBack implements MqttCallback {

    private static final Logger log = LoggerFactory.getLogger(CoverCallBack.class);

    @Autowired
    private ResolveService resolveService;

    private MqttConfig mqttConfig;
    private CoverMqttClientService coverMqttClientService;

    public CoverCallBack(CoverMqttClientService client, MqttConfig mqttConfig) {
        this.coverMqttClientService = client;
        this.mqttConfig = mqttConfig;
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.info("井盖mqtt服务断开连接");
        for (int i = 0; i < 10000; i++) {
            log.info("第【" + (i + 1) + "】次尝试连接MQTT服务");
            boolean cb = coverMqttClientService.connect(mqttConfig);
            if (cb) {
                log.info("井盖mqtt服务重连成功");
                break;
            }
            try {
                if (i < 5) {
                    Thread.sleep(5000);
                } else if (i >= 5 && i < 10) {
                    Thread.sleep(10000);
                } else {
                    Thread.sleep(60000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i == 9999) {
                log.error("井盖mqtt服务重连失败");
                break;
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            String[] topics = topic.split("/");
            if ("set_para".equals(topics[2])) {
                return;
            }
            // 消息体
            byte[] payLoadArray = message.getPayload();
            String jsonStr = Starwsn.messageResolve(topic, payLoadArray);
            log.info("井盖mqtt服务回调信息：{}" ,jsonStr);
            // 数据解析
            resolveService.resolveCallBack(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
