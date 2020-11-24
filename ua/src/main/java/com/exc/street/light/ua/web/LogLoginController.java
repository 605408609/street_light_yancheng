/**
 * @filename:LogLoginController 2020-06-09
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.LogLoginQueryObject;
import com.exc.street.light.ua.service.LogLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: xiezhipeng
 * @time    2020-06-09
 *
 */
@Api(tags = "登录日志")
@RestController
@RequestMapping("/api/ua/log/login")
public class LogLoginController {

    @Autowired
    private LogLoginService logLoginService;

    /**
     * 登录日志列表
     * @param request
     * @param queryObject
     * @return
     */
    @GetMapping
    @RequiresPermissions(value = "permission:module:login:log")
    @ApiOperation(value = "登录日志列表", notes = "作者：xiezhipeng")
    public Result getPages(HttpServletRequest request, LogLoginQueryObject queryObject) {
        return logLoginService.getPages(request, queryObject);
    }

}