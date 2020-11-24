package com.exc.street.light.dlm.timer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exc.street.light.dlm.service.DeviceOnlineService;
import com.exc.street.light.resource.entity.dlm.DeviceOnline;
import com.exc.street.light.resource.vo.DeviceAllVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@EnableAsync
public class DeviceOnlineTimer {
    @Autowired
    private DeviceOnlineService deviceOnlineService;

    /**
     * 固定时间获取当天的最后一次上线时间数据,默认每个11点执行一次
     */
    @Async
    @Scheduled(cron = "0 0 23 * * ?")
    public void getInfoByDevice() {
        List<DeviceAllVo> devices = deviceOnlineService.deviceAll();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());
        for (DeviceAllVo vo : devices) {
            List<DeviceOnline> line = deviceOnlineService.deviceExist(vo.getId(),vo.getType(),format);
            if (line !=null && line.size() > 0) {
                continue;
            }
            DeviceOnline device = new DeviceOnline();
            device.setDeviceId(vo.getId());
            device.setDatetime(vo.getDatetime());
            device.setType(vo.getType());
            device.setLampPostId(vo.getLampPostId());
            deviceOnlineService.save(device);
        }
    }
}
