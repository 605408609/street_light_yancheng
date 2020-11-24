/**
 * @filename:ControlLoopDeviceService 2020-08-24
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.ControlLoopDevice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface ControlLoopDeviceService extends IService<ControlLoopDevice> {

    /**
     * 根据分组id集合查询关联表
     * @param loopIdList
     * @param request
     * @return
     */
    Result getControlLoopDeviceByLoopIdList(List<Integer> loopIdList, HttpServletRequest request);

}