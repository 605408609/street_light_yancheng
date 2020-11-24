/**
 * @filename:SystemAreaParameterController 2020-08-31
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.web;

import com.exc.street.light.dlm.service.SystemAreaParameterService;
import com.exc.street.light.resource.core.Result;
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
 * @time    2020-08-31
 *
 */
@Api(tags = "区域参数控制器")
@RestController
@RequestMapping("/api/dlm/system/area/parameter")
public class SystemAreaParameterController {

    @Autowired
    private SystemAreaParameterService systemAreaParameterService;

    @GetMapping
    @ApiOperation(value = "区域参数信息", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "view:data")
    public Result detailOfAreaParameter(HttpServletRequest request) {
        return systemAreaParameterService.detailOfAreaParameter(request);
    }
	
}