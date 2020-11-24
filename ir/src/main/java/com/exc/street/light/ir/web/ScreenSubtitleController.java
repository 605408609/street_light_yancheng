/**
 * @filename:ScreenSubtitleController 2020-10-23
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ir.web;

import com.exc.street.light.ir.service.ScreenSubtitleService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.vo.req.IrReqScreenSubtitlePlayVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： 显示屏字幕表Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time    2020-10-23
 *
 */
@Api(tags = "显示屏字幕表",value="显示屏字幕表" )
@RestController
@RequestMapping("/api/ir/screen/subtitle")
public class ScreenSubtitleController{

    @Autowired
    private ScreenSubtitleService screenSubtitleService;

    /**
     * 发送字幕
     *
     * @param irReqScreenSubtitlePlayVO
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/send")
//    @RequiresPermissions(value = "sl:info:program:issue")
    public Result sendSubtitle(@RequestBody IrReqScreenSubtitlePlayVO irReqScreenSubtitlePlayVO, HttpServletRequest httpServletRequest) {
        return screenSubtitleService.sendSubtitle(irReqScreenSubtitlePlayVO, httpServletRequest);
    }




}