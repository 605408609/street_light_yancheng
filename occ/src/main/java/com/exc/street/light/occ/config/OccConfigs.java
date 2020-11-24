package com.exc.street.light.occ.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取配置文件中报警设备的默认参数
 * @author huangmin
 * @date 2020/03/26
 */
@Getter
@Setter
@ToString

@Component
@ConfigurationProperties(prefix="occ-configs")
public class OccConfigs {
    /**
     * 定时服务的定时检测已添加的设备进行注册, cron参数值,示例:0 10/30 0/1 * * ? ,含义:从第一分钟开始,每30分钟执行一次
     */
    private String scheduleCheckRegister;
    /**
     * 定时服务的定时检测已注册的设备是否在线, cron参数值,示例:0 1/3 0/1 * * ? ,含义:从第一分钟开始,每3分钟执行一次
     */
    private String scheduleCheckOnline;
    /**
     * 调用接口请求的类型,http或https
     */
    private String httpType;
    /**
     * 调用接口请求的ip,当前服务器的ip
     */
    private String httpIp;
    /**
     * 调用接口请求的端口,当前occ服务的端口
     */
    private Integer httpPort;
    /**
     * 获取灯杆ID的接口地址
     */
    private String controllerRequestGetLampId;
    /**
     * 新增告警信息的接口地址
     */
    private String controllerRequestAddAhPlay;

    /**
     * 向kafka发送数据包的接口地址
     */
    private String controllerRequestSendMsgToKafka;
    /**
     * 修改设备注册状态为成功的接口地址
     */
    private String controllerRequestUpdateStateById;
    
}
