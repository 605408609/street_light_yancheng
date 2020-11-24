package com.exc.street.light.resource.dto.electricity;

import lombok.Data;

/**
 * 路灯网关设备数据excel导入类 顺序与excel模板对应
 *
 * @Author: Xiaok
 * @Date: 2020/11/13 10:45
 */
@Data
public class ElectricityNodeImportDataDTO {

    private String lampPostName;
    private String name;
    private String num;
    private String mac;
    private String ip;
    private Integer port;

}
