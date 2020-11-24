/**
 * @filename:LampLightModeService 2020-08-27
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampLightMode;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface LampLightModeService extends IService<LampLightMode> {

    /**
     * 亮灯方式下拉列表
     * @param request
     * @return
     */
    Result listLightModeWithOptionQuery(HttpServletRequest request);
}