package com.exc.street.light.sl.task;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampDevice;
import com.exc.street.light.sl.service.LampDeviceService;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Callable;

/**
 * 单灯控制任务
 *
 * @author Longshuangyang
 * @date 2020/03/24
 */
@Setter
@Getter
public class SingleControlTask implements Callable<Result> {
    private LampDeviceService lampDeviceService;
    private LampDevice lampDevice;

    public SingleControlTask(LampDeviceService lampDeviceService, LampDevice lampDevice) {
        this.lampDeviceService = lampDeviceService;
        this.lampDevice = lampDevice;
    }

    @Override
    public Result call() throws Exception {
        return lampDeviceService.singleControl(lampDevice);
    }
}
