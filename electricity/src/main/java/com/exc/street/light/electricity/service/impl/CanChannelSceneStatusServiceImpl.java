/**
 * @filename:CanChannelSceneStatusServiceImpl 2020-11-18
 * @project electricity  V1.0
 * Copyright(c) 2018 Xiaok Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.electricity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.mapper.CanChannelSceneStatusMapper;
import com.exc.street.light.electricity.service.CanChannelSceneStatusService;
import com.exc.street.light.electricity.service.CanChannelService;
import com.exc.street.light.electricity.util.AnalysisUtil;
import com.exc.street.light.electricity.util.ConstantUtil;
import com.exc.street.light.electricity.util.ProtocolUtil;
import com.exc.street.light.electricity.util.SocketClient;
import com.exc.street.light.resource.dto.electricity.CanChannelSceneStatusDTO;
import com.exc.street.light.resource.dto.electricity.ControlObject;
import com.exc.street.light.resource.dto.electricity.Scene;
import com.exc.street.light.resource.entity.electricity.CanChannel;
import com.exc.street.light.resource.entity.electricity.CanChannelSceneStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description:TODO(路灯网关回路场景添加状态表服务实现)
 * @version: V1.0
 * @author: Xiaok
 */
@Slf4j
@Service
public class CanChannelSceneStatusServiceImpl extends ServiceImpl<CanChannelSceneStatusMapper, CanChannelSceneStatus> implements CanChannelSceneStatusService {
    @Lazy
    @Autowired
    private CanChannelService canChannelService;

    @Override
    public boolean generateSceneStatusRecordsByNid(Integer nid) {
        log.info("生成回路场景状态记录，接收参数：路灯网关ID={}", nid);
        if (nid == null) {
            log.error("生成回路场景状态记录失败，路灯网关ID为空");
            return false;
        }
        //查询该网关下所有的回路id
        LambdaQueryWrapper<CanChannel> channelQueryWrapper = new LambdaQueryWrapper<>();
        //sid:4 代表交流接触器模块
        channelQueryWrapper.select(CanChannel::getId).eq(CanChannel::getNid, nid).eq(CanChannel::getSid, 4);
        List<CanChannel> channelList = canChannelService.list(channelQueryWrapper);
        if (channelList == null || channelList.isEmpty()) {
            log.error("生成回路场景状态记录失败，未找到网关下sid=4的回路记录，网关ID={}", nid);
            return false;
        }
        //需要新增的状态记录
        List<CanChannelSceneStatus> statusList = new ArrayList<>();
        for (CanChannel canChannel : channelList) {
            statusList.add(new CanChannelSceneStatus().setAddCloseStatus(0)
                    .setAddOpenStatus(0)
                    .setChannelId(canChannel.getId())
                    .setNid(nid));
        }
        this.saveBatch(statusList);
        log.info("生成回路场景状态记录成功，路灯网关ID={}，生成条数={}", nid, statusList.size());
        return true;
    }

    @Override
    public boolean deleteSceneStatusRecordsByNid(List<Integer> nidList) {
        log.info("删除回路场景状态记录，接收参数：路灯网关ID集合={}", nidList);
        if (nidList == null || nidList.isEmpty()) {
            log.error("删除回路场景状态记录失败，路灯网关ID为空");
            return false;
        }
        //过滤为null的数据
        nidList = nidList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (nidList.isEmpty()) {
            log.error("删除回路场景状态记录失败，路灯网关ID集合包含null值");
            return false;
        }
        //批量删除
        this.removeByIds(nidList);
        log.info("删除回路场景状态记录成功，路灯网关ID={}", nidList);
        return true;
    }

    @Override
    public void saveAndOrderLoopScene(String mac) {
        if (StringUtils.isBlank(mac)) {
            return;
        }
        List<CanChannelSceneStatusDTO> sceneStatusList = baseMapper.findSceneStatusList(mac);
        if (sceneStatusList == null || sceneStatusList.isEmpty()) {
            return;
        }
        List<Integer> closeSuccessIds = new ArrayList<>();
        List<Integer> openSuccessIds = new ArrayList<>();
        for (CanChannelSceneStatusDTO sceneStatusDTO : sceneStatusList) {
            if (StringUtils.isAnyBlank(sceneStatusDTO.getIp()) || sceneStatusDTO.getControlId() == null || sceneStatusDTO.getPort() == null) {
                continue;
            }
            int controlId = sceneStatusDTO.getControlId();
            List<ControlObject> controlObjects = new ArrayList<>();
            //when 下发 关场景未成功 then 重新下发一次
            if (sceneStatusDTO.getAddCloseStatus().equals(0)) {
                Scene scene = new Scene();
                //场景编号
                scene.setSn(AnalysisUtil.getSceneIdByLoopNum(controlId, false));
                ControlObject controlObject = new ControlObject();
                controlObject.setTagValue(0);
                controlObject.setControlId(controlId);
                controlObject.setTagId(160 + controlId);
                controlObject.setDeviceAddress(66);
                controlObjects.add(controlObject);
                scene.setControlObjects(controlObjects);
                boolean isSuccess = orderScene(scene, sceneStatusDTO.getIp(), sceneStatusDTO.getPort(), mac);
                if (isSuccess) {
                    closeSuccessIds.add(sceneStatusDTO.getId());
                }
            }
            //when 下发 开场景未成功 then 重新下发一次
            if (sceneStatusDTO.getAddOpenStatus().equals(0)) {
                controlObjects = new ArrayList<>();
                Scene scene = new Scene();
                //场景编号
                scene.setSn(AnalysisUtil.getSceneIdByLoopNum(controlId, true));
                ControlObject controlObject = new ControlObject();
                controlObject.setTagValue(1);
                controlObject.setControlId(controlId);
                controlObject.setTagId(160 + controlId);
                controlObject.setDeviceAddress(66);
                controlObjects.add(controlObject);
                scene.setControlObjects(controlObjects);
                boolean isSuccess = orderScene(scene, sceneStatusDTO.getIp(), sceneStatusDTO.getPort(), mac);
                if (isSuccess) {
                    openSuccessIds.add(sceneStatusDTO.getId());
                }
            }
        }
        //保存成功下发的记录
        if (!openSuccessIds.isEmpty()) {
            LambdaUpdateWrapper<CanChannelSceneStatus> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(CanChannelSceneStatus::getAddOpenStatus, 1).in(CanChannelSceneStatus::getId, openSuccessIds);
            this.update(wrapper);
        }
        if (!closeSuccessIds.isEmpty()) {
            LambdaUpdateWrapper<CanChannelSceneStatus> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(CanChannelSceneStatus::getAddCloseStatus, 1).in(CanChannelSceneStatus::getId, closeSuccessIds);
            this.update(wrapper);
        }
    }

    public boolean orderScene(Scene scene, String ip, Integer port, String mac) {
        //获取设置场景协议
        byte[] bytes = ProtocolUtil.setScene(mac, scene);
        //发送设置场景协议到网关
        byte rtn = AnalysisUtil.getRtn(SocketClient.send(ip, port, bytes));
        return rtn == ConstantUtil.RET_1;
    }
}