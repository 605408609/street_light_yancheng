/**
 * @filename:SystemDeviceTypeTimingModeController 2020-08-26
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.sl.service.SystemDeviceTypeTimingModeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: xiezhipeng
 * @time    2020-08-26
 *
 */
@Api(tags = "设备类型支持的定时方式")
@RestController
@RequestMapping("/api/sl/system/device/type/timing/mode")
public class SystemDeviceTypeTimingModeController {

    @Autowired
    private SystemDeviceTypeTimingModeService systemDeviceTypeTimingModeService;

    @GetMapping
    @ApiOperation(value = "设备类型支持的定时方式", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "view:data")
    public Result deviceTypeTimingModeByIdList(@RequestParam("deviceTypeIdList") List<Integer> deviceTypeIdList, @RequestParam("idSynchro") Integer idSynchro, HttpServletRequest request) {
        return systemDeviceTypeTimingModeService.deviceTypeTimingModeByIdList(deviceTypeIdList, idSynchro, request);
    }

}