/**
 * @filename:SystemDeviceController 2020-09-02
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.SlLampDeviceQuery;
import com.exc.street.light.resource.vo.req.SlReqInstallLampZkzlVO;
import com.exc.street.light.resource.vo.req.SlReqLightControlVO;
import com.exc.street.light.resource.vo.req.SlSystemDeviceUpdateReqVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamReqVO;
import com.exc.street.light.sl.service.SystemDeviceService;
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
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： 设备表Controller</P>
 * @version: V1.0
 * @author: Huang Jin Hao
 * @time 2020-09-02
 *
 */
@Api(tags = "设备表", value = "设备表")
@RestController
@RequestMapping("/api/sl/system/device")
public class SystemDeviceController {

    @Autowired
    private SystemDeviceService systemDeviceService;

    /**
     * @explain 添加灯控设备（临时ZKZL添加接口）
     * @author Huangjinhao
     * @time 2020-09-07
     */
    @PostMapping("/addZkzl")
    @ApiOperation(value = "添加灯控设备", notes = "作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:light:control:add")
    public Result addZkzl(@ApiParam @RequestBody SingleLampParamReqVO singleLampParamRespVO, HttpServletRequest request) {
        return systemDeviceService.addZkzl(singleLampParamRespVO, request);
    }

    /**
     * @explain 灯控设备验证唯一性(NH)
     * @author Huangjinhao
     * @time 2020-03-17
     */
    @PostMapping("/unique")
    @ApiOperation(value = "灯控设备验证唯一性", notes = "灯控设备验证唯一性,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:light:control:add")
    public Result unique(@ApiParam @RequestBody SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request) {
        return systemDeviceService.uniqueNh(singleLampParamReqVO, request);
    }

    /**
     * @explain 灯控设备批量验证唯一性
     * @author Huangjinhao
     * @time 2020-03-17
     */
    @PostMapping("/uniqueBatch")
    @ApiOperation(value = "灯控设备批量验证唯一性", notes = "灯控设备批量验证唯一性,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:light:control:add")
    public Result uniqueBatch(@ApiParam @RequestBody List<SingleLampParamReqVO> singleLampParamReqVOS, HttpServletRequest request) {
        return systemDeviceService.uniqueBatch(singleLampParamReqVOS, request);
    }

    /**
     * @explain 灯控设备分页条件查询
     * @author Huangjinhao
     * @time 2020-09-07
     */
    @GetMapping("/page")
    @ApiOperation(value = "灯控设备分页条件查询", notes = "灯控设备分页条件查询,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:light:control")
    public Result getPage(SlLampDeviceQuery slLampDeviceQuery, HttpServletRequest request) {
        return systemDeviceService.getPage(slLampDeviceQuery, request);
    }

    /**
     * @explain 修改灯控设备
     * @author Longshuangyang
     * @time 2020-03-16
     */
    /*@PutMapping("/update")
    @ApiOperation(value = "修改灯控设备", notes = "修改灯控设备,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control:update")
    @SystemLog(logModul = "设备管理",logType = "编辑",logDesc = "修改灯控设备")
    public Result updateDevice(@ApiParam @RequestBody SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request) {
        return systemDeviceService.updateDevice(singleLampParamReqVO, request);
    }*/

    /**
     * @explain 批量修改灯控设备
     * @author Huangjinhao
     * @time 2020-03-16
     */
    @PutMapping("/updateBatch")
    @ApiOperation(value = "批量修改灯控设备", notes = "批量修改灯控设备,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:light:control:update")
    @SystemLog(logModul = "设备管理",logType = "编辑",logDesc = "修改灯控设备")
    public Result updateDeviceBatch(@ApiParam @RequestBody List<SingleLampParamReqVO> singleLampParamReqVOS, HttpServletRequest request) {
        return systemDeviceService.updateDeviceBatch(singleLampParamReqVOS, request);
    }

    /**
     * @explain 灯控设备详情
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "灯控设备详情", notes = "灯控设备详情,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:light:control:detail")
    public Result detail(@PathVariable Integer id, HttpServletRequest request) {
        return systemDeviceService.detail(id, request);
    }

    @GetMapping("/by/idList")
    @ApiOperation(value = "根据设备id集合查询对象列表", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "view:data")
    public Result getDeviceListByIdList(@RequestParam("deviceIdList") List<Integer> deviceIdList, HttpServletRequest request) {
        return systemDeviceService.getDeviceListByIdList(deviceIdList, request);
    }

    /**
     * @explain 删除设备
     * @author hjh
     * @time 2020-03-23
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除设备", notes = "删除设备,作者：hjh")
    @RequiresPermissions(value = "dm:module:same:device:light:control:delete")
    public Result delete(@PathVariable Integer id,HttpServletRequest request) {
        return systemDeviceService.delete(id, request);
    }

    /**
     * 实时控制
     *
     * @param request
     * @param vo      控制参数
     * @return
     */
    @PostMapping("/control")
    @ApiOperation(value = "下发控制", notes = "下发控制,作者：Huangjinhao")
    @RequiresPermissions(value = {"sl:module:light:strategy:light", "sl:module:map:light"}, logical = Logical.OR)
    @SystemLog(logModul = "智慧照明",logType = "控制",logDesc = "下发控制")
    public Result control(HttpServletRequest request, @RequestBody SlReqLightControlVO vo) {
        return systemDeviceService.control(request, vo);
    }

    /**
     * 按组实时控制
     *
     * @param request
     * @param vo      控制参数
     * @return
     */
    @PostMapping("/controlByGroup")
    @ApiOperation(value = "按组下发控制", notes = "按组下发控制,作者：Huangjinhao")
    @RequiresPermissions(value = {"sl:module:light:strategy:light", "sl:module:map:light"}, logical = Logical.OR)
    @SystemLog(logModul = "智慧照明",logType = "控制",logDesc = "按组下发控制")
    public Result controlByGroup(HttpServletRequest request, @RequestBody SlReqLightControlVO vo) {
        return systemDeviceService.controlByGroup(request, vo);
    }

    @PostMapping("/register")
    @ApiOperation(value = "ZKZL设备注册", notes = "ZKZL设备注册,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:light:control:add")
    public Result registerDevice(HttpServletRequest request, @RequestBody SlReqInstallLampZkzlVO slReqInstallLampZkzlVO) {
        return systemDeviceService.registerDevice(request, slReqInstallLampZkzlVO);
    }

    @PostMapping("/relieveRegister")
    @ApiOperation(value = "ZKZL设备注册", notes = "ZKZL设备注册,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:light:control:add")
    public Result relieveRegister(HttpServletRequest request, @RequestBody List<SlReqInstallLampZkzlVO> slReqInstallLampZkzlVOList) {
        return systemDeviceService.relieveRegisterDevice(request, slReqInstallLampZkzlVOList);
    }
}