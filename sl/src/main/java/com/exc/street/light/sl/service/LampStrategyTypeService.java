/**
 * @filename:LampStrategyTypeService 2020-03-20
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampStrategyType;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface LampStrategyTypeService extends IService<LampStrategyType> {

    /**
     * 查询策略类型下拉列表
     *
     * @param request
     * @return
     */
    Result pulldown(HttpServletRequest request);
}