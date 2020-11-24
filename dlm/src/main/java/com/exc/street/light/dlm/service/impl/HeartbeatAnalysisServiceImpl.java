package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exc.street.light.dlm.service.HeartbeatAnalysisService;
import com.exc.street.light.dlm.service.LocationControlService;
import com.exc.street.light.dlm.utils.*;
import com.exc.street.light.resource.entity.dlm.LocationControl;
import com.exc.street.light.resource.entity.electricity.ElectricityHeartbeatLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 心跳包服务实现类
 *
 * @author Linshiwen
 * @date 2018/7/31
 */
@Service
public class HeartbeatAnalysisServiceImpl implements HeartbeatAnalysisService {
    private static final Logger logger = LoggerFactory.getLogger(HeartbeatAnalysisServiceImpl.class);
    @Autowired
    private LocationControlService controlService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SocketClient socketClient;

    @Override
    public void analyze(byte[] data, String clientIP) {
        //获取物理地址
        String address = ArrayUtil.getAddress(data);
        //通过物理地址查询强电节点IP和端口
        LambdaQueryWrapper<LocationControl> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(LocationControl::getId,LocationControl::getNum,LocationControl::getName,LocationControl::getIp);
        wrapper.eq(LocationControl::getMac, address);
        wrapper.last("limit 1");
        LocationControl control = controlService.getOne(wrapper);
        if (control != null) {
            String num = control.getNum();
            Object startTime = redisUtil.hmGet(ConstantUtil.FIRST_HEARTBEAT_KEY, num);
            if (startTime == null) {
                logger.info("记录第一次心跳包时间:{}", control.getName());
                redisUtil.hmSet(ConstantUtil.FIRST_HEARTBEAT_KEY, num, new Date());
            }
            String ip = control.getIp();
            byte[] time = ProtocolUtil.setTime(address);
            if (clientIP != null && !ip.equals(clientIP)) {
                //保存ip或port
                control.setIp(clientIP);
            }
            control.setIsOnline(1);
            control.setLastOnlineTime(new Date());
            controlService.updateById(control);
            //记录心跳包
            Date date1 = new Date();
            ElectricityHeartbeatLog log = new ElectricityHeartbeatLog();
            log.setNid(control.getId());
            log.setRecordTime(date1);
            log.setNodeTime(date1);
            log.setIp(clientIP);
            String tohex = HexUtil.bytesTohex(data);
            log.setData(StringUtils.deleteWhitespace(tohex));
            redisUtil.lSet(ConstantUtil.HEART_BEAT_KEY, log);
            //value自动写入当前时间戳
            redisUtil.zadd("owner:StrongOnlineSet", control.getNum());
            logger.warn("心跳时间:{}", date1);
        }
    }
}
