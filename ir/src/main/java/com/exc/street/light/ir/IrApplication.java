package com.exc.street.light.ir;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@MapperScan(value = {"com.exc.street.light.*.mapper"})
//@ComponentScan(basePackages = {"com.exc.street.light.ir", "com.exc.street.light.security.base"})
@ComponentScan(basePackages = {"com.exc.street.light.ir", "com.exc.street.light.log_api", "com.exc.street.light.security.base"})
public class IrApplication {

    public static void main(String[] args) {
        SpringApplication.run(IrApplication.class, args);
    }

}
