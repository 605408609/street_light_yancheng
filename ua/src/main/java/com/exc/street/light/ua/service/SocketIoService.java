package com.exc.street.light.ua.service;

/**
 * socketIo服务接口
 *
 * @author Linshiwen
 * @date 2018/6/22
 */
public interface SocketIoService {
    /**
     * 开启服务
     */
    void start();

    /**
     * 关闭服务
     */
    void stop();

    /**
     * 发送消息
     *
     * @param eventType 事件推送类型
     * @param message   推送消息
     */
    void sendMessage(String eventType, Object message);

}
