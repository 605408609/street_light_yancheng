/**
 * @filename:AlarmServiceImpl 2020-09-24
 * @project sl  V1.0
 * Copyright(c) 2018 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.sl.SystemDevice;
import com.exc.street.light.resource.entity.woa.Alarm;
import com.exc.street.light.sl.mapper.AlarmMapper;
import com.exc.street.light.sl.service.AlarmService;
import com.exc.street.light.sl.service.SystemDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;

/**   
 * @Description:TODO(告警表服务实现)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Service
public class AlarmServiceImpl  extends ServiceImpl<AlarmMapper, Alarm> implements AlarmService  {

    @Autowired
    private SystemDeviceService systemDeviceService;

    @Override
    public boolean addLampAlarm(SlLampPost slLampPost, String alarmString, Integer deviceId) {
        String temperature = alarmString.substring(15, 16);
        String electricOne = alarmString.substring(14, 15);
        String voltage = alarmString.substring(13, 14);
        String tilt = alarmString.substring(12, 13);
        String leakage = alarmString.substring(11, 12);
        String electricTwo = alarmString.substring(10, 11);

        Integer lampPostId = slLampPost.getId();
        Alarm alarm = new Alarm();
        if(slLampPost.getLocation()!=null){
            alarm.setAddr(slLampPost.getLocation());
        }
        alarm.setCreateTime(new Date());
        alarm.setLampPostId(lampPostId);
        alarm.setHaveRead(0);
        SystemDevice systemDevice = systemDeviceService.getById(deviceId);
        alarm.setDeviceId(deviceId);
        alarm.setDeviceName(systemDevice.getName());
        alarm.setDeviceTypeId(1);

        if("1".equals(temperature)){
            //生成温度告警
            Alarm lastTime = baseMapper.getLastTime(lampPostId, 5);
            if(lastTime!=null){
                if(lastTime.getStatus()==3){
                    alarm.setTypeId(5);
                    alarm.setStatus(1);
                    baseMapper.insert(alarm);
                }
            }else {
                alarm.setTypeId(5);
                alarm.setStatus(1);
                baseMapper.insert(alarm);
            }
        }else {
            //解除温度告警
            Alarm lastTime = baseMapper.getLastTime(lampPostId, 5);
            if(lastTime!=null){
                if(lastTime.getStatus()!=3){
                    lastTime.setStatus(3);
                    baseMapper.updateById(lastTime);
                }
            }/*else {
                alarm.setTypeId(5);
                alarm.setStatus(3);
                baseMapper.insert(alarm);
            }*/
        }

        if("1".equals(voltage)){
            //生成电压告警
            Alarm lastTime = baseMapper.getLastTime(lampPostId, 4);
            if(lastTime!=null){
                if(lastTime.getStatus()==3){
                    alarm.setTypeId(4);
                    alarm.setStatus(1);
                    baseMapper.insert(alarm);
                }
            }else {
                alarm.setTypeId(4);
                alarm.setStatus(1);
                baseMapper.insert(alarm);
            }
        }else {
            //解除电压告警
            Alarm lastTime = baseMapper.getLastTime(lampPostId, 4);
            if(lastTime!=null){
                if(lastTime.getStatus()!=3){
                    lastTime.setStatus(3);
                    baseMapper.updateById(lastTime);
                }
            }/*else {
                alarm.setTypeId(5);
                alarm.setStatus(4);
                baseMapper.insert(alarm);
            }*/
        }

        if("1".equals(tilt)){
            //生成倾斜告警
            Alarm lastTime = baseMapper.getLastTime(lampPostId, 12);
            if(lastTime!=null){
                if(lastTime.getStatus()==3){
                    alarm.setTypeId(12);
                    alarm.setStatus(1);
                    baseMapper.insert(alarm);
                }
            }else {
                alarm.setTypeId(12);
                alarm.setStatus(1);
                baseMapper.insert(alarm);
            }
        }else {
            //解除倾斜告警
            Alarm lastTime = baseMapper.getLastTime(lampPostId, 12);
            if(lastTime!=null){
                if(lastTime.getStatus()!=3){
                    lastTime.setStatus(3);
                    baseMapper.updateById(lastTime);
                }
            }/*else {
                alarm.setTypeId(12);
                alarm.setStatus(3);
                baseMapper.insert(alarm);
            }*/
        }

        if("1".equals(leakage)){
            //生成漏电告警
            Alarm lastTime = baseMapper.getLastTime(lampPostId, 13);
            if(lastTime!=null){
                if(lastTime.getStatus()==3){
                    alarm.setTypeId(13);
                    alarm.setStatus(1);
                    baseMapper.insert(alarm);
                }
            }else {
                alarm.setTypeId(13);
                alarm.setStatus(1);
                baseMapper.insert(alarm);
            }
        }else {
            //解除漏电告警
            Alarm lastTime = baseMapper.getLastTime(lampPostId, 13);
            if(lastTime!=null){
                if(lastTime.getStatus()!=3){
                    lastTime.setStatus(3);
                    baseMapper.updateById(lastTime);
                }
            }/*else {
                alarm.setTypeId(13);
                alarm.setStatus(3);
                baseMapper.insert(alarm);
            }*/
        }

        if("0".equals(electricOne)&&"0".equals(electricTwo)){
            //解除电流告警
            Alarm lastTime = baseMapper.getLastTime(lampPostId, 3);
            if(lastTime!=null){
                if(lastTime.getStatus()!=3){
                    lastTime.setStatus(3);
                    baseMapper.updateById(lastTime);
                }
            }/*else {
                alarm.setTypeId(3);
                alarm.setStatus(3);
                baseMapper.insert(alarm);
            }*/
        }else {
            //生成电流告警
            Alarm lastTime = baseMapper.getLastTime(lampPostId, 3);
            if(lastTime!=null){
                if(lastTime.getStatus()==3){
                    alarm.setTypeId(3);
                    alarm.setStatus(1);
                    baseMapper.insert(alarm);
                }
            }else {
                alarm.setTypeId(3);
                alarm.setStatus(1);
                baseMapper.insert(alarm);
            }
        }
        return true;
    }

    @Override
    public Alarm getLastTime(Integer lampPostId, Integer typeId) {
        return baseMapper.getLastTime(lampPostId,typeId);
    }
}