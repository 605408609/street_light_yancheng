package com.exc.street.light.electricity.web;

import com.exc.street.light.electricity.service.ComDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.electricity.ComDevice;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @author Linshiwen
* @date 2018/07/04
*/
@Api(tags = "")
@RestController
@RequestMapping("/api/com/device")
public class ComDeviceController {
    @Autowired
    private ComDeviceService comDeviceService;

    @PostMapping
    public Result add(@RequestBody ComDevice comDevice) {
        comDeviceService.save(comDevice);
        return new Result().success("");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        comDeviceService.removeById(id);
        return new Result().success("");
    }

    @PutMapping
    public Result update(@RequestBody ComDevice comDevice) {
        comDeviceService.updateById(comDevice);
        return new Result().success("");
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        ComDevice comDevice = comDeviceService.getById(id);
        return new Result().success(comDevice);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        /*PageHelper.startPage(page, size);
        List<ComDevice> list = comDeviceService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.getSuccessResult(pageInfo);*/
        //todo
        return null;
    }
}
