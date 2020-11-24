/**
 * @filename:LampEnergyYearService 2020-03-28
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.entity.sl.LampEnergyYear;
import com.baomidou.mybatisplus.extension.service.IService;
/**   
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
public interface LampEnergyYearService extends IService<LampEnergyYear> {

    /**
     * 根据创建时间查询对应设备当年能耗信息
     * @return
     */
    LampEnergyYear selectOneByTime(Integer deviceId, String yearTime);
}