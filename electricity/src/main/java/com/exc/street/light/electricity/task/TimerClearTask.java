package com.exc.street.light.electricity.task;

import com.exc.street.light.electricity.service.CanTimingService;
import com.exc.street.light.resource.core.Result;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;

/**
 * 定时清空任务
 *
 * @author LinShiWen
 * @date 2017/12/8
 */
@Setter
@Getter
public class TimerClearTask implements Callable<Result> {
    private Integer nid;
    private CanTimingService canTimingService;
    private HttpServletRequest request;

    public TimerClearTask(Integer nid, CanTimingService canTimingService, HttpServletRequest request) {
        this.nid = nid;
        this.canTimingService = canTimingService;
        this.request = request;
    }

    @Override
    public Result call() throws Exception {
        return canTimingService.clear(nid, request);
    }
}
