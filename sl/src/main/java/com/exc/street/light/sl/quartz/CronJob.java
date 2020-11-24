package com.exc.street.light.sl.quartz;

import com.exc.street.light.resource.quartz.ScheduleParam;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时指定节目任务
 *
 * @author Longshuangyang
 * @date 2020/03/24
 */
@Component
public class CronJob implements Job {
    private Logger logger = LoggerFactory.getLogger(CronJob.class);
    @Autowired
    private QuartzManager quartzManager;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        ScheduleParam param = (ScheduleParam) dataMap.get("scheduleParam");
        logger.info("开始执行定时任务:{}", param.getJobKey());
        quartzManager.setJob(param);
    }


}
