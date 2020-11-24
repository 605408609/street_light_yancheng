package com.exc.street.light.dlm.timer;

import com.exc.street.light.dlm.service.LocationControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Auther: Xiezhipeng
 * @Description: 集控状态定时任务
 * @Date: 2020/11/12 16:25
 */
@Component
@EnableAsync
public class LocationControlStatusTimer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LocationControlService locationControlService;

    /**
     * 定时检测集控的在离线状态
     */
    @Async
    @Scheduled(cron = "${timer.controlStatus}")
    public void getLocationControlStatus() {
        logger.info("定时检测集控的在离线状态");
        locationControlService.getLocationControlStatus();
    }
}
