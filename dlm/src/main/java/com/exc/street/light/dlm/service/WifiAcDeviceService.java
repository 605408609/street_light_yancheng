/**
 * @filename:WifiApDeviceService 2020-03-16
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.wifi.WifiAcDevice;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;

import java.util.List;

/**
 * @Description: AP设备(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface WifiAcDeviceService extends IService<WifiAcDevice> {

    /**
     * 批量插入
     * @param wifiAcDeviceList
     * @return
     */
    Result addList(List<WifiAcDevice> wifiAcDeviceList);

    /**
     * 查询所有
     */
    Result selectAll();
}