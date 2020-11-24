/**
 * @filename:ControlLoopSceneStatusServiceImpl 2020-11-05
 * @project dlm  V1.0
 * Copyright(c) 2018 Xiaok Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.ControlLoopSceneStatusMapper;
import com.exc.street.light.dlm.service.ControlLoopSceneStatusService;
import com.exc.street.light.dlm.utils.AnalysisUtil;
import com.exc.street.light.dlm.utils.ConstantUtil;
import com.exc.street.light.dlm.utils.ProtocolUtil;
import com.exc.street.light.dlm.utils.SocketClient;
import com.exc.street.light.resource.dto.dlm.ControlLoopSceneStatusDTO;
import com.exc.street.light.resource.dto.electricity.ControlObject;
import com.exc.street.light.resource.dto.electricity.Scene;
import com.exc.street.light.resource.entity.dlm.ControlLoopSceneStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 集控回路场景状态表服务实现
 * @version: V1.0
 * @author: Xiaok
 */
@Service
public class ControlLoopSceneStatusServiceImpl extends ServiceImpl<ControlLoopSceneStatusMapper, ControlLoopSceneStatus> implements ControlLoopSceneStatusService {

    @Override
    public void saveLoopScene(String mac) {
        if (StringUtils.isBlank(mac)) {
            return;
        }
        List<ControlLoopSceneStatusDTO> sceneStatusList = baseMapper.findSceneStatusList(mac);
        if (sceneStatusList == null || sceneStatusList.isEmpty()) {
            return;
        }
        List<Integer> closeSuccessIds = new ArrayList<>();
        List<Integer> openSuccessIds = new ArrayList<>();
        for (ControlLoopSceneStatusDTO sceneStatusDTO : sceneStatusList) {
            if (StringUtils.isAnyBlank(sceneStatusDTO.getNum(), sceneStatusDTO.getIp()) || sceneStatusDTO.getPort() == null) {
                continue;
            }
            int num = Integer.parseInt(sceneStatusDTO.getNum());
            List<ControlObject> controlObjects = new ArrayList<>();
            //when 下发 关场景未成功 then 重新下发一次
            if (sceneStatusDTO.getAddCloseStatus().equals(0)) {
                Scene scene = new Scene();
                //场景编号
                scene.setSn(AnalysisUtil.getSceneIdByLoopNum(num, false));
                ControlObject controlObject = new ControlObject();
                controlObject.setTagValue(0);
                controlObject.setControlId(num);
                controlObject.setTagId(160 + num);
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
                scene.setSn(AnalysisUtil.getSceneIdByLoopNum(num, true));
                ControlObject controlObject = new ControlObject();
                controlObject.setTagValue(1);
                controlObject.setControlId(num);
                controlObject.setTagId(160 + num);
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
            LambdaUpdateWrapper<ControlLoopSceneStatus> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(ControlLoopSceneStatus::getAddOpenStatus, 1).in(ControlLoopSceneStatus::getId, openSuccessIds);
            this.update(wrapper);
        }
        if (!closeSuccessIds.isEmpty()) {
            LambdaUpdateWrapper<ControlLoopSceneStatus> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(ControlLoopSceneStatus::getAddCloseStatus, 1).in(ControlLoopSceneStatus::getId, closeSuccessIds);
            this.update(wrapper);
        }
    }

    @Override
    public boolean orderScene(Scene scene, String ip, Integer port, String mac) {
        //获取设置场景协议
        byte[] bytes = ProtocolUtil.setScene(mac, scene);
        //发送设置场景协议到网关
        byte rtn = AnalysisUtil.getRtn(SocketClient.send(ip, port, bytes));
        return rtn == ConstantUtil.RET_1;
    }

    @Override
    public boolean saveSceneStatusRecord(Integer controlId, List<Integer> loopIdList) {
        if (controlId == null || loopIdList == null || loopIdList.isEmpty()) {
            return false;
        }
        List<ControlLoopSceneStatus> statusList = new ArrayList<>();
        for (Integer loopId : loopIdList) {
            statusList.add(new ControlLoopSceneStatus().setAddCloseStatus(0).setAddOpenStatus(0)
                    .setLocationControlId(controlId).setLoopId(loopId));
        }
        return this.saveBatch(statusList);
    }

    @Override
    public boolean deleteSceneStatusByControlId(Integer controlId) {
        if (controlId == null) {
            return false;
        }
        LambdaQueryWrapper<ControlLoopSceneStatus> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ControlLoopSceneStatus::getLocationControlId, controlId);
        return this.remove(wrapper);
    }
}