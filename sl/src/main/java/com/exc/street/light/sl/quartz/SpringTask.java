package com.exc.street.light.sl.quartz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationControl;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.sl.*;
import com.exc.street.light.sl.VO.SingleLampParamVO;
import com.exc.street.light.sl.service.*;
import com.exc.street.light.sl.utils.MessageOperationUtil;
import com.exc.street.light.sl.utils.RedisUtil;
import com.exc.street.light.sl.utils.SendHttpsUtil;
import com.exc.street.light.sl.utils.ZkzlProtocolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class SpringTask {
    private static final Logger log = LoggerFactory.getLogger(SpringTask.class);

    @Autowired
    LampDeviceService lampDeviceService;

    @Autowired
    LampEnergyService lampEnergyService;

    @Autowired
    LampEnergyDayService lampEnergyDayService;

    @Autowired
    LampEnergyMonthService lampEnergyMonthService;

    @Autowired
    SingleLampParamService singleLampParamService;

    @Autowired
    LocationControlService locationControlService;

    @Autowired
    SystemDeviceService systemDeviceService;

    @Autowired
    private LampStrategyService lampStrategyService;

    @Autowired
    private LampStrategyActionService lampStrategyActionService;

    @Autowired
    private DeviceStrategyHistoryService deviceStrategyHistoryService;

    @Autowired
    RedisUtil redisUtil;

    @Scheduled(cron = "0 0/3 * * * *")
    public void strategyDistribution(){
        log.info("定时任务：下发设备策略");
        QueryWrapper<SystemDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("set_strategy",0);
        queryWrapper.isNotNull("strategy_id");
        queryWrapper.and(e->e.le("device_type_id",10).or().in("device_type_id",14,15));
        //获取需要下发策略的设备（含离线状态设备）
        List<SystemDevice> systemDeviceListTemp = systemDeviceService.list(queryWrapper);
        //分离在线设备id集合
        List<Integer> deviceIdListTemp = systemDeviceListTemp.stream().map(SystemDevice::getId).collect(Collectors.toList());
        //分离在线设备id集合
        List<Integer> deviceIdListTempIsOnline = systemDeviceListTemp.stream().filter(e->e.getIsOnline().equals(1)).map(SystemDevice::getId).collect(Collectors.toList());
        log.info("需要下发策略的设备：{}",deviceIdListTempIsOnline);
        if(deviceIdListTemp==null||deviceIdListTemp.size()==0){
            return;
        }
        //每个设备的所有历史记录集合
        QueryWrapper<DeviceStrategyHistory> deviceStrategyHistoryQueryWrapper = new QueryWrapper<>();
        deviceStrategyHistoryQueryWrapper.in("device_id",deviceIdListTemp);
        List<DeviceStrategyHistory> deviceStrategyHistoryAll = deviceStrategyHistoryService.list(deviceStrategyHistoryQueryWrapper);
        //每个设备的最新一条历史记录集合
        List<DeviceStrategyHistory> deviceStrategyHistoryLastList = deviceStrategyHistoryService.selectLastList(deviceIdListTemp);
        Map<Integer, DeviceStrategyHistory> deviceStrategyHistoryLastMap = deviceStrategyHistoryLastList.stream()
                .collect(Collectors.toMap(DeviceStrategyHistory::getDeviceId,obj->obj));
        //修改除最新一条历史记录外的其他记录状态
        deviceStrategyHistoryAll.removeAll(deviceStrategyHistoryLastList);
        deviceStrategyHistoryAll = deviceStrategyHistoryAll.stream().filter(e->e.getIsSuccess()<=50)
                .map(e->{
                    e.setIsSuccess(101);
                    return e;
                }).collect(Collectors.toList());
        /*for (DeviceStrategyHistory deviceStrategyHistory : deviceStrategyHistoryAll) {
            Integer isSuccess = deviceStrategyHistory.getIsSuccess();
            if(isSuccess <= 50){
                deviceStrategyHistory.setIsSuccess(101);
            }
        }*/
        if(deviceStrategyHistoryAll!=null&&deviceStrategyHistoryAll.size()>0){
            deviceStrategyHistoryService.updateBatchById(deviceStrategyHistoryAll);
        }
        log.info("修改被覆盖的策略的历史记录：{}",deviceStrategyHistoryAll);
        //修改正在下发的策略的历史记录状态
        deviceStrategyHistoryLastList = deviceStrategyHistoryLastList.stream().filter(e->e.getIsSuccess()<=50&&deviceIdListTempIsOnline.contains(e.getDeviceId()))
                .map(e->{
                    e.setIsSuccess(e.getIsSuccess()+1);
                    return e;
                }).collect(Collectors.toList());
        if(deviceStrategyHistoryLastList==null||deviceStrategyHistoryLastList.size()==0){
            return;
        }
        Set<Integer> deviceIdSet = deviceStrategyHistoryLastList.stream().map(DeviceStrategyHistory::getDeviceId).distinct().collect(Collectors.toSet());
        if(deviceStrategyHistoryLastList!=null&&deviceStrategyHistoryLastList.size()>0){
            deviceStrategyHistoryService.updateBatchById(deviceStrategyHistoryLastList);
        }
        log.info("修改正在下发的的策略的历史记录：{}",deviceStrategyHistoryLastList);
        //将设备根据策略id分组,同时去除已经下发失败的设备
        Map<Integer, List<SystemDevice>> groupByStrategyMap = systemDeviceListTemp.stream().filter(e->deviceIdSet.contains(e.getId()))
                .collect(Collectors.groupingBy(SystemDevice::getStrategyId));
        List<Integer> strategyIdList = systemDeviceListTemp.stream().filter(e->deviceIdSet.contains(e.getId())).map(SystemDevice::getStrategyId).distinct().collect(Collectors.toList());
        for (Integer strategyId : strategyIdList) {
            List<SystemDevice> systemDeviceList = groupByStrategyMap.get(strategyId);
            List<DeviceStrategyHistory> deviceStrategyHistoryList = new ArrayList<>();
            for (SystemDevice systemDevice : systemDeviceList) {
                //获取当前设备id最近的一条策略历史记录
                DeviceStrategyHistory deviceStrategyHistoryTemp = deviceStrategyHistoryLastMap.get(systemDevice.getId());
                if(!deviceStrategyHistoryTemp.getStrategyId().equals(strategyId)){
                    log.error("数据库最新历史记录中的策略id无法匹配设备绑定的策略id"+deviceStrategyHistoryTemp);
                    return;
                }
                deviceStrategyHistoryList.add(deviceStrategyHistoryTemp);
            }

            //获取策略场景号
            LampStrategy lampStrategy = lampStrategyService.getById(strategyId);
            Integer scene = lampStrategy.getScene();
            if(scene==null){
                //当策略场景为空时，判断策略过期失效，修改历史记录状态
                deviceStrategyHistoryList = deviceStrategyHistoryList.stream()
                        .map(e->{
                            e.setIsSuccess(102);
                            return e;
                        }).collect(Collectors.toList());
                deviceStrategyHistoryService.updateBatchById(deviceStrategyHistoryList);
                log.info("修改过期策略的历史记录：{}",deviceStrategyHistoryList);
                continue;
            }
            //获取策略动作集合
            QueryWrapper<LampStrategyAction> lampStrategyActionQueryWrapper = new QueryWrapper<>();
            lampStrategyActionQueryWrapper.eq("strategy_id",strategyId);
            List<LampStrategyAction> lampStrategyActionList = lampStrategyActionService.list(lampStrategyActionQueryWrapper);
            //执行策略
            lampStrategyService.singleLampExecute(systemDeviceList,lampStrategyActionList,scene,deviceStrategyHistoryList);
        }
    }


    @Scheduled(cron = "0 0/5 * * * *")
    public void taskState(){
        log.info("定时任务：检测灯具的在线状态");
        List<SystemDevice> resultList = new ArrayList<>();
        List<SystemDevice> systemDeviceList = systemDeviceService.list();
        if(systemDeviceList!=null && systemDeviceList.size()>0){
            Date date = new Date();
            long time = date.getTime();
            for (SystemDevice systemDevice : systemDeviceList) {
                Date lastOnlineTime = systemDevice.getLastOnlineTime();
                long lastOnline = lastOnlineTime.getTime();
                if(lastOnline+600000<time){
                    Integer isOnline = systemDevice.getIsOnline();
                    if(isOnline==1){
                        SystemDevice systemDeviceTemp = new SystemDevice();
                        systemDeviceTemp.setId(systemDevice.getId());
                        systemDeviceTemp.setIsOnline(0);
                        resultList.add(systemDeviceTemp);
                    }
                }
                if(lastOnline+240000>time){
                    Integer isOnline = systemDevice.getIsOnline();
                    if(isOnline==0){
                        SystemDevice systemDeviceTemp = new SystemDevice();
                        systemDeviceTemp.setId(systemDevice.getId());
                        systemDeviceTemp.setIsOnline(1);

                        Integer lampPostId = systemDevice.getLampPostId();
                        Integer deviceTypeId = systemDevice.getDeviceTypeId();
                        Integer setLonLat = systemDevice.getSetLonLat();
                        if(lampPostId != null && setLonLat !=1 && deviceTypeId != 11 && deviceTypeId != 12 && deviceTypeId != 13){
                            SlLampPost slLampPost = systemDeviceService.selectLampPostByDeviceId(systemDevice.getId());
                            if(slLampPost!=null){
                                Double longitude = slLampPost.getLongitude();
                                Double latitude = slLampPost.getLatitude();
                                String longitudeString = MessageOperationUtil.numToDHS(longitude, 1) + " E";
                                String latitudeString = MessageOperationUtil.numToDHS(latitude,2) + " N";
                                Boolean aBoolean = singleLampParamService.longitudeAndLatitudeSetting(longitudeString, latitudeString, systemDevice);
                                if(aBoolean){
                                    log.info("设置灯具经纬度成功：{}，{}，{}", longitudeString,latitudeString,systemDevice);
                                    systemDeviceTemp.setSetLonLat(1);
                                }else {
                                    log.info("设置灯具经纬度失败：{}，{}，{}", longitudeString,latitudeString,systemDevice);
                                }
                            }
                        }
                        resultList.add(systemDeviceTemp);
                    }
                }
            }
        }
        if(!resultList.isEmpty()){
            //修改灯具状态
            systemDeviceService.updateBatchById(resultList);
        }
    }


    @Scheduled(cron = "0 0/5 * * * *")
    public void taskStateShuncom(){
        log.info("定时任务：更新顺舟灯具状态相关信息");

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
            List<SingleLampParamVO> singleLampParamVOList = SendHttpsUtil.select("");
            Map<String,Date> map = new HashMap<>();
            //获取设备最后一次的上报时间
            for (SingleLampParamVO singleLampParamVO : singleLampParamVOList) {
                String deviceId = singleLampParamVO.getDeviceId();
                //最后一次更新时间
                String lastModifiedTime = singleLampParamVO.getLastModifiedTime();
                Date parse = dateFormat.parse(lastModifiedTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parse);
                calendar.add(Calendar.HOUR, 8);
                Date time = calendar.getTime();
                map.put(deviceId,time);
            }
            //对比当前时间修改在线状态
            Date date = new Date();
            long time = date.getTime();
            List<LampDevice> resultList = new ArrayList<>();
            Result<List<LampDevice>> list = lampDeviceService.getList();
            List<LampDevice> lampDeviceList = list.getData();
            //List<String> lampDeviceNumList = lampDeviceList.stream().map(LampDevice::getNum).collect(Collectors.toList());
            for (LampDevice lampDevice : lampDeviceList) {
                Date lastOnlineTime = map.get(lampDevice.getNum());
                if(lastOnlineTime!=null){
                    long lastOnline = lastOnlineTime.getTime();
                    Integer networkState = lampDevice.getNetworkState();
                    if(lastOnline+2000000<time){
                        if(networkState==1){
                            LampDevice lampDeviceTemp = new LampDevice();
                            lampDeviceTemp.setId(lampDevice.getId());
                            lampDeviceTemp.setNetworkState(0);
                            resultList.add(lampDeviceTemp);
                        }
                    }
                    if(lastOnline+1800000>time){
                        if(networkState==0){
                            LampDevice lampDeviceTemp = new LampDevice();
                            lampDeviceTemp.setId(lampDevice.getId());
                            lampDeviceTemp.setNetworkState(1);
                            resultList.add(lampDeviceTemp);
                        }
                    }
                }
            }
            //修改灯具状态
            if(!resultList.isEmpty()){
                lampDeviceService.updateBatchById(resultList);
            }
        }catch (Exception e){
            log.info("定时任务：更新灯具状态相关信息失败");
        }

    }

    @Scheduled(cron = "0 0 0/1 * * *")
    public void taskEnergyShuncom(){
        log.info("定时任务：获取顺舟灯具当天的能耗数据");
        Result<List<LampDevice>> list = lampDeviceService.getList();
        List<LampDevice> lampDeviceList = list.getData();
        List<String> lampDeviceNumList = new ArrayList<>();
        for (LampDevice lampDevice : lampDeviceList) {
            if("nb".equals(lampDevice.getModel())&&"EXC2".equals(lampDevice.getFactory())){
                lampDeviceNumList.add(lampDevice.getNum());
            }
        }
        SendHttpsUtil.electricEnergy(lampDeviceNumList);
    }


    /*@Scheduled(cron = "00 10 00 * * *")
    public void taskEnergy(){
        log.info("定时任务：获取前一天的能耗数据");
        SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM-dd");
        //前一天的年月日
        Long dateTime = System.currentTimeMillis() - 86400000;
        Date date = new Date();
        date.setTime(dateTime);
        String yesterday = dateFormatJustDay.format(date);

        //查询最新数据
        Result result = singleLampParamService.getList();
        if(result.getCode()==200){
            List<SingleLampParam> singleLampParamList = (List<SingleLampParam>)result.getData();
            //获取灯具设备集合

            //获取日能耗表对象集合
            List<LampEnergy> lampEnergyList = lampEnergyService.list();
            //将前天的累计能耗和设备id提取出来
            Map<Integer,Double> cumulativeEnergyMap = new HashMap<>();
            for (LampEnergy lampEnergy : lampEnergyList) {
                Date createTime = lampEnergy.getCreateTime();
                String format = dateFormatJustDay.format(createTime);
                if(yesterday.equals(format)){
                    Integer deviceId = lampEnergy.getDeviceId();
                    Double cumulativeEnergy = lampEnergy.getCumulativeEnergy();
                    cumulativeEnergyMap.put(deviceId,cumulativeEnergy);
                }
            }

            //遍历最新的数据集合
            for (SingleLampParam singleLampParam : singleLampParamList) {
                LampDevice lampDevice = lampDeviceService.getById(singleLampParam.getDeviceId());
                if("nb".equals(lampDevice.getModel())&&"EXC2".equals(lampDevice.getFactory())){
                    continue;
                }
                LampEnergy lampEnergy = new LampEnergy();
                //昨天累计能耗
                Double electricEnergyOne = singleLampParam.getElectricEnergy();
                Integer id = singleLampParam.getId();
                //前天累计能耗
                Double aDouble = cumulativeEnergyMap.get(id);
                Double energy = 0.0;
                if(aDouble==null){
                    energy = (electricEnergyOne - 0);
                }else {
                    energy = (electricEnergyOne - aDouble);
                }
                lampEnergy.setEnergy(energy.floatValue());

                lampEnergy.setDeviceId(id);
                lampEnergy.setCreateTime(new Date());
                lampEnergy.setCumulativeEnergy(electricEnergyOne);
                //能耗日期
                //lampEnergy.setEnergyTime(yesterday);

                lampEnergyService.save(lampEnergy);
            }

            log.info("获取前一天的能耗数据成功");
            //更新前一天的能耗
            lampEnergyDayService.addEnergy(yesterday);
        }else {
            log.info("获取前一天的能耗数据失败");
        }
    }*/

    /*@Scheduled(cron = "00 10 00 01 * *")
    public void taskEnergyMonth(){
        log.info("定时任务：更新上个月的能耗数据");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        Date time = calendar.getTime();
        SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM");
        String format = dateFormatJustDay.format(time);
        Result result = lampEnergyMonthService.addEnergy(format);
        if(result.getCode()==200){
            log.info("定时任务：更新上个月的能耗数据成功");
        }else {
            log.info("定时任务：更新上个月的能耗数据失败");
        }

    }*/

    @Scheduled(cron = "0 0/58 * * * *")
    public void getToken(){
        log.info("定时任务：token更新");
        SendHttpsUtil.getToken();
    }

    /**
     * 修改所有中科智联集中控制器及其下所有灯具为在线状态
     */
    @Scheduled(cron = "0 0/5 * * * *")
    public void taskUpdateZkzlOnlineStatus() {
        log.info("定时任务：修改所有中科智联集中控制器及其下所有灯具为在线状态");
        //查询所有中科智联集中控制器
        QueryWrapper<LocationControl> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id", 3);
        List<LocationControl> locationControlList = locationControlService.list(queryWrapper);
        for (LocationControl locationControl : locationControlList) {
            String concentratorId = locationControl.getNum();
            Object statusObject = redisUtil.get(concentratorId + "Online");
            Integer onlineStatus;
            if (statusObject == null) {
                onlineStatus = 0;
            } else {
                onlineStatus = Integer.valueOf(statusObject.toString());
            }
            locationControlService.findAllAndUpdateStatus(concentratorId, onlineStatus);
        }
    }

//    /**
//     * 获取所有中科智联集中控制器是否使用定时控制，如不为时间控制则修改为定时控制
//     */
//    @Scheduled(cron = "0 0/1 * * * *")
//    public void taskRestoreTimeTableControl() {
//        log.info("定时任务：获取所有中科智联集中控制器是否使用定时控制，如不为时间控制则修改为定时控制");
//        //查询所有中科智联集中控制器
//        QueryWrapper<LocationControl> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("type_id", 3);
//        List<LocationControl> locationControlList = locationControlService.list(queryWrapper);
//        for (LocationControl locationControl : locationControlList) {
//            String concentratorId = locationControl.getNum();
//            Object timeTableObject = redisUtil.get(concentratorId + "TimeTable");
//            Integer timeTable;
//            if (timeTableObject != null) {
//                timeTable = Integer.valueOf(timeTableObject.toString());
//                if (timeTable == 0) {
//                    //恢复时间控制
//                    ZkzlProtocolUtil.restoreTimeTableControl(concentratorId);
//                    redisUtil.set(concentratorId + "TimeTable", 0);
//                    log.info("中科智联集中控制器 {} 恢复为定时控制", concentratorId);
//                }
//            } else {
//                //恢复时间控制
//                ZkzlProtocolUtil.restoreTimeTableControl(concentratorId);
//                redisUtil.set(concentratorId + "TimeTable", 0);
//                log.info("中科智联集中控制器 {} 恢复为定时控制", concentratorId);
//            }
//        }
//    }

}


@Component
@Order(value = 1)
class Startup implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Startup.class);
    @Override
    public void run(String... strings) throws Exception {
        log.info("启动获取token");
        SendHttpsUtil.getToken();
    }
}