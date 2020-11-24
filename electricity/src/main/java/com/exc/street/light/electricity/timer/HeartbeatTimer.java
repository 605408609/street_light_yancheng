package com.exc.street.light.electricity.timer;

import com.exc.street.light.electricity.service.ElectricityHeartbeatLogService;
import com.exc.street.light.electricity.util.ConstantUtil;
import com.exc.street.light.electricity.util.RedisUtil;
import com.exc.street.light.resource.entity.electricity.ElectricityHeartbeatLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 电流定时器
 *
 * @author Linshiwen
 * @date 2018/6/28
 */

@Component
@EnableAsync
public class HeartbeatTimer {
    @Autowired
    private ElectricityHeartbeatLogService heartbeatLogService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 每隔1小时将心跳包数据保存到数据库
     */
    @Async
    @Scheduled(cron = "0 35 * * * ?")
    public void getHeartbeat() {
        List<ElectricityHeartbeatLog> list = redisUtil.lGet(ConstantUtil.HEART_BEAT_KEY, 0, -1);
        redisUtil.remove(ConstantUtil.HEART_BEAT_KEY);
        if (list != null && list.size() > 0) {
            heartbeatLogService.saveBatch(list);
        }
    }
}
