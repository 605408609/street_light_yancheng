/**
 * @filename:ScreenSubtitleEmController 2020-11-10
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ir.web;

import com.exc.street.light.ir.service.ScreenSubtitleEmService;
import com.exc.street.light.ir.service.ScreenSubtitleService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.qo.IrScreenDeviceQuery;
import com.exc.street.light.resource.qo.IrScreenSubtitleEmQuery;
import com.exc.street.light.resource.vo.req.IrReqScreenSubtitleEmVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： 传感器关联显示屏显示数据设置表Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time    2020-11-10
 *
 */
@Api(tags = "传感器关联显示屏显示数据设置表",value="传感器关联显示屏显示数据设置表" )
@RestController
@RequestMapping("/api/ir/screen/subtitle/em")
public class ScreenSubtitleEmController{

    @Autowired
    private ScreenSubtitleEmService screenSubtitleEmService;

    /**
     * @explain 添加传感器关联显示屏显示数据设置
     * @author Longshuangyang
     * @time 2020-11-10
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加传感器关联显示屏显示数据设置", notes = "添加传感器关联显示屏显示数据设置,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:sensor:data:add")
    @SystemLog(logModul = "信息发布",logType = "添加传感器关联显示屏显示数据设置",logDesc = "添加传感器关联显示屏显示数据设置")
    public Result add(@ApiParam @RequestBody IrReqScreenSubtitleEmVO irReqScreenSubtitleEmVO, HttpServletRequest request) {
        return screenSubtitleEmService.add(irReqScreenSubtitleEmVO, request);
    }

    /**
     * @explain 删除传感器关联显示屏显示数据设置
     * @author Longshuangyang
     * @time 2020-11-10
     */
    @DeleteMapping("/batch")
    @ApiOperation(value = "删除传感器关联显示屏显示数据设置", notes = "删除传感器关联显示屏显示数据设置,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:sensor:data:delete")
    @SystemLog(logModul = "信息发布",logType = "删除传感器关联显示屏显示数据设置",logDesc = "添加传感器关联显示屏显示数据设置")
    public Result delete(@RequestParam String ids, HttpServletRequest request) {
        return screenSubtitleEmService.batchDelete(ids, request);
    }

    /**
     * @explain 修改传感器关联显示屏显示数据设置
     * @author Longshuangyang
     * @time 2020-11-10
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改传感器关联显示屏显示数据设置", notes = "修改传感器关联显示屏显示数据设置,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:sensor:data:update")
    @SystemLog(logModul = "信息发布",logType = "修改传感器关联显示屏显示数据设置",logDesc = "修改传感器关联显示屏显示数据设置")
    public Result updateDevice(@ApiParam @RequestBody IrReqScreenSubtitleEmVO irReqScreenSubtitleEmVO, HttpServletRequest request) {
        return screenSubtitleEmService.updateDevice(irReqScreenSubtitleEmVO, request);
    }

    /**
     * 获取修改传感器关联显示屏显示数据设置列表(分页查询)
     *
     * @author Longshuangyang
     * @time 2020-11-10
     */
    @GetMapping("/query")
    @ApiOperation(value = "获取修改传感器关联显示屏显示数据设置列表(分页查询)", notes = "获取修改传感器关联显示屏显示数据设置列表(分页查询),作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:sensor:data")
    public Result list(IrScreenSubtitleEmQuery irScreenSubtitleEmQuery, HttpServletRequest httpServletRequest) {
        return screenSubtitleEmService.getQuery(irScreenSubtitleEmQuery, httpServletRequest);
    }

    /**
     * @explain 查询传感器关联显示屏显示数据设置详情
     * @author Longshuangyang
     * @time 2020-11-10
     */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "查询传感器关联显示屏显示数据设置详情", notes = "查询传感器关联显示屏显示数据设置详情,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:sensor:data:detail")
    public Result detail(@PathVariable Integer id, HttpServletRequest request) {
        return screenSubtitleEmService.detail(id, request);
    }

}