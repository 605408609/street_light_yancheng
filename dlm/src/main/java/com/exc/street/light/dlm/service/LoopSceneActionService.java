/**
 * @filename:LoopSceneActionService 2020-11-07
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.exc.street.light.resource.entity.dlm.LoopSceneAction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.vo.req.DlmReqSceneActionVO;

import java.util.List;
/**   
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface LoopSceneActionService extends IService<LoopSceneAction> {

    /**
     * 新增场景动作
     * @param dlmReqSceneActionVOList
     */
    void insertSceneStrategyAction(List<DlmReqSceneActionVO> dlmReqSceneActionVOList);

}