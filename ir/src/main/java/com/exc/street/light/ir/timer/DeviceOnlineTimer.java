package com.exc.street.light.ir.timer;

import com.exc.street.light.ir.service.ScreenDeviceService;
import com.exc.street.light.ir.service.impl.ScreenDeviceServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class DeviceOnlineTimer {
    private static final Logger logger = LoggerFactory.getLogger(DeviceOnlineTimer.class);
    @Autowired
    private ScreenDeviceService screenDeviceService;

    /**
     * 显示屏
     */
    @Async
    @Scheduled(cron = "0 0/5 * * * ?")
    public void getInfoByDevice() {
        screenDeviceService.refresh(null);
    }
}
