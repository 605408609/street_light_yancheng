package com.exc.street.light.sl.web;

import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.resource.utils.HexUtil;
import com.exc.street.light.sl.client.ApplicationRunnerImpl;
import com.exc.street.light.sl.client.MqttPushClient;
import com.exc.street.light.sl.sender.MqttSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @author xujiahao
 * @version 1.0
 * @date 2020/1/8 19:13
 */

@RestController
public class MqController {
    @Autowired
    private MqttSender mqttSender;

    @RequestMapping("/mqttop")
    //@RequiresPermissions(value = "sl:module")
    public String mqttop(){
        String TOPIC1="test_topic1";
        String TOPIC2="test_topic2";
        String TOPIC3="test_topic3";
        String TOPIC4="test_topic4";
        String TOPIC5="test_topic5";
        String TOPIC6="test_topic6";
        int Qos1=1;
        int Qos2=1;
        int Qos3=1;
        int Qos4=1;
        int Qos5=1;
        int Qos6=1;
        String[] topics={TOPIC1,TOPIC2,TOPIC3,TOPIC4,TOPIC5,TOPIC6};
        int[] qos={Qos1,Qos2,Qos3,Qos4,Qos5,Qos6};
        MqttPushClient mqttPushClient= ApplicationRunnerImpl.getMqttClient();
        mqttPushClient.subscribe(topics,qos);
        return "订阅主题";
    }



    @RequestMapping("/mqttest")
    public String mqtest(){
        String TOPIC1="test_topic1";
        String TOPIC2="test_topic2";
        String TOPIC3="test_topic3";
        String TOPIC4="test_topic4";
        String TOPIC5="test_topic5";
        String TOPIC6="test_topic6";
        String[] topics={TOPIC1,TOPIC2,TOPIC3,TOPIC4,TOPIC5,TOPIC6};

        mqttSender.send(topics[new Random().nextInt(5)], JSONObject.toJSONString("该KEYS链接指向用来签署产品代码签名密钥。在PGP链接下载从我们的主网站的"
                + "OpenPGP的兼容签名。的SHA-512链接下载从主站点SHA512校验和。请验证 下载文件的完整性。"));
        return "发送成功吗";
    }

    @RequestMapping("/testSend")
    public String mqtestSend(){
        String TOPIC="glc_iot_dev";
        String senMsg="02 00 E2 40 5E 16 81 D1 00 00 00 00 00 00 00 00 00 5A 5A 48 4A 30 30 31 00 30 02 01 01 01 02 02 01 01 02 03 01 01 02 04 01 01 02 05 01 01 02 06 01 01 02 07 01 00 02 08 01 00 02 09 01 00 02 0A 01 00 02 0B 01 00 02 0C 01 00";
        String arr=senMsg.replaceAll(" ","");
        System.out.println(arr);
        byte[] bytes= HexUtil.hexStringToBytes(arr);
        mqttSender.send(TOPIC,bytes);
        return  "发送成功!";
    }

}
