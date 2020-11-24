/**
 * @filename:ScreenDeviceController 2020-03-17
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.web;

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

import com.exc.street.light.ir.service.ScreenDeviceService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.qo.IrScreenDeviceQuery;
import com.exc.street.light.resource.vo.req.IrReqScreenOperateVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>显示屏控制器</p>
 *
 * <p>说明： Controller</P>
 *
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-03-17
 */
@Api(tags = "显示屏控制器", value = "显示屏控制器")
@RestController
@RequestMapping("/api/ir/screen/device")
public class ScreenDeviceController {

    @Autowired
    private ScreenDeviceService screenDeviceService;

    /**
     * @explain 查询显示屏(根据灯杆id集合)
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/pulldown/by/lamp/post")
    @ApiOperation(value = "查询显示屏(根据灯杆id集合)", notes = "查询显示屏(根据灯杆id集合),作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:screen:detail")
    public Result pulldownByLampPost(@RequestParam(required = false) List<Integer> lampPostIdList, HttpServletRequest request) {
        return screenDeviceService.pulldownByLampPost(lampPostIdList, request);
    }

    /**
     * @explain 查询显示屏(根据灯杆id集合)
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PostMapping("/pulldown/by/lamp/post")
    @ApiOperation(value = "查询显示屏(根据灯杆id集合)", notes = "查询显示屏(根据灯杆id集合),作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:screen:detail")
    public Result pulldownByLampPostGet(@ApiParam @RequestBody List<Integer> lampPostIdList, HttpServletRequest request) {
        return screenDeviceService.pulldownByLampPost(lampPostIdList, request);
    }

    /**
     * @explain 添加显示屏
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加显示屏", notes = "添加显示屏,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:screen:add")
    @SystemLog(logModul = "信息发布",logType = "添加",logDesc = "添加显示屏")
    public Result add(@ApiParam @RequestBody ScreenDevice screenDevice, HttpServletRequest request) {
        return screenDeviceService.add(screenDevice, request);
    }

    /**
     * @explain 修改显示屏
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改显示屏", notes = "修改显示屏,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:screen:update")
    @SystemLog(logModul = "信息发布",logType = "修改显示屏",logDesc = "修改显示屏")
    public Result updateDevice(@ApiParam @RequestBody ScreenDevice screenDevice, HttpServletRequest request) {
        return screenDeviceService.updateDevice(screenDevice, request);
    }

    /**
     * 显示屏更多操作，屏幕开关，设置音量，设置亮度
     *
     * @return
     */
    @PostMapping("/operate")
    @ApiOperation(value = "显示屏更多操作，屏幕开关，设置音量，设置亮度", notes = "显示屏更多操作，屏幕开关，设置音量，设置亮度,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:screen:update")
    @SystemLog(logModul = "信息发布",logType = "显示屏更多操作，屏幕开关，设置音量，设置亮度",logDesc = "显示屏更多操作，屏幕开关，设置音量，设置亮度")
    public Result operate(@RequestBody IrReqScreenOperateVO irReqScreenOperateVO, HttpServletRequest httpServletRequest) {
        return screenDeviceService.operate(irReqScreenOperateVO, httpServletRequest);
    }

    /**
     * 批量删除显示屏设备
     *
     * @param ids
     * @param request
     * @return
     */
    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除显示屏设备", notes = "批量删除显示屏设备,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:screen:delete")
    @SystemLog(logModul = "信息发布",logType = "批量删除",logDesc = "批量删除显示屏设备")
    public Result batchDelete(@RequestParam String ids, HttpServletRequest request) {
        return screenDeviceService.batchDelete(ids, request);
    }

    /**
     * 获取显示屏设备列表(分页查询)
     *
     * @param irScreenDeviceQuery
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value = "获取显示屏设备列表(分页查询)", notes = "获取显示屏设备列表(分页查询),作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:screen")
    public Result list(IrScreenDeviceQuery irScreenDeviceQuery, HttpServletRequest httpServletRequest) {
        return screenDeviceService.getQuery(irScreenDeviceQuery, httpServletRequest);
    }

    /**
     * @explain 显示屏设备验证唯一性
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/unique")
    @ApiOperation(value = "显示屏设备验证唯一性", notes = "显示屏设备验证唯一性,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:screen:add")
    @SystemLog(logModul = "信息发布",logType = "校验",logDesc = "显示屏设备验证唯一性")
    public Result unique(@ApiParam ScreenDevice screenDevice, HttpServletRequest request) {
        return screenDeviceService.unique(screenDevice, request);
    }

    /**
     * @explain 显示屏设备详情
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "显示屏设备详情", notes = "显示屏设备详情,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:screen:detail")
    public Result detail(@PathVariable Integer id, HttpServletRequest request) {
        return screenDeviceService.detail(id, request);
    }

    /**
     * 显示屏数据刷新
     *
     * @return
     */
    @GetMapping("/refresh")
    @ApiOperation(value = "显示屏数据刷新", notes = "显示屏数据刷新,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:screen:detail")
    public Result refresh(@RequestParam(required = false) List<Integer> screenDeviceIdList, HttpServletRequest request) {
        if (screenDeviceIdList != null && screenDeviceIdList.size() > 0) {
            screenDeviceService.refresh(screenDeviceIdList);
        }
        screenDeviceService.refresh(null);
        Result result = new Result();
        return result.success("刷新成功");
    }


    /**
     * 获取截屏
     *
     * @return
     */
    @GetMapping("/getscreenshots")
    @ApiOperation(value = "获取截屏", notes = "获取截屏,作者：wuzili")
    @RequiresPermissions(value = "dm:module:same:device:screen:detail")
    public Result getScreenshots(@RequestParam Integer id) {
        Result result = null;
        if (id != null) {
            result = screenDeviceService.getScreenshots(id);
        }
        return result;
    }

    /**
     * @explain 删除显示屏
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除显示屏", notes = "作者：Longshuangyang")
    @ApiImplicitParam(paramType = "path", name = "id", value = "显示屏id", required = true, dataType = "Long")
    @RequiresPermissions(value = "dm:module:same:device:screen:delete")
    @SystemLog(logModul = "信息发布",logType = "删除",logDesc = "删除显示屏")
    public Result deleteById(@PathVariable("id") Long id) {
        Result result = new Result();
        ScreenDevice obj = screenDeviceService.getById(id);
        if (null != obj) {
            boolean rsg = screenDeviceService.removeById(id);
            if (rsg) {
                result.success("删除成功");
            } else {
                result.error("删除失败！");
            }
        } else {
            result.error("删除的显示屏不存在！");
        }
        return result;
    }

    /**
     * @explain 修改
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @PutMapping
    @ApiOperation(value = "修改", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:screen:update")
    @SystemLog(logModul = "信息发布",logType = "修改",logDesc = "修改显示屏")
    public Result<ScreenDevice> update(@RequestBody ScreenDevice screenDevice) {
        Result<ScreenDevice> result = new Result<>();
        if (null != screenDevice) {
            boolean rsg = screenDeviceService.updateById(screenDevice);
            if (rsg) {
                result.success("修改成功");
            } else {
                result.error("修改失败！");
            }
        } else {
            result.error("请传入正确参数！");
        }
        return result;
    }

}
