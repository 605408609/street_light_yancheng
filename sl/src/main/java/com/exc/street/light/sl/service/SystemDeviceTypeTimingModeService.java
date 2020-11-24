/**
 * @filename:SystemDeviceTypeTimingModeService 2020-08-26
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.SystemDeviceTypeTimingMode;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface SystemDeviceTypeTimingModeService extends IService<SystemDeviceTypeTimingMode> {

    /**
     * 设备类型支持的定时方式
     * @param deviceTypeIdList
     * @param idSynchro
     * @param request
     * @return
     */
    Result deviceTypeTimingModeByIdList(List<Integer> deviceTypeIdList, Integer idSynchro, HttpServletRequest request);
}