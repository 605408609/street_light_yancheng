/**
 * @filename:DeviceStrategyHistoryServiceImpl 2020-09-04
 * @project sl  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.DeviceStrategyHistory;
import com.exc.street.light.resource.entity.sl.LampStrategyAction;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.SlLampDeviceHistoryQuery;
import com.exc.street.light.resource.qo.SlLampStrategyHistoryQuery;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.resp.SlRespDeviceStrategyHistoryVO;
import com.exc.street.light.resource.vo.resp.SlRespLampDeviceHistoryVO;
import com.exc.street.light.sl.mapper.DeviceStrategyHistoryMapper;
import com.exc.street.light.sl.service.DeviceStrategyHistoryService;
import com.exc.street.light.sl.service.LampStrategyActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 历史设备策略中间表(服务实现)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
public class DeviceStrategyHistoryServiceImpl extends ServiceImpl<DeviceStrategyHistoryMapper, DeviceStrategyHistory> implements DeviceStrategyHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(LampStrategyServiceImpl.class);

    @Autowired
    private LampStrategyActionService lampStrategyActionService;

    @Autowired
    private LogUserService logUserService;

    @Override
    public DeviceStrategyHistory selectNewOne(Integer deviceId, Integer strategyId) {
        logger.info("根据设备及策略id获取最新一条数据：{}，{}", deviceId,strategyId);
        return baseMapper.selectNewOne(deviceId, strategyId);
    }

    @Override
    public List<DeviceStrategyHistory> selectLastList(List<Integer> deviceIdList) {
        logger.info("根据设备id集合获取最新历史记录数据集合：{}", deviceIdList);
        return baseMapper.selectLastList(deviceIdList);
    }

    @Override
    public Result getPage(SlLampStrategyHistoryQuery strategyHistoryQuery, HttpServletRequest request) {
        logger.info("getPage - 历史策略分页条件查询 strategyHistoryQuery={}", strategyHistoryQuery);
        // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if (!flag) {
            strategyHistoryQuery.setAreaId(user.getAreaId());
        }
        Result<IPage<SlRespDeviceStrategyHistoryVO>> result = new Result<>();
        Page<SlRespDeviceStrategyHistoryVO> page = new Page<>(strategyHistoryQuery.getPageNum(), strategyHistoryQuery.getPageSize());
        return result.success(baseMapper.selectHistoryStrategyWithPage(page, strategyHistoryQuery));
    }

    @Override
    public Result getHistoryDeviceList(SlLampDeviceHistoryQuery deviceHistoryQuery, HttpServletRequest request) {
        logger.info("getHistoryDeviceList - 查询该策略下所下发的设备 deviceHistoryQuery={}", deviceHistoryQuery);
        Result<List<SlRespLampDeviceHistoryVO>> result = new Result<>();
        if (deviceHistoryQuery.getStrategyId() != null) {
            List<SlRespLampDeviceHistoryVO> deviceHistoryVOList =  baseMapper.selectHistoryDeviceList(deviceHistoryQuery);
            return result.success(deviceHistoryVOList);
        } else {
            return result.success("暂无数据");
        }
    }

    @Override
    public Result updateHistoryDeviceList(List<Integer> historyIdList, HttpServletRequest request) {
        logger.info("updateHistoryDeviceList - 修改重发的历史策略记录信息 historyIdList={}", historyIdList);
        LambdaUpdateWrapper<DeviceStrategyHistory> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(DeviceStrategyHistory::getIsSuccess, 0)
                .in(DeviceStrategyHistory::getId, historyIdList);
        baseMapper.update(null, updateWrapper);
        return new Result().success("下发成功");
    }

    @Override
    public Result getHistoryStrategyList(Integer deviceId, HttpServletRequest request) {
        logger.info("getHistoryStrategyList - 根据设备id查询历史策略集合 deviceId={}", deviceId);
        List<SlRespDeviceStrategyHistoryVO> strategyHistoryVOList = baseMapper.selectHistoryStrategyList(deviceId);
        List<SlRespDeviceStrategyHistoryVO> slRespStrategyVOList = new ArrayList<>();
        if (strategyHistoryVOList.size() > 0) {
            List<Integer> strategyIdList = strategyHistoryVOList.stream().map(SlRespDeviceStrategyHistoryVO::getStrategyId).collect(Collectors.toList());
            if (strategyIdList.size() > 0) {
                // 查询策略下的动作集合
                LambdaQueryWrapper<LampStrategyAction> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(LampStrategyAction::getStrategyId, strategyIdList);
                List<LampStrategyAction> strategyActionList = lampStrategyActionService.list(wrapper);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                for (SlRespDeviceStrategyHistoryVO strategyHistoryVO : strategyHistoryVOList) {
                    // 获取策略下的动作集合
                    List<LampStrategyAction> actionCollect = strategyActionList.stream().filter(a -> a.getStrategyId().equals(strategyHistoryVO.getStrategyId())).collect(Collectors.toList());
                    String minStartDateString = null;
                    Date minStartDate = null;
                    try {
                        minStartDate = dateFormat.parse("2999-01-01");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String maxEndDateString = null;
                    Date maxEndDate = new Date(1);
                    // 用于表示是否需要继续比较结束时间
                    int flag = 1;
                    // 循环策略动作集合,对比时间获取策略的开始结束时间
                    for (LampStrategyAction lampStrategyAction : actionCollect) {
                        // 比较获取最小开始时间
                        String startDateString = lampStrategyAction.getStartDate();
                        if (startDateString == null || "".equals(startDateString)) {
                            continue;
                        }
                        Date startDate = null;
                        try {
                            startDate = dateFormat.parse(startDateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int resStart = minStartDate.compareTo(startDate);
                        if (resStart == 1) {
                            minStartDate = startDate;
                            minStartDateString = startDateString;
                        }
                        // 比较获取最大的结束时间
                        if (flag == 1) {
                            String endDateString = lampStrategyAction.getEndDate();
                            if (endDateString == null || "".equals(endDateString)) {
                                continue;
                            }
                            Date endDate = null;
                            try {
                                endDate = dateFormat.parse(endDateString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (endDate != null) {
                                int resEnd = maxEndDate.compareTo(endDate);
                                if (resEnd == -1) {
                                    maxEndDate = endDate;
                                    maxEndDateString = endDateString;
                                }
                            } else {
                                maxEndDateString = null;
                                flag = 0;
                            }
                        }
                    }
                    strategyHistoryVO.setStartDate(minStartDateString);
                    strategyHistoryVO.setEndDate(maxEndDateString);
                    slRespStrategyVOList.add(strategyHistoryVO);
                }
            }
        }
        Result<List<SlRespDeviceStrategyHistoryVO>> result = new Result<>();
        return result.success(slRespStrategyVOList);
    }
}