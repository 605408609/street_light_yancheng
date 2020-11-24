package com.exc.street.light.em.timer;

import com.exc.street.light.em.service.MeteorologicalDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 获取气象信息定时器
 *
 * @author LeiJing
 * @date 2020/03/25
 */

@Component
@EnableAsync
public class MeteorologicalTimer {

    @Autowired
    private MeteorologicalDeviceService meteorologicalDeviceService;

    /**
     * 固定时间获取一次气象设备气象数据并保存气象历史数据,默认每个整点零一分执行一次
     */
    @Async
    //@Scheduled(cron = "${timer.getInfo}")
    public void getInfoByDevice() {
        meteorologicalDeviceService.getInfoByDevice();
    }

    /**
     * 固定时间获取一次气象设备在线状态并保存气象实时数据,默认三分钟
     */
    @Async
    //@Scheduled(cron = "${timer.getStatus}")
    public void getStatusByDevice() {
        meteorologicalDeviceService.getStatusByDevice();
    }
}
