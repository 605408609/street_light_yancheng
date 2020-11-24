package com.exc.street.light.sl;

import com.exc.street.light.sl.netty.shuncom.MessageRecvExecutor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan(value = {"com.exc.street.light.*.mapper"})
@ComponentScan(basePackages = {"com.exc.street.light.sl", "com.exc.street.light.security.base","com.exc.street.light.log_api"})
public class SlApplication implements CommandLineRunner {

    @Value("${shuncom.netty.port}")
    private int port;

    public static void main(String[] args) {
        SpringApplication.run(SlApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //程序启动后启动顺舟云盒Netty
        MessageRecvExecutor executor = new MessageRecvExecutor(port);
        executor.afterPropertiesSet();
    }
}
