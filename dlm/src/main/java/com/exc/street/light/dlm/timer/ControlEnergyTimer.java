package com.exc.street.light.dlm.timer;

import com.exc.street.light.dlm.service.ControlEnergyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Xiezhipeng
 * @Description 定时获取集中控制器能耗类
 * @Date 2020/10/27
 */
@Component
public class ControlEnergyTimer {

    @Autowired
    private ControlEnergyService controlEnergyService;

    /**
     * 每小时整点插入或更新集控能耗数据
     */
    @Async
    @Scheduled(cron = "0 0 * * * ?")
    public void insertOrUpdateControlEnergy() {
        controlEnergyService.insertOrUpdateControlEnergy();
    }

}
