/**
 * @filename:DeviceStrategyHistoryService 2020-09-04
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.DeviceStrategyHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.qo.SlLampDeviceHistoryQuery;
import com.exc.street.light.resource.qo.SlLampStrategyHistoryQuery;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 *
 */
public interface DeviceStrategyHistoryService extends IService<DeviceStrategyHistory> {
    /**
     * 根据设备id查询历史策略集合
     * @param deviceId
     * @param request
     * @return
     */
    Result getHistoryStrategyList(Integer deviceId, HttpServletRequest request);

    /**
     * 根据设备及策略id取最新一条数据
     * @param deviceId
     * @param strategyId
     * @return
     */
    DeviceStrategyHistory selectNewOne(Integer deviceId, Integer strategyId);

    /**
     * 根据设备id集合获取最新数据集合
     * @param deviceIdList
     * @return
     */
    List<DeviceStrategyHistory> selectLastList(List<Integer> deviceIdList);

    /**
     * 历史策略分页条件查询
     * @param strategyHistoryQuery
     * @param request
     * @return
     */
    Result getPage(SlLampStrategyHistoryQuery strategyHistoryQuery, HttpServletRequest request);

    /**
     * 查询该策略下所下发的设备
     * @param deviceHistoryQuery
     * @param request
     * @return
     */
    Result getHistoryDeviceList(SlLampDeviceHistoryQuery deviceHistoryQuery, HttpServletRequest request);

    /**
     * 修改重发的历史策略记录信息
     * @param historyIdList
     * @param request
     * @return
     */
    Result updateHistoryDeviceList(List<Integer> historyIdList, HttpServletRequest request);
}