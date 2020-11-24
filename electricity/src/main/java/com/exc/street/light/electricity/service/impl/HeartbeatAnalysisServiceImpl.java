package com.exc.street.light.electricity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exc.street.light.electricity.service.ElectricityNodeService;
import com.exc.street.light.electricity.service.HeartbeatAnalysisService;
import com.exc.street.light.electricity.util.*;
import com.exc.street.light.resource.entity.electricity.ElectricityHeartbeatLog;
import com.exc.street.light.resource.entity.electricity.ElectricityNode;
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
    private ElectricityNodeService electricityNodeService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SocketClient socketClient;

    @Override
    public void analyze(byte[] data, String clientIP) {
        //获取物理地址 mac
        String address = ArrayUtil.getAddress(data);
        //通过物理地址查询强电节点IP和端口
        LambdaQueryWrapper<ElectricityNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ElectricityNode::getId, ElectricityNode::getNum, ElectricityNode::getName, ElectricityNode::getIp);
        wrapper.eq(ElectricityNode::getMac, address);
        wrapper.last("limit 1");
        ElectricityNode electricityNode = electricityNodeService.getOne(wrapper);
        if (electricityNode != null) {
            String num = electricityNode.getNum();
            Object startTime = redisUtil.hmGet(ConstantUtil.FIRST_HEARTBEAT_KEY, num);
            if (startTime == null) {
                logger.info("记录第一次心跳包时间:{}", electricityNode.getName());
                redisUtil.hmSet(ConstantUtil.FIRST_HEARTBEAT_KEY, num, new Date());
            }
            String ip = electricityNode.getIp();
            byte[] time = ProtocolUtil.setTime(address);
            if (clientIP != null && !ip.equals(clientIP)) {
                //保存ip或port
                electricityNode.setIp(clientIP);
            }
            electricityNode.setIsOffline(0);
            electricityNode.setLastOnlineTime(new Date());
            electricityNodeService.updateById(electricityNode);
            //记录心跳包
            Date date1 = new Date();
            ElectricityHeartbeatLog log = new ElectricityHeartbeatLog();
            log.setNid(electricityNode.getId());
            log.setRecordTime(date1);
            log.setNodeTime(date1);
            log.setIp(clientIP);
            String tohex = HexUtil.bytesTohex(data);
            log.setData(StringUtils.deleteWhitespace(tohex));
            redisUtil.lSet(ConstantUtil.HEART_BEAT_KEY, log);
            //写入mac进在线列表 value自动写入当前时间戳
            redisUtil.zadd(ConstantUtil.ONLINE_MAC_SET, address);
            logger.warn("心跳时间:{}", date1);
        }
    }
}
