/**
 * @filename:AlarmService 2020-09-24
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.woa.Alarm;

/**
 * @Description:TODO(告警表服务层)
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
public interface AlarmService extends IService<Alarm> {

    /**
     * 解析灯具告警信息
     * @param slLampPost
     * @param alarmString
     * @return
     */
    boolean addLampAlarm(SlLampPost slLampPost, String alarmString,Integer deviceId);

    /**
     * 获取指定灯杆及告警类型的最新一条数据
     * @param lampPostId
     * @param typeId
     * @return
     */
    Alarm getLastTime(Integer lampPostId,Integer typeId);
	
}