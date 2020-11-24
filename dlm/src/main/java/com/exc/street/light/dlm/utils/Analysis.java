package com.exc.street.light.dlm.utils;


import com.exc.street.light.dlm.service.ControlLoopSceneStatusService;
import com.exc.street.light.dlm.service.HeartbeatAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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
    private static ControlLoopSceneStatusService controlLoopSceneStatusService;

    @Autowired
    ApplicationContext applicationContext;
    @PostConstruct
    public void init() {
        Analysis.heartbeatAnalysisService = applicationContext.getBean(HeartbeatAnalysisService.class);
        Analysis.controlLoopSceneStatusService = applicationContext.getBean(ControlLoopSceneStatusService.class);
    }


    public static void receiveData(byte[] data, String clientIp) {
        //设备地址
        String address = ArrayUtil.getAddress(data);
        //获取功能码
        byte controlId = AnalysisUtil.getControlId(data);
        //获取结果码
        byte ret = AnalysisUtil.getRet(data);
        switch (controlId) {
            //心跳包解析
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_5):
                heartbeatAnalysisService.analyze(data, clientIp);
                //写入16个场景
                controlLoopSceneStatusService.saveLoopScene(address);
                break;
            default:
                break;
        }
    }
}
