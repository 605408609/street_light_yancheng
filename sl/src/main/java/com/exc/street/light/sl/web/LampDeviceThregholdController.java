/**
 * @filename:LampDeviceThregholdController 2020-08-22
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.vo.req.SlReqThresholdAddListVO;
import com.exc.street.light.resource.vo.req.SlReqThresholdAddVO;
import com.exc.street.light.sl.service.LampDeviceThregholdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： 设备阈值数据表Controller</P>
 * @version: V1.0
 * @author: Huang Jin Hao
 * @time    2020-08-22
 *
 */
@Api(tags = "设备阈值数据表",value="设备阈值数据表" )
@RestController
@RequestMapping("/api/sl/lamp/device/threghold")
public class LampDeviceThregholdController {

    @Autowired
    LampDeviceThregholdService lampDeviceThregholdService;

    /**
     * @explain 设备阈值详情
     * @author Huangjinhao
     * @time 2020-08-22
     */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "设备阈值详情", notes = "设备阈值详情,作者：Huangjinhao")
    @RequiresPermissions(value = "permission:module:alarm:threshold:detail")
    public Result detail(@PathVariable Integer id, HttpServletRequest request) {
        return lampDeviceThregholdService.detail(id,request);
    }

    /**
     * @explain 添加设备阈值
     * @author Huangjinhao
     * @time 2020-08-24
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加设备阈值", notes = "作者：Huangjinhao")
    @RequiresPermissions(value = "permission:module:alarm:threshold:add")
    @SystemLog(logModul = "运维管理",logType = "添加",logDesc = "添加设备阈值")
    public Result add(@ApiParam @RequestBody SlReqThresholdAddListVO slReqThresholdAddListVO, HttpServletRequest request) {
        return lampDeviceThregholdService.add(slReqThresholdAddListVO,request);
    }

    /**
     * @explain 删除设备阈值
     * @author Huangjinhao
     * @time 2020-08-24
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除设备阈值", notes = "删除设备阈值,作者：Huangjinhao")
    @RequiresPermissions(value = "permission:module:alarm:threshold:delete")
    @SystemLog(logModul = "运维管理",logType = "删除",logDesc = "删除设备阈值")
    public Result delete(HttpServletRequest request) {
        return lampDeviceThregholdService.delete(request);
    }

    /**
     * @explain 修改设备阈值
     * @author Huangjinhao
     * @time 2020-08-24
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改设备阈值", notes = "修改设备阈值,作者：Huangjinhao")
    @RequiresPermissions(value = "permission:module:alarm:threshold:update")
    @SystemLog(logModul = "运维管理",logType = "修改",logDesc = "修改设备阈值")
    public Result updateDevice(@ApiParam @RequestBody List<SlReqThresholdAddVO> slReqThresholdAddVOList, HttpServletRequest request) {
        return lampDeviceThregholdService.updateThreghold(slReqThresholdAddVOList, request);
    }

}