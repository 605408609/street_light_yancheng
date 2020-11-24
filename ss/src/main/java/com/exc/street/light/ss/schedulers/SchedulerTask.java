package com.exc.street.light.ss.schedulers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.exc.street.light.resource.entity.ss.SsDevice;
import com.exc.street.light.resource.utils.ConstantUtil;
import com.exc.street.light.resource.utils.TestingOnlineUtil;
import com.exc.street.light.ss.mapper.SsDeviceDao;
import com.exc.street.light.ss.service.SsDeviceService;
@Component
public class SchedulerTask {
	@Autowired
	private SsDeviceDao ssDeviceDao;
	@Autowired
	private SsDeviceService ssDeviceService;
    /**
     * 定时检测设备是否在线
     */
    @Async
//    @Scheduled(cron = "${cycle-execution.device-online}")
    //每3分钟执行一次检测在线
    @Scheduled(cron = "0 0/3 * * * ?")
    protected void regularlyOnline() {
        List<SsDevice> listAhDevice = ssDeviceDao.selectList(null);
        
        for (SsDevice ahDevice : listAhDevice) {
            if (TestingOnlineUtil.getOnline(ahDevice.getIp())) {
                ahDevice.setNetworkState(ConstantUtil.DEVICE_ONLINE);
            }else {
                ahDevice.setNetworkState(ConstantUtil.DEVICE_OFFLINE);
            }
        }
        ssDeviceService.updateBatchById(listAhDevice);
        
    }
}
