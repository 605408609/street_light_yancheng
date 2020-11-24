package com.exc.street.light.woa.quartz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.woa.Alarm;
import com.exc.street.light.resource.entity.woa.AlarmOrderSet;
import com.exc.street.light.resource.entity.woa.AlarmOrderSetLampPost;
import com.exc.street.light.resource.quartz.ScheduleParam;
import com.exc.street.light.resource.vo.req.WoaReqOrderNewVO;
import com.exc.street.light.woa.mapper.AlarmOrderSetMapper;
import com.exc.street.light.woa.service.AlarmOrderSetLampPostService;
import com.exc.street.light.woa.service.AlarmOrderSetService;
import com.exc.street.light.woa.service.AlarmService;
import com.exc.street.light.woa.service.OrderInfoService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Longshuangyang
 * @date 2020/03/24
 */
@Component
public class QuartzManager {
    private static final Logger logger = LoggerFactory.getLogger(QuartzManager.class);
    public static final String TRIGGER_GROUP_NAME = "alarmOrderSet";
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private AlarmOrderSetService alarmOrderSetService;
    @Autowired
    private AlarmOrderSetMapper alarmOrderSetMapper;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private AlarmOrderSetLampPostService alarmOrderSetLampPostService;


    /**
     * 项目重启时,加载使用中的定时
     */
    @PostConstruct
    public void init() {
        logger.info("导入需执行任务");
        // 获取所有的自动生成工单设置
        LambdaQueryWrapper<AlarmOrderSet> alarmOrderSetWrapper = new LambdaQueryWrapper();
        alarmOrderSetWrapper.eq(AlarmOrderSet::getStatus, 1);
        List<AlarmOrderSet> alarmOrderSetList = alarmOrderSetService.list(alarmOrderSetWrapper);
        List<Integer> setIdList = alarmOrderSetList.stream().map(AlarmOrderSet::getId).collect(Collectors.toList());
        if (setIdList != null && setIdList.size() > 0) {
            // 获取所有设置对应的灯杆关联关系
            List<AlarmOrderSetLampPost> alarmOrderSetLampPostList = (ArrayList) alarmOrderSetLampPostService.listByIds(setIdList);
            for (AlarmOrderSet alarmOrderSet : alarmOrderSetList) {
                // 获取当天设置关联的灯杆id集合
                List<AlarmOrderSetLampPost> collect = alarmOrderSetLampPostList.stream().filter(a -> alarmOrderSet.getId().equals(a.getSetId())).collect(Collectors.toList());
                List<Integer> lampPostIdList = collect.stream().map(AlarmOrderSetLampPost::getLampPostId).collect(Collectors.toList());
                // cron表达式
                String cron = "0 0/" + alarmOrderSet.getAutoDuration() + " * ? * 1,2,3,4,5,6,7";
                // 构造请求参数
                ScheduleParam scheduleParam = new ScheduleParam();
                scheduleParam.setLampPostIdList(lampPostIdList);
                scheduleParam.setJobKey(String.valueOf(alarmOrderSet.getId()));
                scheduleParam.setCron(cron);
                addCronCycleJob(scheduleParam);
                logger.info("启动定时：" + scheduleParam.getJobKey());
            }
        }
    }

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

    public void setJob(ScheduleParam param) {
        // 获取告警生成工单设置对象
        Integer setId = Integer.parseInt(param.getJobKey());
        AlarmOrderSet byId = alarmOrderSetService.getById(setId);
        // 灯杆id集合
        List<Integer> lampPostIdList = param.getLampPostIdList();
        logger.info("灯杆id集合，{}", lampPostIdList);
        if (lampPostIdList == null || lampPostIdList.size() == 0) {
            logger.info("任务" + param.getJobKey() + "没有灯杆");
            return;
        }
        // 获取没有处理的灯杆下的告警集合
        LambdaQueryWrapper<Alarm> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Alarm::getLampPostId, lampPostIdList);
        wrapper.eq(Alarm::getStatus, 1);
        List<Alarm> alarmList = alarmService.list(wrapper);
        // 处理时长得到完成时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日hh:mm:ss");
        Integer handleDuration = byId.getHandleDuration();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + handleDuration);
        Date finishTime = calendar.getTime();
        // 每个个灯杆一个工单
        for (Integer lampPostId : lampPostIdList) {
            List<Alarm> collect = alarmList.stream().filter(a -> lampPostId.equals(a.getLampPostId())).collect(Collectors.toList());
            List<Integer> alarmIdList = collect.stream().map(Alarm::getId).collect(Collectors.toList());
            if (alarmIdList == null || alarmIdList.size() == 0) {
                logger.info("任务" + param.getJobKey() + "没有告警");
                return;
            }
            SlLampPost slLampPost = alarmOrderSetMapper.getLampPostById(lampPostId);
            // 构建添加工单参数
            WoaReqOrderNewVO woaReqOrderNewVO = new WoaReqOrderNewVO();
            woaReqOrderNewVO.setName("系统自动生成" + UUID.randomUUID().toString().replaceAll("-", "").substring(0,8));
            woaReqOrderNewVO.setAlarmIdList(alarmIdList);
            woaReqOrderNewVO.setProcessor(byId.getProcessor());
            woaReqOrderNewVO.setFinishTime(finishTime);
            woaReqOrderNewVO.setFoundMode(1);
            woaReqOrderNewVO.setAlarmTypeId(0);
            woaReqOrderNewVO.setAddr(slLampPost.getLocation());
            woaReqOrderNewVO.setLampPostId(slLampPost.getId());
            orderInfoService.add(null, woaReqOrderNewVO);
        }
    }

}
