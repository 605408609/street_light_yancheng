/**
 * @filename:SlLampPostController 2020-03-17
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.web;

import java.util.ArrayList;
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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exc.street.light.dlm.service.SlLampPostService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.qo.DlmDeviceQuery;
import com.exc.street.light.resource.qo.DlmLampPostQuery;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>灯杆控制器</p>
 *
 * <p>说明： Controller</P>
 *
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-03-17
 */
@Api(tags = "灯杆控制器", value = "灯杆控制器")
@RestController
@RequestMapping("/api/dlm/sl/lamp/post")
public class SlLampPostController {

    @Autowired
    private SlLampPostService slLampPostService;

    /**
     * @explain 查询灯杆下拉列表
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/pulldown")
    @ApiOperation(value = "查询灯杆下拉列表", notes = "查询灯杆下拉列表,作者：Longshuangyang")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceTypeNum", value = "指定设备类型编号（1：灯具，2：WIFI，3：广播，4：监控，5：显示屏，6：一键呼叫，7：气象）", required = false, dataType = "int", paramType = "query")})
    @RequiresPermissions(value = "view:data")
    public Result pulldown(@RequestParam(required = false) Integer siteId,
                           @RequestParam(required = false) Integer lampPostId,
                           @RequestParam(required = false) Integer deviceTypeNum,
                           @RequestParam(required = false) String lampPostName,
                           HttpServletRequest request) {
        return slLampPostService.pulldown(siteId, lampPostId, deviceTypeNum, lampPostName, request);
    }

    /**
     * @explain 查询灯杆带设备详情
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/get")
    @ApiOperation(value = "获取带设备基础信息的灯杆详情", notes = "获取带设备基础信息的灯杆详情,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:lamp:post:detail")
    public Result get(DlmLampPostQuery dlmLampPostQuery, HttpServletRequest request) {
        return slLampPostService.get(dlmLampPostQuery, request);
    }
    /**
     * @explain 获取灯杆默认弹窗的摄像头详情
     * @author Huang Min
     * @time 2020-03-16
     */
    @GetMapping("/get/camera")
//    @ApiOperation(value = "获取灯杆默认弹窗的摄像头详情", notes = "获取灯杆默认弹窗的摄像头详情,作者：Huang Min")
//    @RequiresPermissions(value = "dm:module:lamp:post:detail")
    public Result getCamera(DlmLampPostQuery dlmLampPostQuery, HttpServletRequest request) {
        return slLampPostService.getCamera(dlmLampPostQuery, request);
    }
    /**
     * @explain 查询设备名称列表
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/get/device")
    @ApiOperation(value = "根据设备名称查询设备列表", notes = "根据设备名称查询设备列表,作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result get(DlmDeviceQuery query,HttpServletRequest request) {
        return slLampPostService.getDevice(query.getName(),query.getType(),request);
    }

    /**
     * @explain 查询灯杆不带设备详情
     * @author huangjinhao
     * @time 2020-05-21
     */
    @GetMapping("/getNoDetails")
    @ApiOperation(value = "获取不带设备基础信息的灯杆详情", notes = "获取不带设备基础信息的灯杆详情,作者：huangjinhao")
    @RequiresPermissions(value = "view:data")
    public Result getNoDetails(DlmLampPostQuery dlmLampPostQuery, HttpServletRequest request) {
        return slLampPostService.getNoDetails(dlmLampPostQuery, request);
    }

    /**
     * @explain 批量删除灯杆
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除灯杆", notes = "批量删除灯杆,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:lamp:post:delete")
    @SystemLog(logModul = "设备位置管理",logType = "批量删除",logDesc = "批量删除灯杆")
    public Result batchDelete(@RequestParam String ids, HttpServletRequest request) {
        return slLampPostService.batchDelete(ids, request);
    }

//    /**
//     * @explain 设备列表
//     * @author Longshuangyang
//     * @time 2020-03-16
//     */
//    @GetMapping("/device")
//    @ApiOperation(value = "设备名称列表", notes = "设备名称列表,作者：Longshuangyang")
//    public Result device(@RequestParam String deviceName, HttpServletRequest request) {
//        return slLampPostService.device(deviceName, request);
//    }

    /**
     * @explain 添加灯杆
     * @author Longshuangyang
     * @time 2020-03-25
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加灯杆", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:lamp:post:add")
    @SystemLog(logModul = "设备位置管理",logType = "添加",logDesc = "添加灯杆")
    public Result insert(@ApiParam @RequestBody SlLampPost slLampPost, HttpServletRequest request) {
        return slLampPostService.add(slLampPost, request);
    }

    /**
     * @explain 修改灯杆
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改灯杆", notes = "修改灯杆,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:lamp:post:update")
    @SystemLog(logModul = "设备位置管理",logType = "修改",logDesc = "修改灯杆")
    public Result updateLampPost(@ApiParam @RequestBody SlLampPost slLampPost, HttpServletRequest request) {
        return slLampPostService.updateLampPost(slLampPost, request);
    }

    /**
     * @explain 查询灯杆集合（根据站点id集合post）
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PostMapping("/by/site")
    @ApiOperation(value = "查询灯杆集合（根据站点id集合post）", notes = "查询灯杆集合（根据站点id集合post）,作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result postBySite(@RequestBody List<Integer> siteIdList, HttpServletRequest request) {
        return slLampPostService.getBySite(siteIdList, request);
    }

    /**
     * @explain 查询灯杆集合（根据站点id集合get）
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/by/site")
    @ApiOperation(value = "查询灯杆集合（根据站点id集合get）", notes = "查询灯杆集合（根据站点id集合get）,作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result getBySite(@RequestParam(required = false) List<Integer> siteIdList,
                            HttpServletRequest request) {
        return slLampPostService.getBySite(siteIdList, request);
    }

    /**
     * @explain 查询灯杆集合（根据分组id集合get）
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/by/group")
    @ApiOperation(value = "查询灯杆集合（根据分组id集合get）", notes = "查询灯杆集合（根据分组id集合get）,作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result getByGroup(@RequestParam(required = false) List<Integer> groupIdList,
                             HttpServletRequest request) {
        return slLampPostService.getByGroup(groupIdList, request);
    }

    /**
     * @explain 查询灯杆集合（根据分组id集合post）
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PostMapping("/by/group")
    @ApiOperation(value = "查询灯杆集合（根据分组id集合post）", notes = "查询灯杆集合（根据分组id集合post）,作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result postByGroup(@RequestBody List<Integer> groupIdList,
                              HttpServletRequest request) {
        return slLampPostService.getByGroup(groupIdList, request);
    }

    /**
     * @explain 灯杆分页条件查询
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/page")
    @ApiOperation(value = "灯杆分页条件查询", notes = "灯杆分页条件查询,作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result getPage(DlmLampPostQuery dlmLampPostQuery, HttpServletRequest request) {
        return slLampPostService.getPage(dlmLampPostQuery, request);
    }

    /**
     * @explain 获取各个设备的个数及在线率
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/map/number")
    @ApiOperation(value = "获取各个设备的个数及在线率", notes = "获取各个设备的个数及在线率,作者：Longshuangyang")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "指定查询类型（0:今日 1：本周，2：本月）", required = false, dataType = "int", paramType = "query")})
    @RequiresPermissions(value = "view:data")
    public Result mapNumber(@RequestParam(value = "type", defaultValue = "0", required = false) Integer type,HttpServletRequest request) {
        return slLampPostService.mapNumber(1,type,request);
    }

    /**
     * @explain 获取各个设备的个数及在线率
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/map/number/count")
    @ApiOperation(value = "获取各个设备的个数及在线率(添加设备总数)", notes = "获取各个设备的个数及在线率,作者：Longshuangyang")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "指定查询类型（0:今日 1：本周，2：本月）", required = false, dataType = "int", paramType = "query")})
    @RequiresPermissions(value = "view:data")
    public Result mapNumberCount(@RequestParam(value = "type", defaultValue = "0", required = false) Integer type,HttpServletRequest request) {
        return slLampPostService.mapNumber(2,type,request);
    }

    /**
     * @explain 根据灯杆id集合获取灯杆
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @PostMapping("/get/list")
    @ApiOperation(value = "根据灯杆id集合获取灯杆", notes = "根据灯杆id集合获取灯杆,作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result getList(@RequestBody List<Integer> lampPostIdList,
                          HttpServletRequest request) {
        List<SlLampPost> list = new ArrayList<>();
        LambdaQueryWrapper<SlLampPost> wrapper = new LambdaQueryWrapper();
        if (lampPostIdList != null && lampPostIdList.size() != 0) {
            wrapper.in(SlLampPost::getId, lampPostIdList);
            list = slLampPostService.list(wrapper);
        }
        Result result = new Result();
        return result.success(list);
    }

    /**
     * @explain 删除灯杆
     * @author  Longshuangyang
     * @time    2020-03-16
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除灯杆", notes = "作者：Longshuangyang")
    @ApiImplicitParam(paramType="path", name = "id", value = "灯杆id", required = true, dataType = "Long")
    @RequiresPermissions(value = "dm:module:lamp:post:delete")
    @SystemLog(logModul = "设备位置管理",logType = "删除",logDesc = "删除灯杆")
    public Result deleteById(@PathVariable("id") Long id,  HttpServletRequest request){
        return slLampPostService.delete(id, request);
    }

    /**
     * @explain 灯杆验证唯一性
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/unique")
    @ApiOperation(value = "灯杆验证唯一性", notes = "灯杆验证唯一性,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:lamp:post:add")
    @SystemLog(logModul = "设备位置管理",logType = "验证唯一性",logDesc = "灯杆验证唯一性")
    public Result unique(@ApiParam SlLampPost slLampPost, HttpServletRequest request) {
        return slLampPostService.unique(slLampPost, request);
    }

    /**
     * @explain 根据灯杆id查询灯杆详情
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据灯杆id查询灯杆详情", notes = "根据灯杆id查询灯杆详情,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:lamp:post:detail")
    public Result getLampPostById(@PathVariable("id") Integer id, HttpServletRequest request) {
        return slLampPostService.getLampPostById(id, request);
    }

}