/**
 * @filename:AlarmController 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.web;

import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.QueryObject;
import com.exc.street.light.resource.qo.WoaAlarmQuery;
import com.exc.street.light.resource.vo.req.WoaReqAlarmTypeAnalyseVO;
import com.exc.street.light.resource.vo.req.WoaReqOrderNewVO;
import com.exc.street.light.woa.service.AlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>告警控制器</p>
 *
 * <p>说明： Controller</P>
 *
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-03-28
 */
@Api(tags = "告警控制器", value = "告警控制器")
@RestController
@RequestMapping("/api/woa/alarm")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    /**
     * @explain 首页运维分析数据
     * @author Longshuangyang
     * @time 2020-03-25
     */
    @GetMapping("/analysis")
    @ApiOperation(value = "首页运维分析数据", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result analysis(HttpServletRequest request) {
        return alarmService.analysis(request);
    }

    /**
     * 修改告警信息为已读
     *
     * @param request
     * @param alarmId
     * @return
     */
    @PutMapping("/updateHaveRead")
    @ApiOperation(value = "修改告警信息为已读", notes = "作者：Huangjinhao")
    @RequiresPermissions(value = "view:data")
    public Result update(@RequestParam Integer alarmId, HttpServletRequest request) {
        return alarmService.updateHaveRead(alarmId, request);
    }

    /**
     * @explain 分页条件查询
     * @author Longshuangyang
     * @time 2020-03-25
     */
    @GetMapping("/query")
    @ApiOperation(value = "分页条件查询", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "permission:module:alarm:list")
    public Result query(WoaAlarmQuery woaAlarmQuery, HttpServletRequest httpServletRequest) {
        return alarmService.queryAlarm(woaAlarmQuery, httpServletRequest);
    }

    /**
     * @explain 批量删除告警
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除告警", notes = "批量删除告警,作者：Longshuangyang")
    @RequiresPermissions(value = "permission:module:alarm:manage:delete")
    @SystemLog(logModul = "工单告警",logType = "批量删除告警",logDesc = "批量删除告警")
    public Result batchDelete(@RequestParam String ids, HttpServletRequest request) {
        return alarmService.batchDelete(ids, request);
    }

    /**
     * @explain app首页告警区域分析
     * @author Huangjinhao
     * @time 2020-03-25
     */
    @GetMapping("/area/appAnalyse")
    @ApiOperation(value = "app首页告警区域分析", notes = "作者：Huangjinhao")
    @RequiresPermissions(value = "view:data")
    public Result appAnalyse(QueryObject queryObject, HttpServletRequest httpServletRequest) {
        return alarmService.areaAnalyseApp(queryObject,httpServletRequest);
    }


    /**
     * @explain 告警区域分析
     * @author Longshuangyang
     * @time 2020-03-25
     */
    @GetMapping("/area/analyse")
    @ApiOperation(value = "告警区域分析", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "permission:module:alarm:analysis:area")
    public Result areaAnalyse(QueryObject queryObject, HttpServletRequest httpServletRequest) {
        Result resultPage = alarmService.areaAnalyse(1, queryObject, httpServletRequest);
        Result resultChart = alarmService.areaAnalyse(2, queryObject, httpServletRequest);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page", resultPage.getData());
        jsonObject.put("chart", resultChart.getData());
        Result result = new Result();
        return result.success(jsonObject);
    }

    /**
     * @explain 告警类型分析
     * @author Longshuangyang
     * @time 2020-03-25
     */
    @GetMapping("/type/analyse")
    @ApiOperation(value = "告警类型分析", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "permission:module:alarm:analysis:type")
    public Result typeAnalyse(WoaReqAlarmTypeAnalyseVO woaReqAlarmTypeAnalyseVO, HttpServletRequest httpServletRequest) {
        return alarmService.typeAnalyse(woaReqAlarmTypeAnalyseVO, httpServletRequest);
    }

    /**
     * @explain 我的消息列表查询(app端使用)
     * @author HuangMin
     * @time 2020-03-25
     */
    @GetMapping("/news")
    @ApiOperation(value = "我的消息列表查询", notes = "作者：HuangMin")
    @RequiresPermissions(value = "view:data")
    public Result news(WoaAlarmQuery woaAlarmQuery, HttpServletRequest httpServletRequest) {
        return alarmService.queryNews(woaAlarmQuery, httpServletRequest);
    }

    /**
     * @explain 修改我的消息列表对象为已读状态（app端使用）
     * @author HuangMin
     * @time 2020-03-25
     */
    @PostMapping("/news/status")
    @ApiOperation(value = "修改我的消息列表对象为已读状态", notes = "作者：HuangMin")
    @RequiresPermissions(value = "view:data")
    @SystemLog(logModul = "工单告警",logType = "修改我的消息列表对象为已读状态",logDesc = "修改我的消息列表对象为已读状态")
    public Result newsStatus(@RequestBody WoaAlarmQuery woaAlarmQuery, HttpServletRequest httpServletRequest) {
        return alarmService.newsStatus(woaAlarmQuery, httpServletRequest);
    }

    /**
     * @explain 修改我的消息列表全部已读（app端使用）
     * @author HuangMin
     * @time 2020-03-25
     */
    @PostMapping("/news/all")
    @ApiOperation(value = "修改我的消息列表全部已读", notes = "作者：HuangMin")
    @RequiresPermissions(value = "view:data")
    @SystemLog(logModul = "工单告警",logType = "修改我的消息列表全部已读",logDesc = "修改我的消息列表全部已读")
    public Result newsAll(HttpServletRequest httpServletRequest) {
        return alarmService.newsAll(httpServletRequest);
    }

}