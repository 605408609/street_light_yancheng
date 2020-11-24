package com.exc.street.light.ir.task;

import com.exc.street.light.ir.utils.ScreenSendUtil;
import com.exc.street.light.resource.core.Result;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Callable;

/**
 * 显示屏开关任务
 *
 * @author Longshuangyang
 * @date 2019/08/15
 */
@Setter
@Getter
public class ScreenPlayingProgramTask implements Callable<Result> {

    private String ip;
    private Integer port;
    private String sn;
    private String program;

    public ScreenPlayingProgramTask(String ip, Integer port, String sn, String program) {
        this.ip = ip;
        this.port = port;
        this.sn = sn;
        this.program = program;
    }

    @Override
    public Result call() throws Exception {
        return ScreenSendUtil.threadTask(ip, port, sn, program);
    }

}
