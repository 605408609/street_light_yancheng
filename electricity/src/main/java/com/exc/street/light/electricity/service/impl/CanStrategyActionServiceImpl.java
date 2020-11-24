/**
 * @filename:CanStrategyActionServiceImpl 2020-11-18
 * @project electricity  V1.0
 * Copyright(c) 2018 Xiaok Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.electricity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.mapper.CanStrategyActionMapper;
import com.exc.street.light.electricity.service.CanChannelService;
import com.exc.street.light.electricity.service.CanStrategyActionService;
import com.exc.street.light.electricity.util.DataUtil;
import com.exc.street.light.resource.dto.dlm.ControlLoopTimerDTO;
import com.exc.street.light.resource.entity.electricity.CanChannel;
import com.exc.street.light.resource.entity.electricity.CanStrategyAction;
import com.exc.street.light.resource.vo.req.electricity.ReqCanStrategyActionVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO(路灯网关策略对应动作表服务实现)
 * @version: V1.0
 * @author: Xiaok
 */
@Slf4j
@Service
public class CanStrategyActionServiceImpl extends ServiceImpl<CanStrategyActionMapper, CanStrategyAction> implements CanStrategyActionService {

    private static final SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
    @Lazy
    @Autowired
    private CanChannelService canChannelService;

    @Override
    public boolean saveAction(Integer strategyId, List<ReqCanStrategyActionVO> actionList) {
        log.info("更新路灯网关策略动作集合,接收参数：策略ID={},List<CanStrategyAction>={}", strategyId, actionList);
        if (strategyId == null) {
            log.error("更新路灯网关策略动作失败,策略ID为空");
            return false;
        }
        if (actionList == null || actionList.isEmpty()) {
            log.info("更新路灯网关策略成功,动作集合为空");
            return true;
        }
        //先删除该策略下的动作
        LambdaQueryWrapper<CanStrategyAction> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(CanStrategyAction::getStrategyId, strategyId);
        this.remove(deleteWrapper);
        //再新增动作集合
        List<CanStrategyAction> actions = new ArrayList<>();
        Date now = new Date();
        for (ReqCanStrategyActionVO actionVO : actionList) {
            //开始日期校验，为空默认当前日期
            String startDateStr = actionVO.getStartDate();
            if (StringUtils.isBlank(startDateStr)) {
                startDateStr = dateSdf.format(now);
            }
            actions.add(new CanStrategyAction()
                    .setCreateTime(now)
                    .setEndDate(actionVO.getEndDate())
                    .setStartDate(startDateStr)
                    .setExecutionTime(actionVO.getExecutionTime())
                    .setWeekValue(DataUtil.getCycleType(actionVO.getCycleTypes(), 1))
                    .setIsOpen(actionVO.getIsOpen())
                    .setStrategyId(strategyId)
            );
        }
        this.saveBatch(actions);
        log.info("更新路灯网关策略成功,接收参数：策略ID={},List<CanStrategyAction>={}", strategyId, actionList);
        return true;
    }

    @Override
    public List<ControlLoopTimerDTO> getTimerList(Integer nodeId, List<CanStrategyAction> actionList, List<Integer> controlIdList) {
        List<ControlLoopTimerDTO> timerList = new ArrayList<>();
        if (nodeId == null) {
            return timerList;
        }
        List<ControlLoopTimerDTO> currentOrderTimer = getTimerListByActionAndControlId(actionList, controlIdList);
        if (!currentOrderTimer.isEmpty()) {
            timerList.addAll(currentOrderTimer);
        }
        //设置controlIdList的其他回路绑定的策略也添加进来
        LambdaQueryWrapper<CanChannel> channelQuery = new LambdaQueryWrapper<>();
        channelQuery.select(CanChannel::getControlId, CanChannel::getStrategyId).eq(CanChannel::getNid, nodeId).eq(CanChannel::getSid, 4)
                .notIn(CanChannel::getControlId, controlIdList).isNotNull(CanChannel::getStrategyId);
        List<CanChannel> channelList = canChannelService.list(channelQuery);
        if (channelList == null || channelList.isEmpty()) {
            return timerList;
        }
        //获取策略id集合
        List<Integer> strategyIdList = channelList.stream().map(CanChannel::getStrategyId).distinct().collect(Collectors.toList());
        if (strategyIdList.isEmpty()) {
            return timerList;
        }
        //查询之前下发的其他回路的所有动作
        LambdaQueryWrapper<CanStrategyAction> actionWrapper = new LambdaQueryWrapper<>();
        actionWrapper.in(CanStrategyAction::getStrategyId, strategyIdList);
        List<CanStrategyAction> otherLoopStrategyActionList = this.list(actionWrapper);
        if (otherLoopStrategyActionList.isEmpty()) {
            return timerList;
        }
        //key:策略ID value：对应的动作集合
         Map<Integer, List<CanStrategyAction>> strategyActionMap = otherLoopStrategyActionList.stream().collect(Collectors.groupingBy(CanStrategyAction::getStrategyId));
        for (CanChannel channel : channelList) {
            List<CanStrategyAction> actions = strategyActionMap.get(channel.getStrategyId());
            List<ControlLoopTimerDTO> timerListByActionAndControlId = getTimerListByActionAndControlId(actions, Collections.singletonList(channel.getControlId()));
            if (!timerListByActionAndControlId.isEmpty()) {
                timerList.addAll(timerListByActionAndControlId);
            }
        }
        return timerList;
    }

    /**
     * 生成定时对象
     *
     * @param actionList    动作对象集合
     * @param controlIdList 控制id集合
     * @return 定时对象集合
     */
    private List<ControlLoopTimerDTO> getTimerListByActionAndControlId(List<CanStrategyAction> actionList, List<Integer> controlIdList) {
        List<ControlLoopTimerDTO> timerList = new ArrayList<>();
        if (actionList != null && controlIdList != null) {
            for (CanStrategyAction action : actionList) {
                for (Integer controlId : controlIdList) {
                    int[] intArr = null;
                    if (action.getWeekValue() != null) {
                        Integer[] cycleTypes = DataUtil.getCycleName(action.getWeekValue());
                        // 将日期向前移一天到正常日期
                        List<Integer> cycleTypeList = Arrays.stream(cycleTypes).filter(Objects::nonNull).collect(Collectors.toList());
                        intArr = cycleTypeList.stream().mapToInt(Integer::intValue).toArray();
                    }
                    Date startDate = null;
                    Date endDate = null;
                    try {
                        if (StringUtils.isNotBlank(action.getStartDate())) {
                            startDate = dateSdf.parse(action.getStartDate());
                        }
                        if (StringUtils.isNotBlank(action.getEndDate())) {
                            endDate = dateSdf.parse(action.getEndDate());
                        }
                    } catch (ParseException e) {
                        log.info("网关策略下发,时间转换错误，接收参数：startDate = {},endDate = {}", action.getStartDate(), action.getEndDate());
                    }
                    // 构建下发对象
                    ControlLoopTimerDTO dto = new ControlLoopTimerDTO()
                            //定时类型
                            .setType(1)
                            //回路编号
                            .setLoopNum(controlId)
                            //动作： true-开 false-关
                            .setIsOpen(action.getIsOpen() == 1)
                            //选择周期 没有填null
                            .setCycleTypes(intArr == null || intArr.length == 0 ? null : intArr)
                            //开始时间 没有传null
                            .setStartDate(startDate)
                            //结束时间 没有传null
                            .setEndDate(endDate)
                            //HH:mm:ss 时间点
                            .setTime(action.getExecutionTime());
                    timerList.add(dto);
                }
            }
        }
        return timerList;
    }
}