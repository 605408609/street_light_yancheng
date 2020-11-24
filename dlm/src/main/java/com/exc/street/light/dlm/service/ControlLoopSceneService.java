/**
 * @filename:ControlLoopSceneService 2020-08-24
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.ControlLoopScene;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.vo.req.DlmReqControlLoopSceneVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 场景(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface ControlLoopSceneService extends IService<ControlLoopScene> {

    /**
     * 回路场景下发
     * @param sceneVO
     * @param request
     * @return
     */
    Result issueLoopScene(DlmReqControlLoopSceneVO sceneVO, HttpServletRequest request);
}