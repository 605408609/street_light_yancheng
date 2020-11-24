package com.exc.street.light.pb;

import com.exc.street.light.pb.service.RadioPlayService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan(value = {"com.exc.street.light.pb.mapper", "com.exc.street.light.log_api.mapper"})
@ComponentScan(basePackages = {"com.exc.street.light.pb", "com.exc.street.light.security.base", "com.exc.street.light.log_api"})
public class PbApplication implements CommandLineRunner {
    @Autowired
    private RadioPlayService radioPlayService;

    public static void main(String[] args) {
        SpringApplication.run(PbApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        radioPlayService.login();
    }
}
