package com.exc.street.light.electricity.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.electricity.CanAlarmData;

/**
 *
 * @author CodeGenerator
 * @date 2018/07/31
 */
public interface CanAlarmDataService extends IService<CanAlarmData> {

    void analyze(byte[] data);
}