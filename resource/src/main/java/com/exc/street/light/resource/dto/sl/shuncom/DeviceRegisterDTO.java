package com.exc.street.light.resource.dto.sl.shuncom;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 新设备注册DTO
 *
 * @Author: Xiaok
 * @Date: 2020/8/24 9:51
 */
@Getter
@Setter
@ToString
public class DeviceRegisterDTO {


    /**
     * 设备 id
     */
    private String id;

    /**
     * 设备端口
     */
    private Integer ep;

    /**
     * 协议
     */
    private Integer pid;

    /**
     * 设备类型 id
     */
    private Integer did;

    /**
     * 在线状态 true:在线,false:离线
     */
    private Boolean ol;

    /**
     * 设备类型
     */
    @JSONField(name = "dtype")
    private Integer dType;

    /**
     * 序列码，每个包唯一
     */
    private Long serial;
}
