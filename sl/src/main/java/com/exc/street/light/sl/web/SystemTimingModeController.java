/**
 * @filename:SystemTimingModeController 2020-08-27
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.sl.service.SystemTimingModeService;
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
 * @time    2020-08-27
 *
 */
@Api(tags = "定时方式控制器")
@RestController
@RequestMapping("/api/sl/system/timing/mode")
public class SystemTimingModeController {

    @Autowired
    private SystemTimingModeService systemTimingModeService;

    @ApiOperation(value="定时方式下拉列表", notes = "作者：xiezhipeng")
    @GetMapping("/option")
    @RequiresPermissions(value = "view:data")
    public Result listTimingModeWithOptionQuery(HttpServletRequest request) {
        return systemTimingModeService.listTimingModeWithOptionQuery(request);
    }

}