/**
 * @filename:ControlLoopSceneStatusService 2020-11-05
 * @project dlm  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.dto.electricity.Scene;
import com.exc.street.light.resource.entity.dlm.ControlLoopSceneStatus;

import java.util.List;

/**
 * @Description:TODO(集控回路场景状态表服务层)
 * @version: V1.0
 * @author: Xiaok
 */
public interface ControlLoopSceneStatusService extends IService<ControlLoopSceneStatus> {

    /**
     * 保存或更新  并下发回路场景
     *
     * @param mac
     */
    void saveLoopScene(String mac);

    /**
     * 下发场景
     * @param scene
     * @param ip
     * @param port
     * @param mac
     * @return
     */
    boolean orderScene(Scene scene, String ip, Integer port, String mac);

    /**
     * 保存需要下发的场景状态记录
     * @param controlId 集控器ID
     * @param loopIdList 回路ID
     * @return
     */
    boolean saveSceneStatusRecord(Integer controlId, List<Integer> loopIdList);

    /**
     * 根据集控id删除数据
     * @param controlId
     * @return
     */
    boolean deleteSceneStatusByControlId(Integer controlId);
}