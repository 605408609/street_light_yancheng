/**
 * @filename:AlarmOrderSetController 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.web;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

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

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.woa.AlarmOrderSet;
import com.exc.street.light.resource.qo.WoaAlarmOrderSetQuery;
import com.exc.street.light.resource.vo.req.WoaReqAlarmOrderSetVO;
import com.exc.street.light.woa.service.AlarmOrderSetService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p>告警生成工单设置控制器</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-03-28
 *
 */
@Api(tags = "告警生成工单设置控制器", value = "告警生成工单设置控制器")
@RestController
@RequestMapping("/api/woa/alarm/order/set")
public class AlarmOrderSetController {

    @Autowired
    private AlarmOrderSetService alarmOrderSetService;

    /**
     * @explain 新增工单生成设置
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增工单生成设置", notes = "新增工单生成设置,作者：Longshuangyang")
    @RequiresPermissions(value = "permission:module:order:set:add")
    @SystemLog(logModul = "工单告警",logType = "新增工单生成设置",logDesc = "新增工单生成设置")
    public Result add(@RequestBody WoaReqAlarmOrderSetVO woaReqAlarmOrderSetVO, HttpServletRequest request) {
        return alarmOrderSetService.add(woaReqAlarmOrderSetVO, request);
    }

    /**
     * @explain 分页条件查询
     * @author Longshuangyang
     * @time 2020-03-25
     */
    @GetMapping("/query")
    @ApiOperation(value = "分页条件查询", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "permission:module:order:set")
    public Result query(WoaAlarmOrderSetQuery woaAlarmOrderSetQuery, HttpServletRequest httpServletRequest) {
        return alarmOrderSetService.queryAlarmSet(woaAlarmOrderSetQuery, httpServletRequest);
    }

    /**
     * @explain 批量删除告警设置
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除告警设置", notes = "批量删除告警设置,作者：Longshuangyang")
    @RequiresPermissions(value = "permission:module:order:set:delete")
    @SystemLog(logModul = "工单告警",logType = "批量删除告警设置",logDesc = "批量删除告警设置")
    public Result batchDelete(@RequestParam String ids, HttpServletRequest request) {
        return alarmOrderSetService.batchDelete(ids, request);
    }

    /**
     * @explain 修改工单生成设置
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改工单生成设置", notes = "修改工单生成设置,作者：Longshuangyang")
    @RequiresPermissions(value = "permission:module:order:set:update")
    @SystemLog(logModul = "工单告警",logType = "修改工单生成设置",logDesc = "修改工单生成设置")
    public Result update(@RequestBody WoaReqAlarmOrderSetVO woaReqAlarmOrderSetVO, HttpServletRequest request) {
        return alarmOrderSetService.updateAlarmOrderSet(woaReqAlarmOrderSetVO, request);
    }

    /**
     * @explain 工单生成设置详情
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/get/{id}")
    @ApiOperation(value = "工单生成设置详情", notes = "工单生成设置详情,作者：Longshuangyang")
    @RequiresPermissions(value = "permission:module:order:set:detail")
    @SystemLog(logModul = "工单告警",logType = "工单生成设置详情",logDesc = "工单生成设置详情")
    public Result get(@PathVariable Integer id, HttpServletRequest request) {
        return alarmOrderSetService.get(id, request);
    }

    /**
     * @explain 控制设置开关
     * @author Longshuangyang
     * @time 2020-03-25
     */
    @GetMapping("/control")
    @ApiOperation(value = "控制设置开关", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "permission:module:order:set")
    @SystemLog(logModul = "工单告警",logType = "控制设置开关",logDesc = "控制设置开关")
    public Result control(@PathParam("id") Integer id, @PathParam("status") Integer status, HttpServletRequest httpServletRequest) {
        return alarmOrderSetService.control(id, status, httpServletRequest);
    }

    /**
     * @explain 删除工单生成设置
     * @author  Longshuangyang
     * @time    2020-03-28
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除工单生成设置", notes = "作者：Longshuangyang")
    @ApiImplicitParam(paramType="query", name = "id", value = "工单生成设置id", required = true, dataType = "Long")
    @RequiresPermissions(value = "permission:module:order:set:delete")
    @SystemLog(logModul = "工单告警",logType = "删除工单生成设置",logDesc = "删除工单生成设置")
    public Result deleteById(@PathVariable("id") Long id){
        Result result=new Result();
        AlarmOrderSet obj=alarmOrderSetService.getById(id);
        if (null!=obj) {
            boolean rsg = alarmOrderSetService.removeById(id);
            if (rsg) {
                result.success("删除成功");
            }else {
                result.error("删除失败！");
            }
        }else {
            result.error("删除的对象不存在！");
        }
        return result;
    }
}