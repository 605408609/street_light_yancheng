/**
 * @filename:LoopSceneActionServiceImpl 2020-11-07
 * @project dlm  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.LoopSceneActionMapper;
import com.exc.street.light.dlm.service.LoopSceneActionService;
import com.exc.street.light.dlm.service.LoopSceneActionTimingModeService;
import com.exc.street.light.resource.entity.dlm.LoopSceneAction;
import com.exc.street.light.resource.entity.dlm.LoopSceneActionTimingMode;
import com.exc.street.light.resource.enums.sl.SystemTimingModeEnum;
import com.exc.street.light.resource.utils.CronUtil;
import com.exc.street.light.resource.utils.DataUtil;
import com.exc.street.light.resource.vo.req.DlmReqSceneActionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 场景动作(服务实现)
 *
 * @version: V1.0
 * @author: xiezhipeng
 *
 */
@Service
public class LoopSceneActionServiceImpl extends ServiceImpl<LoopSceneActionMapper, LoopSceneAction> implements LoopSceneActionService {

    private static final Logger logger = LoggerFactory.getLogger(LoopSceneActionServiceImpl.class);

    @Autowired
    private LoopSceneActionTimingModeService loopSceneActionTimingModeService;

    @Override
    public void insertSceneStrategyAction(List<DlmReqSceneActionVO> dlmReqSceneActionVOList) {
        logger.info("insertSceneStrategyAction - 新增场景动作 dlmReqSceneActionVOList=[{}]", dlmReqSceneActionVOList);
        for (DlmReqSceneActionVO dlmReqSceneActionVO : dlmReqSceneActionVOList) {
            if (dlmReqSceneActionVO.getEndDate() == null || dlmReqSceneActionVO.getEndDate().isEmpty()) {
                dlmReqSceneActionVO.setEndDate("2999-01-01");
            }
            LoopSceneAction loopSceneAction = new LoopSceneAction();
            BeanUtils.copyProperties(dlmReqSceneActionVO, loopSceneAction);
            loopSceneAction.setCreateTime(new Date());
            // 定时方式id集合
            List<Integer> timeModeIdList = getTimeModeIdList(dlmReqSceneActionVO, loopSceneAction);
            int result = baseMapper.insert(loopSceneAction);
            if (result < 1) {
                logger.info("insertSceneStrategyAction 新增场景动作失败:{}", dlmReqSceneActionVOList);
            }
            // 绑定策略动作和定时方式的关系
            bindActionTimeMode(loopSceneAction, timeModeIdList);
        }
    }

    /**
     * 绑定策略动作和定时方式的关系
     *
     * @param loopSceneAction
     * @param timeModeIdList
     */
    private void bindActionTimeMode(LoopSceneAction loopSceneAction, List<Integer> timeModeIdList) {
        for (Integer timeModeId : timeModeIdList) {
            LoopSceneActionTimingMode actionTimingMode = new LoopSceneActionTimingMode();
            actionTimingMode.setSceneActionId(loopSceneAction.getId());
            actionTimingMode.setTimingModeId(timeModeId);
            loopSceneActionTimingModeService.save(actionTimingMode);
        }
    }

    /**
     * 获取定时方式id集合
     *
     * @param dlmReqSceneActionVO
     * @param loopSceneAction
     * @return
     */
    private List<Integer> getTimeModeIdList(DlmReqSceneActionVO dlmReqSceneActionVO, LoopSceneAction loopSceneAction) {
        List<Integer> timeModeIdList = new ArrayList<>();
        if (dlmReqSceneActionVO.getExecutionTime() != null && !dlmReqSceneActionVO.getExecutionTime().isEmpty()
                && dlmReqSceneActionVO.getStartDate() != null && !dlmReqSceneActionVO.getStartDate().isEmpty()
                && dlmReqSceneActionVO.getCycleTypes() != null && dlmReqSceneActionVO.getCycleTypes().length > 0) {
            // 1,3,4组合 定时执行、时间段执行和周期执行
            Integer[] cycleTypes = dlmReqSceneActionVO.getCycleTypes();
            int cycleType = DataUtil.getCycleType(cycleTypes, 1);
            loopSceneAction.setWeekValue(cycleType);
            timeModeIdList.add(SystemTimingModeEnum.DS.code());
            timeModeIdList.add(SystemTimingModeEnum.SJD.code());
            timeModeIdList.add(SystemTimingModeEnum.ZQ.code());
        } else if (dlmReqSceneActionVO.getExecutionTime() != null && !dlmReqSceneActionVO.getExecutionTime().isEmpty()
                && dlmReqSceneActionVO.getCycleTypes() != null && dlmReqSceneActionVO.getCycleTypes().length > 0) {
            // 1,4组合 定时执行和周期执行,目前没有这种情况，因为时间段是必选
            Integer[] cycleTypes = dlmReqSceneActionVO.getCycleTypes();
            String weekCron = CronUtil.getWeekCron(dlmReqSceneActionVO.getExecutionTime(), cycleTypes);
            int cycleType = DataUtil.getCycleType(cycleTypes, 1);
            loopSceneAction.setWeekValue(cycleType);
            loopSceneAction.setCron(weekCron);
            timeModeIdList.add(SystemTimingModeEnum.DS.code());
            timeModeIdList.add(SystemTimingModeEnum.ZQ.code());
        } else if (dlmReqSceneActionVO.getExecutionTime() != null && !dlmReqSceneActionVO.getExecutionTime().isEmpty()
                && dlmReqSceneActionVO.getStartDate() != null && !dlmReqSceneActionVO.getStartDate().isEmpty()) {
            // 1,3组合 定时执行和时间段执行
            timeModeIdList.add(SystemTimingModeEnum.DS.code());
            timeModeIdList.add(SystemTimingModeEnum.SJD.code());
        } else if (dlmReqSceneActionVO.getExecutionTime() != null && !dlmReqSceneActionVO.getExecutionTime().isEmpty()) {
            // 1 定时执行
            timeModeIdList.add(SystemTimingModeEnum.DS.code());
            String loopSceneActionCycle = CronUtil.getCronCycle(dlmReqSceneActionVO.getExecutionTime());
            loopSceneAction.setCron(loopSceneActionCycle);
        } else if (dlmReqSceneActionVO.getOpenModeId() != null) {
            // 2 经纬度执行
            timeModeIdList.add(SystemTimingModeEnum.JWD.code());
            timeModeIdList.add(SystemTimingModeEnum.SJD.code());
        }
        return timeModeIdList;
    }
}