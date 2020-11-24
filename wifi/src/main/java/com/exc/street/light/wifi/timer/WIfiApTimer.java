package com.exc.street.light.wifi.timer;

import com.exc.street.light.wifi.service.WifiApDeviceService;
import com.exc.street.light.wifi.service.WifiApService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Xiezhipeng
 * @Description
 * @Date 2020/4/30
 */
@Component
@EnableAsync
public class WIfiApTimer {

    @Autowired
    private WifiApDeviceService wifiApDeviceService;

    @Autowired
    private WifiApService wifiApService;

    /**
     * 固定时间获取一次ap设备在线状态
     */
    @Async
    @Scheduled(cron = "${timer.getApStatus}")
    public void getStatusApDevice() {
        wifiApDeviceService.getStatusApDevice();
    }

    /**
     * 固定时间获取一次ap设备信息
     */
    @Async
    @Scheduled(cron = "${timer.getApRealInfo}")
    public void getInfoApDevice() {
        wifiApService.getInfoApDevice();
    }

    /**
     * 固定时间获取一次ap设备数据并同步到数据库
     */
    @Async
    @Scheduled(cron = "${timer.getApHistoryInfo}")
    public void getHistoryInfoApDevice() {
        wifiApService.getHistoryInfoApDevice();
    }

}
