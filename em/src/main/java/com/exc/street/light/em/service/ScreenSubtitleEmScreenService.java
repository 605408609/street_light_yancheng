/**
 * @filename:ScreenSubtitleEmScreenService 2020-11-16
 * @project em  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.em.service;

import com.exc.street.light.resource.entity.em.ScreenSubtitleEmScreen;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description:TODO(传感器关联显示屏中间表服务层)
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
public interface ScreenSubtitleEmScreenService extends IService<ScreenSubtitleEmScreen> {

    /**
     * 根据显示屏的id集合获取显示屏的编号集合
     * @param screenDeviceIdList
     * @return
     */
    List<String> selectScreenDeviceNumList(List<Integer> screenDeviceIdList);
}