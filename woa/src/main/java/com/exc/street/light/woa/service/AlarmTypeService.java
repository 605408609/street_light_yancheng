/**
 * @filename:AlarmTypeService 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.woa.AlarmType;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface AlarmTypeService extends IService<AlarmType> {

    /**
     * 告警类型下拉列表
     *
     * @param httpServletRequest
     * @return
     */
    Result pulldown(HttpServletRequest httpServletRequest);
}