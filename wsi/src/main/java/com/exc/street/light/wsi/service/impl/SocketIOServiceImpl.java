package com.exc.street.light.wsi.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.exc.street.light.wsi.po.PushMessage;
import com.exc.street.light.wsi.service.SocketIOService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: XuJiaHao
 * @Description: SocketIOService实现类
 * @Date: Created in 16:26 2020/4/2
 * @Modified:
 */
@Service(value = "socketIOService")
public class SocketIOServiceImpl implements SocketIOService {
    private static final Logger logger = LoggerFactory.getLogger(SocketIOServiceImpl.class);

    //客户端集合
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    @Autowired
    private SocketIOServer socketIOServer;

    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     * @throws Exception
     */
    @PostConstruct
    private void autoStartup() throws Exception {
        startServer();
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,解决端口占用问题
     * @throws Exception
     */
    @PreDestroy
    private void autoStop() {
        stopServer();
    }

    @Override
    public void startServer() throws Exception {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            String userToken = getParamsByClient(client,"userToken");
            logger.info("新增连接,连接IP{},",client.getRemoteAddress());
            if (userToken != null) {
                clientMap.put(userToken, client);
            }
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            String userToken = getParamsByClient(client,"userToken");
            if (userToken != null) {
                clientMap.remove(userToken);
                client.disconnect();
            }
        });

        // 处理自定义的事件，与连接监听类似
        socketIOServer.addEventListener(PUSH_EVENT, PushMessage.class, (client, data, ackSender) -> {

        });
        socketIOServer.start();
    }

    @Override
    public void stopServer() {
        if (socketIOServer != null){
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    /**
     * 单个客户端发送信息
     *
     * @param pushMessage
     */
    @Override
    public void pushMessage2Client(PushMessage pushMessage) {
        String userToken = pushMessage.getUserToken();
        if (StringUtils.isNotBlank(userToken)) {
            SocketIOClient client = clientMap.get(userToken);
            if (client != null)
                client.sendEvent(PUSH_EVENT, pushMessage);
        }
    }

    /**
     * 群发消息
     *
     * @param content
     */

    @Override
    public void pushMessage2AllClient(String content) {
        Collection<SocketIOClient> socketIOClients = socketIOServer.getAllClients();
        logger.info("群发消息{}",content);
        if(socketIOClients.size() != 0){
            for (SocketIOClient cli : socketIOClients) {
                if(cli != null){
                    cli.sendEvent(PUSH_EVENT, content);
                }
            }
        }
    }

    /**
     * 获取参数
     *
     * @param client
     * @param param
     * @return
     */
    private String getParamsByClient(SocketIOClient client,String param) {
        // 从请求的连接中拿出参数（这里的loginUserNum必须是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get(param);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
