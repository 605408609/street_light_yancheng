package com.exc.street.light.electricity.web;

import com.exc.street.light.electricity.service.CanChannelTypeService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.electricity.CanChannelType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 回路类型控制器
 *
 * @author Linshiwen
 * @date 2018/05/23
 */
@Api(tags = "回路类型控制器")
@ApiIgnore
@RestController
@RequestMapping("/api/can/channel/type")
public class CanChannelTypeController {
    @Autowired
    private CanChannelTypeService canChannelTypeService;

    /**
     * 回路类型列表
     *
     * @return 列表数据
     */
    @GetMapping
    @ApiOperation(value = "回路类型列表",notes = "回路类型列表")
    @RequiresPermissions(value = "electricity:manage:node:list")
    public Result list() {
        List<CanChannelType> list = canChannelTypeService.list();
        return new Result().success(list);
    }
    /**
     * 继电器回路类型列表
     *
     * @return 列表数据
     */
    @GetMapping("/relay")
    @RequiresPermissions(value = "electricity:manage:scene:list")
    public Result listRelay() {
        List<CanChannelType> list = canChannelTypeService.getRelayType();
        return new Result().success(list);
    }
}
