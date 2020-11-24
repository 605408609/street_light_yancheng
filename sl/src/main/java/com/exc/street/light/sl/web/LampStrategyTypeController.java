/**
 * @filename:LampStrategyTypeController 2020-03-20
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.sl.service.LampStrategyTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>灯具策略类型控制器</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time    2020-03-20
 *
 */
@Api(tags = "灯具策略类型控制器",value="灯具策略类型控制器" )
@RestController
@RequestMapping("/api/sl/lamp/strategy/type")
public class LampStrategyTypeController {

    @Autowired
    private LampStrategyTypeService lampStrategyTypeService;

    /**
     * @explain 查询策略类型下拉列表
     * @author Longshuangyang
     * @time 2020-03-23
     */
    @GetMapping("/pulldown")
    @ApiOperation(value = "查询策略类型下拉列表", notes = "查询策略类型下拉列表,作者：Longshuangyang")
    @RequiresPermissions(value = "sl:module:strategy:manage")
    public Result pulldown(HttpServletRequest request) {
        return lampStrategyTypeService.pulldown(request);
    }

}