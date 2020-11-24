package com.exc.street.light.sl.netty.shuncom;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端数据对象
 *
 * @author Longshuangyang
 * @date 2020/04/30
 */
@Slf4j
@Component
public class GatewayService {

    private static Map<String, SocketChannel> map = new ConcurrentHashMap<>();

    public static void addGatewayChannel(String id, SocketChannel gatewayChannel) {
        map.put(id, gatewayChannel);
    }

    public static Map<String, SocketChannel> getChannels() {
        return map;
    }

    public static SocketChannel getGatewayChannel(String id) {
        return map.get(id);
    }

    public static void removeGatewayChannel(String id) {
        SocketChannel socketChannel = map.get(id);
        for (Map.Entry<String, SocketChannel> channelEntry : map.entrySet()) {
            if (channelEntry.getValue() == socketChannel) {
                map.remove(channelEntry.getKey());
            }
        }
    }

    public static boolean sendMsg(String type, String message) {
        Iterator<String> it = map.keySet().iterator();
        try {
            while (it.hasNext()) {
                String key = it.next();
                SocketChannel obj = map.get(key);
                System.out.println("channel: " + obj.isActive());
                JSONObject jsonObject = new JSONObject(true);
                jsonObject.put("type", type);
                jsonObject.put("data", message);
                String resData = jsonObject.toJSONString();
                obj.writeAndFlush(resData);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 向单个设备下发指令
     * @param mac 顺舟云盒设备mac地址
     * @param msg 需要发送的消息
     * @return 发送结果
     */
    public static boolean sendMsgToOne(String mac, byte[] msg) {
        SocketChannel channel = map.get(mac);
        if (channel == null || !channel.isActive()) {
            log.error("顺舟云盒消息发送失败,对应的连接不存在或已断开,mac={},msg={}", mac, msg);
            return false;
        }
        try {
            channel.writeAndFlush(msg);
        } catch (Exception e) {
            log.error("顺舟云盒消息发送失败,errMsg={}", e.getMessage());
        }
        return true;
    }
}
