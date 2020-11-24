package com.exc.street.light.co.client.service;

import com.exc.street.light.co.config.MqttConfig;

import java.util.List;

/**
 * @Author tanhonghang
 * @create 2020/10/14 22:12
 */
public interface CoverMqttClientService {

    /**
     * 连接
     * @param mqttConfig
     * @return
     */
    boolean connect(MqttConfig mqttConfig);

    /**
     * 订阅该设备号的所有主题
     * @param topic
     * @param Qos
     * @return
     */
    boolean subScription(String topic, int Qos);

    /**
     * 修改周期上传时间
     * @param snCode
     * @param uploadInterval
     * @return
     */
    boolean changeUploadInterval(String snCode, int uploadInterval);

    /**
     * 修改井盖倾角阈值
     * @param snCode
     * @param angleLimit
     * @return
     */
    boolean changeDipAngelLimit(String snCode, int angleLimit);

    /**
     * 批量修改上传周期
     * @param sncodeList
     * @param uploadInterval
     * @return
     */
    boolean changeUploadIntervalBatch(List<String> sncodeList, int uploadInterval);

    /**
     * 批量修改井盖倾角阈值
     * @param sncodeList
     * @param angleLimit
     * @return
     */
    boolean changeDipAngelLimitBatch(List<String> sncodeList, int angleLimit);

    /**
     * 批量修改井盖倾角阈值和上传周期(分开发送指令厂家接收有可能会部分失败)
     * @param sncodeList
     * @param angleLimit
     * @param uploadInterval
     * @return
     */
    boolean changeAngelAndIntervalBatch(List<String> sncodeList,int angleLimit,int uploadInterval);

    /**
     *设备号订阅所有主题
     * @param snCode
     * @return
     */
    boolean snSubScription(String snCode);

    /**
     * 设备号取消订阅所有主题
     * @param snCode
     * @return
     */
    boolean snUnSubScription(String snCode);
}
