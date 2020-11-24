/**
 * @filename:LampGroupController 2020-07-16
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampGroupSingle;
import com.exc.street.light.resource.vo.req.SlReqLampGroupVO;
import com.exc.street.light.resource.vo.resp.SlRespLampGroupSingleVO;
import com.exc.street.light.sl.service.LampGroupService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： 灯具分组表Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-07-16
 *
 */
@Api(tags = "灯具分组表", value = "灯具分组表")
@RestController
@RequestMapping("/api/sl/lamp/group")
public class LampGroupController {

    @Autowired
    private LampGroupService lampGroupService;

    /**
     * @explain 添加灯具分组
     * @author Longshuangyang
     * @time 2020-07-16
     */
    @PostMapping
    @ApiOperation(value = "添加灯具分组", notes = "添加灯具分钟,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control")
    @SystemLog(logModul = "设备管理", logType = "新增", logDesc = "添加灯具分组")
    public Result addLampGroup(@ApiParam @RequestBody SlReqLampGroupVO slReqLampGroupVO, HttpServletRequest request) {
        return lampGroupService.addLampGroup(slReqLampGroupVO, request);
    }

    /**
     * @explain 灯具分组验证唯一性
     * @author Longshuangyang
     * @time 2020-07-16
     */
    @GetMapping("/unique")
    @ApiOperation(value = "灯具分组验证唯一性", notes = "灯具分组验证唯一性,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control")
    @SystemLog(logModul = "智慧照明", logType = "验证唯一性", logDesc = "灯具分组验证唯一性")
    public Result unique(@ApiParam SlReqLampGroupVO slReqLampGroupVO, HttpServletRequest request) {
        return lampGroupService.unique(slReqLampGroupVO, request);
    }

    /**
     * @explain 获取灯具分组列表
     * @author Longshuangyang
     * @time 2020-07-16
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取灯具分组列表", notes = "获取灯具分组列表,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control")
    public Result<List<SlRespLampGroupSingleVO>> getList(HttpServletRequest request) {
        return lampGroupService.getList(request);
    }

    /**
     * @explain 批量删除灯具分组
     * @author Longshuangyang
     * @time 2020-07-16
     */
    @PostMapping("/delete/batch")
    @ApiOperation(value = "批量删除灯具分组", notes = "删除灯具分组,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control:delete")
    @SystemLog(logModul = "设备管理",logType = "批量删除",logDesc = "批量删除灯具分组")
    public Result deleteList(@RequestBody SlReqLampGroupVO slReqLampGroupVO, HttpServletRequest request) {
        return lampGroupService.deleteList(slReqLampGroupVO, request);
    }

}