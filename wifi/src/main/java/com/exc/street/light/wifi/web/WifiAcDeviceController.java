/**
 * @filename:WifiAcDeviceController 2020-03-27
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.wifi.web;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.wifi.WifiAcDevice;
import com.exc.street.light.wifi.qo.AcDeviceQueryObject;
import com.exc.street.light.wifi.service.WifiAcDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: xiezhipeng
 * @time    2020-03-27
 *
 */
@Api(tags = "ac设备接口")
@RestController
@RequestMapping("/api/wifi/wifi/ac/device")
public class WifiAcDeviceController {

    @Autowired
    private WifiAcDeviceService wifiAcDeviceService;

    /**
     * 新增ac设备
     * @param wifiAcDevice
     * @param request
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增ac设备", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:same:device:ac:add")
    @SystemLog(logModul = "设备管理",logType = "新增",logDesc = "新增ac设备")
    public Result insert(@ApiParam @RequestBody WifiAcDevice wifiAcDevice, HttpServletRequest request){
        return wifiAcDeviceService.add(wifiAcDevice,request);
    }

    /**
     * 编辑ac设备
     * @param wifiAcDevice
     * @param request
     * @return
     */
    @PutMapping
    @ApiOperation(value = "编辑ac设备", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:same:device:ac:update")
    @SystemLog(logModul = "设备管理",logType = "编辑",logDesc = "编辑ac设备")
    public Result update(@ApiParam @RequestBody WifiAcDevice wifiAcDevice, HttpServletRequest request){
        return wifiAcDeviceService.modify(wifiAcDevice, request);
    }

    /**
     * 删除ac设备
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除ac设备", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:same:device:ac:delete")
    @SystemLog(logModul = "设备管理",logType = "删除",logDesc = "删除ac设备")
    @ApiImplicitParam(paramType = "path", name = "id", value = "ap设备id", required = true, dataType = "Integer")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        return wifiAcDeviceService.delete(id, request);
    }

    /**
     * AC设备详情
     * @param id
     * @return
     */
    @GetMapping("get/{id}")
    @ApiOperation(value = "AC设备详情", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:same:device:ac:detail")
    @ApiImplicitParam(paramType = "path", name = "id", value = "AC设备id", required = true, dataType = "Integer")
    public Result get(@PathVariable Integer id, HttpServletRequest request) {
        return wifiAcDeviceService.get(id, request);
    }

    /**
     * AC设备列表
     * @param request
     * @param queryObject
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "AC设备列表", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:same:device:ac")
    public Result list(HttpServletRequest request, AcDeviceQueryObject queryObject) {
        return wifiAcDeviceService.getList(request, queryObject);
    }

    /**
     * 批量删除
     * @param request
     * @param ids
     * @return
     */
    @DeleteMapping("/batch/delete")
    @ApiOperation(value = "批量删除", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:same:device:ac:delete")
    @SystemLog(logModul = "设备管理",logType = "删除",logDesc = "批量删除ac设备")
    public Result batchDelete(HttpServletRequest request, String ids) {
        return wifiAcDeviceService.batchDelete(request, ids);
    }

    /**
     * 查询所有ac设备
     * @return
     */
    @GetMapping("/ac/ip")
    @ApiOperation(value = "归属ac的ip下拉框", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = {"dm:module:same:device:ac", "dm:module:same:device:ap"}, logical = Logical.OR)
    public Result getAcIpList(HttpServletRequest request) {
        return wifiAcDeviceService.getAcIpList(request);
    }

    /**
     * AC设备名称和编号的唯一性校验, 0:不存在 1：设备名称已存在 2：设备编号已存在
     * @param id
     * @param name
     * @param num
     * @return
     */
    @GetMapping("/name/num/uniqueness")
    @RequiresPermissions(value = "dm:module:same:device:ac")
    @ApiOperation(value = "AC设备名称和编号的唯一性校验", notes = "0:不存在 1：AC设备名称已存在 2；AC设备编号已存在；作者：xiezhipeng")
    public Result nameUniqueness(@RequestParam(required = false) Integer id, @RequestParam("name") String name, @RequestParam("num") String num) {
        return wifiAcDeviceService.nameUniqueness(id, name, num);
    }

    /**
     * AC设备编号的唯一性校验, 0:不存在 1：已存在
     * @param id
     * @param num
     * @return
     */
//    @GetMapping("/num/uniqueness")
//    @ApiOperation(value = "AC设备编号的唯一性校验", notes = "0:不存在 1：已存在；作者：xiezhipeng")
//    public Result numUniqueness(@RequestParam(required = false) Integer id, @RequestParam("num") String num) {
//        return wifiAcDeviceService.numUniqueness(id, num);
//    }

}