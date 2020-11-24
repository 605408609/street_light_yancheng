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
@ConfigurationProperties(prefix="occ-configs.alarm-config")
public class AlarmConfig {
    /**
     * 设备用户名
     */
    private String deviceUsername;
    /**
     * 设备用户密码
     */
    private String devicePwd;
    /**
     * 设备端口号
     */
    private Integer devicePort;
    /**
     * 当前服务器ip地址
     */
    private String listenIp;
    /**
     * 当前服务器监听端口号
     */
    private Integer listenPort;
    
    
    
}
