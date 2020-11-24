package com.exc.street.light.ed;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@MapperScan(value = {"com.exc.street.light.ed.mapper","com.exc.street.light.log_api.mapper"})
@ComponentScan(basePackages = {"com.exc.street.light.ed", "com.exc.street.light.co","com.exc.street.light.security.base","com.exc.street.light.log_api","com.exc.street.light.lj"})
public class EdApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdApplication.class, args);
    }

}
