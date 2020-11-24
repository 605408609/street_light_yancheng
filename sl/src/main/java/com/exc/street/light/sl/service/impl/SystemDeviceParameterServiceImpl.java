/**
 * @filename:SystemDeviceParameterServiceImpl 2020-09-03
 * @project sl  V1.0
 * Copyright(c) 2018 Huang Jin Hao Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.resource.dto.sl.SystemDeviceParameterFiledDTO;
import com.exc.street.light.resource.entity.sl.SystemDeviceParameter;
import com.exc.street.light.sl.mapper.SystemDeviceParameterMapper;
import com.exc.street.light.sl.service.SystemDeviceParameterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: (设备参数表服务实现)
 * @version: V1.0
 * @author: Huang Jin Hao
 */
@Service
public class SystemDeviceParameterServiceImpl extends ServiceImpl<SystemDeviceParameterMapper, SystemDeviceParameter> implements SystemDeviceParameterService {

    @Override
    public Map<String, Integer> selectFieldByType(Integer deviceType) {
        if (deviceType == null) {
            return null;
        }
        List<SystemDeviceParameterFiledDTO> fieldList = baseMapper.selectFiledByType(deviceType);
        Map<String, Integer> fieldMap = new HashMap<>();
        if (fieldList != null && !fieldList.isEmpty()) {
            for (SystemDeviceParameterFiledDTO field : fieldList) {
                if (StringUtils.isBlank(field.getFiled()) || field.getId() == null) {
                    continue;
                }
                fieldMap.put(field.getFiled(), field.getId());
            }
        }
        return fieldMap;
    }

    @Override
    public SystemDeviceParameter selectByName(Integer deviceTypeId, String name) {
        QueryWrapper<SystemDeviceParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_type_id",deviceTypeId);
        queryWrapper.eq("name",name);
        SystemDeviceParameter systemDeviceParameter = baseMapper.selectOne(queryWrapper);
        return systemDeviceParameter;
    }
}