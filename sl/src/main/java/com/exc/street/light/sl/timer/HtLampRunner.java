package com.exc.street.light.sl.timer;

import com.exc.street.light.sl.sender.HtMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author: Xiaok
 * @Date: 2020/8/13 14:24
 */
@Component
@Order(1)
public class HtLampRunner implements CommandLineRunner {
    @Autowired
    private HtMessageSender htMessageSender;

    @Override
    public void run(String... args) throws Exception {
        //htMessageSender.login();
    }
}
