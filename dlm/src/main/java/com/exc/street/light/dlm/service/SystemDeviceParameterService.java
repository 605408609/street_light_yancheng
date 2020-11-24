/**
 * @filename:SystemDeviceParameterService 2020-09-03
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.sl.SystemDeviceParameter;

import java.util.Map;

/**
 * @Description:TODO(设备参数表服务层)
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
public interface SystemDeviceParameterService extends IService<SystemDeviceParameter> {

    /**
     * 根据类型获取参数字段和对应field id
     * @param deviceType 设备类型id
     * @return map.key:field  map.value:id
     */
    Map<String,Integer> selectFieldByType(Integer deviceType);

    SystemDeviceParameter selectByName(Integer deviceTypeId, String name);

}