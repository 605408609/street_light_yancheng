/**
 * @filename:SsDeviceController 2020-03-17
 * @project ss  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ss.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.PageParam;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ss.SsDevice;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.SsDeviceVO;
import com.exc.street.light.ss.dto.PreviewUrlsDTO;
import com.exc.street.light.ss.manager.CamerasPreviewUrlsManager;
import com.exc.street.light.ss.service.impl.SsDeviceServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： Controller</P>
 *
 * @version: V1.0
 * @author: Huang Min
 * @time 2020-03-17
 */
@Api(description = "", value = "")
@RestController
@CrossOrigin
@RequestMapping("/ss/device")
public class SsDeviceController {

    @Autowired
    protected SsDeviceServiceImpl ssDeviceService;

    @Autowired
    private CamerasPreviewUrlsManager camerasPreviewUrlsManager;

    protected Result result = new Result<>();

    /**
     * @explain 查询对象  <swagger GET请求>
     * @author Huang Min
     * @time 2020-03-17
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取对象", notes = "作者：Huang Min")
    @ApiImplicitParam(paramType = "path", name = "id", value = "对象id", required = true, dataType = "Long")
    @RequiresPermissions(value = "dm:module:same:device:camera:detail")
//    @SystemLog(logModul = "智能安防",logType = "查询对象",logDesc = "查询设备对象")
    public Result<SsDevice> getById(@PathVariable("id") Long id) {
        Result<SsDevice> result = new Result<>();
        SsDevice ssDevice = ssDeviceService.getById(id);
        if (null != ssDevice) {
            String num = ssDevice.getNum();
            if (StringUtils.isNotBlank(num)) {
                PreviewUrlsDTO previewUrlsDTO = camerasPreviewUrlsManager.getPreviewUrlsDtoByNum(num);
                ssDevice.setCameraPreviewUrl(previewUrlsDTO != null ? previewUrlsDTO.getCameraPreviewUrl() : null);
            }
            result.success(ssDevice);
        } else {
            result.error("查询对象不存在！");
        }
        return result;
    }

    /**
     * @explain 修改
     * @author Huang Min
     * @time 2020-03-17
     */
    @PutMapping
    @ApiOperation(value = "修改", notes = "作者：Huang Min")
    @RequiresPermissions(value = "dm:module:same:device:camera:update")
    @SystemLog(logModul = "智能安防",logType = "修改",logDesc = "修改摄像头")
    public Result<SsDevice> update(@RequestBody SsDevice ssDevice) {
        Result<SsDevice> result = new Result<>();
        if (null != ssDevice) {
            boolean rsg = ssDeviceService.updateById(ssDevice);
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

    /**
     * 设备唯一性校验
     *
     * @param t 设备表对象
     * @return
     */
    @PostMapping("/uniqueness")
    @ApiOperation(value = "设备唯一性校验", notes = "作者：Huang Min")
    @RequiresPermissions(value = "dm:module:same:device:camera")
    @SystemLog(logModul = "智能安防",logType = "设备唯一性校验",logDesc = "校验设备编号、名称的唯一性")
    public Result uniqueness(@RequestBody SsDevice t) {
        return ssDeviceService.uniqueness(t);
    }

    /**
     * @explain 分页条件查询、APP的接口
     * @author Huang Min
     * @time 2020-03-17
     */
    @GetMapping("/app/{id}")
    @ApiOperation(value = "分页条件查询、APP的接口", notes = "分页查询,作者：Huang Min")
    @RequiresPermissions(value = "dm:module:same:device:camera")
//    @SystemLog(logModul = "智能安防",logType = "分页条件查询",logDesc = "分页条件查询、APP的接口")
    public Result<IPage<SsDeviceVO>> getPages(@PathVariable("id") Long id) {
        Result<IPage<SsDeviceVO>> returnPage = new Result<IPage<SsDeviceVO>>();
        Page<SsDeviceVO> page = new Page<SsDeviceVO>(0, 10);
        //分页数据
        IPage<SsDeviceVO> pageData = ssDeviceService.selectByIdWithApp(page, id);
        returnPage.success(pageData);

        return returnPage;
    }

    /**
     * @explain 添加
     * @author Huang Min
     * @time 2020-03-17
     */
    @PostMapping
    @ApiOperation(value = "添加", notes = "作者：Huang Min")
    @RequiresPermissions(value = "dm:module:same:device:camera:add")
    @SystemLog(logModul = "智能安防",logType = "添加",logDesc = "添加摄像头")
    public Result insert(@RequestBody SsDevice entity) {
        Result result = new Result();
        if (null != entity) {
            entity.setCreateTime(new Date());
            boolean rsg = ssDeviceService.save(entity);
            if (rsg) {
                result.success("添加成功");
            } else {
                result.error("添加失败！");
            }
        } else {
            result.error("请传入正确参数！");
        }
        return result;
    }

    /**
     * @explain 删除对象
     * @author Huang Min
     * @time 2020-03-17
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "作者：Huang Min")
    @ApiImplicitParam(paramType = "query", name = "id", value = "对象id", required = true, dataType = "Long")
    @RequiresPermissions(value = "dm:module:same:device:camera:delete")
    @SystemLog(logModul = "智能安防",logType = "删除",logDesc = "删除摄像头")
    public Result deleteById(@PathVariable("id") Long id) {
        Result result = new Result();
        SsDevice obj = ssDeviceService.getById(id);
        if (null != obj) {
            boolean rsg = ssDeviceService.removeById(id);
            if (rsg) {
                result.success("删除成功");
            } else {
                result.error("删除失败！");
            }
        } else {
            result.error("删除的对象不存在！");
        }
        return result;
    }

    /**
     * @explain 批量删除
     * @author Huang Min
     * @time 2020-03-12
     */
    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除", notes = "作者：Huang Min")
    @ApiImplicitParam(paramType = "query", name = "ids", value = "多个设备ID,使用逗号分隔", required = true, dataType = "String")
    @RequiresPermissions(value = "dm:module:same:device:camera:delete")
    @SystemLog(logModul = "智能安防",logType = "批量删除",logDesc = "批量删除摄像头")
    public Result<SsDevice> deleteByIdList(String ids) {
        List<Integer> idList = StringConversionUtil.getIdListFromString(ids);

        boolean rsg = ssDeviceService.removeByIds(idList);
        if (rsg) {
            result.success("设备删除成功");
        } else {
            result.error("设备删除失败！");
        }
        return result;
    }

    /**
     * @explain 分页条件查询
     * @author Huang Min
     * @time 2020-03-12
     */
    @GetMapping("page")
    @ApiOperation(value = "分页查询", notes = "分页查询,作者：Huang Min")
    @RequiresPermissions(value = "dm:module:same:device:camera")
//    @SystemLog(logModul = "智能安防",logType = "分页查询",logDesc = "分页查询设备")
    public Result<IPage<SsDeviceVO>> getPages(PageParam<SsDeviceVO> param, SsDeviceVO t, HttpServletRequest request) {
        Result<IPage<SsDeviceVO>> returnPage = new Result<IPage<SsDeviceVO>>();
        Page<SsDeviceVO> page = new Page<SsDeviceVO>(param.getPageNum(), param.getPageSize());
        QueryWrapper<SsDeviceVO> queryWrapper = new QueryWrapper<SsDeviceVO>();
        queryWrapper.setEntity(t);
        //分页数据
        IPage<SsDeviceVO> pageData = ssDeviceService.page(page, queryWrapper, t,request);
        returnPage.success(pageData);

        return returnPage;
    }


    /**
     * @explain 查询灯具(根据灯杆id集合)
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/pulldown/by/lamp/post")
    @RequiresPermissions(value = "dm:module:same:device:camera")
    @ApiOperation(value = "查询灯具(根据灯杆id集合)Get请求", notes = "查询灯具(根据灯杆id集合),作者：Longshuangyang")
    public Result pulldownByLampPost(@RequestParam(required = false) List<Integer> lampPostIdList, HttpServletRequest request) {
        return ssDeviceService.pulldownByLampPost(lampPostIdList, request);
    }

    /**
     * @explain 查询灯具(根据灯杆id集合)
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PostMapping("/pulldown/by/lamp/post")
    @RequiresPermissions(value = "dm:module:same:device:camera")
    @ApiOperation(value = "查询灯具(根据灯杆id集合)", notes = "查询灯具(根据灯杆id集合),作者：Longshuangyang")
    public Result pulldownByLampPostGet(@RequestBody List<Integer> lampPostIdList, HttpServletRequest request) {
        return ssDeviceService.pulldownByLampPost(lampPostIdList, request);
    }
}