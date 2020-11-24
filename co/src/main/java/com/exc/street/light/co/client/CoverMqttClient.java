package com.exc.street.light.co.client;

import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.co.client.service.CoverMqttClientService;
import com.exc.street.light.co.client.service.ResolveService;
import com.exc.street.light.co.config.MqttConfig;
import com.exc.street.light.co.utils.Constants;
import com.starwsn.protocol.common.CmdCode;
import com.starwsn.protocol.core.Starwsn;
import com.starwsn.protocol.dto.CommandInfoDto;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author tanhonghang
 * @create 2020/10/12 9:55
 */
@Component
public class CoverMqttClient implements CoverMqttClientService {


    private static final Logger log = LoggerFactory.getLogger(CoverMqttClient.class);

    @Autowired
    private static MqttClient client;
    @Autowired
    private MqttCallback mqttCallback;
    @Autowired
    private ResolveService resolveService;

    private MqttConnectOptions getOption(String userName, String password, int outTime, int KeepAlive) {
        //MQTT连接设置
        MqttConnectOptions option = new MqttConnectOptions();
        //设置是否清空session,false表示服务器会保留客户端的连接记录，true表示每次连接到服务器都以新的身份连接
        option.setCleanSession(false);
        //设置连接的用户名
        option.setUserName(userName);
        //设置连接的密码
        option.setPassword(password.toCharArray());
        //设置超时时间 单位为秒
        option.setConnectionTimeout(outTime);
        //设置会话心跳时间 单位为秒 服务器会每隔(1.5*keepTime)秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        option.setKeepAliveInterval(KeepAlive);
        option.setMaxInflight(1000);

        return option;
    }


    @Override
    public boolean connect(MqttConfig mqttConfig) {
        try {
            client = new MqttClient(mqttConfig.getHost(), mqttConfig.getClientId(), new MemoryPersistence());
            MqttConnectOptions options = getOption(mqttConfig.getUsername(), mqttConfig.getPassword(),
                    mqttConfig.getTimeout(), mqttConfig.getKeepAlive());
            options.setCleanSession(true);
            try {
                client.setCallback(mqttCallback);
                if (!client.isConnected()) {
                    client.connect(options);
                    log.info("井盖MQTT连接成功");
                    // 订阅所有主题
                    resolveService.subScriptionAll();
                    return true;
                } else {//这里的逻辑是如果连接成功就重新连接
                    client.disconnect();
                    client.connect(options);
                    log.info("井盖MQTT断连成功");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 断线重连
     *
     * @throws Exception
     */
    public Boolean reConnect() throws Exception {
        Boolean isConnected = false;
        if (null != client) {
            client.connect();
            if (client.isConnected()) {
                isConnected = true;
            }
        }
        return isConnected;
    }

    /**
     * 取消主题订阅
     *
     * @param topics 主题名称
     * @return
     */
    public boolean unSubScription(String[] topics) {
        String topic = "";
        try {
            for (int i = 0; i < topics.length; i++) {
                topic = topics[i];
                client.unsubscribe(topic);
                log.info("mqtt主题{" + topic + "}取消订阅成功！！！");
            }
            return true;
        } catch (Exception e) {
            log.error("mqtt主题{" + topic + "}取消订阅失败！！！");
            return false;
        }
    }

    /**
     * 取消主题订阅
     *
     * @param topic 主题名称
     * @return
     */
    public boolean unSubScription(String topic) {
        try {
            client.unsubscribe(topic);
            log.info("mqtt主题{" + topic + "}取消订阅成功！！！");
            return true;
        } catch (Exception e) {
            log.error("mqtt主题{" + topic + "}取消订阅失败！！！");
            return false;
        }
    }

    /**
     * 推送消息
     *
     * @param topicName 主题名称
     * @param payload   消息内容
     * @return
     */
    public boolean publish(String topicName, byte[] payload) {
        try {
            MqttTopic topic = client.getTopic(topicName);
            MqttMessage message = new MqttMessage();
            message.setQos(1);
            message.setRetained(false);
            message.setPayload(payload);
            MqttDeliveryToken token = topic.publish(message);
            token.waitForCompletion();
            log.info("mqtt消息发送结果：" + token.isComplete());
            return token.isComplete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 主题订阅
     *
     * @param topic 主题名称
     * @param Qos   服务质量
     * @return
     */
    @Override
    public boolean subScription(String topic, int Qos) {
        try {
            int[] _Qos = {Qos};
            String[] _topic = {topic};
            client.subscribe(_topic, _Qos);
            log.info("mqtt主题{" + topic + "}订阅成功！！！");
            return true;
        } catch (Exception e) {
            log.error("mqtt主题{" + topic + "}订阅失败！！！");
            return false;
        }
    }

    /**
     * 主题订阅
     *
     * @param topics 主题名称数组
     * @param Qos    服务质量数组（与主题名称一一匹配）
     * @return
     */
    public boolean subScription(String[] topics, int[] Qos) {
        try {
            client.subscribe(topics, Qos);
            String loginfo = "mqtt主题{";
            for (int i = 0; i < topics.length; i++) {
                loginfo += topics[i];
                if (i != topics.length - 1){
                    loginfo += ",";
                }
            }
            loginfo += "}订阅成功！！！";
            log.info(loginfo);
            return true;
        } catch (Exception e) {
            String loginfo = "mqtt主题{";
            for (int i = 0; i < topics.length; i++) {
                loginfo += topics[i];
                if (i != topics.length - 2){
                    loginfo += ",";
                }
            }
            loginfo += "}订阅失败！！！";
            log.error(loginfo);
            return false;
        }
    }


    /**
     * 主题订阅
     *
     * @param snCodes 设备号数组
     * @return
     */
    public boolean subScription(String[] snCodes){
        try {
            String[] topics = new String[snCodes.length];
            for (int i = 0; i < snCodes.length; i++) {
                topics[i] = subScriptionAppend(snCodes[i]);
            }
            client.subscribe(topics);
            log.info("设备【{}】所有主题订阅成功",snCodes);
            return true;
        } catch (Exception e) {
            log.info("设备【{}】所有主题订阅失败",snCodes);
            return false;
        }
    }


    /**
     * 拼接订阅主题
     *
     * @param sn 设备号
     * @return
     */
    public String subScriptionAppend(String sn) {
        StringBuilder builder = new StringBuilder();
        builder.append("ND/").append(sn).append("/#");
        return builder.toString();
    }



    @Override
    public boolean changeUploadInterval(String snCode, int uploadInterval) {
        CommandInfoDto dto = new CommandInfoDto();
        dto.setApiKey(Constants.APIKEY);
        dto.addSncode(snCode);
        dto.addCommandInfo(CmdCode.UPLOAD_INTERVAL, String.valueOf(uploadInterval));
        String result = Starwsn.postDeviceCommand(dto);
        log.info("井盖设备【{}】修改上传周期为【{}】,返回参数:{}",snCode,uploadInterval,result);
        Map map = JSONObject.parseObject(result, Map.class);
        return "success".equals(map.get("status"));
    }


    @Override
    public boolean changeDipAngelLimit(String snCode, int angleLimit) {
        CommandInfoDto dto = new CommandInfoDto();
        dto.setApiKey(Constants.APIKEY);
        dto.addSncode(snCode);
        dto.addCommandInfo(CmdCode.DIP_ANGLE_LIMIT, String.valueOf(angleLimit));
        String result = Starwsn.postDeviceCommand(dto);
        log.info("井盖设备【{}】修改倾角阈值为【{}】,返回参数:{}",snCode,angleLimit,result);
        Map map = JSONObject.parseObject(result, Map.class);
        return "success".equals(map.get("status"));
    }


    @Override
    public boolean changeUploadIntervalBatch(List<String> sncodeList, int uploadInterval) {
        CommandInfoDto dto = new CommandInfoDto();
        dto.setApiKey(Constants.APIKEY);
        if (sncodeList.size() == 0) {
            return false;
        }
        for (String sncode : sncodeList) {
            dto.addSncode(sncode);
        }
        dto.addCommandInfo(CmdCode.UPLOAD_INTERVAL, String.valueOf(uploadInterval));
        String result = Starwsn.postDeviceCommand(dto);
        log.info("井盖设备【{}】修改上传周期为【{}】,返回参数:{}",sncodeList,uploadInterval,result);
        Map map = JSONObject.parseObject(result, Map.class);
        return "success".equals(map.get("status"));
    }


    @Override
    public boolean changeDipAngelLimitBatch(List<String> sncodeList, int angleLimit) {
        CommandInfoDto dto = new CommandInfoDto();
        dto.setApiKey(Constants.APIKEY);
        if (sncodeList.size() == 0) {
            return false;
        }
        for (String sncode : sncodeList) {
            dto.addSncode(sncode);
        }
        dto.addCommandInfo(CmdCode.DIP_ANGLE_LIMIT, String.valueOf(angleLimit));
        String result = Starwsn.postDeviceCommand(dto);
        log.info("井盖设备【{}】修改倾角阈值为【{}】,返回参数:{}",sncodeList,angleLimit,result);
        Map map = JSONObject.parseObject(result, Map.class);
        return "success".equals(map.get("status"));
    }

    @Override
    public boolean changeAngelAndIntervalBatch(List<String> sncodeList, int angleLimit, int uploadInterval) {
        CommandInfoDto dto = new CommandInfoDto();
        dto.setApiKey(Constants.APIKEY);
        if (sncodeList.size() == 0) {
            return false;
        }
        for (String sncode : sncodeList) {
            dto.addSncode(sncode);
        }
        dto.addCommandInfo(CmdCode.DIP_ANGLE_LIMIT, String.valueOf(angleLimit));
        dto.addCommandInfo(CmdCode.UPLOAD_INTERVAL,String.valueOf(uploadInterval));
        String result = Starwsn.postDeviceCommand(dto);
        log.info("井盖设备【{}】修改倾角阈值为【{}】,上传周期为【{}】,返回参数:{}",sncodeList,angleLimit,uploadInterval,result);
        Map map = JSONObject.parseObject(result, Map.class);
        return "success".equals(map.get("status"));
    }


    @Override
    public boolean snUnSubScription(String snCode) {
        String topic = subScriptionAppend(snCode);
        return unSubScription(topic);
    }


    @Override
    public boolean snSubScription(String snCode) {
        String topic = subScriptionAppend(snCode);
        return subScription(topic,1);
    }

}
