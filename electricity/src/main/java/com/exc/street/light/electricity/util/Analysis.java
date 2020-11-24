package com.exc.street.light.electricity.util;


import com.exc.street.light.electricity.service.CanAlarmDataService;
import com.exc.street.light.electricity.service.CanChangeDataService;
import com.exc.street.light.electricity.service.CanChannelSceneStatusService;
import com.exc.street.light.electricity.service.HeartbeatAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;

/**
 * 解析方法
 *
 * @author LinShiWen
 * @date 2017/9/30
 */
@Slf4j
@Component
public class Analysis {

    private static HeartbeatAnalysisService heartbeatAnalysisService;
    private static CanChangeDataService canChangeDataService;
    private static CanAlarmDataService canAlarmDataService;
    private static RedisUtil redisUtil;
    private static CanChannelSceneStatusService canChannelSceneStatusService;

    @Autowired
    ApplicationContext applicationContext;
    @PostConstruct
    public void init() {
        Analysis.heartbeatAnalysisService = applicationContext.getBean(HeartbeatAnalysisService.class);
        Analysis.canChangeDataService = applicationContext.getBean(CanChangeDataService.class);
        Analysis.canAlarmDataService = applicationContext.getBean(CanAlarmDataService.class);
        Analysis.redisUtil = applicationContext.getBean(RedisUtil.class);
        Analysis.canChannelSceneStatusService = applicationContext.getBean(CanChannelSceneStatusService.class);
    }


    public static void receiveData(byte[] data, String clientIp) {
        //获取功能码
        byte controlId = AnalysisUtil.getControlId(data);
        //获取mac地址
        String mac = ArrayUtil.getAddress(data);
        //获取结果码
        byte ret = AnalysisUtil.getRet(data);
        switch (controlId) {
            //心跳包解析
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_5):
                heartbeatAnalysisService.analyze(data, clientIp);
                //下发场景
                canChannelSceneStatusService.saveAndOrderLoopScene(mac);
                break;
            //变化数据上传解析
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_21):
                Object changeData = redisUtil.hmGet(ConstantUtil.MAC_CHANGE_DATA_KEY, mac);
                if (changeData == null) {
                    redisUtil.hmSet(ConstantUtil.MAC_CHANGE_DATA_KEY, mac, data);
                    canChangeDataService.analyze(data);
                }
                if (changeData != null) {
                    boolean b = Arrays.equals(data, (byte[]) changeData);
                    if (!b) {
                        log.info("网关:{},变化数据不一样进行解析", mac);
                        redisUtil.hmSet(ConstantUtil.MAC_CHANGE_DATA_KEY, mac, data);
                        canChangeDataService.analyze(data);
                    } else {
                        log.info("网关:{},变化数据一样不解析", mac);
                    }
                }
                break;
            //告警数据上传解析
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_22):
                canAlarmDataService.analyze(data);
                break;
            //设置网关时间
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_3):
                if (ret == ConstantUtil.RET_1) {
                    //成功
                }
                break;
            //获取网关时间
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_4):
                //网关时间
                Date time = AnalysisUtil.getTime(data);
                break;
            //设置场景
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_6):
                if (ret == ConstantUtil.RET_1) {
                    //成功
                }
                break;
            //设置定时
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_8):
                if (ret == ConstantUtil.RET_1) {
                    //成功
                }
                break;
            //设置网关经纬度
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_19):
                if (ret == ConstantUtil.RET_1) {
                    //成功
                }
                break;
            //获取经纬度和日出日落时间
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_20):
                double[] lngAndLat = AnalysisUtil.getLngAndLat(data);
                break;
            //单控指令
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_45):
                if (ret == ConstantUtil.RET_1) {
                    //成功
                }
                break;
            //组控指令
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_46):
                if (ret == ConstantUtil.RET_1) {
                    //成功
                }
                break;
            default:
                break;
        }
    }
}
