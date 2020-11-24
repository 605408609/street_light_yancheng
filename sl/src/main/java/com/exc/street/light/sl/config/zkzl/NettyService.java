package com.exc.street.light.sl.config.zkzl;

import com.exc.street.light.sl.netty.zkzl.MessageRecvExecutor;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: XuJiaHao
 * @Description:
 * @Date: Created in 15:15 2020/6/16
 * @Modified:
 */
@Component
public class NettyService {
    @Value("${zkzl.netty.port}")
    private int port;

    public ConcurrentMap<String, Object> channelMap = new ConcurrentHashMap<>();

    public ChannelGroup channelgroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @PostConstruct
    public void startNetty() {
        new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                MessageRecvExecutor messageRecvExecutor = new MessageRecvExecutor(port, channelMap, channelgroup);
                messageRecvExecutor.afterPropertiesSet();
            }
        }.start();
    }

    @PreDestroy
    public void close() {
        try {

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


}
