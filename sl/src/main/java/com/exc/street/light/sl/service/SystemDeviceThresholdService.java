/**
 * @filename:SystemDeviceThresholdService 2020-08-22
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.entity.sl.SystemDeviceThreshold;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description:TODO(设备阈值表服务层)
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
public interface SystemDeviceThresholdService extends IService<SystemDeviceThreshold> {

    /**
     * 根据阈值字段查询阈值信息
     * @param filed
     * @return
     */
    SystemDeviceThreshold getOneByFiled(String filed,Integer deviceTypeId);

    List<SystemDeviceThreshold> getListByDeviceTypeId(Integer deviceTypeId);
}