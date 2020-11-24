/**
 * @filename:LampStrategyService 2020-08-26
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.DeviceStrategyHistory;
import com.exc.street.light.resource.entity.sl.LampStrategy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.sl.LampStrategyAction;
import com.exc.street.light.resource.entity.sl.SystemDevice;
import com.exc.street.light.resource.qo.SlLampStrategyQuery;
import com.exc.street.light.resource.vo.req.SlReqLampStrategyExecuteVO;
import com.exc.street.light.resource.vo.req.SlReqStrategyVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 灯具策略(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface LampStrategyService extends IService<LampStrategy> {

    /**
     * 新增策略
     * @param slReqStrategyVO
     * @param request
     * @return
     */
    Result insertLampStrategy(SlReqStrategyVO slReqStrategyVO, HttpServletRequest request);

    /**
     * 编辑策略
     * @param slReqStrategyVO
     * @param request
     * @return
     */
    Result updateLampStrategy(SlReqStrategyVO slReqStrategyVO, HttpServletRequest request);

    /**
     * 策略详情
     * @param strategyId
     * @param request
     * @return
     */
    Result getLampStrategy(Integer strategyId, HttpServletRequest request);

    /**
     * 删除策略
     * @param strategyId
     * @param request
     * @return
     */
    Result deleteLampStrategy(Integer strategyId, HttpServletRequest request);

    /**
     * 灯控策略分页条件查询
     * @param slLampStrategyQuery
     * @param request
     * @return
     */
    Result getPage(SlLampStrategyQuery slLampStrategyQuery, HttpServletRequest request);

    /**
     * 下发策略
     * @param request
     * @param slReqLampStrategyExecuteVO
     * @return
     */
    Result execute(HttpServletRequest request, SlReqLampStrategyExecuteVO slReqLampStrategyExecuteVO);

    /**
     * 更新设备当前绑定的策略
     * @param systemDeviceIdList
     * @param strategyId
     */
    void updateDeviceAndStrategy(List<Integer> systemDeviceIdList, Integer strategyId);

    /**
     * 更新设备当前绑定的策略
     * @param deviceStrategyHistoryIdList
     */
    void updateDeviceAndStrategy(List<Integer> deviceStrategyHistoryIdList);

    /**
     * 自研灯具下发策略
     * @param systemDeviceList
     * @param lampStrategyActionList
     * @param scene
     * @return
     */
    Result singleLampExecute(List<SystemDevice> systemDeviceList, List<LampStrategyAction> lampStrategyActionList,Integer scene,List<DeviceStrategyHistory> deviceStrategyHistoryList);
}