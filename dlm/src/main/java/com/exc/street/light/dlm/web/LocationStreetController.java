/**
 * @filename:LocationStreetController 2020-03-16
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exc.street.light.dlm.service.LocationStreetService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationStreet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>街道控制器</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-03-16
 *
 */
@Api(tags = "街道控制器", value = "街道控制器")
@RestController
@RequestMapping("/api/dlm/location/street")
public class LocationStreetController {

    @Autowired
    private LocationStreetService locationStreetService;

    /**
     * @explain 查询街道下拉列表
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/pulldown")
    @ApiOperation(value = "查询街道下拉列表", notes = "查询街道下拉列表,作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result pulldown(@RequestParam(required = false) Integer areaId,
                           @RequestParam(required = false) String streetName,
                           HttpServletRequest request) {
        return locationStreetService.pulldown(areaId,streetName,request);
    }

    /**
     * @explain 添加街道
     * @author  Longshuangyang
     * @time    2020-03-25
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加街道", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:location:street:add")
    @SystemLog(logModul = "设备位置管理",logType = "添加",logDesc = "添加街道")
    public Result insert(@ApiParam @RequestBody LocationStreet locationStreet, HttpServletRequest request){
        return locationStreetService.add(locationStreet,request);
    }

    /**
     * @explain 修改街道
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改街道", notes = "修改街道,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:location:street:update")
    @SystemLog(logModul = "设备位置管理",logType = "修改",logDesc = "修改街道")
    public Result updateStreet(@ApiParam @RequestBody LocationStreet locationStreet, HttpServletRequest request) {
        return locationStreetService.updateStreet(locationStreet, request);
    }

    /**
     * @explain 根据区域id集合查询街道集合
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/by/area")
    @ApiOperation(value = "根据区域id集合查询街道集合", notes = "根据区域id集合查询街道集合,作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result getByArea(@RequestParam(required = false) List<Integer> areaIdList,
                           HttpServletRequest request) {
        return locationStreetService.getByArea(areaIdList,request);
    }

    /**
     * @explain 删除街道
     * @author  Longshuangyang
     * @time    2020-03-16
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除街道", notes = "作者：Longshuangyang")
    @ApiImplicitParam(paramType="path", name = "id", value = "街道id", required = true, dataType = "Long")
    @RequiresPermissions(value = "dm:module:location:street:delete")
    @SystemLog(logModul = "设备位置管理",logType = "删除",logDesc = "删除街道")
    public Result deleteById(@PathVariable("id") Long id,  HttpServletRequest request){
        return locationStreetService.delete(id, request);
    }

    /**
     * @explain 街道验证唯一性
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/unique")
    @ApiOperation(value = "街道验证唯一性", notes = "街道验证唯一性,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:location:street:add")
    @SystemLog(logModul = "设备位置管理",logType = "验证唯一性",logDesc = "街道验证唯一性")
    public Result unique(@ApiParam LocationStreet locationStreet, HttpServletRequest request) {
        return locationStreetService.unique(locationStreet, request);
    }

    /**
     * @explain 根据id查询街道信息
     * @author  Huangjinhao
     * @time    2020-03-16
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询街道信息", notes = "作者：Huangjinhao")
    @ApiImplicitParam(paramType="path", name = "id", value = "街道id", required = true, dataType = "Long")
    @RequiresPermissions(value = "dm:module:location")
    public Result get(@PathVariable("id") Long id,  HttpServletRequest request){
        return locationStreetService.get(id, request);
    }

}