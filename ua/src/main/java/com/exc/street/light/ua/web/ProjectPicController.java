/**
 * @filename:ProjectPicController 2020-10-20
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ua.web;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.ua.service.ProjectPicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： Controller</P>
 *
 * @version: V1.0
 * @author: xiezhipeng
 * @time 2020-10-20
 */
@Api(tags = "项目图片控制器", value = "项目图片控制器")
@RestController
@RequestMapping("/api/ua/project/pic")
public class ProjectPicController {

    @Autowired
    private ProjectPicService projectPicService;

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/upload")
    @ApiOperation(value = "上传图片", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "view:data")
    @SystemLog(logModul = "位置管理",logType = "上传图片",logDesc = "上传图片")
    public Result add(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {
        return projectPicService.uploadPic(file, httpServletRequest);
    }

    /**
     * 根据图片id删除图片
     *
     * @param picId
     * @param request
     * @return
     */
    @DeleteMapping("/delete/{picId}")
    @ApiOperation(value = "根据id删除图片", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "view:data")
    @SystemLog(logModul = "位置管理",logType = "删除图片",logDesc = "删除图片")
    public Result delete(@PathVariable Integer picId, HttpServletRequest request) {
        return projectPicService.deletePicById(picId, request);
    }

}