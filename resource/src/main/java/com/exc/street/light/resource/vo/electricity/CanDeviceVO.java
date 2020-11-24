package com.exc.street.light.resource.vo.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 强电设备视图
 *
 * @author Linshiwen
 * @date 2018/5/22
 */
@Setter
@Getter
@ToString
public class CanDeviceVO {
    private Integer id;


    /**
     * canId
     */
    private String canId;


    /**
     * 模块名称
     */
    private String name;

    /**
     * 设备物理地址
     */
    private String address;

    /**
     * 模块类型(1:场景模块 2:驱动模块 3:输入模块...)
     */
    private String moduleTypeName;

    private Integer mid;
}