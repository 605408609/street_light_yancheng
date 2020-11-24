package com.exc.street.light.ed.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exc.street.light.ed.service.EdAshcanService;
import com.exc.street.light.ed.utils.RedisUtil;
import com.exc.street.light.resource.entity.ed.EdAshcan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.tools.jar.CommandLine;

import java.util.List;

/**
 * 垃圾桶定时判断设备状态
 *
 * @author wanglijun
 * @date 2020/10/20
 */
@Component
@EnableAsync
public class EdAshcanTask implements CommandLineRunner {

    @Autowired
    private EdAshcanService edAshcanService;

    /**
     * 判断设备在线离线状态
     */
    @Scheduled(cron = "0 0/5 * * * *")
    public void edAshcanTaskupdate() {
        LambdaQueryWrapper<EdAshcan> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EdAshcan::getSendId,EdAshcan::getStatus);
        List<EdAshcan> list = edAshcanService.list(wrapper);
        edAshcanService.statusUpdate(list);
    }

    @Override
    public void run(String... args) throws Exception {
        LambdaQueryWrapper<EdAshcan> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EdAshcan::getSendId,EdAshcan::getStatus);
        List<EdAshcan> list = edAshcanService.list(wrapper);
        edAshcanService.statusUpdate(list);
    }
}
