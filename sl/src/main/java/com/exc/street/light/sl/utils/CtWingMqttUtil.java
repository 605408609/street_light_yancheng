package com.exc.street.light.sl.utils;

import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.sl.config.parameter.CtWingMqttApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ctwing 操作mqtt类型设备工具类
 *
 * @Author: Xiaok
 * @Date: 2020/9/15 10:42
 */
@Slf4j
@Component
public class CtWingMqttUtil {

    private static CtWingMqttApi ctWingMqttApi;

    /**
     * 操作人
     */
    private static String operator = "feiyue1";

    @Autowired
    public void ctWingMqttApi(CtWingMqttApi ctWingMqttApi) {
        CtWingMqttUtil.ctWingMqttApi = ctWingMqttApi;
    }

    /**
     * 通过ctwing平台获取时间偏移量
     *
     * @return 时间偏移量
     */
    public static long getTimeOffset() {
        return CTWingApi.getTimeOffset();
    }

    /**
     * 签名
     *
     * @param param       构造签名需要的参数
     * @param timestamp   时间戳
     * @param application App Key
     * @param secret      App secret
     * @param body        消息体
     * @return 签名String
     * @throws Exception 异常
     */
    public static String sign(Map<String, String> param, long timestamp, String application, String secret, byte[] body) throws Exception {
        return CTWingApi.sign(param, timestamp, application, secret, body);
    }

    /**
     * 封装请求头map
     *
     * @param version 请求版本号
     * @param bodyStr 请求body
     * @return 请求头map
     * @throws Exception 异常
     */
    private static Map<String, String> getHeaderMap(String version, String bodyStr, Map<String, String> joinParam) throws Exception {
        // 获取时间戳
        long timestamp = System.currentTimeMillis() + getTimeOffset();
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        // 生成格式化的日期字符串
        String dateString = dateFormat.format(date);
        Map<String, String> headMap = new HashMap<>(7);
        // head中添加公共参数
        // MasterKey加在此处head中
        headMap.put("MasterKey", ctWingMqttApi.getMasterKey());
        headMap.put("application", ctWingMqttApi.getApplication());
        headMap.put("timestamp", String.valueOf(timestamp));
        headMap.put("version", version);
        headMap.put("Content-Type", "application/json;charset=utf-8");
        headMap.put("Date", dateString);

        // 构造签名需要的参数,如果参数中有MasterKey，则添加来参与签名计算,
        // 其他参数根据实际API从URL中获取,如有其他参数,写法参考get示例
        Map<String, String> param = new HashMap<>();
        param.put("MasterKey", ctWingMqttApi.getMasterKey());
        if (joinParam != null && !joinParam.isEmpty()) {
            param.putAll(joinParam);
        }
        // 添加签名
        headMap.put("signature", sign(param, timestamp, ctWingMqttApi.getApplication(), ctWingMqttApi.getSecret(), StringUtils.isBlank(bodyStr) ? null : bodyStr.getBytes()));
        return headMap;
    }

    /**
     * 消息下发
     *
     * @param deviceId 设备ID
     * @param message  下发指令str
     * @return 下发结果
     * @throws Exception 异常
     */
    public static boolean sendMessage(String deviceId, String message) throws Exception {
        if (StringUtils.isAnyBlank(deviceId, message)) {
            return false;
        }
        //指令参数
        JSONObject paramsObj = new JSONObject(true);
        paramsObj.put("data", message);
        //封装content对象
        JSONObject contentObj = new JSONObject(true);
        //服务定义时的服务标识
        contentObj.put("serviceIdentifier", "DownLink");
        //指令参数
        contentObj.put("params", paramsObj);

        JSONObject sendObj = new JSONObject(true);
        //指令内容，必填，格式为Json。
        sendObj.put("content", contentObj);
        //设备ID，（当指令级别为设备级时必填，为设备组级时则不填）
        sendObj.put("deviceId", deviceId);
        //操作者，必填
        sendObj.put("operator", operator);
        //产品ID，必填
        sendObj.put("productId", ctWingMqttApi.getProductId());
        //设备离线时指令缓存时长，选填。单位：秒，取值范围：0-864000。
        sendObj.put("ttl", Integer.parseInt(ctWingMqttApi.getOutTime()));
        //设备组ID，选填，当指令级别为设备级，deviceId不为空，deviceGroupId为空；当指令级别为设备组级，deviceId为空，deviceGroupId不为空。
        sendObj.put("deviceGroupId", "");
        //指令级别，1或2为设备级别,3为设备组级别，选填。不填默认设备级。
        sendObj.put("level", 1);

        //获取headerMap
        Map<String, String> headerMap = getHeaderMap(ctWingMqttApi.getSendMessage().get("api-version"), sendObj.toString(), null);
        //发送请求
        String resultStr = HttpUtil.post(ctWingMqttApi.getSendMessage().get("http-url"), sendObj.toString(), headerMap);
        log.info("CtWingMqtt-下发指令返回消息:{}", resultStr);
        JSONObject resultObj = JSONObject.parseObject(resultStr);
        if (resultObj == null || resultObj.getInteger("code") == null) {
            log.error("CtWingMqtt-下发指令失败,resultJson = {}", resultObj);
            return false;
        }
        return resultObj.getInteger("code").equals(0);
    }

    /**
     * 创建设备
     *
     * @param deviceSn   设备编号，非空
     * @param deviceName 设备名称，非空
     * @return 是否成功；设备id
     * @throws Exception 异常
     */
    public static Result<String> createDevice(String deviceSn, String deviceName) throws Exception {
        Result<String> result = new Result<>();
        if (StringUtils.isAnyBlank(deviceName, deviceSn)) {
            return result.error("设备编号和设备名称不可为空");
        }
        //下发的命令json
        JSONObject sendObj = new JSONObject(true);
        //设备名称，必填
        sendObj.put("deviceName", deviceName);
        //设备编号，MQTT,T_Link,TCP,HTTP,JT/T808，南向云协议必填
        sendObj.put("deviceSn", deviceSn);
        //操作者，必填
        sendObj.put("operator", operator);
        //产品ID，必填
        sendObj.put("productId", Integer.parseInt(ctWingMqttApi.getProductId()));

        //获取headerMap
        Map<String, String> headerMap = getHeaderMap(ctWingMqttApi.getCreateDevice().get("api-version"), sendObj.toString(), null);
        //发送请求
        String resultStr = HttpUtil.post(ctWingMqttApi.getCreateDevice().get("http-url"), sendObj.toString(), headerMap);
        log.info("CtWingMqtt-创建设备返回消息:{}", resultStr);
        JSONObject resultObj = JSONObject.parseObject(resultStr);
        if (resultObj == null || resultObj.getInteger("code") == null || resultObj.getJSONObject("result") == null) {
            log.error("CtWingMqtt-创建设备失败,数据存在空值,returnJson = {}", resultObj);
            return result.error("创建设备失败");
        }
        if (!resultObj.getInteger("code").equals(0)) {
            log.error("CtWingMqtt-创建设备失败,出现错误,returnJson = {}", resultObj);
            return result.error("创建设备失败");
        }
        JSONObject resResultObj = resultObj.getJSONObject("result");
        String deviceId = resResultObj.getString("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            log.error("CtWingMqtt-创建设备失败,设备id为空,returnJson = {}", resultObj);
            return result.error("创建设备失败");
        }
        return result.success("创建设备成功", deviceId);
    }

    /**
     * 设备批量删除
     *
     * @param deviceIds 可以删除多个设备（最多支持200个设备）。多个设备id，中间以逗号 "," 隔开
     * @return 是否成功
     */
    public static boolean deleteDevice(String deviceIds) throws Exception {
        if (StringUtils.isBlank(deviceIds)) {
            log.error("CtWingMqtt-删除设备失败,设备ID为空");
            return false;
        }
        deviceIds = Stream.of(deviceIds.split(",")).filter(StringUtils::isNotBlank).distinct().collect(Collectors.joining(","));
        if (StringUtils.isBlank(deviceIds)) {
            log.error("CtWingMqtt-删除设备失败,设备ID为空");
            return false;
        }
        //封装删除设备命令json
        Map<String, String> paramMap = new HashMap<>(2);
        paramMap.put("productId", ctWingMqttApi.getProductId());
        paramMap.put("deviceIds", deviceIds);

        //获取请求头
        Map<String, String> headerMap = getHeaderMap(ctWingMqttApi.getDeleteDevice().get("api-version"), null, paramMap);
        //拼接请求url
        String httpUrl = ctWingMqttApi.getDeleteDevice().get("http-url") + "?productId=" + ctWingMqttApi.getProductId() + "&deviceIds=" + deviceIds;
        String resultStr = HttpUtil.delete(httpUrl, null, headerMap);
        //返回结果
        JSONObject resultObj = JSONObject.parseObject(resultStr);
        if (resultObj == null || resultObj.getInteger("code") == null) {
            log.error("CtWingMqtt-删除设备失败,数据存在空值,returnJson = {}", resultObj);
            return false;
        }
        if (!resultObj.getInteger("code").equals(0)) {
            log.error("CtWingMqtt-删除设备失败,returnJson = {}", resultObj);
            return false;
        }
        return true;
    }
}
