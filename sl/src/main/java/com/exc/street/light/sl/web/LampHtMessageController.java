/**
 * @filename:LampHtMessageController 2020-09-02
 * @project sl  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.sl.ht.HtCommandDTO;
import com.exc.street.light.resource.enums.sl.ht.HtLoopOutputTypeEnum;
import com.exc.street.light.resource.vo.req.htLamp.*;
import com.exc.street.light.sl.sender.HtMessageSender;
import com.exc.street.light.sl.service.LampHtMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： 华体集中控制器消息记录Controller</P>
 *
 * @version: V1.0
 * @author: Xiaok
 * @time 2020-09-02
 */
@Api(tags = "华体集中控制器消息记录", value = "华体集中控制器消息记录")
@RestController
@Slf4j
@RequestMapping("/api/sl/lamp/ht/message")
public class LampHtMessageController {

    @Autowired
    private HtMessageSender htMessageSender;
    @Autowired
    private LampHtMessageService lampHtMessageService;

    @ApiOperation("华体集中控制器回路手动输出")
    @PostMapping("/setLampPostOutput")
    public Result setLampPostOutput(@RequestBody HtSetLampPostOutputRequestVO requestVO) {
        if (requestVO == null) {
            return new Result<>().error("参数缺失");
        }
        List<HtLoopOutputTypeEnum> actTypeList = new ArrayList<>();
        if (requestVO.getActList() != null && !requestVO.getActList().isEmpty()) {
            for (Integer act : requestVO.getActList()) {
                actTypeList.add(HtLoopOutputTypeEnum.getByCode(act));
            }
        }
        HtCommandDTO htCommandDTO = htMessageSender.setLocationControlLoopOutPut(actTypeList);
        return lampHtMessageService.orderCommand(requestVO.getLocationControlId(), requestVO.getLocationControlAddr(), htCommandDTO);
    }

    @ApiOperation("华体集中控制器回路 设置定时计划")
    @PostMapping("/setLampPostOutputTimer")
    public Result setLampPostOutputTimer(@RequestBody HtSetLocationControlLoopPlanRequestVO requestVO) {
        if (requestVO == null) {
            return new Result<>().error("参数缺失");
        }
        List<HtLampPostPlanRequestVO> planList = new ArrayList<>();
        if (requestVO.getPlanList() != null && !requestVO.getPlanList().isEmpty()) {
            for (HtSetControlPlanRequestVO plan : requestVO.getPlanList()) {
                HtLampPostPlanRequestVO planRequestVO = new HtLampPostPlanRequestVO();
                BeanUtils.copyProperties(plan, planRequestVO);
                planRequestVO.setTypeEnum(HtLoopOutputTypeEnum.getByCode(plan.getActType()));
                planList.add(planRequestVO);
            }
        }
        HtCommandDTO htCommandDTO = htMessageSender.setLocationControlLoopPlan(planList);
        return lampHtMessageService.orderCommand(requestVO.getLocationControlId(), requestVO.getLocationControlAddr(), htCommandDTO);
    }

    /**
     * 控制单灯支路输出
     *
     * @param requestVOS 参数
     * @return
     */
    @ApiOperation("华体集中控制器回路 控制单灯支路输出")
    @PostMapping("/setSingleLampOut")
    public Result setSingleLampOut(@RequestBody List<HtSetSingleLampOutputRequestVO> requestVOS) {
        if (requestVOS == null || requestVOS.isEmpty()) {
            return new Result<>().error("参数缺失");
        }
        for (HtSetSingleLampOutputRequestVO requestVO : requestVOS) {
            try {
                HtCommandDTO htCommandDTO = htMessageSender.setSingleLampOutput(requestVO.getNodeList(), requestVO.getActList());
                lampHtMessageService.orderCommand(requestVO.getLocationControlId(), requestVO.getLocationControlAddr(), htCommandDTO);
            } catch (Exception e) {
                log.error("华体-控制单灯支路输出失败,e={}", e.getMessage());
                return new Result<>().error("下发失败");
            }
        }
        return new Result<>().success("下发成功");
    }


    /**
     * 添加单灯支路输出计划
     *
     * @param requestVOS 参数
     * @return
     */
    @ApiOperation("华体集中控制器回路 添加单灯支路输出策略")
    @PostMapping("/setSingleLampOutPlan")
    public Result setSingleLampOutPlan(@RequestBody List<HtSetSingleLampOutputPlanRequestVO> requestVOS) {
        if (requestVOS == null || requestVOS.isEmpty()) {
            return new Result<>().error("参数缺失");
        }
        for (HtSetSingleLampOutputPlanRequestVO requestVO : requestVOS) {
            try {
                HtCommandDTO htCommandDTO = htMessageSender.setSingleLampLoopPlanByDateRange(requestVO.getRoList());
                lampHtMessageService.orderCommand(requestVO.getLocationControlId(), requestVO.getLocationControlAddr(), htCommandDTO);
            } catch (Exception e) {
                log.error("华体-下发单灯支路输出计划失败,e={}", e.getMessage());
                return new Result<>().error("下发失败");
            }
        }
        return new Result<>().success("下发成功");
    }

}