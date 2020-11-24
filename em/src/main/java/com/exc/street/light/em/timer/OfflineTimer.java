package com.exc.street.light.em.timer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exc.street.light.em.service.MeteorologicalDeviceService;
import com.exc.street.light.em.service.MeteorologicalRealService;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.entity.em.MeteorologicalReal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Longshuangyang
 * @Description 判断气象设备离线
 * @Date 2020/5/20
 */
@Component
@EnableAsync
public class OfflineTimer {

    @Autowired
    private MeteorologicalRealService meteorologicalRealService;
    @Autowired
    private MeteorologicalDeviceService meteorologicalDeviceService;

    /**
     * 判断气象设备离线
     */
    //@Async
    @Scheduled(cron = "0 0/5 * * * ?")
    public void getStatusAcDevice() {
        // 更新设备网络状态对象
        LambdaQueryWrapper<MeteorologicalDevice> wrapper = new LambdaQueryWrapper();
        wrapper.eq(MeteorologicalDevice::getNetworkState, 1);
        List<MeteorologicalDevice> meteorologicalDeviceList = meteorologicalDeviceService.list(wrapper);
        // 当前时间
        Date date = new Date();
        // 所有设备的当前状态
        List<MeteorologicalReal> list = meteorologicalRealService.list();
        for (MeteorologicalDevice meteorologicalDevice : meteorologicalDeviceList) {
            List<MeteorologicalReal> collect = list.stream().filter(a -> meteorologicalDevice.getId().equals(a.getDeviceId())).collect(Collectors.toList());
            if (collect != null && collect.size() > 0) {
                Date createTime = collect.get(0).getCreateTime();
                // 如果当前时间大于最后一次获得数据时间小于5分钟，则为在线，其余为离线
                if (createTime != null && date.getTime() - createTime.getTime() < 1000 * 60 * 5) {
                    meteorologicalDevice.setNetworkState(1);
                } else {
                    meteorologicalDevice.setNetworkState(0);
                }
            } else {
                meteorologicalDevice.setNetworkState(0);
            }
            meteorologicalDevice.setLastOnlineTime(date);
        }
        if (meteorologicalDeviceList != null && meteorologicalDeviceList.size() > 0) {
            meteorologicalDeviceService.updateBatchById(meteorologicalDeviceList);
        }
    }
}