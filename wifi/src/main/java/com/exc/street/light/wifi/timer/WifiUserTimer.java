package com.exc.street.light.wifi.timer;

import com.exc.street.light.wifi.service.WifiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Xiezhipeng
 * @Description 定时获取wifi用户信息并将其插入数据库表
 * @Date 2020/4/30
 */

@Component
@EnableAsync
public class WifiUserTimer {

    @Autowired
    private WifiUserService wifiUserService;

    /**
     * 固定时间获取一次用户信息
     */
    @Async
    @Scheduled(cron = "${timer.getWifiUserInfo}")
    public void getWifiUserInfo() {
        wifiUserService.getWifiUserInfo();
    }
}
