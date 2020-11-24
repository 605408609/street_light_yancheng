/**
 * @filename:LampDeviceStrategyController 2020-03-24
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.sl.service.LampDeviceStrategyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>路灯设备与策略关系控制器</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-03-24
 *
 */
@Api(tags = "路灯设备与策略关系控制器", value = "路灯设备与策略关系控制器")
@RestController
@RequestMapping("/api/sl/lamp/device/strategy")
public class LampDeviceStrategyController{

    @Autowired
    private LampDeviceStrategyService lampDeviceStrategyService;

    @PostMapping("/list")
    @ApiOperation(value = "设备和策略关系集合", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "sl:module:strategy:manage")
    public Result list(@ApiParam @RequestBody Map<String,List<Integer>> map, HttpServletRequest request) {
        List<Integer> lampDeviceIdList = map.get("lampDeviceIdList");
        List<Integer> lampStrategyIdList = map.get("lampStrategyIdList");
        return lampDeviceStrategyService.list(lampDeviceIdList, lampStrategyIdList,request);
    }
}