package com.exc.street.light.occ.service;

import com.exc.street.light.occ.po.PushMessage;

/**
 * @Author: XuJiaHao
 * @Description: 提供SocketIOService服务
 * @Date: Created in 14:00 2020/4/2
 * @Modified:
 */
public interface SocketIOService {

    //推送的事件
    String PUSH_EVENT = "push_event";

    // 启动
    void startServer() throws Exception;

    // 停止
    void stopServer() ;

    // 推送服务
    void pushMessage2Client(PushMessage pushMessage);

    // 群推
    void pushMessage2AllClient(String content);
}
