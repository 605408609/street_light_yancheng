/**
 * @filename:EdManholeCoverDeviceController 2020-09-28
 * @project ed  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ed.web;

import com.exc.street.light.ed.service.EdManholeCoverDeviceService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.vo.req.EdReqManholeCoverDevicePageVO;
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
 * <p>说明： 井盖设备表Controller</P>
 * @version: V1.0
 * @author: Huang Jin Hao
 * @time    2020-09-28
 *
 */
@Api(tags = "井盖设备表",value="井盖设备表" )
@RestController
@RequestMapping("/api/ed/ed/manhole/cover/device")
public class EdManholeCoverDeviceController {

    @Autowired
    private EdManholeCoverDeviceService edManholeCoverDeviceService;

    /**
     * @explain 井盖设备分页条件查询
     * @author Huangjinhao
     * @time 2020-09-07
     */
    @GetMapping("/page")
    @ApiOperation(value = "井盖设备分页条件查询", notes = "井盖设备分页条件查询,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:manhole:cover:detail")
    public Result getPage(EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO, HttpServletRequest request) {
        return edManholeCoverDeviceService.getPage(edReqManholeCoverDevicePageVO, request);
    }

    /**
     * @explain 井盖设备验证唯一性
     * @author Huangjinhao
     * @time 2020-03-17
     */
    @PostMapping("/unique")
    @ApiOperation(value = "井盖设备验证唯一性", notes = "井盖设备验证唯一性,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:manhole:cover:add")
    public Result unique(@ApiParam @RequestBody EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO, HttpServletRequest request) {
        return edManholeCoverDeviceService.unique(edReqManholeCoverDevicePageVO, request);
    }

    /**
     * @explain 添加井盖设备
     * @author Huangjinhao
     * @time 2020-09-07
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加井盖设备", notes = "作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:manhole:cover:add")
    @SystemLog(logModul = "设备管理",logType = "新增",logDesc = "新增垃圾桶设备")
    public Result add(@ApiParam @RequestBody EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO, HttpServletRequest request) {
        return edManholeCoverDeviceService.add(edReqManholeCoverDevicePageVO, request);
    }

    /**
     * @explain 修改井盖设备
     * @author Huangjinhao
     * @time 2020-03-16
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改井盖设备", notes = "修改井盖设备,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:manhole:cover:update")
    @SystemLog(logModul = "设备管理",logType = "编辑",logDesc = "编辑井盖设备")
    public Result updateDevice(@ApiParam @RequestBody EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO, HttpServletRequest request) {
        return edManholeCoverDeviceService.updateDevice(edReqManholeCoverDevicePageVO, request);
    }

    /**
     * @explain 批量删除井盖设备
     * @author Huangjinhao
     * @time 2020-03-23
     */
    @DeleteMapping("/delete/{ids}")
    @ApiOperation(value = "批量删除井盖设备", notes = "批量删除井盖设备,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:manhole:cover:delete")
    @SystemLog(logModul = "设备管理",logType = "删除",logDesc = "删除井盖设备")
    public Result delete(@PathVariable String ids, HttpServletRequest request) {
        return edManholeCoverDeviceService.deleteByIds(ids, request);
    }

    /**
     * @explain 设置开盖报警倾角阈值
     * @author Huangjinhao
     * @time 2020-03-16
     */
    @PostMapping("/setThreshold")
    @ApiOperation(value = "设置开盖报警倾角阈值", notes = "设置开盖报警倾角阈值,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:manhole:cover:update")
    @SystemLog(logModul = "设备管理",logType = "编辑",logDesc = "设置开盖报警倾角阈值")
    public Result setThreshold(@ApiParam @RequestBody List<EdReqManholeCoverDevicePageVO> edReqManholeCoverDevicePageVOS, HttpServletRequest request) {
        return edManholeCoverDeviceService.setThreshold(edReqManholeCoverDevicePageVOS, request);
    }

}