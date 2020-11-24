package com.exc.street.light.electricity.web;

import com.exc.street.light.electricity.service.CanSupervisionTypeService;
import com.exc.street.light.resource.core.Result;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 监控点类型控制器
 *
 * @author Linshiwen
 * @date 2018/05/23
 */
@Api(tags = "监控点类型控制器")
@RestController
@RequestMapping("/api/can/supervision/type")
public class CanSupervisionTypeController {
    @Autowired
    private CanSupervisionTypeService canSupervisionTypeService;

    /**
     * 监控类型列表
     *
     * @return 列表数据
     */
    @GetMapping
    @RequiresPermissions(value = "electricity:manage:node:list")
    public Result list() {
        return new Result().success(canSupervisionTypeService.list());
    }
}
