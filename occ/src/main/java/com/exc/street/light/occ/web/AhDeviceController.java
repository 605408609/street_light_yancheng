/**
 * @filename:AhDeviceController 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.occ.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.occ.service.AhDeviceService;
import com.exc.street.light.resource.core.PageParam;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.occ.AhDevice;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.OccAhDeviceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： Controller</P>
 *
 * @version: V1.0
 * @author: Huang Min
 * @time 2020-03-16
 */
@Api(description = "", value = "")
@RestController
@CrossOrigin
@RequestMapping("/ah/device")
public class AhDeviceController {

    protected Result<OccAhDeviceVO> resultVO = new Result<OccAhDeviceVO>();

    protected Result result = new Result();

    @Autowired
    protected AhDeviceService ahDeviceService;

    /**
     * @explain 查询对象  <swagger GET请求>
     * @author Huang Min
     * @time 2020-03-17
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取对象", notes = "作者：Huang Min")
    @ApiImplicitParam(paramType = "path", name = "id", value = "对象id", required = true, dataType = "Long")
    @RequiresPermissions(value = "dm:module:same:device:call:detail")
    public Result<AhDevice> getById(@PathVariable("id") Long id) {
        AhDevice obj = ahDeviceService.getById(id);
        if (null != obj) {
            result.success(obj);
        } else {
            result.error("查询对象不存在！");
        }
        return result;
    }

    /**
     * @explain 修改
     * @author Huang Min
     * @time 2020-03-16
     */
    @PutMapping
    @ApiOperation(value = "修改", notes = "作者：Huang Min")
    @RequiresPermissions(value = "dm:module:same:device:call:update")
    @SystemLog(logModul = "紧急呼叫",logType = "修改",logDesc = "修改报警盒设备对象")
    public Result<AhDevice> update(@RequestBody AhDevice ahDevice) {
        if (null != ahDevice) {
            boolean rsg = ahDeviceService.updateById(ahDevice);
            if (rsg) {
                result.success("修改成功");
            } else {
                result.error("修改失败！");
            }
        } else {
            result.error("请传入正确参数！");
        }
        return result;
    }
    /**
     * @explain 修改设备注册状态为成功,程序内部接口 /api/occ/ah/device/updateStateById
     * @author Huang Min
     * @time 2020-03-16
     */
    @PostMapping(value = "/updateStateById")
    public void updateStateById(@RequestBody AhDevice ahDevice) {
        if (null != ahDevice) {
            boolean rsg = ahDeviceService.updateById(ahDevice);
            if (rsg) {
                result.success("修改成功");
            } else {
                result.error("修改失败！");
            }
        } else {
            result.error("请传入正确参数！");
        }
    }

    /**
     * 唯一性校验
     *
     * @param t 设备表对象
     * @return
     */
    @PostMapping("/uniqueness")
    @RequiresPermissions(value = "dm:module:same:device:call")
    @SystemLog(logModul = "紧急呼叫",logType = "校验对象唯一性",logDesc = "校验对象唯一性，检验参数包括‘设备编号’，‘设备名称’")
    public Result uniqueness(@RequestBody AhDevice t) {
        return ahDeviceService.uniqueness(t);
    }

    /**
     * @explain 紧急呼叫  <swagger GET请求>
     * @author Huang Min
     * @time 2020-03-17
     */
    @GetMapping("/app/{id}")
    @ApiOperation(value = "查询对象（APP工作台）", notes = "作者：Huang Min")
    @ApiImplicitParam(paramType = "path", name = "id", value = "灯杆id", required = true, dataType = "Long")
    @RequiresPermissions(value = "dm:module:same:device:call")
    public Result<OccAhDeviceVO> getInfoByWorkbench(@PathVariable("id") Long id) {
        OccAhDeviceVO obj = ahDeviceService.selectInfoByWorkbench(id);
        if (null != obj) {
            resultVO.success(obj);
        } else {
            resultVO.error("查询对象不存在！");
        }
        return resultVO;
    }

    /**
     * @explain 添加
     * @author Huang Min
     * @time 2020-03-16
     */
    @PostMapping
    @ApiOperation(value = "添加", notes = "作者：Huang Min")
    @RequiresPermissions(value = "dm:module:same:device:call:add")
    @SystemLog(logModul = "紧急呼叫",logType = "新增",logDesc = "新增报警盒设备对象")
    public Result insert(@RequestBody AhDevice entity) {
        Result result = new Result();
        if (null != entity) {
            entity.setCreateTime(new Date());
            boolean rsg = ahDeviceService.save(entity);
            if (rsg) {
                result.success("添加成功");
            } else {
                result.error("添加失败！");
            }
        } else {
            result.error("请传入正确参数！");
        }
        return result;
    }

    /**
     * @explain 删除对象
     * @author Huang Min
     * @time 2020-03-17
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "作者：Huang Min")
    @ApiImplicitParam(paramType = "query", name = "id", value = "对象id", required = true, dataType = "Long")
    @RequiresPermissions(value = "dm:module:same:device:call:delete")
    @SystemLog(logModul = "紧急呼叫",logType = "删除",logDesc = "删除报警盒设备对象")
    public Result deleteById(@PathVariable("id") Long id) {
        Result result = new Result();
        AhDevice obj = ahDeviceService.getById(id);
        if (null != obj) {
            boolean rsg = ahDeviceService.removeById(id);
            if (rsg) {
                result.success("删除成功");
            } else {
                result.error("删除失败！");
            }
        } else {
            result.error("删除的对象不存在！");
        }
        return result;
    }

    /**
     * @explain 删除对象
     * @author Huang Min
     * @time 2020-03-12
     */
    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除", notes = "作者：Huang Min")
    @ApiImplicitParam(paramType = "query", name = "ids", value = "多个设备ID,使用逗号分隔", required = true, dataType = "String")
    @RequiresPermissions(value = "dm:module:same:device:call:delete")
    @SystemLog(logModul = "紧急呼叫",logType = "批量删除",logDesc = "批量删除报警盒设备对象")
    public Result<AhDevice> deleteByIdList(String ids) {
        List<Integer> idList = StringConversionUtil.getIdListFromString(ids);

        boolean rsg = ahDeviceService.removeByIds(idList);
        if (rsg) {
            result.success("设备删除成功");
        } else {
            result.error("设备删除失败！");
        }
        return result;
    }

    /**
     * @explain 分页条件查询
     * @author Huang Min
     * @time 2020-03-12
     */
    @GetMapping("page")
    @ApiOperation(value = "分页查询", notes = "分页查询,作者：Huang Min")
    @RequiresPermissions(value = "dm:module:same:device:call")
    public Result<IPage<OccAhDeviceVO>> getPages(PageParam<OccAhDeviceVO> param, OccAhDeviceVO t, HttpServletRequest request) {
        Result<IPage<OccAhDeviceVO>> returnPage = new Result<IPage<OccAhDeviceVO>>();
        Page<OccAhDeviceVO> page = new Page<OccAhDeviceVO>(param.getPageNum(), param.getPageSize());
        QueryWrapper<OccAhDeviceVO> queryWrapper = new QueryWrapper<OccAhDeviceVO>();
        queryWrapper.setEntity(t);
        //分页数据
        IPage<OccAhDeviceVO> pageData = ahDeviceService.page(page, queryWrapper, t,request);
        returnPage.success(pageData);

        return returnPage;
    }

    /**
     * @explain 查询灯具(根据灯杆id集合)
     * @author Longshuangyang
     * @time 2020-03-16
     */
    
    @GetMapping("/pulldown/by/lamp/post")
    @RequiresPermissions(value = "dm:module:same:device:call:detail")
    @ApiOperation(value = "查询灯具(根据灯杆id集合)", notes = "查询灯具(根据灯杆id集合),作者：Longshuangyang")
    public Result pulldownByLampPost(@RequestParam(required = false) List<Integer> lampPostIdList, HttpServletRequest request) {
        return ahDeviceService.pulldownByLampPost(lampPostIdList, request);
    }

    /**
     * @explain 查询灯具(根据灯杆id集合)
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PostMapping("/pulldown/by/lamp/post")
    @RequiresPermissions(value = "dm:module:same:device:call:detail")
    @ApiOperation(value = "查询灯具(根据灯杆id集合)", notes = "查询灯具(根据灯杆id集合),作者：Longshuangyang")
    public Result pulldownByLampPostGet(@RequestBody List<Integer> lampPostIdList, HttpServletRequest request) {
    	return ahDeviceService.pulldownByLampPost(lampPostIdList, request);
    }


}