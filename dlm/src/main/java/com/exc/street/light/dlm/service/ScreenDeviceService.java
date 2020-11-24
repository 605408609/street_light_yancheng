/**
 * @filename:ScreenDeviceService 2020-03-17
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.vo.resp.DlmRespDevicePublicParVO;

import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
public interface ScreenDeviceService extends IService<ScreenDevice> {
    /**
     * 批量插入
     * @param screenDeviceList
     * @return
     */
    Result addList(List<ScreenDevice> screenDeviceList);

    /**
     * 查询所有
     */
    Result selectAll();

    /**
     * 根据灯杆id获取灯具返回对象集合
     * @param slLampPostIdList
     * @return
     */
    List<DlmRespDevicePublicParVO> getDlmRespDeviceVOList(List<Integer> slLampPostIdList);
}