/**
 * @filename:RadioDeviceController 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.pb.web;

import cn.hutool.core.date.StopWatch;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.pb.service.RadioDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.pb.RadioDevice;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.PbRadioDeviceQueryObject;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.req.PbReqBatchUpdateVolumeVO;
import com.google.common.base.Stopwatch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>公共广播设备控制器</p>
 *
 * <p>说明： Controller</P>
 *
 * @version: V1.0
 * @author: LeiJing
 * @time 2020-03-21
 */
@Api(tags = "广播设备", value = "")
@RestController
@RequestMapping("/api/pb/radio/device")
public class RadioDeviceController {
    @Autowired
    private RadioDeviceService radioDeviceService;

    @Autowired
    private LogUserService logUserService;

    /**
     * 新增公共广播设备
     *
     * @param radioDevice
     * @param request
     * @return
     */
    @PostMapping
    @RequiresPermissions(value = "dm:module:same:device:broadcast:add")
    @ApiOperation(value = "新增公共广播设备", notes = "新增公共广播设备,作者：LeiJing")
    @SystemLog(logModul = "设备管理", logType = "新增", logDesc = "新增公共广播设备")
    public Result addDevice(@ApiParam @RequestBody RadioDevice radioDevice, HttpServletRequest request) {
        return radioDeviceService.addDevice(radioDevice, request);
    }

    /**
     * 编辑公共广播设备
     *
     * @param radioDevice
     * @param request
     * @return
     */
    @PutMapping
    @RequiresPermissions(value = "dm:module:same:device:broadcast:update")
    @ApiOperation(value = "编辑公共广播设备", notes = "编辑公共广播设备,作者：LeiJing")
    @SystemLog(logModul = "设备管理", logType = "编辑", logDesc = "编辑公共广播设备")
    public Result updateDevice(@ApiParam @RequestBody RadioDevice radioDevice, HttpServletRequest request) {
        return radioDeviceService.updateDevice(radioDevice, request);
    }

    /**
     * 删除公共广播设备
     *
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions(value = "dm:module:same:device:broadcast:delete")
    @ApiOperation(value = "删除公共广播设备", notes = "删除公共广播设备,作者：LeiJing")
    @SystemLog(logModul = "设备管理", logType = "删除", logDesc = "删除公共广播设备")
    public Result deleteDevice(@PathVariable Integer id, HttpServletRequest request) {
        return radioDeviceService.deleteDevice(id, request);
    }

    /**
     * 批量删除公共广播设备
     *
     * @param ids
     * @param request
     * @return
     */
    @DeleteMapping("/batch")
    @RequiresPermissions(value = "dm:module:same:device:broadcast:delete")
    @ApiOperation(value = "批量删除公共广播设备", notes = "批量删除公共广播设备,作者：LeiJing")
    @SystemLog(logModul = "设备管理", logType = "删除", logDesc = "批量删除公共广播设备")
    public Result batchDeleteDevice(@RequestParam String ids, HttpServletRequest request) {
        List<Integer> idList = StringConversionUtil.getIdListFromString(ids);
        return radioDeviceService.batchDeleteDevice(idList, request);
    }

    /**
     * 获取公共广播设备详细信息
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/{id}")
    @RequiresPermissions(value = "dm:module:same:device:broadcast:detail")
    @ApiOperation(value = "获取公共广播设备详细信息", notes = "获取公共广播设备详细信息,作者：LeiJing")
    public Result getDeviceInfo(@PathVariable Integer id, HttpServletRequest request) {
        return radioDeviceService.getDeviceInfo(id, request);
    }

    /**
     * 获取公共广播设备列表
     *
     * @param qo
     * @param request
     * @return
     */
    @GetMapping("/getList")
    @RequiresPermissions(value = "dm:module:same:device:broadcast")
    @ApiOperation(value = "获取公共广播设备列表", notes = "获取公共广播设备列表,作者：LeiJing")
    public Result getDeviceList(PbRadioDeviceQueryObject qo, HttpServletRequest request) {
        //根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if (!flag) {
            qo.setAreaId(user.getAreaId());
        }
        return radioDeviceService.getDeviceList(qo, request);
    }

    /**
     * 批量导入公共广播设备
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/batchImport")
    @RequiresPermissions(value = "dm:module:same:device:broadcast:add")
    @ApiOperation(value = "批量导入公共广播设备", notes = "批量导入公共广播设备,作者：LeiJing")
    @SystemLog(logModul = "设备管理", logType = "导入", logDesc = "导入公共广播设备")
    public Result batchImportDevice(MultipartFile file, HttpServletRequest request) {
        return radioDeviceService.batchImportDevice(file, request);
    }

    /**
     * 公共广播设备名称和编号唯一性验证
     *
     * @param radioDevice
     * @param request
     * @return
     */
    @PostMapping("/uniqueness")
    @RequiresPermissions(value = "dm:module:same:device:broadcast")
    @ApiOperation(value = "公共广播设备名称和编号唯一性验证", notes = "公共广播设备名称和编号唯一性验证,作者：LeiJing")
    public Result uniqueness(@RequestBody RadioDevice radioDevice, HttpServletRequest request) {
        return radioDeviceService.uniqueness(radioDevice, request);
    }

    /**
     * 获取公共广播设备下拉列表
     *
     * @param request
     * @return
     */
    @GetMapping("/getPulldownList")
    @RequiresPermissions(value = "dm:module:same:device:broadcast")
    @ApiOperation(value = "获取公共广播设备下拉列表", notes = "获取公共广播设备下拉列表,作者：LeiJing")
    public Result getDevicePulldownList(HttpServletRequest request) {
        return radioDeviceService.getDevicePulldownList(request);
    }

    /**
     * 根据灯杆id集合查询公共广播设备
     *
     * @author LeiJing
     * @time 2020-03-28
     */
    @GetMapping("/pulldown/by/lamp/post")
    @RequiresPermissions(value = "dm:module:same:device:broadcast")
    @ApiOperation(value = "根据灯杆id集合查询公共广播设备", notes = "根据灯杆id集合查询公共广播设备,作者：LeiJing")
    public Result pulldownByLampPost(@RequestParam(required = false) List<Integer> lampPostIdList, HttpServletRequest request) {
        return radioDeviceService.pulldownByLampPost(lampPostIdList, request);
    }

    /**
     * @explain 查询灯具(根据灯杆id集合)
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PostMapping("/pulldown/by/lamp/post")
    @RequiresPermissions(value = "dm:module:same:device:broadcast")
    @ApiOperation(value = "查询灯具(根据灯杆id集合)", notes = "查询灯具(根据灯杆id集合),作者：Longshuangyang")
    public Result pulldownByLampPostGet(@RequestBody List<Integer> lampPostIdList, HttpServletRequest request) {
        return radioDeviceService.pulldownByLampPost(lampPostIdList, request);
    }

    /**
     * 批量调节公共广播设备音量
     *
     * @param pbReqBatchUpdateVolumeVO
     * @param request
     * @return
     */
    @PutMapping("/updateVolume")
    @RequiresPermissions(value = "dm:module:same:device:broadcast:update")
    @ApiOperation(value = "批量调节公共广播设备音量", notes = "批量调节公共广播设备音量,作者：LeiJing")
    @SystemLog(logModul = "设备管理", logType = "音量调节", logDesc = "批量调节公共广播设备音量")
    public Result updateDeviceVolume(@RequestBody PbReqBatchUpdateVolumeVO pbReqBatchUpdateVolumeVO, HttpServletRequest request) {
        return radioDeviceService.updateDeviceVolume(pbReqBatchUpdateVolumeVO, request);
    }

    @GetMapping("/getTermState")
    @RequiresPermissions(value = "dm:module:same:device:broadcast")
    @ApiOperation(value = "获取终端状态", notes = "获取终端状态,作者：Xiaokun")
    public Result getTermState(@ApiParam(name = "termId", value = "雷拓终端id") Integer termId, HttpServletRequest request) {
        return radioDeviceService.getTermState(termId, request);
    }

    @GetMapping("/getPlayingProgram/{id}")
    @RequiresPermissions(value = "dm:module:same:device:broadcast")
    @ApiOperation(value = "获取设备正在播放的节目", notes = "获取设备正在播放的节目,作者：Xiaokun")
    public Result getPlayingProgram(@ApiParam(name = "id", value = "设备id") @PathVariable("id") Integer id, HttpServletRequest request) {
        return radioDeviceService.getPlayingProgram(id, request);
    }
}