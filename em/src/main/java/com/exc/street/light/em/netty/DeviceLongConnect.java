package com.exc.street.light.em.netty;

import com.exc.street.light.em.config.HttpApi;
import com.exc.street.light.em.mapper.MeteorologicalHistoryDao;
import com.exc.street.light.em.mapper.MeteorologicalRealDao;
import com.exc.street.light.em.service.MeteorologicalDeviceService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.ServerSocket;

/**
 * 设备长连接服务启动类
 *
 * @Author Longshuangyang
 * @Data 2019/12/05
 */
@Component
public class DeviceLongConnect {
    @Value("${netty.port}")
    public int port;
    ServerSocket serverSocket = null;
    @Autowired
    private MeteorologicalDeviceService meteorologicalDeviceService;
    @Resource
    private MeteorologicalRealDao meteorologicalRealDao;
    @Resource
    private MeteorologicalHistoryDao meteorologicalHistoryDao;
    @Autowired
    private HttpApi httpApi;

    @PostConstruct
    public void startNetty() {
        new Thread() {
            @SneakyThrows
            @Override
            public void run() {
//                try {
//                    serverSocket = new ServerSocket(port);
//                    System.out.println("服务器启动监听" + port + ":");
//                    //通过死循环开启长连接，开启线程去处理消息
//                    while (true) {
//                        Socket socket = serverSocket.accept();
//                        new Thread(new ReceiveMsgThread(socket, iotDataService)).start();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (serverSocket != null) {
//                            serverSocket.close();
//                        }
//                    } catch (Exception e2) {
//                        e2.printStackTrace();
//                    }
//                }
                MessageRecvExecutor messageRecvExecutor = new MessageRecvExecutor(port, meteorologicalDeviceService, meteorologicalRealDao, meteorologicalHistoryDao, httpApi);
                messageRecvExecutor.afterPropertiesSet();
            }
        }.start();
    }

    @PreDestroy
    public void close() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
