/**
 * @filename:ControlLoopDeviceController 2020-08-24
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.web;

import com.exc.street.light.dlm.service.ControlLoopDeviceService;
import com.exc.street.light.resource.core.Result;
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
 * @time    2020-08-24
 *
 */
@Api(tags = "集中控制器分组或回路与设备")
@RestController
@RequestMapping("/api/dlm/control/loop/device")
public class ControlLoopDeviceController {

    @Autowired
    private ControlLoopDeviceService controlLoopDeviceService;

    @GetMapping("/by/loop")
    @ApiOperation(value = "根据分组id集合查询关联表", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "view:data")
    public Result getControlLoopDeviceByLoopIdList(@RequestParam(required = false) List<Integer> loopIdList, HttpServletRequest request) {
        return controlLoopDeviceService.getControlLoopDeviceByLoopIdList(loopIdList, request);
    }

}