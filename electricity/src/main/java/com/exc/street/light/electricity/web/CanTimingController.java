package com.exc.street.light.electricity.web;

import com.exc.street.light.electricity.service.CanTimingService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.vo.electricity.CanTimingVO;
import com.exc.street.light.resource.vo.electricity.TimerPatchVO;
import com.exc.street.light.resource.vo.electricity.TimerVO;
import com.exc.street.light.resource.vo.req.electricity.ReqElectricityScriptVO;
import com.exc.street.light.resource.vo.req.electricity.ReqTimingExecuteVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 强电定时控制器
 *
 * @author Linshiwen
 * @date 2018/05/31
 */
@Api(tags = "强电定时控制器")
@ApiIgnore
@RestController
@RequestMapping("/api/can/timing")
public class CanTimingController {
    private static final Logger logger = LoggerFactory.getLogger(CanTimingController.class);
    @Autowired
    private CanTimingService canTimingService;

    /**
     * 新增定时器(集控选择场景)
     *
     * @param timerVO
     * @param request
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增定时器(集控选择场景)",notes = "新增定时器(集控选择场景)")
    @RequiresPermissions(value = "electricity:manage:timer:add")
    public Result add(@RequestBody TimerVO timerVO, HttpServletRequest request) {
        logger.info("接收参数:" + timerVO);
        Result result = canTimingService.add(timerVO, request);
        return result;
    }

    /**
     * 新增站点日程定时器
     *
     * @param vo
     * @param request
     * @return
     */
    @PostMapping("/site")
    @RequiresPermissions(value = "electricity:manage:timer:add")
    public Result addSite(@RequestBody ReqElectricityScriptVO vo, HttpServletRequest request) {
        logger.info("接收参数:{}", vo);
        Result result = canTimingService.addSite(vo, request);
        return result;
    }

    /**
     * 新增定时器(场景选择集控)
     *
     * @param timerPatchVO
     * @param request
     * @return
     */
    @PostMapping("/patch")
    @ApiOperation(value = "新增定时器(场景选择集控)",notes = "新增定时器(场景选择集控)")
    @RequiresPermissions(value = "electricity:manage:timer:add")
    public Result patchAdd(@RequestBody TimerPatchVO timerPatchVO, HttpServletRequest request) {
        logger.info("新增定时器(场景选择集控)接收参数:TimerPatchVO={}" , timerPatchVO);
        return canTimingService.patchAdd(timerPatchVO, request);
    }

    /**
     * 成对删除
     *
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions(value = "electricity:manage:timer:delete")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        return canTimingService.delete2ById(id, request);
    }

    /**
     * 清空集控定时
     *
     * @param nid
     * @param request
     * @return
     */
    @DeleteMapping("/node")
    @RequiresPermissions(value = "electricity:manage:timer:delete")
    public Result clear(Integer nid, HttpServletRequest request) {
        return canTimingService.clear(nid, request);
    }

    /**
     * 清空站点定时
     *
     * @param siteId
     * @param request
     * @return
     */
    @DeleteMapping("/site")
    @RequiresPermissions(value = "electricity:manage:timer:delete")
    public Result clearSite(Integer siteId, HttpServletRequest request) {
        return canTimingService.clearSite(siteId, request);
    }

    /**
     * 清空分区定时
     *
     * @param partitionId
     * @param request
     * @return
     */
    @DeleteMapping("/partition")
    @RequiresPermissions(value = "electricity:manage:timer:delete")
    public Result clearPartition(Integer partitionId, HttpServletRequest request) {
        return canTimingService.clearPartition(partitionId, request);
    }

    /**
     * 清空所有定时
     *
     * @param request
     * @return
     */
    @DeleteMapping("/all")
    @RequiresPermissions(value = "electricity:manage:timer:delete")
    public Result clearAll(HttpServletRequest request) {
        return canTimingService.clearAll(request);
    }

    /**
     * 根据集控id查询定时器数量
     *
     * @param nid 集控id
     * @return
     */
    @GetMapping
    @ApiOperation(value = "根据集控id查询定时器数量",notes = "根据集控id查询定时器数量")
    @RequiresPermissions(value = "electricity:manage:scene:current")
    public Result list(Integer nid) {
        List<CanTimingVO> views = canTimingService.findByNid(nid);
        return new Result().success(views);
    }

    /**
     * 修改定时是否执行
     *
     * @param reqTimingExecuteVO
     * @return
     */
    @PostMapping("/changeExecuteState")
    @RequiresPermissions(value = "electricity:manage:timer:execute")
    public Result changeExecuteState(@RequestBody ReqTimingExecuteVO reqTimingExecuteVO) {
        if (reqTimingExecuteVO != null) {
            int nid = reqTimingExecuteVO.getNid();
            int id = reqTimingExecuteVO.getId();
            int isExecute = reqTimingExecuteVO.getIsExecute();
            return canTimingService.changeExecuteState(nid, id, isExecute);
        }
        return new Result().error("传入参数为null");
    }
}
