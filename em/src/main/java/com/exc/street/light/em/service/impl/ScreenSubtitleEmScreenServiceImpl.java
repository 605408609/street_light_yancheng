/**
 * @filename:ScreenSubtitleEmScreenServiceImpl 2020-11-16
 * @project em  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.em.service.impl;

import com.exc.street.light.resource.entity.em.ScreenSubtitleEmScreen;
import com.exc.street.light.em.mapper.ScreenSubtitleEmScreenMapper;
import com.exc.street.light.em.service.ScreenSubtitleEmScreenService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**   
 * @Description:TODO(传感器关联显示屏中间表服务实现)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Service
public class ScreenSubtitleEmScreenServiceImpl  extends ServiceImpl<ScreenSubtitleEmScreenMapper, ScreenSubtitleEmScreen> implements ScreenSubtitleEmScreenService  {

    @Override
    public List<String> selectScreenDeviceNumList(List<Integer> screenDeviceIdList) {
        return this.baseMapper.selectScreenDeviceNumList(screenDeviceIdList);
    }
}