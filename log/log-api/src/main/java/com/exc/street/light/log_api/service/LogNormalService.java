/**
 * @filename:LogNormalService 2020-05-08
 * @project log  V1.0
 * Copyright(c) 2020 xuJiaHao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.log_api.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.log.LogNormal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.qo.LogDataQueryObject;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xuJiaHao
 * 
 */
public interface LogNormalService extends IService<LogNormal> {
    Result getPage(LogDataQueryObject logDataQueryObject, HttpServletRequest request);
}