/**
 * @filename:DeviceUpgradeLogStatusServiceImpl 2020-08-25
 * @project sl  V1.0
 * Copyright(c) 2018 Huang Jin Hao Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.sl.*;
import com.exc.street.light.resource.utils.HexUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.SlControlSystemDeviceVO;
import com.exc.street.light.sl.VO.SingleLamp;
import com.exc.street.light.sl.config.parameter.PathApi;
import com.exc.street.light.sl.mapper.DeviceUpgradeLogStatusMapper;
import com.exc.street.light.sl.service.*;
import com.exc.street.light.sl.utils.*;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO(设备升级状态表服务实现)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 *
 */
@Service
public class DeviceUpgradeLogStatusServiceImpl  extends ServiceImpl<DeviceUpgradeLogStatusMapper, DeviceUpgradeLogStatus> implements DeviceUpgradeLogStatusService  {

    private static final Logger logger = LoggerFactory.getLogger(DeviceUpgradeLogStatusServiceImpl.class);

    @Autowired
    private RedisUtil redisUtil;

    /*@Autowired
    private LampDeviceService lampDeviceService;*/

    @Autowired
    private PathApi pathApi;

    @Autowired
    private SingleLampParamService singleLampParamService;

    @Autowired
    private DeviceUpgradeLogService deviceUpgradeLogService;

    @Autowired
    private SystemDeviceService systemDeviceService;


    @Override
    public Result upgradeStatusAgain(Integer logIdOld, HttpServletRequest request) {
        logger.info("重新升级，升级记录id：{}",logIdOld);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Result result = new Result();
        //拆分id字符串
        List<Integer> deviceIdList = new ArrayList<>();

        DeviceUpgradeLog deviceUpgradeLogOld = deviceUpgradeLogService.getById(logIdOld);
        List<DeviceUpgradeLogStatus> listByLogId = getListByLogId(deviceUpgradeLogOld.getId());
        for (DeviceUpgradeLogStatus deviceUpgradeLogStatus : listByLogId) {
            if(deviceUpgradeLogStatus.getIsSuccess()==2){
                Integer deviceId = deviceUpgradeLogStatus.getDeviceId();
                deviceIdList.add(deviceId);
            }
        }
        //获取设备信息
        if(deviceIdList==null || deviceIdList.size()==0){
            return new Result().error("升级设备为空");
        }
        /*List<SingleLampParam> singleLampParamList = singleLampParamService.getListByIdList(deviceIdList);
        List<Integer> lampDeviceIdList = singleLampParamList.stream().map(SingleLampParam::getDeviceId).distinct().collect(Collectors.toList());
        Result<List<LampDevice>> lampDeviceListResult = lampDeviceService.getListByIdList(lampDeviceIdList);
        if(lampDeviceListResult.getCode()==200&&lampDeviceListResult.getData()!=null){
            lampDeviceList = lampDeviceListResult.getData();
        }*/

        /*List<SystemDevice> systemDeviceList = new ArrayList<>();
        Result deviceListByIdList = systemDeviceService.getDeviceListByIdList(deviceIdList, request);
        if(deviceListByIdList.getCode()==200&&deviceListByIdList.getData()!=null){
            systemDeviceList = (List<SystemDevice>) deviceListByIdList.getData();
        }*/
        //获取设备信息
        List<SystemDevice> systemDeviceTempList = new ArrayList<>();
        Result deviceListByIdList = systemDeviceService.getDeviceListByIdList(deviceIdList, request);
        if(deviceListByIdList.getCode()==200&&deviceListByIdList.getData()!=null){
            systemDeviceTempList = (List<SystemDevice>) deviceListByIdList.getData();
        }else {
            return result.error("不存在升级设备");
        }
        List<SystemDevice> systemDeviceList = new ArrayList<>();
        Map<Pair<String, Integer>, List<SystemDevice>> singleLampParamMap =
                systemDeviceTempList.stream().collect(Collectors.groupingBy(p -> Pair.of(p.getNum(), p.getDeviceTypeId())));
        List<Pair<String, Integer>> deviceGroupingByFlagList = systemDeviceTempList.stream().map(p -> Pair.of(p.getNum(), p.getDeviceTypeId())).distinct().collect(Collectors.toList());
        for (Pair<String, Integer> stringIntegerPair : deviceGroupingByFlagList) {
            String num = stringIntegerPair.getLeft();
            Integer deviceTypeId = stringIntegerPair.getRight();
            Result systemDeviceById = systemDeviceService.getSystemDeviceById(null, null, num, deviceTypeId);
            if(systemDeviceById!=null){
                List<SystemDevice> data = (List<SystemDevice>) systemDeviceById.getData();
                systemDeviceList.addAll(data);
            }
        }
        List<Integer> deviceTypeIdList = systemDeviceList.stream().map(SystemDevice::getDeviceTypeId).distinct().collect(Collectors.toList());
        if(deviceTypeIdList.size()!=1){
            logger.info("id字符串解析设备非同一类型");
            return result.error("所选设备非同一类型");
        }
        if(deviceTypeIdList.get(0)>10&&deviceTypeIdList.get(0)!=14&&deviceTypeIdList.get(0)!=15){
            logger.info("该类型设备不支持升级：" + deviceTypeIdList.get(0));
            return result.error("该类型设备不支持升级：" + deviceTypeIdList.get(0));
        }
        // 获取文件名
        String fileName = deviceUpgradeLogOld.getFileName();
        logger.info("写入文件，文件名称："+fileName);
        //判断文件名
        String typeFlag = fileName.substring(8, 9);
        String softwareVersion = fileName.substring(fileName.length()-7,fileName.length()-4);
        if("N".equals(typeFlag)){
            if(deviceTypeIdList.get(0)!=1&&deviceTypeIdList.get(0)!=2){
                return new Result().error("设备类型与升级文件不匹配");
            }
        }else if("C".equals(typeFlag)){
            if(deviceTypeIdList.get(0)!=14&&deviceTypeIdList.get(0)!=15){
                return new Result().error("设备类型与升级文件不匹配");
            }
        }else if("L".equals(typeFlag)){
            if(deviceTypeIdList.get(0)!=9&&deviceTypeIdList.get(0)!=10){
                return new Result().error("设备类型与升级文件不匹配");
            }
        }else {
            return new Result().error("设备类型与升级文件不匹配");
        }
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!"bin".equals(suffixName)) {
            logger.info("导入文件" + fileName + "失败,文件类型错误,不是bin文件");
            return new Result().error("导入文件" + fileName + "失败,文件类型错误,不是bin文件");
        }
        String crc16 = Crc16.generateCrc16(String.valueOf(System.currentTimeMillis()));
        String realName = crc16 + "." + suffixName;
        String path = pathApi.getFile();
        String realPath = path + "/" + realName;
        try {
            FileUtil.copyFile(deviceUpgradeLogOld.getPreserveName(),realPath);

            DeviceUpgradeLog deviceUpgradeLog = new DeviceUpgradeLog();
            deviceUpgradeLog.setCreator(userId);
            deviceUpgradeLog.setCreateTime(new Date());
            deviceUpgradeLog.setFileName(fileName);
            deviceUpgradeLog.setPreserveName(realPath);
            deviceUpgradeLog.setEditionNew(softwareVersion);
            deviceUpgradeLogService.save(deviceUpgradeLog);
            Integer logId = deviceUpgradeLog.getId();

            List<DeviceUpgradeLogStatus> deviceUpgradeLogStatusList = new ArrayList<>();
            for (SystemDevice systemDevice : systemDeviceList) {
                DeviceUpgradeLogStatus deviceUpgradeLogStatus = new DeviceUpgradeLogStatus();
                deviceUpgradeLogStatus.setDeviceId(systemDevice.getId());
                deviceUpgradeLogStatus.setLogId(logId);
                deviceUpgradeLogStatus.setIsSuccess(0);
                SingleLampParam singleLampParam = singleLampParamService.getlastTimeByDeviceId(systemDevice.getId());
                if(singleLampParam!=null){
                    String softwareVersionOld = singleLampParam.getSoftwareVersion();
                    if(softwareVersionOld!=null&&softwareVersionOld.length()>0){
                        deviceUpgradeLogStatus.setEditionOld(softwareVersionOld);
                    }
                }

                deviceUpgradeLogStatusList.add(deviceUpgradeLogStatus);
            }
            this.saveBatch(deviceUpgradeLogStatusList);

            /*for (SystemDevice systemDevice : systemDeviceList) {
                String crc16Redis = Crc16.generateCrc16(String.valueOf(System.currentTimeMillis())+systemDevice.getNum());
                String num = systemDevice.getNum();
                Integer deviceTypeId = systemDevice.getDeviceTypeId();
                List<String> messageList = MessageOperationUtil.analysisOtaFile(realPath, num,deviceTypeId);
                if(messageList==null || messageList.size()==0 || "01010101".equals(messageList.get(0))){
                    return new Result().error("读取文件出错");
                }
                redisUtil.set(crc16Redis,messageList);
                redisUtil.expire(crc16Redis,10800);
                String otaReady = "";
                if(deviceTypeId==1||deviceTypeId==2){
                    otaReady = MessageGeneration.nbOtaReady(num,crc16Redis);
                }else if (deviceTypeId==5||deviceTypeId==6){
                    otaReady = CatOneMessageGeneration.catOneOtaReady(num,crc16Redis);
                }else if(deviceTypeId==3||deviceTypeId==4){
                    otaReady = LoraOldMessageGeneration.loraOldOtaReady(crc16Redis);
                }else if(deviceTypeId==9||deviceTypeId==10){
                    otaReady = LoraNewMessageGeneration.loraNewOtaReady(crc16Redis);
                }else if(deviceTypeId==7||deviceTypeId==8){
                    otaReady = DxnbMessageGeneration.dxnbOtaReady(crc16Redis);
                }else if (deviceTypeId==14||deviceTypeId==15){
                    otaReady = DxCatOneMessageGeneration.dxCatOneOtaReady(crc16Redis);
                }
                *//*Result<LampDevice> lampDeviceResult = lampDeviceService.getByNum(num,model,factory);
                LampDevice lampDeviceResultData = lampDeviceResult.getData();
                String sendId = lampDeviceResultData.getSendId();*//*
                String sendId = systemDevice.getReserveOne();
                MessageOperationUtil.sendByMode(otaReady,deviceTypeId,sendId);
            }*/
            String softwareVersionHex = HexUtil.intToHexString(Integer.parseInt(softwareVersion));
            for (Pair<String, Integer> stringIntegerPair : deviceGroupingByFlagList) {
                List<SystemDevice> systemDevices = singleLampParamMap.get(stringIntegerPair);
                String systemDeviceNum = stringIntegerPair.getLeft();
                String prefix = "";
                for (int i = 0; i < 15 - systemDeviceNum.length(); i++) {
                    prefix += "0";
                }
                systemDeviceNum = prefix + systemDeviceNum;
                String crc16Redis = Crc16.generateCrc16(String.valueOf(System.currentTimeMillis()) + systemDeviceNum + logId);
                String num = stringIntegerPair.getLeft();
                Integer deviceTypeId = stringIntegerPair.getRight();
                List<String> messageList = MessageOperationUtil.analysisOtaFile(realPath, num,deviceTypeId);
                if(messageList==null || messageList.size()==0 || "01010101".equals(messageList.get(0))){
                    return new Result().error("读取文件出错");
                }
                redisUtil.set(crc16Redis,messageList);
                redisUtil.expire(crc16Redis,10800);
                redisUtil.set(crc16Redis+"logId",String.valueOf(logId));
                redisUtil.expire(crc16Redis,10800);
                String otaReady = "";
                String otaVersion = "";
                if(deviceTypeId==1||deviceTypeId==2){
                    otaReady = MessageGeneration.nbOtaReady("1007",num,crc16Redis);
                    otaVersion = MessageGeneration.nbOtaReady("2018",num,softwareVersionHex);
                }else if (deviceTypeId==5||deviceTypeId==6){
                    otaReady = CatOneMessageGeneration.catOneOtaReady("1007",num,crc16Redis);
                    otaVersion = CatOneMessageGeneration.catOneOtaReady("2018",num,softwareVersionHex);
                }else if(deviceTypeId==3||deviceTypeId==4){
                    otaReady = LoraOldMessageGeneration.loraOldOtaReady("1007",crc16Redis);
                    otaVersion = LoraOldMessageGeneration.loraOldOtaReady("2018",softwareVersionHex);
                }else if(deviceTypeId==9||deviceTypeId==10){
                    otaReady = LoraNewMessageGeneration.loraNewOtaReady("1007",crc16Redis);
                    otaVersion = LoraNewMessageGeneration.loraNewOtaReady("2018",softwareVersionHex);
                }else if(deviceTypeId==7||deviceTypeId==8){
                    otaReady = DxnbMessageGeneration.dxnbOtaReady("1007",crc16Redis);
                    otaVersion = DxnbMessageGeneration.dxnbOtaReady("2018",softwareVersionHex);
                }else if (deviceTypeId==14||deviceTypeId==15){
                    otaReady = DxCatOneMessageGeneration.dxCatOneOtaReady("1007",crc16Redis);
                    otaVersion = DxCatOneMessageGeneration.dxCatOneOtaReady("2018",softwareVersionHex);
                }
            /*Result<LampDevice> lampDeviceResult = lampDeviceService.getByNum(num,model,factory);
            LampDevice lampDeviceResultData = lampDeviceResult.getData();
            String sendId = lampDeviceResultData.getSendId();*/
                String sendId = systemDevices.get(0).getReserveOne();
                MessageOperationUtil.sendByMode(otaReady,deviceTypeId,sendId);
                MessageOperationUtil.sendByMode(otaVersion,deviceTypeId,sendId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            /*if(!realPath.isEmpty()){
                // 删除源文件
                File file0 = new File(realPath);
                if (file0.exists()) {
                    file0.delete();
                }
            }*/
        }


        return new Result().success("下发成功");
    }

    @Override
    public Result upgradeStatus(MultipartFile multipartFile, String ids, HttpServletRequest request) {
        logger.info("保存ota升级信息，文件名：{}；升级id字符串：{}",multipartFile.getOriginalFilename(),ids);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Result result = new Result();
        //拆分id字符串
        String[] deviceIds = ids.split(",");
        List<Integer> deviceIdList = new ArrayList<>();
        try {
            for (String deviceId : deviceIds) {
                deviceIdList.add(Integer.parseInt(deviceId));
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("id字符串解析出错");
            return result.error("参数传递错误");
        }
        if(deviceIdList==null || deviceIdList.size()==0){
            return result.error("升级设备为空");
        }
        //获取设备信息
        List<SystemDevice> systemDeviceTempList = new ArrayList<>();
        Result deviceListByIdList = systemDeviceService.getDeviceListByIdList(deviceIdList, request);
        if(deviceListByIdList.getCode()==200&&deviceListByIdList.getData()!=null){
            systemDeviceTempList = (List<SystemDevice>) deviceListByIdList.getData();
        }else {
            return result.error("不存在升级设备");
        }
        List<SystemDevice> systemDeviceList = new ArrayList<>();
        Map<Pair<String, Integer>, List<SystemDevice>> singleLampParamMap =
                systemDeviceTempList.stream().collect(Collectors.groupingBy(p -> Pair.of(p.getNum(), p.getDeviceTypeId())));
        List<Pair<String, Integer>> deviceGroupingByFlagList = systemDeviceTempList.stream().map(p -> Pair.of(p.getNum(), p.getDeviceTypeId())).distinct().collect(Collectors.toList());
        for (Pair<String, Integer> stringIntegerPair : deviceGroupingByFlagList) {
            String num = stringIntegerPair.getLeft();
            Integer deviceTypeId = stringIntegerPair.getRight();
            Result systemDeviceById = systemDeviceService.getSystemDeviceById(null, null, num, deviceTypeId);
            if(systemDeviceById!=null){
                List<SystemDevice> data = (List<SystemDevice>) systemDeviceById.getData();
                systemDeviceList.addAll(data);
            }
        }
        List<Integer> deviceTypeIdList = systemDeviceList.stream().map(SystemDevice::getDeviceTypeId).distinct().collect(Collectors.toList());
        if(deviceTypeIdList.size()!=1){
            logger.info("id字符串解析设备非同一类型");
            return result.error("所选设备非同一类型");
        }
        if(deviceTypeIdList.get(0)>10&&deviceTypeIdList.get(0)!=14&&deviceTypeIdList.get(0)!=15){
            logger.info("该类型设备不支持升级：" + deviceTypeIdList.get(0));
            return result.error("该类型设备不支持升级：" + deviceTypeIdList.get(0));
        }
        //上传文件
        if (multipartFile.isEmpty()) {
            return new Result().error("文件为空");
        }
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        logger.info("写入文件，文件名称："+fileName);
        //判断文件名
        String typeFlag = fileName.substring(8, 9);
        String softwareVersion = fileName.substring(fileName.length()-7,fileName.length()-4);
        if("N".equals(typeFlag)){
            if(deviceTypeIdList.get(0)!=1&&deviceTypeIdList.get(0)!=2){
                return new Result().error("设备类型与升级文件不匹配");
            }
        }else if("C".equals(typeFlag)){
            if(deviceTypeIdList.get(0)!=14&&deviceTypeIdList.get(0)!=15){
                return new Result().error("设备类型与升级文件不匹配");
            }
        }else if("L".equals(typeFlag)){
            if(deviceTypeIdList.get(0)!=9&&deviceTypeIdList.get(0)!=10){
                return new Result().error("设备类型与升级文件不匹配");
            }
        }else {
            return new Result().error("设备类型与升级文件不匹配");
        }
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!"bin".equals(suffixName)) {
            logger.info("导入文件" + fileName + "失败,文件类型错误,不是bin文件");
            return new Result().error("导入文件" + fileName + "失败,文件类型错误,不是bin文件");
        }
        String crc16 = Crc16.generateCrc16(String.valueOf(System.currentTimeMillis()));
        String realName = crc16 + "." + suffixName;
        String path = pathApi.getFile();
        String realPath = path + "/" + realName;
        try {
            multipartFile.transferTo(new File(realPath));
            //新增升级记录
            DeviceUpgradeLog deviceUpgradeLog = new DeviceUpgradeLog();
            deviceUpgradeLog.setCreator(userId);
            deviceUpgradeLog.setCreateTime(new Date());
            deviceUpgradeLog.setFileName(fileName);
            deviceUpgradeLog.setPreserveName(realPath);
            deviceUpgradeLog.setEditionNew(softwareVersion);
            deviceUpgradeLogService.save(deviceUpgradeLog);
            Integer logId = deviceUpgradeLog.getId();

            List<DeviceUpgradeLogStatus> deviceUpgradeLogStatusList = new ArrayList<>();
            for (SystemDevice systemDevice : systemDeviceList) {
                DeviceUpgradeLogStatus deviceUpgradeLogStatus = new DeviceUpgradeLogStatus();
                deviceUpgradeLogStatus.setDeviceId(systemDevice.getId());
                deviceUpgradeLogStatus.setLogId(logId);
                deviceUpgradeLogStatus.setIsSuccess(0);
                SingleLampParam singleLampParam = singleLampParamService.getlastTimeByDeviceId(systemDevice.getId());
                if(singleLampParam!=null){
                    String softwareVersionOld = singleLampParam.getSoftwareVersion();
                    if(softwareVersionOld!=null&&softwareVersionOld.length()>0){
                        deviceUpgradeLogStatus.setEditionOld(softwareVersionOld);
                    }
                }
                deviceUpgradeLogStatusList.add(deviceUpgradeLogStatus);


            }
            this.saveBatch(deviceUpgradeLogStatusList);

            String softwareVersionHex = HexUtil.intToHexString(Integer.parseInt(softwareVersion));
            for (Pair<String, Integer> stringIntegerPair : deviceGroupingByFlagList) {
                List<SystemDevice> systemDevices = singleLampParamMap.get(stringIntegerPair);
                String systemDeviceNum = stringIntegerPair.getLeft();
                String prefix = "";
                for (int i = 0; i < 15 - systemDeviceNum.length(); i++) {
                    prefix += "0";
                }
                systemDeviceNum = prefix + systemDeviceNum;
                String crc16Redis = Crc16.generateCrc16(String.valueOf(System.currentTimeMillis()) + systemDeviceNum + logId);
                String num = stringIntegerPair.getLeft();
                Integer deviceTypeId = stringIntegerPair.getRight();
                List<String> messageList = MessageOperationUtil.analysisOtaFile(realPath, num,deviceTypeId);
                if(messageList==null || messageList.size()==0 || "01010101".equals(messageList.get(0))){
                    return new Result().error("读取文件出错");
                }
                redisUtil.set(crc16Redis,messageList);
                redisUtil.expire(crc16Redis,10800);
                redisUtil.set(crc16Redis+"logId",String.valueOf(logId));
                redisUtil.expire(crc16Redis,10800);
                String otaReady = "";
                String otaVersion = "";
                if(deviceTypeId==1||deviceTypeId==2){
                    otaReady = MessageGeneration.nbOtaReady("1007",num,crc16Redis);
                    otaVersion = MessageGeneration.nbOtaReady("2018",num,softwareVersionHex);
                }else if (deviceTypeId==5||deviceTypeId==6){
                    otaReady = CatOneMessageGeneration.catOneOtaReady("1007",num,crc16Redis);
                    otaVersion = CatOneMessageGeneration.catOneOtaReady("2018",num,softwareVersionHex);
                }else if(deviceTypeId==3||deviceTypeId==4){
                    otaReady = LoraOldMessageGeneration.loraOldOtaReady("1007",crc16Redis);
                    otaVersion = LoraOldMessageGeneration.loraOldOtaReady("2018",softwareVersionHex);
                }else if(deviceTypeId==9||deviceTypeId==10){
                    otaReady = LoraNewMessageGeneration.loraNewOtaReady("1007",crc16Redis);
                    otaVersion = LoraNewMessageGeneration.loraNewOtaReady("2018",softwareVersionHex);
                }else if(deviceTypeId==7||deviceTypeId==8){
                    otaReady = DxnbMessageGeneration.dxnbOtaReady("1007",crc16Redis);
                    otaVersion = DxnbMessageGeneration.dxnbOtaReady("2018",softwareVersionHex);
                }else if (deviceTypeId==14||deviceTypeId==15){
                    otaReady = DxCatOneMessageGeneration.dxCatOneOtaReady("1007",crc16Redis);
                    otaVersion = DxCatOneMessageGeneration.dxCatOneOtaReady("2018",softwareVersionHex);
                }
            /*Result<LampDevice> lampDeviceResult = lampDeviceService.getByNum(num,model,factory);
            LampDevice lampDeviceResultData = lampDeviceResult.getData();
            String sendId = lampDeviceResultData.getSendId();*/
                String sendId = systemDevices.get(0).getReserveOne();
                MessageOperationUtil.sendByMode(otaReady,deviceTypeId,sendId);
                MessageOperationUtil.sendByMode(otaVersion,deviceTypeId,sendId);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            /*if(!realPath.isEmpty()){
                // 删除源文件
                File file0 = new File(realPath);
                if (file0.exists()) {
                    file0.delete();
                }
            }*/
        }
        return new Result().success("下发成功");
    }

    @Override
    public List<DeviceUpgradeLogStatus> getListByLogId(Integer logId) {
        QueryWrapper<DeviceUpgradeLogStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("log_id",logId);
        return baseMapper.selectList(queryWrapper);
    }

}