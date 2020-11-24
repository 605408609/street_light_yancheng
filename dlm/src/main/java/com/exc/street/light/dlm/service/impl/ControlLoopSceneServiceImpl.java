/**
 * @filename:ControlLoopSceneServiceImpl 2020-08-24
 * @project dlm  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.config.parameter.HttpSlApi;
import com.exc.street.light.dlm.mapper.ControlLoopSceneMapper;
import com.exc.street.light.dlm.service.ControlLoopSceneService;
import com.exc.street.light.dlm.service.ControlLoopService;
import com.exc.street.light.dlm.service.LocationControlService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.ControlLoop;
import com.exc.street.light.resource.entity.dlm.ControlLoopScene;
import com.exc.street.light.resource.entity.dlm.LocationControl;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.vo.req.DlmReqControlLoopSceneVO;
import com.exc.street.light.resource.vo.req.htLamp.HtSetControlPlanRequestVO;
import com.exc.street.light.resource.vo.req.htLamp.HtSetLocationControlLoopPlanRequestVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 回路场景下发(服务实现)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
public class ControlLoopSceneServiceImpl extends ServiceImpl<ControlLoopSceneMapper, ControlLoopScene> implements ControlLoopSceneService {

    private static final Logger logger = LoggerFactory.getLogger(ControlLoopSceneServiceImpl.class);

    @Autowired
    private ControlLoopService controlLoopService;

    @Autowired
    private LocationControlService locationControlService;

    @Autowired
    private HttpSlApi httpSlApi;

    @Override
    public Result issueLoopScene(DlmReqControlLoopSceneVO sceneVO, HttpServletRequest request) {
        logger.info("issueLoopScene - 回路场景下发 sceneVO=[{}]", sceneVO);
        List<ControlLoop> controlLoopList = null;
        ControlLoopScene loopScene = new ControlLoopScene();
        if (sceneVO.getLoopIdList() != null && sceneVO.getLoopIdList().size() > 0) {
            BeanUtils.copyProperties(sceneVO, loopScene);
            int result = baseMapper.insert(loopScene);
            if (result < 1) {
                logger.info("issueLoopScene - 回路场景下发失败 sceneVO=[{}]", sceneVO);
                return new Result().error("回路场景下发失败");
            }
            // 更新集控回路表中的场景id
            LambdaUpdateWrapper<ControlLoop> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(ControlLoop::getSceneId, loopScene.getId())
                    .in(ControlLoop::getId, sceneVO.getLoopIdList());
            controlLoopService.update(updateWrapper);
            // 根据回路id集合查询所有的集控器信息
            LambdaQueryWrapper<ControlLoop> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(ControlLoop::getId, sceneVO.getLoopIdList());
            controlLoopList = controlLoopService.list(wrapper);
        } else {
            return new Result().error("请勾选回路后再下发场景");
        }
        // 场景下发控制
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("token", request.getHeader("token"));
        headerMap.put("Content-Type", "application/json");
        if (controlLoopList != null && controlLoopList.size() > 0) {
            //过滤出所有回路所关联的集控器id
            List<Integer> controlIdList = controlLoopList.stream().map(ControlLoop::getControlId).distinct().collect(Collectors.toList());
            for (Integer controlId : controlIdList) {
                // 根据id获取对象信息
                LocationControl locationControl = locationControlService.getById(controlId);
                // 过滤出该集控器下的回路信息
                List<ControlLoop> loopList = controlLoopList.stream().filter(controlLoop -> controlLoop.getControlId().equals(controlId)).collect(Collectors.toList());
                // 构建下发对象
                HtSetLocationControlLoopPlanRequestVO planRequestVO = new HtSetLocationControlLoopPlanRequestVO();
                planRequestVO.setLocationControlId(controlId);
                planRequestVO.setLocationControlAddr(locationControl.getNum());
                List<HtSetControlPlanRequestVO> requestVOList = new ArrayList<>();
                HtSetControlPlanRequestVO requestVO = new HtSetControlPlanRequestVO();
                requestVO.setTime(sceneVO.getExecutionTime());
                requestVO.setActType(sceneVO.getIsOpen());
                List<Integer> loopNumList = loopList.stream().map(e -> Integer.parseInt(e.getNum())).collect(Collectors.toList());
                requestVO.setLoop1(loopNumList.contains(1));
                requestVO.setLoop2(loopNumList.contains(2));
                requestVO.setLoop3(loopNumList.contains(3));
                requestVO.setLoop4(loopNumList.contains(4));
                requestVOList.add(requestVO);
                planRequestVO.setPlanList(requestVOList);
                String jsonString = JSONObject.toJSONString(planRequestVO);
                try {
                    JSONObject loopControlResult = JSON.parseObject(HttpUtil.post(httpSlApi.getUrl() + httpSlApi.getLoopScene(), jsonString, headerMap));
                    JSONObject loopControlObj = loopControlResult.getJSONObject("data");
                    System.out.println(loopControlObj);
                } catch (Exception e) {
                    logger.error("场景下发失败", e.getMessage());
                    return new Result().error("场景下发失败");
                }
            }
        }
        return new Result().success("下发成功");
    }
}