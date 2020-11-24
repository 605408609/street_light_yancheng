/**
 * @filename:SystemAreaParameterService 2020-08-31
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.SystemAreaParameter;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface SystemAreaParameterService extends IService<SystemAreaParameter> {

    /**
     * 区域参数信息
     * @param request
     * @return
     */
    Result detailOfAreaParameter(HttpServletRequest request);
}