/**
 * @filename:LocationAreaController 2020-03-16
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.web;

import com.exc.street.light.dlm.service.LocationAreaService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationArea;
import com.exc.street.light.resource.qo.DlmControlLoopOfDeviceQuery;
import com.exc.street.light.resource.vo.req.DlmReqLocationAreaVO;
import com.exc.street.light.resource.vo.req.SlReqLightControlVO;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>区域控制器</p>
 *
 * <p>说明： Controller</P>
 *
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-03-16
 */
@Api(tags = "区域控制器", value = "区域控制器")
@RestController
@RequestMapping("/api/dlm/location/area")
public class LocationAreaController {

    @Autowired
    private LocationAreaService locationAreaService;

    /**
     * @explain 获取市级灯具信息
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/getLampCity")
    @ApiOperation(value = "获取市级灯具信息", notes = "获取市级灯具信息,作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result getLampCity(HttpServletRequest request) {
        return locationAreaService.getLampCity(request);
    }


    /**
     * 城市灯具控制
     *
     * @param request
     * @param vo      控制参数
     * @return
     * @author Huangjinahao
     */
    @PostMapping("/cityControl")
    @ApiOperation(value = "城市灯具控制", notes = "城市灯具控制,作者：Huangjinahao")
    @RequiresPermissions(value = "view:data")
    @SystemLog(logModul = "设备位置管理", logType = "城市灯具控制", logDesc = "城市灯具控制")
    public Result cityControl(HttpServletRequest request, @RequestBody SlReqLightControlVO vo) {
        return locationAreaService.cityControl(request, vo);
    }


    /**
     * 灯具整体控制
     *
     * @param request
     * @param vo      控制参数
     * @return
     * @author Huangjinahao
     */
    @PostMapping("/wholeControl")
    @ApiOperation(value = "灯具整体控制", notes = "灯具整体控制,作者：Huangjinahao")
    @RequiresPermissions(value = "view:data")
    @SystemLog(logModul = "设备位置管理", logType = "灯具整体控制", logDesc = "灯具整体控制")
    public Result wholeControl(HttpServletRequest request, @RequestBody SlReqLightControlVO vo) {
        return locationAreaService.wholeControl(request, vo);
    }

    /**
     * @param deviceTypeNum 指定设备类型编号
     * @explain 查询区域详细列表(含分区筛选)
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询区域详细列表(含分区筛选)", notes = "查询区域详细列表(含分区筛选),作者：Longshuangyang")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hierarchy", value = "指定获取参数层级1为区域，依次递加，为5时需要指定设备类型编号才生效，默认3", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "deviceTypeNum", value = "指定设备类型编号（1：灯具，2：WIFI，3：广播，4：监控，5：显示屏，6：一键呼叫，7：气象）", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "eliminate", value = "剔除指定级别没有数据的分支（0：否，1：是），默认1", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页，默认0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数,为0则查全部，默认0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "lampPositionId", value = "灯具路数筛选", required = false, dataType = "int", paramType = "query")
    })
    @RequiresPermissions(value = "view:data")
    public Result list(@RequestParam(required = false) Integer hierarchy,
                       @RequestParam(required = false) Integer deviceTypeNum,
                       @RequestParam(required = false) Integer eliminate,
                       @RequestParam(required = false) Integer pageNum,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestParam(required = false) Integer lampPositionId,
                       HttpServletRequest request) {
        return locationAreaService.getList(hierarchy, deviceTypeNum, eliminate, pageNum, pageSize, lampPositionId, request);
    }


    /**
     * @explain 查询区域下拉列表
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/pulldown")
    @ApiOperation(value = "查询区域下拉列表", notes = "查询区域列表,作者：Longshuangyang")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaName", value = "区域名称（空则查询所有）", required = false, dataType = "String", paramType = "query")})
    @RequiresPermissions(value = "view:data")
    public Result pulldown(@RequestParam(required = false) String areaName, HttpServletRequest request) {
        return locationAreaService.pulldown(areaName, request);
    }

    /**
     * @explain 添加区域
     * @author Longshuangyang
     * @time 2020-03-25
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加区域", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:location:region:add")
    @SystemLog(logModul = "设备位置管理", logType = "添加", logDesc = "添加区域")
    public Result insert(@ApiParam @RequestBody DlmReqLocationAreaVO locationArea, HttpServletRequest request) {
        return locationAreaService.add(locationArea, request);
    }

    /**
     * @explain 删除分区
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除分区", notes = "作者：Longshuangyang")
    @ApiImplicitParam(paramType = "path", name = "id", value = "分区id", required = true, dataType = "Long")
    @RequiresPermissions(value = "dm:module:location:region:delete")
    @SystemLog(logModul = "设备位置管理", logType = "删除", logDesc = "删除分区")
    public Result deleteById(@PathVariable("id") Long id, HttpServletRequest request) {
        return locationAreaService.delete(id, request);
    }

    /**
     * @explain 区域验证唯一性
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/unique")
    @ApiOperation(value = "区域验证唯一性", notes = "区域验证唯一性,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:location:region:add")
    @SystemLog(logModul = "设备位置管理", logType = "验证唯一性", logDesc = "区域验证唯一性")
    public Result unique(@ApiParam LocationArea locationArea, HttpServletRequest request) {
        return locationAreaService.unique(locationArea, request);
    }

    /**
     * @explain 修改区域
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改区域", notes = "修改区域,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:location:region:update")
    @SystemLog(logModul = "设备位置管理", logType = "修改", logDesc = "修改区域")
    public Result updateArea(@ApiParam @RequestBody DlmReqLocationAreaVO locationArea, HttpServletRequest request) {
        return locationAreaService.updateArea(locationArea, request);
    }

    /**
     * @explain 根据区域id查询区域详情
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据区域id查询区域详情", notes = "根据区域id查询区域详情,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:location")
    public Result get(@PathVariable("id") Integer id, HttpServletRequest request) {
        return locationAreaService.get(id, request);
    }

    /**
     * 根据集控id和分组id查询灯具
     * @param loopQuery
     * @param request
     * @return
     */
    @GetMapping("/control/group")
    @RequiresPermissions(value = "view:data")
    @ApiOperation(value = "查询集控分组下的设备信息", notes = "作者：xiezhipeng")
    public Result getDeviceByControlIdAndLoopId(DlmControlLoopOfDeviceQuery loopQuery, HttpServletRequest request) {
        return locationAreaService.getDeviceByControlIdAndLoopId(loopQuery, request);
    }

    /**
     * 根据设备型号查询灯具下拉列表
     * @param deviceType
     * @param request
     * @return
     */
    @GetMapping("/pulldownByDeviceType")
    @RequiresPermissions(value = "view:data")
    @ApiOperation(value = "查询集控分组下的设备信息", notes = "作者：huangjinhao")
    public Result pulldownByDeviceType(Integer deviceType,Integer logId,Integer isSuccess,HttpServletRequest request) {
        return locationAreaService.getListByDeviceType(deviceType,logId,isSuccess,null, null, null, null, request);
    }

}