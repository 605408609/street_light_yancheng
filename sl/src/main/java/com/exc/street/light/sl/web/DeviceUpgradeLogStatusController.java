/**
 * @filename:DeviceUpgradeLogStatusController 2020-08-25
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.sl.service.DeviceUpgradeLogStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： 设备升级状态表Controller</P>
 * @version: V1.0
 * @author: Huang Jin Hao
 * @time    2020-08-25
 *
 */
@Api(tags = "设备升级状态表",value="设备升级状态表" )
@RestController
@RequestMapping("/api/sl/device/upgrade/log/status")
public class DeviceUpgradeLogStatusController {

    @Autowired
    DeviceUpgradeLogStatusService deviceUpgradeLogStatusService;

    /**
     * ota升级
     * @param request
     * @param file
     * @return
     */
    @PostMapping("/ota")
    @ApiOperation(value = "ota升级", notes = "ota升级,作者：Huangjinahao")
    @RequiresPermissions(value = "permission:module:firmware:update:add")
    @SystemLog(logModul = "运维管理",logType = "添加",logDesc = "ota升级")
    public Result ota(HttpServletRequest request, @RequestParam(required = true) MultipartFile file, @RequestParam(required = true) String deviceIdList) {
        return deviceUpgradeLogStatusService.upgradeStatus(file,deviceIdList,request);
    }

     /**
     * ota重新升级
     * @param request
     * @param logId
     * @return
     */
    @PostMapping("/otaAgain")
    @ApiOperation(value = "ota升级", notes = "ota升级,作者：Huangjinahao")
    @RequiresPermissions(value = "permission:module:firmware:update:update")
    @SystemLog(logModul = "运维管理",logType = "添加",logDesc = "ota重新升级")
    public Result otaAgain(HttpServletRequest request, @RequestParam(required = true) Integer logId) {
        return deviceUpgradeLogStatusService.upgradeStatusAgain(logId,request);
    }

}