package com.exc.street.light.ed.quartz;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.exc.street.light.ed.mapper.AlarmMapper;
import com.exc.street.light.ed.mapper.EdAshcanMapper;
import com.exc.street.light.ed.mapper.EdManholeCoverDeviceMapper;
import com.exc.street.light.ed.po.KafkaMessage;
import com.exc.street.light.ed.service.KafkaMessageService;
import com.exc.street.light.resource.dto.WebsocketQuery;
import com.exc.street.light.resource.entity.ed.EdAshcan;
import com.exc.street.light.resource.entity.ed.EdManholeCoverDevice;
import com.exc.street.light.resource.entity.woa.Alarm;
import com.exc.street.light.resource.vo.req.SlControlSystemDeviceVO;
import com.exc.street.light.resource.vo.resp.EdRespAlarmVO;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpringTask {

    private static final Logger log = LoggerFactory.getLogger(SpringTask.class);

    @Autowired
    private AlarmMapper alarmMapper;

    @Autowired
    private EdAshcanMapper edAshcanMapper;

    @Autowired
    private EdManholeCoverDeviceMapper edManholeCoverDeviceMapper;

    @Autowired
    private KafkaMessageService kafkaMessageService;

    @Scheduled(cron = "0 0/5 * * * *")
    public void strategyDistribution(){
        log.info("定时任务：重新推送未处理且已读的垃圾桶/井盖告警信息");
        QueryWrapper<Alarm> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("device_type_id",14,15);
        queryWrapper.eq("status",1);
        queryWrapper.eq("have_read",1);
        List<Alarm> alarmList = alarmMapper.selectList(queryWrapper);
        if(alarmList==null){
            log.info("无需要重新推送的告警信息");
            return;
        }
        List<EdAshcan> edAshcanList = new ArrayList<>();
        List<EdManholeCoverDevice> edManholeCoverDeviceList = new ArrayList<>();
        List<Integer> edAshcanIdList = alarmList.stream().filter(e -> e.getDeviceTypeId().equals(14)).map(Alarm::getDeviceId).distinct().collect(Collectors.toList());
        if(edAshcanIdList!=null&&edAshcanIdList.size()>0){
            edAshcanList = edAshcanMapper.selectBatchIds(edAshcanIdList);
        }
        List<Integer> edManholeCoverDeviceIdList = alarmList.stream().filter(e -> e.getDeviceTypeId().equals(15)).map(Alarm::getDeviceId).distinct().collect(Collectors.toList());
        if(edManholeCoverDeviceIdList!=null&&edManholeCoverDeviceIdList.size()>0){
            edManholeCoverDeviceList = edManholeCoverDeviceMapper.selectBatchIds(edManholeCoverDeviceIdList);
        }
        Map<Integer, String> edAshcanMap = edAshcanList.stream().collect(Collectors.toMap(EdAshcan::getId, EdAshcan::getNum));
        Map<Integer, String> edManholeCoverDeviceMap = edManholeCoverDeviceList.stream().collect(Collectors.toMap(EdManholeCoverDevice::getId, EdManholeCoverDevice::getNum));


        Map<Pair<Integer, Integer>, List<Alarm>> alarmGroupingByFlagMap =
                alarmList.stream().collect(Collectors.groupingBy(p -> Pair.of(p.getDeviceId(), p.getDeviceTypeId())));
        List<Pair<Integer, Integer>> alarmGroupingByFlagList = alarmList.stream().map(p -> Pair.of(p.getDeviceId(), p.getDeviceTypeId())).distinct().collect(Collectors.toList());

        for (Pair<Integer, Integer> alarmGroupingPair : alarmGroupingByFlagList) {
            List<Alarm> alarmLists = alarmGroupingByFlagMap.get(alarmGroupingPair);
            Alarm alarm = alarmLists.get(0);
            KafkaMessage kafkaMessage = new KafkaMessage();
            kafkaMessage.setType(1);
            kafkaMessage.setIs2All(2);
            kafkaMessage.setUserIds(null);
            WebsocketQuery<EdRespAlarmVO> websocketQuery = new WebsocketQuery<>();
            Integer deviceTypeId = alarm.getDeviceTypeId();
            Integer deviceId = alarm.getDeviceId();
            //获取告警对象
            EdRespAlarmVO edRespAlarmVO = null;
            if(deviceTypeId==14){
                websocketQuery.setType(17);
                String num = edAshcanMap.get(deviceId);
                if(num!=null){
                    edRespAlarmVO = edAshcanMapper.selectEdAshcanAlarm(num);
                }

            }else {
                websocketQuery.setType(18);
                String num = edManholeCoverDeviceMap.get(deviceId);
                if(num!=null){
                    edRespAlarmVO = edManholeCoverDeviceMapper.selectEdManholeCoverDeviceAlarm(num);
                }
            }
            edRespAlarmVO.setAlarmTime(alarm.getCreateTime());
            edRespAlarmVO.setAlarmStatus(1);
            edRespAlarmVO.setId(alarm.getId());
            websocketQuery.setData(edRespAlarmVO);
            String message = JSON.toJSONString(websocketQuery);
            kafkaMessage.setMessage(message);
            kafkaMessageService.sendMessage("websocket1",kafkaMessage);
            log.info("告警信息重新推送成功：{}",kafkaMessage);
        }
    }



}