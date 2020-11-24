/**
 * @filename:LampBrightController 2020-03-20
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampLoopType;
import com.exc.street.light.resource.entity.sl.LampPosition;
import com.exc.street.light.sl.service.LampLoopTypeService;
import com.exc.street.light.sl.service.LampPositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Api(tags = "灯具位置",value="灯具位置" )
@RestController
@RequestMapping("/api/sl/lamp/position")
public class LampPositionController {

    @Autowired
    LampPositionService lampPositionService;

    @GetMapping("/list")
    @ApiOperation(value = "灯具位置集合", notes = "灯具位置集合,作者：Huangjinahao")
    @RequiresPermissions(value = "dm:module:same:device:light:control")
    public Result list(HttpServletRequest request) {
        List<LampPosition> list = lampPositionService.list();
        return new Result().success(list);
    }

}