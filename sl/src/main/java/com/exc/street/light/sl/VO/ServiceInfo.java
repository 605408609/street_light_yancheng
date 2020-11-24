package com.exc.street.light.sl.VO;

import lombok.Data;

import java.util.List;

/**
 * 设备的服务信息
 */
@Data
public class ServiceInfo {

    //屏蔽的设备控制命令列表
    private List<String>  muteCmds;
}
