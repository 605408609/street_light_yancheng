package com.exc.street.light.sl.timer;

import com.exc.street.light.sl.sender.HtMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 华体灯控定时器
 *
 * @Author: Xiaok
 * @Date: 2020/8/13 11:23
 */
@Component
@EnableAsync
public class HtLampTimer {
    @Autowired
    private HtMessageSender htMessageSender;

    /**
     * 定时登录
     */
    @Async
    @Scheduled(cron = "${htLamp.timer.login}")
    public void login() {
        //htMessageSender.login();
    }
}
