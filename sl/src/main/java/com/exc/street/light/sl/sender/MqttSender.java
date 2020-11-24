package com.exc.street.light.sl.sender;

import com.exc.street.light.sl.client.MqttPushClient;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author xujiahao
 * @version 1.0
 * @date 2020/1/8 15:36
 */
@Component(value = "mqttSender")
public class MqttSender {
    private static final Logger log = LoggerFactory.getLogger(MqttSender.class);


    /**
     * @param queueName 主题名称
     * @param msg String类似数据
     */
    @Async
    public void send(String queueName, String msg) {
        publish(2,queueName, msg);
    }

    /**
     * @param queueName 主题名称
     * @param msg byte[]类型数据
     */
    @Async
    public void send(String queueName, byte[] msg) {
        publish(2,queueName, msg);
    }


    /**
     * 发布，默认qos为0，非持久化
     * @param topic
     * @param pushMessage
     */
    public void publish(String topic,String pushMessage){
        publish(1, false, topic, pushMessage);
    }

    /**
     * 发布
     * @param qos
     * @param retained
     * @param topic
     * @param pushMessage
     */
    public void publish(int qos,boolean retained,String topic,String pushMessage){
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mTopic = MqttPushClient.getClient().getTopic(topic);
        if(null == mTopic){
            log.error("MQTT topic 不存在");
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param qos
     * @param retained
     * @param topic
     * @param pushBytes
     */

    public void publish(int qos,boolean retained,String topic,byte[] pushBytes){
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushBytes);
        MqttTopic mTopic = MqttPushClient.getClient().getTopic(topic);
        if(null == mTopic){
            log.error("MQTT topic 不存在");
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布消息的服务质量(推荐为：2-确保消息到达一次。0-至多一次到达；1-至少一次到达，可能重复)，
     * retained 默认：false-非持久化（是指一条消息消费完，就会被删除；持久化，消费完，还会保存在服务器中，当新的订阅者出现，继续给新订阅者消费）
     * @param topic
     * @param pushMessage
     */
    public void publish(int qos, String topic, String pushMessage){
        publish(qos, false, topic, pushMessage);
    }

    /**
     *
     * @param qos
     * @param topic
     * @param pushBytes
     */
    public void publish(int qos, String topic, byte[] pushBytes){
        publish(qos, false, topic, pushBytes);
    }

}
