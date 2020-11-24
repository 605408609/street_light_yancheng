package com.exc.street.light.electricity.netty;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author:xujiahao
 * @Description
 * @Data:Created in 19:14 2018/6/28
 * @Modified By:
 */
@Slf4j
@Component
public class NettyStart implements CommandLineRunner {
    @Value("${netty.port}")
    public int port;

    @Override
    public void run(String... args) throws Exception {
        new Thread(){
            @SneakyThrows
            @Override
            public void run(){
                MessageRecvExecutor mg = new MessageRecvExecutor(port);
                mg.afterPropertiesSet();
            }
        }.start();
    }
}
