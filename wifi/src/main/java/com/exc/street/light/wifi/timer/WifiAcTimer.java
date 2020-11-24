package com.exc.street.light.wifi.timer;

import com.exc.street.light.wifi.service.WifiAcDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Xiezhipeng
 * @Description 获取ac设备数据定时类
 * @Date 2020/5/2
 */
@Component
@EnableAsync
public class WifiAcTimer {

    @Autowired
    private WifiAcDeviceService wifiAcDeviceService;

    /**
     * 固定时间获取一次ac设备在线状态
     */
    @Async
    @Scheduled(cron = "${timer.getAcStatus}")
    public void getStatusAcDevice() {
        wifiAcDeviceService.getStatusAcDevice();
    }
}
