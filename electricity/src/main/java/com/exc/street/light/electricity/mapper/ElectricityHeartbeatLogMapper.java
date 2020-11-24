package com.exc.street.light.electricity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.electricity.ElectricityHeartbeatLog;

import java.util.Date;

/**
 * 心跳包日志mapper
 *
 * @author Linshiwen
 * @date 2018/11/13
 */
public interface ElectricityHeartbeatLogMapper extends BaseMapper<ElectricityHeartbeatLog> {
    /**
     * 根据节点id查询
     *
     * @param nid
     * @return
     */
    Date selectByNid(Integer nid);
}