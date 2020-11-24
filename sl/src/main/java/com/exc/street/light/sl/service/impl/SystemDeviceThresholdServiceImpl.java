/**
 * @filename:SystemDeviceThresholdServiceImpl 2020-08-22
 * @project sl  V1.0
 * Copyright(c) 2018 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.exc.street.light.resource.entity.sl.SystemDeviceThreshold;
import com.exc.street.light.sl.mapper.SystemDeviceThresholdMapper;
import com.exc.street.light.sl.service.SystemDeviceThresholdService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**   
 * @Description:TODO(设备阈值表服务实现)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Service
public class SystemDeviceThresholdServiceImpl  extends ServiceImpl<SystemDeviceThresholdMapper, SystemDeviceThreshold> implements SystemDeviceThresholdService  {

    @Override
    public SystemDeviceThreshold getOneByFiled(String filed,Integer deviceTypeId) {
        QueryWrapper<SystemDeviceThreshold> queryWrapper = new QueryWrapper<>();
        SystemDeviceThreshold systemDeviceThreshold = new SystemDeviceThreshold();
        if(filed!=null){
            queryWrapper.eq("filed",filed);
            queryWrapper.eq("device_type_id",deviceTypeId);
            systemDeviceThreshold = baseMapper.selectOne(queryWrapper);
        }
        return systemDeviceThreshold;
    }

    @Override
    public List<SystemDeviceThreshold> getListByDeviceTypeId(Integer deviceTypeId) {
        QueryWrapper<SystemDeviceThreshold> queryWrapper = new QueryWrapper<>();
        List<SystemDeviceThreshold> systemDeviceThresholdList = new ArrayList<>();
        if(deviceTypeId!=null){
            queryWrapper.eq("device_type_id",deviceTypeId);
            systemDeviceThresholdList = baseMapper.selectList(queryWrapper);
        }
        return systemDeviceThresholdList;
    }
}