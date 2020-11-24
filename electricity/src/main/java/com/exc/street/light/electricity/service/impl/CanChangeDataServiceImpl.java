package com.exc.street.light.electricity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.mapper.CanChangeDataMapper;
import com.exc.street.light.electricity.service.*;
import com.exc.street.light.electricity.util.AnalysisUtil;
import com.exc.street.light.electricity.util.ConstantUtil;
import com.exc.street.light.electricity.util.RedisUtil;
import com.exc.street.light.resource.entity.electricity.CanChangeData;
import com.exc.street.light.resource.entity.electricity.ElectricityNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 变化数据解析服务实现类
 *
 * @author Linshiwen
 * @date 2018/7/31
 */
@Service
public class CanChangeDataServiceImpl extends ServiceImpl<CanChangeDataMapper, CanChangeData> implements CanChangeDataService {
    private static final Logger logger = LoggerFactory.getLogger(CanChangeDataServiceImpl.class);
    @Autowired
    private CanChangeDataMapper canChangeDataMapper;
    @Autowired
    private CanDeviceService canDeviceService;
    @Lazy
    @Autowired
    private CanChannelService canChannelService;
    @Autowired
    private ElectricityNodeService electricityNodeService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ComChannelService comChannelService;
    /*@Autowired
    private AlarmLogService alarmLogService;
    @Autowired
    private SocketIoService socketIoService;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private FaultStatisticalService faultStatisticalService;*/

    @Override
    public void analyze(byte[] data) {
        //分析变化数据
        Map<String, List<CanChangeData>> changeData = AnalysisUtil.getChangeData(data);
        Set<String> strings = changeData.keySet();
        Object[] array = strings.toArray();
        String address = (String) array[0];
        logger.info("开始解析变化数据:{}", address);
        //通过物理地址查询对应的节点
        LambdaQueryWrapper<ElectricityNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ElectricityNode::getMac, address);
        wrapper.last("limit 1");
        ElectricityNode electricityNode = electricityNodeService.getOne(wrapper);
        Integer nid = electricityNode.getId();
        //遍历变化数据
        List<CanChangeData> changeDataList = changeData.get(address);
        Date date = new Date();
        //回路开关变化
        List<CanChangeData> collect = changeDataList.stream().filter(e -> (e.getType() == 1))
                .collect(Collectors.toList());
        for (CanChangeData canChangeData : collect) {
            //将变化数据缓存到redis
            canChangeData.setNid(nid);
            //接收时间
            canChangeData.setReceiveTime(date);
            redisUtil.lSet(ConstantUtil.CHANGE_DATA_KEY, canChangeData);
            canChannelService.handleSwitch(nid, canChangeData);
        }
        //回路电流变化
        List<CanChangeData> collect2 = changeDataList.stream()
                .filter(canChangeData -> canChangeData.getType() == 2)
//                .filter(canChangeData -> canChangeData.getTagId() < 401)
                .collect(Collectors.toList());
        for (CanChangeData canChangeData : collect2) {
            //将变化数据缓存到redis
            canChangeData.setNid(nid);
            //接收时间
            canChangeData.setReceiveTime(date);
            redisUtil.lSet(ConstantUtil.CHANGE_DATA_KEY, canChangeData);
            canChannelService.handleCurrent(nid, canChangeData);
        }
        logger.info("结束解析变化数据:{}", address);
    }

    public void setStatus(Date date, ElectricityNode electricityNode) {
        /*logger.info("更新节点:{}开关量为开", electricityNode.getName());
        electricityNode.setIsOpen(0);
        logger.info("保存到强电箱开门故障中");
        FaultStatistical faultStatistical = new FaultStatistical();
        faultStatistical.setNum(electricityNode.getNum());
        faultStatistical.setCategory(5);
        faultStatistical.setCreateTime(date);
        faultStatistical.setBuildingId(electricityNode.getBuildingId());
        Building building = buildingService.findById(electricityNode.getBuildingId());
        if (building != null) {
            faultStatistical.setPartitionId(building.getPartitionId());
        } else {
            faultStatistical.setPartitionId(1);
        }
        faultStatistical.setType(ConstantUtil.FAULT_TYPE_49);
        faultStatistical.setContent("强电节点:" + electricityNode.getName() + ",电箱门打开");
        faultStatisticalService.save(faultStatistical);*/
    }
}
