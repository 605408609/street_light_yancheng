package com.exc.street.light.sl.quartz;

import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampDevice;
import com.exc.street.light.resource.quartz.ScheduleParam;
import com.exc.street.light.resource.utils.BaseConstantUtil;
import com.exc.street.light.resource.vo.req.SlReqCommandParamVO;
import com.exc.street.light.resource.vo.req.SlReqCommandVO;
import com.exc.street.light.resource.vo.req.SlReqDeviceControlVO;
import com.exc.street.light.sl.config.parameter.HttpDlmApi;
import com.exc.street.light.sl.service.LampDeviceService;
import com.exc.street.light.sl.task.SingleControlTask;
import com.exc.street.light.sl.utils.LampApiUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.*;

/**
 * @author Longshuangyang
 * @date 2020/03/24
 */
@Component
public class QuartzManager {
    private static final Logger logger = LoggerFactory.getLogger(QuartzManager.class);
    public static final String TRIGGER_GROUP_NAME = "lightStrategy";
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private LampApiUtil lampApiUtil;
    @Autowired
    private HttpDlmApi httpDlmApi;
    @Autowired
    private LampDeviceService lampDeviceService;

//    /**
//     * 项目重启时,加载使用中的策略
//     */
//    @PostConstruct
//    public void init() {
//        Condition condition = new Condition(LightStrategy.class);
//        condition.createCriteria().andEqualTo("status", 1);
//        List<LightStrategy> strategies = lightStrategyService.findByCondition(condition);
//        for (LightStrategy strategy : strategies) {
//            Condition condition1 = new Condition(LightCron.class);
//            condition1.createCriteria().andEqualTo("strategyId", strategy.getId());
//            List<LightCron> cronList = lightCronService.findByCondition(condition1);
//            for (LightCron cron : cronList) {
//                ScheduleParam param = new ScheduleParam();
//                param.setCron(cron.getCron());
//                param.setJobKey(String.valueOf(cron.getId()));
//                param.setType(cron.getType());
//                param.setLightness(cron.getLightness());
//                param.setSiteId(strategy.getSiteId());
//                int mode = cron.getMode();
//                if (mode == 1) {
//                    logger.info("下发周期任务:{},名称:{}", cron.getId(), cron.getName());
//                    this.addCronCycleJob(param);
//                }
//                if (mode == 2) {
//                    logger.info("下发定时任务:{},名称:{}", cron.getId(), cron.getName());
//                    Date startDate = TimeUtil.getDate(cron.getStartDate(), cron.getStartTime());
//                    param.setStartDate(startDate);
//                    Date endDate = TimeUtil.getDate(cron.getEndDate(), cron.getStartTime());
//                    param.setEndDate(endDate);
//                    long currentTimeMillis = System.currentTimeMillis();
//                    long endDateTime = endDate.getTime();
//                    if (currentTimeMillis <= endDateTime) {
//                        this.addCronJob(param);
//                    }
//                }
//            }
//        }
//    }

    /**
     * 周期执行
     *
     * @param param
     */
    public void addCronCycleJob(ScheduleParam param) {
        try {
            String jobKey = param.getJobKey();
            JobDetail jobDetail = JobBuilder.newJob(CronJob.class)
                    .withIdentity(jobKey, TRIGGER_GROUP_NAME).build();
            jobDetail.getJobDataMap().put("scheduleParam", param);
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(param.getCron());
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobKey, TRIGGER_GROUP_NAME)
                    .withSchedule(cronScheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 按日期指定播放节目定时器
     *
     * @param param
     */
    public void addCronJob(ScheduleParam param) {
        try {
            String jobKey = param.getJobKey();
            JobDetail jobDetail = JobBuilder.newJob(CronJob.class).withIdentity(jobKey, TRIGGER_GROUP_NAME).build();
            jobDetail.getJobDataMap().put("scheduleParam", param);
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(param.getCron())
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobKey, TRIGGER_GROUP_NAME)
                    .withSchedule(cronScheduleBuilder).startAt(param.getStartDate())
                    .endAt(param.getEndDate()).build();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 停止定时任务
     *
     * @param key
     */
    public void stop(String key) {
        try {
            JobKey jobKey = new JobKey(key, TRIGGER_GROUP_NAME);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail != null) {
                logger.info("停止定时任务" + jobKey);
                scheduler.deleteJob(jobKey);

            }
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 暂停定时任务
     *
     * @param key
     */
    public void pause(String key) {
        try {
            JobKey jobKey = new JobKey(key, TRIGGER_GROUP_NAME);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail != null) {
                logger.info("暂停定时任务" + jobKey);
                scheduler.pauseJob(jobKey);
            }
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void setJob(ScheduleParam param) {
        Result token = lampApiUtil.getToken();
        if (token.getCode() == Const.CODE_FAILED) {
            //请求失败,直接返回
            logger.error("请求第三方路灯平台失败");
            return;
        }
        Collection<SingleControlTask> tasks = new ArrayList<>();
        // 循环路灯设备的集合
        for (LampDevice lampDevice : param.getLampDeviceList()) {
            //设置控制任务
            SlReqDeviceControlVO controlVO = new SlReqDeviceControlVO();
            controlVO.setToken(token.getMessage());
            controlVO.setConfirm(false);
            controlVO.setDevport(0);
            controlVO.setDeveui(lampDevice.getNum());
            controlVO.setDevtype(BaseConstantUtil.LIGHT_DEVICE_TYPE);
            //命令对象
            SlReqCommandVO commandVO = new SlReqCommandVO();
            commandVO.setCmd(BaseConstantUtil.SINGLE_CMD);
            //参数对象
            SlReqCommandParamVO paramVO = new SlReqCommandParamVO();
            paramVO.setCtrl0(param.getLightness());
            paramVO.setCtrl1(0);
            paramVO.setManctrldelay(BaseConstantUtil.MANCTRL_DELAY);
            commandVO.setParas(paramVO);
            controlVO.setCommand(commandVO);
            SingleControlTask task = new SingleControlTask(lampDeviceService, lampDevice);
            tasks.add(task);
        }
        //开启多线程
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("channel-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(BaseConstantUtil.THREADPOOLSIZE_2,
                BaseConstantUtil.THREADPOOLSIZE_2, 0L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), namedThreadFactory);
        logger.info("运行任务");
        Result result1 = lampDeviceService.getResult(tasks, executorService);
        logger.info("运行结果:{}", result1);
    }
}
