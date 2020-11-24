/**
 * @filename:PermissionController 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.ua.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: xiezhipeng
 * @time    2020-03-12
 *
 */
@Api(tags = "权限接口")
@RestController
@RequestMapping("/api/ua/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 权限树
     * @param request
     * @return
     */
    @GetMapping
    @RequiresPermissions(value = "permission:module:user")
    @ApiOperation(value = "权限树", notes = "作者：xiezhipeng")
    public Result selectAll(HttpServletRequest request) {
        return permissionService.selectAll(request);
    }

    /**
     * 根据创建人id查询权限信息
     * @param founderId
     * @return
     */
    @GetMapping("/founder/id")
    @RequiresPermissions(value = "permission:module:user")
    @ApiOperation(value = "根据创建人id查询权限信息", notes = "作者：xiezhipeng")
    public Result selectByFounderId(@RequestParam("founderId") Integer founderId, HttpServletRequest request) {
        return permissionService.selectByFounderId(founderId, request);
    }
}