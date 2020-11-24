/**
 * @filename:LampHtMessageServiceImpl 2020-09-02
 * @project sl  V1.0
 * Copyright(c) 2018 Xiaok Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.sl.ht.HtCommandDTO;
import com.exc.street.light.resource.entity.sl.LampHtMessage;
import com.exc.street.light.resource.enums.sl.ht.HtCommandTypeEnum;
import com.exc.street.light.resource.enums.sl.ht.HtParameterEnum;
import com.exc.street.light.resource.enums.sl.ht.HtResultEnum;
import com.exc.street.light.sl.mapper.LampHtMessageMapper;
import com.exc.street.light.sl.sender.HtMessageSender;
import com.exc.street.light.sl.service.LampDeviceParameterService;
import com.exc.street.light.sl.service.LampHtMessageService;
import com.exc.street.light.sl.service.SystemDeviceParameterService;
import com.exc.street.light.sl.service.SystemDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Description: TODO(华体集中控制器消息记录服务实现)
 * @version: V1.0
 * @author: Xiaok
 */
@Slf4j
@Service
public class LampHtMessageServiceImpl extends ServiceImpl<LampHtMessageMapper, LampHtMessage> implements LampHtMessageService {
    @Autowired
    private HtMessageSender htMessageSender;
    @Autowired
    private LampDeviceParameterService lampDeviceParameterService;
    @Autowired
    private SystemDeviceService systemDeviceService;
    @Autowired
    private SystemDeviceParameterService systemDeviceParameterService;

    @Override
    public Result orderCommand(Integer locationControlId, String locationControlAddr, HtCommandDTO htCommandDto) {
        if (locationControlId == null || StringUtils.isBlank(locationControlAddr) || htCommandDto == null
                || htCommandDto.getCmdTypeEnum() == null) {
            return new Result<>().error("参数缺失");
        }
        Result result = new Result();
        result.error("消息下发失败");
        Integer orderIndex = generateOrderIndex(locationControlId, htCommandDto.getCmdTypeEnum().code());
        if (orderIndex == null) {
            log.error("华体消息发送-生成消息序号失败,locationControlId={}", locationControlId);
            return new Result<>().error("平台生成华体消息序号失败");
        }
        if (StringUtils.isBlank(locationControlAddr)) {
            log.info("华体集中器未绑定通讯地址,locationControlId = {},locationControlAddr={}", locationControlId, locationControlAddr);
            return new Result<>().error("集中器未绑定通讯地址");
        }
        JSONObject param = new JSONObject(true);
        param.put("id", locationControlAddr);
        param.put("orderindex", orderIndex);
        param.put("orderkind", htCommandDto.getCmdTypeEnum().code());
        if (htCommandDto.getDataObj() != null && !htCommandDto.getDataObj().isEmpty()) {
            for (Map.Entry<String, Object> paramObjMap : htCommandDto.getDataObj().entrySet()) {
                param.put(paramObjMap.getKey(), paramObjMap.getValue());
            }
        }
        String resultStr = htMessageSender.sendRequest(param, htCommandDto.getCmdTypeEnum());
        if (StringUtils.isBlank(resultStr)) {
            result.error("消息下发失败,请求失败");
        }
        JSONObject resultObj = JSONObject.parseObject(resultStr);
        //下发结果
        Integer orderResult = null;
        //根据返回值判断是否成功
        if (resultObj != null) {
            String rCode = resultObj.getString("rcode");
            if (StringUtils.isNotBlank(rCode)) {
                try {
                    orderResult = Integer.parseInt(rCode);
                    if (orderResult.equals(HtResultEnum.SUCCESS.code())) {
                        result.success("消息下发成功");
                    } else {
                        result.error("消息下发失败," + HtResultEnum.getMsgByCode(orderResult));
                    }
                } catch (NumberFormatException e) {
                    log.error("华体-返回值转换数字失败,errMsg={}", e.getMessage());
                    result.error("消息下发失败,异常错误");
                }
            }
        }
        //保存下发返回结果
        saveResult(orderIndex, orderResult, resultStr);
        return result;
    }

    @Override
    public Result aloneInfo(JSONObject resObj) {
        if (resObj == null) {
            return null;
        }
        //集中控制器通讯地址
        String lampPostAddr = resObj.getString("id");
        //命令类型
        Integer orderKind = resObj.getInteger("orderkind");
        if (StringUtils.isBlank(lampPostAddr) || orderKind == null || orderKind != HtCommandTypeEnum.DATA_UPLOAD_SINGLE_LAMP.code()) {
            return null;
        }
        //单灯控制器数据
        JSONArray dataArr = resObj.getJSONArray("data");
        if (dataArr == null || dataArr.isEmpty()) {
            return null;
        }
        //获取参数code和对应id
        Map<String, Integer> fieldTypeMap = systemDeviceParameterService.selectFieldByType(12);
        for (int i = 0; i < dataArr.size(); i++) {
            JSONObject dataObj = dataArr.getJSONObject(i);
            //单灯控制器索引
            Integer index = dataObj.getInteger("index");
            //通讯状态 1-在线,2-离线
            Integer state = dataObj.getInteger("state");
            //通讯状态变更时间 yyyy-MM-dd HH:mm:ss
            String time = dataObj.getString("time");
            //漏电流
            Float leakc = dataObj.getFloat("leakc");
            //电压频率
            Float vf = dataObj.getFloat("vf");

            //设备在线状态
            Integer onlineStatus = null;
            if (state != null && state == 2) {
                onlineStatus = 0;
            } else if (state != null && state == 1) {
                onlineStatus = 1;
            }

            //单灯控制器支路数据
            JSONArray loopDataArr = dataObj.getJSONArray("data");
            if (loopDataArr == null || loopDataArr.isEmpty()) {
                continue;
            }
            for (int j = 0; j < loopDataArr.size(); j++) {
                JSONObject loopDataObj = loopDataArr.getJSONObject(j);
                //输出类型 HtSingleLampBranchOutputEnum
                Integer act = loopDataObj.getInteger("act");
                //功率
                Float gl = loopDataObj.getFloat("gl");
                //功率因数
                Float gly = loopDataObj.getFloat("gly");
                //电压
                Float v = loopDataObj.getFloat("v");
                //电流
                Float c = loopDataObj.getFloat("c");

                //获取灯具设备ID
                Integer deviceId = lampDeviceParameterService.selectSystemDeviceIdByIndexAndLoopNum(lampPostAddr, HtParameterEnum.INDEX.code(),
                        index, HtParameterEnum.LOOP_NUM.code(), j + 1);
                //更新设备状态
                if (onlineStatus != null) {
                    systemDeviceService.updateDeviceOnlineStatus(deviceId, onlineStatus);
                }
                //更新输出类型
                lampDeviceParameterService.saveParamValue(deviceId, fieldTypeMap.get(HtParameterEnum.ACT.code()),
                        act != null ? act.toString() : null);
                //更新功率
                lampDeviceParameterService.saveParamValue(deviceId, fieldTypeMap.get(HtParameterEnum.POWER.code()),
                        gl != null ? gl.toString() : null);
                //更新功率因数
                lampDeviceParameterService.saveParamValue(deviceId, fieldTypeMap.get(HtParameterEnum.POWER_FACTOR.code()),
                        gly != null ? gly.toString() : null);
                //更新电压
                lampDeviceParameterService.saveParamValue(deviceId, fieldTypeMap.get(HtParameterEnum.VOLTAGE.code()),
                        v != null ? v.toString() : null);
                //更新电流
                lampDeviceParameterService.saveParamValue(deviceId, fieldTypeMap.get(HtParameterEnum.ELECTRIC_CURRENT.code()),
                        c != null ? c.toString() : null);
            }
        }
        return null;
    }

    /**
     * 生成下发唯一标识
     *
     * @param locationControlId 华体集控器ID
     * @param orderKind         命令类型
     * @return
     */
    private Integer generateOrderIndex(Integer locationControlId, Integer orderKind) {
        if (locationControlId == null || orderKind == null) {
            return null;
        }
        LampHtMessage htMsg = new LampHtMessage();
        htMsg.setLocationControlId(locationControlId);
        htMsg.setOrderCmdType(orderKind);
        htMsg.setOrderTime(new Date());
        return baseMapper.insert(htMsg);
    }

    /**
     * 保存下发的返回结果
     *
     * @param orderIndex     消息唯一标识
     * @param orderResult    消息下发返回结果
     * @param orderResultStr 下发返回的消息
     */
    private void saveResult(int orderIndex, Integer orderResult, String orderResultStr) {
        //根据消息唯一标识 保存返回结果
        LampHtMessage htMsg = this.getById(orderIndex);
        if (htMsg != null) {
            htMsg.setOrderResult(orderResult);
            htMsg.setOrderCmd(orderResultStr);
            this.save(htMsg);
        }
    }
}