package com.exc.street.light.electricity.task;

import com.exc.street.light.electricity.service.CanTimingService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.vo.electricity.TimerVO;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;

/**
 * 定时控制任务
 *
 * @author LinShiWen
 * @date 2017/12/8
 */
@Setter
@Getter
public class CanTimerTask implements Callable<Result> {
    private TimerVO timerVO;
    private CanTimingService canTimingService;
    private HttpServletRequest request;

    public CanTimerTask(TimerVO timerVO, CanTimingService canTimingService, HttpServletRequest request) {
        this.timerVO = timerVO;
        this.canTimingService = canTimingService;
        this.request = request;
    }

    @Override
    public Result call() throws Exception {
        return canTimingService.add(timerVO, request);
    }
}
