package com.exc.street.light.em.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.socket.SocketChannel;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端数据对象
 *
 * @Author Longshuangyang
 * @Data 2020/04/30
 */
public class GatewayService {

    private static Map<String, SocketChannel> map = new ConcurrentHashMap<>();

    public static void addGatewayChannel(String id, SocketChannel gateway_channel){
        map.put(id, gateway_channel);
    }

    public static Map<String, SocketChannel> getChannels(){
        return map;
    }

    public static SocketChannel getGatewayChannel(String id){
        return map.get(id);
    }

    public static void removeGatewayChannel(String id){
        map.remove(id);
    }

    public static boolean sendMsg(String type,String message){
        Iterator<String> it = map.keySet().iterator();
        try {
            while (it.hasNext()){
                String key = it.next();
                SocketChannel obj = map.get(key);
                System.out.println("channel: " + obj.isActive());
                JSONObject jsonObject = new JSONObject(true);
                jsonObject.put("type",type);
                jsonObject.put("data",message);
                String resData = jsonObject.toJSONString();
                obj.writeAndFlush(resData);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
