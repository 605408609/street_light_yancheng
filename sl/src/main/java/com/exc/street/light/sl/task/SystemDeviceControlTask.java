package com.exc.street.light.sl.task;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampDevice;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.vo.req.SlControlSystemDeviceVO;
import com.exc.street.light.sl.service.LampDeviceService;
import com.exc.street.light.sl.service.SystemDeviceService;
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
public class SystemDeviceControlTask implements Callable<Result> {
    private List<SlControlSystemDeviceVO> slControlSystemDeviceVOList;
    private SystemDeviceService systemDeviceService;

    public SystemDeviceControlTask(List<SlControlSystemDeviceVO> slControlSystemDeviceVOList, SystemDeviceService systemDeviceService) {
        this.slControlSystemDeviceVOList = slControlSystemDeviceVOList;
        this.systemDeviceService = systemDeviceService;
    }

    @Override
    public Result call() throws Exception {
        return systemDeviceService.singleControl(slControlSystemDeviceVOList);
    }
}
