/**
 * @filename:WifiApDeviceController 2020-03-16
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.wifi.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import com.exc.street.light.wifi.qo.ApDeviceQueryObject;
import com.exc.street.light.wifi.service.WifiApDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: xiezhipeng
 * @time    2020-03-16
 *
 */
@Api(tags = "ap设备接口")
@RestController
@RequestMapping("/api/wifi/wifi/ap/device")
public class WifiApDeviceController {

    @Autowired
    private WifiApDeviceService wifiApDeviceService;

    /**
     * 新增ap设备
     * @param wifiApDevice
     * @param request
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增ap设备", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:same:device:ap:add")
    @SystemLog(logModul = "设备管理",logType = "新增",logDesc = "新增ap设备")
    public Result insert(@ApiParam @RequestBody WifiApDevice wifiApDevice, HttpServletRequest request){
        return wifiApDeviceService.add(wifiApDevice,request);
    }

    /**
     * 编辑ap设备
     * @param wifiApDevice
     * @param request
     * @return
     */
    @PutMapping
    @ApiOperation(value = "编辑ap设备", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:same:device:ap:update")
    @SystemLog(logModul = "设备管理",logType = "编辑",logDesc = "编辑ap设备")
    public Result update(@ApiParam @RequestBody WifiApDevice wifiApDevice, HttpServletRequest request){
        return wifiApDeviceService.modify(wifiApDevice, request);
    }

    /**
     * 删除ap设备
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除ap设备", notes = "作者：xiezhipeng")
    @ApiImplicitParam(paramType = "path", name = "id", value = "ap设备id", required = true, dataType = "Integer")
    @RequiresPermissions(value = "dm:module:same:device:ap:delete")
    @SystemLog(logModul = "设备管理",logType = "删除",logDesc = "删除ap设备")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        return wifiApDeviceService.delete(id, request);
    }

    /**
     * AP设备详情
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    @ApiOperation(value = "AP设备详情", notes = "作者：xiezhipeng")
    @ApiImplicitParam(paramType = "path", name = "id", value = "AP设备id", required = true, dataType = "Integer")
    @RequiresPermissions(value = "dm:module:same:device:ap:detail")
    public Result get(@PathVariable Integer id, HttpServletRequest request) {
        return wifiApDeviceService.get(id, request);
    }

    /**
     * AP设备列表
     * @param request
     * @param queryObject
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "AP设备列表", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:same:device:ap")
    public Result list(HttpServletRequest request, ApDeviceQueryObject queryObject) {
        return wifiApDeviceService.getList(request, queryObject);
    }

    /**
     * 批量删除
     * @param request
     * @param ids
     * @return
     */
    @DeleteMapping("/batch/delete")
    @ApiOperation(value = "批量删除", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:same:device:ap:delete")
    @SystemLog(logModul = "设备管理",logType = "删除",logDesc = "批量删除ap设备")
    public Result batchDelete(HttpServletRequest request, String ids) {
        return wifiApDeviceService.batchDelete(request, ids);
    }

    /**
     * 网络状态下拉框
     * @return
     */
    @GetMapping("/network/status")
    @ApiOperation(value = "网络状态下拉框", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:same:device:ap")
    public Result getNetworkStatusList() {
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "离线");
        map.put(1, "在线");
        Result result = new Result();
        return result.success(map);
    }

    /**
     * 查询AP(根据灯杆id集合)
     * @return
     */
    @GetMapping("/pulldown/by/lamp/post")
    @ApiOperation(value = "查询所有AP(根据灯杆id集合)", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:same:device:ap")
    public Result pulldownByLampPost(@RequestParam(required = false) List<Integer> lampPostIdList, HttpServletRequest request) {
        return wifiApDeviceService.pulldownByLampPost(lampPostIdList, request);
    }

    /**
     * @explain 查询灯具(根据灯杆id集合)
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PostMapping("/pulldown/by/lamp/post")
    @RequiresPermissions(value = "dm:module:same:device:ap")
    @ApiOperation(value = "查询灯具(根据灯杆id集合)", notes = "查询灯具(根据灯杆id集合),作者：Longshuangyang")
    public Result pulldownByLampPostGet(@RequestBody List<Integer> lampPostIdList, HttpServletRequest request) {
        return wifiApDeviceService.pulldownByLampPost(lampPostIdList, request);
    }

    /**
     * AP设备名称和编号唯一性校验, 0:不存在 1：已存在
     * @param id
     * @param name
     * @param num
     * @return
     */
    @GetMapping("/name/num/uniqueness")
    @RequiresPermissions(value = "dm:module:same:device:ap")
    @ApiOperation(value = "AP设备名称和编号唯一性校验", notes = "0:不存在 1：设备名称已存在 2:设备编号已存在;作者：xiezhipeng")
    public Result nameUniqueness(@RequestParam(required = false) Integer id, @RequestParam("name") String name, @RequestParam("num") String num) {
        return wifiApDeviceService.nameUniqueness(id, name, num);
    }

    /**
     * AP设备编号唯一性校验, 0:不存在 1：已存在
     * @param id
     * @param num
     * @return
     */
//    @GetMapping("num/uniqueness")
//    @ApiOperation(value = "AP设备编号唯一性校验", notes = "0:不存在 1：已存在;作者：xiezhipeng")
//    public Result numUniqueness(@RequestParam(required = false) Integer id, @RequestParam("num") String num) {
//        return wifiApDeviceService.numUniqueness(id, num);
//    }

    /**
     * 查询所有ap设备
     * @param name
     * @return
     */
    @GetMapping("/all")
    @ApiOperation(value = "查询所有ap设备", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:same:device:ap")
    public Result getAllApList(String name) {
        Result result = new Result();
        return result.success(wifiApDeviceService.list(new LambdaQueryWrapper<WifiApDevice>().like(name != null ,WifiApDevice::getName, name)));
    }

    /**
     * AP设备简单信息，app端使用
     * @param id
     * @return
     */
    @GetMapping("get/app/{id}")
    @ApiOperation(value = "AP设备简单信息", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:same:device:ap:detail")
    @ApiImplicitParam(paramType = "path", name = "id", value = "AP设备id", required = true, dataType = "Integer")
    public Result getByApp(@PathVariable Integer id) {
        return wifiApDeviceService.getByApp(id);
    }

}