/**
 * @filename:RadioMaterialController 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.pb.web;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.pb.service.RadioMaterialService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.PbRadioMaterialQueryObject;
import com.exc.street.light.resource.utils.StringConversionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: LeiJing
 * @time 2020-03-21
 *
 */
@Api(tags = "广播素材", value = "")
@RestController
@RequestMapping("/api/pb/radio/material")
public class RadioMaterialController {
    @Autowired
    private RadioMaterialService radioMaterialService;

    @PostMapping
    @ApiOperation(value = "新增公共广播素材", notes = "新增公共广播素材,作者：LeiJing")
    @RequiresPermissions(value = "pb:module:material:upload")
    @SystemLog(logModul = "公共广播",logType = "新增",logDesc = "新增公共广播素材")
//    public Result addMaterial(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
//    public Result addMaterial(@ApiParam @RequestBody MultipartFile file, HttpServletRequest request) {
    public Result addMaterial(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return radioMaterialService.add(file, request);
    }

//    @PutMapping
//    @ApiOperation(value = "编辑公共广播素材", notes = "编辑公共广播素材,作者：LeiJing")
//    public Result updateMaterial(@ApiParam @RequestBody RadioMaterial radioMaterial, HttpServletRequest request) {
//        return radioMaterialService.update(radioMaterial, request);
//    }

    @DeleteMapping("/{id}")
    @RequiresPermissions(value = "pb:module:material:delete")
    @SystemLog(logModul = "公共广播",logType = "删除",logDesc = "删除公共广播素材")
    @ApiOperation(value = "删除公共广播素材", notes = "删除公共广播素材,作者：LeiJing")
    public Result deleteMaterial(@PathVariable Integer id, HttpServletRequest request) {
        return radioMaterialService.delete(id, request);
    }

    @DeleteMapping("/batch")
    @RequiresPermissions(value = "pb:module:material:delete")
    @ApiOperation(value = "批量删除公共广播素材", notes = "批量删除公共广播素材,作者：LeiJing")
    @SystemLog(logModul = "公共广播",logType = "删除",logDesc = "批量新增公共广播素材")
    public Result batchDeleteMaterial(@RequestParam String ids, HttpServletRequest request) {
        List<Integer> idList = StringConversionUtil.getIdListFromString(ids);
        return radioMaterialService.batchDelete(idList);
    }

    @GetMapping("/{id}")
    @RequiresPermissions(value = "pb:module:material")
    @ApiOperation(value = "获取公共广播素材详细信息", notes = "获取公共广播素材详细信息,作者：LeiJing")
    public Result getMaterialInfo(@PathVariable Integer id, HttpServletRequest request) {
        return radioMaterialService.getInfo(id, request);
    }

    @GetMapping("/getList")
    @RequiresPermissions(value = "pb:module:material")
    @ApiOperation(value = "获取公共广播素材列表", notes = "获取公共广播素材列表,作者：LeiJing")
    public Result getMaterialList(PbRadioMaterialQueryObject qo, HttpServletRequest request) {
        return radioMaterialService.getList(qo, request);
    }
}