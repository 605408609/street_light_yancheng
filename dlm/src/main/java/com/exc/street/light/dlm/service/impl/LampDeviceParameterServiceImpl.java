package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.LampDeviceParameterMapper;
import com.exc.street.light.dlm.service.LampDeviceParameterService;
import com.exc.street.light.dlm.service.SystemDeviceParameterService;
import com.exc.street.light.resource.entity.sl.LampDeviceParameter;
import com.exc.street.light.resource.entity.sl.SystemDeviceParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: (设备参数数据表服务实现)
 * @version: V1.0
 * @author: Huang Jin Hao
 */
@Service
public class LampDeviceParameterServiceImpl extends ServiceImpl<LampDeviceParameterMapper, LampDeviceParameter> implements LampDeviceParameterService {

    @Autowired
    private SystemDeviceParameterService systemDeviceParameterService;

    @Override
    public Integer selectSystemDeviceIdByIndexAndLoopNum(String locationControlNum, String indexField, Integer index,
                                                         String loopNumField, Integer loopNum) {
        if (StringUtils.isBlank(locationControlNum) || StringUtils.isBlank(indexField) || StringUtils.isBlank(loopNumField) || index == null || loopNum == null) {
            return null;
        }
        return baseMapper.selectSystemDeviceIdByIndexAndLoopNum(locationControlNum, indexField, index, loopNumField, loopNum);
    }

    @Override
    public boolean saveParamValue(Integer deviceId, Integer paramId, String paramValue) {
        if (deviceId == null || paramId == null) {
            return false;
        }
        QueryWrapper<LampDeviceParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        queryWrapper.eq("parameter_id",paramId);
        Integer integer = baseMapper.selectCount(queryWrapper);
        if(integer!=null&&integer>0){
            baseMapper.updateParamValue(deviceId, paramValue, paramId);
        }else {
            baseMapper.insertParamValue(deviceId, paramValue, paramId);
        }
        return true;
    }

    @Override
    public boolean addDefaultParamValue(Integer deviceId, Integer deviceTypeId) {
        List<LampDeviceParameter> lampDeviceParameterList = new ArrayList<>();
        LambdaQueryWrapper<SystemDeviceParameter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SystemDeviceParameter::getId);
        queryWrapper.eq(SystemDeviceParameter::getDeviceTypeId,deviceTypeId);
        List<SystemDeviceParameter> systemDeviceParameterList = systemDeviceParameterService.list(queryWrapper);
        List<Integer> systemDeviceParameterIdList = systemDeviceParameterList.stream().map(SystemDeviceParameter::getId).collect(Collectors.toList());
        for (Integer systemDeviceParameterId : systemDeviceParameterIdList) {
            LampDeviceParameter lampDeviceParameter = new LampDeviceParameter();
            lampDeviceParameter.setDeviceId(deviceId);
            lampDeviceParameter.setParameterId(systemDeviceParameterId);
            lampDeviceParameter.setParameterValue("0");
            lampDeviceParameterList.add(lampDeviceParameter);
        }
        return this.saveBatch(lampDeviceParameterList);
    }

    @Override
    public boolean deleteByDeviceIds(List<Integer> deviceIdList) {
        QueryWrapper<LampDeviceParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("device_id",deviceIdList);
        baseMapper.delete(queryWrapper);
        return true;
    }

    @Override
    public String select(Integer deviceId, String name,Integer deviceTypeId) {
        String value = baseMapper.select(deviceId,name,deviceTypeId);
        return value;
    }

    @Override
    public Integer selectDeviceIdByLoopNum(String num, String loopNum, Integer deviceTypeId) {
        return baseMapper.selectDeviceIdByLoopNum(num,loopNum,deviceTypeId);
    }
}