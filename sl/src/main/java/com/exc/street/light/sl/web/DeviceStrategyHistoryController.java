/**
 * @filename:DeviceStrategyHistoryController 2020-09-04
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.SlLampDeviceHistoryQuery;
import com.exc.street.light.resource.qo.SlLampStrategyHistoryQuery;
import com.exc.street.light.sl.service.DeviceStrategyHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: xiezhipeng
 * @time 2020-09-04
 *
 */
@Api(tags = "设备策略历史中间表")
@RestController
@RequestMapping("/api/sl/device/strategy/history")
public class DeviceStrategyHistoryController {

    @Autowired
    private DeviceStrategyHistoryService deviceStrategyHistoryService;

    @GetMapping
    @ApiOperation(value = "根据设备id查询历史策略集合", notes = "根据设备id查询历史策略集合,作者：xiezhipeng")
    @RequiresPermissions(value = "sl:module:strategy:manage:detail")
    public Result getHistoryStrategyList(@RequestParam("deviceId") Integer deviceId, HttpServletRequest request) {
        return deviceStrategyHistoryService.getHistoryStrategyList(deviceId, request);
    }

    @GetMapping("/page")
    @ApiOperation(value = "历史策略分页条件查询", notes = "历史策略分页条件查询,作者：xiezhipeng")
    @RequiresPermissions(value = "sl:module:strategy:manage:detail")
    public Result getPage(SlLampStrategyHistoryQuery strategyHistoryQuery, HttpServletRequest request) {
        return deviceStrategyHistoryService.getPage(strategyHistoryQuery, request);
    }

    @GetMapping("/devices")
    @ApiOperation(value = "查询该策略下所下发的设备", notes = "查询该策略下所下发的设备,作者：xiezhipeng")
    @RequiresPermissions(value = "sl:module:strategy:manage:detail")
    public Result getHistoryDeviceList(SlLampDeviceHistoryQuery deviceHistoryQuery, HttpServletRequest request) {
        return deviceStrategyHistoryService.getHistoryDeviceList(deviceHistoryQuery, request);
    }

    @GetMapping("/ids")
    @ApiOperation(value = "修改重发的历史策略记录信息", notes = "修改重发的历史策略记录信息,作者：xiezhipeng")
    @RequiresPermissions(value = "sl:module:strategy:manage:detail")
    public Result updateHistoryDeviceList(@RequestParam("historyIdList") List<Integer> historyIdList, HttpServletRequest request) {
        return deviceStrategyHistoryService.updateHistoryDeviceList(historyIdList, request);
    }

}