/**
 * @filename:LocationControlServiceImpl 2020-09-15
 * @project sl  V1.0
 * Copyright(c) 2018 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.exc.street.light.resource.entity.dlm.ControlLoopDevice;
import com.exc.street.light.resource.entity.dlm.LocationControl;
import com.exc.street.light.resource.entity.sl.SystemDevice;
import com.exc.street.light.sl.mapper.LocationControlMapper;
import com.exc.street.light.sl.mapper.SystemDeviceMapper;
import com.exc.street.light.sl.service.ControlLoopDeviceService;
import com.exc.street.light.sl.service.LocationControlService;
import com.exc.street.light.sl.service.SystemDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: LeiJing
 */
@Service
public class LocationControlServiceImpl extends ServiceImpl<LocationControlMapper, LocationControl> implements LocationControlService {
    private static final Logger logger = LoggerFactory.getLogger(LampStrategyTypeServiceImpl.class);

    @Autowired
    private ControlLoopDeviceService controlLoopDeviceService;
    @Autowired
    private SystemDeviceService systemDeviceService;
    @Autowired
    private SystemDeviceMapper systemDeviceMapper;
    @Autowired
    private LocationControlService locationControlService;

    @Override
    public void findAllAndUpdateStatus(String concentratorId, Integer status) {
        try {
            //获取中科智联集中控制器对象
            QueryWrapper<LocationControl> locationControlQueryWrapper = new QueryWrapper<>();
            locationControlQueryWrapper.eq("num", concentratorId);
            LocationControl locationControl = locationControlService.getOne(locationControlQueryWrapper);
            //修改中科智联集中控制器在线离线状态
            locationControl.setIsOnline(status);
            locationControlService.updateById(locationControl);
            if (status == 1) {
                logger.info("中科智联集中控制器 {} 修改为在线状态", locationControl.getName());
            } else {
                logger.info("中科智联集中控制器 {} 修改为离线状态", locationControl.getName());
            }
            //获取集中控制器下单灯控制器集合
            QueryWrapper<ControlLoopDevice> controlLoopQueryWrapper = new QueryWrapper<>();
            controlLoopQueryWrapper.eq("control_id", locationControl.getId());
            List<ControlLoopDevice> controlLoopDeviceList = controlLoopDeviceService.list(controlLoopQueryWrapper);
             List<Integer> zkzlDeviceIdList = controlLoopDeviceList.stream().map(ControlLoopDevice::getDeviceId).distinct().collect(Collectors.toList());
            if (zkzlDeviceIdList.size() > 0) {
                List<SystemDevice> systemDeviceList = systemDeviceMapper.selectBatchIds(zkzlDeviceIdList);
                //修改指定集中控制器下所有单灯控制器在线/离线状态
                for (SystemDevice systemDevice : systemDeviceList) {
                    //灯具序号不为空则已绑定集中控制器，状态修改
                    if (systemDevice.getReserveOne() != null) {
                        systemDevice.setIsOnline(status);
                        systemDevice.setLastOnlineTime(new Date());
                        systemDeviceMapper.updateById(systemDevice);
                    }
                }
            }
            if (status == 1) {
                logger.info("修改中科智联集中控制器 {}，编号 {} 下所有单灯控制器为在线状态", locationControl.getName(), concentratorId);
            } else {
                logger.info("修改中科智联集中控制器 {}，编号 {} 下所有单灯控制器为离线状态", locationControl.getName(), concentratorId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStatusByNum(String num, Integer status) {
        QueryWrapper<SystemDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("num", num);
        SystemDevice systemDevice = systemDeviceMapper.selectOne(queryWrapper);
        systemDevice.setIsOnline(status);
        systemDeviceMapper.updateById(systemDevice);
    }
}