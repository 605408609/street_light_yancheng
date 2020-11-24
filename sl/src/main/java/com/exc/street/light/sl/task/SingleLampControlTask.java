package com.exc.street.light.sl.task;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampDevice;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.sl.service.LampDeviceService;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 单灯控制任务
 *
 * @author Longshuangyang
 * @date 2020/03/24
 */
@Setter
@Getter
public class SingleLampControlTask implements Callable<Result> {
    private List<SingleLampParam> singleLampParamList;
    private LampDeviceService lampDeviceService;
    private LampDevice lampDevice;

    public SingleLampControlTask(List<SingleLampParam> singleLampParamList, LampDeviceService lampDeviceService, LampDevice lampDevice) {
        this.singleLampParamList = singleLampParamList;
        this.lampDeviceService = lampDeviceService;
        this.lampDevice = lampDevice;
    }

    @Override
    public Result call() throws Exception {
        return lampDeviceService.singleControl(singleLampParamList, lampDevice);
    }
}
