package com.exc.street.light.electricity.util;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.exc.street.light.electricity.service.CanChannelService;
import com.exc.street.light.electricity.service.ElectricityNodeService;
import com.exc.street.light.resource.dto.electricity.Online;
import com.exc.street.light.resource.dto.electricity.OnlineReturnJson;
import com.exc.street.light.resource.dto.electricity.OnlineStr;
import com.exc.street.light.resource.entity.electricity.ElectricityNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:xujiahao
 * @Description 在线工具类
 * @Data:Created in 10:07 2018/2/2
 * @Modified By:
 */
@Component
public class OnlineUtil {
    private static final Logger logger = LoggerFactory.getLogger(OnlineUtil.class);
    public static ElectricityNodeService electricityNodeService;
    public static CanChannelService canChannelService;
    private static RedisUtil redisUtil;

    @Autowired
    public void setElectricityNodeService(ElectricityNodeService electricityNodeService) {
        OnlineUtil.electricityNodeService = electricityNodeService;
    }

    @Autowired
    public void setCanChannelService(CanChannelService canChannelService) {
        OnlineUtil.canChannelService = canChannelService;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        OnlineUtil.redisUtil = redisUtil;
    }

    /**
     * websocket返回json生成类
     *
     * @param onlineMap 在线集合
     * @param expireSec 超时的时间 s
     * @return
     */
    public static String returnJson(HashMap<String, Double> onlineMap, int expireSec) {
        OnlineReturnJson returnJson = new OnlineReturnJson();
        returnJson.setType(6);
        OnlineStr[] onlineStrs = new OnlineStr[onlineMap.size()];
        if (onlineMap.isEmpty()) {
            returnJson.setData(onlineStrs);
            return JSON.toJSONString(returnJson);
        }
        //提取map中的mac地址集合
        List<String> macList = onlineMap.entrySet().stream().filter(e -> StringUtils.isNotBlank(e.getKey())).map(e -> e.getKey()).collect(Collectors.toList());
        if (macList.isEmpty()) {
            returnJson.setData(onlineStrs);
            return JSON.toJSONString(returnJson);
        }
        LambdaQueryWrapper<ElectricityNode> nodeWrapper = new LambdaQueryWrapper<>();
        nodeWrapper.select(ElectricityNode::getId, ElectricityNode::getNum, ElectricityNode::getName,ElectricityNode::getMac, ElectricityNode::getIp, ElectricityNode::getIsOffline)
                .in(ElectricityNode::getMac, macList);
        List<ElectricityNode> nodeList = electricityNodeService.list(nodeWrapper);
        if (nodeList.isEmpty()) {
            returnJson.setData(onlineStrs);
            return JSON.toJSONString(returnJson);
        }
        //离线id集合
        List<Integer> offlineIdList = new ArrayList<>();
        //在线id集合
        List<Integer> onlineIdList = new ArrayList<>();
        int i = 0;
        for (ElectricityNode node : nodeList) {
            OnlineStr onlineStr = new OnlineStr();
            onlineStr.setNodeName(node.getMac());
            Double lastOnlineTime = onlineMap.get(node.getMac());
            if (lastOnlineTime == null) {
                continue;
            }
            //得到存入时间和当前时间的差
            long time = (long) (System.currentTimeMillis() - lastOnlineTime);
            //超时时间设置为10分钟
            if ((time - expireSec * 1000) > 0) {
                //超时
                onlineStr.setIsOnline(0);
                onlineStr.setOfflineTime(DateUtil.date2String(DateUtil.long2Date(time)));
                Object status = redisUtil.hmGet(ConstantUtil.MAC_HEARTBEAT_STATUS_KEY, node.getMac());
                if (status == null) {
                    // 当值为空时,节点处理离线状态,记录第一次离线故障
//                    faultStatisticalService.add(node, faultType);
                }
                // 当前节点在线,且最新状态是离线时,才报节点离线故障;status做非空判断,避免第一次插入两条相同的故障
                if (node.getIsOffline().equals(0) && status != null) {
//                    faultStatisticalService.add(node, faultType);
                }
                // 设置节点状态在redis的值
                redisUtil.hmSet(ConstantUtil.MAC_HEARTBEAT_STATUS_KEY, node.getMac(), 1);
                offlineIdList.add(node.getId());
            } else {
                //仍然在线
                redisUtil.hmSet(ConstantUtil.MAC_HEARTBEAT_STATUS_KEY, node.getMac(), 0);
                if (node.getIsOffline().equals(1)) {
                    onlineIdList.add(node.getId());
                }
                onlineStr.setIsOnline(1);
                onlineStr.setOfflineTime(DateUtil.date2String(new Date()));
            }
            onlineStrs[i++] = onlineStr;
        }
        //更新设备状态
        if (!onlineIdList.isEmpty()) {
            LambdaUpdateWrapper<ElectricityNode> updateOnlineWrapper = new LambdaUpdateWrapper<>();
            updateOnlineWrapper.set(ElectricityNode::getIsOffline, 0).in(ElectricityNode::getId, onlineIdList);
            electricityNodeService.update(updateOnlineWrapper);
        }
        if (!offlineIdList.isEmpty()) {
            LambdaUpdateWrapper<ElectricityNode> updateOnlineWrapper = new LambdaUpdateWrapper<>();
            updateOnlineWrapper.set(ElectricityNode::getIsOffline, 1).in(ElectricityNode::getId, offlineIdList);
            electricityNodeService.update(updateOnlineWrapper);
        }
        returnJson.setData(onlineStrs);
        return JSON.toJSONString(returnJson);
    }
}
