package com.exc.street.light.dlm.timer;

import com.exc.street.light.dlm.utils.OnlineUtil;
import com.exc.street.light.dlm.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @Author:xujiahao
 * @Description 定时上传任务类
 * @Data:Created in 20:55 2018/8/14
 * @Modified By:
 */

@Component
@EnableAsync
public class OnlineStatusTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private OnlineUtil onlineUtil;

    /**
     * 每3分钟执行一次
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    @Async
    public void getToken() {
        HashMap<String, Double> onlineMap = (HashMap<String, Double>) redisUtil.zgetAllMap("owner:StrongOnlineSet");
        logger.info("onlineMap :{}", onlineMap);
        String onlineStr = onlineUtil.returnJson(onlineMap, 720);
        logger.info("getToken定时任务启动 发送数据{}", onlineStr);
        //向前台发送在线数据
        //socketIoService.sendMessage("online", onlineStr);
    }
}
