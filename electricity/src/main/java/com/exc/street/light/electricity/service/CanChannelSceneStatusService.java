/**
 * @filename:CanChannelSceneStatusService 2020-11-18
 * @project electricity  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.electricity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.electricity.CanChannelSceneStatus;

import java.util.List;

/**
 * @Description: (路灯网关回路场景添加状态表服务层)
 * @version: V1.0
 * @author: Xiaok
 *
 */
public interface CanChannelSceneStatusService extends IService<CanChannelSceneStatus> {

    /**
     * 根据路灯网关ID生成回路场景状态记录
     * @param nid 路灯网关ID
     * @return 是否成功
     */
    boolean generateSceneStatusRecordsByNid(Integer nid);

    /**
     * 根据路灯网关ID删除回路场景状态记录
     * @param nidList 路灯网关ID
     * @return 是否成功
     */
    boolean deleteSceneStatusRecordsByNid(List<Integer> nidList);

    /**
     * 保存或更新  并下发回路场景
     *
     * @param mac
     */
    void saveAndOrderLoopScene(String mac);
}