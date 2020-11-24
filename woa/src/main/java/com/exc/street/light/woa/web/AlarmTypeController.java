/**
 * @filename:AlarmTypeController 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.woa.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.woa.service.AlarmTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>告警类型控制器</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time    2020-03-28
 *
 */
@Api(tags = "告警类型控制器",value="告警类型控制器" )
@RestController
@RequestMapping("/api/woa/alarm/type")
public class AlarmTypeController {

    @Autowired
    private AlarmTypeService alarmTypeService;

    /**
     * @explain 告警类型下拉列表
     * @author Longshuangyang
     * @time 2020-03-25
     */
    @GetMapping("/pulldown")
    @ApiOperation(value = "告警类型下拉列表", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "permission:module:alarm:list")
    public Result pulldown(HttpServletRequest httpServletRequest) {
        return alarmTypeService.pulldown(httpServletRequest);
    }

}