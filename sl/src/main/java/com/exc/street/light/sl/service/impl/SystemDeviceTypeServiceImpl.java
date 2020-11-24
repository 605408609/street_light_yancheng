/**
 * @filename:SystemDeviceTypeServiceImpl 2020-08-22
 * @project sl  V1.0
 * Copyright(c) 2018 Huang Jin Hao Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.exc.street.light.resource.entity.sl.SystemDeviceType;
import com.exc.street.light.sl.mapper.SystemDeviceTypeMapper;
import com.exc.street.light.sl.service.SystemDeviceTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description:TODO(设备类型表服务实现)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 *
 */
@Service
public class SystemDeviceTypeServiceImpl extends ServiceImpl<SystemDeviceTypeMapper, SystemDeviceType> implements SystemDeviceTypeService {

    private static final Logger logger = LoggerFactory.getLogger(SystemDeviceTypeServiceImpl.class);

    @Override
    public List<Integer> getStrategyIdListByDeviceTypeIdList(List<Integer> deviceTypeIdList, Integer size) {
        logger.info("getStrategyIdListByDeviceTypeIdList - 根据设备类型id集合查询支持该设备类型的策略集合 deviceTypeIdList=[{}]", deviceTypeIdList);
        List<Integer> strategyIdList = baseMapper.selectStrategyIdListByDeviceTypeIdList(deviceTypeIdList, size);
        return strategyIdList;
    }
}