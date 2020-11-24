package com.exc.street.light.em;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@MapperScan(value = {"com.exc.street.light.em.mapper","com.exc.street.light.log_api.mapper"})
@ComponentScan(basePackages = {"com.exc.street.light.em", "com.exc.street.light.security.base","com.exc.street.light.log_api"})
public class EmApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmApplication.class, args);
    }

}
