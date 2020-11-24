package com.exc.street.light.pb.timer;

import com.exc.street.light.pb.service.RadioDeviceService;
import com.exc.street.light.pb.service.RadioPlayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 获取雷拓广播信息定时器
 *
 * @author LeiJing
 * @date 2020/03/25
 */

@Component
@EnableAsync
public class RadioTimer {
    @Autowired
    private RadioPlayService radioPlayService;
    @Autowired
    private RadioDeviceService radioDeviceService;


    /**
     * 固定时间登录一次并获取雷拓IP广播平台身份验证JSESSIONID
     */
    @Async
    @Scheduled(cron = "${timer.login}")
    public void getInfoByDevice() {
        radioPlayService.login();
    }

    /**
     * 定时刷新设备状态
     */
    @Async
    @Scheduled(cron = "${timer.updateDeviceStatus}")
    public void updateDeviceStatus() {
        radioDeviceService.updateDeviceStatus();
    }


    /**
     * 定时刷新定时任务状态
     */
    @Async
    @Scheduled(cron = "${timer.updateTaskStatus}")
    public void updatePlayStatus(){
        radioPlayService.updatePlayStatus();
    }

}
