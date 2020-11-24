/**
 * @filename:LampEnergyYearServiceImpl 2020-03-28
 * @project sl  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service.impl;

import com.exc.street.light.resource.entity.sl.LampEnergyYear;
import com.exc.street.light.sl.mapper.LampEnergyYearMapper;
import com.exc.street.light.sl.service.LampEnergyYearService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**   
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Service
public class LampEnergyYearServiceImpl  extends ServiceImpl<LampEnergyYearMapper, LampEnergyYear> implements LampEnergyYearService  {

    @Override
    public LampEnergyYear selectOneByTime(Integer deviceId, String yearTime) {
        LampEnergyYear lampEnergyYear = baseMapper.selectOneByTime(deviceId,yearTime);
        return lampEnergyYear;
    }
}