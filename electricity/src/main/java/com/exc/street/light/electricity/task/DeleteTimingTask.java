package com.exc.street.light.electricity.task;

import com.exc.street.light.electricity.service.CanTimingService;
import com.exc.street.light.resource.core.Result;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Callable;

/**
 * 删除场景定时任务
 *
 * @author Longshuangyang
 * @date 2019/8/1
 */
@Setter
@Getter
public class DeleteTimingTask implements Callable<Result> {
    private Integer nid;
    private CanTimingService canTimingService;
    private String sceneName;
    private String parse;
    private Integer cycleType;
    private Integer type;

    public DeleteTimingTask(Integer nid, Integer cycleType, Integer type, CanTimingService canTimingService, String sceneName) {
        this.nid = nid;
        this.cycleType = cycleType;
        this.type = type;
        this.canTimingService = canTimingService;
        this.sceneName = sceneName;
    }

    @Override
    public Result call() throws Exception {
        return canTimingService.deleteTiming(nid,cycleType,type, sceneName);
    }

}
