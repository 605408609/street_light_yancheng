/**
 * @filename:ControlLoopSceneController 2020-08-24
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.web;

import com.exc.street.light.dlm.service.ControlLoopSceneService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.vo.req.DlmReqControlLoopSceneVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: xiezhipeng
 * @time    2020-08-24
 *
 */
@Api(tags = "回路场景控制器")
@RestController
@RequestMapping("/api/dlm/control/loop/scene")
public class ControlLoopSceneController {

    @Autowired
    private ControlLoopSceneService controlLoopSceneService;

    @PostMapping
    @ApiOperation(value="场景下发", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:power:distribution:cabinet:circuit:issue")
    @SystemLog(logModul = "设备位置管理", logType = "下发", logDesc = "场景下发")
    public Result issueLoopScene(@RequestBody DlmReqControlLoopSceneVO sceneVO, HttpServletRequest request){
        return controlLoopSceneService.issueLoopScene(sceneVO, request);
    }
	
}