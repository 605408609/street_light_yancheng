package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampDevice;
import com.exc.street.light.resource.entity.sl.LampStrategyAction;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.entity.sl.SystemDevice;
import com.exc.street.light.resource.utils.HexUtil;
import com.exc.street.light.resource.vo.req.SlControlSystemDeviceVO;
import com.exc.street.light.resource.vo.resp.SlRespLampGroupSingleParamVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamRespVO;
import com.exc.street.light.resource.vo.sl.StrategyTempVO;
import com.exc.street.light.sl.config.parameter.CtwingApi;
import com.exc.street.light.sl.config.parameter.LoraApi;
import com.exc.street.light.sl.mapper.LampDeviceDao;
import com.exc.street.light.sl.mapper.SingleLampParamDao;
import com.exc.street.light.sl.service.KafkaMessageService;
import com.exc.street.light.sl.service.LampStrategyActionService;
import com.exc.street.light.sl.service.SingleLampParamService;
import com.exc.street.light.sl.service.SystemDeviceService;
import com.exc.street.light.sl.utils.*;
import org.omg.CORBA.INTERNAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: HuangJinHao
 *
 */
@Service
public class SingleLampParamServiceImpl extends ServiceImpl<SingleLampParamDao,SingleLampParam> implements SingleLampParamService {

    private static final Logger logger = LoggerFactory.getLogger(SingleLampParamServiceImpl.class);

    @Autowired
    private SingleLampParamDao singleLampParamDao;
    @Autowired
    KafkaMessageService kafkaMessageService;
    @Autowired
    LampStrategyActionService lampStrategyActionService;
    @Autowired
    private LoraApi loraApi;
    @Autowired
    private CtwingApi ctwingApi;
    @Autowired
    private SystemDeviceService systemDeviceService;

    @Override
    public Result ota(String ADR,String sendMode,String sendId) {
        String message = "";
        if("nb".equals(sendMode)){
            message = MessageGeneration.nbSetParam(ADR, 1, WithParamMatch.OTA);
        }else if("cat1".equals(sendMode)){
            message = CatOneMessageGeneration.catOneSetParam(ADR, 1, WithParamMatch.OTA);
        }else if("lora_old".equals(sendMode)){
            message = LoraOldMessageGeneration.loraOldSetParam(1, WithParamMatch.OTA);
        }else if("lora_new".equals(sendMode)){
            message = LoraNewMessageGeneration.loraNewSetParam(1, WithParamMatch.OTA);
        }else if("dxnb".equals(sendMode)){
            message = DxnbMessageGeneration.dxnbSetParam(1, WithParamMatch.OTA);
        }
        boolean flag = MessageOperationUtil.sendByMode(message, sendMode, sendId);
        if(flag){
            return new Result().success("升级准备完成");
        }else {
            return new Result().error("升级准备失败");
        }

    }

    @Override
    public SingleLampParam getlastTimeByDeviceId(Integer deviceId) {
        SingleLampParam singleLampParam = baseMapper.getlastTimeByDeviceId(deviceId);
        return singleLampParam;
    }

    @Override
    public Result<List<SingleLampParamRespVO>> getList(String deviceName, HttpServletRequest request) {
        List<SingleLampParamRespVO> list = baseMapper.getList(deviceName);
        Result<List<SingleLampParamRespVO>> result = new Result();
        return result.success(list);
    }

    @Override
    public Result singleLampControl(List<SlControlSystemDeviceVO> slControlSystemDeviceVOList) {
        logger.info("路灯单灯控制接收参数:{}", slControlSystemDeviceVOList);
        boolean result = false;
        if(slControlSystemDeviceVOList != null&&slControlSystemDeviceVOList.size()>0){
            if(slControlSystemDeviceVOList.size()==1){
                SlControlSystemDeviceVO slControlSystemDeviceVO = slControlSystemDeviceVOList.get(0);
                String ADR = slControlSystemDeviceVO.getNum();
                Integer deviceTypeId = slControlSystemDeviceVO.getDeviceTypeId();
                Integer stateOne = slControlSystemDeviceVO.getDeviceState();
                Integer brightnessOne = slControlSystemDeviceVO.getBrightness();
                String loopNumString = slControlSystemDeviceVO.getLoopNum();
                if(loopNumString==null){
                    loopNumString = "1";
                }
                Integer loopNum = Integer.parseInt(loopNumString);
                String sendId = slControlSystemDeviceVO.getReserveOne();
                /*String stateTwo = singleLampParam.getStateTwo();
                Integer brightnessTwo = singleLampParam.getBrightnessTwo();*/
                if(stateOne == null || brightnessOne == null){
                    logger.info("发送消息参数错误");
                    return new Result().error("消息发送失败");
                }
                //控制路灯
                if(brightnessOne>100||brightnessOne<0){
                    logger.info("超出亮度范围");
                    return new Result().error("超出亮度范围");
                }
                String message = "";
                String sendMode = "";
                if(deviceTypeId==1||deviceTypeId==2){
                    sendMode = "nb";
                    message = MessageGeneration.nbSingleLampControl(ADR, brightnessOne, HexUtil.intToHexStringOne(stateOne),loopNum);
                }else if(deviceTypeId==5||deviceTypeId==6){
                    sendMode = "cat1";
                    message = CatOneMessageGeneration.catOneSingleLampControl(ADR, brightnessOne, HexUtil.intToHexStringOne(stateOne),loopNum);
                }else if(deviceTypeId==3||deviceTypeId==4){
                    sendMode = "lora_old";
                    message = LoraOldMessageGeneration.loraOldSingleLampControl(brightnessOne, HexUtil.intToHexStringOne(stateOne),loopNum);
                }else if(deviceTypeId==9||deviceTypeId==10){
                    sendMode = "lora_new";
                    message = LoraNewMessageGeneration.loraNewSingleLampControl(brightnessOne, HexUtil.intToHexStringOne(stateOne),loopNum);
                }else if(deviceTypeId==7||deviceTypeId==8){
                    sendMode = "dxnb";
                    message = DxnbMessageGeneration.dxnbSingleLampControl(brightnessOne, HexUtil.intToHexStringOne(stateOne),loopNum);
                }else if(deviceTypeId==14||deviceTypeId==15){
                    sendMode = "dxCat1";
                    message = DxCatOneMessageGeneration.dxCatOneSingleLampControl(brightnessOne, HexUtil.intToHexStringOne(stateOne),loopNum);
                }
                System.out.println(message);
                if(message!=null&&message.length()>0){
                    result = MessageOperationUtil.sendByMode(message, sendMode,sendId);
                }
            }else if(slControlSystemDeviceVOList.size()==2){
                Integer loopNumOne = Integer.parseInt(slControlSystemDeviceVOList.get(0).getLoopNum());
                SlControlSystemDeviceVO slControlSystemDeviceVOOne = new SlControlSystemDeviceVO();
                SlControlSystemDeviceVO SlControlSystemDeviceVOTwo = new SlControlSystemDeviceVO();
                if(loopNumOne == 1){
                    slControlSystemDeviceVOOne = slControlSystemDeviceVOList.get(0);
                    SlControlSystemDeviceVOTwo = slControlSystemDeviceVOList.get(1);
                }else {
                    slControlSystemDeviceVOOne = slControlSystemDeviceVOList.get(1);
                    SlControlSystemDeviceVOTwo = slControlSystemDeviceVOList.get(0);
                }
                String ADR = slControlSystemDeviceVOOne.getNum();
                Integer deviceTypeId = slControlSystemDeviceVOOne.getDeviceTypeId();
                String sendId = slControlSystemDeviceVOOne.getReserveOne();
                Integer stateOne = slControlSystemDeviceVOOne.getDeviceState();
                Integer brightnessOne = slControlSystemDeviceVOOne.getBrightness();
                Integer stateTwo = SlControlSystemDeviceVOTwo.getDeviceState();
                Integer brightnessTwo = SlControlSystemDeviceVOTwo.getBrightness();
                if(stateOne == null || brightnessOne == null || stateTwo == null || brightnessTwo == null){
                    logger.info("发送消息参数错误");
                    return new Result().error("消息发送失败");
                }
                //控制路灯
                if(brightnessOne>100||brightnessOne<0||brightnessTwo>100||brightnessTwo<0){
                    logger.info("超出亮度范围");
                    return new Result().error("超出亮度范围");
                }
                String message = "";
                String sendMode = "";
                if(deviceTypeId==1||deviceTypeId==2){
                    sendMode = "nb";
                    message = MessageGeneration.nbSingleLampControl(ADR, brightnessOne, HexUtil.intToHexStringOne(stateOne),brightnessTwo,HexUtil.intToHexStringOne(stateTwo));
                }else if(deviceTypeId==5||deviceTypeId==6){
                    sendMode = "cat1";
                    message = CatOneMessageGeneration.catOneSingleLampControl(ADR, brightnessOne, HexUtil.intToHexStringOne(stateOne),brightnessTwo,HexUtil.intToHexStringOne(stateTwo));
                }else if(deviceTypeId==3||deviceTypeId==4){
                    sendMode = "lora_old";
                    message = LoraOldMessageGeneration.loraOldSingleLampControl(brightnessOne, HexUtil.intToHexStringOne(stateOne),brightnessTwo,HexUtil.intToHexStringOne(stateTwo));
                }else if(deviceTypeId==9||deviceTypeId==10){
                    sendMode = "lora_new";
                    message = LoraNewMessageGeneration.loraNewSingleLampControl(brightnessOne, HexUtil.intToHexStringOne(stateOne),brightnessTwo,HexUtil.intToHexStringOne(stateTwo));
                }else if(deviceTypeId==7||deviceTypeId==8){
                    sendMode = "dxnb";
                    message = DxnbMessageGeneration.dxnbSingleLampControl(brightnessOne, HexUtil.intToHexStringOne(stateOne),brightnessTwo,HexUtil.intToHexStringOne(stateTwo));
                }else if(deviceTypeId==14||deviceTypeId==15){
                    sendMode = "dxCat1";
                    message = DxCatOneMessageGeneration.dxCatOneSingleLampControl(brightnessOne, HexUtil.intToHexStringOne(stateOne),brightnessTwo,HexUtil.intToHexStringOne(stateTwo));
                }
                System.out.println(message);
                if(message!=null&&message.length()>0){
                    result = MessageOperationUtil.sendByMode(message,sendMode,sendId);
                }
            }
        }


        if(result){
            return new Result().success("发送成功");
        }else {
            return new Result().error("消息发送失败");
        }

    }

    @Override
    public Result controlByShuncom(SlControlSystemDeviceVO slControlSystemDeviceVO) {
        logger.info("路灯单灯控制接收参数:{}", slControlSystemDeviceVO);
        Result result = new Result();
        String num = slControlSystemDeviceVO.getNum();
        Integer brightState = slControlSystemDeviceVO.getDeviceState();
        Integer brightness = slControlSystemDeviceVO.getBrightness();
        String state = "00";
        if(brightState==0){
            state = "01";
        }
        String stateMessage = MessageGeneration.control("00000001","05",state,brightness);
        String brightnessMessage = MessageGeneration.control("00000001","04","00",brightness);
        SendHttpsUtil.issue(stateMessage, num);
        SendHttpsUtil.issue(brightnessMessage, num);
        /*IssueResponse issue = SendHttpsUtil.issue(message, num);
        String status = issue.getStatus();
        if("FAILED".equals(status)){
            result.error("命令下发失败");
        }
        if("TIMEOUT".equals(status)){
            result.error("命令下发已超时");
        }*/
        return result.success("命令已下发",slControlSystemDeviceVO);
    }

    @Override
    public Result deleteByDeviceId(List<Integer> deviceIdList) {
        QueryWrapper<SingleLampParam> queryWrapper = new QueryWrapper();
        queryWrapper.in("device_id",deviceIdList);
        baseMapper.delete(queryWrapper);
        return new Result().success("删除成功");
    }

    /*@Override
    public boolean setStrategy(List<LampStrategyAction> lampStrategyActionList) {
        List<LampDevice> lampDeviceList = lampDeviceDao.selectList(null);
        for (LampDevice lampDevice : lampDeviceList) {
            if(("nb".equals(lampDevice.getModel())||"cat1".equals(lampDevice.getModel())||"dxnb".equals(lampDevice.getModel()))&&"EXC1".equals(lampDevice.getFactory())){
                String ADR = lampDevice.getNum();
                String model = lampDevice.getModel();
                String sendId = lampDevice.getSendId();
                List<String> messageList = new ArrayList<>();
                if("nb".equals(model)){
                    messageList = MessageGeneration.nbDailySchedule(ADR,lampStrategyActionList);
                }else if("cat1".equals(model)){
                    messageList = CatOneMessageGeneration.catOneDailySchedule(ADR,lampStrategyActionList);
                }else if("dxnb".equals(model)){
                    messageList = DxnbMessageGeneration.dxnbDailySchedule(lampStrategyActionList);
                }
                if(!messageList.isEmpty()){
                    for (int i = 0;i<messageList.size();i++){
                        String message = messageList.get(i);
                        //发送消息
                        Long dateTime = System.currentTimeMillis() + 1000 + i * 1000;
                        Date date = new Date();
                        date.setTime(dateTime);
                        SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String format = dateFormatJustDay.format(date);
                        MessageOperationUtil.sendTimingMessage(format,message,model,sendId);
                        //MessageOperationUtil.sendByMode(message,sendMode,sendId);
                    }

                }else {
                    logger.info("策略报文生成错误");
                }
            }else {
                continue;
            }

        }
        List<String> messageList = LoraOldMessageGeneration.loraOldDailySchedule(lampStrategyActionList);
        if(!messageList.isEmpty()){
            for (int i = 0;i<messageList.size();i++){
                String message = messageList.get(i);
                //发送消息
                Long dateTime = System.currentTimeMillis() + 1000 + i * 1000;
                Date date = new Date();
                date.setTime(dateTime);
                SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = dateFormatJustDay.format(date);
                MessageOperationUtil.sendTimingMessage(format,message,"loraMc",loraApi.getLoraMcId());
                //MessageOperationUtil.sendByMode(message,sendMode,sendId);
            }

        }else {
            logger.info("策略报文生成错误");
        }
        return true;
    }*/


    @Override
    public boolean setStrategy(List<List<SlControlSystemDeviceVO>> deviceGroupingByFlag, List<LampStrategyAction> lampStrategyActionList,Integer scene) {
        List<StrategyTempVO> strategyTempVOList = new ArrayList<>();
        Integer strategyId = -1;
        if(lampStrategyActionList!=null&&lampStrategyActionList.size()>0){
            strategyId = lampStrategyActionList.get(0).getStrategyId();
        }else {
            return false;
        }
        /*Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,Integer.parseInt(ctwingApi.getRetransmissionTime()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");*/
        for (List<SlControlSystemDeviceVO> slControlSystemDeviceVOS : deviceGroupingByFlag) {
            //calendar.add(Calendar.MILLISECOND,Integer.parseInt(ctwingApi.getIntervalTime()));
            Integer deviceTypeId = slControlSystemDeviceVOS.get(0).getDeviceTypeId();
            String ADR = slControlSystemDeviceVOS.get(0).getNum();
            String sendId = slControlSystemDeviceVOS.get(0).getReserveOne();
            SystemDevice systemDevice = systemDeviceService.getById(slControlSystemDeviceVOS.get(0).getId());
            Integer isOnline = systemDevice.getIsOnline();
            if(deviceTypeId==11){
                continue;
            }
            List<Integer> loopNums = new ArrayList<>();
            //List<Integer> deviceIds = new ArrayList<>();
            List<Integer> strategyHistoryIdList = new ArrayList<>();
            for (SlControlSystemDeviceVO slControlSystemDeviceVO : slControlSystemDeviceVOS) {
                Integer loopNum = Integer.parseInt(slControlSystemDeviceVO.getLoopNum());
                //Integer id = slControlSystemDeviceVO.getId();
                loopNums.add(loopNum);
                //deviceIds.add(id);
                strategyHistoryIdList.add(slControlSystemDeviceVO.getStrategyHistoryId());
            }
            List<String> messageList = new ArrayList<>();
            if(deviceTypeId==1||deviceTypeId==2){
                messageList = MessageGeneration.nbDailySchedule(loopNums,ADR,lampStrategyActionList,scene,strategyHistoryIdList);
            }else if(deviceTypeId==5||deviceTypeId==6){
                messageList = CatOneMessageGeneration.catOneDailySchedule(loopNums,ADR,lampStrategyActionList,scene,strategyHistoryIdList);
            }else if(deviceTypeId==3||deviceTypeId==4){
                messageList = LoraOldMessageGeneration.loraOldDailySchedule(loopNums,lampStrategyActionList,scene,strategyHistoryIdList);
            }else if(deviceTypeId==9||deviceTypeId==10){
                messageList = LoraNewMessageGeneration.loraNewDailySchedule(loopNums,lampStrategyActionList,scene,strategyHistoryIdList);
            }else if(deviceTypeId==7||deviceTypeId==8){
                messageList = DxnbMessageGeneration.dxnbDailySchedule(loopNums,lampStrategyActionList,scene,strategyHistoryIdList);
            }else if(deviceTypeId==14||deviceTypeId==15){
                messageList = DxCatOneMessageGeneration.dxCatOneDailySchedule(loopNums,lampStrategyActionList,scene,strategyHistoryIdList);
            }
            if(!messageList.isEmpty()){
                System.out.println(messageList);
                for (String message : messageList) {
                    if(isOnline==0){
                        continue;
                    }
                    StrategyTempVO strategyTempVO = new StrategyTempVO();
                    strategyTempVO.setMessage(message);
                    strategyTempVO.setSendId(sendId);
                    strategyTempVO.setDeviceTypeId(deviceTypeId);
                    strategyTempVOList.add(strategyTempVO);
                    //发送消息
                    //MessageOperationUtil.sendByMode(message,deviceTypeId,sendId);
                }
                //下发策略重发的定时任务
                //MessageOperationUtil.sendTimingStrategyMessageAgain(simpleDateFormat.format(calendar.getTime()),messageList,deviceTypeId,sendId,deviceIds,strategyId);
            }else {
                logger.info("策略报文生成错误");
                return false;
            }
        }

        List<List<StrategyTempVO>> listList = new ArrayList<>();
        if (strategyTempVOList != null && strategyTempVOList.size() > 0) {
            int n = 0;
            while (true) {
                List<StrategyTempVO> collect = strategyTempVOList.stream().skip(n * 12).limit(12).collect(Collectors.toList());
                n += 1;
                if(collect!=null&&collect.size()>0){
                    listList.add(collect);
                }
                if(collect.size()<12){
                    break;
                }
            }
        }
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        for (int i = 0; i <listList.size(); i++) {
            List<StrategyTempVO> strategyTempVOS = listList.get(i);
            c.add(Calendar.MILLISECOND,Integer.parseInt(ctwingApi.getIntervalTime()));
            MessageOperationUtil.sendTimingStrategyMessage(dateFormat.format(c.getTime()),strategyTempVOS);
        }
        return true;
    }

    @Override
    public void updateOne(SingleLampParam singleLampParam) {
        singleLampParamDao.updateById(singleLampParam);
    }

    @Override
    public SingleLampParam getSingleLampById(Integer id) {
        SingleLampParam singleLampParam = singleLampParamDao.selectById(id);
        return singleLampParam;
    }

    @Override
    public SingleLampParam getSingleLampOne(String deviceNum,Integer loopNum,Integer topicNum) {
        /*QueryWrapper<SingleLampParam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_num",deviceNum);
        queryWrapper.eq("routes_num",routesNum);
        SingleLampParam singleLampParam = singleLampParamDao.selectOne(queryWrapper);*/
        if(topicNum == 4){
            return baseMapper.getSingleLampOneNewLora(deviceNum,loopNum);
        }
        return baseMapper.getSingleLampOne(deviceNum,loopNum,topicNum);
    }

    @Override
    public List<SingleLampParam> getSingleLampByDeviceId(Integer deviceId,String name) {
        QueryWrapper<SingleLampParam> queryWrapper = new QueryWrapper<>();
        if(deviceId!=null){
            queryWrapper.eq("device_id", deviceId);
        }
        if(name!=null){
            queryWrapper.eq("name",name);
        }
        List<SingleLampParam> singleLampParamList = singleLampParamDao.selectList(queryWrapper);
        return singleLampParamList;
    }

    @Override
    public List<SingleLampParam> getSingleLampLikeName(Integer deviceId,String name) {
        QueryWrapper<SingleLampParam> queryWrapper = new QueryWrapper<>();
        if(deviceId!=null){
            queryWrapper.eq("device_id", deviceId);
        }
        if(name!=null){
            queryWrapper.like("name",name);
        }
        List<SingleLampParam> singleLampParamList = singleLampParamDao.selectList(queryWrapper);
        return singleLampParamList;
    }

    @Override
    public List<SingleLampParam> getSingleLampByDeviceIds(List<Integer> deviceIds) {
        QueryWrapper<SingleLampParam> queryWrapper = new QueryWrapper<>();
        if(deviceIds==null||deviceIds.size()==0){
            deviceIds.add(-1);
        }
        queryWrapper.in("device_id", deviceIds);
        List<SingleLampParam> singleLampParamList = singleLampParamDao.selectList(queryWrapper);
        return singleLampParamList;
    }

    @Override
    public Integer add(SingleLampParam singleLampParam) {
        return singleLampParamDao.insert(singleLampParam);
    }

    @Override
    public Result getList() {
        List<SingleLampParam> singleLampParamList = baseMapper.selectList(null);
        return new Result().success(singleLampParamList);
    }


    @Override
    public List<SingleLampParam> getListByIdList(List<Integer> SingleLampParamIdList) {
        LambdaQueryWrapper<SingleLampParam> wrapper = new LambdaQueryWrapper();
        if (!SingleLampParamIdList.isEmpty()) {
            wrapper.in(SingleLampParam::getId, SingleLampParamIdList);
        }
        List<SingleLampParam> list = this.list(wrapper);
        return list;
    }

    @Override
    public Boolean longitudeAndLatitudeSetting(String longitude, String latitude,SystemDevice systemDevice){
        String num = systemDevice.getNum();
        String sendId = systemDevice.getReserveOne();
        Integer deviceTypeId = systemDevice.getDeviceTypeId();
        String message = "";
        if(deviceTypeId==1||deviceTypeId==2){
            message = MessageGeneration.nbLongitudeAndLatitude(num, longitude, latitude);
        }else if (deviceTypeId==5||deviceTypeId==6){
            message = CatOneMessageGeneration.catOneLongitudeAndLatitude(num, longitude, latitude);
        }else if(deviceTypeId==3||deviceTypeId==4){
            message = LoraOldMessageGeneration.loraOldLongitudeAndLatitude(longitude, latitude);
        }else if(deviceTypeId==9||deviceTypeId==10){
            message = LoraNewMessageGeneration.loraNewLongitudeAndLatitude(longitude, latitude);
        }else if(deviceTypeId==7||deviceTypeId==8){
            message = DxnbMessageGeneration.dxnbLongitudeAndLatitude(longitude, latitude);
        }else if (deviceTypeId==14||deviceTypeId==15){
            message = DxCatOneMessageGeneration.dxCatOneLongitudeAndLatitude(longitude, latitude);
        }
        //发送消息
        return MessageOperationUtil.sendByMode(message,deviceTypeId,sendId);
    }

    @Override
    public Boolean ipSetting(String ip,SystemDevice systemDevice){
        String num = systemDevice.getNum();
        String sendId = systemDevice.getReserveOne();
        Integer deviceTypeId = systemDevice.getDeviceTypeId();
        String message = "";
        if(deviceTypeId==1||deviceTypeId==2){
            message = MessageGeneration.nbIpMessage(num, ip);
        }else if (deviceTypeId==5||deviceTypeId==6){
            message = CatOneMessageGeneration.catOneIpMessage(num, ip);
        }else if(deviceTypeId==3||deviceTypeId==4){
            message = LoraOldMessageGeneration.loraOldIpMessage(ip);
        }else if(deviceTypeId==9||deviceTypeId==10){
            message = LoraNewMessageGeneration.loraNewIpMessage(ip);
        }else if(deviceTypeId==7||deviceTypeId==8){
            message = DxnbMessageGeneration.dxnbIpMessage(ip);
        }else if (deviceTypeId==14||deviceTypeId==15){
            message = DxCatOneMessageGeneration.dxCatOneIpMessage(ip);
        }
        //发送消息
        return MessageOperationUtil.sendByMode(message, deviceTypeId, sendId);
    }

    @Override
    public Boolean alarmVoltage(int voltage,SystemDevice systemDevice){
        String ADR = systemDevice.getNum();
        String sendId = systemDevice.getReserveOne();
        Integer deviceTypeId = systemDevice.getDeviceTypeId();
        String message = "";
        if(deviceTypeId==1||deviceTypeId==2){
            message = MessageGeneration.nbSetParam(ADR,voltage, WithParamMatch.ALARMVOLTAGE);
        }else if (deviceTypeId==5||deviceTypeId==6){
            message = CatOneMessageGeneration.catOneSetParam(ADR,voltage, WithParamMatch.ALARMVOLTAGE);
        }else if(deviceTypeId==3||deviceTypeId==4){
            message = LoraOldMessageGeneration.loraOldSetParam(voltage, WithParamMatch.ALARMVOLTAGE);
        }else if(deviceTypeId==9||deviceTypeId==10){
            message = LoraNewMessageGeneration.loraNewSetParam(voltage, WithParamMatch.ALARMVOLTAGE);
        }else if(deviceTypeId==7||deviceTypeId==8){
            message = DxnbMessageGeneration.dxnbSetParam(voltage, WithParamMatch.ALARMVOLTAGE);
        }else if (deviceTypeId==14||deviceTypeId==15){
            message = DxCatOneMessageGeneration.dxCatOneSetParam(voltage, WithParamMatch.ALARMVOLTAGE);
        }
        return MessageOperationUtil.sendByMode(message,deviceTypeId,sendId);
    }

    @Override
    public Boolean alarmCurrent(int current,SystemDevice systemDevice){
        String ADR = systemDevice.getNum();
        String sendId = systemDevice.getReserveOne();
        Integer deviceTypeId = systemDevice.getDeviceTypeId();
        String message = "";
        if(deviceTypeId==1||deviceTypeId==2){
            message = MessageGeneration.nbSetParam(ADR,current, WithParamMatch.ALARMCURRENT);
        }else if (deviceTypeId==5||deviceTypeId==6){
            message = CatOneMessageGeneration.catOneSetParam(ADR,current, WithParamMatch.ALARMCURRENT);
        }else if(deviceTypeId==3||deviceTypeId==4){
            message = LoraOldMessageGeneration.loraOldSetParam(current, WithParamMatch.ALARMCURRENT);
        }else if(deviceTypeId==9||deviceTypeId==10){
            message = LoraNewMessageGeneration.loraNewSetParam(current, WithParamMatch.ALARMCURRENT);
        }else if(deviceTypeId==7||deviceTypeId==8){
            message = DxnbMessageGeneration.dxnbSetParam(current, WithParamMatch.ALARMCURRENT);
        }else if (deviceTypeId==14||deviceTypeId==15){
            message = DxCatOneMessageGeneration.dxCatOneSetParam(current, WithParamMatch.ALARMCURRENT);
        }
        return MessageOperationUtil.sendByMode(message,deviceTypeId,sendId);
    }

    @Override
    public Boolean alarmTemperature(int temperature,SystemDevice systemDevice){
        String ADR = systemDevice.getNum();
        String sendId = systemDevice.getReserveOne();
        Integer deviceTypeId = systemDevice.getDeviceTypeId();
        String message = "";
        if(deviceTypeId==1||deviceTypeId==2){
            message = MessageGeneration.nbSetParam(ADR,temperature, WithParamMatch.ALARMTEMPERATURE);
        }else if (deviceTypeId==5||deviceTypeId==6){
            message = CatOneMessageGeneration.catOneSetParam(ADR,temperature, WithParamMatch.ALARMTEMPERATURE);
        }else if(deviceTypeId==3||deviceTypeId==4){
            message = LoraOldMessageGeneration.loraOldSetParam(temperature, WithParamMatch.ALARMTEMPERATURE);
        }else if(deviceTypeId==9||deviceTypeId==10){
            message = LoraNewMessageGeneration.loraNewSetParam(temperature, WithParamMatch.ALARMTEMPERATURE);
        }else if(deviceTypeId==7||deviceTypeId==8){
            message = DxnbMessageGeneration.dxnbSetParam(temperature, WithParamMatch.ALARMTEMPERATURE);
        }else if (deviceTypeId==14||deviceTypeId==15){
            message = DxCatOneMessageGeneration.dxCatOneSetParam(temperature, WithParamMatch.ALARMTEMPERATURE);
        }
        return MessageOperationUtil.sendByMode(message,deviceTypeId,sendId);
    }

    @Override
    public Boolean interval(int interval,SystemDevice systemDevice){
        Integer deviceTypeId = systemDevice.getDeviceTypeId();
        String ADR = systemDevice.getNum();
        String sendId = systemDevice.getReserveOne();
        String message = "";
        if(deviceTypeId==1||deviceTypeId==2){
            message = MessageGeneration.nbSetParam(ADR,interval, WithParamMatch.INTERVAL);
        }else if (deviceTypeId==5||deviceTypeId==6){
            message = CatOneMessageGeneration.catOneSetParam(ADR,interval, WithParamMatch.INTERVAL);
        }else if(deviceTypeId==3||deviceTypeId==4){
            message = LoraOldMessageGeneration.loraOldSetParam(interval, WithParamMatch.INTERVAL);
        }else if(deviceTypeId==9||deviceTypeId==10){
            message = LoraNewMessageGeneration.loraNewSetParam(interval, WithParamMatch.INTERVAL);
        }else if(deviceTypeId==7||deviceTypeId==8){
            message = DxnbMessageGeneration.dxnbSetParam(interval, WithParamMatch.INTERVAL);
        }else if (deviceTypeId==14||deviceTypeId==15){
            message = DxCatOneMessageGeneration.dxCatOneSetParam(interval, WithParamMatch.INTERVAL);
        }
        return MessageOperationUtil.sendByMode(message,deviceTypeId,sendId);
    }

    @Override
    public Boolean leakageCurrent(int leakageCurrent, SystemDevice systemDevice) {
        String ADR = systemDevice.getNum();
        String sendId = systemDevice.getReserveOne();
        Integer deviceTypeId = systemDevice.getDeviceTypeId();
        String message = "";
        if(deviceTypeId==1||deviceTypeId==2){
            message = MessageGeneration.nbSetParam(ADR,leakageCurrent, WithParamMatch.LEAKAGECURRENT);
        }else if (deviceTypeId==5||deviceTypeId==6){
            message = CatOneMessageGeneration.catOneSetParam(ADR,leakageCurrent, WithParamMatch.LEAKAGECURRENT);
        }else if(deviceTypeId==3||deviceTypeId==4){
            message = LoraOldMessageGeneration.loraOldSetParam(leakageCurrent, WithParamMatch.LEAKAGECURRENT);
        }else if(deviceTypeId==9||deviceTypeId==10){
            message = LoraNewMessageGeneration.loraNewSetParam(leakageCurrent, WithParamMatch.LEAKAGECURRENT);
        }else if(deviceTypeId==7||deviceTypeId==8){
            message = DxnbMessageGeneration.dxnbSetParam(leakageCurrent, WithParamMatch.LEAKAGECURRENT);
        }else if (deviceTypeId==14||deviceTypeId==15){
            message = DxCatOneMessageGeneration.dxCatOneSetParam(leakageCurrent, WithParamMatch.LEAKAGECURRENT);
        }
        return MessageOperationUtil.sendByMode(message,deviceTypeId,sendId);
    }

    @Override
    public Boolean tiltAngle(int tiltAngle, SystemDevice systemDevice) {
        String ADR = systemDevice.getNum();
        String sendId = systemDevice.getReserveOne();
        Integer deviceTypeId = systemDevice.getDeviceTypeId();
        String message = "";
        if(deviceTypeId==1||deviceTypeId==2){
            message = MessageGeneration.nbTiltAngle(ADR,tiltAngle);
        }else if (deviceTypeId==5||deviceTypeId==6){
            message = CatOneMessageGeneration.catOneTiltAngle(ADR,tiltAngle);
        }else if(deviceTypeId==3||deviceTypeId==4){
            message = LoraOldMessageGeneration.loraOldTiltAngle(tiltAngle);
        }else if(deviceTypeId==9||deviceTypeId==10){
            message = LoraNewMessageGeneration.loraNewTiltAngle(tiltAngle);
        }else if(deviceTypeId==7||deviceTypeId==8){
            message = DxnbMessageGeneration.dxnbTiltAngle(tiltAngle);
        }else if (deviceTypeId==14||deviceTypeId==15){
            message = DxCatOneMessageGeneration.dxCatOneTiltAngle(tiltAngle);
        }
        return MessageOperationUtil.sendByMode(message,deviceTypeId,sendId);
    }

    @Override
    public Boolean parameterClear(List<SystemDevice> systemDeviceList){
        for (SystemDevice systemDevice : systemDeviceList) {
            String ADR = systemDevice.getNum();
            String sendId = systemDevice.getReserveOne();
            Integer deviceTypeId = systemDevice.getDeviceTypeId();
            String message = "";
            if(deviceTypeId==1||deviceTypeId==2){
                message = MessageGeneration.nbNoParam(ADR, NoParamMatch.PARAMETERCLEAR);
            }else if (deviceTypeId==5||deviceTypeId==6){
                message = CatOneMessageGeneration.catOneNoParam(ADR, NoParamMatch.PARAMETERCLEAR);
            }else if(deviceTypeId==3||deviceTypeId==4){
                message = LoraOldMessageGeneration.loraOldNoParam(NoParamMatch.PARAMETERCLEAR);
            }else if(deviceTypeId==9||deviceTypeId==10){
                message = LoraNewMessageGeneration.loraNewNoParam(NoParamMatch.PARAMETERCLEAR);
            }else if(deviceTypeId==7||deviceTypeId==8){
                message = DxnbMessageGeneration.dxnbNoParam(NoParamMatch.PARAMETERCLEAR);
            }else if (deviceTypeId==14||deviceTypeId==15){
                message = DxCatOneMessageGeneration.dxCatOneNoParam(NoParamMatch.PARAMETERCLEAR);
            }
            MessageOperationUtil.sendByMode(message,deviceTypeId,sendId);
        }
        return true;
    }

    @Override
    public Result setParam(String paramName,String paramValue,SystemDevice systemDevice){
        try {
            switch (paramName){
                case "reportingInterval":
                    int interval = Integer.parseInt(paramValue);
                    interval(interval,systemDevice);
                    break;
                case "alarmVoltage":
                    int alarmVoltage = Integer.parseInt(paramValue);
                    alarmVoltage(alarmVoltage,systemDevice);
                    break;
                case "alarmCurrent":
                    int alarmCurrent = Integer.parseInt(paramValue);
                    alarmCurrent(alarmCurrent,systemDevice);
                    break;
                case "alarmTemperature":
                    int alarmTemperature = Integer.parseInt(paramValue)*10;
                    alarmTemperature(alarmTemperature,systemDevice);
                    break;
                case "leakageCurrent":
                    int leakageCurrent = Integer.parseInt(paramValue);
                    leakageCurrent(leakageCurrent,systemDevice);
                    break;
                case "tiltAngle":
                    int tiltAngle = Integer.parseInt(paramValue);
                    tiltAngle(tiltAngle,systemDevice);
                    break;
                case "ip":
                    ipSetting(paramValue,systemDevice);
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            return new Result().error("请按规则填写参数");
        }
        return new Result().success("添加成功");
    }

    @Override
    public List<SingleLampParam> listByAreaId(Integer areaId) {
        logger.info("根据分区id查询所有灯具列表");
        List<SingleLampParam> singleLampParamList = this.baseMapper.listByAreaId(areaId);
        return singleLampParamList;
    }

    @Override
    public List<SlRespLampGroupSingleParamVO> listByLampGroupIdAndSingleIdList(Integer lampGroup, List<Integer> singleLampIdList) {
        logger.info("根据灯具分组id和灯具id集合查询灯具列表");
        return this.baseMapper.listByLampGroupIdAndSingleIdList(lampGroup,singleLampIdList);
    }

    @Override
    public boolean saveBatch(Collection<SingleLampParam> entityList) {
        return false;
    }
}
