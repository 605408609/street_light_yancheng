/**
 * @filename:MeteorologicalDeviceController 2020-03-21
 * @project em  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.em.web;

import com.exc.street.light.em.service.MeteorologicalDeviceService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.qo.EmMeteorologicalDeviceQueryObject;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.req.EmReqStatisParamVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>气象设备控制器</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: LeiJing
 * @time 2020-03-21
 *
 */
@Api(tags = "气象设备控制器", value = "气象设备控制器")
@RestController
@RequestMapping("/api/em/meteorological/device")
public class MeteorologicalDeviceController {
    @Autowired
    private MeteorologicalDeviceService meteorologicalDeviceService;

    @PostMapping
    @RequiresPermissions("dm:module:same:device:sensor:add")
    @ApiOperation(value = "新增气象设备", notes = "新增气象设备,作者：LeiJing")
    @SystemLog(logModul = "环境监测",logType = "新增",logDesc = "新增气象设备")
    public Result addDevice(@ApiParam @RequestBody MeteorologicalDevice meteorologicalDevice, HttpServletRequest request) {
        return meteorologicalDeviceService.addDevice(meteorologicalDevice, request);
    }

    @PutMapping
    @RequiresPermissions("dm:module:same:device:sensor:update")
    @ApiOperation(value = "编辑气象设备", notes = "编辑气象设备,作者：LeiJing")
    @SystemLog(logModul = "环境监测",logType = "编辑",logDesc = "编辑气象设备")
    public Result updateDevice(@ApiParam @RequestBody MeteorologicalDevice meteorologicalDevice, HttpServletRequest request) {
        return meteorologicalDeviceService.updateDevice(meteorologicalDevice, request);
    }

    @DeleteMapping("/{id}")
    @RequiresPermissions("dm:module:same:device:sensor:delete")
    @ApiOperation(value = "删除气象设备", notes = "删除气象设备,作者：LeiJing")
    @SystemLog(logModul = "环境监测",logType = "删除",logDesc = "删除气象设备")
    public Result deleteDevice(@PathVariable Integer id, HttpServletRequest request) {
        return meteorologicalDeviceService.deleteDevice(id, request);
    }

    @DeleteMapping("/batch")
    @RequiresPermissions("dm:module:same:device:sensor:delete")
    @ApiOperation(value = "批量删除气象设备", notes = "批量删除气象设备,作者：LeiJing")
    @SystemLog(logModul = "环境监测",logType = "删除",logDesc = "批量删除气象设备")
    public Result batchDeleteDevice(@RequestParam String ids, HttpServletRequest request) {
        List<Integer> idList = StringConversionUtil.getIdListFromString(ids);
        return meteorologicalDeviceService.batchDeleteDevice(idList);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("dm:module:same:device:sensor:detail")
    @ApiOperation(value = "获取气象设备详细信息", notes = "获取气象设备详细信息,作者：LeiJing")
    public Result getDeviceInfo(@PathVariable Integer id, HttpServletRequest request) {
        return meteorologicalDeviceService.getDeviceInfo(id, request);
    }

    @GetMapping("/getList")
    @RequiresPermissions("dm:module:same:device:sensor")
    @ApiOperation(value = "获取气象设备列表", notes = "获取气象设备列表,作者：LeiJing")
    public Result getDeviceList(EmMeteorologicalDeviceQueryObject qo, HttpServletRequest request) {
        return meteorologicalDeviceService.getDeviceList(qo, request);
    }

    @PostMapping("/batchImport")
    @RequiresPermissions("dm:module:same:device:sensor:add")
    @ApiOperation(value = "批量导入气象设备", notes = "批量导入气象设备,作者：LeiJing")
    @SystemLog(logModul = "环境监测",logType = "导入",logDesc = "导入气象设备")
    public Result batchImportDevice(MeteorologicalDevice meteorologicalDevice, HttpServletRequest request) {
        return meteorologicalDeviceService.batchImportDevice(meteorologicalDevice, request);
    }

    @PostMapping("/uniqueness")
    @RequiresPermissions("dm:module:same:device:sensor")
    @ApiOperation(value = "气象设备名称和编号唯一性验证", notes = "气象设备名称和编号唯一性验证,作者：LeiJing")
    public Result uniqueness(@RequestBody MeteorologicalDevice meteorologicalDevice, HttpServletRequest request) {
        return meteorologicalDeviceService.uniqueness(meteorologicalDevice, request);
    }

    @GetMapping("/getPulldownList")
    @RequiresPermissions("dm:module:same:device:sensor")
    @ApiOperation(value = "获取气象设备下拉列表", notes = "获取气象设备下拉列表,作者：LeiJing")
    public Result getDevicePulldownList(HttpServletRequest request) {
        return meteorologicalDeviceService.getDevicePulldownList(request);
    }

    /**
     * 获取所有设备的实时气象信息并保存至气象历史数据库
     *
     * @return
     */
    @GetMapping("/getInfoByDevice")
    @RequiresPermissions("em:module:statistical:charts")
    @ApiOperation(value = "获取所有设备的实时气象信息并保存至气象历史数据库", notes = "获取所有设备的实时气象信息并保存至气象历史数据库,作者：LeiJing")
    public Result getInfoByDevice() {
        return meteorologicalDeviceService.getInfoByDevice();
    }

    /**
     * 根据查询日期和查询气象信息类型统计当天气象数据，24小时气象数据
     *
     * @param emReqStatisParamVO
     * @param request
     * @return
     */
    @GetMapping("/statis")
    @RequiresPermissions(value = {"em:module:statistical:charts","em:module:real:time:data"}, logical = Logical.OR)
    @ApiOperation(value = "根据查询日期和查询气象信息类型统计当天气象数据", notes = "获取气象设备详细信息,作者：LeiJing")
    public Result statis(EmReqStatisParamVO emReqStatisParamVO, HttpServletRequest request) {
        return meteorologicalDeviceService.statis(emReqStatisParamVO, request);
    }

    /**
     * 获取所有设备的实时在线状态并保存至数据库，保存实时气象数据
     *
     * @return
     */
    @GetMapping("/getStatusByDevice")
    @RequiresPermissions("em:module:real:time:data")
    @ApiOperation(value = "获取所有设备的实时在线状态并保存至数据库", notes = "获取所有设备的实时在线状态并保存至数据库,作者：LeiJing")
    public Result getStatusByDevice() {
        return meteorologicalDeviceService.getStatusByDevice();
    }

    /**
     * 获取气象设备实时数据，传气象设备id则查询指定气象设备，不传气象设备id则查询所有气象设备
     *
     * @param deviceId
     * @return
     */
    @GetMapping("/getReal")
    @RequiresPermissions("em:module:real:time:data")
    @ApiOperation(value = "获取气象设备实时数据", notes = "获取气象设备实时数据,作者：LeiJing")
    public Result getReal(Integer deviceId) {
        return meteorologicalDeviceService.getReal(deviceId);
    }

    /**
     * 根据灯杆id集合查询气象设备
     *
     * @author LeiJing
     * @time 2020-03-28
     */
    @GetMapping("/pulldown/by/lamp/post")
    @RequiresPermissions("dm:module:same:device:sensor")
    @ApiOperation(value = "根据灯杆id集合查询气象设备", notes = "根据灯杆id集合查询气象设备,作者：LeiJing")
    public Result pulldownByLampPost(@RequestParam(required = false) List<Integer> lampPostIdList, HttpServletRequest request) {
        return meteorologicalDeviceService.pulldownByLampPost(lampPostIdList, request);
    }

    /**
     * @explain 根据灯杆id集合查询气象设备
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PostMapping("/pulldown/by/lamp/post")
    @RequiresPermissions("dm:module:same:device:sensor")
    @ApiOperation(value = "根据灯杆id集合查询气象设备", notes = "根据灯杆id集合查询气象设备,作者：Longshuangyang")
    public Result pulldownByLampPostGet(@RequestBody List<Integer> lampPostIdList, HttpServletRequest request) {
        return meteorologicalDeviceService.pulldownByLampPost(lampPostIdList, request);
    }

    /**
     * 根据灯杆id集合查询气象设备
     *
     * @author LeiJing
     * @time 2020-03-28
     */
    @PostMapping("/pulldown/by/lamp/post2")
    @RequiresPermissions("dm:module:same:device:sensor")
    @ApiOperation(value = "根据灯杆id集合查询气象设备", notes = "根据灯杆id集合查询气象设备,作者：LeiJing")
    public Result pulldownByLampPost2(@RequestBody List<Integer> lampPostIdList, HttpServletRequest request) {
        return meteorologicalDeviceService.pulldownByLampPost2(lampPostIdList, request);
    }
}