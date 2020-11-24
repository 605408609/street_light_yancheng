package com.exc.street.light.dlm.utils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exc.street.light.dlm.service.LocationControlService;
import com.exc.street.light.resource.dto.electricity.Online;
import com.exc.street.light.resource.dto.electricity.OnlineReturnJson;
import com.exc.street.light.resource.dto.electricity.OnlineStr;
import com.exc.street.light.resource.entity.dlm.LocationControl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
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
    @Autowired
    private LocationControlService locationControlService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * websocket返回json生成类
     *
     * @param onlineMap 在线集合
     * @param expireSec 超时的时间 s
     * @return
     */
    public String returnJson(HashMap<String, Double> onlineMap, int expireSec) {
        OnlineReturnJson returnJson = new OnlineReturnJson();
        OnlineStr[] onlineStrs = new OnlineStr[onlineMap.size()];
        List<LocationControl> controlList = new ArrayList<>();
        int i = 0;
//        FaultType faultType = faultTypeService.findById(ConstantUtil.FAULT_TYPE_29);
        Set<String> controlNumSet = onlineMap.keySet().stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        if(controlNumSet.isEmpty()){
            returnJson.setType(6);
            returnJson.setData(onlineStrs);
            return JSON.toJSONString(returnJson);
        }
        LambdaQueryWrapper<LocationControl> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(LocationControl::getId, LocationControl::getNum, LocationControl::getName, LocationControl::getIp, LocationControl::getIsOnline);
        wrapper.in(LocationControl::getNum, controlNumSet);
        List<LocationControl> controlList1 = locationControlService.list(wrapper);
        Date now = new Date();
        for (LocationControl control : controlList1) {
            Online online = new Online();
            OnlineStr onlineStr = new OnlineStr();
            onlineStr.setNodeName(control.getNum());
            online.setNodeName(control.getNum());
            //得到存入时间和当前时间的差
            long time = (long) (System.currentTimeMillis() - onlineMap.get(control.getNum()));
            Integer isOnline = control.getIsOnline();
            if ((time - expireSec * 1000) > 0) {
                //长于10分钟 离线
                control.setIsOnline(0);
                control.setLastOnlineTime(DateUtil.long2Date(time));
                onlineStr.setIsOnline(0);
                onlineStr.setOfflineTime(DateUtil.date2String(DateUtil.long2Date(time)));
                Object status = redisUtil.hmGet(ConstantUtil.MAC_HEARTBEAT_STATUS_KEY, control.getNum());
                if (status == null) {
                    // 当值为空时,节点处理离线状态,记录第一次离线故障
//                    faultStatisticalService.add(node, faultType);
                }
                //当前节点在线,且最新状态是离线时,才报节点离线故障;status做非空判断,避免第一次插入两条相同的故障
                if (isOnline.equals(1) && status != null) {
//                    faultStatisticalService.add(node, faultType);
                }
                //设置节点状态在redis的值
                redisUtil.hmSet(ConstantUtil.MAC_HEARTBEAT_STATUS_KEY, control.getNum(), 1);
            } else {
                //短于10分钟
                redisUtil.hmSet(ConstantUtil.MAC_HEARTBEAT_STATUS_KEY, control.getNum(), 0);
                /*if (isOffline.equals(1)) {
                    logger.info("查询节点回路状态", node.getName());
                    canChannelService.updateState(node);
                }*/
                control.setIsOnline(1);
                control.setLastOnlineTime(now);
                onlineStr.setIsOnline(1);
                onlineStr.setOfflineTime(DateUtil.date2String(now));
            }
            //添加至数据库
            controlList.add(control);
            onlineStrs[i] = onlineStr;
            ++i;
        }
        if (!controlList.isEmpty()) {
            //更新在线状态
            locationControlService.updateBatchById(controlList);
        }
        returnJson.setType(6);
        returnJson.setData(onlineStrs);
        return JSON.toJSONString(returnJson);
    }
}
