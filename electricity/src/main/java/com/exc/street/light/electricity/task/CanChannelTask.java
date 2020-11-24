package com.exc.street.light.electricity.task;

import com.exc.street.light.electricity.service.CanChannelService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.vo.electricity.CanChannelControl;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Callable;

/**
 * 回路控制任务
 *
 * @author LinShiWen
 * @date 2017/12/8
 */
@Setter
@Getter
public class CanChannelTask implements Callable<Result> {
    private CanChannelControl canChannelControl;
    private CanChannelService canChannelService;

    public CanChannelTask(CanChannelControl canChannelControl, CanChannelService canChannelService) {
        this.canChannelControl = canChannelControl;
        this.canChannelService = canChannelService;
    }

    @Override
    public Result call() throws Exception {
        return canChannelService.getSceneControlResult(canChannelControl);
    }
}
