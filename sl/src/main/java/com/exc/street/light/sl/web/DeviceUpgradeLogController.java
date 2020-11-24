/**
 * @filename:DeviceUpgradeLogController 2020-08-25
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.sl.service.DeviceUpgradeLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： 升级记录表Controller</P>
 * @version: V1.0
 * @author: Huang Jin Hao
 * @time    2020-08-25
 *
 */
@Api(tags = "升级记录表",value="升级记录表" )
@RestController
@RequestMapping("/api/sl/device/upgrade/log")
public class DeviceUpgradeLogController {

    @Autowired
    DeviceUpgradeLogService deviceUpgradeLogService;

    /**
     * @explain 升级记录条件查询
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/pulldown")
    @ApiOperation(value = "升级记录分页条件查询", notes = "升级记录分页条件查询,作者：Huangjinhao")
    @RequiresPermissions(value = "permission:module:firmware:update:detail")
    public Result getPage(Integer isSuccess,Integer pageNum,Integer pageSize,HttpServletRequest request) {
        return deviceUpgradeLogService.pulldown(isSuccess,pageNum,pageSize,request);
    }

    /**
     * @explain 删除升级记录
     * @author hjh
     * @time 2020-03-23
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除升级记录", notes = "删除升级记录,作者：Huangjinhao")
    @RequiresPermissions(value = "permission:module:firmware:update:delete")
    @SystemLog(logModul = "运维管理",logType = "删除",logDesc = "删除升级记录")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        return deviceUpgradeLogService.delete(id, request);
    }

}