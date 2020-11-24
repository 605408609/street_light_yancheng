/**
 * @filename:LampEnergyDayService 2020-03-28
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampEnergyDay;
import com.exc.street.light.resource.vo.req.SlReqEnergyStatisticslVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
public interface LampEnergyDayService extends IService<LampEnergyDay> {

    /**
     * 新增日能耗数据
     * @param dayTime
     * @return
     */
    Result addEnergy(String dayTime);

    /**
     * 根据创建时间查询日能耗信息集合
     * @return
     */
    Result selectByEnergy(String monthTime);

    /**
     * 根据创建时间查询对应设备当天能耗信息
     * @return
     */
    LampEnergyDay selectOneByTime(Integer deviceId,String dayTime);

    /**
     * 获取指定时间段的能耗数据
     * @param slReqEnergyStatisticslVO
     * @param request
     * @return
     */
    Result energy(SlReqEnergyStatisticslVO slReqEnergyStatisticslVO, HttpServletRequest request);
}