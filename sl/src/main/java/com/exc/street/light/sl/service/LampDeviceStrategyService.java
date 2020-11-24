/**
 * @filename:LampDeviceStrategyService 2020-03-24
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampDeviceStrategy;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface LampDeviceStrategyService extends IService<LampDeviceStrategy> {

    /**
     * 灯具设备与策略中间关系集合
     *
     * @param lampDeviceIdList
     * @param lampStrategyIdList
     * @param request
     * @return
     */
    Result list(List<Integer> lampDeviceIdList, List<Integer> lampStrategyIdList, HttpServletRequest request);

    /**
     * 根据设备id查询当前正在使用的策略
     * @param deviceId
     * @return
     */
    Result usedStrategyById(Integer deviceId);
}