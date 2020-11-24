package com.exc.street.light.electricity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.mapper.ElectricityHeartbeatLogMapper;
import com.exc.street.light.electricity.service.ElectricityHeartbeatLogService;
import com.exc.street.light.resource.entity.electricity.ElectricityHeartbeatLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
* @author Linshiwen
* @date 2018/11/01
*/
@Service
public class ElectricityHeartbeatLogServiceImpl extends ServiceImpl<ElectricityHeartbeatLogMapper,ElectricityHeartbeatLog> implements ElectricityHeartbeatLogService {
    @Autowired
    private ElectricityHeartbeatLogMapper electricityHeartbeatLogMapper;

    @Override
    public Date findByNid(Integer nid) {
        return electricityHeartbeatLogMapper.selectByNid(nid);
    }
}
