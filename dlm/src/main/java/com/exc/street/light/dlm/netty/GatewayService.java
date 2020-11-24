package com.exc.street.light.dlm.netty;

import org.apache.commons.lang3.StringUtils;

import io.netty.channel.socket.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端数据对象
 *
 * @author Longshuangyang
 * @date 2020/04/30
 */
public class GatewayService {

    private static Map<String, SocketChannel> map = new ConcurrentHashMap<>();

    public static void addGatewayChannel(String id, SocketChannel gateway_channel) {
        map.put(id, gateway_channel);
    }

    public static Map<String, SocketChannel> getChannels() {
        return map;
    }

    public static SocketChannel getGatewayChannel(String id) {
        return map.get(id);
    }

    public static void removeGatewayChannel(String id) {
        map.remove(id);
    }

    public static void removeGatewayChannel(SocketChannel channel) {
        Map<String, SocketChannel> channels = getChannels();
        for (Map.Entry<String, SocketChannel> channelEntry : channels.entrySet()) {
            if (channel == channelEntry.getValue()) {
                map.remove(channelEntry.getKey());
                break;
            }
        }
    }

    /**
     * 给单个网关发消息
     *
     * @param id  网关地址
     * @param msg 消息
     * @return
     */
    public static boolean sendMessage(String id, byte[] msg) {
        if (StringUtils.isBlank(id)) {
            return false;
        }
        SocketChannel gatewayChannel = getGatewayChannel(id);
        if (gatewayChannel == null) {
            return false;
        }
        //发送消息
        gatewayChannel.writeAndFlush(msg);
        return true;
    }

    /**
     * 群发消息
     *
     * @param msg
     * @return
     */
    public static boolean sendAllMessage(byte[] msg) {
        Map<String, SocketChannel> channels = getChannels();
        for (Map.Entry<String, SocketChannel> channelEntry : channels.entrySet()) {
            SocketChannel channel = channelEntry.getValue();
            //发送消息
            channel.writeAndFlush(msg);
        }
        return true;
    }
}
