/**
 * @filename:LampEnergyMonthService 2020-03-28
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampEnergyMonth;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface LampEnergyMonthService extends IService<LampEnergyMonth> {

    /**
     * 首页月度能耗数据
     *
     * @param request
     * @return
     */
    Result monthly(HttpServletRequest request);

    /**
     * 新增月能耗数据
     * @param monthTime
     * @return
     */
    Result addEnergy(String monthTime);

    /**
     * 根据创建时间查询月能耗信息集合
     * @return
     */
    Result selectByEnergy(List<Integer> deviceIdList,String yearTime);

    /**
     * 根据创建时间查询对应设备当月能耗信息
     * @return
     */
    LampEnergyMonth selectOneByTime(Integer deviceId, String monthTime);
}