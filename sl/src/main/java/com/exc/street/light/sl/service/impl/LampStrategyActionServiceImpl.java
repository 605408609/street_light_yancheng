/**
 * @filename:LampStrategyActionServiceImpl 2020-08-26
 * @project sl  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.resource.entity.sl.LampStrategyAction;
import com.exc.street.light.resource.entity.sl.LampStrategyActionDeviceType;
import com.exc.street.light.resource.entity.sl.LampStrategyActionTimingMode;
import com.exc.street.light.resource.enums.sl.SystemTimingModeEnum;
import com.exc.street.light.resource.utils.CronUtil;
import com.exc.street.light.resource.utils.DataUtil;
import com.exc.street.light.resource.vo.req.SlReqStrategyActionVO;
import com.exc.street.light.sl.mapper.LampStrategyActionMapper;
import com.exc.street.light.sl.service.LampStrategyActionDeviceTypeService;
import com.exc.street.light.sl.service.LampStrategyActionService;
import com.exc.street.light.sl.service.LampStrategyActionTimingModeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 策略动作(服务实现)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
public class LampStrategyActionServiceImpl extends ServiceImpl<LampStrategyActionMapper, LampStrategyAction> implements LampStrategyActionService {

    private static final Logger logger = LoggerFactory.getLogger(LampStrategyActionServiceImpl.class);

    @Autowired
    private LampStrategyActionDeviceTypeService lampStrategyActionDeviceTypeService;

    @Autowired
    private LampStrategyActionTimingModeService lampStrategyActionTimingModeService;

    @Override
    public void insertLampStrategyAction(List<SlReqStrategyActionVO> slReqStrategyActionVOList) {
        logger.info("insertLampStrategyAction 添加策略动作:{}", slReqStrategyActionVOList);
        for (SlReqStrategyActionVO slReqStrategyActionVO : slReqStrategyActionVOList) {
            // 设备类型id集合
            List<Integer> deviceTypeIdOfActionList = slReqStrategyActionVO.getDeviceTypeIdOfActionList();
            if (deviceTypeIdOfActionList != null && deviceTypeIdOfActionList.size() > 0) {
                if (slReqStrategyActionVO.getEndDate() == null || slReqStrategyActionVO.getEndDate().isEmpty()) {
                    slReqStrategyActionVO.setEndDate("2999-01-01");
                }
                LampStrategyAction lampStrategyAction = new LampStrategyAction();
                BeanUtils.copyProperties(slReqStrategyActionVO, lampStrategyAction);
                lampStrategyAction.setCreateTime(new Date());
                // 定时方式id集合
                List<Integer> timeModeIdList = getTimeModeIdList(slReqStrategyActionVO, lampStrategyAction);
                int result = baseMapper.insert(lampStrategyAction);
                if (result < 1) {
                    logger.info("insertLampStrategyAction 添加策略动作失败:{}", slReqStrategyActionVOList);
                }
                // 绑定策略动作和定时方式的关系
                bindActionTimeMode(lampStrategyAction, timeModeIdList);
                // 绑定策略动作和设备类型的关系
                for (Integer deviceTypeId : deviceTypeIdOfActionList) {
                    // 绑定策略动作和设备类型的关系
                    LampStrategyActionDeviceType actionDeviceType = new LampStrategyActionDeviceType();
                    actionDeviceType.setLampStrategyActionId(lampStrategyAction.getId());
                    actionDeviceType.setDeviceTypeId(deviceTypeId);
                    lampStrategyActionDeviceTypeService.save(actionDeviceType);
                }
            }
        }
    }

    /**
     * 绑定策略动作和定时方式的关系
     *
     * @param lampStrategyAction
     * @param timeModeIdList
     */
    private void bindActionTimeMode(LampStrategyAction lampStrategyAction, List<Integer> timeModeIdList) {
        for (Integer timeModeId : timeModeIdList) {
            LampStrategyActionTimingMode actionTimingMode = new LampStrategyActionTimingMode();
            actionTimingMode.setStrategyActionId(lampStrategyAction.getId());
            actionTimingMode.setTimingModeId(timeModeId);
            lampStrategyActionTimingModeService.save(actionTimingMode);
        }
    }

    /**
     * 获取定时方式id集合
     *
     * @param slReqStrategyActionVO
     * @param lampStrategyAction
     * @return
     */
    private List<Integer> getTimeModeIdList(SlReqStrategyActionVO slReqStrategyActionVO, LampStrategyAction lampStrategyAction) {
        List<Integer> timeModeIdList = new ArrayList<>();
        if (slReqStrategyActionVO.getExecutionTime() != null && !slReqStrategyActionVO.getExecutionTime().isEmpty()
                && slReqStrategyActionVO.getStartDate() != null && !slReqStrategyActionVO.getStartDate().isEmpty()
                && slReqStrategyActionVO.getCycleTypes() != null && slReqStrategyActionVO.getCycleTypes().length > 0) {
            // 1,3,4组合 定时执行、时间段执行和周期执行
            Integer[] cycleTypes = slReqStrategyActionVO.getCycleTypes();
            int cycleType = DataUtil.getCycleType(cycleTypes, 1);
            lampStrategyAction.setWeekValue(cycleType);
            timeModeIdList.add(SystemTimingModeEnum.DS.code());
            timeModeIdList.add(SystemTimingModeEnum.SJD.code());
            timeModeIdList.add(SystemTimingModeEnum.ZQ.code());
        } else if (slReqStrategyActionVO.getExecutionTime() != null && !slReqStrategyActionVO.getExecutionTime().isEmpty()
                && slReqStrategyActionVO.getCycleTypes() != null && slReqStrategyActionVO.getCycleTypes().length > 0) {
            // 1,4组合 定时执行和周期执行,目前没有这种情况，因为时间段是必选
            Integer[] cycleTypes = slReqStrategyActionVO.getCycleTypes();
            String weekCron = CronUtil.getWeekCron(slReqStrategyActionVO.getExecutionTime(), cycleTypes);
            int cycleType = DataUtil.getCycleType(cycleTypes, 1);
            lampStrategyAction.setWeekValue(cycleType);
            lampStrategyAction.setCron(weekCron);
            timeModeIdList.add(SystemTimingModeEnum.DS.code());
            timeModeIdList.add(SystemTimingModeEnum.ZQ.code());
        } else if (slReqStrategyActionVO.getExecutionTime() != null && !slReqStrategyActionVO.getExecutionTime().isEmpty()
                && slReqStrategyActionVO.getStartDate() != null && !slReqStrategyActionVO.getStartDate().isEmpty()) {
            // 1,3组合 定时执行和时间段执行
            timeModeIdList.add(SystemTimingModeEnum.DS.code());
            timeModeIdList.add(SystemTimingModeEnum.SJD.code());
        } else if (slReqStrategyActionVO.getExecutionTime() != null && !slReqStrategyActionVO.getExecutionTime().isEmpty()) {
            // 1 定时执行
            timeModeIdList.add(SystemTimingModeEnum.DS.code());
            String lampStrategyActionCycle = CronUtil.getCronCycle(slReqStrategyActionVO.getExecutionTime());
            lampStrategyAction.setCron(lampStrategyActionCycle);
        } else if (slReqStrategyActionVO.getLightModeId() != null) {
            // 2 经纬度执行
            timeModeIdList.add(SystemTimingModeEnum.JWD.code());
            timeModeIdList.add(SystemTimingModeEnum.SJD.code());
        }
        return timeModeIdList;
    }
}