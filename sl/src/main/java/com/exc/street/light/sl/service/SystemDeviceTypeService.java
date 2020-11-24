/**
 * @filename:SystemDeviceTypeService 2020-08-22
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.entity.sl.SystemDeviceType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: (设备类型表服务层)
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
public interface SystemDeviceTypeService extends IService<SystemDeviceType> {

    /**
     * 根据设备类型id集合查询支持该设备类型的策略集合
     * @param deviceTypeIdList
     * @param size
     * @return
     */
    List<Integer> getStrategyIdListByDeviceTypeIdList(List<Integer> deviceTypeIdList, Integer size);

}