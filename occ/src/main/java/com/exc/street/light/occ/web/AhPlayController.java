/**
 * @filename:AhPlayController 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.occ.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.occ.mapper.AhPlayDao;
import com.exc.street.light.occ.service.AhPlayService;
import com.exc.street.light.resource.core.PageParam;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.occ.AhPlay;
import com.exc.street.light.resource.vo.OccAhPlayVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Huang Min
 * @time 2020-03-16
 *
 */
@Api(description = "", value = "")
@RestController
@CrossOrigin
@RequestMapping("/ah/play")
public class AhPlayController {
    @Autowired
    private AhPlayDao ahPlayDao;
    @Autowired
    protected AhPlayService ahPlayService;

    private static Result<OccAhPlayVO> resultVO = new Result<OccAhPlayVO>();

    protected Result result = new Result();

    /**
     * @explain 查询对象  <swagger GET请求>
     * @author Huang Min
     * @time 2020-03-17
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取对象", notes = "作者：Huang Min")
    @ApiImplicitParam(paramType = "path", name = "id", value = "对象id", required = true, dataType = "Long")
    @RequiresPermissions(value = "occ:module:alarm:info")
    public Result<AhPlay> getById(@PathVariable("id") Long id) {
        AhPlay obj = ahPlayService.getById(id);
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
    @RequiresPermissions(value = "occ:module:alarm:info")
    @SystemLog(logModul = "紧急呼叫",logType = "修改设备",logDesc = "修改设备")
    public Result<AhPlay> update(@RequestBody AhPlay ahPlay) {
        if (null != ahPlay) {
            boolean rsg = ahPlayService.updateById(ahPlay);
            if (rsg) {
                result.success("修改成功");
                return result;
            } else {
                result.error("修改失败！");
                return result;
            }
        } else {
            result.error("请传入正确参数！");
            return result;
        }
    }

    /**
     * @explain app接口，查询对象  <swagger GET请求>
     * @author Huang Min
     * @time 2020-03-17
     */
    @GetMapping("/app/{id}")
    @ApiOperation(value = "获取对象", notes = "作者：Huang Min")
    @ApiImplicitParam(paramType = "path", name = "id", value = "对象id", required = true, dataType = "Long")
    @RequiresPermissions(value = "occ:module:alarm:info")
    public Result<OccAhPlayVO> getById1(@PathVariable("id") Long id) {
        OccAhPlayVO obj = ahPlayService.getByAhPlayId(id);
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
    public Result insert(@RequestBody AhPlay entity) {
        Result result = new Result();
        if (null != entity) {
            entity.setCreateTime(new Date());
            boolean rsg = ahPlayService.save(entity);
            if (rsg) {
                result.success("添加成功");
                return result;
            } else {
                result.error("添加失败！");
                return result;
            }
        } else {
            result.error("请传入正确参数！");
            return result;
        }
    }

    /**
     * @explain 分页条件查询
     * @author Huang Min
     * @time 2020-03-12
     */
    @GetMapping("page")
    @ApiOperation(value = "分页查询", notes = "分页查询,作者：Huang Min")
    @RequiresPermissions(value = "occ:module:alarm:info")
    public Result<IPage<OccAhPlayVO>> getPages(PageParam<OccAhPlayVO> param, OccAhPlayVO t, HttpServletRequest request) {
        Result<IPage<OccAhPlayVO>> returnPage = new Result<IPage<OccAhPlayVO>>();
        Page<OccAhPlayVO> page = new Page<OccAhPlayVO>(param.getPageNum(), param.getPageSize());
        QueryWrapper<OccAhPlayVO> queryWrapper = new QueryWrapper<OccAhPlayVO>();
        queryWrapper.setEntity(t);
        //分页数据
        IPage<OccAhPlayVO> pageData = ahPlayService.page(page, queryWrapper, t,request);
        returnPage.success(pageData);

        return returnPage;
    }

    /**
     * @explain 删除对象
     * @author Huang Min
     * @time 2020-03-17
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "作者：Huang Min")
    @ApiImplicitParam(paramType = "query", name = "id", value = "对象id", required = true, dataType = "Long")
    @RequiresPermissions(value = "occ:module:alarm:info:delete")
    @SystemLog(logModul = "紧急呼叫",logType = "删除对象",logDesc = "删除设备对象")
    public Result deleteById(@PathVariable("id") Long id) {
        Result result = new Result();
        AhPlay obj = ahPlayService.getById(id);
        if (obj != null) {
            boolean rsg = ahPlayService.removeById(id);
            if (rsg) {
                result.success("删除成功");
                return result;
            } else {
                result.error("删除失败！");
                return result;
            }
        } else {
            result.error("删除的对象不存在！");
            return result;
        }
    }

    /**
     * @explain 根据紧急报警设备编号查询所属灯杆信息  <swagger GET请求>
     *     本程序内部调用的接口，关闭权限验证
     * @author Huang Min
     * @time 2020-03-17
     */
    @PostMapping("/getLampId")
    public OccAhPlayVO selectByDeviceNum(@RequestBody AhPlay ahPlay) {
        OccAhPlayVO obj = ahPlayDao.selectByDeviceNum(ahPlay.getDeviceNum());
        return obj;
    }

    /**
     * @explain 修改我的消息列表对象为已读状态
     * @author HuangMin
     * @time 2020-03-25
     */
    @PostMapping("/news/status")
    @RequiresPermissions(value = "occ:module:alarm:info")
    @ApiOperation(value = "批量修改我的消息列表对象为已读状态", notes = "作者：HuangMin")
    public Result newsStatus(@RequestBody OccAhPlayVO occAhPlayVO, HttpServletRequest httpServletRequest) {
        return ahPlayService.newsStatus(occAhPlayVO, httpServletRequest);
    }

    /**
     * @explain 修改我的消息列表全部已读
     * @author HuangMin
     * @time 2020-03-25
     */
    @PostMapping("/news/all")
    @RequiresPermissions(value = "occ:module:alarm:info")
    @ApiOperation(value = "修改我的消息列表全部已读", notes = "作者：HuangMin")
    public Result newsAll(HttpServletRequest httpServletRequest) {
        return ahPlayService.newsAll(httpServletRequest);
    }
}