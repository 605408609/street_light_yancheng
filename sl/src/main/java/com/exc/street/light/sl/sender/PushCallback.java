package com.exc.street.light.sl.sender;

import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.utils.HexUtil;
import com.exc.street.light.resource.vo.SingleLampParamTempVO;
import com.exc.street.light.sl.client.ApplicationRunnerImpl;
import com.exc.street.light.sl.client.MqttPushClient;
import com.exc.street.light.sl.config.MqttConfiguration;
import com.exc.street.light.sl.service.SingleLampParamService;
import com.exc.street.light.sl.service.SystemDeviceService;
import com.exc.street.light.sl.utils.MessageOperationUtil;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xujiahao
 * @version 1.0
 * @date 2020/1/8 15:38
 */
public class PushCallback implements MqttCallback {

    @Autowired
    SingleLampParamService singleLampParamService;

    // private RedisTemplate<String, Object> redisDao;

    private static final Logger log = LoggerFactory.getLogger(PushCallback.class);
    private MqttPushClient client;

    // private MqttPtsService mqttStpService;
    //    public PushCallback() {
    //   }
    private MqttConfiguration mqttConfiguration;

    public PushCallback(MqttPushClient client, MqttConfiguration mqttConfiguration) {
        this.client = client;
        this.mqttConfiguration = mqttConfiguration;
    }
  /*  public PushCallback(MqttPushClient client,RedisTemplate<String, Object> redisUtils,MqttPtsService mqttStpService) {
        this.client = client;
        this.redisDao = redisUtils;
        this.mqttStpService=mqttStpService;
    }*/
    @Autowired
  SystemDeviceService systemDeviceService;

    @Override
    public void connectionLost(Throwable cause) {
        /** 连接丢失后，一般在这里面进行重连 **/
        if (client != null) {
            while (true) {
                try {
                    log.info("[MQTT] 连接断开，30S之后尝试重连...");
                    Thread.sleep(30000);
                    if (MqttPushClient.getClient().isConnected() == true) {
                        log.info("[MQTT] 重连成功！");
                        break;
                    }else{
                        MqttPushClient mqttPushClient = new MqttPushClient();
                        mqttPushClient.connect(mqttConfiguration);
                        ApplicationRunnerImpl.setMqttClient(mqttPushClient);
                    }
                }catch (Exception e) {
                    log.error("[MQTT] 连接断开，重连失败！");
                    continue;
                }
            }
        }
        //log.info(cause.getMessage());
    }


    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //publish后会执行到这里
        log.info("pushComplete---------" + token.isComplete());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        log.info("接收消息主题 : " + topic);
        log.info("接收消息Qos : " + message.getQos());
        log.info("接收消息内容 : " + new String(message.getPayload()));
        log.info("接收消息内容bytes : " + HexUtil.bytesTohex(message.getPayload()));
        log.info("接受消息的来源: "+message.getId());

        /*FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("C:/Users/Gigabyte/Desktop/上报信息.txt",true);
            fileOutputStream.write(message.getPayload());
            String newLine = System.getProperty("line.separator");
            fileOutputStream.write(newLine.getBytes());
        }catch (IOException e){
            log.info("上报信息写出出错");
            return;
        }finally {
            if(fileOutputStream != null){
                fileOutputStream.close();
            }
        }*/

        try {
            Integer topicNum = 0;
            String[] topics = mqttConfiguration.getTopic();
            for (int i = 0; i<topics.length; i++){
                String updateTopic = topics[i];
                if(updateTopic.equals(topic)){
                    topicNum = i;
                }
            }
            List<SingleLampParamTempVO> singleLampParamList = new ArrayList<>();
            if(topicNum==0){
                singleLampParamList = MessageOperationUtil.nbGetInformation(new String(message.getPayload()));
            }else {
                singleLampParamList = MessageOperationUtil.catOneGetInformation(new String(message.getPayload()));
            }
            Integer deviceTypeId = 0;
            //解析报文
            if(singleLampParamList != null && singleLampParamList.size()>0){
                if(topicNum==0){
                    List<Integer> deviceTypeIdList = new ArrayList<>();
                    deviceTypeIdList.add(1);
                    deviceTypeIdList.add(2);
                    Integer selectCountByNum = systemDeviceService.selectCountByNum(singleLampParamList.get(0).getDeviceNum(), deviceTypeIdList);
                    if(selectCountByNum==1){
                        deviceTypeId = 1;
                    }else if(selectCountByNum==2){
                        deviceTypeId = 2;
                    }else {
                        return;
                    }
                }else {
                    List<Integer> deviceTypeIdList = new ArrayList<>();
                    deviceTypeIdList.add(5);
                    deviceTypeIdList.add(6);
                    Integer selectCountByNum = systemDeviceService.selectCountByNum(singleLampParamList.get(0).getDeviceNum(), deviceTypeIdList);
                    if(selectCountByNum==1){
                        deviceTypeId = 5;
                    }else if(selectCountByNum==2){
                        deviceTypeId = 6;
                    }else {
                        return;
                    }
                }
                for (int i = 0; i<singleLampParamList.size(); i++){
                    //处理报文
                    SingleLampParamTempVO singleLampParam = singleLampParamList.get(i);
                    if(i%2==0){
                        singleLampParam.setLoopNum(1);
                    }else {
                        singleLampParam.setLoopNum(2);
                    }
                    MessageOperationUtil.remind(singleLampParam,deviceTypeId);
                }
            }
        }catch (Exception e){
            log.info("处理接收报文出错"+e);
            e.printStackTrace();
        }
        try {
            //            JSONObject jsonObject = JSON.parseObject(new String(message.getPayload(), "UTF-8"));
            //            MqttDataEntity mqttDataEntity=(MqttDataEntity)JSONObject.toJavaObject(jsonObject, MqttDataEntity.class);
            //            getMqttDataEntity(jsonObject);
            //            mqttStpService.handleReceiveMsg(mqttDataEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
