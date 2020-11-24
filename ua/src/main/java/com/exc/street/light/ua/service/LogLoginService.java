/**
 * @filename:LogLoginService 2020-06-09
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.log.LogLogin;
import com.exc.street.light.resource.qo.LogLoginQueryObject;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:登录日志(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface LogLoginService extends IService<LogLogin> {

    /**
     * 登录日志列表
     * @param request
     * @param queryObject
     * @return
     */
    Result getPages(HttpServletRequest request, LogLoginQueryObject queryObject);
}