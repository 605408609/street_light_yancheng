/**
 * @filename:ScreenMaterialController 2020-04-02
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.exc.street.light.ir.service.ScreenMaterialService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenMaterial;
import com.exc.street.light.resource.qo.IrMaterialQueryObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>素材控制器</p>
 *
 * <p>说明： Controller</P>
 *
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-04-02
 */
@Api(tags = "素材控制器", value = "素材控制器")
@RestController
@RequestMapping("/api/ir/screen/material")
public class ScreenMaterialController {

    @Autowired
    private ScreenMaterialService screenMaterialService;

    /**
     * 上传文本素材
     *
     * @param material
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/upload/text")
    @ApiOperation(value = "上传文本素材", notes = "上传文本素材,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:material:upload:text")
    @SystemLog(logModul = "信息发布",logType = "上传",logDesc = "上传文本素材")
    public Result uploadText(@RequestBody ScreenMaterial material, HttpServletRequest httpServletRequest) {
        return screenMaterialService.uploadText(material, httpServletRequest);
    }

    /**
     * 上传视频图片
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/upload")
    @ApiOperation(value = "上传视频图片", notes = "上传视频图片,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:material:upload:video")
    @SystemLog(logModul = "信息发布",logType = "上传",logDesc = "上传视频图片")
    public Result addResource(@RequestParam("file") MultipartFile file,
                              HttpServletRequest httpServletRequest) {
        return screenMaterialService.uploadVideoImage(file, httpServletRequest);
    }

    /**
     * 获取素材库列表(分页查询)
     *
     * @param IrMaterialQueryObject
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取素材库列表(分页查询)", notes = "获取素材库列表(分页查询),作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:material")
    public Result list(IrMaterialQueryObject IrMaterialQueryObject, HttpServletRequest httpServletRequest) {
        return screenMaterialService.getQuery(IrMaterialQueryObject, httpServletRequest);
    }

    /**
     * 根据素材id删除素材
     *
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "根据素材id删除素材", notes = "根据素材id删除素材,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:material:delete")
    @SystemLog(logModul = "信息发布",logType = "删除",logDesc = "根据素材id删除素材")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        return screenMaterialService.delete(id, request);
    }

    /**
     * 批量删除显示屏素材
     *
     * @param ids
     * @param request
     * @return
     */
    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除显示屏素材", notes = "批量删除显示屏素材,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:material:delete")
    @SystemLog(logModul = "信息发布",logType = "批量删除",logDesc = "批量删除显示屏素材")
    public Result batchDelete(@RequestParam String ids, HttpServletRequest request) {
        return screenMaterialService.batchDelete(ids, request);
    }

    /**
     * 根据素材id获取关联节目列表
     *
     * @param ids
     * @return
     */
    @GetMapping("/program")
    @ApiOperation(value = "根据素材id获取关联节目列表", notes = "根据素材id获取关联节目列表,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:program")
    public Result programList(@RequestParam("ids") String ids, HttpServletRequest request) {
        return screenMaterialService.programList(ids, request);
    }

}