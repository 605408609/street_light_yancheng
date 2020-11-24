package com.exc.street.light.ua.service.impl;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.exc.street.light.ua.service.SocketIoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Linshiwen
 * @date 2018/6/22
 */
@Service
public class SocketIoServiceImpl implements SocketIoService {


    private SocketIOServer server;

    @Value("${socket.port}")
    private int port;

    @Override
    @PostConstruct
    public void start() {
        Configuration config = new Configuration();
        config.setPort(port);
        server = new SocketIOServer(config);
        try {
            server.start();
            System.out.println("server started");
            /*Thread.sleep(Integer.MAX_VALUE);
            server.stop();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @PreDestroy
    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

    @Override
    public void sendMessage(String eventType, Object message) {
        System.out.println(eventType);
        System.out.println(message);
        server.getBroadcastOperations().sendEvent(eventType, message);
    }

}
