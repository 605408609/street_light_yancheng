/**
 * @filename:LampEnergyService 2020-03-20
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampEnergy;
import com.exc.street.light.resource.vo.req.SlReqEnergyStatisticslVO;
import com.exc.street.light.resource.vo.req.SlReqLightControlVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 *
 */
public interface LampEnergyService extends IService<LampEnergy> {

    /**
     * 累计计算日、月、年能耗数据
     */
    Result cumulativeEnergyByTime(Integer deviceId,Float energy);

    /**
     * 获取指定时间段的能耗数据
     * @param slReqEnergyStatisticslVO
     * @param request
     * @return
     */
    //Result energy(SlReqEnergyStatisticslVO slReqEnergyStatisticslVO, HttpServletRequest request);

    /**
     * 统计能耗相关数据
     * @param request
     * @return
     */
    Result energyInformation(HttpServletRequest request);

    /**
     * 获取指定deviceId和energyTime的能耗
     * @param deviceId
     * @param energyTime
     * @return
     */
    Result select(Integer deviceId,String energyTime);

    /**
     * 根据energyTime删除能耗数据
     * @param energyTime
     * @return
     */
    Result deleteByEnergyTime(String energyTime);

    /**
     * 获取近七天的能耗
     * @param request
     * @return
     */
    Result weekEnergy(HttpServletRequest request);


    /**
     *  获取近七天的亮灯率
     * @param request
     * @return
     */
    Result weekLightingRate(HttpServletRequest request);

}