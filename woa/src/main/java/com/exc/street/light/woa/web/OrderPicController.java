/**
 * @filename:OrderPicController 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.woa.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.woa.service.OrderPicService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>工单图片控制器</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time    2020-03-28
 *
 */
@Api(tags = "工单图片控制器",value="工单图片控制器" )
@RestController
@RequestMapping("/api/woa/order/pic")
public class OrderPicController {

    @Autowired
    private OrderPicService orderPicService;

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/upload")
    @ApiOperation(value = "上传图片", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = {"permission:module:order:create:add", "permission:module:alarm:manage:add"}, logical = Logical.OR)
    @SystemLog(logModul = "工单告警",logType = "上传图片",logDesc = "上传图片")
    public Result addResource(@RequestParam("file") MultipartFile file,
                              HttpServletRequest httpServletRequest) {
        return orderPicService.uploadImage(file, httpServletRequest);
    }

    /**
     * 根据图片id删除图片
     *
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "根据图片id删除图片", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = {"permission:module:order:create:add", "permission:module:alarm:manage:add"}, logical = Logical.OR)
    @SystemLog(logModul = "工单告警",logType = "根据图片id删除图片",logDesc = "根据图片id删除图片")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        return orderPicService.delete(id, request);
    }
}