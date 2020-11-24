package com.exc.street.light.electricity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.electricity.ElectricityHeartbeatLog;

import java.util.Date;


/**
 * 心跳包日志服务接口
 *
 * @author Linshiwen
 * @date 2018/11/01
 */
public interface ElectricityHeartbeatLogService extends IService<ElectricityHeartbeatLog> {
    /**
     * 根据节点id查询
     *
     * @param id
     * @return
     */
    Date findByNid(Integer id);
}
