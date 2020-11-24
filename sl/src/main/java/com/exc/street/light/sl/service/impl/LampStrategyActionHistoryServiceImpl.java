/**
 * @filename:LampStrategyActionHistoryServiceImpl 2020-03-23
 * @project sl  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampStrategyActionHistory;
import com.exc.street.light.resource.utils.DataUtil;
import com.exc.street.light.resource.vo.req.SlReqStrategyActionVO;
import com.exc.street.light.sl.mapper.LampStrategyActionHistoryDao;
import com.exc.street.light.sl.service.LampStrategyActionHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class LampStrategyActionHistoryServiceImpl extends ServiceImpl<LampStrategyActionHistoryDao, LampStrategyActionHistory> implements LampStrategyActionHistoryService {
    private static final Logger logger = LoggerFactory.getLogger(LampStrategyActionHistoryServiceImpl.class);

    @Override
    public Result getList(Integer lampStrategyTypeId, HttpServletRequest request) {
        logger.info("查询策略历史动作列表，接收参数：{}", lampStrategyTypeId);
        LambdaQueryWrapper<LampStrategyActionHistory> wrapper = new LambdaQueryWrapper();
        if (lampStrategyTypeId != null) {
            wrapper.eq(LampStrategyActionHistory::getStrategyTypeId, lampStrategyTypeId);
        }
        List<LampStrategyActionHistory> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result add(List<SlReqStrategyActionVO> slReqStrategyActionVOList, HttpServletRequest request) {
        logger.info("添加策略历史动作:{}", slReqStrategyActionVOList);
        Result result = new Result();
        // 先删除当前策略类型下的历史动作
//        LambdaQueryWrapper<LampStrategyActionHistory> wrapper = new LambdaQueryWrapper();
//        if (slReqStrategyActionVOList != null && slReqStrategyActionVOList.size() > 0 && slReqStrategyActionVOList.get(0) != null) {
//            wrapper.eq(LampStrategyActionHistory::getStrategyTypeId, slReqStrategyActionVOList.get(0).getStrategyTypeId());
//        } else {
//            wrapper.eq(LampStrategyActionHistory::getStrategyTypeId, null);
//        }
//        this.remove(wrapper);
//        // 保存新的动作为历史动作
//        for (SlReqStrategyActionVO slReqStrategyActionVO : slReqStrategyActionVOList) {
//            LampStrategyActionHistory lampStrategyActionHistory = new LampStrategyActionHistory();
//            lampStrategyActionHistory.setCreateTime(new Date());
//            lampStrategyActionHistory.setEndDate(slReqStrategyActionVO.getEndDate());
//            lampStrategyActionHistory.setStartDate(slReqStrategyActionVO.getStartDate());
//            lampStrategyActionHistory.setExecutionTime(slReqStrategyActionVO.getExecutionTime());
//            BeanUtils.copyProperties(slReqStrategyActionVO, lampStrategyActionHistory);
//            int mode = slReqStrategyActionVO.getExecutionMode();
//            if (mode == 1) {
//                //保存定时策略
//                String lampStrategyActionHistoryCycle = CronUtil.getCronCycle(slReqStrategyActionVO.getExecutionTime());
//                lampStrategyActionHistory.setCron(lampStrategyActionHistoryCycle);
//            }
//            if (mode == 2) {
//                //保存周期策略
//                Integer[] cycleTypes = slReqStrategyActionVO.getCycleTypes();
//                String weekCron = CronUtil.getWeekCron(slReqStrategyActionVO.getExecutionTime(), cycleTypes);
//                int cycleType = DataUtil.getCycleType(cycleTypes, 1);
//                lampStrategyActionHistory.setWeekValue(cycleType);
//                lampStrategyActionHistory.setCron(weekCron);
//            }
//            this.save(lampStrategyActionHistory);
//        }
        return result.success("");
    }

    @Override
    public Result detail(Integer strategyTypeId, HttpServletRequest request) {
        logger.info("获取策略历史动作详情:{}", strategyTypeId);
        LambdaQueryWrapper<LampStrategyActionHistory> wrapper = new LambdaQueryWrapper();
        if (strategyTypeId != null) {
            wrapper.eq(LampStrategyActionHistory::getStrategyTypeId, strategyTypeId);
        }
        List<LampStrategyActionHistory> lampStrategyActionHistoryList = this.list(wrapper);
        // 够建返回对象
        List<SlReqStrategyActionVO> slReqStrategyActionVOList = new ArrayList<>();
        for (LampStrategyActionHistory lampStrategyActionHistory : lampStrategyActionHistoryList) {
            SlReqStrategyActionVO slReqStrategyActionVO = new SlReqStrategyActionVO();
            BeanUtils.copyProperties(lampStrategyActionHistory, slReqStrategyActionVO);
            if (lampStrategyActionHistory.getExecutionMode() == 2) {
                Integer[] cycleName = DataUtil.getCycleName(lampStrategyActionHistory.getWeekValue());
                slReqStrategyActionVO.setCycleTypes(cycleName);
            }
            slReqStrategyActionVOList.add(slReqStrategyActionVO);
        }
        Result result = new Result();
        return result.success(slReqStrategyActionVOList);
    }
}