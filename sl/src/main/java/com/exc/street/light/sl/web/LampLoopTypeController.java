/**
 * @filename:LampBrightController 2020-03-20
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampLoopType;
import com.exc.street.light.sl.service.LampLoopTypeService;
import com.exc.street.light.sl.service.SocketIOService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>灯具数据统计控制器</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Haungjinhao
 * @time    2020-03-20
 *
 */
@Api(tags = "回路类型",value="回路类型" )
@RestController
@RequestMapping("/api/sl/lamp/loop/type")
public class LampLoopTypeController {

    @Autowired
    LampLoopTypeService lampLoopTypeService;


    @GetMapping("/list")
    @ApiOperation(value = "回路类型集合", notes = "回路类型集合,作者：Huangjinahao")
    @RequiresPermissions(value = "dm:module:same:device:light:control")
    public Result list(HttpServletRequest request) {
        List<LampLoopType> list = lampLoopTypeService.list();
        return new Result().success(list);
    }

}