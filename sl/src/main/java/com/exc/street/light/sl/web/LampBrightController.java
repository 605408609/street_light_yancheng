/**
 * @filename:LampBrightController 2020-03-20
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.sl.service.SocketIOService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>灯具数据统计控制器</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time    2020-03-20
 *
 */
@Api(tags = "灯具数据统计控制器",value="灯具数据统计控制器" )
@RestController
@RequestMapping("/api/sl/lamp/bright")
public class LampBrightController{

    @Autowired
    SocketIOService socketIOService;

    /**
     * ota升级
     * @param request
     * @param multipartFile
     * @param num
     * @return
     */
    @PostMapping("/ota")
    @ApiOperation(value = "ota升级", notes = "ota升级,作者：Huangjinahao")
    @RequiresPermissions(value = "sl:module")
    public Result ota(HttpServletRequest request, @RequestParam(required = true) MultipartFile multipartFile,@RequestParam(required = true) String num,@RequestParam(required = true) String model,@RequestParam(required = true) String factory) {
        return socketIOService.writeFile(multipartFile,num,model,factory,request);
    }

}